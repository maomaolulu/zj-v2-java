package may.yuntian.jianping.vo;

import lombok.Data;

@Data
public class ProtectionVo {
    /**
     * 对应关系表id
     */
    private Long id;
    /**
     * 项目id
     */
    private Long projectId;
    /**
     * 防护用品id
     */
    private Long protectionId;
    /**
     * 类型
     */
    private String type;
    /**
     * 个人防护用品
     */
    private String name;
    /**
     * 说明
     */
    private String illustrate;
    /**
     * snr
     */
    private Integer snr;
    /**
     * nrr
     */
    private Integer nrr;

}
