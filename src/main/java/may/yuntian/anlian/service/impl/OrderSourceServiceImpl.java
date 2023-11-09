package may.yuntian.anlian.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.OrderSourceEntity;
import may.yuntian.anlian.mapper.OrderSourceMapper;
import may.yuntian.anlian.service.OrderSourceService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *项目隶属来源表
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @data 2021-03-22
 */
@Service("orderSourceService")
public class OrderSourceServiceImpl extends ServiceImpl<OrderSourceMapper, OrderSourceEntity> implements OrderSourceService {

	
	/**
	 * 获取项目隶属 type为3的列表
	 * @return
	 */
	public List<OrderSourceEntity> getOrderList() {
		List<OrderSourceEntity> orderList = baseMapper.selectList(new QueryWrapper<OrderSourceEntity>()
				.eq("type", 3)
				);
		
		return orderList;
	}
	
	/**
	 * 获取业务来源 type为4的列表
	 * @return
	 */
	public List<OrderSourceEntity> getSourceListList() {
		List<OrderSourceEntity> sourceListList = baseMapper.selectList(new QueryWrapper<OrderSourceEntity>()
				.eq("type", 4)
				);
		
		return sourceListList;
	}
	
	/**
	 * 根据ID列表查询类型信息名称列表
	 */
	public List<String> getOrderSourceByIds(List<Long> ids) {
		List list = baseMapper.selectObjs(new QueryWrapper<OrderSourceEntity>().select("order_source").in("id", ids));
		return (List<String>)list;
	}
  
}
