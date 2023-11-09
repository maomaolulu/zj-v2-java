package may.yuntian.jianping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.jianping.entity.AlDeliverReceived;
import may.yuntian.jianping.mapper.AlDeliverReceivedMapper;
import may.yuntian.jianping.service.AlDeliverReceivedService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 送样收样记录表 服务实现类
 * </p>
 *
 * @author cwt
 * @since 2023-03-16
 */
@Service
public class AlDeliverReceivedServiceImpl extends ServiceImpl<AlDeliverReceivedMapper, AlDeliverReceived> implements AlDeliverReceivedService {

    /**
     * 获取项目的送样状态
     *
     * @param projectId
     * @return
     */
    @Override
    public List<AlDeliverReceived> alDeliverReceived(String projectId) {
        return baseMapper.selectList(new QueryWrapper<AlDeliverReceived>().eq("project_id", projectId)
                .in("status", 2, 3, 4));
    }


    /**
     * 获取是否送样
     * @param projectId
     * @return
     */
    public boolean isDeliver(Long projectId){
        int count = baseMapper.selectCount(new QueryWrapper<AlDeliverReceived>().eq("project_id", projectId)
                .in("status", 2, 3, 4));
        if (count > 0){
            return true;
        }else {
            return false;
        }
    }
}
