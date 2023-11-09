package may.yuntian.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.modules.sys.entity.SysUserRoleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户与角色对应关系
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-16
 */
@Mapper
public interface SysUserRoleDao extends BaseMapper<SysUserRoleEntity> {
	
	/**
	 * 根据用户ID，获取角色ID列表
	 */
	List<Long> queryRoleIdList(Long userId);
	
	/**
	 * 根据角色ID，获取用户ID列表
	 */
	List<Long> queryUserIdList(Long roleId);
	
	/**
	 * 根据角色ID与用户ID获取对应关系信息
	 */
	SysUserRoleEntity queryByRoleIdAndUserId(SysUserRoleEntity entity);


	/**
	 * 根据角色ID数组，批量删除
	 */
	int deleteBatch(Long[] roleIds);
}
