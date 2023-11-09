package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.jianping.entity.QuestionAdviceProjectEntity;

/**
 * 存在的问题及建议对应项目关系
 * 业务逻辑层接口
 * 
 * @author LiXin
 * @date 2020-12-30
 */
public interface QuestionAdviceProjectService extends IService<QuestionAdviceProjectEntity> {


    void deleteReportImproveId(Long projectId);
}

