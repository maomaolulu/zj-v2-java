package may.yuntian.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.modules.sys.entity.SysMenuEntity;

import java.util.List;


/**
 * 菜单管理
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-16
 */
public interface SysMenuService extends IService<SysMenuEntity> {

	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 * @param menuIdList  用户菜单ID
	 */
	List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList);

	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 */
	List<SysMenuEntity> queryListParentId(Long parentId);
	
	/**
	 * 获取不包含按钮的菜单列表
	 */
	List<SysMenuEntity> queryNotButtonList();
	
	/**
	 * 获取用户菜单列表
	 */
	List<SysMenuEntity> getUserMenuList(Long userId);

	/**
	 * 删除
	 */
	void delete(Long menuId);

    /**
     * 根据系统类型获取菜单
     * @param userId
     * @param type
     * @return
     */
    List<SysMenuEntity> getMenuListByUidAndtype(Long userId, String type);
}
