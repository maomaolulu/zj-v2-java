package may.yuntian.anlian.vo;

import com.baomidou.mybatisplus.annotation.TableId;


/**
 * 项目日期查询
 */
public class ProjectDateVo {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 委托日期开始
     */
    private String entrustDateStart;
    /**
     * 委托日期结束
     */
    private String entrustDateEnd;
    /**
     * 签订日期开始
     */
    private String signDateStart;
    /**
     * 签订日期结束
     */
    private String signDateEnd;
    /**
     * 要求报告完成日期开始
     */
    private String claimEndDateStart;
    /**
     * 要求报告完成日期结束
     */
    private String claimEndDateEnd;
    /**
     * 任务下发日期
     */
    private String taskReleaseDate;
    /**
     * 计划完成日期
     */
    private String planFinishDate;
    /**
     * 计划调查日期
     */
    private String planSurveyDate;
    /**
     * 计划采样开始日期
     */
    private String planStartDate;
    /**
     * 计划采样结束日期
     */
    private String planEndDate;
    /**
     * 实际调查日期开始
     */
    private String surveyDateStart;
    /**
     * 实际调查日期结束
     */
    private String surveyDateEnd;
    /**
     * 实际采样开始日期
     */
    private String startDate;
    /**
     * 实际采样结束日期
     */
    private String endDate;
    /**
     * 采样方案制定日期
     */
    private String makePlanDate;
    /**
     * 报告调查日期(报告上展示)
     */
    private String reportSurveyDate;
    /**
     * 报告采样计划制定日期(报告上展示)
     */
    private String reportLayoutDate;
    /**
     * 报告采样开始日期(报告上展示)
     */
    private String reportStartDate;
    /**
     * 报告采样结束日期(报告上展示)
     */
    private String reportEndDate;
    /**
     * 送样日期
     */
    private String deliverDate;
    /**
     * 收样日期
     */
    private String receivedDate;
    /**
     * 物理因素发送日期
     */
    private String physicalSendDate;
    /**
     * 物理因素接收日期
     */
    private String physicalAcceptDate;
    /**
     * 采样记录发送日期
     */
    private String gatherSendDate;
    /**
     * 采样记录接收日期
     */
    private String gatherAcceptDate;
    /**
     * 数据报告出具日期(报告上展示)
     */
    private String labReportIssue;
    /**
     * 检测报告发送日期(实验室报告)
     */
    private String labReportSend;
    /**
     * 检测报告接收日期
     */
    private String labReportAccept;
    /**
     * 检测结果处理过程记录日期(报告上展示)
     */
    private String labResultDeal;

    /**
     * 审核开始日期(内审)开始
     */
    private String examineStartStart;
    /**
     * 审核开始日期(内审)结束
     */
    private String examineStartEnd;
    /**
     * 技术审核日期
     */
    private String technicalAudit;
    /**
     * 评审开始日期(专家)(报告上展示)
     */
    private String reviewStart;
    /**
     * 出版前校核(开始)日期(报告上展示)
     */
    private String checkStart;
    /**
     * 报告移交日期(项目负责人移交)
     */
    private String reportTransfer;
    /**
     * (正式)报告接收日期
     */
    private String reportAccept;
    /**
     * 报告签发日期开始
     */
    private String reportIssueStart;
    /**
     * 报告签发日期结束
     */
    private String reportIssueEnd;
    /**
     * 报告装订日期开始
     */
    private String reportBindingStart;
    /**
     * 报告装订日期结束
     */
    private String reportBindingEnd;
    /**
     * 报告寄送日期开始
     */
    private String reportSendStart;
    /**
     * 报告寄送日期结束
     */
    private String reportSendEnd;
    /**
     * 报告归档日期开始
     */
    private String reportFilingStart;
    /**
     * 报告归档日期结束
     */
    private String reportFilingEnd;
    /**
     * 收款日期日期开始
     */
    private String receiveAmountStart;
    /**
     * 收款日期日期结束
     */
    private String receiveAmountEnd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getEntrustDateStart() {
        return entrustDateStart;
    }

