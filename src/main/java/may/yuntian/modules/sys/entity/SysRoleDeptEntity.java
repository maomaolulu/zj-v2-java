package may.yuntian.modules.sys.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * 角色与部门对应关系
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-16
 */
@TableName("sys_role_dept")
public class SysRoleDeptEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@TableId
	private Long id;

	/**
	 * 角色ID
	 */
	private Long roleId;

	/**
	 * 部门ID
	 */
	private Long deptId;
	
	/**
	 * 数据类型(1:部门,2:项目类型)
	 */
	private Integer type;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	/**
	 * 获取：数据类型(1:部门,2:项目类型)
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：数据类型(1:部门,2:项目类型)
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	
}
