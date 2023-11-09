package may.yuntian.laboratory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.laboratory.entity.DetectionEntity;

import java.util.Map;

/**
 * 检测情况
 * 业务逻辑层接口
 *
 * @author LiXin
 * @date 2020-12-17
 */
public interface DetectionService extends IService<DetectionEntity> {

	DetectionEntity getByProjectId(Long projectId);

	PageUtils queryPage(Map<String, Object> params);
	
	/**
	 * 重构新增或修改接口
	 * @param detectionEntity
	 */
	 void saveOrUpdateByprojectId(DetectionEntity detectionEntity);


}