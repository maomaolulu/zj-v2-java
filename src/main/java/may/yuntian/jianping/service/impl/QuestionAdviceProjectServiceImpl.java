package may.yuntian.jianping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.jianping.entity.QuestionAdviceProjectEntity;
import may.yuntian.jianping.mapper.QuestionAdviceProjectMapper;
import may.yuntian.jianping.service.QuestionAdviceProjectService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 存在的问题及建议对应项目关系
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @date 2020-12-30
 */
@Service("questionAdviceProjectService")
public class QuestionAdviceProjectServiceImpl extends ServiceImpl<QuestionAdviceProjectMapper, QuestionAdviceProjectEntity> implements QuestionAdviceProjectService {




    public void deleteReportImproveId(Long projectId){
        int count = baseMapper.selectCount(new QueryWrapper<QuestionAdviceProjectEntity>()
            .eq("project_id",projectId).gt("report_improve_id",0)
        );
        if (count>0){
            baseMapper.delete(new QueryWrapper<QuestionAdviceProjectEntity>()
                    .eq("project_id",projectId).gt("report_improve_id",0));
        }
    }
  
}
