package may.yuntian.anlian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.ProjectDateEntity;
import may.yuntian.anlian.mapper.ProjectDateMapper;
import may.yuntian.anlian.service.ProjectDateService;
import may.yuntian.anlian.utils.StringUtils;
import org.springframework.stereotype.Service;

@Service("projectDateService")
public class ProjectDateServiceImpl extends ServiceImpl<ProjectDateMapper, ProjectDateEntity> implements ProjectDateService {


    /**
     * 根据项目ID获取项目日期相关信息
     * @param projctId
     * @return
     */
    public ProjectDateEntity getOneByProjetId(Long projctId){
        ProjectDateEntity projectDateEntity = baseMapper.selectOne(new QueryWrapper<ProjectDateEntity>().eq("project_id",projctId));
        return projectDateEntity;
    }


    /**
     * 获取是否物理发送
     * @param projectId
     * @return
     */
    public boolean isPhysicalSend(Long projectId){
        ProjectDateEntity projectDateEntity =  this.getOneByProjetId(projectId);
        if (StringUtils.isNotNull(projectDateEntity.getPhysicalSendDate())){
            return true;
        }else {
            return false;
        }




    }

}
