package may.yuntian.report.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.R;
import may.yuntian.minio.utils.MinioUtil;
import may.yuntian.report.entity.ReportReviewEntity;
import may.yuntian.report.service.ReportReviewService;
import may.yuntian.sys.utils.Result;
import may.yuntian.untils.AlRedisUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 报告技术审核记录表管理
 * WEB请求处理层
 *
 * @author LinXin
 * @date 2022-04-14
 */
@RestController
@Api(tags="报告技术审核记录表")
@RequestMapping("/review")
public class ReportReviewController {
    @Autowired
    private ReportReviewService reportReviewService;
    @Autowired
    private AlRedisUntil alRedisUntil;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("根据条件分页查询报告技术审核记录表列表")
//    @RequiresPermissions("report:review:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = reportReviewService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 根据ID信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("根据ID显示报告技术审核记录表详情")
//    @RequiresPermissions("report:review:evInfo")
    public Result info(@PathVariable("id") Long id){
        ReportReviewEntity reportReview = reportReviewService.getById(id);

        return Result.ok().put("reportReview", reportReview);
    }
    
    /**
     * 根据项目ID获取报告技术审核信息
     */
    @GetMapping("/infoByProjectId/{projectId}")
    @ApiOperation("根据项目ID显示报告技术审核记录详情")
//    @RequiresPermissions("report:review:evInfo")
    public Result infoByProjectId(@PathVariable("projectId") Long projectId){
    	ReportReviewEntity reportReview = reportReviewService.getByProjectId(projectId);
    	
    	return Result.ok().put("reportReview", reportReview);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增报告技术审核记录表")
    @ApiOperation("新增报告技术审核记录表")
//    @RequiresPermissions("report:review:save")
    public Result save(@RequestBody ReportReviewEntity reportReview){
        reportReviewService.save(reportReview);

        return Result.ok();
    }

    /**
     * 修改-有文件上传
     */
    @PostMapping("/updateNew")
    @SysLog("修改报告技术审核记录表-有文件上传")
    @ApiOperation("修改报告技术审核记录表-有文件上传")
//    @RequiresPermissions("report:review:update")
    public Result updateNew(@RequestBody ReportReviewEntity reportReview){
        ReportReviewEntity oldReportReview = reportReviewService.getById(reportReview.getId());
        if (null != oldReportReview.getPath() && !oldReportReview.getPath().equals(reportReview.getPath())){
            MinioUtil.remove(oldReportReview.getPath());
        }
        if (null != reportReview.getPath() && !oldReportReview.getPath().equals(reportReview.getPath())){
            Object o = alRedisUntil.hget("anlian-java",reportReview.getPath());
            if (null!=o){
                alRedisUntil.hdel("anlian-java",reportReview.getPath());
            }
        }
//        reportReviewService.updateById(reportReview);
        reportReviewService.updateInfo(reportReview);

        return Result.ok();
    }


    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改报告技术审核记录表")
    @ApiOperation("修改报告技术审核记录表(selectDictList需要传回为新选中的值)")
//    @RequiresPermissions("report:review:update")
    public Result update(@RequestBody ReportReviewEntity reportReview){
        ReportReviewEntity oldReportReview = reportReviewService.getById(reportReview.getId());
        if (null != oldReportReview.getPath() && !oldReportReview.getPath().equals(reportReview.getPath())){
            MinioUtil.remove(oldReportReview.getPath());
        }
        if (null != reportReview.getPath() && !oldReportReview.getPath().equals(reportReview.getPath())){
            Object o = alRedisUntil.hget("anlian-java",reportReview.getPath());
            if (null!=o){
                alRedisUntil.hdel("anlian-java",reportReview.getPath());
            }
        }
//        reportReviewService.updateById(reportReview);
        reportReviewService.saveOrUpdate(reportReview);
        
        return Result.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除报告技术审核记录表")
    @ApiOperation("删除报告技术审核记录表")
//    @RequiresPermissions("report:review:delete")
    public Result delete(@RequestBody Long[] ids){
        List<ReportReviewEntity> list = reportReviewService.listByIds(Arrays.asList(ids));
        for (ReportReviewEntity reportReview:list){
            MinioUtil.remove(reportReview.getPath());
        }
        reportReviewService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
