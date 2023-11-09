package may.yuntian.jianping.controller;

import io.swagger.annotations.ApiOperation;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.R;
import may.yuntian.jianping.entity.CraftProcessEntity;
import may.yuntian.jianping.service.CraftProcessService;
import may.yuntian.minio.utils.MinioUtil;
import may.yuntian.sys.utils.Result;
import may.yuntian.untils.AlRedisUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * 工艺流程控制层
 */
@RestController
@RequestMapping("/craftProcess")
public class CraftProcessController {
    @Autowired
    private CraftProcessService craftProcessService;
    @Autowired
    private AlRedisUntil alRedisUntil;

    /**
     * 查询工艺流程列表
     * @param projectId
     * @return
     */
    @GetMapping("/craftProcessList/{projectId}")
    @ApiOperation("根据项目Id查询工艺流程列表")
    public Result craftProcessList(@PathVariable("projectId") Long projectId){
        List<CraftProcessEntity> craftProcessEntities = craftProcessService.craftList(projectId);

        return Result.data(craftProcessEntities);
    }

    /**
     * 添加
     * @param craftProcessEntity
     * @return
     */
    @PostMapping("/add")
    @SysLog("添加一条工艺流程")
    @ApiOperation("添加一条工艺流程")
    public Result addCraft(@RequestBody CraftProcessEntity craftProcessEntity){
        List<String> list = craftProcessEntity.getPathAllList();

        if (list.size()>0 && list!=null){
            String path="";
            for (String s : list) {
                    Object o = alRedisUntil.hget("anlian-java",s);
                    if (null!=o){
                        alRedisUntil.hdel("anlian-java",s);
                    }
                path=path+s+",";
            }
            craftProcessEntity.setPath(path);
        }
        boolean b = craftProcessService.save(craftProcessEntity);
        if (b){
            return Result.ok();
        }else {
            return Result.error("保存失败");
        }
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    @SysLog("删除工艺流程")
    @ApiOperation("删除工艺流程")
    public Result delete(@RequestBody Long [] ids){
        List<CraftProcessEntity> craftProcessEntityList = craftProcessService.listByIds(Arrays.asList(ids));
        for (CraftProcessEntity craftProcessEntity:craftProcessEntityList){
            if (StringUtils.isNotBlank(craftProcessEntity.getPath())){
                List<String> removes = new ArrayList<>(Arrays.asList(craftProcessEntity.getPath().split(",")));
                if (StringUtils.isNotEmpty(removes)){
                    for (String a : removes){
                        if (StringUtils.isNotBlank(a)){
                            MinioUtil.remove(a);
                        }
                    }
                }
            }
        }
        boolean b = craftProcessService.removeByIds(Arrays.asList(ids));
        if (b){
            return Result.ok();
        }else {
            return Result.error("删除失败");
        }
    }

    /**
     * 单个修改
     * @param craftProcessEntity
     * @return
     */
    @PostMapping("/update")
    @SysLog("单个修改工艺流程")
    @ApiOperation("单个修改工艺流程")
    public Result update(@RequestBody CraftProcessEntity craftProcessEntity){
        List<String> list = craftProcessEntity.getPathAllList();
        CraftProcessEntity oldCraftProcess = craftProcessService.getById(craftProcessEntity.getId());
        if (StringUtils.isNotBlank(oldCraftProcess.getPath())&&StringUtils.isNotEmpty(list)){
            List<String> removes = new ArrayList<>(Arrays.asList(oldCraftProcess.getPath().split(",")));
            System.out.println("removes = " + removes);
            if (StringUtils.isNotEmpty(removes)){

                removes.removeAll(list);
                for (String a : removes){
                    if (StringUtils.isNotBlank(a)){
                        MinioUtil.remove(a);
                    }
                }
            }
        }
        if (list.size()>0 && list!=null){
            String path="";
            for (String s : list) {
                Object o = alRedisUntil.hget("anlian-java",s);
                if (null!=o){
                    alRedisUntil.hdel("anlian-java",s);
                }
                path=path+s+",";
            }
            craftProcessEntity.setPath(path);
        }
        boolean b = craftProcessService.updateById(craftProcessEntity);
        if (b){
            return Result.ok();
        }else {
            return Result.error("修改失败");
        }
    }

}
