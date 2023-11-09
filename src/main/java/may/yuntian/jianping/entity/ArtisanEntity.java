package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;


/**
 *技术人员信息实体类
 * 
 * @author LiXin
 * @data 2020-11-17
 */
@TableName("t_artisan")
public class ArtisanEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 *自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 *技术人员ID
	 */
	private Long userid;
	/**
	 *技术类型
	 */
	private Integer type;
	/**
	 *工号
	 */
	private String jobNum;
	/**
	 *技术人员名称
	 */
	private String username;
	/**
	 *部门ID
	 */
	private Long deptId;

	/**
	 *数据入库时间
	 */
	private Date createtime;
	/**
	 *修改时间
	 */
	private Date updatetime;
	
	/**
	 *开始时间
	 */
	@TableField(exist=false)
	private Date startDate;
	/**
	 *结束时间
	 */
	@TableField(exist=false)
	private Date endDate;
	/**
	 *任务人员表
	 * 
	 */
//	@TableField(exist=false)
//	private List<PlanUserEntity> planUserList;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getJobNum() {
		return jobNum;
	}
	public void setJobNum(String jobNum) {
		this.jobNum = jobNum;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
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
//	public List<PlanUserEntity> getPlanUserList() {
//		return planUserList;
//	}
//	public void setPlanUserList(List<PlanUserEntity> planUserList) {
//		this.planUserList = planUserList;
//	}

}
