package may.yuntian.modules.sys_v2.utils;

import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys_v2.entity.SysUser;

import static org.apache.shiro.SecurityUtils.getSubject;

/**
 * shiro工具
 *
 * @author hjy
 * @date 2023/3/31 14:14
 */
public class ShiroUtilsV2 {
    /**
     * 获取用户信息
     */
    public static SysUser getUserEntity() {
        SysUserEntity temp = (SysUserEntity) getSubject().getPrincipal();
        SysUser sysUser = new SysUser();
        sysUser.setSubjection(temp.getSubjection());
        sysUser.setUserId(temp.getUserId());
        sysUser.setUserName(temp.getUsername());
        sysUser.setEmail(temp.getEmail());
        sysUser.setJobNum(temp.getJobNum());
        sysUser.setDeptId(temp.getDeptId());
        return sysUser;
    }

    /**
     * 获取用户姓名id
     */
    public static Long getUserId() {
        return getUserEntity().getUserId();
    }

    /**
     * 获取用户所属部门
     */
    public static Long getDeptId() {
        return getUserEntity().getDeptId();
    }

    /**
     * 获取用户姓名
     */
    public static String getUserName() {
        return getUserEntity().getUserName();
    }

    /**
     * 获取当前用户隶属公司
     */
    public static String getCompanyName() {
        return getUserEntity().getSubjection();
    }

    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }
}
