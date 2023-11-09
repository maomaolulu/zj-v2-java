package may.yuntian.jianping.controller;

import io.swagger.annotations.ApiOperation;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.jianping.entity.MaterialEntity;
import may.yuntian.jianping.service.MaterialService;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 原辅料控制层
 */
@RestController
@RequestMapping("/material")
public class MaterialController {
    @Autowired
    private MaterialService materialService;

    /**
     * 查询原辅料列表
     * @param projectId
     * @return
     */
    @GetMapping("/list/{projectId}")
    @ApiOperation("分页查询列表原辅料")
    public Result list(@PathVariable("projectId") Long projectId){
        List<MaterialEntity> list = materialService.listAll(projectId);

        return Result.resultData(list);
    }

    /**
     * 单个修改
     * @param materialEntity
     * @return
     */
    @PostMapping("/update")
    @SysLog("修改原辅料")
    @ApiOperation("修改原辅料")
    public Result update(@RequestBody MaterialEntity materialEntity){
        if (StringUtils.isBlank(materialEntity.getState())||materialEntity.getState().equals("/")){
            materialEntity.setType(2);
        }
        boolean b = materialService.updateById(materialEntity);
        if (b){
            return Result.ok();
        }else {
            return Result.error("修改失败");
        }
    }

    /**
     * 修改多个
     * @param materialEntityList
     * @return
     */
    @PostMapping("/updateAll")
    @SysLog("批量修改原辅料")
    @ApiOperation("批量修改原辅料")
    public Result updateAll(@RequestBody List<MaterialEntity> materialEntityList){
        for (MaterialEntity materialEntity:materialEntityList){
            if (StringUtils.isBlank(materialEntity.getState())||materialEntity.getState().equals("/")){
                materialEntity.setType(2);
            }
        }
        boolean b = materialService.saveOrUpdateBatch(materialEntityList);
        if (b){
            return Result.ok();
        }else {
            return Result.error("修改失败");
        }
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    @SysLog("删除原辅料")
    @ApiOperation("删除原辅料")
    public Result delete(@RequestBody Long [] ids){
        boolean b = materialService.removeByIds(Arrays.asList(ids));
        if (b){
            return Result.ok();
        }else {
            return Result.error("删除失败");
        }
    }

    /**
     * 添加单个
     * @param materialEntity
     * @return
     */
    @PostMapping("/add")
    @SysLog("添加原辅料")
    @ApiOperation("添加原辅料")
    public Result add(@RequestBody MaterialEntity materialEntity){
        if (StringUtils.isBlank(materialEntity.getState())||materialEntity.getState().equals("/")){
            materialEntity.setType(2);
        }
        boolean b = materialService.save(materialEntity);
        if (b){
            return Result.ok();
        }else {
            return Result.error("添加失败");
        }
    }

    /**
     * 添加多个辅料
     * @param materialEntityList
     * @return
     */
    @PostMapping("/addAll")
    @SysLog("批量添加原辅料")
    @ApiOperation("批量添加原辅料")
    public Result addAll(@RequestBody List<MaterialEntity> materialEntityList){
        for (MaterialEntity m:materialEntityList
             ) {
            if (StringUtils.isBlank(m.getState())||m.getState().equals("/")){
                m.setType(2);
            }
        }
        boolean b = materialService.saveBatch(materialEntityList);
        if (b){
            return Result.ok();
        }else {
            return Result.error("批量添加失败");
        }
    }
}
