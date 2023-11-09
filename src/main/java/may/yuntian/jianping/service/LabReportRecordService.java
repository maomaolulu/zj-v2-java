package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.jianping.entity.LabReportRecordEntity;

import java.util.Date;
import java.util.Map;

/**
 * 报告编制记录表
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2023-02-03 16:54:59
 */
public interface LabReportRecordService extends IService<LabReportRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取最近一次实验室报告出具日期
     * @param projectId
     * @return
     */
    Date getLabReportDate(Long projectId);
    
}

