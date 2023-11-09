package may.yuntian.jianping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.jianping.entity.WarningBasicEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 *职业危害警示标识设置基本信息
 * 数据持久层接口
 * 
 * @author LiXin
 * @data 2021-03-18
 */
@Mapper
public interface WarningBasicMapper extends BaseMapper<WarningBasicEntity> {
	
}
