package may.yuntian.jianping.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.jianping.dto.SignatureInformationDTO;
import may.yuntian.jianping.entity.ArtisanEntity;
import may.yuntian.jianping.entity.SignatureEntity;
import may.yuntian.jianping.entity.SignatureUserEntity;
import may.yuntian.jianping.service.ArtisanService;
import may.yuntian.jianping.service.SignatureUserService;
import may.yuntian.minio.utils.MinioUtil;
import may.yuntian.sys.utils.Result;
import may.yuntian.untils.AlRedisUntil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * WEB请求处理层
 *
 * @author : lixin
 * @date : 2022-3-15
 * @desc : 电子签名对应人员表
 */
@RestController
@Api(tags = "电子签名对应人员")
@RequestMapping("/signatureUser")
public class SignatureUserController {

    @Autowired
    private SignatureUserService signatureUserService;
    @Autowired
    private ArtisanService artisanService;
    @Autowired
    private AlRedisUntil alRedisUntil;


    /**
     * 获取报告采样人员调查人员列表--artisan表中数据
     *
     * @param name
     * @return
     */
    @GetMapping("/getDioCha")
    @ApiOperation("获取报告采样人员调查人员列表")
    public Result getDioCha(String name) {
        List<ArtisanEntity> list = artisanService.getDioChaList(name);
        return Result.data(list);
    }

    /**
     * 获取人员信息列表
     *
     * @return
     */
    @GetMapping("/getPersonnelInformation")
    @ApiOperation("获取人员信息列表")
    public Result getPersonnelInformation() {
        List<SignatureUserEntity> list = signatureUserService.getPersonnelInformation();
        return Result.data(list);
    }

    /**
     * 保存人员签名信息
     *
     * @param dto
     * @return
     */
    @PostMapping("/update")
    @ApiOperation("保存人员签名信息")
    @RequiresPermissions("zj:signatureUser:update")
    public Result update(@RequestBody SignatureInformationDTO dto) {
        SignatureUserEntity oldSignature = signatureUserService.getOneInfo(dto.getUserId());
        if (null != oldSignature.getPath() && !oldSignature.getPath().equals(dto.getPath())){
            MinioUtil.remove(oldSignature.getPath());
        }
        if (null != dto.getPath() && !oldSignature.getPath().equals(dto.getPath())){
            Object o = alRedisUntil.hget("anlian-java",dto.getPath());
            if (null!=o){
                alRedisUntil.hdel("anlian-java",dto.getPath());
            }
        }


        signatureUserService.updateSave(dto);
        return Result.ok();
    }

    /**
     * 已审核签名列表
     *
     * @return
     */
    @GetMapping("/queryPageByStatus")
    @ApiOperation("已审核签名列表")
    public Result queryPageByStatus(@RequestParam Map<String, Object> params) {
        List<SignatureUserEntity> list = signatureUserService.queryPageByStatus(params);
        return Result.resultData(list);
    }

    /**
     * 待审核签名列表
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("待审核签名列表")
    public Result list(@RequestParam Map<String, Object> params) {
        List<SignatureUserEntity> list = signatureUserService.listInfo(params);
        return Result.resultData(list);
    }


    /**
     * 驳回签名按钮
     *
     * @param user
     * @return
     */
    @PostMapping("/reject")
    @ApiOperation("保存人员签名信息")
    @RequiresPermissions("zj:signatureUser:reject")
    public Result reject(@RequestBody SignatureUserEntity user) {
        signatureUserService.reject(user);
        return Result.ok();
    }

    /**
     * 通过签名按钮
     *
     * @param user
     * @return
     */
    @PostMapping("/pass")
    @ApiOperation("保存人员签名信息")
    @RequiresPermissions("zj:signatureUser:pass")
    public Result pass(@RequestBody SignatureUserEntity user) {
        signatureUserService.pass(user);
        return Result.ok();
    }

    /**
     * 禁用或启用按钮
     *
     * @param user
     * @return
     */
    @PostMapping("/enableOrDisable")
    @ApiOperation("保存人员签名信息")
    @RequiresPermissions("zj:signatureUser:enableOrDisable")
    public Result enableOrDisable(@RequestBody SignatureUserEntity user) {
        signatureUserService.enableOrDisable(user);
        return Result.ok();
    }
}
