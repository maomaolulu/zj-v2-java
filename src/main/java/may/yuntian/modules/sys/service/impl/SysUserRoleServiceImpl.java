package may.yuntian.modules.sys.service.impl;

import may.yuntian.common.utils.MapUtils;
import may.yuntian.modules.sys.dao.SysUserRoleDao;
import may.yuntian.modules.sys.entity.SysUserRoleEntity;
import may.yuntian.modules.sys.service.SysUserRoleService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;



/**
 * 用户与角色对应关系
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-16
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleDao, SysUserRoleEntity> implements SysUserRoleService {

	@Override
	public void saveOrUpdate(Long userId, List<Long> roleIdList) {
		//先删除用户与角色关系
		this.removeByMap(new MapUtils().put("user_id", userId));

		if(roleIdList == null || roleIdList.size() == 0){
			return ;
		}

		//保存用户与角色关系
		List<SysUserRoleEntity> list = new ArrayList<>(roleIdList.size());
		for(Long roleId : roleIdList){
			SysUserRoleEntity sysUserRoleEntity = new SysUserRoleEntity();
			sysUserRoleEntity.setUserId(userId);
			sysUserRoleEntity.setRoleId(roleId);

			list.add(sysUserRoleEntity);
		}
		this.saveBatch(list);
	}

	@Override
	public List<Long> queryRoleIdList(Long userId) {
		return baseMapper.queryRoleIdList(userId);
	}
	/**
	 * 根据角色ID，获取用户ID列表
	 */
	@Override
	public List<Long> queryUserIdList(Long roleId) {
		return baseMapper.queryUserIdList(roleId);
	}

	@Override
	public int deleteBatch(Long[] roleIds){
		return baseMapper.deleteBatch(roleIds);
	}
	
	/**
	 * 根据角色ID与用户ID获取对应关系信息
	 */
	public SysUserRoleEntity queryByRoleIdAndUserId(SysUserRoleEntity entity) {
		return baseMapper.queryByRoleIdAndUserId(entity);
	}
}
