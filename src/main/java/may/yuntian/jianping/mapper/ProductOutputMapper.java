package may.yuntian.jianping.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.jianping.entity.ProductOutputEntity;

/**
 * 主要产品与年产量
 * 数据持久层接口
 * 
 * @author LiXin
 * @email ''
 * @date 2022-11-10 15:52:36
 */
@Mapper
public interface ProductOutputMapper extends BaseMapper<ProductOutputEntity> {
	
}
