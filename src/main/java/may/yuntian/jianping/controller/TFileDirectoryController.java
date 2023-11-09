package may.yuntian.jianping.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.R;
import may.yuntian.jianping.entity.TFileDirectoryEntity;
import may.yuntian.jianping.service.TFileDirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 文件目录表管理
 * WEB请求处理层
 *
 * @author LiXin
 * @email ''
 * @date 2022-04-14 20:45:49
 */
@RestController
@Api(tags="文件目录表")
@RequestMapping("jianping/tfiledirectory")
public class TFileDirectoryController {
    @Autowired
    private TFileDirectoryService tFileDirectoryService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("根据条件分页查询文件目录表列表")
//    @RequiresPermissions("jianping:tfiledirectory:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = tFileDirectoryService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 根据项目ID获取列表
     */
    @GetMapping("/getList/{projectId}")
    @ApiOperation("根据项目ID获取列表")
    public R getList(@PathVariable("projectId") Long projectId){
        List<TFileDirectoryEntity> list = tFileDirectoryService.getListByProjectId(projectId);

        return R.ok().put("list", list);
    }



    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("根据ID显示文件目录表详情")
//    @RequiresPermissions("jianping:tfiledirectory:info")
    public R info(@PathVariable("id") Long id){
        TFileDirectoryEntity tFileDirectory = tFileDirectoryService.getById(id);

        return R.ok().put("tFileDirectory", tFileDirectory);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增文件目录表")
    @ApiOperation("新增文件目录表")
//    @RequiresPermissions("jianping:tfiledirectory:save")
    public R save(@RequestBody TFileDirectoryEntity tFileDirectory){
        tFileDirectoryService.save(tFileDirectory);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改文件目录表")
    @ApiOperation("修改文件目录表")
//    @RequiresPermissions("jianping:tfiledirectory:update")
    public R update(@RequestBody TFileDirectoryEntity tFileDirectory){
        tFileDirectoryService.updateById(tFileDirectory);
        
        return R.ok();
    }

    /**
     * 批量修改
     */
    @PostMapping("/updateBatch")
    @SysLog("批量修改文件目录表")
    @ApiOperation("批量修改文件目录表")
//    @RequiresPermissions("jianping:tfiledirectory:update")
    public R updateBatch(@RequestBody List<TFileDirectoryEntity> tFileDirectoryList){
        tFileDirectoryService.saveOrUpdateBatch(tFileDirectoryList);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除文件目录表") 
    @ApiOperation("删除文件目录表") 
//    @RequiresPermissions("jianping:tfiledirectory:delete")
    public R delete(@RequestBody Long[] ids){
        tFileDirectoryService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
