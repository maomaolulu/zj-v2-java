package may.yuntian.jianping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.jianping.entity.LabAcceptProjectEntity;
import may.yuntian.jianping.entity.WeatherEntity;
import may.yuntian.jianping.mapper.WeatherMapper;
import may.yuntian.jianping.mongodto.BatchGatherLis;
import may.yuntian.jianping.mongodto.Scene;
import may.yuntian.jianping.mongodto.WeatherDto;
import may.yuntian.jianping.mongodto.ZjPlanRecord;
import may.yuntian.jianping.service.LabAcceptProjectService;
import may.yuntian.jianping.service.WeatherService;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ZhuYiCheng
 * @date 2023/7/11 14:57
 */
@Service("weatherService")
public class WeatherServiceImpl extends ServiceImpl<WeatherMapper, WeatherEntity> implements WeatherService {


    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private LabAcceptProjectService labAcceptProjectService;



    /**
     * 获取采样时的天气情况列表
     */
    @Override
    public List<WeatherDto> weatherLis1(Long project_id) {

        List<WeatherEntity> weatherList = this.list(new QueryWrapper<WeatherEntity>().eq("project_id", project_id));
        List<WeatherDto> newWeatherList = new ArrayList<>();
        Map<String, Integer> weatherMap = new HashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (WeatherEntity weatherEntity : weatherList) {
            WeatherDto wDict = new WeatherDto();
            wDict.setId(weatherEntity.getId());
            wDict.setProject_id(weatherEntity.getProjectId());
            wDict.setWeather(weatherEntity.getWeather());
            wDict.setTemperature_min(weatherEntity.getTemperatureMin());
            wDict.setTemperature_max(weatherEntity.getTemperatureMax());
            wDict.setHumidity_rh(weatherEntity.getHumidityRh());
            wDict.setWind_speed(weatherEntity.getWindSpeed());
            wDict.setPressure(weatherEntity.getPressure());
            wDict.setGather_date(weatherEntity.getGatherDate() != null ? sdf.format(weatherEntity.getGatherDate()) : "");
            wDict.setUpdatetime(weatherEntity.getUpdatetime() != null ? sdf1.format(weatherEntity.getUpdatetime()) : "");
            wDict.setCreatetime(sdf1.format(weatherEntity.getCreatetime()));
            wDict.setTotal_record_num(0);
            wDict.setSample_record_num(0);
            wDict.setSample_delivery_status(0);
            weatherMap.put(wDict.getGather_date(), newWeatherList.size());
            newWeatherList.add(wDict);
        }

        List<ZjPlanRecord> recordList = mongoTemplate.find(new Query(Criteria.where("project_id").is(project_id)),
                ZjPlanRecord.class);
        List<LabAcceptProjectEntity> acceptProject = labAcceptProjectService
                .list(new QueryWrapper<LabAcceptProjectEntity>().eq("project_id", project_id));

        for (ZjPlanRecord zjPlanRecord : recordList) {
            List<BatchGatherLis> batch_gather_lis = zjPlanRecord.getBatch_gather_lis();
            for (BatchGatherLis batch_gather : batch_gather_lis) {
                String gather_date = batch_gather.getGather_date();
                if (weatherMap.containsKey(gather_date)) {
                    Integer wIndex = weatherMap.get(gather_date);
                    WeatherDto weatherDto = newWeatherList.get(wIndex);

                    weatherDto.setTotal_record_num(weatherDto.getTotal_record_num() + 1);
                    List<String> print_bar_code_lis = batch_gather.getPrint_bar_code_lis();
                    if (!print_bar_code_lis.isEmpty()) {
                        weatherDto.setSample_record_num(weatherDto.getSample_record_num() + 1);
                    }
                }
            }
        }
        for (LabAcceptProjectEntity labAcceptProjectEntity : acceptProject) {
            String gatherDateStr = sdf.format(labAcceptProjectEntity.getGatherDate());
            Integer wIndex = weatherMap.get(gatherDateStr);
            WeatherDto weatherDto = newWeatherList.get(wIndex);
            if (labAcceptProjectEntity.getSampleDeliveryStatus() == 1) {
                weatherDto.setSample_delivery_status(1);
            } else if (labAcceptProjectEntity.getSampleDeliveryStatus() > 1) {
                weatherDto.setSample_delivery_status(2);
            }
        }
        return newWeatherList;
    }


