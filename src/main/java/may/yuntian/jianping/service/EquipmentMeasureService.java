package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.jianping.entity.EquipmentMeasureEntity;

import java.util.List;
import java.util.Map;

/**
 * 设备布局测点布置图调查
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2022-09-18 18:50:58
 */
public interface EquipmentMeasureService extends IService<EquipmentMeasureEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<EquipmentMeasureEntity> getListByProjectId(Long projectId);
    
}

