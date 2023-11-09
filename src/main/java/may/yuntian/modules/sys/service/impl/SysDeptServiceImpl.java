package may.yuntian.modules.sys.service.impl;

import may.yuntian.common.annotation.DataFilter;
import may.yuntian.modules.sys.dao.SysDeptDao;
import may.yuntian.modules.sys.entity.SysDeptEntity;
import may.yuntian.modules.sys.service.SysDeptService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("sysDeptService")
public class SysDeptServiceImpl extends ServiceImpl<SysDeptDao, SysDeptEntity> implements SysDeptService {
	
	@Override
	@DataFilter(subDept = true, user = false, tableAlias = "t1")
	public List<SysDeptEntity> queryList(Map<String, Object> params){
		return baseMapper.queryList(params);
	}
	
	/**
	 * 获取本部门及子部门信息列表
	 * @param deptId
	 * @return
	 */
	public List<SysDeptEntity> querySubList(Long deptId){
		System.out.println("获取本部门及子部门信息列表：deptId="+deptId);
		//部门ID列表
        List<Long> deptIdList = queryDeptIdAndSubList(deptId);
        
        List<SysDeptEntity> deptList = baseMapper.selectList(new QueryWrapper<SysDeptEntity>().in( (deptIdList!=null), "dept_id", deptIdList));
        System.out.println("获取本部门及子部门信息列表完成：deptList="+deptList.toString());
		return deptList;
	}

	@Override
	public List<Long> queryDetpIdList(Long parentId) {
		return baseMapper.queryDetpIdList(parentId);
	}
	
	/**
	 * 获取子部门ID
	 */
	@Override
	public List<Long> getSubDeptIdList(Long deptId){
		System.out.println("获取子部门deptId="+deptId);
		//部门及子部门ID列表
		List<Long> deptIdList = new ArrayList<>();

		//获取子部门ID
		List<Long> subIdList = queryDetpIdList(deptId);
		getDeptTreeList(subIdList, deptIdList);
		System.out.println("获取子部门deptId="+deptId+";完成：deptIdList="+deptIdList.toString());
		return deptIdList;
	}
	
	/**
	 * 获取本部门及子部门ID
	 */
	public List<Long> queryDeptIdAndSubList(Long deptId){
		//获取子部门ID
		List<Long> deptIdList = getSubDeptIdList(deptId);
		
		//用户子部门ID列表
		if(StringUtils.checkValNotNull(deptId)) {
			deptIdList.add(deptId);//添加本级部门，否则将只是子部门
		}
		
		return deptIdList;
	}
	
	/**
	 * 获取父级及顶级ID
	 */
	public List<Long> queryDeptAndPrentList(Long deptId) {
		Long aLong= deptId;
		ArrayList<Long> deptIdList = new ArrayList<>();
		for (int i = 0;; i++) {
		   Long pDeptId = baseMapper.queryDetpId(aLong);
		   if(pDeptId==0)
			  break;
		   deptIdList.add(pDeptId);
		   aLong=pDeptId;
		}
		
		return deptIdList;
	}

	/**
	 * 递归
	 */
	private void getDeptTreeList(List<Long> subIdList, List<Long> deptIdList){
		System.out.println("部门递归：subIdList="+subIdList.toString()+" ;deptIdList="+deptIdList.toString());
		for(Long deptId : subIdList){
			List<Long> list = queryDetpIdList(deptId);
			if(list.size() > 0){
				getDeptTreeList(list, deptIdList);
			}

			deptIdList.add(deptId);
		}
	}
	
	/**
	 * 根据部门名称获取部门ID
	 * @param name
	 * @return
	 */
	@Override
	public Long getDeptIdByDeptName(String name) {
		SysDeptEntity sysDeptEntity = baseMapper.selectOne(new QueryWrapper<SysDeptEntity>()
				.eq("name", name)
				.last("limit 1")
				);
		Long deptId = sysDeptEntity.getDeptId();
		return deptId;
	}
	
}
