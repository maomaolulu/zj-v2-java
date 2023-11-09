package may.yuntian.jianping.service.impl;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import may.yuntian.jianping.mapper.SubstanceDetectionMapper;
import may.yuntian.jianping.entity.SubstanceDetectionEntity;
import may.yuntian.jianping.service.SubstanceDetectionService;

/**
 * 物质检测信息表（职卫）
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2023-06-28 15:02:08
 */
@Service("substanceDetectionService")
public class SubstanceDetectionServiceImpl extends ServiceImpl<SubstanceDetectionMapper, SubstanceDetectionEntity> implements SubstanceDetectionService {


    @Override
    public SubstanceDetectionEntity getInfoBySubstanceIdAndCompanyOrder(Long substanceId,String companyOrder){
        SubstanceDetectionEntity substanceDetectionEntity = baseMapper.selectOne(new QueryWrapper<SubstanceDetectionEntity>()
                .eq("substance_id",substanceId)
                .eq("lab_source",companyOrder)
        );
        return substanceDetectionEntity;
    }

}