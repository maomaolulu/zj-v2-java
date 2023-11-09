package may.yuntian.jianping.service.impl;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;

import may.yuntian.jianping.mapper.LabReportRecordMapper;
import may.yuntian.jianping.entity.LabReportRecordEntity;
import may.yuntian.jianping.service.LabReportRecordService;

/**
 * 报告编制记录表
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2023-02-03 16:54:59
 */
@Service("labReportRecordService")
public class LabReportRecordServiceImpl extends ServiceImpl<LabReportRecordMapper, LabReportRecordEntity> implements LabReportRecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<LabReportRecordEntity> page = this.page(
                new Query<LabReportRecordEntity>().getPage(params),
                new QueryWrapper<LabReportRecordEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 获取最近一次实验室报告出具日期
     * @param projectId
     * @return
     */
    public Date getLabReportDate(Long projectId){
        Date labReportDate = baseMapper.getReportDateDate(projectId);
        return labReportDate;
    }

}
