package may.yuntian.jianping.controller;

import io.swagger.annotations.ApiOperation;
import may.yuntian.jianping.dto.ReportDto;
import may.yuntian.jianping.service.ReportStatisticsService;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author gy
 * @date 2023-08-08 9:24
 */
@RestController
@RequestMapping("/reportStatistics")
public class ReportStatisticsController {

    @Autowired
    private ReportStatisticsService reportStatisticsService;

    /**
     * 各阶段项目数量统计
     */
    @GetMapping("/everyStageProject")
    @ApiOperation("各阶段项目数量统计")
    public Result everyStageProject(ReportDto dto){
        return Result.ok("查询各阶段项目数量统计成功",reportStatisticsService.everyStageProject(dto));
    }

    /**
     * 签发概况
     */
    @GetMapping("/issuanceOverview")
    @ApiOperation("签发概况")
    public Result issuanceOverview(ReportDto dto){
        return Result.ok("查询签发概况成功", reportStatisticsService.issuanceOverview(dto));
    }

    /**
     * 签发量统计
     */
    @GetMapping("/issuanceCount")
    @ApiOperation("签发量统计")
    public Result issuanceCount(ReportDto dto){
        return Result.ok("查询签发量统计成功",reportStatisticsService.issuanceCount(dto));
    }
}
