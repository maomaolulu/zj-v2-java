package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.jianping.entity.EquipmentLayoutEntity;

import java.util.List;
import java.util.Map;

public interface EquipmentLayoutService extends IService<EquipmentLayoutEntity> {
    List<EquipmentLayoutEntity> getList(Long projectId);

    void init(EquipmentLayoutEntity layout);
}
