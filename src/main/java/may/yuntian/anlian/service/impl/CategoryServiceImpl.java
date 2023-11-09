package may.yuntian.anlian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.CategoryEntity;
import may.yuntian.anlian.mapper.CategoryMapper;
import may.yuntian.anlian.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类型信息记录
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @date 2020-12-10
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryEntity> implements CategoryService {

	
    
    public QueryWrapper<CategoryEntity> queryWrapper(CategoryEntity params) {
    	String name = params.getName();//类型名称    模糊搜索
    	String module = params.getModule();
    	
    	QueryWrapper<CategoryEntity> queryWrapper = new QueryWrapper<CategoryEntity>()
                .eq(StringUtils.isNotBlank(module),"module",module)
                .like(StringUtils.isNotBlank(name),"name", name);

        return queryWrapper;
    }
    
	/**
	 * 根据条件查询类型信息ID列表
	 */
	public List<Long> getCategoryIds(CategoryEntity params) {
		  List list = baseMapper.selectObjs(queryWrapper(params).select("id"));
		  
		  return (List<Long>)list;
	}
	
	/**
	 * 根据ID列表查询类型信息名称列表
	 */
	public List<String> getCategoryNameByIds(List<Long> ids) {
		List list = baseMapper.selectObjs(new QueryWrapper<CategoryEntity>().select("name").in("id", ids));
		return (List<String>)list;
	}
	
	
    /**
     * 显示全部类型信息记录列表
     * @return
     */
    public List<CategoryEntity> listAll() {
    	List<CategoryEntity> list = this.list();
    	return list;
    }
    
    /**
     * 通过模块查询列表
     */
    public List<CategoryEntity> listByModule(String module){
		List<CategoryEntity> categoryList = baseMapper.selectList(new QueryWrapper<CategoryEntity>()
				.eq(StringUtils.isNotBlank(module),"module",module)
				);
    	
    	return categoryList;
    }
    
    /**
	 * 通过模块查询出没有子集的子集列表
	 * @param module
	 * @return
	 */
    public List<CategoryEntity> getList(String module){
    	List<CategoryEntity> categoryList = baseMapper.getList(module);
		return categoryList;
    }
    
    /**
     * 获取无子集列表
     * @param module
     * @return
     */
    public List<CategoryEntity> getListByPid(String module){
    	List<CategoryEntity> categoryEntityList = baseMapper.selectList(new QueryWrapper<CategoryEntity>()
    			.eq("pid", 0)
    			.eq(StringUtils.isNotBlank(module),"module",module)
    			);
    	return categoryEntityList;
    }
    

}
