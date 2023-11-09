package may.yuntian.jianping.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.R;
import may.yuntian.jianping.entity.WarningSignsEntity;
import may.yuntian.jianping.service.WarningSignsService;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 职业危害警示标识设置一览表
 * 
 * @author LiXin
 * @data 2021-03-17
 */

@RestController
@Api(tags="职业危害警示标识设置一览表")
@RequestMapping("/warningSigns")
public class WarningSignsController {
	@Autowired
	private WarningSignsService warningSignsService;
	
	/**
	 *列表
	 */
    @GetMapping("/list")
    @ApiOperation("根据条件分页查询职业危害警示标识设置列表")
//    @RequiresPermissions("anlian:protectionEvaluation:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = warningSignsService.queryPage(params);

        return Result.ok().put("page", page);
    }
    
	/**
	 * 根据项目ID显示技术人员排单记录详情
	 */
	@GetMapping("/infoByProjectId/{projectId}")
	@ApiOperation("根据项目ID显示职业危害警示标识设置详情")
//	@RequiresPermissions("anlian:detection:list")
	public Result infoByProjectId(@PathVariable("projectId") Long projectId) {
		List<WarningSignsEntity> warningSignsList = warningSignsService.seleteByProjectId(projectId);

		return Result.ok().put("warningSignsList", warningSignsList);
	}
	
	/**
	 * 信息
	 */
	@GetMapping("/info/{id}")
	@ApiOperation("根据ID显示职业危害警示标识设置详情")
//	@RequiresPermissions("anlian:protectionEvaluation:evInfo")
	public Result info(@PathVariable("id") Long id) {
		WarningSignsEntity warningSigns = warningSignsService.getById(id);

		return Result.ok().put("warningSigns", warningSigns);
	}
	
	
    /**
     * 初始化生成职业危害警示标识设置记录
     */
    @PostMapping("/initializeWarningSigns")
    @SysLog("初始化生成职业危害警示标识设置记录")
    @ApiOperation("初始化生成职业危害警示标识设置记录")
//    @RequiresPermissions("anlian:protectionEvaluation:save")
    public Result initializeWarningSigns(@RequestBody WarningSignsEntity warningSigns){
    	warningSignsService.initializeWarningSigns(warningSigns);

        return Result.ok();
    }
	
	

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增职业危害警示标识设置记录")
    @ApiOperation("新增职业危害警示标识设置记录")
//    @RequiresPermissions("anlian:protectionEvaluation:save")
    public Result save(@RequestBody WarningSignsEntity warningSigns){
    	warningSignsService.save(warningSigns);

        return Result.ok();
    }

    /**
     * 批量修改
     */
    @PostMapping("/updateBatch")
    @SysLog("修改职业危害警示标识设置记录")
    @ApiOperation("修改职业危害警示标识设置记录")
//    @RequiresPermissions("anlian:protectionEvaluation:update")
    public Result updateBatch(@RequestBody List<WarningSignsEntity> electromagneticList){
        warningSignsService.updateBatchById(electromagneticList);

        return Result.ok();
    }


    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除职业危害警示标识设置记录")
    @ApiOperation("删除职业危害警示标识设置记录")
//    @RequiresPermissions("anlian:protectionEvaluation:delete")
    public Result delete(@RequestBody Long[] ids){
    	warningSignsService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
