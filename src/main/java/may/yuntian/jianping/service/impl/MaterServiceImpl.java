package may.yuntian.jianping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.jianping.entity.MaterialEntity;
import may.yuntian.jianping.mapper.MaterialMapper;
import may.yuntian.jianping.service.MaterialService;
import may.yuntian.sys.utils.PageUtil2;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("all")
@Service("materialService")
public class MaterServiceImpl extends ServiceImpl<MaterialMapper, MaterialEntity> implements MaterialService {

    @Override
    public List<MaterialEntity> listAll(Long projectId) {
        QueryWrapper<MaterialEntity> queryWrapper = new QueryWrapper<MaterialEntity>();
        queryWrapper.eq("project_id", projectId);
        PageUtil2.startPage();
        List<MaterialEntity> list = baseMapper.selectList(queryWrapper);


        return list;
    }
}
