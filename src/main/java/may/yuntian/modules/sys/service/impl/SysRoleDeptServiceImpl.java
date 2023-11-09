package may.yuntian.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import may.yuntian.modules.sys.dao.SysRoleDeptDao;
import may.yuntian.modules.sys.dao.SysUserRoleDao;
import may.yuntian.modules.sys.entity.SysRoleDeptEntity;
import may.yuntian.modules.sys.service.SysRoleDeptService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 角色与部门对应关系
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2018-02-16
 */
@Service("sysRoleDeptService")
public class SysRoleDeptServiceImpl extends ServiceImpl<SysRoleDeptDao, SysRoleDeptEntity> implements SysRoleDeptService {
	@Autowired
	private SysUserRoleDao sysUserRoleDao;//用户与角色对应关系
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveOrUpdate(Long roleId, List<Long> deptIdList, List<Long> projectTypeList, List<Long> orderList, List<Long> sourceList) {
		//先删除角色与部门关系
		deleteBatch(new Long[]{roleId});

		if(deptIdList.size() == 0){
			return ;
		}

		//保存角色与数据权限部门关系
		for(Long deptId : deptIdList){
			SysRoleDeptEntity sysRoleDeptEntity = new SysRoleDeptEntity();
			sysRoleDeptEntity.setDeptId(deptId);
			sysRoleDeptEntity.setRoleId(roleId);
			sysRoleDeptEntity.setType(1);//数据类型(1:部门,2:项目类型,3:项目隶属,4:业务来源)
			this.save(sysRoleDeptEntity);
		}
		
		//保存角色与项目类型权限关系
		if(projectTypeList!=null) {
			for(Long projectTypeId : projectTypeList){
				SysRoleDeptEntity sysRoleDeptEntity = new SysRoleDeptEntity();
				sysRoleDeptEntity.setDeptId(projectTypeId);
				sysRoleDeptEntity.setRoleId(roleId);
				sysRoleDeptEntity.setType(2);//数据类型(1:部门,2:项目类型,3:项目隶属,4:业务来源)
				this.save(sysRoleDeptEntity);
			}
		}
		//保存角色与项目隶属权限关系
		if(orderList!=null) {
			for(Long orderId : orderList){
				SysRoleDeptEntity sysRoleDeptEntity = new SysRoleDeptEntity();
				sysRoleDeptEntity.setDeptId(orderId);
				sysRoleDeptEntity.setRoleId(roleId);
				sysRoleDeptEntity.setType(3);//数据类型(1:部门,2:项目类型,3:项目隶属,4:业务来源)
				this.save(sysRoleDeptEntity);
			}
		}
		//保存角色与业务来源权限关系
		if(sourceList!=null) {
			for(Long sourceId : sourceList){
				SysRoleDeptEntity sysRoleDeptEntity = new SysRoleDeptEntity();
				sysRoleDeptEntity.setDeptId(sourceId);
				sysRoleDeptEntity.setRoleId(roleId);
				sysRoleDeptEntity.setType(4);//数据类型(1:部门,2:项目类型,3:项目隶属,4:业务来源)
				this.save(sysRoleDeptEntity);
			}
		}
	}

	
	@Override
	public List<Long> queryDeptIdList(Long[] roleIds) {
		return baseMapper.queryDeptIdList(roleIds);
	}
	
	/**
	 * 根据角色ID，获取项目类型列表
	 * @param roleIds
	 * @return
	 */
	@Override
	public List<Long> queryProjectTypeList(Long[] roleIds) {
		return baseMapper.queryProjectTypeList(roleIds);
	}
	
	/**
	 * 根据角色ID，获取项目隶属列表
	 * @param roleIds
	 * @return
	 */
	@Override
	public List<Long> queryOrderList(Long[] roleIds) {
		return baseMapper.queryOrderList(roleIds);
	}
	
	/**
	 * 根据角色ID，获取业务来源列表
	 * @param roleIds
	 * @return
	 */
	@Override
	public List<Long> querySourceList(Long[] roleIds) {
		return baseMapper.querySourceList(roleIds);
	}
	
	/**
	 * 根据用户获取部门权限列表
	 */
	@Override
	public List<Long> queryDeptIdListByUserId(Long userId) {
		return baseMapper.queryDeptIdList(queryRoleIdList(userId));
	}
	
	/**
	 * 根据用户，获取项目隶属列表
	 * @param roleIds
	 * @return
	 */
	@Override
	public List<Long> queryOrderListByUserId(Long userId) {
		return baseMapper.queryOrderList(queryRoleIdList(userId));
	}
	
	/**
	 * 根据用户，获取业务来源列表
	 * @param roleIds
	 * @return
	 */
	@Override
	public List<Long> querySourceListByUserId(Long userId) {
		return baseMapper.querySourceList(queryRoleIdList(userId));
	}
	
	/**
	 * 根据用户，获取项目类型列表
	 * @param roleIds
	 * @return
	 */
	@Override
	public List<Long> queryProjectTypeListByUserId(Long userId) {
		return baseMapper.queryProjectTypeList(queryRoleIdList(userId));
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	private Long[] queryRoleIdList(Long userId) {
		List<Long> list = sysUserRoleDao.queryRoleIdList(userId);
		return list.toArray(new Long[list.size()]);
	}
	
	/**
	 * 根据角色ID，获取角色对应的部门与项目类型列表
	 * @param roleId
	 * @return
	 */
	@Override
	public List<SysRoleDeptEntity> queryList(Long roleId) {
		return baseMapper.selectList(new QueryWrapper<SysRoleDeptEntity>().eq("role_id", roleId));
	}

	@Override
	public int deleteBatch(Long[] roleIds){
		return baseMapper.deleteBatch(roleIds);
	}
}
