package may.yuntian.anlian.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.anlian.entity.AgreementEntity;

/**
 * 项目对应的委托协议列表
 * 数据持久层接口
 * 
 * @author LiXin
 * @email ''
 * @date 2023-06-27 14:25:05
 */
@Mapper
public interface AgreementMapper extends BaseMapper<AgreementEntity> {
	
}
