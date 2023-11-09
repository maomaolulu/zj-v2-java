package may.yuntian.laboratory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;
import may.yuntian.jianping.mongoservice.PlanRecordService;
import may.yuntian.jianping.vo.SampleEquipVo;
import may.yuntian.laboratory.entity.SampleBasisEquipEntity;
import may.yuntian.laboratory.mapper.SampleBasisEquipMapper;
import may.yuntian.laboratory.service.SampleBasisEquipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 检测依据及设备
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @date 2020-12-17
 */
@Service("sampleBasisEquipService")
public class SampleBasisEquipServiceImpl extends ServiceImpl<SampleBasisEquipMapper, SampleBasisEquipEntity> implements SampleBasisEquipService {

    @Autowired
    private PlanRecordService planRecordService;


	public PageUtils queryPage(Map<String, Object> params) {
		IPage<SampleBasisEquipEntity> page = this.page(
				new Query<SampleBasisEquipEntity>().getPage(params),
				new QueryWrapper<SampleBasisEquipEntity>()
				);
				
		return new PageUtils(page);
		
	}

    /**
     * 根据项目ID获取检测依据及设备列表
     * @param projectId
     * @return
     */
	public List<SampleBasisEquipEntity> getListByProjectId(Long projectId){
        List<SampleBasisEquipEntity> list = baseMapper.selectList(new QueryWrapper<SampleBasisEquipEntity>().eq("project_id",projectId));
	    return list;
    }


    /**
     * 生成检测依据及设备
     * @param projectId
     */
    public void generateSampleBasis(Long projectId){

        baseMapper.delete(new QueryWrapper<SampleBasisEquipEntity>().eq("project_id",projectId));

        List<SampleEquipVo> sampleEquipVoList = planRecordService.getListByProjectId(projectId);
        List<SampleBasisEquipEntity> list = new ArrayList<>();
        if (sampleEquipVoList.size()>0){

            Map<String,List<SampleEquipVo>> map = sampleEquipVoList.stream().collect(Collectors.groupingBy(SampleEquipVo::getSubstanceName));
            System.out.println("===========================");
            System.out.println(map);
            for (String key:map.keySet()){
                SampleBasisEquipEntity sampleBasisEquipEntity = new SampleBasisEquipEntity();
                List<SampleEquipVo> sampleEquipVos = map.get(key);
                String equip = sampleEquipVos.stream().map(SampleEquipVo::getEquip).distinct().collect(Collectors.joining(","));
                String recordIds = sampleEquipVos.stream().map(SampleEquipVo::getId).distinct().collect(Collectors.joining(","));
                sampleBasisEquipEntity.setProjectId(projectId);
                sampleBasisEquipEntity.setTestItem(key);
                sampleBasisEquipEntity.setSubstanceId(sampleEquipVos.get(0).getSubstanceId());
                sampleBasisEquipEntity.setBasisName(sampleEquipVos.get(0).getBasisName());
                sampleBasisEquipEntity.setEquip(equip);
                sampleBasisEquipEntity.setItemCate(sampleEquipVos.get(0).getSType());
                sampleBasisEquipEntity.setTestBasis(sampleEquipVos.get(0).getTestBasis());
                sampleBasisEquipEntity.setTestMethod(sampleEquipVos.get(0).getTestMethod());
                sampleBasisEquipEntity.setRecordIds(recordIds);
                list.add(sampleBasisEquipEntity);
            }
            this.saveBatch(list);
        }
    }

    /**
     * 修改检测依据及设备并回填
     * @param list
     */
    public void updateBatchEquip(List<SampleBasisEquipEntity> list){
        this.updateBatchById(list);
        List<SampleBasisEquipEntity> sampleBasisEquipEntities = list.stream().filter(i->i.getItemCate()<=2).collect(Collectors.toList());
        for (SampleBasisEquipEntity sampleBasisEquipEntity:sampleBasisEquipEntities){
            if (sampleBasisEquipEntity.getTestItem().equals("一氧化碳")||sampleBasisEquipEntity.getTestItem().equals("二氧化碳")){
                continue;
            }else {
                String recordIds = sampleBasisEquipEntity.getRecordIds();
                List<String> recordIdList = Arrays.asList(recordIds.split(",")) ;
                for (String recordId:recordIdList){
                    planRecordService.updateEquipment(recordId,sampleBasisEquipEntity.getEquip());
                }
            }
        }
    }


}
