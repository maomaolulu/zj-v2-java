package may.yuntian.jianping.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.jianping.entity.ProjectUserEntity;
import may.yuntian.jianping.mapper.ProjectUserMapper;
import may.yuntian.jianping.service.ProjectUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("projectUserService")
public class ProjectUserServiceImpl extends ServiceImpl<ProjectUserMapper, ProjectUserEntity> implements ProjectUserService {

    /**
     * 通过项目Id获取采样人员与组长列表
     * @param projectId
     * @return
     */
    public List<ProjectUserEntity> getListByProjectId(Long projectId){
        List<ProjectUserEntity> list = baseMapper.selectList(new QueryWrapper<ProjectUserEntity>()
                .eq("project_id",projectId)
        );
        return list;
    }

    /**
     * 获取组员及组长列表
     * @param projectId
     * @return
     */
    public String getListByTypeAndProjectId(Long projectId){
        List<ProjectUserEntity> list = baseMapper.selectList(new QueryWrapper<ProjectUserEntity>()
                .eq("project_id",projectId)
                .and(i->i.eq("types",1).or().eq("types",4))
                .orderByDesc("types")
        );
        String samplePerson;
        if (list!=null&&list.size()>0){
            List<String> nameList = list.stream().map(ProjectUserEntity::getUsername).collect(Collectors.toList());
            samplePerson = StringUtils.join(nameList,",");
        }else {
            samplePerson = "";
        }
        return samplePerson;
    }

    /**
     * 根据类型获取人员名称
     * @param type
     * @param projectId
     * @return
     */
    public String getTypeName(Integer type,Long projectId){
//        110;
        List<ProjectUserEntity> list = this.getListByType(type,projectId);
        String samplePerson;
        if (list!=null&&list.size()>0){
            List<String> nameList = list.stream().map(ProjectUserEntity::getUsername).collect(Collectors.toList());
            samplePerson = StringUtils.join(nameList,",");
        }else {
            samplePerson = "";
        }
        return samplePerson;
    }


    /**
     * 根据类型和项目ID获取人员列表
     * @param type
     * @param projectId
     * @return
     */
    public List<ProjectUserEntity> getListByType(Integer type, Long projectId){
        List<ProjectUserEntity> list = baseMapper.selectList(new QueryWrapper<ProjectUserEntity>()
            .eq("types",type)
            .eq("project_id",projectId)
        );
        return list;
    }


    public List<Long> getPlanIdListByUsername(String username){
        List<ProjectUserEntity> planUserList = baseMapper.selectList(new QueryWrapper<ProjectUserEntity>()
                .eq("username", username)
                .and(i->i.eq("types", 1).or().eq("types", 4))

        );
        List<Long> projectIdList = planUserList.stream().map(ProjectUserEntity::getProjectId).distinct().collect(Collectors.toList());//项目id

        return projectIdList;
    }

    /**
     * 获取报告签字人员-第一条
     * @param projectId
     * @return
     */
    public ProjectUserEntity getJiShu(Long projectId){
        ProjectUserEntity projectUserEntity = baseMapper.selectOne(new QueryWrapper<ProjectUserEntity>()
            .eq("types",2).last("limit 1")
        );
        return projectUserEntity;
    }


    /**
     * 通过项目Id获取采样人员与组长列表
     * @param
     * @return
     */
    public List<ProjectUserEntity> getListByProjectIdList(List<Long> projectIdList){
        List<ProjectUserEntity> list = baseMapper.selectList(new QueryWrapper<ProjectUserEntity>()
                .in("project_id",projectIdList)
        );
        return list;
    }

}
