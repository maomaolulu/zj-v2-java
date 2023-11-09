package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.jianping.dto.UsableDto;
import may.yuntian.jianping.entity.EquipmentEntity;

import java.util.Map;

/**
 * @author ZhuYiCheng
 * @date 2023/7/13 10:35
 */
public interface EquipmentService extends IService<EquipmentEntity> {

    /**
     * 空气(毒物和粉尘)中有害物质检测可用仪器列表
     * @return
     */
    Map<String, Object> airEqLis();

    /**
     * 根据采样记录中物质信息获取可用采样设备名称
     * @return
     */
    Map<String, Object> usableLis(UsableDto dto);
}
