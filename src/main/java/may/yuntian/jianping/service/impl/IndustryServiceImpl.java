package may.yuntian.jianping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.jianping.entity.IndustryEntity;
import may.yuntian.jianping.mapper.IndustryMapper;
import may.yuntian.jianping.service.IndustryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("industryService")
public class IndustryServiceImpl extends ServiceImpl<IndustryMapper, IndustryEntity> implements IndustryService {
    @Override
    public List<IndustryEntity> listJoint(String joint) {
        ArrayList<IndustryEntity> list = new ArrayList<>();
        if (joint=="" || joint==null){
            List<IndustryEntity> industryEntities = this.list(new QueryWrapper<IndustryEntity>()
                    .like("joint", "C")
                    .last("limit 50")
            );
            list.addAll(industryEntities);
        }else {
            List<IndustryEntity> list1 = this.list(new QueryWrapper<IndustryEntity>()
                    .like("joint", joint)
            );
            list.addAll(list1);
        }
        return list;
    }
}
