package may.yuntian.jianping.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.R;
import may.yuntian.jianping.entity.EquipmentLayoutEntity;
import may.yuntian.jianping.service.EquipmentLayoutService;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 设备布局控制层
 */
@RestController
@RequestMapping("/equipmentLayout")
public class EquipmentLayoutController {
    @Autowired
    private EquipmentLayoutService equipmentLayoutService;

    /**
     * 查询设备布局列表
     * @param projectId
     * @return
     */
    @GetMapping("/getList/{projectId}")
    @ApiOperation("根据项目Id获取到设备布局列表")
    public Result getList(@PathVariable("projectId") Long projectId){
        List<EquipmentLayoutEntity> list = equipmentLayoutService.getList(projectId);

        return Result.resultData(list);
    }


    /**
     * 初始化设备布局
     * @param layout
     * @return
     */
    @PostMapping("/init")
    @SysLog("初始化设备布局")
    @ApiOperation("初始化设备布局")
    public Result init(@RequestBody EquipmentLayoutEntity layout){
        equipmentLayoutService.init(layout);
        return Result.ok();
    }


    /**
     * 添加设备布局
     * @param entity
     * @return
     */
    @PostMapping("/add")
    @SysLog("新增设备布局")
    @ApiOperation("新增设备布局")
    public Result add(@RequestBody EquipmentLayoutEntity entity){
        boolean b = equipmentLayoutService.save(entity);

        if (b){
            return Result.ok();
        }else {
            return Result.error("保存失败");
        }
    }

    /**
     * 批量保存
     * @param list
     * @return
     */
    @PostMapping("/addAll")
    @SysLog("批量保存设备布局")
    @ApiOperation("批量保存设备布局")
    public Result addAll(@RequestBody List<EquipmentLayoutEntity> list){
        boolean b = equipmentLayoutService.saveBatch(list);
        if (b){
            return Result.ok();
        }else {
            return Result.error("批量保存失败");
        }
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    @SysLog("删除设备布局")
    @ApiOperation("删除设备布局")
    public Result delete(@RequestBody Long [] ids){
        boolean b = equipmentLayoutService.removeByIds(Arrays.asList(ids));

        if (b){
            return Result.ok();
        }else {
            return Result.error("删除失败");
        }
    }

    /**
     * 单个修改
     * @param entity
     * @return
     */
    @PostMapping("/update")
    @SysLog("修改设备布局")
    @ApiOperation("修改设备布局")
    public Result update(@RequestBody EquipmentLayoutEntity entity){
        boolean b = equipmentLayoutService.updateById(entity);

        if (b){
            return Result.ok();
        }else {
            return Result.error("修改失败");
        }
    }

    /**
     * 批量修改
     * @param list
     * @return
     */
    @PostMapping("/updateAll")
    @SysLog("批量修改设备布局")
    @ApiOperation("批量修改设备布局")
    public Result updateAll(@RequestBody List<EquipmentLayoutEntity> list){
        boolean b = equipmentLayoutService.saveOrUpdateBatch(list);

        if (b){
            return Result.ok();
        }else {
            return Result.error("批量修改失败");
        }
    }
}
