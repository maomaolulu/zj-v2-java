package may.yuntian.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import may.yuntian.common.validator.group.AddGroup;
import may.yuntian.common.validator.group.UpdateGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统用户
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-16
 */
@TableName("sys_user")
public class SysUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户ID
	 */
	@TableId
	private Long userId;

	/**
	 * 用户名
	 */
	@NotBlank(message="用户名不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String username;
	
	/**
	 * 员工编号
	 */
	private String jobNum;

	/**
	 * 密码
	 */
	@NotBlank(message="密码不能为空", groups = AddGroup.class)
	private String password;

	/**
	 * 盐
	 */
	private String salt;

	/**
	 * 邮箱
	 */
//	@NotBlank(message="邮箱不能为空", groups = {AddGroup.class, UpdateGroup.class})
//	@Email(message="邮箱格式不正确", groups = {AddGroup.class, UpdateGroup.class})
	private String email;

	/**
	 * 手机号
	 */
	private String mobile;
	
	/**
	 * 隶属公司
	 */
	private String subjection;
	
	/**
	 * 用户类型  1：安联   2：其他中介
	 */
	private Integer type;
	
	/**
	 * 状态  0：禁用   1：正常
	 */
	private Integer status;
	
	/**
	 * 绑定IP，限定固定IP登录
	 */
	private String ip;
	
	/**
	 * 连续登录失败次数
	 */
	private Integer defeats;
	/**
	 * 入职时间
	 */
	private String entryTime;
	/**
	 * 离职时间
	 */
	private String resignationTime;
	/**
	 * 中介关联用户ID
	 */
	private Long belongUserid;
	/**
	 * 中介关联用户名称
	 */
	@TableField(exist=false)
	private String belongUsername;
	/**
	 * 密码修改次数
	 */
	private Integer changeNumber;
	/**
	 * 是否建立档案  0：默认   1：建立  2：不建立
	 */
	private Integer isBookbuilding;
	/**
	 * 性别（1男，2女）
	 */
	private Integer sex;
	
	/**
	 * 角色ID列表
	 */
	@TableField(exist=false)
	private List<Long> roleIdList;
	
	/**
	 * 部门ID
	 */
	@NotNull(message="部门不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private Long deptId;

	/**
	 * 部门名称
	 */
	@TableField(exist=false)
	private String deptName;
	
	/**
	 * 人员档案条数
	 */
	@TableField(exist=false)
	private Integer personBasicFilesCount;
	/**
	 * 人员荣誉证书条数
	 */
	@TableField(exist=false)
	private Integer personHonorCrtificateCount;
	/**
	 * 人员质量监督条数
	 */
	@TableField(exist=false)
	private Integer personSupervisionRecordsCount;
	/**
	 * 人员技能证书条数
	 */
	@TableField(exist=false)
	private Integer personTechnicalCertificateCount;
	/**
	 * 人员培训条数
	 */
	@TableField(exist=false)
	private Integer personTrainCount;
	
	/**
	 * 创建者ID
	 */
	private Long createUserId;
	

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 设置：
	 * @param userId 
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * 获取：
	 * @return Long
	 */
	public Long getUserId() {
		return userId;
	}
	
	/**
	 * 设置：用户名
	 * @param username 用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 获取：用户名
	 * @return String
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * 设置：密码
	 * @param password 密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 获取：密码
	 * @return String
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * 设置：邮箱
	 * @param email 邮箱
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 获取：邮箱
	 * @return String
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * 设置：手机号
	 * @param mobile 手机号
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * 获取：手机号
	 * @return String
	 */
	public String getMobile() {
		return mobile;
	}
	
	/**
	 * 设置：状态  0：禁用   1：正常
	 * @param status 状态  0：禁用   1：正常
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 获取：状态  0：禁用   1：正常
	 * @return Integer
	 */
	public Integer getStatus() {
		return status;
	}
	
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getDefeats() {
		return defeats;
	}

	public void setDefeats(Integer defeats) {
		this.defeats = defeats;
	}

	/**
	 * 设置：创建时间
	 * @param createTime 创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取：创建时间
	 * @return Date
	 */
	public Date getCreateTime() {
		return createTime;
	}

	public List<Long> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<Long> roleIdList) {
		this.roleIdList = roleIdList;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	/**
	 * 员工编号
	 * @return
	 */
	public String getJobNum() {
		return jobNum;
	}
	
	/**
	 * 员工编号
	 * @param jobNum
	 */
	public void setJobNum(String jobNum) {
		this.jobNum = jobNum;
	}
	
	

	public String getSubjection() {
		return subjection;
	}

	public void setSubjection(String subjection) {
		this.subjection = subjection;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * 入职时间
	 */
	public String getEntryTime() {
		return entryTime;
	}
	/**
	 * 入职时间
	 */
	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}
	/**
	 * 离职时间
	 */
	public String getResignationTime() {
		return resignationTime;
	}
	/**
	 * 离职时间
	 */
	public void setResignationTime(String resignationTime) {
		this.resignationTime = resignationTime;
	}
	/**
	 * 中介关联用户ID
	 */
	public Long getBelongUserid() {
		return belongUserid;
	}
	/**
	 * 中介关联用户ID
	 */
	public void setBelongUserid(Long belongUserid) {
		this.belongUserid = belongUserid;
	}
	/**
	 * 密码修改次数
	 */
	public Integer getChangeNumber() {
		return changeNumber;
	}
	/**
	 * 密码修改次数
	 */
	public void setChangeNumber(Integer changeNumber) {
		this.changeNumber = changeNumber;
	}

	public String getBelongUsername() {
		return belongUsername;
	}

	public void setBelongUsername(String belongUsername) {
		this.belongUsername = belongUsername;
	}
	/**
	 * 是否建立档案  0：默认   1：建立  2：不建立
	 */
	public Integer getIsBookbuilding() {
		return isBookbuilding;
	}
	/**
	 * 是否建立档案  0：默认   1：建立  2：不建立
	 */
	public void setIsBookbuilding(Integer isBookbuilding) {
		this.isBookbuilding = isBookbuilding;
	}
	/**
	 * 人员档案条数
	 */
	public Integer getPersonBasicFilesCount() {
		return personBasicFilesCount;
	}
	/**
	 * 人员档案条数
	 */
	public void setPersonBasicFilesCount(Integer personBasicFilesCount) {
		this.personBasicFilesCount = personBasicFilesCount;
	}
	/**
	 * 人员荣誉证书条数
	 */
	public Integer getPersonHonorCrtificateCount() {
		return personHonorCrtificateCount;
	}
	/**
	 * 人员荣誉证书条数
	 */
	public void setPersonHonorCrtificateCount(Integer personHonorCrtificateCount) {
		this.personHonorCrtificateCount = personHonorCrtificateCount;
	}
	/**
	 * 人员质量监督条数
	 */
	public Integer getPersonSupervisionRecordsCount() {
		return personSupervisionRecordsCount;
	}
	/**
	 * 人员质量监督条数
	 */
	public void setPersonSupervisionRecordsCount(Integer personSupervisionRecordsCount) {
		this.personSupervisionRecordsCount = personSupervisionRecordsCount;
	}
	/**
	 * 人员技能证书条数
	 */
	public Integer getPersonTechnicalCertificateCount() {
		return personTechnicalCertificateCount;
	}
	/**
	 * 人员技能证书条数
	 */
	public void setPersonTechnicalCertificateCount(Integer personTechnicalCertificateCount) {
		this.personTechnicalCertificateCount = personTechnicalCertificateCount;
	}
	/**
	 * 人员培训条数
	 */
	public Integer getPersonTrainCount() {
		return personTrainCount;
	}
	/**
	 * 人员培训条数
	 */
	public void setPersonTrainCount(Integer personTrainCount) {
		this.personTrainCount = personTrainCount;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	@Override
	public String toString() {
		return "SysUserEntity [userId=" + userId + ", username=" + username + ", jobNum=" + jobNum + ", password="
				+ password + ", salt=" + salt + ", email=" + email + ", mobile=" + mobile + ", subjection=" + subjection
				+ ", type=" + type + ", status=" + status + ", ip=" + ip + ", defeats=" + defeats + ", entryTime="
				+ entryTime + ", resignationTime=" + resignationTime + ", belongUserid=" + belongUserid
				+ ", roleIdList=" + roleIdList + ", deptId=" + deptId + ", deptName=" + deptName + ", createUserId="
				+ createUserId + ", createTime=" + createTime + "]";
	}

	
	
}
