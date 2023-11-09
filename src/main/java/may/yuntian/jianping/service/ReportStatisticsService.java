package may.yuntian.jianping.service;

import may.yuntian.jianping.dto.ReportDto;

import java.util.Map;

/**
 * @author gy
 * @date 2023-08-08 9:44
 */
public interface ReportStatisticsService {

    /**
     * 各阶段项目数量统计
     */
    Map<String, Object> everyStageProject(ReportDto dto);

    /**
     * 签发概况
     */
    Object issuanceOverview(ReportDto dto);

    /**
     * 签发量统计
     */
    Map<String, Object> issuanceCount(ReportDto dto);
}
