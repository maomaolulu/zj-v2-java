package may.yuntian.commission.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.commission.entity.PerformanceEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 人员绩效表
 * 数据持久层接口
 * 
 * @author LiXin
 * @email ''
 * @date 2022-06-17 09:54:17
 */
@Mapper
public interface PerformanceMapper extends BaseMapper<PerformanceEntity> {
	
}
