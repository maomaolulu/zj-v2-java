package may.yuntian.jianping.dto;

import lombok.Data;

/**
 * 发送留言
 *
 * @Author yrb
 * @Date 2023/4/7 13:59
 * @Version 1.0
 * @Description
 */
@Data
public class AbuSendNoteDTO {
    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 项目编号
     */
    private String identifier;

    /**
     * 受检单位
     */
    private String company;
    /**
     * 委托单位
     */
    private String entrustCompany;

    /**
     * 接收人手机号码
     */
    private String masterPhone;

    /**
     * 留言信息
     */
    private String note;

    /**
     * 业务员（市场人员）/接收人姓名
     */
    private String salesman;
    /**
     * 业务员ID/接收人Id
     */
    private Long salesmanId;

    /**
     * 项目录入人/发送人
     */
    private String writer;

    /**
     * 项目状态
     */
    private Integer status;
}
