package may.yuntian.modules.sys_v2.controller;

import may.yuntian.modules.sys_v2.annotation.AuthCode;
import may.yuntian.modules.sys_v2.entity.AjaxResult;
import may.yuntian.modules.sys_v2.entity.SysMenu;
import may.yuntian.modules.sys_v2.entity.SysUser;
import may.yuntian.modules.sys_v2.entity.vo.AuthCodeVo;
import may.yuntian.modules.sys_v2.entity.vo.RouterVo;
import may.yuntian.modules.sys_v2.service.SysMenuV2Service;
import may.yuntian.modules.sys_v2.service.impl.SysPermissionService;
import may.yuntian.modules.sys_v2.utils.ShiroUtilsV2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 登录信息获取
 *
 * @author hjy
 */
@RestController
public class SysLoginV2Controller {

    private final SysMenuV2Service menuService;

    private final SysPermissionService permissionService;

    public SysLoginV2Controller(SysMenuV2Service menuService, SysPermissionService permissionService) {
        this.menuService = menuService;
        this.permissionService = permissionService;
    }

    /**
     * 获取用户及路由信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo(String type) {
        SysUser user = ShiroUtilsV2.getUserEntity();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId(), type);
        List<RouterVo> menuList = menuService.buildMenus(menus);
        ajax.put("menuList", menuList);
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        Long userId = ShiroUtilsV2.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId,"");
        return AjaxResult.success(menuService.buildMenus(menus));
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("test")
    @AuthCode(url = "testMenu", system = "sys")
    public AjaxResult getTest(AuthCodeVo authCode) {
        return AjaxResult.success(authCode);
    }
}
