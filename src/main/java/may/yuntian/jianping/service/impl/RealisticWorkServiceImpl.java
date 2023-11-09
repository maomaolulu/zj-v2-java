package may.yuntian.jianping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.jianping.entity.RealisticRecordEntity;
import may.yuntian.jianping.entity.RealisticWorkEntity;
import may.yuntian.jianping.mapper.RealisticWorkMapper;
import may.yuntian.jianping.service.RealisticRecordService;
import may.yuntian.jianping.service.RealisticWorkService;
import may.yuntian.sys.utils.PageUtil2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("realisticWorkService")
public class RealisticWorkServiceImpl extends ServiceImpl<RealisticWorkMapper, RealisticWorkEntity> implements RealisticWorkService {

   @Autowired
   private RealisticRecordService realisticRecordService;

    @Override
    public List<RealisticWorkEntity> getList(Long projectId) {
        QueryWrapper<RealisticWorkEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_id", projectId);
        PageUtil2.startPage();
        List<RealisticWorkEntity> list = baseMapper.selectList(queryWrapper);
        list.forEach(v-> {
            Long id = v.getId();
            List<RealisticRecordEntity> listByRealisticId = realisticRecordService.getListByRealisticId(id);

            v.setAllRealisticRecord(listByRealisticId);
        });
        return list;
    }
}
