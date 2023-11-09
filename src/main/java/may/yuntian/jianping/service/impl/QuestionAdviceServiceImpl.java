package may.yuntian.jianping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;
import may.yuntian.jianping.entity.QuestionAdviceEntity;
import may.yuntian.jianping.mapper.QuestionAdviceMapper;
import may.yuntian.jianping.service.QuestionAdviceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 存在的问题及建议
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @date 2020-12-30
 */
@Service("QuestionAdviceService")
public class QuestionAdviceServiceImpl extends ServiceImpl<QuestionAdviceMapper, QuestionAdviceEntity> implements QuestionAdviceService {
	

	
	@Override
	public List<QuestionAdviceEntity> queryPage(Map<String, Object> params) {
		String openQuestion = (String)params.get("openQuestion");
		String advice = (String)params.get("advice");
		
		QueryWrapper<QuestionAdviceEntity> queryWrapper = new QueryWrapper<QuestionAdviceEntity>()
                .like(StringUtils.isNotBlank(openQuestion),"open_question", openQuestion)
                .like(StringUtils.isNotBlank(advice),"advice", advice);

		List<QuestionAdviceEntity> list = baseMapper.selectList(queryWrapper);
        return list;
	}
	
	
	/**
	 * 不分页按条件查询列表
	 * @param
	 * @return
	 */
	public List<QuestionAdviceEntity> listAll(){
		
		List<QuestionAdviceEntity> questionAdviceList = this.list();
		return questionAdviceList;
	}
	
	

	
	/**
	 * 不分页查询条件
	 * @param params
	 * @return
	 */
	public QueryWrapper<QuestionAdviceEntity> queryWrapperByParams(QuestionAdviceEntity params){
		
		String openQuestion = params.getOpenQuestion();
		String advice = params.getAdvice();
		
		QueryWrapper<QuestionAdviceEntity> queryWrapper = new QueryWrapper<QuestionAdviceEntity>()
				.like(StringUtils.isNotBlank(openQuestion),"open_question", openQuestion)
                .like(StringUtils.isNotBlank(advice),"advice", advice)
                .orderByDesc("id");
		
		return queryWrapper;
	}
	

}