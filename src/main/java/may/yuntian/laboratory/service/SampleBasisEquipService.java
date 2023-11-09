package may.yuntian.laboratory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.laboratory.entity.SampleBasisEquipEntity;

import java.util.List;
import java.util.Map;

/**
 * 检测依据及设备
 * 业务逻辑层接口
 *
 * @author LiXin
 * @date 2020-12-17
 */
public interface SampleBasisEquipService extends IService<SampleBasisEquipEntity> {

	PageUtils queryPage(Map<String, Object> params);


    /**
     * 根据项目ID获取检测依据及设备列表
     * @param projectId
     * @return
     */
    List<SampleBasisEquipEntity> getListByProjectId(Long projectId);


    /**
     * 生成检测依据及设备
     * @param projectId
     */
    void generateSampleBasis(Long projectId);


    /**
     * 修改检测依据及设备并回填
     * @param list
     */
    void updateBatchEquip(List<SampleBasisEquipEntity> list);

}