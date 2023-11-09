package may.yuntian.anlian.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.CommissionEntity;
import may.yuntian.anlian.mapper.CommissionMapper;
import may.yuntian.anlian.service.CommissionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 提成记录
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @date 2020-12-09
 */
@Service("commissionService")
public class CommissionServiceImpl extends ServiceImpl<CommissionMapper, CommissionEntity> implements CommissionService {

    
    
    /**
     * 获取已提成项目id
     */
    public List<Long> getNotNullIdList() {
		List<CommissionEntity> list = baseMapper.selectList(new QueryWrapper<CommissionEntity>()
				.eq("type", "采样提成")
				.isNotNull("commission_date")
				);
		List<Long> idList = list.stream().distinct().map(CommissionEntity::getProjectId).collect(Collectors.toList());
		return idList;
	}
    

    
    /**
     * 通过项目id和提成类型获取列表
     */
    public CommissionEntity getCommissionByProjectIdAndType(Long projectId, String type) {
    	CommissionEntity commissionEntity = baseMapper.selectOne(new QueryWrapper<CommissionEntity>()
				.eq("project_id", projectId)
				.eq("type", type)
				);
    	return commissionEntity;
	}
}
