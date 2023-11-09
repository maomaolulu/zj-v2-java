package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.jianping.entity.QuestionAdviceEntity;

import java.util.List;
import java.util.Map;

/**
 * 存在的问题及建议
 * 业务逻辑层接口
 *
 * @author LiXin
 * @date 2020-12-30
 */

public interface QuestionAdviceService extends IService<QuestionAdviceEntity> {

    List<QuestionAdviceEntity> queryPage(Map<String, Object> params);

	/**
	 * 不分页按条件查询列表
	 * @param
	 * @return
	 */
	List<QuestionAdviceEntity> listAll();


}
