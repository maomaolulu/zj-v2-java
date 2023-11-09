package may.yuntian.modules.sys.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 角色
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-16
 */
@TableName("sys_role")
public class SysRoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 角色ID
	 */
	@ApiModelProperty(value="角色ID")
	@TableId
	private Long roleId;

	/**
	 * 角色名称
	 */
	@ApiModelProperty(value="角色名称")
	@NotBlank(message="角色名称不能为空")
	private String roleName;

	/**
	 * 备注
	 */
	@ApiModelProperty(value="备注")
	private String remark;
	
	/**
	 * 创建者ID
	 */
	@ApiModelProperty(value="创建者ID")
	private Long createUserId;
	
	/**
	 * 角色隶属公司（1.杭州，2.嘉兴，3.宁波）
	 */
	@ApiModelProperty(value="角色隶属公司")
	private Integer subordinate;
	
	/**
	 * 部门ID
	 */
	@ApiModelProperty(value="部门ID")
//	@NotNull(message="部门不能为空")
	private Long deptId;

	/**
	 * 部门名称
	 */
	@ApiModelProperty(value="部门名称")
	@TableField(exist=false)
	private String deptName;

	@ApiModelProperty(value="菜单权限")
	@TableField(exist=false)
	private List<Long> menuIdList;
	
	/**
	 * 部门数据权限
	 */
	@ApiModelProperty(value="部门数据权限")
	@TableField(exist=false)
	private List<Long> deptIdList;
	
	/**
	 * 项目类型权限
	 */
	@ApiModelProperty(value="项目类型权限")
	@TableField(exist=false)
	private List<Long> projectTypeList;
	
	/**
	 * 项目隶属权限
	 */
	@ApiModelProperty(value="项目隶属权限")
	@TableField(exist=false)
	private List<Long> orderList;
	
	/**
	 * 业务来源权限
	 */
	@ApiModelProperty(value="业务来源权限")
	@TableField(exist=false)
	private List<Long> sourceList;
	
	/**
	 * 此角色中包含的系统用户信息
	 */
	@ApiModelProperty(value="此角色中包含的系统用户信息")
	@TableField(exist=false)
	private List<SysUserEntity> sysUserList;
	
	
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 设置：
	 * @param roleId 
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/**
	 * 获取：
	 * @return Long
	 */
	public Long getRoleId() {
		return roleId;
	}
	
	/**
	 * 设置：角色名称
	 * @param roleName 角色名称
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * 获取：角色名称
	 * @return String
	 */
	public String getRoleName() {
		return roleName;
	}
	
	/**
	 * 设置：备注
	 * @param remark 备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取：备注
	 * @return String
	 */
	public String getRemark() {
		return remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getSubordinate() {
		return subordinate;
	}

	public void setSubordinate(Integer subordinate) {
		this.subordinate = subordinate;
	}

	public List<Long> getMenuIdList() {
		return menuIdList;
	}

	public void setMenuIdList(List<Long> menuIdList) {
		this.menuIdList = menuIdList;
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

	public List<Long> getDeptIdList() {
		return deptIdList;
	}

	public void setDeptIdList(List<Long> deptIdList) {
		this.deptIdList = deptIdList;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	
	/**
	 * 项目类型权限
	 */
	public List<Long> getProjectTypeList() {
		return projectTypeList;
	}

	/**
	 * 项目类型权限
	 */
	public void setProjectTypeList(List<Long> projectTypeList) {
		this.projectTypeList = projectTypeList;
	}

	public List<Long> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Long> orderList) {
		this.orderList = orderList;
	}

	public List<Long> getSourceList() {
		return sourceList;
	}

	public void setSourceList(List<Long> sourceList) {
		this.sourceList = sourceList;
	}

	public List<SysUserEntity> getSysUserList() {
		return sysUserList;
	}

	public void setSysUserList(List<SysUserEntity> sysUserList) {
		this.sysUserList = sysUserList;
	}

	@Override
	public String toString() {
		return "SysRoleEntity [roleId=" + roleId + ", roleName=" + roleName + ", remark=" + remark + ", createUserId="
				+ createUserId + ", deptId=" + deptId + ", deptName=" + deptName + ", menuIdList=" + menuIdList
				+ ", deptIdList=" + deptIdList + ", projectTypeList=" + projectTypeList + ", orderList=" + orderList
				+ ", sourceList=" + sourceList + ", sysUserList=" + sysUserList + ", createTime=" + createTime + "]";
	}

	
}
