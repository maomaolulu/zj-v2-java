package may.yuntian.report.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.vo.ReportSelectDictVo;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.report.entity.ReportReviewDictEntity;

import java.util.List;
import java.util.Map;

/**
 * 报告技术审核项对应关系表
 * 业务逻辑层接口
 *
 * @author LinXin
 * @date 2022-04-14
 */
public interface ReportReviewDictService extends IService<ReportReviewDictEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    List<ReportSelectDictVo> selectSysDictByProjectIdAndType(Long projectId, String type);
}

