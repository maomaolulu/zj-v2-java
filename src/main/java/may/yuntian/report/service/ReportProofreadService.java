package may.yuntian.report.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.report.entity.ReportProofreadEntity;

import java.util.Map;

/**
 * 报告技术校核记录表
 * 业务逻辑层接口
 *
 * @author LinXin
 * @date 2022-04-14
 */
public interface ReportProofreadService extends IService<ReportProofreadEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * 根据项目ID获取报告技术校核记录信息
     * @param projectId
     * @return
     */
    ReportProofreadEntity getByProjectId(Long projectId);
    
    /**
     * 将项目信息赋值到报告技术中作为初始化数据
     * @param projectId
     * @return
     */
    boolean initialize(Long projectId);
    
    /**
     * 根据项目ID查询是否已经存在
     * @param projectId 项目ID
     * @return boolean
     */
    Boolean notExistByProject(Long projectId);

    /**
     * 新版修改
     * @param entity
     */
    void updateInfo(ReportProofreadEntity entity);
    
}