    public void setEntrustDateStart(String entrustDateStart) {
        this.entrustDateStart = entrustDateStart;
    }

    public String getEntrustDateEnd() {
        return entrustDateEnd;
    }

    public void setEntrustDateEnd(String entrustDateEnd) {
        this.entrustDateEnd = entrustDateEnd;
    }

    public String getSignDateStart() {
        return signDateStart;
    }

    public void setSignDateStart(String signDateStart) {
        this.signDateStart = signDateStart;
    }

    public String getSignDateEnd() {
        return signDateEnd;
    }

    public void setSignDateEnd(String signDateEnd) {
        this.signDateEnd = signDateEnd;
    }

    public String getClaimEndDateStart() {
        return claimEndDateStart;
    }

    public void setClaimEndDateStart(String claimEndDateStart) {
        this.claimEndDateStart = claimEndDateStart;
    }

    public String getClaimEndDateEnd() {
        return claimEndDateEnd;
    }

    public void setClaimEndDateEnd(String claimEndDateEnd) {
        this.claimEndDateEnd = claimEndDateEnd;
    }

    public String getTaskReleaseDate() {
        return taskReleaseDate;
    }

    public void setTaskReleaseDate(String taskReleaseDate) {
        this.taskReleaseDate = taskReleaseDate;
    }

    public String getPlanFinishDate() {
        return planFinishDate;
    }

    public void setPlanFinishDate(String planFinishDate) {
        this.planFinishDate = planFinishDate;
    }

    public String getPlanSurveyDate() {
        return planSurveyDate;
    }

    public void setPlanSurveyDate(String planSurveyDate) {
        this.planSurveyDate = planSurveyDate;
    }

    public String getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(String planStartDate) {
        this.planStartDate = planStartDate;
    }

    public String getPlanEndDate() {
        return planEndDate;
    }

    public void setPlanEndDate(String planEndDate) {
        this.planEndDate = planEndDate;
    }

    public String getSurveyDateStart() {
        return surveyDateStart;
    }

    public void setSurveyDateStart(String surveyDateStart) {
        this.surveyDateStart = surveyDateStart;
    }

    public String getSurveyDateEnd() {
        return surveyDateEnd;
    }

