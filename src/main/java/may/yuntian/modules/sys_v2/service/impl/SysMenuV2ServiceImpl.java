package may.yuntian.modules.sys_v2.service.impl;


import may.yuntian.modules.sys_v2.entity.SysMenu;
import may.yuntian.modules.sys_v2.entity.SysRole;
import may.yuntian.modules.sys_v2.entity.TreeSelect;
import may.yuntian.modules.sys_v2.entity.vo.RouterVo;
import may.yuntian.modules.sys_v2.mapper.SysMenuMapper;
import may.yuntian.modules.sys_v2.mapper.SysRoleMapper;
import may.yuntian.modules.sys_v2.mapper.SysRoleMenuMapper;
import may.yuntian.modules.sys_v2.mapper.SysUserRoleMapper;
import may.yuntian.modules.sys_v2.service.SysMenuV2Service;
import may.yuntian.modules.sys_v2.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单接口实现层
 *
 * @author hjy
 * @date 2023/03/31 9:08
 */
@Service()
public class SysMenuV2ServiceImpl implements SysMenuV2Service {

    private final SysMenuMapper menuMapper;

    private final SysRoleMapper roleMapper;

    private final SysRoleMenuMapper roleMenuMapper;

    private final SysUserRoleMapper userRoleMapper;

    public SysMenuV2ServiceImpl(SysMenuMapper menuMapper, SysRoleMapper roleMapper, SysRoleMenuMapper roleMenuMapper, SysUserRoleMapper userRoleMapper) {
        this.menuMapper = menuMapper;
        this.roleMapper = roleMapper;
        this.roleMenuMapper = roleMenuMapper;
        this.userRoleMapper = userRoleMapper;
    }

    /**
     * 根据用户查询系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuList(Long userId) {
        return selectMenuList(new SysMenu(), userId);
    }

    /**
     * 获取菜单列表
     *
     * @param menu   菜单信息
     * @param userId 登陆人id
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuList(SysMenu menu, Long userId) {
        List<SysMenu> menuList = null;
        // 管理员显示所有菜单信息
        if (userRoleMapper.checkUserRole(userId, "admin", "")) {
            menuList = menuMapper.selectMenuList(menu);
        } else {
            menu.getParams().put("userId", userId);
            menuList = menuMapper.selectMenuListByUserId(menu);
        }
        return menuList;
    }

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    @Override
    public SysMenu selectMenuById(Long menuId) {
        return menuMapper.selectMenuById(menuId);
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByUserId(Long userId) {
        List<String> perms = menuMapper.selectMenuPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        SysRole role = roleMapper.selectById(roleId);
        return menuMapper.selectMenuListByRoleId(roleId, role.isMenuCheckStrictly());
    }

    /**
     * 新增保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int insertMenu(SysMenu menu) {
        return menuMapper.insertMenu(menu);
    }

    /**
     * 修改保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int updateMenu(SysMenu menu) {
        return menuMapper.updateMenu(menu);
    }

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户名称
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuTreeByUserId(Long userId, String type) {
        List<SysMenu> menus = null;
        if (userRoleMapper.checkUserRole(userId, "admin", type)) {
            menus = menuMapper.selectMenuTreeAll(type);
        } else {
            menus = menuMapper.selectMenuTreeByUserId(userId, type);
        }
        return getChildPerms(menus, 0);
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, int parentId) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext(); ) {
            SysMenu t = (SysMenu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean hasChildByMenuId(Long menuId) {
        int result = menuMapper.hasChildByMenuId(menuId);
        return result > 0;
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVo> buildMenus(List<SysMenu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setMenuId(menu.getMenuId());
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(menu.getName());
            router.setIcon(menu.getIcon());
            router.setModuleName(menu.getModuleName());
            router.setUrl(menu.getUrl());
            router.setType(menu.getType());
            List<SysMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && (1 == menu.getType() || 0 == menu.getType())) {
                router.setList(buildMenus(cMenus));
            } else if (isMenuFrame(menu)) {
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setMenuId(menu.getMenuId());
                children.setHidden("1".equals(menu.getVisible()));
                children.setUrl(menu.getUrl());
                children.setName(menu.getName());
                children.setModuleName(menu.getModuleName());
                children.setIcon(menu.getIcon());
                children.setType(menu.getType());
                childrenList.add(children);
                router.setList(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean checkMenuExistRole(Long menuId) {
        int result = roleMenuMapper.checkMenuExistRole(menuId);
        return result > 0;
    }

    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public int deleteMenuById(Long menuId) {
        return menuMapper.deleteMenuById(menuId);
    }

    /**
     * 校验权限名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public String checkMenuPermUnique(SysMenu menu) {
        Long menuId = StringUtils.isNull(menu.getMenuId()) ? -1L : menu.getMenuId();
        SysMenu info = menuMapper.checkMenuPermUnique(menu.getPerms(), menu.getParentId(), menu.getModuleName());
        if (StringUtils.isNotNull(info) && info.getMenuId().longValue() != menuId.longValue()) {
            return "0";
        }
        return "1";
    }

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public String checkMenuNameUnique(SysMenu menu) {
        Long menuId = StringUtils.isNull(menu.getMenuId()) ? -1L : menu.getMenuId();
        SysMenu info = menuMapper.checkMenuNameUnique(menu.getName(), menu.getParentId(), menu.getModuleName());
        if (StringUtils.isNotNull(info) && info.getMenuId().longValue() != menuId.longValue()) {
            return "0";
        }
        return "1";
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus) {
        List<SysMenu> menuTrees = buildMenuTree(menus);
        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        List<Long> tempList = new ArrayList<Long>();
        for (SysMenu menu : menus) {
            tempList.add(menu.getMenuId());
        }
        for (Iterator<SysMenu> iterator = menus.iterator(); iterator.hasNext(); ) {
            SysMenu menu = (SysMenu) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId())) {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext()) {
            SysMenu n = (SysMenu) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0;
    }

    /**
     * 内链域名特殊字符替换
     *
     * @return
     */
    public String innerLinkReplaceEach(String path) {
        return StringUtils.replaceEach(path, new String[]{"http://", "https://"},
                new String[]{"", ""});
    }


    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(SysMenu menu) {
        String routerName = StringUtils.capitalize(menu.getUrl());
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = menu.getUrl();
        // 内链打开外网方式
//        if (menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
//            routerPath = innerLinkReplaceEach(routerPath);
//        }
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && "1".equals(menu.getType())) {
            routerPath = "/" + menu.getUrl();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(SysMenu menu) {
        return menu.getParentId().intValue() == 0 && 1 == menu.getType();
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(SysMenu menu) {
        return StringUtils.startsWithAny(menu.getUrl(), "http://", "https://");
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysMenu menu) {
        return menu.getParentId().intValue() != 0 && 1 == menu.getType();
    }

}
