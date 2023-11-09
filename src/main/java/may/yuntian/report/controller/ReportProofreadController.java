package may.yuntian.report.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.R;
import may.yuntian.minio.utils.MinioUtil;
import may.yuntian.report.entity.ReportProofreadEntity;
import may.yuntian.report.service.ReportProofreadService;
import may.yuntian.sys.utils.Result;
import may.yuntian.untils.AlRedisUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 报告技术校核记录表管理
 * WEB请求处理层
 *
 * @author LinXin
 * @date 2022-04-14
 */
@RestController
@Api(tags="报告技术校核记录表")
@RequestMapping("/proofread")
public class ReportProofreadController {
    @Autowired
    private ReportProofreadService reportProofreadService;
    @Autowired
    private AlRedisUntil alRedisUntil;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("根据条件分页查询报告技术校核记录表列表")
//    @RequiresPermissions("report:proofread:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = reportProofreadService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("根据ID显示报告技术校核记录表详情")
//    @RequiresPermissions("report:proofread:evInfo")
    public Result info(@PathVariable("id") Long id){
        ReportProofreadEntity reportProofread = reportProofreadService.getById(id);

        return Result.ok().put("reportProofread", reportProofread);
    }
    
    /**
     * 根据项目ID获取报告技术校核信息
     */
    @GetMapping("/infoByProjectId/{projectId}")
    @ApiOperation("根据项目ID显示报告技术校核记录详情")
//    @RequiresPermissions("report:proofread:evInfo")
    public Result infoByProjectId(@PathVariable("projectId") Long projectId){
    	ReportProofreadEntity reportProofread = reportProofreadService.getByProjectId(projectId);
    	
    	return Result.ok().put("reportProofread", reportProofread);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增报告技术校核记录表")
    @ApiOperation("新增报告技术校核记录表")
//    @RequiresPermissions("report:proofread:save")
    public Result save(@RequestBody ReportProofreadEntity reportProofread){
        reportProofreadService.save(reportProofread);

        return Result.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改报告技术校核记录表")
    @ApiOperation("修改报告技术校核记录表")
//    @RequiresPermissions("report:proofread:update")
    public Result update(@RequestBody ReportProofreadEntity reportProofread){
        ReportProofreadEntity oldProofread = reportProofreadService.getById(reportProofread.getId());
        if (null != oldProofread.getPath() && !oldProofread.getPath().equals(reportProofread.getPath())){
            MinioUtil.remove(oldProofread.getPath());
        }
        if (null != reportProofread.getPath() && !oldProofread.getPath().equals(reportProofread.getPath())){
            Object o = alRedisUntil.hget("anlian-java",reportProofread.getPath());
            if (null!=o){
                alRedisUntil.hdel("anlian-java",reportProofread.getPath());
            }
        }
    	reportProofreadService.saveOrUpdate(reportProofread);
        //reportProofreadService.updateById(reportProofread);
        
        return Result.ok();
    }

    /**
     * 新版修改-有上传文件
     */
    @PostMapping("/newUpdate")
    @SysLog("修改报告技术校核记录表-有上传文件")
    @ApiOperation("修改报告技术校核记录表-有上传文件")
//    @RequiresPermissions("report:proofread:update")
    public Result newUpdate(@RequestBody ReportProofreadEntity reportProofread){
        ReportProofreadEntity oldProofread = reportProofreadService.getById(reportProofread.getId());
        if (null != oldProofread.getPath() && !oldProofread.getPath().equals(reportProofread.getPath())){
            MinioUtil.remove(oldProofread.getPath());
        }
        if (null != reportProofread.getPath() && !oldProofread.getPath().equals(reportProofread.getPath())){
            Object o = alRedisUntil.hget("anlian-java",reportProofread.getPath());
            if (null!=o){
                alRedisUntil.hdel("anlian-java",reportProofread.getPath());
            }
        }
        reportProofreadService.updateInfo(reportProofread);
        //reportProofreadService.updateById(reportProofread);

        return Result.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除报告技术校核记录表")
    @ApiOperation("删除报告技术校核记录表")
//    @RequiresPermissions("report:proofread:delete")
    public Result delete(@RequestBody Long[] ids){

        List<ReportProofreadEntity> list = reportProofreadService.listByIds(Arrays.asList(ids));
        for (ReportProofreadEntity reportProofread:list){
            MinioUtil.remove(reportProofread.getPath());
        }

        reportProofreadService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
