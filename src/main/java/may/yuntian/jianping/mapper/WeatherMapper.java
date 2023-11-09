package may.yuntian.jianping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.jianping.entity.WeatherEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ZhuYiCheng
 * @date 2023/7/11 14:52
 */
@Mapper
public interface WeatherMapper extends BaseMapper<WeatherEntity> {
}
