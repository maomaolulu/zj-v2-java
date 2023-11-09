package may.yuntian.report.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.report.entity.ReportReviewEntity;

import java.util.Map;

/**
 * 报告技术审核记录表
 * 业务逻辑层接口
 *
 * @author LinXin
 * @date 2022-04-14
 */
public interface ReportReviewService extends IService<ReportReviewEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * 根据项目ID获取报告技术审核信息
     * @param projectId
     * @return
     */
    ReportReviewEntity getByProjectId(Long projectId);
    
    /**
     * 将项目信息赋值到报告技术中作为初始化数据
     * @param projectId
     * @return
     */
    boolean initialize(Long projectId);

    /**
     * 新版修改
     * @param entity
     */
    void updateInfo(ReportReviewEntity entity);
    
    /**
     * 根据项目ID查询是否已经存在
     * @param projectId 项目ID
     * @return boolean
     */
    Boolean notExistByProject(Long projectId);
}

