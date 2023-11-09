package may.yuntian.jianping.controller;

import io.swagger.annotations.ApiOperation;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.jianping.entity.LawsEntity;
import may.yuntian.jianping.service.LawsService;
import may.yuntian.jianping.vo.StandardsVo;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 法律法规请求层
 */
@RestController
@RequestMapping("/laws")
public class LawsController {
    @Autowired
    private LawsService lawsService;

    /**
     * 根据项目ID获取法律法规信息
     */
    @GetMapping("/listByProjectId/{projectId}")
    @ApiOperation("根据项目ID获取法律法规信息(selectValue表示已经选择的值(1选中))")
    public Result listByProjectId(@PathVariable("projectId") Long projectId){
        List<LawsEntity> laws = lawsService.getListByProjectId(projectId);

        return Result.data(laws);
    }

    @PostMapping("/addLawsProject")
    @SysLog("根据项目id保存法律法规")
    @ApiOperation("根据项目id保存法律法规")
    public Result addLawsProject(@RequestBody StandardsVo standardsVo){
        Long projectId = standardsVo.getProjectId();
        Long[] lawIds = standardsVo.getLawIds();
        lawsService.addLawsProject(projectId, lawIds);

        return Result.ok();
    }


}
