package may.yuntian.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.modules.sys.entity.SysDeptEntity;

import java.util.List;
import java.util.Map;

/**
 * 部门管理
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2018-02-16
 */
public interface SysDeptService extends IService<SysDeptEntity> {

	List<SysDeptEntity> queryList(Map<String, Object> map);

	/**
	 * 查询子部门ID列表
	 * @param parentId  上级部门ID
	 */
	List<Long> queryDetpIdList(Long parentId);
	
	/**
	 * 获取本部门及子部门ID
	 */
	List<Long> queryDeptIdAndSubList(Long deptId);
	
	/**
	 * 获取本部门及子部门信息列表
	 * @param deptId
	 * @return
	 */
	List<SysDeptEntity> querySubList(Long deptId);

	/**
	 * 获取子部门ID，用于数据过滤
	 */
	List<Long> getSubDeptIdList(Long deptId);
	
	/**
	 * 获取父级及顶级ID
	 */
	List<Long> queryDeptAndPrentList(Long deptId);
	
	/**
	 * 根据部门名称获取部门ID
	 * @param name
	 * @return
	 */
	Long getDeptIdByDeptName(String name);

}
