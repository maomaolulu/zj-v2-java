package may.yuntian.anlian.service.impl;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import may.yuntian.anlian.mapper.LinkTestMapper;
import may.yuntian.anlian.entity.LinkTestEntity;
import may.yuntian.anlian.service.LinkTestService;

/**
 * 
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2023-05-08 09:09:26
 */
@Service("linkTestService")
public class LinkTestServiceImpl extends ServiceImpl<LinkTestMapper, LinkTestEntity> implements LinkTestService {

    @Override
    public List<LinkTestEntity> queryPage(Map<String, Object> params) {
        QueryWrapper<LinkTestEntity> queryWrapper = new QueryWrapper<>();

        List<LinkTestEntity> list = baseMapper.selectList(queryWrapper);

        return list;
    }



}