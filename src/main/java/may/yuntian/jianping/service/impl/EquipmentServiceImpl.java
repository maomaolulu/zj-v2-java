package may.yuntian.jianping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import may.yuntian.jianping.dto.UsableDto;
import may.yuntian.jianping.entity.EquipmentEntity;
import may.yuntian.jianping.mapper.EquipmentMapper;
import may.yuntian.jianping.service.EquipmentService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author ZhuYiCheng
 * @date 2023/7/13 10:36
 */
@Service("equipmentService")
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, EquipmentEntity> implements EquipmentService {



    /**
     * 空气(毒物和粉尘)中有害物质检测可用仪器列表
     *
     * @return
     */
    @Override
    public Map<String, Object> airEqLis() {
        ArrayList<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        ArrayList<Integer> list2 = new ArrayList<>();
        list2.add(4);
        list2.add(5);
        list2.add(22);
        List<EquipmentEntity> equip_lis = this.list(new QueryWrapper<EquipmentEntity>().in("state", list1).in("category_id", list2));
        List<EquipmentEntity> correct_equip_lis = this.list(new QueryWrapper<EquipmentEntity>().eq("category_id", 20));
        List<HashMap<String, Object>> dust_fixed_lis = new ArrayList<>();
        List<HashMap<String, Object>> dust_solo_lis = new ArrayList<>();
        List<HashMap<String, Object>> toxic_lis = new ArrayList<>();
        for (EquipmentEntity item_eq : equip_lis) {
            if (item_eq.getCategoryId() == 4) {
                HashMap<String, Object> item_dict = new HashMap<>();
                item_dict.put("name", item_eq.getName());
                item_dict.put("model", item_eq.getModel());
                item_dict.put("asset_sn", item_eq.getAssetSn());
                item_dict.put("measure_scope", item_eq.getMeasureScope());
                item_dict.put("correction_factor", item_eq.getCorrectionFactor());
                item_dict.put("ms_min", item_eq.getMsMin());
                item_dict.put("ms_max", item_eq.getMsMax());
                dust_fixed_lis.add(item_dict);
            } else if (item_eq.getCategoryId() == 5) {
                HashMap<String, Object> item_dict = new HashMap<>();
                item_dict.put("name", item_eq.getName());
                item_dict.put("model", item_eq.getModel());
                item_dict.put("asset_sn", item_eq.getAssetSn());
                item_dict.put("measure_scope", item_eq.getMeasureScope());
                item_dict.put("correction_factor", item_eq.getCorrectionFactor());
                item_dict.put("ms_min", item_eq.getMsMin());
                item_dict.put("ms_max", item_eq.getMsMax());
                dust_solo_lis.add(item_dict);
            }else {
                HashMap<String, Object> item_dict = new HashMap<>();
                item_dict.put("name", item_eq.getName());
                item_dict.put("model", item_eq.getModel());
                item_dict.put("asset_sn", item_eq.getAssetSn());
                item_dict.put("measure_scope", item_eq.getMeasureScope());
                item_dict.put("correction_factor", item_eq.getCorrectionFactor());
                item_dict.put("ms_min", item_eq.getMsMin());
                item_dict.put("ms_max", item_eq.getMsMax());
                dust_solo_lis.add(item_dict);
                toxic_lis.add(item_dict);
            }
        }
        List<HashMap<String, Object>> new_correct_equip_lis = new ArrayList<>();
        for (EquipmentEntity item_ce : correct_equip_lis) {
            HashMap<String, Object> item_dict = new HashMap<>();
            item_dict.put("name", item_ce.getName());
            item_dict.put("model", item_ce.getModel());
            item_dict.put("asset_sn", item_ce.getAssetSn());
            item_dict.put("measure_scope", item_ce.getMeasureScope());
            item_dict.put("correction_factor", item_ce.getCorrectionFactor());
            new_correct_equip_lis.add(item_dict);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("dust_fixed_lis", dust_fixed_lis);
        map.put("dust_solo_lis", dust_solo_lis);
        map.put("toxic_lis", toxic_lis);
        map.put("correct_equip_lis", new_correct_equip_lis);

        return map;
    }

    @Override
    public Map<String, Object> usableLis(UsableDto dto) {
        QueryWrapper<EquipmentEntity> wrapperE = new QueryWrapper<>();
        QueryWrapper<EquipmentEntity> wrapperCe = new QueryWrapper<>();
        List<EquipmentEntity> equipLis = new ArrayList<>();
        List<EquipmentEntity> correctEquipLis = new ArrayList<>();
        wrapperE.in("state",Arrays.asList(1,2,7));
        boolean ifSelectCe = false, otherSType = false;
        List<Integer> list = Arrays.asList(10, 23);
        List<Integer> list1 = Arrays.asList(11, 23);
        switch (dto.getS_type()){
            // 毒物
            case 1:
                switch (dto.getSub_name()){
//                    case "游离二氧化硅":
//                        break;
                    case "一氧化碳":
                        wrapperE.in("category_id", list);
                        break;
                    case "二氧化碳":
                        wrapperE.in("category_id", list1);
                        break;
                    default:
                        wrapperE.eq("category_id", 22).lt("ms_min",dto.getFlow()).gt("ms_max",dto.getFlow());
                        wrapperCe.eq("category_id",20);
                        ifSelectCe = true;
                        break;
                }
                break;
            // 粉尘
            case 2:
                if (dto.getIs_fixed() == 1){
                    wrapperE.eq("category_id", 4).lt("ms_min",dto.getFlow()).gt("ms_max",dto.getFlow());
                }else {
                    wrapperE.in("category_id", Arrays.asList(4, 5, 22)).lt("ms_min",dto.getFlow()).gt("ms_max",dto.getFlow());
                    wrapperCe.eq("category_id",20);
                    ifSelectCe = true;
                }
                break;
            // 噪声
            case 3:
                wrapperE.eq("category_id",7);
                wrapperCe.eq("category_id",21);
                ifSelectCe = true;
                break;
            // 高温
            case 4:
                wrapperE.eq("category_id",8);
                break;
            // 紫外辐射
            case 5:
                wrapperE.eq("category_id",9);
                break;
            // 手传振动
            case 6:
                wrapperE.eq("category_id",15);
                break;
            // 工频电场、高频电磁场、超高频辐射
            case 7:
            case 8:
            case 9:
                wrapperE.eq("category_id",12);
                break;
            // 微波辐射
            case 10:
                wrapperE.eq("category_id",19);
                break;
            // 风速
            case 11:
                wrapperE.eq("category_id",18);
                break;
            // 照度
            case 12:
                wrapperE.eq("category_id",16);
                break;
            // 激光辐射
            case 13:
                wrapperE.eq("category_id",17);
                break;
            default:
                otherSType = true;
                break;
        }
        if (!otherSType){
            equipLis = this.list(wrapperE);
        }
        if (ifSelectCe){
            correctEquipLis = this.list(wrapperCe);
        }
        Map<String, Object> returnMap = new HashMap<>(2);
        List<Map<String,Object>> newEquipLis = new ArrayList<>();
        List<Map<String,Object>> newCorrectEquipLis = new ArrayList<>();
        listToListMap(equipLis, newEquipLis);
        listToListMap(correctEquipLis, newCorrectEquipLis);
        returnMap.put("equip_lis", newEquipLis);
        returnMap.put("correct_equip_lis", newCorrectEquipLis);
        return returnMap;
    }

    private void listToListMap(List<EquipmentEntity> correctEquipLis, List<Map<String, Object>> newCorrectEquipLis) {
        if (correctEquipLis.size()>0){
            for (EquipmentEntity e:correctEquipLis){
                Map<String,Object> map = new HashMap<>(5);
                map.put("name", e.getName());
                map.put("model", e.getModel());
                map.put("asset_sn", e.getAssetSn());
                map.put("measure_scope", e.getMeasureScope());
                map.put("correction_factor", e.getCorrectionFactor());
                newCorrectEquipLis.add(map);
            }
        }
    }

}
