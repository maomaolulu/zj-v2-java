package may.yuntian.laboratory.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.R;
import may.yuntian.laboratory.entity.SampleBasisEquipEntity;
import may.yuntian.laboratory.service.SampleBasisEquipService;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *检测依据及设备管理
 * @author LiXin
 * @date 2020-11-28
 */

@RestController
@Api(tags="检测依据及设备管理")
@RequestMapping("/sampleBasisEquip")
public class SampleBasisEquipController {
	@Autowired
	private SampleBasisEquipService sampleBasisEquipService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("根据条件分页查询检测依据及设备列表")
//    @RequiresPermissions("anlian:sampleBasisEquip:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = sampleBasisEquipService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 根据项目ID获取检测依据及设备列表
     */
    @GetMapping("/listByProjectId/{projectId}")
    @ApiOperation("根据项目ID获取检测依据及设备列表")
//    @RequiresPermissions("anlian:detection:list")
    public Result listByProjectId(@PathVariable("projectId") Long projectId){
        List<SampleBasisEquipEntity> list = sampleBasisEquipService.getListByProjectId(projectId);

        return Result.data(list);
    }

    /**
     * 生成检测依据及设备
     */
    @PostMapping("/generateSampleBasis")
    @ApiOperation("生成检测依据及设备")
//    @RequiresPermissions("anlian:detection:save")
    public Result generateSampleBasis(@RequestBody Map<String,Long> params){

        System.out.println(params.get("projectId"));
        Long projectId = params.get("projectId");
        System.out.println(projectId);
        sampleBasisEquipService.generateSampleBasis(Long.valueOf(params.get("projectId")));
        return Result.ok();
    }

    /**
     * 修改检测依据及设备并回填
     */
    @PostMapping("/updateBatchEquip")
    @SysLog("修改检测依据及设备并回填")
    @ApiOperation("修改检测依据及设备并回填")
//    @RequiresPermissions("anlian:detection:save")
    public Result updateBatchEquip(@RequestBody List<SampleBasisEquipEntity> list){
        sampleBasisEquipService.updateBatchEquip(list);
        return Result.ok();
    }

}
