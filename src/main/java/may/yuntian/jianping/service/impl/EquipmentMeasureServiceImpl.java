package may.yuntian.jianping.service.impl;

import may.yuntian.jianping.entity.EquipmentMeasureEntity;
import may.yuntian.jianping.mapper.EquipmentMeasureMapper;
import may.yuntian.jianping.service.EquipmentMeasureService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;


/**
 * 设备布局测点布置图调查
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2022-09-18 18:50:58
 */
@Service("equipmentMeasureService")
public class EquipmentMeasureServiceImpl extends ServiceImpl<EquipmentMeasureMapper, EquipmentMeasureEntity> implements EquipmentMeasureService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<EquipmentMeasureEntity> page = this.page(
                new Query<EquipmentMeasureEntity>().getPage(params),
                new QueryWrapper<EquipmentMeasureEntity>()
        );

        return new PageUtils(page);
    }


    public List<EquipmentMeasureEntity> getListByProjectId(Long projectId){
        QueryWrapper<EquipmentMeasureEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_id", projectId);
//        PageUtil2.startPage();
        List<EquipmentMeasureEntity> list = baseMapper.selectList(queryWrapper);

        return list;
    }

}
