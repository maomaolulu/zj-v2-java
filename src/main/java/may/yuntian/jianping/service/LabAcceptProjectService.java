package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.jianping.entity.LabAcceptProjectEntity;

import java.util.List;
import java.util.Map;

/**
 * 实验室-接受的项目(按照原始记录单批次区分)
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2023-02-17 15:37:49
 */
public interface LabAcceptProjectService extends IService<LabAcceptProjectEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取实验室-接受的项目(按照原始记录单批次区分)信息
     * @param projectId
     * @return
     */
    LabAcceptProjectEntity getOneByProjectId(Long projectId);

    /**
     * 获取实验室-接受的项目(按照原始记录单批次区分)信息
     * @param projectId
     * @return
     */
    List<LabAcceptProjectEntity> getListByProjectId(Long projectId);
    
}

