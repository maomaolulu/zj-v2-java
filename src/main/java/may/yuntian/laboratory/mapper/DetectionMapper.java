package may.yuntian.laboratory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.laboratory.entity.DetectionEntity;
import org.apache.ibatis.annotations.Mapper;


/**
 * 检测情况
 * 数据持久层接口
 * 
 * @author LiXin
 * @date 2020-12-17
 */
@Mapper
public interface DetectionMapper extends BaseMapper<DetectionEntity> {

}
