package may.yuntian.jianping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.utils.Number2Money;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;
import may.yuntian.jianping.entity.SubstanceSampleEntity;
import may.yuntian.jianping.mapper.SubstanceSampleMapper;
import may.yuntian.jianping.service.SubstanceSampleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 检测法规依据数据
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2022-03-07 09:39:33
 */
@Service("substanceSampleService")
public class SubstanceSampleServiceImpl extends ServiceImpl<SubstanceSampleMapper, SubstanceSampleEntity> implements SubstanceSampleService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        params = Number2Money.getPageInfo(params);
        IPage<SubstanceSampleEntity> page = this.page(
                new Query<SubstanceSampleEntity>().getPage(params),
                new QueryWrapper<SubstanceSampleEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 根据检物质ID和采样方式查询检测物质依据
     * @param
     * @param sampleMode 采样方式
     * @return entity
     */
    public SubstanceSampleEntity selectBySubstanceIdAndType(Long substanceId, Integer sampleMode) {
        SubstanceSampleEntity entity= baseMapper.selectOne(new QueryWrapper<SubstanceSampleEntity>().eq("substance_id", substanceId)
                .eq("sample_mode", sampleMode)
        );
        return entity;
    }



    /**
     * 通过id数组和类型 获取substanceIds
     * @param idList
     * @return
     */
    public String getListByIds(List<Long> idList) {
        List<SubstanceSampleEntity> gismethodList = baseMapper.selectList(new QueryWrapper<SubstanceSampleEntity>()
                .in("id", idList)
        );
        String substanceIds = gismethodList.stream().map(p->String.valueOf(p.getSubstanceId())).collect(Collectors.joining(","));
        return substanceIds;
    }

}
