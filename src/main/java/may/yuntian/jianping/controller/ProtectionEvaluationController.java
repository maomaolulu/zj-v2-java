package may.yuntian.jianping.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.R;
import may.yuntian.jianping.entity.ProtectionEvaluationEntity;
import may.yuntian.jianping.service.ProtectionEvaluationService;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *个人防护用品有效性评价
 * @author LiXin
 * @date 2020-12-18
 */

@RestController
@Api(tags="个人防护用品有效性评价")
@RequestMapping("/protectionEvaluation")
public class ProtectionEvaluationController {
	@Autowired
	private ProtectionEvaluationService protectionEvaluationService;
	

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("根据条件分页查询个人防护用品有效性评价列表")
//    @RequiresPermissions("anlian:protectionEvaluation:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = protectionEvaluationService.queryPage(params);

        return Result.ok().put("page", page);
    }
    
    /**
     * 根据项目ID获取检测依据及设备列表
     */
    @GetMapping("/listByProjectId")
    @ApiOperation("根据项目ID获取个人防护用品有效性评价")
//    @RequiresPermissions("anlian:protectionEvaluation:list")
    public Result listByProjectId(@RequestParam Map<String, Object> map){
    	List<ProtectionEvaluationEntity> list = protectionEvaluationService.listByProjectId(map);
    	
    	return Result.ok().put("list", list);
    }

    /**
     * 根据项目ID获取检测依据及设备列表
     */
    @GetMapping("/getListByProjectId/{projectId}")
    @ApiOperation("根据项目ID获取个人防护用品有效性评价")
//    @RequiresPermissions("anlian:protectionEvaluation:list")
    public Result listByProjectId(@PathVariable("projectId") Long projectId){
        List<ProtectionEvaluationEntity> list = protectionEvaluationService.getListByprojectId(projectId);

        return Result.ok().put("list", list);
    }


    /**
     * 初始化
     */
    @PostMapping("/initialization")
    @SysLog("初始化个人防护用品有效性评价")
    @ApiOperation("初始化个人防护用品有效性评价")
//    @RequiresPermissions("anlian:protectionEvaluation:save")
    public Result initializationProtectionEvaluation(@RequestBody ProtectionEvaluationEntity protectionEvaluation){
        Long projectId = protectionEvaluation.getProjectId();
        protectionEvaluationService.initializationProtectionEvaluation(projectId);
        return Result.ok();
    }


    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增个人防护用品有效性评价")
    @ApiOperation("新增个人防护用品有效性评价")
//    @RequiresPermissions("anlian:protectionEvaluation:save")
    public Result save(@RequestBody ProtectionEvaluationEntity protectionEvaluation){
    	protectionEvaluationService.save(protectionEvaluation);

        return Result.ok();
    }
    

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改个人防护用品有效性评价")
    @ApiOperation("修改个人防护用品有效性评价")
//    @RequiresPermissions("anlian:protectionEvaluation:update")
    public Result update(@RequestBody ProtectionEvaluationEntity protectionEvaluation){
    	protectionEvaluationService.updateById(protectionEvaluation);
        
        return Result.ok();
    }


    /**
     * 批量修改
     */
    @PostMapping("/updateBatch")
    @SysLog("批量修改个人防护用品有效性评价")
    @ApiOperation("批量修改个人防护用品有效性评价")
//    @RequiresPermissions("anlian:protectionEvaluation:update")
    public Result updateBatch(@RequestBody List<ProtectionEvaluationEntity> protectionEvaluationList){
        protectionEvaluationService.saveOrUpdateBatch(protectionEvaluationList);

        return Result.ok();
    }

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	@SysLog("删除个人防护用品有效性评价")
	@ApiOperation("删除个人防护用品有效性评价")
//	@RequiresPermissions("anlian:protectionEvaluation:delete")
	public Result delete(@RequestBody Long[] ids) {
		protectionEvaluationService.removeByIds(Arrays.asList(ids));

		return Result.ok();
	}

}
