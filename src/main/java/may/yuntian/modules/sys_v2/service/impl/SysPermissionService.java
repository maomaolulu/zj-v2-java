package may.yuntian.modules.sys_v2.service.impl;

import may.yuntian.modules.sys_v2.entity.SysUser;
import may.yuntian.modules.sys_v2.service.SysMenuV2Service;
import may.yuntian.modules.sys_v2.service.SysRoleV2Service;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 用户权限处理
 *
 * @author ruoyi
 */
@Component
public class SysPermissionService {

    private final SysRoleV2Service roleService;

    private final SysMenuV2Service menuService;

    public SysPermissionService(SysRoleV2Service roleService, SysMenuV2Service menuService) {
        this.roleService = roleService;
        this.menuService = menuService;
    }

    /**
     * 获取角色数据权限
     *
     * @param user 用户信息
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(SysUser user) {
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        if (roleService.checkUserRole(user.getUserId(), "admin", "")) {
            roles.add("admin");
        } else {
            roles.addAll(roleService.selectRolePermissionByUserId(user.getUserId()));
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(SysUser user) {
        Set<String> perms = new HashSet<String>();
        // 管理员拥有所有权限
        if (roleService.checkUserRole(user.getUserId(), "admin", "")) {
            perms.add("*:*:*");
        } else {
            perms.addAll(menuService.selectMenuPermsByUserId(user.getUserId()));
        }
        return perms;
    }
}
