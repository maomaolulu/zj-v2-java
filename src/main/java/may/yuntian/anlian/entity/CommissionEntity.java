package may.yuntian.anlian.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 *提成记录实体类
 * 
 * @author LiXin
 * @data 2020-12-09
 */
@TableName("t_commission")
public class CommissionEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 *自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 *项目ID
	 */
	private Long projectId;
	/**
	 *提成类型 
	 */
	private String type;
	/**
	 *状态(1未提成 2已提成 3异常)
	 */
	private Integer state;
	/**
	 *提成金额(元)
	 */
	private BigDecimal cmsAmount;
	/**
	 *提成人(可能是多个)
	 */
	private String personnel;
	/**
	 *隶属公司
	 */
	private String subjection;
	/**
	 *提成日期
	 */
	private Date commissionDate;
	/**
	 *统计日期
	 */
	private Date countDate;
	/**
	 * 数据入库时间
	 */
	private Date createtime;
	/**
	 * 修改时间
	 */
	private Date updatetime;
	/**
	 * 开始时间
	 */
	@TableField(exist=false)
	private Date startDate;
	/**
	 * 结束时间
	 */
	@TableField(exist=false)
	private Date endDate;
	/**
	 * 多个类型字符串以，隔开
	 */
	@TableField(exist=false)
	private String types;
	/**
	 * 关联的项目信息
	 */
	@TableField(exist=false)
	private ProjectEntity project;

    /**
     *
     */
    private String pType;

    public String getpType() {
        return pType;
    }

    public void setpType(String pType) {
        this.pType = pType;
    }

    /**
	 *获取自增主键ID
	 */
	public Long getId() {
		return id;
	}
	/**
	 *设置自增主键ID
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取项目ID
	 */
	public Long getProjectId() {
		return projectId;
	}
	/**
	 * 设置项目ID
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public BigDecimal getCmsAmount() {
		return cmsAmount;
	}
	public void setCmsAmount(BigDecimal cmsAmount) {
		this.cmsAmount = cmsAmount;
	}
	public String getPersonnel() {
		return personnel;
	}
	public void setPersonnel(String personnel) {
		this.personnel = personnel;
	}
	public String getSubjection() {
		return subjection;
	}
	public void setSubjection(String subjection) {
		this.subjection = subjection;
	}
	public Date getCommissionDate() {
		return commissionDate;
	}
	public void setCommissionDate(Date commissionDate) {
		this.commissionDate = commissionDate;
	}
	public Date getCountDate() {
		return countDate;
	}
	public void setCountDate(Date countDate) {
		this.countDate = countDate;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getTypes() {
		return types;
	}
	public void setTypes(String types) {
		this.types = types;
	}
	@Override
	public String toString() {
		return "CommissionEntity [id=" + id + ", projectId=" + projectId + ", type=" + type + ", state=" + state
				+ ", cmsAmount=" + cmsAmount + ", personnel=" + personnel + ", subjection=" + subjection
				+ ", commissionDate=" + commissionDate + ", countDate=" + countDate + ", createtime=" + createtime
				+ ", updatetime=" + updatetime + "]";
	}
	public ProjectEntity getProject() {
		return project;
	}
	public void setProject(ProjectEntity project) {
		this.project = project;
	}

	

}
