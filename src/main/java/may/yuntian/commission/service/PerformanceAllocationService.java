package may.yuntian.commission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.commission.entity.PerformanceAllocationEntity;
import may.yuntian.commission.vo.PerformanceNodeVo;
import may.yuntian.common.utils.PageUtils;

import java.util.Map;

/**
 * 绩效分配表
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2022-06-17 09:54:17
 */
public interface PerformanceAllocationService extends IService<PerformanceAllocationEntity> {


    /**
     * 根据ID获取详情信息
     * @param id
     * @return
     */
    PerformanceAllocationEntity getInfo(Long id);


    /**
     * 检评采样提成
     * @param performanceNodeVo
     */
    void caiyangZjCommission(PerformanceNodeVo performanceNodeVo);



    
}

