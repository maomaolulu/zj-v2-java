package may.yuntian.anlian.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("al_project_date")
@Data
public class ProjectDateEntity {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 任务ID(x)
     */
    private Long taskId;

    /**
     * 委托日期
     */
    private Date entrustDate;

    /**
     * 签订日期
     */
    private Date signDate;

    /**
     * 要求报告完成日期
     */
    private Date claimEndDate;

    /**
     * 任务下发日期
     */
    private Date taskReleaseDate;

    /**
     * 计划完成日期
     */
    private Date planFinishDate;

    /**
     * 计划调查日期
     */
    private Date planSurveyDate;

    /**
     * 计划采样开始日期
     */
    private Date planStartDate;

    /**
     * 计划采样结束日期
     */
    private Date planEndDate;

    /**
     * 实际调查日期
     */
    private Date surveyDate;

    /**
     * 实际采样开始日期
     */
    private Date startDate;

    /**
     * 实际采样结束日期
     */
    private Date endDate;

    /**
     * 采样方案制定日期
     */
    private Date makePlanDate;

    /**
     * 报告调查日期(报告上展示)
     */
    private Date reportSurveyDate;

    /**
     * 报告采样计划制定日期(报告上展示)
     */
    private Date reportLayoutDate;

    /**
     * 报告采样开始日期(报告上展示)
     */
    private Date reportStartDate;

    /**
     * 报告采样结束日期(报告上展示)
     */
    private Date reportEndDate;

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
     * 数据报告出具日期(报告上展示)
     */
    private Date labReportIssue;

    /**
     * 检测报告发送日期(实验室报告)
     */
    private Date labReportSend;

    /**
     * 检测报告接收日期
     */
    private Date labReportAccept;

    /**
     * 检测结果处理过程记录日期(报告上展示)
     */
    private Date labResultDeal;

    /**
     * 审核开始日期(内审)
     */
    private Date examineStart;

    /**
     * 技术审核日期
     */
    private Date technicalAudit;

    /**
     * 评审开始日期(专家)(报告上展示)
     */
    private Date reviewStart;

    /**
     * 出版前校核(开始)日期(报告上展示)
     */
    private Date checkStart;

    /**
     * 报告移交日期(项目负责人移交)
     */
    private Date reportTransfer;

    /**
     * (正式)报告接收日期
     */
    private Date reportAccept;

    /**
     * 报告签发日期
     */
    private Date reportIssue;

    /**
     * 报告装订日期
     */
    private Date reportBinding;

    /**
     * 报告寄送日期
     */
    private Date reportSend;

    /**
     * 报告归档日期
     */
    private Date reportFiling;

    /**
     * 收款日期日期
     */
    private Date receiveAmount;

    /**
     * 报告封面日期
     */
    private Date reportCoverDate;


}
