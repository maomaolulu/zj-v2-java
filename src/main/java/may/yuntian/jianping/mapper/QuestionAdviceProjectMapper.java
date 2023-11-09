package may.yuntian.jianping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.jianping.entity.QuestionAdviceProjectEntity;
import org.apache.ibatis.annotations.Mapper;


/**
 * 存在的问题及建议对应项目关系
 * 数据持久层接口
 * 
 * @author LiXin
 * @date 2020-12-30
 */
@Mapper
public interface QuestionAdviceProjectMapper extends BaseMapper<QuestionAdviceProjectEntity> {
	
}
