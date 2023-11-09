package may.yuntian.jianping.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.jianping.mongodto.WeatherDto;
import may.yuntian.jianping.service.WeatherService;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/7/10 15:38
 */
@RestController
@Api(tags="检测时气象条件")
@RequestMapping("/sample_plan")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;


    /**
     * 获取采样时的天气情况列表
     */
    @GetMapping("/weather_lis")
    @ApiOperation("获取采样时的天气情况列表")
    public Result weatherLis1(Long project_id) {

        List<WeatherDto> weatherList = weatherService.weatherLis1(project_id);
        return Result.ok("信息获取成功", weatherList);
    }


    /**
     * 修改采样时的天气情况
     */
    @PostMapping("/set_weather")
    @ApiOperation("修改采样时的天气情况")
    @SysLog(value = "修改采样时的天气情况")
    public Result setWeather1(@RequestBody WeatherDto weatherDto) {

        return weatherService.setWeather1(weatherDto);
    }

}
