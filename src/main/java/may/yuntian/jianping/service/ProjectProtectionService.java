package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.jianping.entity.ProjectProtectionEntity;
import may.yuntian.jianping.vo.ProtectionVo;

import java.util.List;

/**
 * 项目防护用品信息
 * 业务逻辑层接口
 *
 * @author zhanghao
 * @date 2022-03-10
 */
public interface ProjectProtectionService extends IService<ProjectProtectionEntity> {

    List<ProtectionVo> getProtectionList(Long projectId);

    /**
     * 批量保存对因对应关系
     * @param protectionVos
     */
    void saveProjectProtection(List<ProtectionVo> protectionVos);




}