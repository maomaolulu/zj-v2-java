package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.jianping.entity.CompanySurveyEntity;
import may.yuntian.jianping.vo.CompanySurveyVo;

import java.util.List;
import java.util.Map;

/**
 * @author mi
 */
public interface CompanySurveyService extends IService<CompanySurveyEntity> {
    /**
     * 根据项目Id获取项目信息
     */
    CompanySurveyEntity getOne(Long projectId);

    /**
     * 检评需要公示列表 // 项目是检评的  加删除
     *
     * @param params
     * @return
     */
    List<CompanySurveyVo> pageListJp(Map<String, Object> params);




}
