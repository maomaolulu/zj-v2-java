package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.jianping.entity.WeatherEntity;
import may.yuntian.jianping.mongodto.WeatherDto;
import may.yuntian.sys.utils.Result;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/7/11 14:53
 */
public interface WeatherService extends IService<WeatherEntity> {

    /**
     * 获取采样时的天气情况列表
     */
    List<WeatherDto> weatherLis1(Long project_id);


    /**
     * 修改采样时的天气情况
     */
    Result setWeather1(WeatherDto weatherDto);
}
