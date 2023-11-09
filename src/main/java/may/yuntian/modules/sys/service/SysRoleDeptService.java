package may.yuntian.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.modules.sys.entity.SysRoleDeptEntity;

import java.util.List;


/**
 * 角色与部门对应关系
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2018-02-16
 */
public interface SysRoleDeptService extends IService<SysRoleDeptEntity> {
	
	void saveOrUpdate(Long roleId, List<Long> deptIdList, List<Long> projectTypeList, List<Long> orderList, List<Long> sourceList);
	
	/**
	 * 根据角色ID，获取部门ID列表
	 */
	List<Long> queryDeptIdList(Long[] roleIds) ;
	
	/**
	 * 根据角色ID，获取项目类型列表
	 * @param roleIds
	 * @return
	 */
	List<Long> queryProjectTypeList(Long[] roleIds);
	
	/**
	 * 根据角色ID，获取项目隶属列表
	 * @param roleIds
	 * @return
	 */
	List<Long> queryOrderList(Long[] roleIds);
	
	/**
	 * 根据角色ID，获取业务来源列表
	 * @param roleIds
	 * @return
	 */
	List<Long> querySourceList(Long[] roleIds);
	
	/**
	 * 根据用户获取部门权限列表
	 */
	List<Long> queryDeptIdListByUserId(Long userId);
	
	/**
	 * 根据用户，获取项目类型列表
	 * @param roleIds
	 * @return
	 */
	List<Long> queryProjectTypeListByUserId(Long userId);
	
	/**
	 * 根据角色ID数组，批量删除
	 */
	int deleteBatch(Long[] roleIds);
	
	/**
	 * 根据角色ID，获取角色对应的部门与项目类型列表
	 * @param roleId
	 * @return
	 */
	List<SysRoleDeptEntity> queryList(Long roleId);
	

	/**
	 * 根据用户，获取项目隶属列表
	 * @param roleIds
	 * @return
	 */
	List<Long> queryOrderListByUserId(Long userId);

	/**
	 * 根据用户，获取业务来源列表
	 * @param roleIds
	 * @return
	 */
	List<Long> querySourceListByUserId(Long userId);

}
