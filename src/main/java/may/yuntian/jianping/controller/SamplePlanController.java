package may.yuntian.jianping.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.jianping.dto.PlanRecordDto;
import may.yuntian.jianping.mongoentity.PlanRecordEntity;
import may.yuntian.jianping.mongoservice.SamplePlanService;
import may.yuntian.jianping.vo.PostPfnVo;
import may.yuntian.sys.utils.Result;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author gy
 * @date 2023-07-27 19:01
 */
@RestController
@Api(tags="采样计划")
@RequestMapping("/sample_plan")
public class SamplePlanController {

    @Autowired
    private SamplePlanService samplePlanService;

    /**
     * 采样方案展示
     */
    @GetMapping("/plan_lis")
    @ApiOperation("采样方案展示")
    public Result planLis(@RequestParam("project_id") Long projectId) {
        return samplePlanService.planLis(projectId);
    }

    /**
     * 设置空白样
     */
    @PostMapping("/set_kb_code")
    @ApiOperation("设置空白样")
    public Result setKbCode(@RequestBody PlanRecordDto dto){
        return samplePlanService.setKbCode(dto);
    }

    /**
     * 设置采样记录备注(批量)
     */
    @PostMapping("/set_plan_lis")
    @ApiOperation("设置采样记录备注(批量)")
    public Result setPlanLis(@RequestBody JSONObject param){
        return samplePlanService.setPlanLis(param);
    }

    /**
     * 生成采样方案
     */
    @PostMapping("/generate_plan")
    @ApiOperation("生成采样方案")
    @SysLog("生成采样方案")
    public Result generatePlan(@RequestBody JSONObject params){
//        return samplePlanService.generatePlan(params);
        return Result.error("java接口暂时不可用, 请联系刘俊使用python接口");
    }

    /**
     * 批量设置采样日期
     */
    @PostMapping("/set_sample_date")
    @ApiOperation("批量设置采样日期")
    public Result setSampleDate(@RequestBody JSONObject param){
        return samplePlanService.setSampleDate(param);
    }

    /**
     * 批量设置采样时段
     */
    @PostMapping("/set_time_frame")
    @ApiOperation("批量设置采样时段")
    public Result setTimeFrame(@RequestBody JSONObject param){
        return samplePlanService.setTimeFrame(param);
    }

    /**
     * 一键生成空白样
     */
    @PostMapping("/generate_kb_code")
    @ApiOperation("一键生成空白样")
    public Result generateKbCode(@RequestBody JSONObject param){
        return samplePlanService.generateKbCode(param);
    }

    /**
     * 修复result为空
     */
    @GetMapping("/setResult")
    @ApiOperation("修复result为空")
    public void setPointId(Long projectId){
        samplePlanService.fillResult(projectId);
    }

    /**
     * 搜寻有重复点位id的项目
     */
    @GetMapping("/findChongFu")
    @ApiOperation("搜寻有重复点位id的项目")
    public void findChongFu(){
        samplePlanService.findChongFu();
    }
}
