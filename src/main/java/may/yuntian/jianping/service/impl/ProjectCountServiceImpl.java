package may.yuntian.jianping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;
import may.yuntian.jianping.entity.ProjectCountEntity;
import may.yuntian.jianping.mapper.ProjectCountMapper;
import may.yuntian.jianping.service.ProjectCountService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2022-07-25 14:15:40
 */
@Service("projectCountService")
public class ProjectCountServiceImpl extends ServiceImpl<ProjectCountMapper, ProjectCountEntity> implements ProjectCountService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjectCountEntity> page = this.page(
                new Query<ProjectCountEntity>().getPage(params),
                new QueryWrapper<ProjectCountEntity>()
        );

        return new PageUtils(page);
    }


    /**
     * 根据项目ID获取项目信息录入节点信息
     * @param projectId
     * @return
     */
    public ProjectCountEntity getOneByProjectId(Long projectId){
        ProjectCountEntity projectCountEntity = baseMapper.selectOne(new QueryWrapper<ProjectCountEntity>().eq("project_id",projectId));

        return projectCountEntity;
    }

}
