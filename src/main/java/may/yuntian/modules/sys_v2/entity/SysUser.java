package may.yuntian.modules.sys_v2.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 系统用户
 *
 * @author hjy
 * @date 2022/7/22 13:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUser extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 用户账号
     */
    private String userName;
    /**
     * 员工编号
     */
    private String jobNum;
    /**
     * 隶属公司
     */
    private String subjection;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 用户性别（1男，2女）
     */
    private Integer sex;

    /**
     * 密码
     */
    private String password;
    /**
     * 盐
     */
    private String salt;

    /**
     * 状态  0：禁用   1：正常  2:离职
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 部门对象
     */
    private SysDept dept;

    /**
     * 角色对象
     */
    private List<SysRole> roles;

    /**
     * 角色组
     */
    private Long[] roleIds;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * token数据
     */
    private String token;

    public SysUser() {

    }

    public SysUser(Long userId) {
        this.userId = userId;
    }

    public boolean isAdmin() {
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }
}
