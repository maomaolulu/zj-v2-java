package may.yuntian.modules.sys_v2.controller;

import may.yuntian.modules.sys.service.SysRoleDeptService;
import may.yuntian.modules.sys_v2.entity.*;
import may.yuntian.modules.sys_v2.entity.vo.SelectVo;
import may.yuntian.modules.sys_v2.entity.vo.SysRoleVo;
import may.yuntian.modules.sys_v2.service.SysRoleV2Service;
import may.yuntian.modules.sys_v2.service.SysUserV2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色控制层
 *
 * @author hjy
 * @date 2023/3/31 10:55
 */
@RestController
@RequestMapping("/sys/v2/role/")
public class SysRoleV2Controller extends BaseController {

    private final SysRoleV2Service roleService;

    private final SysUserV2Service userService;
    @Autowired
    private SysRoleDeptService sysRoleDeptService;

    public SysRoleV2Controller(SysRoleV2Service roleService, SysUserV2Service userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    /**
     * 角色列表
     */
//    @RequiresPermissions("system:role:list")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody SysRole role) {
        //查询某人是否拥有某权限
        if (!roleService.checkUserRole(getUserId(), "admin", "")) {
            role.setDataCompany(getCompanyName());
        }
        startPage();
        List<SysRole> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }


    /**
     * 根据角色编号获取详细信息
     */
//    @RequiresPermissions("system:role:query")
    @GetMapping(value = "/{roleId}")
    public AjaxResult getInfo(@PathVariable Long roleId) {
        roleService.checkRoleDataScope(roleId);
        return AjaxResult.success(roleService.selectRoleById(roleId));
    }

    /**
     * 新增角色
     */
//    @RequiresPermissions("system:role:add")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysRoleVo roleVo) {
        SysRole role = roleVo.getRole();
        if ("1".equals(roleService.checkRoleNameUnique(role))) {
            return AjaxResult.error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if ("1".equals(roleService.checkRoleKeyUnique(role))) {
            return AjaxResult.error("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setCreateBy(getUsername());
        roleVo.setRole(role);
        return toAjax(roleService.insertRole(roleVo));

    }

    /**
     * 修改保存角色
     */
//    @RequiresPermissions("system:role:edit")
    @PutMapping
//    public AjaxResult edit(@Validated @RequestBody SysRole role,RequestBody SysRoleVo roleVo) {
    public AjaxResult edit(@Validated @RequestBody SysRoleVo roleVo) {
        SysRole role = roleVo.getRole();
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        if ("1".equals(roleService.checkRoleNameUnique(role))) {
            return AjaxResult.error("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if ("1".equals(roleService.checkRoleKeyUnique(role))) {
            return AjaxResult.error("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setUpdateBy(getUsername());

        //保存角色与部门关系
        if (roleVo.getDeptIdList() != null) {
            sysRoleDeptService.saveOrUpdate(role.getRoleId(), roleVo.getDeptIdList(), roleVo.getProjectTypeList(), roleVo.getOrderList(), roleVo.getSourceList());
        }

        if (roleService.updateRole(role) > 0) {
            return AjaxResult.success();
        }
        return AjaxResult.error("修改角色'" + role.getRoleName() + "'失败，请联系管理员");
    }

    /**
     * 修改保存数据权限
     */
//    @RequiresPermissions("system:role:edit")
    @PutMapping("/dataScope")
    public AjaxResult dataScope(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        return toAjax(roleService.authDataScope(role));
    }

    /**
     * 状态修改
     */
//    @RequiresPermissions("system:role:edit")
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        role.setUpdateBy(getUsername());
        return toAjax(roleService.updateRoleStatus(role));
    }

    /**
     * 删除角色
     */
//    @RequiresPermissions("system:role:remove")
    @DeleteMapping("/{roleIds}")
    public AjaxResult remove(@PathVariable Long[] roleIds) {
        return toAjax(roleService.deleteRoleByIds(roleIds));
    }

    /**
     * 获取角色选择框列表
     */
//    @RequiresPermissions("system:role:query")
    @GetMapping("/optionSelect")
    public AjaxResult optionSelect() {
        return AjaxResult.success(roleService.selectRoleAll());
    }

    /**
     * 查询已分配用户角色列表
     */
//    @RequiresPermissions("system:role:list")
    @GetMapping("/authUser/allocatedList")
    public TableDataInfo allocatedList(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectAllocatedList(user);
        return getDataTable(list);
    }

    /**
     * 查询未分配用户角色列表
     */
//    @RequiresPermissions("system:role:list")
    @GetMapping("/authUser/unallocatedList")
    public TableDataInfo unallocatedList(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUnallocatedList(user);
        return getDataTable(list);
    }

    /**
     * 取消授权用户
     */
//    @RequiresPermissions("system:role:edit")
    @PutMapping("/authUser/cancel")
    public AjaxResult cancelAuthUser(@RequestBody SysUserRole userRole) {
        return toAjax(roleService.deleteAuthUser(userRole));
    }

    /**
     * 批量取消授权用户
     */
//    @RequiresPermissions("system:role:edit")
    @PutMapping("/authUser/cancelAll")
    public AjaxResult cancelAuthUserAll(@RequestBody SelectVo vo) {
        return toAjax(roleService.deleteAuthUsers(vo.getRoleId(), vo.getUserIds()));
    }

    /**
     * 批量选择用户授权
     */
//    @RequiresPermissions("system:role:edit")
    @PutMapping("/authUser/selectAll")
    public AjaxResult selectAuthUserAll(@RequestBody SelectVo vo) {
        roleService.checkRoleDataScope(vo.getRoleId());
        return toAjax(roleService.insertAuthUsers(vo.getRoleId(), vo.getUserIds()));
    }

}
