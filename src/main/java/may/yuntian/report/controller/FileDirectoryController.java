package may.yuntian.report.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.report.entity.FileDirectoryEntity;
import may.yuntian.report.service.FileDirectoryService;
import may.yuntian.sys.utils.Result;
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
@RequestMapping("filedirectory")
public class FileDirectoryController {
    @Autowired
    private FileDirectoryService fileDirectoryService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("根据条件分页查询文件目录表列表")
//    @RequiresPermissions("jianping:tfiledirectory:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = fileDirectoryService.queryPage(params);

        return Result.ok().put("page", page);
    }

    /**
     * 根据项目ID获取列表
     */
    @GetMapping("/getList/{projectId}")
    @ApiOperation("根据项目ID获取列表")
    public Result getList(@PathVariable("projectId") Long projectId){
        List<FileDirectoryEntity> list = fileDirectoryService.getListByProjectId(projectId);

        return Result.ok().put("list", list);
    }



    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("根据ID显示文件目录表详情")
//    @RequiresPermissions("jianping:tfiledirectory:evInfo")
    public Result info(@PathVariable("id") Long id){
        FileDirectoryEntity tFileDirectory = fileDirectoryService.getById(id);

        return Result.ok().put("tFileDirectory", tFileDirectory);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增文件目录表")
    @ApiOperation("新增文件目录表")
//    @RequiresPermissions("jianping:tfiledirectory:save")
    public Result save(@RequestBody FileDirectoryEntity tFileDirectory){
        fileDirectoryService.save(tFileDirectory);

        return Result.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改文件目录表")
    @ApiOperation("修改文件目录表")
//    @RequiresPermissions("jianping:tfiledirectory:update")
    public Result update(@RequestBody FileDirectoryEntity tFileDirectory){
        fileDirectoryService.updateById(tFileDirectory);
        
        return Result.ok();
    }

    /**
     * 批量修改
     */
    @PostMapping("/updateBatch")
    @SysLog("批量修改文件目录表")
    @ApiOperation("批量修改文件目录表")
//    @RequiresPermissions("jianping:tfiledirectory:update")
    public Result updateBatch(@RequestBody List<FileDirectoryEntity> tFileDirectoryList){
        fileDirectoryService.saveOrUpdateBatch(tFileDirectoryList);

        return Result.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除文件目录表") 
    @ApiOperation("删除文件目录表") 
//    @RequiresPermissions("jianping:tfiledirectory:delete")
    public Result delete(@RequestBody Long[] ids){
        fileDirectoryService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
