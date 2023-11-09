package may.yuntian.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.modules.sys.entity.SysRoleEntity;

import java.util.List;
import java.util.Map;


/**
 * 角色
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-16
 */
public interface SysRoleService extends IService<SysRoleEntity> {

	PageUtils queryPage(Map<String, Object> params);

	boolean save(SysRoleEntity role);

	void update(SysRoleEntity role);

	void deleteBatch(Long[] roleIds);
	
	/**
	 * 获取管理员信息
	 * @return
	 */
	List<SysRoleEntity> getList(); 

	
	/**
	 * 查询用户创建的角色ID列表
	 */
	List<Long> queryRoleIdList(Long createUserId);
}
