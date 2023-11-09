package may.yuntian.modules.sys_v2.controller;

import may.yuntian.common.annotation.SysLog;
import may.yuntian.modules.sys.service.SysRoleDeptService;
import may.yuntian.modules.sys_v2.entity.AjaxResult;
import may.yuntian.modules.sys_v2.entity.SysMenu;
import may.yuntian.modules.sys_v2.service.SysMenuV2Service;
import may.yuntian.modules.sys_v2.utils.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单控制层
 *
 * @author hjy
 * @date 2023/03/31 9:08
 */
@RestController
@RequestMapping("/sys/v2/menu/")
public class SysMenuV2Controller extends BaseController {

    private final SysMenuV2Service menuService;
    @Autowired
    private SysRoleDeptService sysRoleDeptService;

    public SysMenuV2Controller(SysMenuV2Service menuService) {
        this.menuService = menuService;
    }

    /**
     * 获取菜单列表
     */
    @RequiresPermissions("sys:menu:list")
    @GetMapping("/list")
    public AjaxResult list(SysMenu menu) {
        if (StringUtils.isEmpty(menu.getModuleName())) {
            menu.setModuleName("sys");
        }
        List<SysMenu> menus = menuService.selectMenuList(menu, getUserId());
        return AjaxResult.success(menus);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @RequiresPermissions("sys:menu:info")
    @GetMapping(value = "/{menuId}")
    public AjaxResult getInfo(@PathVariable Long menuId) {
        return AjaxResult.success(menuService.selectMenuById(menuId));
    }

    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/treeSelect")
    public AjaxResult treeSelect(SysMenu menu) {
        if (StringUtils.isEmpty(menu.getModuleName())) {
            menu.setModuleName("sys");
        }
        List<SysMenu> menus = menuService.selectMenuList(menu, getUserId());
        return AjaxResult.success(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeSelect/{roleId}")
    public AjaxResult roleMenuTreeSelect(@PathVariable("roleId") Long roleId) {
        AjaxResult ajax = AjaxResult.success();
        //查询角色对应的菜单
        ajax.put("menuIdList", menuService.selectMenuListByRoleId(roleId));
        //查询角色对应的部门
        List<Long> deptIdList = sysRoleDeptService.queryDeptIdList(new Long[]{roleId});
        ajax.put("deptIdList", deptIdList);
        //查询角色对应的项目类型权限
        List<Long> projectTypeList = sysRoleDeptService.queryProjectTypeList(new Long[]{roleId});
        ajax.put("projectTypeList", projectTypeList);
        //查询角色对应的项目隶属权限
        List<Long> orderList = sysRoleDeptService.queryOrderList(new Long[]{roleId});
        ajax.put("orderList", orderList);
        //查询角色对应的业务来源权限
        List<Long> sourceList = sysRoleDeptService.querySourceList(new Long[]{roleId});
        ajax.put("sourceList", sourceList);
        return ajax;
    }

    /**
     * 新增菜单
     */
    @RequiresPermissions("sys:menu:save")
    @SysLog("菜单管理-新增")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysMenu menu) {
        if ("0".equals(menuService.checkMenuNameUnique(menu))) {
            return AjaxResult.error("新增菜单'" + menu.getName() + "'失败，菜单名称已存在");
        }
        if ("0".equals(menuService.checkMenuPermUnique(menu))) {
            return AjaxResult.error("新增菜单'" + menu.getPerms() + "'失败，权限标识符已存在");
        }
        menu.setCreateBy(getUsername());
        return toAjax(menuService.insertMenu(menu));
    }

    /**
     * 修改菜单
     */
    @RequiresPermissions("sys:menu:update")
    @SysLog("菜单管理-修改")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysMenu menu) {
        if ("0".equals(menuService.checkMenuNameUnique(menu))) {
            return AjaxResult.error("修改菜单'" + menu.getName() + "'失败，菜单名称已存在");
        } else if (menu.getMenuId().equals(menu.getParentId())) {
            return AjaxResult.error("修改菜单'" + menu.getName() + "'失败，上级菜单不能选择自己");
        }
        menu.setUpdateBy(getUsername());
        return toAjax(menuService.updateMenu(menu));
    }

    /**
     * 删除菜单
     */
    @RequiresPermissions("sys:menu:delete")
    @SysLog("菜单管理-删除")
    @DeleteMapping("/{menuId}")
    public AjaxResult remove(@PathVariable("menuId") Long menuId) {
        if (menuService.hasChildByMenuId(menuId)) {
            return AjaxResult.error("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId)) {
            return AjaxResult.error("菜单已分配,不允许删除");
        }
        return toAjax(menuService.deleteMenuById(menuId));
    }

}
