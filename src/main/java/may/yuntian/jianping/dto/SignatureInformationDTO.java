package may.yuntian.jianping.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 签名信息 表单
 *
 * @author cwt
 * @Create 2023-4-19 13:37:35
 */
@Data
public class SignatureInformationDTO implements Serializable {

    private static final long serialVersionUID = 42L;

    /**
     * useId
     */
    private Integer userId;

    /**
     * 签名路径
     */
    private String path;
}
