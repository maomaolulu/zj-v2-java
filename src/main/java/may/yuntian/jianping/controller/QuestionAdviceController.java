package may.yuntian.jianping.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.R;
import may.yuntian.jianping.entity.QuestionAdviceEntity;
import may.yuntian.jianping.entity.QuestionAdviceProjectEntity;
import may.yuntian.jianping.service.QuestionAdviceProjectService;
import may.yuntian.jianping.service.QuestionAdviceService;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *  存在的问题及建议管理 WEB请求处理层
 *
 * @author LiXin
 * @date 2020-12-30
 */

@RestController
@Api(tags = "存在的问题及建议")
@RequestMapping("/questionAdvice")
public class QuestionAdviceController {
	@Autowired
	private QuestionAdviceService questionAdviceService;
	@Autowired
	private QuestionAdviceProjectService questionAdviceProjectService;

	/**
	 * 列表
	 */
	@GetMapping("/list")
	@ApiOperation("根据条件分页查询存在的问题及建议列表")
//	@RequiresPermissions("report:review:list")
	public Result list(@RequestParam Map<String, Object> params) {
        List<QuestionAdviceEntity> page = questionAdviceService.queryPage(params);

		return Result.resultData(page);
	}

	/**
	 * 显示全部列表
	 * 
	 * @return
	 */
	@GetMapping("/listAll")
	@ApiOperation("显示全部存在的问题及建议列表")
	public Result listAll() {
		List<QuestionAdviceEntity> list = questionAdviceService.listAll();

		return Result.ok().put("list", list);
	}

	/**
     * 根据项目ID获取存在的问题及建议
     */
    @GetMapping("/listByProjectId/{projectId}")
    @ApiOperation("根据项目ID获取存在的问题及建议信息")
//    @RequiresPermissions("report:review:evInfo")
    public Result listByProjectId(@PathVariable("projectId") Long projectId){
    	List<QuestionAdviceEntity> questionAdvice = questionAdviceService.listAll();
    	List<QuestionAdviceProjectEntity> questionAdviceProjectList = questionAdviceProjectService.list(new QueryWrapper<QuestionAdviceProjectEntity>()
    			.eq("project_id", projectId)
    			);
    	
    	return Result.ok().put("questionAdvice", questionAdvice).put("questionAdviceProjectList", questionAdviceProjectList);
    }
    
    
	/**
	 * 保存或修改
	 */
	@PostMapping("/saveBatch")
	@SysLog("新增存在的问题及建议对应关系")
	@ApiOperation("新增存在的问题及建议对应关系")
//	@RequiresPermissions("report:review:save")
	public R saveBatch(@RequestBody List<QuestionAdviceProjectEntity> questionAdviceProjectList) {
		questionAdviceProjectList.forEach(a->{
			Long projectId = a.getProjectId();
			questionAdviceProjectService.remove(new QueryWrapper<QuestionAdviceProjectEntity>()
					.eq("project_id", projectId)
					);
		});
		
		questionAdviceProjectService.saveBatch(questionAdviceProjectList);

		return R.ok();
	}
    

    
	/**
	 * 信息
	 */
	@GetMapping("/info/{id}")
	@ApiOperation("根据ID显示存在的问题及建议")
//	@RequiresPermissions("report:review:evInfo")
	public R info(@PathVariable("id") Long id) {
		QuestionAdviceEntity questionAdvice = questionAdviceService.getById(id);

		return R.ok().put("questionAdvice", questionAdvice);
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	@SysLog("新增存在的问题及建议")
	@ApiOperation("新增存在的问题及建议")
//	@RequiresPermissions("report:review:save")
	public R save(@RequestBody QuestionAdviceEntity questionAdvice) {
		questionAdviceService.save(questionAdvice);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	@SysLog("修改存在的问题及建议")
	@ApiOperation("修改存在的问题及建议")
//	@RequiresPermissions("report:review:update")
	public R update(@RequestBody QuestionAdviceEntity questionAdvice) {
		questionAdviceService.updateById(questionAdvice);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	@SysLog("删除存在的问题及建议")
	@ApiOperation("删除存在的问题及建议")
//	@RequiresPermissions("report:review:delete")
	public R delete(@RequestBody Long[] ids) {
		questionAdviceService.removeByIds(Arrays.asList(ids));

		return R.ok();
	}

}
