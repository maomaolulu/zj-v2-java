package may.yuntian.jianping.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;

import may.yuntian.jianping.mapper.MainDataMapper;
import may.yuntian.jianping.entity.MainDataEntity;
import may.yuntian.jianping.service.MainDataService;

/**
 * 实验室-主数据
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2022-09-13 16:54:23
 */
@Service("mainDataService")
public class MainDataServiceImpl extends ServiceImpl<MainDataMapper, MainDataEntity> implements MainDataService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MainDataEntity> page = this.page(
                new Query<MainDataEntity>().getPage(params),
                new QueryWrapper<MainDataEntity>()
        );

        return new PageUtils(page);
    }

}
