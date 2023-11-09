package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.jianping.entity.ReportImprove;

/**
 * 检测环境条件记录
 * 
 * @author LiXin
 * @date 2020-09-29
 */

public interface ReportImproveService extends IService<ReportImprove> {

    /**
     * 初始化报告信息完善信息
     * @param projectId
     */
    void initializeReport(Long projectId);
	
}
