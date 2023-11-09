package may.yuntian.commission.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.commission.entity.PerformanceEntity;
import may.yuntian.commission.mapper.PerformanceMapper;
import may.yuntian.commission.service.PerformanceService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 人员绩效表
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2022-06-17 09:54:17
 */
@Service("performanceService")
public class PerformanceServiceImpl extends ServiceImpl<PerformanceMapper, PerformanceEntity> implements PerformanceService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PerformanceEntity> page = this.page(
                new Query<PerformanceEntity>().getPage(params),
                new QueryWrapper<PerformanceEntity>()
        );

        page.getRecords().forEach(action->{
//            if (action.getCumulativeOutput())
            BigDecimal percentComplete = action.getCumulativeOutput().divide(action.getTargetOutput(),2,BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(100));
            action.setPercentComplete(percentComplete);

        });

        return new PageUtils(page);
    }

    /**
     * 根据人员ID获取人员产出信息
     * @param userid
     * @return
     */
    public PerformanceEntity getByUserid(Long userid){
        PerformanceEntity performance = baseMapper.selectOne(new QueryWrapper<PerformanceEntity>().eq("userid",userid));

        return performance;
    }




}
