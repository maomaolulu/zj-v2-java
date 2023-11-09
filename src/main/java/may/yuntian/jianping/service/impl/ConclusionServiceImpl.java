package may.yuntian.jianping.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;

import may.yuntian.jianping.mapper.ConclusionMapper;
import may.yuntian.jianping.entity.ConclusionEntity;
import may.yuntian.jianping.service.ConclusionService;

/**
 * 结论
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2022-11-15 08:54:28
 */
@Service("conclusionService")
public class ConclusionServiceImpl extends ServiceImpl<ConclusionMapper, ConclusionEntity> implements ConclusionService {

    public List<ConclusionEntity> getListByProjectId(Long projectId){
        List<ConclusionEntity> list = baseMapper.selectList(new QueryWrapper<ConclusionEntity>()
            .eq("project_id",projectId)
        );
        return list;
    }

}
