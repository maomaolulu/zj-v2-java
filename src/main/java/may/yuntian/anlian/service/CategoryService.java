package may.yuntian.anlian.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.entity.CategoryEntity;

import java.util.List;

/**
 * 类型信息记录
 * 业务逻辑层接口
 *
 * @author LiXin
 * @date 2020-12-10
 */
public interface CategoryService extends IService<CategoryEntity> {

	/**
	 * 显示全部事业部业绩目标信息列表
	 * @return
	 */
	List<CategoryEntity> listAll();


	QueryWrapper<CategoryEntity> queryWrapper(CategoryEntity params);


	/**
	 * 根据条件查询类型信息ID列表
	 * @return
	 */
	List<Long> getCategoryIds(CategoryEntity params);

	/**
	 * 根据ID列表查询类型信息名称列表
	 */
	List<String> getCategoryNameByIds(List<Long> ids);

	/**
	 * 通过模块查询列表
	 * @param module
	 * @return
	 */
	List<CategoryEntity> listByModule(String module);
	
	/**
	 * 通过模块查询出没有子集的子集列表
	 * @param module
	 * @return
	 */
	List<CategoryEntity> getList(String module);


    /**
     * 获取无子集列表
     * @param module
     * @return
     */
    List<CategoryEntity> getListByPid(String module);
	
}