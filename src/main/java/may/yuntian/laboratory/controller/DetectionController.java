package may.yuntian.laboratory.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.laboratory.entity.DetectionEntity;
import may.yuntian.laboratory.service.DetectionService;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 *检测情况管理
 * @author LiXin
 * @date 2020-11-28
 */

@RestController
@Api(tags="检测情况管理")
@RequestMapping("/detection")
public class DetectionController {
	@Autowired
	private DetectionService detectionService;

    
    /**
     * 根据项目ID获取检测情况列表
     */
    @GetMapping("/listByProjectId/{projectId}")
    @ApiOperation("根据项目ID获取检测情况列表")
//    @RequiresPermissions("anlian:detection:list")
    public Result listByProjectId(@PathVariable("projectId") Long projectId){
    	DetectionEntity list = detectionService.getByProjectId(projectId);
    	
    	return Result.data(list);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增检测情况")
    @ApiOperation("新增检测情况")
//    @RequiresPermissions("anlian:detection:save")
    public Result save(@RequestBody DetectionEntity detection){
    	detectionService.save(detection);

        return Result.ok();
    }
    

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改检测情况")
    @ApiOperation("修改检测情况")
//    @RequiresPermissions("anlian:detection:update")
    public Result update(@RequestBody DetectionEntity detection){
    	detectionService.saveOrUpdateByprojectId(detection);
        
        return Result.ok();
    }

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	@SysLog("删除检测情况")
	@ApiOperation("删除检测情况")
//	@RequiresPermissions("anlian:detection:delete")
	public Result delete(@RequestBody Long[] ids) {
		detectionService.removeByIds(Arrays.asList(ids));

		return Result.ok();
	}

}
