package may.yuntian.jianping.controller;

import java.util.List;
import java.util.Map;
import java.util.Arrays;

import may.yuntian.sys.utils.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.annotation.SysLog;

import may.yuntian.jianping.entity.ProductOutputEntity;
import may.yuntian.jianping.service.ProductOutputService;


/**
 * 主要产品与年产量管理
 * WEB请求处理层
 *
 * @author LiXin
 * @email ''
 * @date 2022-11-10 15:52:36
 */
@RestController
@Api(tags="主要产品与年产量")
@RequestMapping("/productOutput")
public class ProductOutputController {
    @Autowired
    private ProductOutputService productOutputService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("根据条件分页查询主要产品与年产量列表")
//    @RequiresPermissions("jianping:productOutput:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = productOutputService.queryPage(params);

        return Result.ok().put("page", page);
    }

    /**
     * 信息
     */
    @GetMapping("/getList/{projectId}")
    @ApiOperation("根据ID显示主要产品与年产量详情")
//    @RequiresPermissions("jianping:productOutput:evInfo")
    public Result getList(@PathVariable("projectId") Long projectId){
        List<ProductOutputEntity> list = productOutputService.getListByProjectId(projectId);

        return Result.data(list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("根据ID显示主要产品与年产量详情")
    @RequiresPermissions("jianping:productOutput:evInfo")
    public Result info(@PathVariable("id") Long id){
        ProductOutputEntity productOutput = productOutputService.getById(id);

        return Result.ok().put("productOutput", productOutput);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增主要产品与年产量")
    @ApiOperation("新增主要产品与年产量")
//    @RequiresPermissions("jianping:productOutput:save")
    public Result save(@RequestBody ProductOutputEntity productOutput){
        productOutputService.save(productOutput);

        return Result.ok();
    }

    /**
     * 批量保存
     */
    @PostMapping("/saveOrUpdateBatch")
    @SysLog("新增主要产品与年产量")
    @ApiOperation("新增主要产品与年产量")
//    @RequiresPermissions("jianping:productOutput:save")
    public Result saveOrUpdateBatch(@RequestBody List<ProductOutputEntity> productOutput){
        productOutputService.saveOrUpdateBatch(productOutput);

        return Result.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改主要产品与年产量")
    @ApiOperation("修改主要产品与年产量")
//    @RequiresPermissions("jianping:productOutput:update")
    public Result update(@RequestBody ProductOutputEntity productOutput){
        productOutputService.updateById(productOutput);
        
        return Result.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除主要产品与年产量") 
    @ApiOperation("删除主要产品与年产量") 
//    @RequiresPermissions("jianping:productOutput:delete")
    public Result delete(@RequestBody Long[] ids){
        productOutputService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
