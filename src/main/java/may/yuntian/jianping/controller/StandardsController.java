package may.yuntian.jianping.controller;

import io.swagger.annotations.ApiOperation;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.jianping.entity.StandardsEntity;
import may.yuntian.jianping.service.StandardsService;
import may.yuntian.jianping.vo.StandardsVo;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 引用标准
 */
@RestController
@RequestMapping("/standards")
public class StandardsController {
    @Autowired
    private StandardsService standardsService;

    /**
     * 根据项目id获取法律列表
     * @param projectId
     * @return
     */
    @GetMapping("/listByProjectId/{projectId}")
    @ApiOperation("根据项目id查询出已选择未选择")
    public Result listByProjectId(@PathVariable("projectId") Long projectId){
        Map<String, Object> standardsAll = standardsService.getStandardsAll(projectId);

        return Result.data(standardsAll);
    }

    /**
     * 保存
     * @param standardsVo
     * @return
     */
    @PostMapping("/update")
    @SysLog("保存采样依据及其关系")
    @ApiOperation("保存采样依据及其关系")
    public Result update(@RequestBody StandardsVo standardsVo){
        Long[] ids = standardsVo.getIds();
        Long projectId = standardsVo.getProjectId();

        standardsService.updateStandardsProject(projectId, ids);
        return Result.ok();
    }



}
