package may.yuntian.jianping.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.R;
import may.yuntian.jianping.entity.WarningBasicEntity;
import may.yuntian.jianping.service.WarningBasicService;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 *职业危害警示标识设置基本信息
 * WEB请求处理层
 *
 * @author LiXin
 * @data 2021-03-18
 */
@RestController
@Api(tags="职业危害警示标识设置基本信息")
@RequestMapping("/warningBasic")
public class WarningBasicController {
    @Autowired
    private WarningBasicService warningBasicService;

    
	/**
	 *列表
	 */
    @GetMapping("/listByName")
    @ApiOperation("根据条件查询职业危害警示标识设置列表")
//    @RequiresPermissions("anlian:detection:list")
    public Result listByName(@RequestParam Map<String, Object> params){
       List<WarningBasicEntity> warningBasicList = warningBasicService.list();

        return Result.ok().put("warningBasicList", warningBasicList);
    }
    

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("根据ID显示职业危害警示标识设置基本信息详情")
//    @RequiresPermissions("anlian:protectionEvaluation:evInfo")
    public Result info(@PathVariable("id") Long id){
    	WarningBasicEntity warningBasic = warningBasicService.getById(id);

        return Result.ok().put("warningBasic", warningBasic);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增职业危害警示标识设置基本信息")
    @ApiOperation("新增职业危害警示标识设置基本信息")
//    @RequiresPermissions("anlian:protectionEvaluation:save")
    public Result save(@RequestBody WarningBasicEntity warningBasic){
    	warningBasicService.save(warningBasic);

        return Result.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改职业危害警示标识设置基本信息")
    @ApiOperation("修改职业危害警示标识设置基本信息")
//    @RequiresPermissions("anlian:protectionEvaluation:update")
    public Result update(@RequestBody WarningBasicEntity warningBasic){
    	warningBasicService.updateById(warningBasic);
        
        return Result.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除职业危害警示标识设置基本信息")
    @ApiOperation("删除职业危害警示标识设置基本信息")
//    @RequiresPermissions("anlian:protectionEvaluation:delete")
    public Result delete(@RequestBody Long[] ids){
    	warningBasicService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
