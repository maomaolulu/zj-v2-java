package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.jianping.entity.SubstanceDetectionEntity;

import java.util.Map;

/**
 * 物质检测信息表（职卫）
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2023-06-28 15:02:08
 */
public interface SubstanceDetectionService extends IService<SubstanceDetectionEntity> {
    public SubstanceDetectionEntity getInfoBySubstanceIdAndCompanyOrder(Long substanceId,String companyOrder);

}

