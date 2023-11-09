package may.yuntian.jianping.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.R;
import may.yuntian.jianping.entity.SampleImgEntity;
import may.yuntian.jianping.service.SampleImgService;
import may.yuntian.minio.utils.MinioUtil;
import may.yuntian.untils.AlRedisUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 采样影像记录管理
 * WEB请求处理层
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-09-14 22:38:35
 */
@RestController
@Api(tags="采样记录:采样影像记录")
@RequestMapping("/sampleImg")
public class SampleImgController {
    @Autowired
    private SampleImgService sampleImgService;
    @Autowired
    private AlRedisUntil alRedisUntil;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("根据条件分页查询采样影像记录列表")
//    @RequiresPermissions("sample:gather:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = sampleImgService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 根据项目ID获取列表
     */
    @GetMapping("/getList/{projectId}")
    @ApiOperation("根据项目ID获取列表")
//    @RequiresPermissions("sample:gather:evInfo")
    public R getList(@PathVariable("projectId") Long projectId){
        List<SampleImgEntity> list = sampleImgService.getListByProjectId(projectId);

        return R.ok().put("data", list);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("根据ID显示采样影像记录详情")
//    @RequiresPermissions("sample:gather:evInfo")
    public R info(@PathVariable("id") Long id){
        SampleImgEntity sampleImg = sampleImgService.getById(id);

        return R.ok().put("sampleImg", sampleImg);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增采样影像记录")
    @ApiOperation("新增采样影像记录")
//    @RequiresPermissions("sample:gather:save")
    public R save(@RequestBody SampleImgEntity sampleImg){
        if (null != sampleImg.getUrl()){
            Object o = alRedisUntil.hget("anlian-java",sampleImg.getUrl());
            if (null!=o){
                alRedisUntil.hdel("anlian-java",sampleImg.getUrl());
            }
        }
        sampleImgService.save(sampleImg);

        return R.ok();
    }

    /**
     * 批量保存或修改
     */
    @PostMapping("/saveOrUpdateBatch")
    @SysLog("修改采样影像记录")
    @ApiOperation("修改采样影像记录")
//    @RequiresPermissions("sample:gather:update")
    public R saveOrUpdateBatch(@RequestBody List<SampleImgEntity> sampleImgList){
        if (sampleImgList!=null&&sampleImgList.size()>0){

            //TODO 处理图片缓存
           for (SampleImgEntity sampleImg:sampleImgList){
               if (null!=sampleImg.getId()){
                    SampleImgEntity oldSample = sampleImgService.getById(sampleImg.getId());
                   if (StringUtils.isNotBlank(sampleImg.getUrl()) && StringUtils.isNotBlank(oldSample.getUrl()) && !oldSample.getUrl().equals(sampleImg.getUrl())){
                       MinioUtil.remove(oldSample.getUrl());
                       Object o = alRedisUntil.hget("anlian-java",sampleImg.getUrl());
                       if (null!=o){
                           alRedisUntil.hdel("anlian-java",sampleImg.getUrl());
                       }
                   }
               }else {
                   if (StringUtils.isNotBlank(sampleImg.getUrl())){
                       Object o = alRedisUntil.hget("anlian-java",sampleImg.getUrl());
                       if (null!=o){
                           alRedisUntil.hdel("anlian-java",sampleImg.getUrl());
                       }
                   }
               }
           }
            sampleImgService.saveSampleImg(sampleImgList);
        }else {
            return R.error("请填写采集影像后再保存.");
        }

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改采样影像记录")
    @ApiOperation("修改采样影像记录")
//    @RequiresPermissions("sample:gather:update")
    public R update(@RequestBody SampleImgEntity sampleImg){
        SampleImgEntity oldSSampleImg = sampleImgService.getById(sampleImg.getId());
        if (null != oldSSampleImg.getUrl() && !oldSSampleImg.getUrl().equals(sampleImg.getUrl())){
            MinioUtil.remove(oldSSampleImg.getUrl());
        }
        if (null != sampleImg.getUrl() && !oldSSampleImg.getUrl().equals(sampleImg.getUrl())){
            Object o = alRedisUntil.hget("anlian-java",sampleImg.getUrl());
            if (null!=o){
                alRedisUntil.hdel("anlian-java",sampleImg.getUrl());
            }
        }

        sampleImgService.updateById(sampleImg);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除采样影像记录")
    @ApiOperation("删除采样影像记录")
//    @RequiresPermissions("sample:gather:delete")
    public R delete(@RequestBody Long[] ids){

        List<SampleImgEntity> list = sampleImgService.listByIds(Arrays.asList(ids));
        for (SampleImgEntity sampleImg:list){
            MinioUtil.remove(sampleImg.getUrl());
        }
        sampleImgService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
