package may.yuntian.jianping.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.jianping.dto.SignatureDto;
import may.yuntian.jianping.service.DateSignatureService;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author gy
 * @date 2023-07-19 14:01
 */
@RestController
@Api(tags = "签名模块")
@RequestMapping("/date_signature")
public class DateSignatureController {

    @Autowired
    private DateSignatureService dateSignatureService;


    /**
     * 采样计划保存方案编制人，审核人以及批准人签名信息
     */
    @PostMapping("/signature_save_sp")
    @ApiOperation("采样计划保存方案编制人，审核人以及批准人签名信息")
    public Result signatureSaveSp(@RequestBody JSONObject param) {
        return dateSignatureService.signatureSaveSp(param);
    }

    /**
     * 采样记录保存采样人，复核人签名信息
     */
    @PostMapping("/signature_save")
    @ApiOperation("采样记录保存采样人，复核人签名信息")
    public Result signatureSave(@RequestBody JSONObject param) {
        return dateSignatureService.signatureSave(param);
    }



    /**
     * 获取签名下采样人和复核人等数据
     */
    @GetMapping("/signature_lis")
    @ApiOperation("获取签名下采样人和复核人等数据")
    public Result weatherLis1(SignatureDto dto) {
        return dateSignatureService.signatureLis1(dto);
    }
}
