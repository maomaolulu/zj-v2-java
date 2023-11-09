package may.yuntian.jianping.service;

import com.alibaba.fastjson.JSONObject;
import may.yuntian.jianping.dto.SignatureDto;
import may.yuntian.sys.utils.Result;

/**
 * @author gy
 * @date 2023-07-19 14:05
 */
public interface DateSignatureService {

    /**
     * 采样计划保存方案编制人，审核人以及批准人签名信息
     */
    Result signatureSaveSp(JSONObject param);

    /**
     *采样记录保存采样人，复核人签名信息
     */
    Result signatureSave(JSONObject param);


    /**
     * 获取签名下采样人和复核人等数据
     */
    Result signatureLis1(SignatureDto dto);
}
