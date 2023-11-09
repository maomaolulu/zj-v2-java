package may.yuntian.jianping.controller;

import java.util.Map;
import java.util.Arrays;
import java.util.List;

import may.yuntian.jianping.entity.SignatureEntity;
import may.yuntian.jianping.service.SignatureService;
import may.yuntian.minio.utils.MinioUtil;
import may.yuntian.untils.AlRedisUntil;
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

import may.yuntian.common.utils.R;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.annotation.SysLog;

import may.yuntian.anlian.utils.StringUtils;


/**
 * 电子签名路径表
 * 
 * @author LiXin
 * @date 2021-01-29
 */
@RestController
@Api(tags="电子签名信息")
@RequestMapping("signature")
public class SignatureController {
    @Autowired
    private SignatureService signatureService;
    @Autowired
    private AlRedisUntil alRedisUntil;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("根据条件分页查询电子签名列表")
//    @RequiresPermissions("sample:company:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = signatureService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 根据项目ID获取列表
     */
    @GetMapping("/getListByProjectId/{projectId}")
    @ApiOperation("根据ID显示电子签名详情")
//    @RequiresPermissions("sample:company:evInfo")
    public R getListByProjectId(@PathVariable("projectId") Long projectId){
        List<SignatureEntity> signatureList1 = signatureService.getListByProjectId(projectId);
        List<SignatureEntity> signatureList2 = signatureService.getListNeiBuByProjectId(projectId);

        return R.ok().put("signatureList", signatureList1).put("neiBuList",signatureList2);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("根据ID显示电子签名详情")
//    @RequiresPermissions("sample:company:evInfo")
    public R info(@PathVariable("id") Long id){
    	SignatureEntity signature = signatureService.getById(id);

        return R.ok().put("signature", signature);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("新增电子签名")
    @ApiOperation("新增电子签名")
//    @RequiresPermissions("sample:company:save")
    public R save(@RequestBody SignatureEntity signature){

        if (null != signature.getPath()){
            Object o = alRedisUntil.hget("anlian-java",signature.getPath());
            if (null!=o){
                alRedisUntil.hdel("anlian-java",signature.getPath());
            }
        }

        List<String> useSpitList = signature.getUseSpitList();
        if(useSpitList!=null&&useSpitList.size()>0) {
            String useList = StringUtils.join(useSpitList,",");
            signature.setUseList(useList);
        }else {
            signature.setUseList("");
        }
    	signatureService.save(signature);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改电子签名")
    @ApiOperation("修改电子签名")
//    @RequiresPermissions("sample:company:update")
    public R update(@RequestBody SignatureEntity signature){
        SignatureEntity oldSignature = signatureService.getById(signature);
        if (null != oldSignature.getPath() && !oldSignature.getPath().equals(signature.getPath())){
            MinioUtil.remove(oldSignature.getPath());
        }
        if (null != signature.getPath() && !oldSignature.getPath().equals(signature.getPath())){
            Object o = alRedisUntil.hget("anlian-java",signature.getPath());
            if (null!=o){
                alRedisUntil.hdel("anlian-java",signature.getPath());
            }
        }

    	List<String> useSpitList = signature.getUseSpitList();
    	if(useSpitList!=null&&useSpitList.size()>0) {
    		String useList = StringUtils.join(useSpitList,",");
        	signature.setUseList(useList);
    	}else {
			signature.setUseList("");
		}
    	
    	signatureService.updateById(signature);
        
        return R.ok();
    }

    /**
     * 批量修改
     */
    @PostMapping("/updateBatch")
    @SysLog("修改电子签名")
    @ApiOperation("修改电子签名")
//    @RequiresPermissions("sample:company:update")
    public R updateBatch(@RequestBody List<SignatureEntity> signatureList){
        for (SignatureEntity signature:signatureList){

            SignatureEntity oldSignature = signatureService.getById(signature.getId());
            if (null != oldSignature.getPath() && !oldSignature.getPath().equals(signature.getPath())){
                MinioUtil.remove(oldSignature.getPath());
            }
            if (null != signature.getPath() && !oldSignature.getPath().equals(signature.getPath())){
                Object o = alRedisUntil.hget("anlian-java",signature.getPath());
                if (null!=o){
                    alRedisUntil.hdel("anlian-java",signature.getPath());
                }
            }

            List<String> useSpitList = signature.getUseSpitList();
            if(useSpitList!=null&&useSpitList.size()>0) {
                String useList = StringUtils.join(useSpitList,",");
                signature.setUseList(useList);
            }else {
                signature.setUseList("");
            }

            signatureService.updateById(signature);
        }


        return R.ok();
    }



    /**
     * 删除
     */
    @PostMapping("/delete")
    @SysLog("删除电子签名") 
    @ApiOperation("删除电子签名") 
//    @RequiresPermissions("sample:company:delete")
    public R delete(@RequestBody Long[] ids){

        List<SignatureEntity> signatureEntities = signatureService.listByIds(Arrays.asList(ids));
        for (SignatureEntity signatureEntity:signatureEntities){
            if (null!=signatureEntity.getPath()){
                MinioUtil.remove(signatureEntity.getPath());
            }
        }
    	signatureService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
