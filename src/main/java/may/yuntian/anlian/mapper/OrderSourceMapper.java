package may.yuntian.anlian.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.anlian.entity.OrderSourceEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 *项目隶属来源表
 * 数据持久层接口
 * 
 * @author LiXin
 * @data 2021-03-22
 */
@Mapper
public interface OrderSourceMapper extends BaseMapper<OrderSourceEntity> {
	
}
