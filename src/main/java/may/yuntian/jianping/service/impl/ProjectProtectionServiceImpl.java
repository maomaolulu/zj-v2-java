package may.yuntian.jianping.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.jianping.entity.ProjectProtectionEntity;
import may.yuntian.jianping.mapper.ProjectProtectionMapper;
import may.yuntian.jianping.service.ProjectProtectionService;
import may.yuntian.jianping.vo.ProtectionVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 项目防护用品信息
 * 业务逻辑层实现类
 *
 * @author zhanghao
 * @date 2022-03-10
 */
@Service("projectProtectionService")
public class ProjectProtectionServiceImpl extends ServiceImpl<ProjectProtectionMapper, ProjectProtectionEntity> implements ProjectProtectionService {


    @Override
    public List<ProtectionVo> getProtectionList(Long projectId) {
        QueryWrapper<ProtectionVo> protectionVoQueryWrapper = new QueryWrapper<>();
        protectionVoQueryWrapper.eq("project_id",projectId);
        return baseMapper.getProtectionList(protectionVoQueryWrapper);
    }


    /**
     * 批量保存对因对应关系
     * @param protectionVos
     */
    public void saveProjectProtection(List<ProtectionVo> protectionVos){
        Long projectId = protectionVos.get(0).getProjectId();
        baseMapper.delete(new QueryWrapper<ProjectProtectionEntity>().eq("project_id",projectId));
        List<ProjectProtectionEntity> projectProtectionEntities = new ArrayList<>();
        for (ProtectionVo protectionVo:protectionVos){
            ProjectProtectionEntity projectProtectionEntity = new ProjectProtectionEntity();
            projectProtectionEntity.setProjectId(protectionVo.getProjectId());
            projectProtectionEntity.setProtectionId(protectionVo.getProtectionId());
            projectProtectionEntities.add(projectProtectionEntity);
        }
        this.saveBatch(projectProtectionEntities);
    }


}
