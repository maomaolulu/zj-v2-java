package may.yuntian.modules.sys.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.modules.sys.entity.SysRoleDeptEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色与部门对应关系
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-16
 */
@Mapper
public interface SysRoleDeptDao extends BaseMapper<SysRoleDeptEntity> {
	
	/**
	 * 根据角色ID，获取部门ID列表
	 */
	List<Long> queryDeptIdList(Long[] roleIds);
	
	/**
	 * 根据角色ID，获取项目类型列表
	 */
	List<Long> queryProjectTypeList(Long[] roleIds);
	
	/**
	 * 根据角色ID，获取项目隶属列表
	 */
	List<Long> queryOrderList(Long[] roleIds);
	
	/**
	 * 根据角色ID，获取业务来源列表
	 */
	List<Long> querySourceList(Long[] roleIds);

	/**
	 * 根据角色ID数组，批量删除
	 */
	int deleteBatch(Long[] roleIds);
}
