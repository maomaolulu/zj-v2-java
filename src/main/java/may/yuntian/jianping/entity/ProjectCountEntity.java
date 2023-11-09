package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author LiXin
 * @email ''
 * @date 2022-07-25 14:15:40
 */
@Data
@TableName("al_project_count")
public class ProjectCountEntity implements Serializable {
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
	 * 资料收集开始日期(首次录入)
	 */
	private Date collectInfo;
	/**
	 * 资料收集最近一次操作时间
	 */
	private Date collectInfoLast;
	/**
	 * 调查信息录入
	 */
	private Date survey;
	/**
	 * 调查信息最近一次操作时间
	 */
	private Date surveyLast;
	/**
	 * 方案提交
	 */
	private Date planCommit;
	/**
	 * 方案提交最近一次操作时间
	 */
	private Date planCommitLast;
	/**
	 * 采样记录时间
	 */
	private Date sampleRecord;
	/**
	 * 采样记录最近一次操作时间
	 */
	private Date sampleRecordLast;
	/**
	 * 送样单生成
	 */
	private Date deliverySheet;
	/**
	 * 送样单最近一次生成
	 */
	private Date deliverySheetLast;
	/**
	 * 检测结果录入
	 */
	private Date testResult;
	/**
	 * 检测结果最近一次操作
	 */
	private Date testResultLast;
	/**
	 * 结果计算
	 */
	private Date resultMath;
	/**
	 * 结果计算最近一次操作
	 */
	private Date resultMathLast;
	/**
	 * 报告生成时间
	 */
	private Date reportGenerate;
	/**
	 * 报告生成最近一次操作时间
	 */
	private Date reportGenerateLast;
	/**
	 * 最终报告上传时间(首次上传)
	 */
	private Date finalReport;
	/**
	 * 最终报告最近一次上传时间
	 */
	private Date finalReportLast;
	/**
	 * 修改时间
	 */
	private Date updateTime;

	/**
	 * 设置：ID
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：ID
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：项目ID
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	/**
	 * 获取：项目ID
	 */
	public Long getProjectId() {
		return projectId;
	}
	/**
	 * 设置：资料收集开始日期(首次录入)
	 */
	public void setCollectInfo(Date collectInfo) {
		this.collectInfo = collectInfo;
	}
	/**
	 * 获取：资料收集开始日期(首次录入)
	 */
	public Date getCollectInfo() {
		return collectInfo;
	}
	/**
	 * 设置：资料收集最近一次操作时间
	 */
	public void setCollectInfoLast(Date collectInfoLast) {
		this.collectInfoLast = collectInfoLast;
	}
	/**
	 * 获取：资料收集最近一次操作时间
	 */
	public Date getCollectInfoLast() {
		return collectInfoLast;
	}
	/**
	 * 设置：调查信息录入
	 */
	public void setSurvey(Date survey) {
		this.survey = survey;
	}
	/**
	 * 获取：调查信息录入
	 */
	public Date getSurvey() {
		return survey;
	}
	/**
	 * 设置：调查信息最近一次操作时间
	 */
	public void setSurveyLast(Date surveyLast) {
		this.surveyLast = surveyLast;
	}
	/**
	 * 获取：调查信息最近一次操作时间
	 */
	public Date getSurveyLast() {
		return surveyLast;
	}
	/**
	 * 设置：方案提交
	 */
	public void setPlanCommit(Date planCommit) {
		this.planCommit = planCommit;
	}
	/**
	 * 获取：方案提交
	 */
	public Date getPlanCommit() {
		return planCommit;
	}
	/**
	 * 设置：方案提交最近一次操作时间
	 */
	public void setPlanCommitLast(Date planCommitLast) {
		this.planCommitLast = planCommitLast;
	}
	/**
	 * 获取：方案提交最近一次操作时间
	 */
	public Date getPlanCommitLast() {
		return planCommitLast;
	}
	/**
	 * 设置：采样记录时间
	 */
	public void setSampleRecord(Date sampleRecord) {
		this.sampleRecord = sampleRecord;
	}
	/**
	 * 获取：采样记录时间
	 */
	public Date getSampleRecord() {
		return sampleRecord;
	}
	/**
	 * 设置：采样记录最近一次操作时间
	 */
	public void setSampleRecordLast(Date sampleRecordLast) {
		this.sampleRecordLast = sampleRecordLast;
	}
	/**
	 * 获取：采样记录最近一次操作时间
	 */
	public Date getSampleRecordLast() {
		return sampleRecordLast;
	}
	/**
	 * 设置：送样单生成
	 */
	public void setDeliverySheet(Date deliverySheet) {
		this.deliverySheet = deliverySheet;
	}
	/**
	 * 获取：送样单生成
	 */
	public Date getDeliverySheet() {
		return deliverySheet;
	}
	/**
	 * 设置：送样单最近一次生成
	 */
	public void setDeliverySheetLast(Date deliverySheetLast) {
		this.deliverySheetLast = deliverySheetLast;
	}
	/**
	 * 获取：送样单最近一次生成
	 */
	public Date getDeliverySheetLast() {
		return deliverySheetLast;
	}
	/**
	 * 设置：检测结果录入
	 */
	public void setTestResult(Date testResult) {
		this.testResult = testResult;
	}
	/**
	 * 获取：检测结果录入
	 */
	public Date getTestResult() {
		return testResult;
	}
	/**
	 * 设置：检测结果最近一次操作
	 */
	public void setTestResultLast(Date testResultLast) {
		this.testResultLast = testResultLast;
	}
	/**
	 * 获取：检测结果最近一次操作
	 */
	public Date getTestResultLast() {
		return testResultLast;
	}
	/**
	 * 设置：结果计算
	 */
	public void setResultMath(Date resultMath) {
		this.resultMath = resultMath;
	}
	/**
	 * 获取：结果计算
	 */
	public Date getResultMath() {
		return resultMath;
	}
	/**
	 * 设置：结果计算最近一次操作
	 */
	public void setResultMathLast(Date resultMathLast) {
		this.resultMathLast = resultMathLast;
	}
	/**
	 * 获取：结果计算最近一次操作
	 */
	public Date getResultMathLast() {
		return resultMathLast;
	}
	/**
	 * 设置：报告生成时间
	 */
	public void setReportGenerate(Date reportGenerate) {
		this.reportGenerate = reportGenerate;
	}
	/**
	 * 获取：报告生成时间
	 */
	public Date getReportGenerate() {
		return reportGenerate;
	}
	/**
	 * 设置：报告生成最近一次操作时间
	 */
	public void setReportGenerateLast(Date reportGenerateLast) {
		this.reportGenerateLast = reportGenerateLast;
	}
	/**
	 * 获取：报告生成最近一次操作时间
	 */
	public Date getReportGenerateLast() {
		return reportGenerateLast;
	}
	/**
	 * 设置：最终报告上传时间(首次上传)
	 */
	public void setFinalReport(Date finalReport) {
		this.finalReport = finalReport;
	}
	/**
	 * 获取：最终报告上传时间(首次上传)
	 */
	public Date getFinalReport() {
		return finalReport;
	}
	/**
	 * 设置：最终报告最近一次上传时间
	 */
	public void setFinalReportLast(Date finalReportLast) {
		this.finalReportLast = finalReportLast;
	}
	/**
	 * 获取：最终报告最近一次上传时间
	 */
	public Date getFinalReportLast() {
		return finalReportLast;
	}
	/**
	 * 设置：修改时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
}
