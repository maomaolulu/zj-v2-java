package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 实验室-接受的项目(按照原始记录单批次区分)
 * 
 * @author LiXin
 * @email ''
 * @date 2023-02-17 15:37:49
 */
@Data
@TableName("lab_accept_project")
public class LabAcceptProjectEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键，自增
	 */
	@TableId
	private Long id;
	/**
	 * 数据所属公司
	 */
	private String dataBelong;
	/**
	 * 送样单数据来源：1：检评； 2：评价；3：环境；4：公卫；
	 */
	private Integer sampleDataSource;

    /** 是否已接收到物理数据：0：未接收； 1 已接收； */
    private Integer isExistPhysics;
	/**
	 * 主业务项目id
	 */
	private Long projectId;
	/**
	 * 主业务项目类型：1：化学物理；2：纯化学；3：纯物理；
	 */
	private Integer projectType;
	/**
	 * 送样单批次 (职卫: 项目编号+轮次+批次)
	 */
	private String sampleDeliveryBatch;
	/**
	 * 采样日期
	 */
	private Date gatherDate;
	/**
	 * 送样时间
	 */
	private Date sampleDeliveryTime;
	/**
	 * 状态 送样单进度：1：待收样(默认);2：检测中;3：待结果审核;  4：待编制;   5-项目归档
	 */
	private Integer sampleDeliveryStatus;
	/**
	 * 收样时间
	 */
	private Date sampleCollectionTime;
	/**
	 * 是否编制  0：未编制；1：已编制
	 */
	private Integer isCompilation;
	/**
	 * 创建人
	 */
	private String createBy;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新人
	 */
	private String updateBy;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 采样方案/记录对应的id(职卫需要用)
	 */
	private String planRecordIds;
	/**
	 * 采样轮次 默认第一轮(职卫需要用)
	 */
	private Integer sampleRound;
	/**
	 * 采样批次 (职卫需要用)
	 */
	private Integer sampleBatch;
	/**
	 * 采样任务id(环境公卫)
	 */
	private Long taskId;
	/**
	 * 采样日期(环境公卫)
	 */
	private Date testDate;
	/**
	 * 送样日期(环境公卫)
	 */
	private Date deliverDate;
	/**
	 * 送样人id(环境公卫)
	 */
	private Long deliverUser;

	/**
	 * 设置：主键，自增
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：主键，自增
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：数据所属公司
	 */
	public void setDataBelong(String dataBelong) {
		this.dataBelong = dataBelong;
	}
	/**
	 * 获取：数据所属公司
	 */
	public String getDataBelong() {
		return dataBelong;
	}
	/**
	 * 设置：送样单数据来源：1：检评； 2：评价；3：环境；4：公卫；
	 */
	public void setSampleDataSource(Integer sampleDataSource) {
		this.sampleDataSource = sampleDataSource;
	}
	/**
	 * 获取：送样单数据来源：1：检评； 2：评价；3：环境；4：公卫；
	 */
	public Integer getSampleDataSource() {
		return sampleDataSource;
	}
	/**
	 * 设置：主业务项目id
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	/**
	 * 获取：主业务项目id
	 */
	public Long getProjectId() {
		return projectId;
	}
	/**
	 * 设置：主业务项目类型：1：化学物理；2：纯化学；3：纯物理；
	 */
	public void setProjectType(Integer projectType) {
		this.projectType = projectType;
	}
	/**
	 * 获取：主业务项目类型：1：化学物理；2：纯化学；3：纯物理；
	 */
	public Integer getProjectType() {
		return projectType;
	}
	/**
	 * 设置：送样单批次 (职卫: 项目编号+轮次+批次)
	 */
	public void setSampleDeliveryBatch(String sampleDeliveryBatch) {
		this.sampleDeliveryBatch = sampleDeliveryBatch;
	}
	/**
	 * 获取：送样单批次 (职卫: 项目编号+轮次+批次)
	 */
	public String getSampleDeliveryBatch() {
		return sampleDeliveryBatch;
	}
	/**
	 * 设置：采样日期
	 */
	public void setGatherDate(Date gatherDate) {
		this.gatherDate = gatherDate;
	}
	/**
	 * 获取：采样日期
	 */
	public Date getGatherDate() {
		return gatherDate;
	}
	/**
	 * 设置：送样时间
	 */
	public void setSampleDeliveryTime(Date sampleDeliveryTime) {
		this.sampleDeliveryTime = sampleDeliveryTime;
	}
	/**
	 * 获取：送样时间
	 */
	public Date getSampleDeliveryTime() {
		return sampleDeliveryTime;
	}
	/**
	 * 设置：状态 送样单进度：1：待收样(默认);2：检测中;3：待结果审核;  4：待编制;   5-项目归档
	 */
	public void setSampleDeliveryStatus(Integer sampleDeliveryStatus) {
		this.sampleDeliveryStatus = sampleDeliveryStatus;
	}
	/**
	 * 获取：状态 送样单进度：1：待收样(默认);2：检测中;3：待结果审核;  4：待编制;   5-项目归档
	 */
	public Integer getSampleDeliveryStatus() {
		return sampleDeliveryStatus;
	}
	/**
	 * 设置：收样时间
	 */
	public void setSampleCollectionTime(Date sampleCollectionTime) {
		this.sampleCollectionTime = sampleCollectionTime;
	}
	/**
	 * 获取：收样时间
	 */
	public Date getSampleCollectionTime() {
		return sampleCollectionTime;
	}
	/**
	 * 设置：是否编制  0：未编制；1：已编制
	 */
	public void setIsCompilation(Integer isCompilation) {
		this.isCompilation = isCompilation;
	}
	/**
	 * 获取：是否编制  0：未编制；1：已编制
	 */
	public Integer getIsCompilation() {
		return isCompilation;
	}
	/**
	 * 设置：创建人
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取：创建人
	 */
	public String getCreateBy() {
		return createBy;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：更新人
	 */
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	/**
	 * 获取：更新人
	 */
	public String getUpdateBy() {
		return updateBy;
	}
	/**
	 * 设置：更新时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * 设置：采样方案/记录对应的id(职卫需要用)
	 */
	public void setPlanRecordIds(String planRecordIds) {
		this.planRecordIds = planRecordIds;
	}
	/**
	 * 获取：采样方案/记录对应的id(职卫需要用)
	 */
	public String getPlanRecordIds() {
		return planRecordIds;
	}
	/**
	 * 设置：采样轮次 默认第一轮(职卫需要用)
	 */
	public void setSampleRound(Integer sampleRound) {
		this.sampleRound = sampleRound;
	}
	/**
	 * 获取：采样轮次 默认第一轮(职卫需要用)
	 */
	public Integer getSampleRound() {
		return sampleRound;
	}
	/**
	 * 设置：采样批次 (职卫需要用)
	 */
	public void setSampleBatch(Integer sampleBatch) {
		this.sampleBatch = sampleBatch;
	}
	/**
	 * 获取：采样批次 (职卫需要用)
	 */
	public Integer getSampleBatch() {
		return sampleBatch;
	}
	/**
	 * 设置：采样任务id(环境公卫)
	 */
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	/**
	 * 获取：采样任务id(环境公卫)
	 */
	public Long getTaskId() {
		return taskId;
	}
	/**
	 * 设置：采样日期(环境公卫)
	 */
	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}
	/**
	 * 获取：采样日期(环境公卫)
	 */
	public Date getTestDate() {
		return testDate;
	}
	/**
	 * 设置：送样日期(环境公卫)
	 */
	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}
	/**
	 * 获取：送样日期(环境公卫)
	 */
	public Date getDeliverDate() {
		return deliverDate;
	}
	/**
	 * 设置：送样人id(环境公卫)
	 */
	public void setDeliverUser(Long deliverUser) {
		this.deliverUser = deliverUser;
	}
	/**
	 * 获取：送样人id(环境公卫)
	 */
	public Long getDeliverUser() {
		return deliverUser;
	}
}
