package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.jianping.entity.SubstanceSampleEntity;

import java.util.List;
import java.util.Map;

/**
 * 检测法规依据数据
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2022-03-07 09:39:33
 */
public interface SubstanceSampleService extends IService<SubstanceSampleEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * 根据检物质ID和采样方式查询检测物质依据
     * @param
     * @param sampleMode 采样方式
     * @return entity
     */
    SubstanceSampleEntity selectBySubstanceIdAndType(Long substanceId, Integer sampleMode);

    /**
     * 通过id数组和类型 获取substanceIds
     * @param idList
     * @return
     */
    String getListByIds(List<Long> idList);
    
}