    public void setSurveyDateEnd(String surveyDateEnd) {
        this.surveyDateEnd = surveyDateEnd;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getMakePlanDate() {
        return makePlanDate;
    }

    public void setMakePlanDate(String makePlanDate) {
        this.makePlanDate = makePlanDate;
    }

    public String getReportSurveyDate() {
        return reportSurveyDate;
    }

    public void setReportSurveyDate(String reportSurveyDate) {
        this.reportSurveyDate = reportSurveyDate;
    }

    public String getReportLayoutDate() {
        return reportLayoutDate;
    }

    public void setReportLayoutDate(String reportLayoutDate) {
        this.reportLayoutDate = reportLayoutDate;
    }

    public String getReportStartDate() {
        return reportStartDate;
    }

    public void setReportStartDate(String reportStartDate) {
        this.reportStartDate = reportStartDate;
    }

    public String getReportEndDate() {
        return reportEndDate;
    }

    public void setReportEndDate(String reportEndDate) {
        this.reportEndDate = reportEndDate;
    }

    public String getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(String deliverDate) {
        this.deliverDate = deliverDate;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getPhysicalSendDate() {
        return physicalSendDate;
    }

    public void setPhysicalSendDate(String physicalSendDate) {
        this.physicalSendDate = physicalSendDate;
    }

    public String getPhysicalAcceptDate() {
        return physicalAcceptDate;
    }

    public void setPhysicalAcceptDate(String physicalAcceptDate) {
        this.physicalAcceptDate = physicalAcceptDate;
    }

    public String getGatherSendDate() {
        return gatherSendDate;
    }

    public void setGatherSendDate(String gatherSendDate) {
        this.gatherSendDate = gatherSendDate;
    }

    public String getGatherAcceptDate() {
        return gatherAcceptDate;
    }

    public void setGatherAcceptDate(String gatherAcceptDate) {
        this.gatherAcceptDate = gatherAcceptDate;
    }

    public String getLabReportIssue() {
        return labReportIssue;
    }

    public void setLabReportIssue(String labReportIssue) {
        this.labReportIssue = labReportIssue;
    }

    public String getLabReportSend() {
        return labReportSend;
    }

    public void setLabReportSend(String labReportSend) {
        this.labReportSend = labReportSend;
    }

    public String getLabReportAccept() {
        return labReportAccept;
    }

    public void setLabReportAccept(String labReportAccept) {
        this.labReportAccept = labReportAccept;
    }

    public String getLabResultDeal() {
        return labResultDeal;
    }

    public void setLabResultDeal(String labResultDeal) {
        this.labResultDeal = labResultDeal;
    }


    public String getTechnicalAudit() {
        return technicalAudit;
    }

    public void setTechnicalAudit(String technicalAudit) {
        this.technicalAudit = technicalAudit;
    }

    public String getReviewStart() {
        return reviewStart;
    }

    public void setReviewStart(String reviewStart) {
        this.reviewStart = reviewStart;
    }

    public String getCheckStart() {
        return checkStart;
    }

    public void setCheckStart(String checkStart) {
        this.checkStart = checkStart;
    }

    public String getReportTransfer() {
        return reportTransfer;
    }

    public void setReportTransfer(String reportTransfer) {
        this.reportTransfer = reportTransfer;
    }

    public String getReportAccept() {
        return reportAccept;
    }

    public void setReportAccept(String reportAccept) {
        this.reportAccept = reportAccept;
    }

    public String getReportIssueStart() {
        return reportIssueStart;
    }

    public void setReportIssueStart(String reportIssueStart) {
        this.reportIssueStart = reportIssueStart;
    }

    public String getReportIssueEnd() {
        return reportIssueEnd;
    }

    public void setReportIssueEnd(String reportIssueEnd) {
        this.reportIssueEnd = reportIssueEnd;
    }

    public String getReportBindingStart() {
        return reportBindingStart;
    }

    public void setReportBindingStart(String reportBindingStart) {
        this.reportBindingStart = reportBindingStart;
    }

    public String getReportBindingEnd() {
        return reportBindingEnd;
    }

    public void setReportBindingEnd(String reportBindingEnd) {
        this.reportBindingEnd = reportBindingEnd;
    }

    public String getReportSendStart() {
        return reportSendStart;
    }

    public void setReportSendStart(String reportSendStart) {
        this.reportSendStart = reportSendStart;
    }

    public String getReportSendEnd() {
        return reportSendEnd;
    }

    public void setReportSendEnd(String reportSendEnd) {
        this.reportSendEnd = reportSendEnd;
    }

    public String getReportFilingStart() {
        return reportFilingStart;
    }

    public void setReportFilingStart(String reportFilingStart) {
        this.reportFilingStart = reportFilingStart;
    }

    public String getReportFilingEnd() {
        return reportFilingEnd;
    }

    public void setReportFilingEnd(String reportFilingEnd) {
        this.reportFilingEnd = reportFilingEnd;
    }

    public String getReceiveAmountStart() {
        return receiveAmountStart;
    }

    public void setReceiveAmountStart(String receiveAmountStart) {
        this.receiveAmountStart = receiveAmountStart;
    }

    public String getReceiveAmountEnd() {
        return receiveAmountEnd;
    }

    public void setReceiveAmountEnd(String receiveAmountEnd) {
        this.receiveAmountEnd = receiveAmountEnd;
    }


    public String getExamineStartStart() {
        return examineStartStart;
    }

    public void setExamineStartStart(String examineStartStart) {
        this.examineStartStart = examineStartStart;
    }

    public String getExamineStartEnd() {
        return examineStartEnd;
    }

    public void setExamineStartEnd(String examineStartEnd) {
        this.examineStartEnd = examineStartEnd;
    }
}
