package may.yuntian.jianping.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.jianping.entity.ProtectionEntity;
import may.yuntian.jianping.mapper.ProtectionMapper;
import may.yuntian.jianping.service.ProtectionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 个人防护用品信息
 * 业务逻辑层实现类
 *
 * @author zhanghao
 * @date 2022-03-10
 */
@Service("protectionService")
public class ProtectionServiceImpl extends ServiceImpl<ProtectionMapper, ProtectionEntity> implements ProtectionService {


    @Override
    public List<ProtectionEntity> getProtectionList(String name) {
        QueryWrapper<ProtectionEntity> protectionEntityQueryWrapper = new QueryWrapper<>();
        protectionEntityQueryWrapper.like(StrUtil.isNotBlank(name),"name",name);
        protectionEntityQueryWrapper.last(StrUtil.isBlank(name)," limit 50 ");

        return this.list(protectionEntityQueryWrapper);
    }
}
