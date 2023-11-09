package may.yuntian.jianping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.jianping.entity.RealisticRecordEntity;
import may.yuntian.jianping.mapper.RealisticRecordMapper;
import may.yuntian.jianping.service.RealisticRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("realisticRecordService")
public class RealisticRecordServiceImpl extends ServiceImpl<RealisticRecordMapper, RealisticRecordEntity> implements RealisticRecordService {
    @Override
    public List<RealisticRecordEntity> getListByRealisticId(Long id) {

        List<RealisticRecordEntity> list = this.list(
                new QueryWrapper<RealisticRecordEntity>()
                        .eq("realistic_id", id)
                        .orderByAsc("working_time"));

        return list;
    }

    @Override
    public void deleteRealisticId(Long realisticId) {
        baseMapper.delete(new QueryWrapper<RealisticRecordEntity>().eq("realistic_id", realisticId));
    }
}
