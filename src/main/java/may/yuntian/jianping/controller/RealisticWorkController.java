package may.yuntian.jianping.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.R;
import may.yuntian.jianping.entity.RealisticRecordEntity;
import may.yuntian.jianping.entity.RealisticWorkEntity;
import may.yuntian.jianping.service.RealisticRecordService;
import may.yuntian.jianping.service.RealisticWorkService;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/realisticWork")
public class RealisticWorkController {

    @Autowired
    private RealisticWorkService realisticWorkService;
    @Autowired
    private RealisticRecordService realisticRecordService;

    @GetMapping("/list/{projectId}")
    @ApiOperation("列表查询")
    public Result getList(@PathVariable("projectId") Long projectId){
        List<RealisticWorkEntity> list = realisticWorkService.getList(projectId);

        return Result.resultData(list);
    }

    @PostMapping("/add")
    @SysLog("添加工作日写实")
    @ApiOperation("添加工作日写实")
    public Result addRealisticWork(@RequestBody RealisticWorkEntity realisticWorkEntity){
        realisticWorkService.save(realisticWorkEntity);

        Long id = realisticWorkEntity.getId();
        List<RealisticRecordEntity> allRealisticRecord = realisticWorkEntity.getAllRealisticRecord();
        for (RealisticRecordEntity realisticRecordEntity : allRealisticRecord) {
            realisticRecordEntity.setRealisticId(id);
            realisticRecordService.save(realisticRecordEntity);
        }
        return Result.ok();
    }

    @PostMapping("/update")
    @SysLog("修改工作日写实")
    @ApiOperation("修改工作日写实")
    public Result update(@RequestBody RealisticWorkEntity realisticWorkEntity){
        realisticWorkService.updateById(realisticWorkEntity);

        Long realisticId = realisticWorkEntity.getId();
        List<RealisticRecordEntity> realisticReList = realisticRecordService.list(new QueryWrapper<RealisticRecordEntity>()
                .eq("realistic_id", realisticId)
        );
        List<Long> realisticIds = realisticReList.stream().map(RealisticRecordEntity::getId).collect(Collectors.toList());
        if(realisticReList != null) {
            realisticRecordService.removeByIds(realisticIds);
        }

        List<RealisticRecordEntity> realisticRecordList = realisticWorkEntity.getAllRealisticRecord();
        realisticRecordService.saveOrUpdateBatch(realisticRecordList);

        return Result.ok();
    }

    @PostMapping("/delete")
    @SysLog("删除工作日写实")
    @ApiOperation("删除工作日写实")
    public Result delete(Long [] ids){
        for (Long id : ids) {
            realisticRecordService.deleteRealisticId(id);
        }
        realisticWorkService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }
}
