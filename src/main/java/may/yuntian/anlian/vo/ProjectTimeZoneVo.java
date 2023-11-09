package may.yuntian.anlian.vo;


import lombok.Data;

import java.util.Date;

@Data
public class ProjectTimeZoneVo {
    private static final long serialVersionUID = 1L;

    /**
     * 项目ID
     */
    private Long id;
    /**
     * 项目编号
     */
    private String identifier;
    /**
     * 受检企业名称
     */
    private String company;
    /**
     * 项目状态(1项目录入，2任务分配，5采样，10收样，20检测报告，30报告装订，40质控签发，50报告寄送，60项目归档，70项目结束，98任务挂起，99项目中止)
     */
    private Integer status;
    /**
     * 项目类型
     */
    private String type;
    /**
     * 负责人id
     */
    private Long chargeId;
    /**
     * 负责人
     */
    private String charge;

    /**
     * 任务下发日期
     */
    private Date taskReleaseDate;
    /**
     * 实际调查日期
     */
    private Date surveyDate;
    /**
     * 采样方案制定日期
     */
    private Date makePlanDate;
    /**
     * 报告封面日期
     */
    private Date reportCoverDate;
    /**
     * 计划采样开始日期
     */
    private Date planStartDate;

    /**
     * 计划采样结束日期
     */
    private Date planEndDate;
    /**
     * 实际采样开始日期
     */
    private Date startDate;

    /**
     * 实际采样结束日期
     */
    private Date endDate;
    /**
     * 送样日期
     */
    private Date deliverDate;

    /**
     * 收样日期
     */
    private Date receivedDate;

    /**
     * 物理因素发送日期
     */
    private Date physicalSendDate;

    /**
     * 物理因素接收日期
     */
    private Date physicalAcceptDate;

    /**
     * 采样记录发送日期
     */
    private Date gatherSendDate;

    /**
     * 采样记录接收日期
     */
    private Date gatherAcceptDate;
    /**
     * 报告移交日期(项目负责人移交)
     */
    private Date reportTransfer;
    /**
     * (正式)报告接收日期
     */
    private Date reportAccept;
    /**
     * 报告装订日期
     */
    private Date reportBinding;
    /**
     * 报告归档日期
     */
    private Date reportFiling;


    /**
     * 采样人员
     */
    private String samplePerson;

    /**
     * 是否提成
     */
    private Date commissionDate;

}
