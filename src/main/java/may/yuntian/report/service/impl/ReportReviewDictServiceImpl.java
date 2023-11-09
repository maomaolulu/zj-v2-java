package may.yuntian.report.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.vo.ReportSelectDictVo;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;
import may.yuntian.report.entity.ReportReviewDictEntity;
import may.yuntian.report.mapper.ReportReviewDictMapper;
import may.yuntian.report.service.ReportReviewDictService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 报告技术审核项对应关系表
 * 业务逻辑层实现类
 *
 * @author LinXin
 * @date 2022-04-14
 */
@Service("reportReviewDictService")
public class ReportReviewDictServiceImpl extends ServiceImpl<ReportReviewDictMapper, ReportReviewDictEntity> implements ReportReviewDictService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
    	String projectId = (String)params.get("projectId");		//项目ID
        IPage<ReportReviewDictEntity> page = this.page(
                new Query<ReportReviewDictEntity>().getPage(params),
                new QueryWrapper<ReportReviewDictEntity>()
                .eq(StringUtils.isNotBlank(projectId),"project_id", projectId)
        );

        return new PageUtils(page);
    }

//	@Override
	public List<ReportSelectDictVo> selectSysDictByProjectIdAndType(Long projectId, String type) {
		//System.out.println("报告技术审核项对应关系表查询参数:contractId="+contractId+" type="+type);
		return baseMapper.selectSysDictByProjectIdAndType(projectId, type);
	}
    
  
}
