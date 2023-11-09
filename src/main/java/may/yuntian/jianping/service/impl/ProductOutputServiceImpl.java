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

import may.yuntian.jianping.mapper.ProductOutputMapper;
import may.yuntian.jianping.entity.ProductOutputEntity;
import may.yuntian.jianping.service.ProductOutputService;

/**
 * 主要产品与年产量
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2022-11-10 15:52:36
 */
@Service("productOutputService")
public class ProductOutputServiceImpl extends ServiceImpl<ProductOutputMapper, ProductOutputEntity> implements ProductOutputService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductOutputEntity> page = this.page(
                new Query<ProductOutputEntity>().getPage(params),
                new QueryWrapper<ProductOutputEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 根据项目ID获取产品及产量信息
     * @param projectId
     * @return
     */
    public List<ProductOutputEntity> getListByProjectId(Long projectId){
        List<ProductOutputEntity> list = baseMapper.selectList(new QueryWrapper<ProductOutputEntity>().eq("project_id",projectId));
        return list;
    }

}
