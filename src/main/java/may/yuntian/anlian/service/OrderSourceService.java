package may.yuntian.anlian.service;


import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.entity.OrderSourceEntity;

import java.util.List;

/**
 *项目隶属来源表
 * 业务逻辑层接口
 * 
 * @author LiXin
 * @data 2021-03-22
 */
public interface OrderSourceService extends IService<OrderSourceEntity> {


	/**
	 * 获取项目隶属 type为3的列表
	 * @return
	 */
	List<OrderSourceEntity> getOrderList();
	
	/**
	 * 获取业务来源 type为4的列表
	 * @return
	 */
	List<OrderSourceEntity> getSourceListList();
	
	/**
	 * 根据ID列表查询类型信息名称列表
	 */
	List<String> getOrderSourceByIds(List<Long> ids);
	
}

