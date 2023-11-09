package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.entity.CommissionEntity;

import java.util.List;

/**
 * 提成记录
 * 业务逻辑层接口
 *
 * @author LiXin
 * @date 2020-12-09
 */
public interface CommissionService extends IService<CommissionEntity> {

    /**
     * 通过项目id和提成类型获取列表
     */
    CommissionEntity getCommissionByProjectIdAndType(Long projectId, String type);
    

    
    /**
     * 获取已提成项目id
     */
    List<Long> getNotNullIdList();
    


	
}