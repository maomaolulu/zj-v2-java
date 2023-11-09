package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.jianping.entity.ProtectionEvaluationEntity;

import java.util.List;
import java.util.Map;

/**
 * 个人防护用品有效性评价
 * 业务逻辑层接口
 *
 * @author LiXin
 * @date 2020-12-17
 */
public interface ProtectionEvaluationService extends IService<ProtectionEvaluationEntity> {



	PageUtils queryPage(Map<String, Object> params);
	
	/**
  	 * 根据项目ID获取个人防护用品有效性评价
    * @param
    * @return
    */
   List<ProtectionEvaluationEntity> listByProjectId(Map<String, Object> params);

    /**
     * 根据项目ID获取列表
     * @param projectId
     * @return
     */
    List<ProtectionEvaluationEntity> getListByprojectId(Long projectId);

    /**
     * 初始化个人防护用品
     * @param projectId
     */
    void initializationProtectionEvaluation(Long projectId);

}