package may.yuntian.jianping.controller;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.anlian.vo.CommissionTimeNodeVo;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.jianping.mongoentity.PlanRecordEntity;
import may.yuntian.jianping.mongoservice.PlanRecordService;
import may.yuntian.jianping.mongoservice.PostPfnService;
import may.yuntian.jianping.mongoservice.WorkspaceService;
import may.yuntian.jianping.vo.*;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 作业情况
 * @author LiXin
 * @date 2022-08-14
 * @menu 作业情况
 */

@RestController
@Api(tags="作业情况")
@RequestMapping("/planRecord")
public class PlanRecordController {
    @Autowired
    private PlanRecordService planRecordService;

    /**
     * 分页列表
     */
    @GetMapping("/list/{projectId}")
    @ApiOperation("获取列表")
//    @RequiresPermissions("anlian:project:evInfo")
    public Result list(@PathVariable("projectId") Long projectId){
        List<MathResultVo> airMathResultList = new ArrayList<>();
        List<MathResultVo> silicaMathResultList = new ArrayList<>();

        List<MathResultVo> mathResultVoList = planRecordService.getResultList(projectId);
        for (MathResultVo mathResultVo : mathResultVoList){
            if (mathResultVo.getSubstanceName().contains("游离二氧化硅")){
                silicaMathResultList.add(mathResultVo);
            }else {
                airMathResultList.add(mathResultVo);
            }
        }

        return Result.data(airMathResultList).put("silica",silicaMathResultList);
    }

    @GetMapping("/info/{id}")
    @ApiOperation("获取列表")
//    @RequiresPermissions("anlian:project:evInfo")
    public Result info(@PathVariable("id") String id){
        PlanRecordEntity planRecordEntity = planRecordService.getOneById(id);
        return Result.data(planRecordEntity);
    }




    /**
     * 空气中有害物质批量修改检测结果
     */
    @PostMapping("/updateBatch")
    @SysLog("空气中有害物质批量修改检测结果")
    @ApiOperation("空气中有害物质批量修改检测结果")
//    @RequiresPermissions("anlian:project:save")
    public Result insertBatch(@RequestBody List<MathResultVo> mathResultVoList){
        planRecordService.updateMathResult(mathResultVoList);
        return Result.ok();
    }


    /**
     * 游离二氧化硅批量修改结果
     */
    @PostMapping("/updateSilica")
    @SysLog("游离二氧化硅批量修改结果")
    @ApiOperation("游离二氧化硅批量修改结果")
//    @RequiresPermissions("anlian:project:save")
    public Result updateSilica(@RequestBody List<MathResultVo> mathResultVoList){
        planRecordService.updateSilica(mathResultVoList);
        return Result.ok();
    }

    /**
     * 物理送样
     */
    @PostMapping("/physicalSend")
    @SysLog("物理送样")
    @ApiOperation("物理送样")
//    @RequiresPermissions("anlian:project:save")
    public Result physicalSend(@RequestBody CommissionTimeNodeVo commissionTimeNodeVo, HttpServletRequest httpRequest){
        Result result = planRecordService.physicalSend(commissionTimeNodeVo,httpRequest);
        return result;
    }

//    /**
//     * pc样品信息外层进行保存
//     */
//    @PostMapping("/page_set_out_record")
//    @SysLog("pc样品信息外层进行保存")
//    @ApiOperation("pc样品信息外层进行保存")
//    @RequiresPermissions("anlian:project:save")
//    public Result pageSetOutRecord(@RequestBody JsonArray jsonArray){
//        return planRecordService.pageSetOutRecord(jsonArray);
//    }

}
