package may.yuntian.jianping.controller;

import java.util.List;
import java.util.Arrays;

import may.yuntian.jianping.entity.EquipmentMeasureEntity;
import may.yuntian.jianping.service.EquipmentMeasureService;
import may.yuntian.minio.utils.MinioUtil;
import may.yuntian.sys.utils.Result;
import may.yuntian.untils.AlRedisUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import may.yuntian.common.annotation.SysLog;



/**
 * 设备布局测点布置图调查管理
 * WEB请求处理层
 *
 * @author LiXin
 * @email ''
 * @date 2022-09-18 18:50:58
 */
@RestController
@Api(tags="设备布局测点布置图调查")
@RequestMapping("/equipmentMeasure")
public class EquipmentMeasureController {
    @Autowired
    private EquipmentMeasureService equipmentMeasureService;
    @Autowired
    private AlRedisUntil alRedisUntil;



    /**
     * 列表
     */
    @GetMapping("/list/{projectId}")
    @ApiOperation("根据条件分页查询设备布局测点布置图调查列表")
    public Result list(@PathVariable("projectId") Long projectId){
        List<EquipmentMeasureEntity> list = equipmentMeasureService.getListByProjectId(projectId);

        return Result.data(list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("根据ID显示设备布局测点布置图调查详情")
    public Result info(@PathVariable("id") Long id){
        EquipmentMeasureEntity zjEquipmentMeasure = equipmentMeasureService.getById(id);

        return Result.ok().put("zjEquipmentMeasure", zjEquipmentMeasure);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增设备布局测点布置图调查")
    @ApiOperation("新增设备布局测点布置图调查")
    public Result save(@RequestBody EquipmentMeasureEntity equipmentMeasure){
        if (null != equipmentMeasure.getPath()){
            Object o = alRedisUntil.hget("anlian-java",equipmentMeasure.getPath());
            if (null!=o){
                alRedisUntil.hdel("anlian-java",equipmentMeasure.getPath());
            }
        }
        equipmentMeasureService.save(equipmentMeasure);

        return Result.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改设备布局测点布置图调查")
    @ApiOperation("修改设备布局测点布置图调查")
    public Result update(@RequestBody EquipmentMeasureEntity equipmentMeasure){
        EquipmentMeasureEntity oldMeasureEntity = equipmentMeasureService.getById(equipmentMeasure.getId());
        if (null != oldMeasureEntity.getPath()&& !oldMeasureEntity.getPath().equals(equipmentMeasure.getPath())){
            MinioUtil.remove(oldMeasureEntity.getPath());
        }
        if (null != equipmentMeasure.getPath()&& !oldMeasureEntity.getPath().equals(equipmentMeasure.getPath())){
            Object o = alRedisUntil.hget("anlian-java",equipmentMeasure.getPath());
            if (null!=o){
                alRedisUntil.hdel("anlian-java",equipmentMeasure.getPath());
            }
        }
        equipmentMeasureService.updateById(equipmentMeasure);
        
        return Result.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除设备布局测点布置图调查") 
    @ApiOperation("删除设备布局测点布置图调查")
    public Result delete(@RequestBody Long[] ids){
        List<EquipmentMeasureEntity> equipmentMeasureEntities = equipmentMeasureService.listByIds(Arrays.asList(ids));
        for (EquipmentMeasureEntity equipmentMeasure:equipmentMeasureEntities){
            if (null != equipmentMeasure.getPath()){
                MinioUtil.remove(equipmentMeasure.getPath());
            }
        }
        equipmentMeasureService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