    /**
     * 修改采样时的天气情况
     */
    @Override
    public Result setWeather1(WeatherDto weatherDto) {
        Long wId = weatherDto.getId();
        Long projectId = weatherDto.getProject_id();

        // 查询天气情况
        WeatherEntity weatherInfo = this.getById(wId);
        if (weatherInfo == null || !Objects.equals(weatherInfo.getProjectId(), projectId)) {
            return Result.error(403, "需要修改的天气情况信息不存在");
        }
        String temperatureMin = weatherDto.getTemperature_min() != null ? weatherDto.getTemperature_min() : "";
        String temperatureMax = weatherDto.getTemperature_max() != null ? weatherDto.getTemperature_max() : "";
        String pressure = weatherDto.getPressure() != null ? weatherDto.getPressure() : "";
        String humidity = weatherDto.getHumidity_rh() != null ? weatherDto.getHumidity_rh() : "";
        String weather = weatherDto.getWeather() != null ? weatherDto.getWeather() : "";
        String wind_speed = weatherDto.getWind_speed() != null ? weatherDto.getWind_speed() : "";
        weatherInfo.setWeather(weather);
        weatherInfo.setTemperatureMin(temperatureMin);
        weatherInfo.setTemperatureMax(temperatureMax);
        weatherInfo.setWindSpeed(wind_speed);
        weatherInfo.setHumidityRh(humidity);
        weatherInfo.setPressure(pressure);
        boolean b2 = this.updateById(weatherInfo);

        if (weatherInfo.getGatherDate() == null) {
            return Result.ok("信息修改成功");
        }

        int tempMark = 0;
        if (!temperatureMin.isEmpty() && !temperatureMax.isEmpty()) {
            tempMark = 1;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String gatherDate = sdf.format(weatherInfo.getGatherDate());

        List<ZjPlanRecord> planList = mongoTemplate.find(new Query(Criteria.where("project_id")
                        .is(projectId).and("batch_gather_lis.gather_date").is(gatherDate)),
                ZjPlanRecord.class);

        Map<String, Map<String, Object>> updateMap = new HashMap<>();
        for (ZjPlanRecord plan : planList) {
            String planId = plan.get_id();
            String emp = "";
            List<BatchGatherLis> batchGatherLis = plan.getBatch_gather_lis();
            for (int batchIndex = 0; batchIndex < batchGatherLis.size(); batchIndex++) {
                BatchGatherLis batchGather = batchGatherLis.get(batchIndex);
                if (!batchGather.getGather_date().equals(gatherDate)) {
                    continue;
                }
                Scene scene = batchGather.getScene();
                if (tempMark == 1 && StringUtils.isEmpty(scene.getTemp())) {
                    List<String> stringList = new ArrayList<>();
                    stringList.add(temperatureMin);
                    stringList.add(temperatureMax);
                    double averageTemp = sumStrAverage(stringList);
                    double temp;
                    if (5.5 <= averageTemp && averageTemp <= 34.5) {
                        Random random = new Random();
                        temp = averageTemp + random.nextInt(11) / 10.0 - 0.5;
                    } else {
                        temp = averageTemp;
                    }
                    Map<String, Object> updateField = new HashMap<>();
                    updateField.put(String.format("batch_gather_lis.%d.scene.temp", batchIndex),
                            formatNumOrStrToNoFloatKeepInt(temp, 1));
                    updateMap.merge(planId, updateField, (f1, f2) -> {
                        f1.putAll(f2);
                        return f1;
                    });
                }
                if (StringUtils.isEmpty(scene.getPressure()) && !pressure.isEmpty()) {
                    Map<String, Object> updateField = new HashMap<>();
                    updateField.put(String.format("batch_gather_lis.%d.scene.pressure", batchIndex), pressure);
                    updateMap.merge(planId, updateField, (f1, f2) -> {
                        f1.putAll(f2);
                        return f1;
                    });
                }
                if (StringUtils.isEmpty(scene.getHumidity()) && !humidity.isEmpty()) {
                    Map<String, Object> updateField = new HashMap<>();
                    updateField.put(String.format("batch_gather_lis.%d.scene.humidity", batchIndex), humidity);
                    updateMap.merge(planId, updateField, (f1, f2) -> {
                        f1.putAll(f2);
                        return f1;
                    });
                }
            }
        }

        for (Map.Entry<String, Map<String, Object>> entry : updateMap.entrySet()) {
            String planId = entry.getKey();
            Map<String, Object> stat = entry.getValue();
            Query query = new Query(Criteria.where("_id").is(planId));
            Update update = new Update();
            String key = "";
            for (String s : stat.keySet()) {
                key = s;
                String value = stat.get(key).toString();
                update.set(key, value);
            }
            mongoTemplate.updateFirst(query, update, "zj_plan_record");
        }
        return Result.ok("信息修改成功");
    }


    private double sumStrAverage(List<String> list) {
        double sum = 0.0;
        int count = 0;
        for (String str : list) {
            if (!str.isEmpty()) {
                sum += Double.parseDouble(str);
                count++;
            }
        }
        return sum / count;
    }

    private double formatNumOrStrToNoFloatKeepInt(double num, int decimalPlaces) {
        return BigDecimal.valueOf(num)
                .setScale(decimalPlaces, RoundingMode.HALF_UP)
                .doubleValue();
    }


}
