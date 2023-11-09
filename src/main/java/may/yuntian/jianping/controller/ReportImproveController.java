package may.yuntian.jianping.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.R;
import may.yuntian.jianping.entity.ReportImprove;
import may.yuntian.jianping.service.ReportImproveService;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 报告信息完善
 * WEB请求处理层
 *
 * @author LiXin
 * @date 2020-09-29
 */

@RestController
@Api(tags = "报告信息完善")
@RequestMapping("/reportImprove")
public class ReportImproveController {
	
	@Autowired
	private ReportImproveService reportImproveService;
	

	
	/**
     * 根据项目ID获取报告信息完善列表
     */
    @GetMapping("/infoByProjectId/{projectId}")
    @ApiOperation("根据项目ID显示报告信息完善详情")
//    @RequiresPermissions("report:review:evInfo")
    public Result infoByProjectId(@PathVariable("projectId") Long projectId){
    	ReportImprove reportImprove = reportImproveService.getOne(
    			new QueryWrapper<ReportImprove>()
    			.eq("project_id", projectId)
    		);
    	if(reportImprove==null){
            reportImprove=new ReportImprove();
        }
    	return Result.ok().put("reportImprove", reportImprove);
    }
	
	
	/**
	 * 信息
	 */
	@GetMapping("/info/{id}")
	@ApiOperation("根据ID显示报告信息完善详情")
//	@RequiresPermissions("report:review:evInfo")
	public Result info(@PathVariable("id") Long id) {
		ReportImprove reportImprove = reportImproveService.getById(id);
		
		return Result.ok().put("reportImprove", reportImprove);
	}
 
    /**
     * 初始化报告信息完善信息
     */
    @GetMapping("/initializeReport/{projectId}")
    @SysLog("初始化报告信息完善数据")
    @ApiOperation("初始化报告信息完善数据")
//    @RequiresPermissions("report:review:save")
    public Result initializeReport(@PathVariable("projectId") Long projectId) {
    	reportImproveService.initializeReport(projectId);
    	return Result.ok();
    }
    
    
    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改或新增报告信息完善数据")
    @ApiOperation("修改或新增报告信息完善数据")
//    @RequiresPermissions("report:review:update")
    public Result update(@RequestBody ReportImprove reportImprove){
    	reportImproveService.saveOrUpdate(reportImprove);
        return Result.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除报告信息完善数据")
    @ApiOperation("删除报告信息完善数据")
//    @RequiresPermissions("report:review:delete")
    public R delete(@RequestBody Long[] ids){
    	reportImproveService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
}