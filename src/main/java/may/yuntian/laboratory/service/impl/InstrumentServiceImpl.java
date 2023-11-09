package may.yuntian.laboratory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.laboratory.entity.InstrumentEntity;
import may.yuntian.laboratory.mapper.InstrumentMapper;
import may.yuntian.laboratory.service.InstrumentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务层逻辑实现代码
 */
@Service("instrumentService")
public class InstrumentServiceImpl extends ServiceImpl<InstrumentMapper, InstrumentEntity> implements InstrumentService {

    @Override
    public List<InstrumentEntity> selectAllAndSplicing(String name) {
        List<InstrumentEntity> list = baseMapper.selectList(
                new QueryWrapper<InstrumentEntity>().like(StringUtils.isNotBlank(name) ,"name",name)
                .last(StringUtils.isBlank(name),"limit 50")
        );
        list.forEach(action->{
            action.setLink(action.getModel()+action.getName()+action.getCode());
        });


        return list;
    }
}
