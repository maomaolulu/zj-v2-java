package may.yuntian.jianping.mongoservice;

import cn.hutool.core.date.DateUtil;
import io.swagger.models.auth.In;
import may.yuntian.jianping.dto.ResultDto;
import org.bson.types.ObjectId;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mongodb.client.result.UpdateResult;
import may.yuntian.anlian.entity.ProjectDateEntity;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.service.ProjectDateService;
import may.yuntian.anlian.service.ProjectService;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.jianping.dto.MongoUpdateDto;
import may.yuntian.jianping.dto.PlanRecordDto;
import may.yuntian.jianping.entity.*;
import may.yuntian.jianping.mapper.SubstanceMapper;
import may.yuntian.jianping.service.*;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author gy
 * @date 2023-07-27 19:05
 */
@Service
public class SamplePlanService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private CompanySurveyService companySurveyService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectDateService projectDateService;

    @Autowired
    private LabAcceptProjectService acceptProjectService;

    @Autowired
    private EquipmentLayoutService equipmentLayoutService;

    @Autowired
    private EvalHtLimitService evalHtLimitService;

    @Autowired
    private MainDataService mainDataService;

    @Autowired
    private SubstanceMapper substanceMapper;

    @Autowired
    private ProjectCountService projectCountService;

    /**
     * 查询采样方案列表
     */
    public Result planLis(Long projectId){
        ProjectDateEntity projectDate = projectDateService.getOne(new QueryWrapper<ProjectDateEntity>().eq("project_id",projectId));
        if (projectDate == null) {
            return Result.error(501, "项目信息不存在", null);
        }
        String sDate = DateUtil.format(projectDate.getPlanStartDate(),"yyyy-MM-dd");
        String eDate = DateUtil.format(projectDate.getPlanEndDate(),"yyyy-MM-dd");
        Query query = new Query();
        query.addCriteria(Criteria.where("project_id").is(projectId));
        query.with(Sort.by(Sort.Direction.ASC, "relation_pfn_ids.0", "_id"));
        List<JSONObject> planLis = mongoTemplate.find(query, JSONObject.class, "zj_plan_record");
        Map<String, Object> returnMap = new HashMap<>(3);
        returnMap.put("start_date", sDate);
        returnMap.put("end_date", eDate);
        List<Object> returnList= new ArrayList<>();
        for (JSONObject planObj : planLis){
            planObj.put("id",planObj.getString("_id"));
            planObj.put("mid",planObj.getString("m_id"));
            returnList.add(planObj);
        }
        returnMap.put("list", returnList);
        return Result.ok("信息获取成功", returnMap);
    }

    /**
     * 设置空白样品
     */
    public Result setKbCode(PlanRecordDto planRecord){
        Long projectId = planRecord.getProject_id();
        String id = planRecord.getId();
        Query query = new Query();
        query.addCriteria(Criteria.where("project_id").is(projectId)).addCriteria(Criteria.where("_id").is(id));
        List<JSONObject> planLis = mongoTemplate.find(query, JSONObject.class, "zj_plan_record");
        List<ProjectEntity> projectLis = projectService.list(new QueryWrapper<ProjectEntity>().eq("id",projectId));
        ProjectEntity projectInfo = projectLis.get(0);
        if (planLis.size() == 0){
            return Result.error(403, "需要设置空白样的采样方案信息有误", null);
        }
        JSONObject plan = planLis.get(0);
        Integer sDept = plan.getJSONObject("substance").getJSONObject("substance_info").getInteger("s_dept");
        String sampleTableName = plan.getJSONObject("substance").getJSONObject("substance_info").getString("sample_tablename");
        List<Integer> list1 = Arrays.asList(1,2);
        List<String> list2 = Arrays.asList("co","co2");
        if (!list1.contains(sDept) || list2.contains(sampleTableName)){
            return Result.error(403,"该采样方案不能设置空白样", null);
        }
        Date nowTime = new Date();
        if (plan.getJSONArray("sample_kb_code_lis").size() > 0){
            if (plan.getInteger("multi_substance") == 2){
                Query query1 = new Query();
                query1.addCriteria(Criteria.where("_id").is(plan.getString("_id")));
                Update planUpdate = new Update();
                planUpdate.set("update_time", nowTime);
                for (int i = 0; i < plan.getJSONArray("batch_gather_lis").size(); i++){
                    planUpdate.set("batch_gather_lis." + i + ".sample_kb_code_lis", new ArrayList<>());
                    planUpdate.set("batch_gather_lis." + i + ".has_kb_code", 2);
                    planUpdate.set("batch_gather_lis." + i + ".gather_kb_map", new HashMap<>());
                }
                planUpdate.set("sample_kb_code_lis", new ArrayList<>());
                UpdateResult result = mongoTemplate.updateFirst(query1, planUpdate, "zj_plan_record");
                if (result.getModifiedCount() > 0){
                    return Result.ok("空白样删除成功");
                }else {
                    return Result.error("空白样删除失败");
                }
            }else {
                Query query1 = new Query();
                query1.addCriteria(Criteria.where("project_id").is(projectId));
                query1.addCriteria(Criteria.where("point_id").is(plan.getString("point_id")));
                query1.addCriteria(Criteria.where("multi_sub_name").is(plan.getString("multi_sub_name")));
                List<JSONObject> planLis1 = mongoTemplate.find(query1, JSONObject.class, "zj_plan_record");
                List<MongoUpdateDto> planFltUpdate = new ArrayList<>();
                for (JSONObject planOne : planLis1){
                    Update planUpdate = new Update().set("update_time", nowTime);
                    Update planReduction = new Update().set("update_time", planOne.getString("update_time"));
                    for (int i = 0; i < planOne.getJSONArray("batch_gather_lis").size(); i++){
                        planUpdate.set("batch_gather_lis." + i + ".sample_kb_code_lis", new ArrayList<>());
                        planUpdate.set("batch_gather_lis." + i + ".has_kb_code", 2);
                        planUpdate.set("batch_gather_lis." + i + ".gather_kb_map", new HashMap<>());
                        planReduction.set("batch_gather_lis." + i + ".sample_kb_code_lis", planOne.getJSONArray("batch_gather_lis").getJSONObject(i).getJSONArray("sample_kb_code_lis"));
                        planReduction.set("batch_gather_lis." + i + ".has_kb_code", planOne.getJSONArray("batch_gather_lis").getJSONObject(i).getString("has_kb_code"));
                        planReduction.set("batch_gather_lis." + i + ".gather_kb_map", planOne.getJSONArray("batch_gather_lis").getJSONObject(i).getJSONObject("gather_kb_map"));
                    }
                    planUpdate.set("sample_kb_code_lis", new ArrayList<>());
                    planReduction.set("sample_kb_code_lis", plan.getJSONArray("sample_kb_code_lis"));
                    MongoUpdateDto map = new MongoUpdateDto();
                    map.setFlt(new Query().addCriteria(Criteria.where("_id").is(planOne.getString("_id"))));
                    map.setUpdate(planUpdate);
                    map.setReduction(planReduction);
                    planFltUpdate.add(map);
                }
                try{
                    mongoUpdate(planFltUpdate);
                }catch (Exception e) {
                    mongoReduction(planFltUpdate);
                    return Result.ok("空白样删除失败", null);
                }
                return Result.ok("空白样删除成功", null);
            }
        }else {
            if (plan.getInteger("multi_substance") == 2){
                Update planUpdate = new Update().set("update_time",nowTime);
                List<Object> sampleKbCodeLis = new ArrayList<>();
                for (int i = 0; i < plan.getJSONArray("batch_gather_lis").size(); i++){
                    List<String> batchSampleKbCodeLis = new ArrayList<>();
                    String batchMinCode = plan.getJSONArray("batch_gather_lis").getJSONObject(i).getJSONArray("sample_code_lis").getString(0);
                    List<String> barCodeLis = getBarCodes(projectInfo.getCompanyOrder(), 3);
                    List<String> kbCodeList = Arrays.asList("1", "2", "3");
                    for (int barCodeI = 0; barCodeI < kbCodeList.size(); barCodeI++) {
                        String kbCodeN = kbCodeList.get(barCodeI);
                        String sampleKbCode = batchMinCode + "k" + kbCodeN;
                        batchSampleKbCodeLis.add(sampleKbCode);
                        JSONObject gatherOneParams = new JSONObject();
                        gatherOneParams.put("sample_code", sampleKbCode);
                        gatherOneParams.put("bar_code", barCodeLis.get(barCodeI));
                        JSONObject gatherOne = getGatherOneTmpl(gatherOneParams);
                        String updateKey = "batch_gather_lis." + i + ".gather_kb_map." + sampleKbCode;
                        planUpdate.set(updateKey, gatherOne);
                    }
                    planUpdate.set("batch_gather_lis." + i + ".sample_kb_code_lis", batchSampleKbCodeLis);
                    planUpdate.set("batch_gather_lis." + i + ".has_kb_code", 1);
                    sampleKbCodeLis.addAll(batchSampleKbCodeLis);
                }
                planUpdate.set("sample_kb_code_lis", sampleKbCodeLis);
                UpdateResult updateResult = mongoTemplate.updateFirst(new Query().addCriteria(Criteria.where("_id").is(plan.getString("_id"))), planUpdate, "zj_plan_record");
                if (updateResult.getModifiedCount() > 0){
                    return Result.ok("空白样设置成功", null);
                }else {
                    return Result.error("空白样设置失败").put("data", null);
                }
            }else {
                Query query1 = new Query();
                query1.addCriteria(Criteria.where("project_id").is(projectId));
                query1.addCriteria(Criteria.where("point_id").is(plan.getString("point_id")));
                query1.addCriteria(Criteria.where("multi_sub_name").is(plan.getString("multi_sub_name")));
                List<JSONObject> planLis1 = mongoTemplate.find(query1, JSONObject.class, "zj_plan_record");
                List<MongoUpdateDto> planFltUpdate = new ArrayList<>();
                Update planUpdate = new Update().set("update_time", nowTime);
                Update planReduction = new Update().set("update_time", plan.getString("update_time"));
                ArrayList<Object> addSampleKbCodeLis = new ArrayList<>();
                for (int i = 0; i < plan.getJSONArray("batch_gather_lis").size(); i++){
                    JSONObject batchGatherOne = plan.getJSONArray("batch_gather_lis").getJSONObject(i);
                    if (batchGatherOne.getInteger("has_kb_code")==2){
                        List<Object> batchSampleKbCodeLis = new ArrayList<>();
                        String batchMinCode = batchGatherOne.getJSONArray("sample_code_lis").getString(0);
                        List<String> barCodeLis = getBarCodes(projectInfo.getCompanyOrder(), 3);
                        List<String> kbCodeList = Arrays.asList("1", "2", "3");
                        for (int barCodeI = 0; barCodeI < kbCodeList.size(); barCodeI++) {
                            String kbCodeN = kbCodeList.get(barCodeI);
                            String sampleKbCode = batchMinCode + "k" + kbCodeN;
                            batchSampleKbCodeLis.add(sampleKbCode);
                            JSONObject gatherOneParams = new JSONObject();
                            gatherOneParams.put("sample_code", sampleKbCode);
                            gatherOneParams.put("bar_code", barCodeLis.get(barCodeI));
                            JSONObject gatherOne = getGatherOneTmpl(gatherOneParams);
                            String updateKey = "batch_gather_lis." + i + ".gather_kb_map." + sampleKbCode;
                            planUpdate.set(updateKey, gatherOne);
                        }
                        planUpdate.set("batch_gather_lis." + i + ".has_kb_code", 1);
                        planUpdate.set("batch_gather_lis." + i + ".sample_kb_code_lis", batchSampleKbCodeLis);
                        planReduction.set("batch_gather_lis." + i + ".has_kb_code", 2);
                        planReduction.set("batch_gather_lis." + i + ".sample_kb_code_lis", batchGatherOne.getJSONArray("sample_kb_code_lis"));
                        planReduction.set("batch_gather_lis." + i + ".gather_kb_map", batchGatherOne.getJSONObject("gather_kb_map"));
                        addSampleKbCodeLis.addAll(batchSampleKbCodeLis);
                    }
                }
                if (addSampleKbCodeLis.size()>0){
                    plan.getJSONArray("sample_kb_code_lis").addAll(addSampleKbCodeLis);
                    planUpdate.set("sample_kb_code_lis", plan.getJSONArray("sample_kb_code_lis"));
                    planReduction.set("sample_kb_code_lis", plan.getJSONArray("sample_kb_code_lis"));
                    for (JSONObject mP : planLis1){
                        planReduction.set("update_time", mP.getString("update_time"));
                        MongoUpdateDto map = new MongoUpdateDto();
                        map.setFlt(new Query().addCriteria(Criteria.where("_id").is(mP.getString("_id"))));
                        map.setUpdate(planUpdate);
                        map.setReduction(planReduction);
                        planFltUpdate.add(map);
                    }
                }
                if (planFltUpdate.size() > 0){
                    try{
                        mongoUpdate(planFltUpdate);
                    }catch (Exception e) {
                        mongoReduction(planFltUpdate);
                        return Result.error("空白样设置失败").put("data", null);
                    }
                }
                return Result.ok("空白样设置成功", null);
            }
        }
    }

    /**
     * 对zj_plan_record进行更新回退
     */
    private void mongoReduction(List<MongoUpdateDto> planFltUpdate) {
        for (MongoUpdateDto itemFu : planFltUpdate){
            mongoTemplate.updateFirst(itemFu.getFlt(), itemFu.getReduction(),"zj_plan_record");
        }
    }

    /**
     * 对zj_plan_record进行更新
     */
    private void mongoUpdate(List<MongoUpdateDto> planFltUpdate) {
        for (MongoUpdateDto itemFu : planFltUpdate){
            mongoTemplate.updateFirst(itemFu.getFlt(), itemFu.getUpdate(),"zj_plan_record");
        }
    }

    /**
     * 批量设置采样方案备注
     */
    public Result setPlanLis(JSONObject param){
        Long projectId = param.getLong("project_id");
        JSONArray dataLis = param.getJSONArray("plan_lis");
        if (dataLis.size() == 0){
            return Result.error(501,"请选择需要设置备注的采样方案").put("data", null);
        }
        List<String> ids = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        for (Object item : dataLis){
            JSONObject jObject = JSONObject.parseObject(JSON.toJSONString(item));
            String planId = jObject.getString("id");
            ids.add(planId);
            dataMap.put(planId, item);
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("project_id").is(projectId));
        query.addCriteria(Criteria.where("_id").in(ids));
        List<JSONObject> lis = mongoTemplate.find(query, JSONObject.class, "zj_plan_record");
        if (lis.size() != dataLis.size()){
            return Result.ok("需要设置备注的采样方案信息不正确", null).put("code", 403);
        }
        List<MongoUpdateDto> planFltUpdate = new ArrayList<>();
        Date nowTime = new Date();
        for (JSONObject plan : lis){
            String sampleNote = JSONObject.parseObject(JSON.toJSONString(dataMap.get(plan.getString("_id")))).getString("sample_note");
            MongoUpdateDto map = new MongoUpdateDto();
            map.setFlt(new Query().addCriteria(Criteria.where("_id").is(plan.get("_id"))));
            map.setUpdate(new Update().set("sample_note",sampleNote).set("update_time", nowTime));
            map.setReduction(new Update().set("sample_note",plan.getString("sample_note")).set("update_time", plan.getString("update_time")));
            planFltUpdate.add(map);
        }
        try{
            mongoUpdate(planFltUpdate);
        }catch (Exception e){
            mongoReduction(planFltUpdate);
            return Result.error("方案备注设置失败").put("data", null);
        }
        return Result.ok("方案备注设置成功", null);
    }

    private List<String> getBarCodes(String companyOrder, int num){
        Map<String, String> subjectionMap = new HashMap<>();
        subjectionMap.put("杭州安联","01");
        subjectionMap.put("嘉兴安联","02");
        subjectionMap.put("宁波安联","03");
        subjectionMap.put("上海量远","04");
        String theKey = DateUtil.format(new Date(), "yyyyMMdd") + subjectionMap.get(companyOrder);
        Long barCodeSort = redisTemplate.opsForHash().increment("max_bar_code", theKey, num);
        int barCodeMin = Math.toIntExact(barCodeSort - num + 1);
        String barCodePrefix = theKey.substring(2);
        List<String> resArr = new ArrayList<>();
        for (int i=0; i<num; i++){
            Integer currentBarCode = barCodeMin + i;
            String currentBarCodeStr = currentBarCode + "";
            int currentBarCodeStrLen = currentBarCodeStr.length();
            String barCodeOne;
            if (currentBarCodeStrLen < 4) {
                StringBuilder zeros = new StringBuilder();
                for (int j=0; j<4 - currentBarCodeStrLen; j++){
                    zeros.append("0");
                }
                barCodeOne = barCodePrefix + zeros + currentBarCodeStr;
            } else {
                barCodeOne = barCodePrefix + currentBarCodeStr;
            }
            resArr.add(barCodeOne);
        }
        return resArr;
    }

    /**
     * 批量设置采样日期
     */
    public Result setSampleDate(JSONObject param){
        int projectId = param.getInteger("project_id");
        JSONArray planIdStrLis = param.getJSONArray("ids");
        JSONArray dateLis = param.getJSONArray("sample_date_lis");
        if (planIdStrLis.size() == 0){
            return Result.error(403,"请选择需要设置日期的采样方案").put("data", null);
        }
        List<Object> planIds = new ArrayList<>();
        for (Object pIdStr : planIdStrLis){
            if (pIdStr != null){
                planIds.add(pIdStr);
            }
        }
        List<WeatherEntity> weatherLis = weatherService.list(new QueryWrapper<WeatherEntity>().eq("project_id", projectId));
        Sort.Order order1 = Sort.Order.asc("relation_pfn_ids.0");
        Sort.Order order2 = Sort.Order.asc("_id");
        Sort sort = Sort.by(order1, order2);
        List<JSONObject> planLis = mongoTemplate.find(new Query().addCriteria(Criteria.where("project_id").is(projectId)).with(sort),JSONObject.class,"zj_plan_record");
        List<LabAcceptProjectEntity> ap = acceptProjectService.list(new QueryWrapper<LabAcceptProjectEntity>().eq("project_id",projectId));
        List<String> deliveryDateLis = new ArrayList<>();
        if (ap.size() > 0){
            for (LabAcceptProjectEntity apOne : ap){
                Date gatherDate = apOne.getGatherDate();
                String date = DateUtil.format(gatherDate, "yyyy-MM-dd");
                deliveryDateLis.add(date);
            }
        }
        List<MongoUpdateDto> planFltUpdate = new ArrayList<>();
        List<String> allDateLis = new ArrayList<>();
        Date nowTime = new Date();
        for (JSONObject plan : planLis){
            if (planIds.contains(plan.getString("_id"))){
                for (int index = 0; index<plan.getJSONArray("batch_gather_lis").size(); index++){
                    JSONObject batchGatherOne = plan.getJSONArray("batch_gather_lis").getJSONObject(index);
                    String gatherDate = batchGatherOne.getString("gather_date");
                    String newDate;
                    if (!deliveryDateLis.contains(gatherDate)){
                        newDate = dateLis.getString(index);
                        if (deliveryDateLis.contains(newDate)){
                            // 能修改的日期不允许修改成送样日期
                            return Result.error(403,newDate+"已送样不允许设置该日期").put("data", null);
                        }
                    }else {
                        // 已送样的日期不允许再修改
                        return Result.error(403,gatherDate+"已送样不允许设置该日期").put("data", null);
                    }
                    if(newDate != null){
                        if (!allDateLis.contains(newDate)){
                            allDateLis.add(newDate);
                        }
                    }
                    MongoUpdateDto map = new MongoUpdateDto();
                    map.setFlt(new Query().addCriteria(Criteria.where("_id").is(plan.get("_id"))));
                    map.setUpdate(new Update().set("batch_gather_lis." + index + ".gather_date",newDate).set("update_time", nowTime));
                    map.setReduction(new Update().set("batch_gather_lis." + index + ".gather_date",batchGatherOne.getString("gather_date")).set("update_time", plan.getString("update_time")));
                    planFltUpdate.add(map);
                }
            }else {
                for (Object jsonObject : plan.getJSONArray("batch_gather_lis")){
                    JSONObject batchGatherOne = JSONObject.parseObject(JSONObject.toJSONString(jsonObject));
                    if (batchGatherOne.get("gather_date") != null && !allDateLis.contains(batchGatherOne.getString("gather_date"))){
                        allDateLis.add(batchGatherOne.getString("gather_date"));
                    }
                }
            }
        }
        Map<String, WeatherEntity> weatherMap = new HashMap<>();
        List<Long> weatherIds = new ArrayList<>();
        for (WeatherEntity itemW : weatherLis){
            String wDate = DateUtil.format(itemW.getGatherDate(), "yyyy-MM-dd");
            weatherMap.put(wDate, itemW);
            weatherIds.add(itemW.getId());
        }
        List<WeatherEntity> addLis = new ArrayList<>();
        for (String gatherDate : allDateLis){
            if (weatherMap.containsKey(gatherDate)){
                weatherIds.remove(weatherMap.get(gatherDate).getId());
            }else {
                WeatherEntity entity = new WeatherEntity();
                entity.setGatherDate(DateUtil.parse(gatherDate));
                entity.setProjectId((long)projectId);
                addLis.add(entity);
            }
        }
        try {
            mongoUpdate(planFltUpdate);
            if (addLis.size() > 0){
                weatherService.saveBatch(addLis);
            }
            if (weatherIds.size() > 0){
                weatherService.removeByIds(weatherIds);
            }
        }catch (Exception e){
            mongoReduction(planFltUpdate);
            return Result.error(500,"采样日期设置失败", null);
        }
        return Result.ok("采样日期设置成功", null);
    }

    /**
     * 批量设置采样时段
     */
    public Result setTimeFrame(JSONObject param){
        int projectId = param.getInteger("project_id");
        JSONArray planIdStrLis = param.getJSONArray("ids");
        JSONArray timeFrameLisS = param.getJSONArray("time_frame");
        List<String> timeFrameLis = new ArrayList<>();
        for (Object itemTF : timeFrameLisS){
            JSONObject item_t_f = JSONObject.parseObject(JSONObject.toJSONString(itemTF));
            String tFOne = "";
            if (item_t_f.getString("start_time") != null){
                tFOne = item_t_f.getString("start_time") + "-" + item_t_f.getString("end_time");
            }
            timeFrameLis.add(tFOne);
        }
        int timeFrameNum = timeFrameLis.size();
        if (planIdStrLis.size() == 0){
            return Result.error(403,"请选择需要设置时段的采样方案", null);
        }
        List<String> planIds = new ArrayList<>();
        for (Object pIdStr : planIdStrLis){
            if (pIdStr != null){
                planIds.add(pIdStr.toString());
            }
        }
        Sort.Order order1 = Sort.Order.asc("relation_pfn_ids.0");
        Sort.Order order2 = Sort.Order.asc("_id");
        Sort sort = Sort.by(order1, order2);
        Query query = new Query();
        query.addCriteria(Criteria.where("project_id").is(projectId));
        query.addCriteria(Criteria.where("_id").in(planIds));
        query.with(sort);
        List<JSONObject> planLis = mongoTemplate.find(query,JSONObject.class,"zj_plan_record");
        if (planLis.size() != planIds.size()){
            return Result.error(403, "需要设置时段的采样方案信息有误", null);
        }
        List<MongoUpdateDto> planFltUpdate = new ArrayList<>();
        Date nowTime = new Date();
        for (JSONObject plan : planLis){
            Update planUpdate = new Update().set("update_time", nowTime);
            Update planReduction = new Update().set("update_time", plan.getString("update_time"));
            if (timeFrameNum < plan.getInteger("sample_num")){
                return Result.error(403, "采样时段的个数不足，请重新设置", null);
            }
            for (int i = 0; i < plan.getJSONArray("batch_gather_lis").size(); i++){
                JSONObject batchGatherOne = plan.getJSONArray("batch_gather_lis").getJSONObject(i);
                int index = 0;
                for (String sampleCode : batchGatherOne.getJSONObject("gather_map").keySet()){
                    String updateKey = "batch_gather_lis." + i + ".gather_map." + sampleCode + ".time_frame";
                    planUpdate.set(updateKey, timeFrameLis.get(index));
                    planReduction.set(updateKey, batchGatherOne.getJSONObject("gather_map").getJSONObject(sampleCode).getString("time_frame"));
                    index+=1;
                }
            }
            List<String> totalTimeFrameLis = new ArrayList<>();
            for (int iT = 0; iT < plan.getInteger("sample_num"); iT++){
                totalTimeFrameLis.add(timeFrameLis.get(iT));
            }
            StringBuilder total_time_frame = totalTimeFrameLis.size() > 0 ? new StringBuilder(totalTimeFrameLis.get(0)) : new StringBuilder("");
            if (totalTimeFrameLis.size()>1){
                for (int i = 1; i < totalTimeFrameLis.size(); i++){
                    total_time_frame.append("、").append(totalTimeFrameLis.get(i));
                }
            }
            planUpdate.set("total_time_frame", total_time_frame.toString());
            planReduction.set("total_time_frame", plan.getString("total_time_frame"));
            MongoUpdateDto map = new MongoUpdateDto();
            map.setFlt(new Query().addCriteria(Criteria.where("_id").is(plan.get("_id"))));
            map.setUpdate(planUpdate);
            map.setReduction(planReduction);
            planFltUpdate.add(map);
        }
        try{
            mongoUpdate(planFltUpdate);
        }catch (Exception e){
            e.printStackTrace();
            mongoReduction(planFltUpdate);
            return Result.error(500, "采样时段设置失败", null);
        }
        return Result.ok("采样时段设置成功", null);
    }

    /**
     * 一键生成空白样
     */
    public Result generateKbCode(JSONObject param){
        int projectId = param.getInteger("project_id");
        Sort.Order order1 = Sort.Order.asc("relation_pfn_ids.0");
        Sort.Order order2 = Sort.Order.asc("_id");
        Sort sort = Sort.by(order1, order2);
        Query query = new Query();
        query.addCriteria(Criteria.where("project_id").is(projectId));
        query.with(sort);
        List<JSONObject> planLis = mongoTemplate.find(query,JSONObject.class,"zj_plan_record");
        List<ProjectEntity> projectLis = projectService.list(new QueryWrapper<ProjectEntity>().eq("id", projectId));
        ProjectEntity projectInfo = projectLis.get(0);
        JSONObject aloneKbMap = new JSONObject();
        List<MongoUpdateDto> planFltUpdate = new ArrayList<>();
        JSONObject mergePlanMap = new JSONObject();
        List<Map<String, Object>> multiSubTypeLis = new ArrayList<>();
        Date nowTime = new Date();
        for (JSONObject plan : planLis){
            JSONObject substanceInfo = plan.getJSONObject("substance").getJSONObject("substance_info");
            if (substanceInfo.getInteger("s_type") == 1){
                String sampleTablename = substanceInfo.getString("sample_tablename");
                List<String> condition = Arrays.asList("co", "co2");
                if (condition.contains(sampleTablename) || "游离二氧化硅".equals(plan.getJSONObject("substance").getString("name"))){
                    continue;
                }
                if (plan.getInteger("multi_substance") == 1){
                    if (!mergePlanMap.containsKey(plan.getString("point_id"))){
                        mergePlanMap.put(plan.getString("point_id"), new HashMap<>());
                    }
                    if (!mergePlanMap.getJSONObject(plan.getString("point_id")).containsKey(plan.getString("multi_sub_type"))){
                        Map<String, Object> map1 = new HashMap<>(2);
                        map1.put("point_id", plan.getString("point_id"));
                        map1.put("multi_sub_type", plan.getString("multi_sub_type"));
                        multiSubTypeLis.add(map1);

                        Map<String, Object> map2 = new HashMap<>(2);
                        map2.put("plan_lis", new ArrayList<>());
                        map2.put("sub_ids", new ArrayList<>());
                        mergePlanMap.getJSONObject(plan.getString("point_id")).put(plan.getString("multi_sub_type"), map2);
                    }
                    mergePlanMap.getJSONObject(plan.getString("point_id")).getJSONObject(plan.getString("multi_sub_type")).getJSONArray("plan_lis").add(plan);
                    mergePlanMap.getJSONObject(plan.getString("point_id")).getJSONObject(plan.getString("multi_sub_type")).getJSONArray("sub_ids").add(plan.getJSONObject("substance").getString("id"));
                }else {
                    Update planUpdate = new Update().set("update_time", nowTime);
                    Update planReduction = new Update().set("update_time", plan.getString("update_time"));
                    List<Object> addSampleKbCodeLis = new ArrayList<>();
                    for (int i = 0; i < plan.getJSONArray("batch_gather_lis").size(); i++){
                        JSONObject batchGatherOne = plan.getJSONArray("batch_gather_lis").getJSONObject(i);
                        String sampleDate = StringUtils.isNotBlank(batchGatherOne.getString("gather_date")) ? batchGatherOne.getString("gather_date") : "bk";
                        if (!aloneKbMap.containsKey(sampleDate)){
                            aloneKbMap.put(sampleDate, getKbMap());
                        }
                        JSONArray chemIds = aloneKbMap.getJSONObject(sampleDate).getJSONArray("chem_ids");
                        if (!chemIds.contains(plan.getJSONObject("substance").getString("id"))){
                            chemIds.add(plan.getJSONObject("substance").getString("id"));
                            if(batchGatherOne.getInteger("has_kb_code") == 2){
                                List<String> batchSampleKbCodeLis = new ArrayList<>();
                                String batchMinCode = batchGatherOne.getJSONArray("sample_code_lis").getString(0);
                                List<String> barCodeLis = getBarCodes(projectInfo.getCompanyOrder(),3);
                                List<String> kbCodeList = Arrays.asList("1", "2", "3");
                                for (int barCodeI = 0; barCodeI < kbCodeList.size(); barCodeI++) {
                                    String kbCodeN = kbCodeList.get(barCodeI);
                                    String sampleKbCode = batchMinCode + "k" + kbCodeN;
                                    batchSampleKbCodeLis.add(sampleKbCode);
                                    JSONObject gatherOneParams = new JSONObject();
                                    gatherOneParams.put("sample_code", sampleKbCode);
                                    gatherOneParams.put("bar_code", barCodeLis.get(barCodeI));
                                    JSONObject gatherOne = getGatherOneTmpl(gatherOneParams);
                                    String updateKey = "batch_gather_lis." + i + ".gather_kb_map." + sampleKbCode;
                                    planUpdate.set(updateKey, gatherOne);
                                }
                                planUpdate.set("batch_gather_lis." + i + ".has_kb_code", 1);
                                planUpdate.set("batch_gather_lis." + i + ".sample_kb_code_lis", batchSampleKbCodeLis);
                                planReduction.set("batch_gather_lis." + i + ".has_kb_code", 2);
                                planReduction.set("batch_gather_lis." + i + ".sample_kb_code_lis", batchGatherOne.getJSONArray("sample_kb_code_lis"));
                                planReduction.set("batch_gather_lis." + i + ".gather_kb_map", batchGatherOne.getJSONObject("gather_kb_map"));
                                addSampleKbCodeLis.addAll(batchSampleKbCodeLis);
                            }
                        }
                    }
                    fillPlanFltUpdate(planFltUpdate, plan, planUpdate, planReduction, addSampleKbCodeLis);
                }
            }
            else if (substanceInfo.getInteger("s_type") == 2){
                Update planUpdate = new Update().set("update_time", nowTime);
                Update planReduction = new Update().set("update_time", plan.get("update_time"));
                JSONArray addSampleKbCodeLis = new JSONArray();
                String fixedSoloKey = plan.getInteger("is_fixed") == 1 ? "dust_fixed_kb" : "dust_solo_kb";
                String dustKbKey = StringUtils.isNotBlank(plan.getJSONObject("substance").getJSONObject("substance_info").getString("total_dust_id")) && plan.getJSONObject("substance").getJSONObject("substance_info").getInteger("total_dust_id") != 0 ? "breath" : "total";
                for (int i = 0; i < plan.getJSONArray("batch_gather_lis").size(); i++){
                    JSONObject batchGatherOne = plan.getJSONArray("batch_gather_lis").getJSONObject(i);
                    String sampleDate = StringUtils.isNotBlank(batchGatherOne.getString("gather_date")) ? batchGatherOne.getString("gather_date") : "bk";
                    if (!aloneKbMap.containsKey(sampleDate)) {
                        aloneKbMap.put(sampleDate, getKbMap());
                    }
                    if (aloneKbMap.getJSONObject(sampleDate).getJSONObject(fixedSoloKey).getInteger(dustKbKey) ==2) {
                        aloneKbMap.getJSONObject(sampleDate).getJSONObject(fixedSoloKey).put(dustKbKey, 1);
                        if (batchGatherOne.getInteger("has_kb_code") == 2) {
                            List<String> batchSampleKbCodeLis = new ArrayList<>();
                            String batchMinCode = batchGatherOne.getJSONArray("sample_code_lis").getString(0);
                            List<String> barCodeLis = getBarCodes(projectInfo.getCompanyOrder(), 3);
                            List<String> kbCodeList = Arrays.asList("1", "2", "3");
                            for (int barCodeI = 0; barCodeI < kbCodeList.size(); barCodeI++) {
                                String kbCodeN = kbCodeList.get(barCodeI);
                                String sampleKbCode = batchMinCode + "k" + kbCodeN;
                                batchSampleKbCodeLis.add(sampleKbCode);
                                JSONObject gatherOneParams = new JSONObject();
                                gatherOneParams.put("sample_code", sampleKbCode);
                                gatherOneParams.put("bar_code", barCodeLis.get(barCodeI));
                                JSONObject gatherOne = getGatherOneTmpl(gatherOneParams);
                                String updateKey = "batch_gather_lis." + i + ".gather_kb_map." + sampleKbCode;
                                planUpdate.set(updateKey, gatherOne);
                            }
                            planUpdate.set("batch_gather_lis." + i + ".has_kb_code", 1);
                            planUpdate.set("batch_gather_lis." + i + ".sample_kb_code_lis", batchSampleKbCodeLis);
                            planReduction.set("batch_gather_lis." + i + ".has_kb_code", 2);
                            planReduction.set("batch_gather_lis." + i + ".sample_kb_code_lis", batchGatherOne.getJSONArray("sample_kb_code_lis"));
                            planReduction.set("batch_gather_lis." + i + ".gather_kb_map", batchGatherOne.getJSONObject("gather_kb_map"));
                            addSampleKbCodeLis.addAll(batchSampleKbCodeLis);
                        }
                    }
                }
                fillPlanFltUpdate(planFltUpdate, plan, planUpdate, planReduction, addSampleKbCodeLis);
            }
        }
        JSONObject mergeKbMap = new JSONObject();
        for (Map<String, Object> multiSubType : multiSubTypeLis){
            JSONObject subIdMap = mergePlanMap.getJSONObject(multiSubType.get("point_id").toString());
            String multiSubTypeKey = multiSubType.get("multi_sub_type").toString();
            JSONArray subIds = subIdMap.getJSONObject(multiSubTypeKey).getJSONArray("sub_ids");
            JSONArray mPlanLis = subIdMap.getJSONObject(multiSubTypeKey).getJSONArray("plan_lis");
            JSONObject mPlan = mPlanLis.getJSONObject(0);
            Update planUpdate = new Update().set("update_time", nowTime);
            Update planReduction = new Update().set("update_time", mPlan.getString("update_time"));
            List<Object> addSampleKbCodeLis = new ArrayList<>();
            for (int i = 0; i<mPlan.getJSONArray("batch_gather_lis").size(); i++){
                JSONObject batchGatherOne = mPlan.getJSONArray("batch_gather_lis").getJSONObject(i);
                String sampleDate = StringUtils.isNotBlank(batchGatherOne.getString("gather_date")) ? batchGatherOne.getString("gather_date") : "bk";
                if (!mergeKbMap.containsKey(sampleDate)){
                    mergeKbMap.put(sampleDate, new ArrayList<>());
                }
                int hasKb = 2;
                for (Object subId : subIds){
                    if (!mergeKbMap.getJSONArray(sampleDate).contains(subId)){
                        hasKb = 1;
                        mergeKbMap.getJSONArray(sampleDate).add(subId);
                    }
                }
                if (hasKb ==1){
                    if (batchGatherOne.getInteger("has_kb_code") == 2){
                        List<String> batchSampleKbCodeLis = new ArrayList<>();
                        String batchMinCode = batchGatherOne.getJSONArray("sample_code_lis").getString(0);
                        List<String> barCodeLis = getBarCodes(projectInfo.getCompanyOrder(), 3);
                        List<String> kbCodeList = Arrays.asList("1", "2", "3");
                        for (int barCodeI = 0; barCodeI < kbCodeList.size(); barCodeI++) {
                            String kbCodeN = kbCodeList.get(barCodeI);
                            String sampleKbCode = batchMinCode + "k" + kbCodeN;
                            batchSampleKbCodeLis.add(sampleKbCode);
                            JSONObject gatherOneParams = new JSONObject();
                            gatherOneParams.put("sample_code", sampleKbCode);
                            gatherOneParams.put("bar_code", barCodeLis.get(barCodeI));
                            JSONObject gatherOne = getGatherOneTmpl(gatherOneParams);
                            String updateKey = "batch_gather_lis." + i + ".gather_kb_map." + sampleKbCode;
                            planUpdate.set(updateKey, gatherOne);
                        }
                        planUpdate.set("batch_gather_lis." + i + ".has_kb_code", 1);
                        planUpdate.set("batch_gather_lis." + i + ".sample_kb_code_lis", batchSampleKbCodeLis);
                        planReduction.set("batch_gather_lis." + i + ".has_kb_code", 2);
                        planReduction.set("batch_gather_lis." + i + ".sample_kb_code_lis", batchGatherOne.getJSONArray("sample_kb_code_lis"));
                        planReduction.set("batch_gather_lis." + i + ".gather_kb_map", batchGatherOne.getJSONObject("gather_kb_map"));
                        addSampleKbCodeLis.addAll(batchSampleKbCodeLis);
                    }
                }
            }
            if (addSampleKbCodeLis.size() > 0){
                JSONArray mergeList = new JSONArray(mPlan.getJSONArray("sample_kb_code_lis"));
                mergeList.addAll(addSampleKbCodeLis);
                planUpdate.set("sample_kb_code_lis", mergeList);
                planReduction.set("sample_kb_code_lis", mPlan.getJSONArray("sample_kb_code_lis"));
                for (int l = 0; l < mPlanLis.size(); l++){
                    JSONObject mP = mPlanLis.getJSONObject(l);
                    MongoUpdateDto dto = new MongoUpdateDto();
                    dto.setFlt(new Query().addCriteria(Criteria.where("_id").is(mP.get("_id"))));
                    dto.setUpdate(planUpdate);
                    dto.setReduction(planReduction);
                    planFltUpdate.add(dto);
                }
            }
        }
        if (planFltUpdate.size() > 0){
            try{
                mongoUpdate(planFltUpdate);
            }catch (Exception e){
                mongoReduction(planFltUpdate);
                return Result.error(500,"空白样生成失败",null);
            }
        }
        return Result.ok("空白样生成成功", null);
    }

    private void fillPlanFltUpdate(List<MongoUpdateDto> planFltUpdate, JSONObject mPlan, Update planUpdate, Update planReduction, List<Object> addSampleKbCodeLis) {
        if (addSampleKbCodeLis.size() > 0){
            JSONArray array = new JSONArray();
            array.addAll(mPlan.getJSONArray("sample_kb_code_lis"));
            array.addAll(addSampleKbCodeLis);
            planUpdate.set("sample_kb_code_lis", array);
            planReduction.set("sample_kb_code_lis", mPlan.getJSONArray("sample_kb_code_lis"));
            MongoUpdateDto map = new MongoUpdateDto();
            map.setFlt(new Query().addCriteria(Criteria.where("_id").is(mPlan.get("_id"))));
            map.setUpdate(planUpdate);
            map.setReduction( planReduction);
            planFltUpdate.add(map);
        }
    }

    private JSONObject getKbMap(){
        JSONObject returnMap = new JSONObject();
        JSONObject oneMap = new JSONObject();
        oneMap.put("total", 2);
        oneMap.put("breath", 2);
        JSONObject oneMap1 = new JSONObject();
        oneMap1.put("total", 2);
        oneMap1.put("breath", 2);
        returnMap.put("chem_ids", new ArrayList<>());
        returnMap.put("dust_fixed_kb", oneMap);
        returnMap.put("dust_solo_kb", oneMap1);
        return returnMap;
    }

    /**
     * 采样方案的生成部分字段需要从现场调查获取 除了岗位/点位/工种名称 物质信息这些  还需要作业人数/作业内容/设备信息/ppe/eqp等
     * TODO 作业人数/作业内容/设备信息/ppe/eqp的定点说明:   对于定点采样记录来说  找出在该岗位作业人数最多的工种  该工种在该岗位的作业人数/作业内容/ppe 代入采样记录中  设备和epe该岗位的设备和epe信息
     * TODO 作业人数/作业内容/设备信息/ppe/eqp的个体说明:   对于个体采样记录来说  将所有作业岗位的作业内容/设备/epe汇总到采样记录里  然后采样人员自行修改  ppe也是汇总但要去重
     */
    @Transactional(rollbackFor = Exception.class)
    public Result generatePlan(@RequestBody JSONObject params){
        // TODO: 2023/9/6 如果有收样信息则返回"已收养项目不可重新生成采样方案"

        int projectId = params.getInteger("project_id");
        // 基础数据获取
        List<ProjectEntity> projectLis = projectService.list(new QueryWrapper<ProjectEntity>().eq("id", projectId));
        List<CompanySurveyEntity> comSurveyLis = companySurveyService.list(new QueryWrapper<CompanySurveyEntity>().eq("project_id",projectId));
        if (projectLis.size() == 0 || comSurveyLis.size() == 0){
            return Result.error(501,"该项目不存在", null);
        }
        CompanySurveyEntity comSurveyInfo = comSurveyLis.get(0);
        String comIdentifier = comSurveyInfo.getIdentifier();
        if (comSurveyInfo.getIdentifier().contains("-")){
            String[] identifierParts = comSurveyInfo.getIdentifier().split("-");
            comIdentifier = identifierParts[1] + identifierParts[0].substring(identifierParts[0].length() - 2) + identifierParts[2];
        }
        ProjectEntity projectInfo = projectLis.get(0);
        if (StringUtils.isBlank(projectInfo.getCompanyOrder())){
            return Result.error(501,"该项目没有指定项目隶属公司，无法生成采样方案",null);
        }
        List<JSONObject> workSpaceLis = mongoTemplate.find(new Query().addCriteria(Criteria.where("project_id").is(projectId)), JSONObject.class, "zj_workspace");
        List<JSONObject> pfnLis = mongoTemplate.find(new Query().addCriteria(Criteria.where("project_id").is(projectId)), JSONObject.class, "zj_post_pfn");
        if (workSpaceLis.size() == 0 || pfnLis.size() == 0){
            return Result.error(500,"该项目没有录入调查信息无法生成采样方案", null);
        }
        JSONObject eqLayoutPostIdMap = getEqLayoutPostIdMapByPjtId(projectId);
        JSONObject htLimitMap = getHtLimit();
        List<Map<Integer,JSONObject>> list1 = samplePlanSubstanceMap(projectInfo.getCompanyOrder());
        JSONArray list2 = getProjectPlanMap(projectId);
        JSONArray list3 = getProjectResultMap(projectId);
        Map<Integer, JSONObject> fixedSstMap = list1.get(0);
        Map<Integer, JSONObject> soloSstMap = list1.get(1);
        JSONObject oldFixedPlanMap = list2.getJSONObject(0);
        JSONObject oldSoloPlanMap = list2.getJSONObject(1);
//        JSONObject oldKbMap = list2.getJSONObject(2);
        JSONArray oldPlanLis = list2.getJSONArray(3);
        JSONObject oldFixedResultMap = list3.getJSONObject(0);
        JSONObject oldSoloResultMap = list3.getJSONObject(1);
        JSONArray oldResultLis = list3.getJSONArray(2);

        JSONObject workspaceMap = new JSONObject();
        JSONObject areaMap = new JSONObject();
        List<Object> countWspLis = new ArrayList<>();
        for (JSONObject itemWs : workSpaceLis){
            String postIdStr = itemWs.getString("_id");
            String wsIdStr = itemWs.getString("workshop_id");
            String areaIdStr = itemWs.getString("area_id");
            workspaceMap.put(postIdStr, itemWs);
            String workshop = itemWs.getString("workshop");
            if (StringUtils.isNotBlank(workshop) && !countWspLis.contains(workshop)){
                countWspLis.add(workshop);
            }
            if(!areaMap.containsKey(areaIdStr)){
                JSONObject map = new JSONObject();
                map.put("workshop_id", wsIdStr);
                map.put("workshop", workshop);
                map.put("area_id", areaIdStr);
                map.put("area", itemWs.getString("area"));
                map.put("workshop_area", itemWs.getString("workshop_area"));
                areaMap.put(areaIdStr, map);
            }
        }
        JSONObject pfnMap = new JSONObject();
        JSONObject postPfnMap = new JSONObject();
        JSONObject pfnOperationMap = new JSONObject();
        for (JSONObject pfnObj : pfnLis){
            String pfnIdStr = pfnObj.getString("_id");
            pfnMap.put(pfnIdStr, pfnObj);
            if (!pfnOperationMap.containsKey(pfnIdStr)){
                JSONObject map = new JSONObject();
                map.put("ppe_name_lis", new ArrayList<>());
                map.put("ppe_lis", new ArrayList<>());
                map.put("work_content_lis", new ArrayList<>());
                map.put("equip_name_lis", new ArrayList<>());
                map.put("equip_lis", new ArrayList<>());
                map.put("epe_name_lis", new ArrayList<>());
                map.put("epe_lis", new ArrayList<>());
                pfnOperationMap.put(pfnIdStr, map);
            }
            for (String postIdStr : pfnObj.getJSONObject("work_track").keySet()){
                if (pfnObj.getJSONObject("work_track").getJSONObject(postIdStr).containsKey("ppe")){
                    for (Object ppe : pfnObj.getJSONObject("work_track").getJSONObject(postIdStr).getJSONArray("ppe")){
                        JSONObject ppeInfo = JSONObject.parseObject(JSON.toJSONString(ppe));
                        JSONArray PpeNameLis = pfnOperationMap.getJSONObject(pfnIdStr).getJSONArray("ppe_name_lis");
                        if (!PpeNameLis.contains(ppeInfo.get("name"))){
                            PpeNameLis.add(ppeInfo.get("name"));
                            Map<String, Object> map = new HashMap<>();
                            map.put("name", ppeInfo.get("name"));
                            map.put("working", ppeInfo.get("ppe_wear"));
                            pfnOperationMap.getJSONObject(pfnIdStr).getJSONArray("ppe_lis").add(map);
                        }
                    }
                }
                JSONArray epeMap = workspaceMap.getJSONObject(postIdStr).getJSONArray("epe");
                if (epeMap != null){
                    for (Object epe : epeMap){
                        JSONObject epeInfo = JSONObject.parseObject(JSON.toJSONString(epe));
                        JSONArray ppeNameLis = pfnOperationMap.getJSONObject(pfnIdStr).getJSONArray("epe_name_lis");
                        if (!ppeNameLis.contains(epeInfo.get("engineering_protection"))){
                            ppeNameLis.add(epeInfo.get("engineering_protection"));
                            JSONObject map = new JSONObject();
                            map.put("name", epeInfo.get("engineering_protection"));
                            map.put("working", epeInfo.get("run_condition"));
                            pfnOperationMap.getJSONObject(pfnIdStr).getJSONArray("epe_lis").add(map);
                        }
                    }
                }
                JSONArray equipInfos = new JSONArray();
                if (eqLayoutPostIdMap!=null && eqLayoutPostIdMap.size() != 0){
                    equipInfos = eqLayoutPostIdMap.getJSONArray(postIdStr);
                }
                if (equipInfos != null){
                    for (Object object : equipInfos){
                        JSONObject equipInfo = JSONObject.parseObject(JSON.toJSONString(object));
                        JSONArray equipNameLis = pfnOperationMap.getJSONObject(pfnIdStr).getJSONArray("equip_name_lis");
                        if (!equipNameLis.contains(equipInfo.get("name"))){
                            equipNameLis.add(equipInfo.get("name"));
                            pfnOperationMap.getJSONObject(pfnIdStr).getJSONArray("equip_lis").add(equipInfo);
                        }
                    }
                }
                JSONArray workContentLis = pfnOperationMap.getJSONObject(pfnIdStr).getJSONArray("work_content_lis");
                Object workSituation = pfnObj.getJSONObject("work_track").getJSONObject(postIdStr).get("work_situation");
                if (!workContentLis.contains(workSituation)){
                    workContentLis.add(workSituation);
                }
                if (!postPfnMap.containsKey(postIdStr)){
                    Map<String,Object> map = new HashMap<>();
                    map.put("largest_people_pfn_id", "");
                    map.put("largest_people_pfn_num", 0);
                    map.put("pfn_num", 0);
                    map.put("pfn_id_lis", new ArrayList<>());
                    postPfnMap.put(postIdStr,map);
                }
                JSONObject postIdValue = postPfnMap.getJSONObject(postIdStr);
                if (!postIdValue.getJSONArray("pfn_id_lis").contains(pfnIdStr)){
                    postIdValue.put("pfn_num", postIdValue.getInteger("pfn_num") + 1);
                    postIdValue.getJSONArray("pfn_id_lis").add(pfnIdStr);
                    if (StringUtils.isNotBlank(postIdValue.getString("largest_people_pfn_id"))){
                        if (pfnObj.getInteger("people_num") > postIdValue.getInteger("largest_people_pfn_num")){
                            postIdValue.put("largest_people_pfn_id", pfnIdStr);
                            postIdValue.put("largest_people_pfn_num", pfnObj.get("people_num"));
                        }
                    }else {
                        postIdValue.put("largest_people_pfn_id", pfnIdStr);
                        postIdValue.put("largest_people_pfn_num", pfnObj.get("people_num"));
                    }
                }
            }
        }

        // 方案生成 0准备接收数据
        Date nowTime = new Date();
        Integer maxPointCode = comSurveyInfo.getMaxPointCode() != null && comSurveyInfo.getMaxPointCode() != 0 ? comSurveyInfo.getMaxPointCode() : 0;
        // 记录需要修改的岗位信息
        List<MongoUpdateDto> wsFltUpdate = new ArrayList<>();
        // 记录需要修改的工种信息
        List<MongoUpdateDto> pfnFltUpdate = new ArrayList<>();
        // 记录需要新增的数据(采样方案)
        JSONArray addLis = new JSONArray();
        // 记录需要新增的结果计算数据
        JSONArray addResultLis = new JSONArray();
        // 已经生成方案的岗位id
        JSONArray hasGenerateWsIds = new JSONArray();
        // 项目是否有化学样品
        int projectHasChemistry = 2;
        // 项目是否检测物理
        int projectHasPhysic = 2;
        // 方案生成 1处理工种对应的定点信息
        for (JSONObject itemPfn : pfnLis){
            String pfnIdStr = itemPfn.getString("_id");
//            int hasMergeSub;
            for (String postIdStr : itemPfn.getJSONObject("work_track").keySet()){
                if (hasGenerateWsIds.contains(postIdStr)){
                    continue;
                }
                int postHasTestSiO2 = 0;
                JSONObject wsObj = workspaceMap.getJSONObject(postIdStr);
                String postLargestPeoplePfnId = postPfnMap.getJSONObject(postIdStr).getString("largest_people_pfn_id");
                JSONObject pfnMapValue = pfnMap.getJSONObject(postLargestPeoplePfnId);
                Map<String, Object> postOperationInfo = new HashMap<>();
                List<Map<String, Object>> epeOutList = new ArrayList<>();
                List<Map<String, Object>> ppeOutList = new ArrayList<>();
                postOperationInfo.put("people_num", pfnMapValue.get("people_num"));
                postOperationInfo.put("shift_num", pfnMapValue.get("shift_num"));
                postOperationInfo.put("work_content", pfnMapValue.getJSONObject("work_track").getJSONObject(postIdStr).get("work_situation"));
                postOperationInfo.put("equip_lis", eqLayoutPostIdMap.get(postIdStr));
                if (wsObj.getJSONArray("epe") != null){
                    for (Object object : wsObj.getJSONArray("epe")){
                        Map<String, Object> epe = new HashMap<>();
                        JSONObject itemEpe = JSONObject.parseObject(JSON.toJSONString(object));
                        epe.put("name", itemEpe.get("engineering_protection"));
                        epe.put("working", itemEpe.get("run_condition"));
                        epeOutList.add(epe);
                    }
                }
                postOperationInfo.put("epe", epeOutList);
                JSONArray ppeMap = pfnMap.getJSONObject(postLargestPeoplePfnId).getJSONObject("work_track").getJSONObject(postIdStr).getJSONArray("ppe");
                if (ppeMap != null){
                    for (Object object : ppeMap){
                        Map<String, Object> ppe = new HashMap<>();
                        JSONObject itemPpe = JSONObject.parseObject(JSON.toJSONString(object));
                        ppe.put("name", itemPpe.get("name"));
                        ppe.put("working", itemPpe.get("ppe_wear"));
                        ppeOutList.add(ppe);
                    }
                }
                postOperationInfo.put("ppe", ppeOutList);
                Integer postPeopleNum = (Integer) postOperationInfo.get("people_num");
                Integer postOperatorsNum = (Integer) postOperationInfo.get("shift_num");
                JSONArray dealWorkspaceHazard = dealWorkspaceHazards(wsObj.getJSONArray("fixed_hazards"), fixedSstMap);
//                hasMergeSub = (int)dealWorkspaceHazard.get(0);
                JSONObject mergeSubMap = dealWorkspaceHazard.getJSONObject(1);
                JSONArray subLis = dealWorkspaceHazard.getJSONArray(2);
                Update wsUpdate = new Update().set("update_time", nowTime);
                Update wsReduction = new Update().set("update_time", itemPfn.getString("update_time"));
                JSONObject wsOldPlanMap = oldFixedPlanMap.containsKey(postIdStr) ? oldFixedPlanMap.getJSONObject(postIdStr).getJSONObject("point_map") : new JSONObject();
                for(Object pointId : wsObj.getJSONArray("point_ids")){
                    JSONObject pointOldPlanMap = wsOldPlanMap.containsKey(pointId.toString()) ? wsOldPlanMap.getJSONObject(pointId.toString()).getJSONObject("sub_map") : new JSONObject();
                    JSONObject pointOldResultMap = oldFixedResultMap.containsKey(pointId.toString()) ? oldFixedResultMap.getJSONObject(pointId.toString()) : new JSONObject();
                    JSONObject pointObj = wsObj.getJSONObject("point_map").getJSONObject(pointId.toString());
                    Integer maxSampleCode = pointObj.getInteger("max_sample_code") == null ? pointObj.getInteger("maxSampleCode") : pointObj.getInteger("max_sample_code");
                    JSONArray workspacePoint = dealWorkspacePoint(pointObj,maxPointCode,wsUpdate,wsReduction);
                    Integer pointCode = (Integer) workspacePoint.get(0);
                    String pointCodeStr = workspacePoint.get(1).toString();
                    JSONObject pointDustContactDurationMap = new JSONObject();
                    for (int i = 0; i<subLis.size(); i++){
                        JSONObject itemSub = subLis.getJSONObject(i);
                        String substanceId = itemSub.getString("substance_id");
                        JSONObject subData = fixedSstMap.get(Integer.parseInt(substanceId));
                        Integer sampleDays = 0;
                        Integer sampleNum = 0;
                        for (Object o2 : itemSub.getJSONArray("pfn_ids")){
                            String sPfnIdStr = o2.toString();
                            Integer sPfnSampleNum = pfnMap.getJSONObject(sPfnIdStr).getJSONObject("work_track")
                                    .getJSONObject(postIdStr).getJSONObject("touch_hazards").getJSONObject(substanceId).getInteger("sample_num");
                            Integer sPfnSampleDays = pfnMap.getJSONObject(sPfnIdStr).getJSONObject("work_track")
                                    .getJSONObject(postIdStr).getJSONObject("touch_hazards").getJSONObject(substanceId).getInteger("sample_days");
                            if (sPfnSampleNum != null && sPfnSampleNum > sampleNum){
                                sampleNum = sPfnSampleNum;
                            }
                            if (sPfnSampleDays != null && sPfnSampleDays > sampleDays){
                                sampleDays = sPfnSampleDays;
                            }
                        }
                        if (sampleDays == null || sampleNum == null){
                            continue;
                        }
                        if("游离二氧化硅".equals(itemSub.getString("name"))){
                            if (postHasTestSiO2 == 1){
                                continue;
                            }
                            List<String> list = Arrays.asList("-1", "0", "");
                            if (!list.contains(wsObj.getString("silica_share"))){
                                postHasTestSiO2 = 1;
                                continue;
                            }else {
                                postHasTestSiO2 = 1;
                            }
                        }
                        ObjectId planId = new ObjectId();
                        JSONObject oldPlan = new JSONObject();
                        JSONObject piece = new JSONObject();
                        piece.put("people_num",0);
                        piece.put("equip_name","");
                        piece.put("equip_lis",new ArrayList<>());
                        piece.put("work_content","");
                        piece.put("equip_working_lis",new ArrayList<>());
                        piece.put("epe_working_lis",new ArrayList<>());
                        piece.put("ppe_working_lis",new ArrayList<>());
                        oldPlan.put("m_id",planId);
                        oldPlan.put("batch_gather_lis",new JSONArray());
                        oldPlan.put("sample_days",0);
                        oldPlan.put("sample_num",0);
                        oldPlan.put("sample_note","");
                        oldPlan.put("operation",piece);

                        JSONObject oldResultMap = pointOldResultMap.containsKey(substanceId) ? pointOldResultMap.getJSONObject(substanceId) : new JSONObject();
                        if (pointOldPlanMap.containsKey(substanceId)){
                            JSONObject oldPlanOperation = pointOldPlanMap.getJSONObject(substanceId).getJSONObject("operation");
                            oldPlan.put("m_id", pointOldPlanMap.getJSONObject(substanceId).get("m_id"));
                            oldPlan.put("batch_gather_lis", pointOldPlanMap.getJSONObject(substanceId).get("batch_gather_lis"));
                            oldPlan.put("sample_days", pointOldPlanMap.getJSONObject(substanceId).get("sample_days"));
                            oldPlan.put("sample_num", pointOldPlanMap.getJSONObject(substanceId).get("sample_num"));
                            oldPlan.put("sample_note", pointOldPlanMap.getJSONObject(substanceId).get("sample_note"));
                            oldPlan.getJSONObject("operation").put("people_num", oldPlanOperation.get("people_num"));
                            oldPlan.getJSONObject("operation").put("equip_name", oldPlanOperation.get("equip_name"));
                        }
                        JSONObject planDict = getPlanTmpl(planId, wsObj, null, pointObj, subData, itemSub, mergeSubMap, nowTime);
                        planDict.put("identifier", comIdentifier);
                        planDict.put("m_id", oldPlan.getString("m_id"));
                        planDict.put("project_id", projectId);
                        planDict.put("point_code", pointCodeStr);
                        planDict.put("point_code_num", pointCode);
                        planDict.getJSONObject("operation").put("people_num", postPeopleNum);
                        planDict.getJSONObject("operation").put("equip_name", oldPlan.getJSONObject("operation").get("equip_name"));
                        planDict.getJSONObject("operation").put("equip_lis", postOperationInfo.get("equip_lis"));
                        planDict.getJSONObject("operation").put("work_content", postOperationInfo.get("work_content"));
                        planDict.put("sample_note", oldPlan.get("sample_note"));
                        oldPlan.getJSONObject("operation").put("operators_num", postOperatorsNum);
                        oldPlan.getJSONObject("operation").put("work_content", postOperationInfo.get("work_content"));
                        oldPlan.getJSONObject("operation").put("equip_working_lis", postOperationInfo.get("equip_lis"));
                        oldPlan.getJSONObject("operation").put("epe_working_lis", postOperationInfo.get("epe"));
                        oldPlan.getJSONObject("operation").put("ppe_working_lis", postOperationInfo.get("ppe"));
                        JSONArray resultDictLis = new JSONArray();
                        JSONObject newContactDurationMap;
                        if (itemSub.getInteger("s_type") == 1){
                            String sampleTablename = subData.getString("sample_tablename");
                            if ("co".equals(sampleTablename) || "co2".equals(sampleTablename)){
                                int hasCode = 2;
                                maxSampleCode = dealsubPlan(hasCode, maxSampleCode, planDict, oldPlan, sampleDays, sampleNum, projectInfo.getCompanyOrder());
                                JSONArray result = getChemistryResultTmpl(planDict, itemSub, pfnMap, oldResultMap, nowTime, null, null);
                                resultDictLis = result.getJSONArray(0);
                                newContactDurationMap = result.getJSONObject(1);
                                for (int dayI = 0; dayI<planDict.getJSONArray("batch_gather_lis").size(); dayI++ ){
                                    JSONObject pBatchGatherOne = planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI);
                                    for (String pCode : pBatchGatherOne.getJSONObject("gather_map").keySet()){
                                        Object codeContactDuration =  pBatchGatherOne.getJSONObject("gather_map").getJSONObject(pCode).get("contact_duration");
                                        if(newContactDurationMap.getJSONObject(pCode).getInteger("has_valid_value") == 1){
                                            if (!newContactDurationMap.getJSONObject(pCode).getJSONArray("lis").contains(codeContactDuration)){
                                                Random random = new Random();
                                                int anInt = random.nextInt(newContactDurationMap.getJSONObject(pCode).getJSONArray("lis").size());
                                                planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(pCode).put("contact_duration", newContactDurationMap.getJSONObject(pCode).getJSONArray("lis").getString(anInt));
                                            }
                                        }else {
                                            planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(pCode).put("contact_duration", "");
                                        }
                                    }
                                }
                                projectHasChemistry = 1;
                            }else if ("游离二氧化硅".equals(itemSub.getString("name"))){
                                int hasCode = 1;
                                sampleDays = 1;
                                sampleNum = 1;
                                maxSampleCode = dealsubPlan(hasCode, maxSampleCode, planDict, oldPlan, sampleDays, sampleNum, projectInfo.getCompanyOrder());
                                resultDictLis = new JSONArray();
                            }else {
                                int hasCode = 1;
                                if (StringUtils.isNotBlank(subData.getString("total_merge_name")))  {
                                    JSONObject mergePlanTmpl = new JSONObject();
                                    mergePlanTmpl.put("batch_gather_lis", new JSONArray());
                                    mergePlanTmpl.put("sample_days", 0);
                                    mergePlanTmpl.put("sample_num", 0);
                                    if (oldPlan.getJSONArray("batch_gather_lis").size() > 0 && mergePlanTmpl.getJSONArray("batch_gather_lis").size() == 0){
                                        for (int j = 0; j < oldPlan.getJSONArray("batch_gather_lis").size(); j++){
                                            JSONObject batchGatherDict = (JSONObject) oldPlan.getJSONArray("batch_gather_lis").getJSONObject(j).clone();
                                            // 不能共用检测结果
                                            for (String bglCodeOne : batchGatherDict.getJSONObject("gather_map").keySet()){
                                                batchGatherDict.getJSONObject("gather_map").getJSONObject(bglCodeOne).put("result", new JSONObject());
                                            }
                                            for (String bglKbCodeOne : batchGatherDict.getJSONObject("gather_kb_map").keySet()){
                                                batchGatherDict.getJSONObject("gather_kb_map").getJSONObject(bglKbCodeOne).put("result", new JSONObject());
                                            }
                                            mergePlanTmpl.getJSONArray("batch_gather_lis").add(batchGatherDict);
                                        }
                                    }
                                    mergePlanTmpl.put("sample_days", sampleDays);
                                    mergePlanTmpl.put("sample_num", sampleNum);
                                    newContactDurationMap = new JSONObject();
                                    JSONObject mergeContactDurationMapTpl = new JSONObject();
                                    JSONObject planNewContactDurationMap = new JSONObject();
                                    if (itemSub.get("merge_sub_lis") != null){
                                        for (int k = 0; k < itemSub.getJSONArray("merge_sub_lis").size(); k++){
                                            JSONObject itemSubM = itemSub.getJSONArray("merge_sub_lis").getJSONObject(k);
                                            ObjectId mPlanId = new ObjectId();
                                            subData = fixedSstMap.get(Integer.parseInt(substanceId));
                                            planDict = getPlanTmpl(mPlanId, wsObj, null, pointObj, subData, itemSubM, mergeSubMap, nowTime);
                                            oldPlan = new JSONObject();
                                            JSONObject operationMap = new JSONObject();
                                            oldPlan.put("m_id", mPlanId);
                                            oldPlan.put("batch_gather_lis", new JSONArray());
                                            oldPlan.put("sample_days", 0);
                                            oldPlan.put("sample_num", 0);
                                            operationMap.put("people_num", 0);
                                            operationMap.put("equip_name", "");
                                            operationMap.put("equip_lis", new JSONArray());
                                            operationMap.put("work_content", "");
                                            operationMap.put("equip_working_lis", new JSONArray());
                                            operationMap.put("epe_working_lis", new JSONArray());
                                            operationMap.put("ppe_working_lis", new JSONArray());
                                            oldPlan.put("operation",operationMap);
                                            if (pointOldPlanMap.containsKey(substanceId)){
                                                JSONObject oldPlanOperation = pointOldPlanMap.getJSONObject(substanceId).getJSONObject("operation");
                                                oldPlan = new JSONObject();
                                                operationMap = new JSONObject();
                                                oldPlan.put("m_id", pointOldPlanMap.getJSONObject(substanceId).get("m_id"));
                                                oldPlan.put("batch_gather_lis", pointOldPlanMap.getJSONObject(substanceId).get("batch_gather_lis"));
                                                oldPlan.put("sample_days", pointOldPlanMap.getJSONObject(substanceId).get("sample_days"));
                                                oldPlan.put("sample_num", pointOldPlanMap.getJSONObject(substanceId).get("sample_num"));
                                                operationMap.put("people_num", oldPlanOperation.get("people_num") != null ? oldPlanOperation.get("people_num") : 0);
                                                operationMap.put("equip_name", oldPlanOperation.get("equip_name") != null ? oldPlanOperation.get("equip_name") : "");
                                                operationMap.put("equip_lis", new JSONArray());
                                                operationMap.put("work_content", "");
                                                operationMap.put("equip_working_lis", new JSONArray());
                                                operationMap.put("epe_working_lis", new JSONArray());
                                                operationMap.put("ppe_working_lis", new JSONArray());
                                                oldPlan.put("operation",operationMap);
                                                oldPlan.put("sample_note", pointOldPlanMap.getJSONObject(substanceId).get("sample_note"));
                                            }
                                            planDict.put("identifier", comIdentifier);
                                            planDict.put("m_id", oldPlan.get("m_id"));
                                            planDict.put("project_id", projectId);
                                            planDict.put("point_code", pointCodeStr);
                                            planDict.put("point_code_num", pointCode);
                                            operationMap = new JSONObject();
                                            operationMap.put("people_num",postPeopleNum);
                                            operationMap.put("equip_name",oldPlan.getJSONObject("operation").get("equip_name"));
                                            operationMap.put("equip_lis",postOperationInfo.get("equip_lis"));
                                            operationMap.put("work_content",postOperationInfo.get("work_content"));
                                            planDict.put("operation", operationMap);
                                            oldPlan.getJSONObject("operation").put("operators_num", postOperatorsNum);
                                            oldPlan.getJSONObject("operation").put("work_content", postOperationInfo.get("work_content"));
                                            oldPlan.getJSONObject("operation").put("equip_working_lis", postOperationInfo.get("equip_lis"));
                                            oldPlan.getJSONObject("operation").put("epe_working_lis", postOperationInfo.get("epe"));
                                            oldPlan.getJSONObject("operation").put("ppe_working_lis", postOperationInfo.get("ppe"));
                                            maxSampleCode = dealMergeSubPlan(hasCode, maxSampleCode, planDict, oldPlan, sampleDays, sampleNum, mergePlanTmpl, projectInfo.getCompanyOrder());
                                            JSONArray result = getChemistryResultTmpl(planDict, itemSub, pfnMap, oldResultMap, nowTime, mergeContactDurationMapTpl, null);
                                            resultDictLis = result.getJSONArray(0);
                                            JSONObject mergeNewContactDurationMap = result.getJSONObject(1);
                                            if (newContactDurationMap == null || newContactDurationMap.isEmpty()){
                                                newContactDurationMap = mergeNewContactDurationMap;
                                                for (int dayI = 0; dayI < planDict.getJSONArray("batch_gather_lis").size(); dayI++){
                                                    JSONObject pBatchGatherOne = planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI);
                                                    for (String pCode : pBatchGatherOne.getJSONObject("gather_map").keySet()){
                                                        Object codeContactDuration = pBatchGatherOne.getJSONObject("gather_map").getJSONObject(pCode).get("contact_duration");
                                                        if (newContactDurationMap.getJSONObject(pCode).getInteger("has_valid_value") == 1){
                                                            if (!newContactDurationMap.getJSONObject(pCode).getJSONArray("lis").contains(codeContactDuration)){
                                                                Random random = new Random();
                                                                JSONArray lis = newContactDurationMap.getJSONObject(pCode).getJSONArray("lis");
                                                                planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(pCode).put("contact_duration", lis.getString(random.nextInt(lis.size())));
                                                            }
                                                            planNewContactDurationMap.put(pCode, planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(pCode).get("contact_duration"));
                                                        }else {
                                                            planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(pCode).put("contact_duration", "");
                                                            planNewContactDurationMap.put(pCode, "");
                                                        }
                                                    }
                                                }
                                                for (int m = 0; m < resultDictLis.size(); m++){
                                                    JSONObject itemRes = resultDictLis.getJSONObject(m);
                                                    mergeContactDurationMapTpl.put(itemRes.getString("pfn_id"), new JSONArray());
                                                    for (String resCode : itemRes.getJSONArray("batch_sample_lis").getJSONObject(0).getJSONObject("gather_map").keySet()){
                                                        mergeContactDurationMapTpl.getJSONArray(itemRes.getString("pfn_id")).add(itemRes.getJSONArray("batch_sample_lis").getJSONObject(0).getJSONObject("gather_map").getJSONObject(resCode).get("contact_duration"));
                                                    }
                                                }
                                            }else {
                                                for (int dayI = 0; dayI<planDict.getJSONArray("batch_gather_lis").size(); dayI++){
                                                    JSONObject pBatchGatherOne = planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI);
                                                    for (String pCode : pBatchGatherOne.getJSONObject("gather_map").keySet()){
                                                        planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(pCode).put("contact_duration", planNewContactDurationMap.getString(pCode));
                                                    }
                                                }
                                            }
                                            addLis.add(planDict);
                                            addResultLis.addAll(resultDictLis);
                                            projectHasChemistry = 1;
                                            continue;
                                        }
                                    }
                                }else {
                                    maxSampleCode = dealsubPlan(hasCode, maxSampleCode, planDict, oldPlan, sampleDays, sampleNum, projectInfo.getCompanyOrder());
                                    JSONArray result = getChemistryResultTmpl(planDict, itemSub, pfnMap, oldResultMap, nowTime, null, null);
                                    resultDictLis = result.getJSONArray(0);
                                    newContactDurationMap = result.getJSONObject(1);
                                    for (int dayI = 0; dayI < planDict.getJSONArray("batch_gather_lis").size(); dayI++ ){
                                        JSONObject pBatchGatherOne = planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI);
                                        for (String pCode : pBatchGatherOne.getJSONObject("gather_map").keySet()){
                                            Object codeContactDuration = pBatchGatherOne.getJSONObject("gather_map").getJSONObject(pCode).get("contact_duration");
                                            if (newContactDurationMap.getJSONObject(pCode).getInteger("has_valid_value") == 1){
                                                if (!newContactDurationMap.getJSONObject(pCode).getJSONArray("lis").contains(codeContactDuration)){
                                                    Random random = new Random();
                                                    JSONArray lis = newContactDurationMap.getJSONObject(pCode).getJSONArray("lis");
                                                    planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(pCode).put("contact_duration", lis.getString(random.nextInt(lis.size())));
                                                }
                                            }else {
                                                planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(pCode).put("contact_duration", "");
                                            }
                                        }
                                    }
                                    projectHasChemistry = 1;
                                }
                            }
                        }else if (itemSub.getInteger("s_type") == 2){
                            int hasCode = 1;
                            maxSampleCode = dealsubPlan(hasCode, maxSampleCode, planDict, oldPlan, sampleDays, sampleNum, projectInfo.getCompanyOrder());
                            JSONArray result = getChemistryResultTmpl(planDict, itemSub, pfnMap, oldResultMap, nowTime, new JSONObject(), pointDustContactDurationMap);
                            resultDictLis = result.getJSONArray(0);
                            newContactDurationMap = result.getJSONObject(1);
                            for (int dayI = 0; dayI < planDict.getJSONArray("batch_gather_lis").size(); dayI++ ){
                                JSONObject pBatchGatherOne = planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI);
//                                JSONArray dayDustContactDurationLis = new JSONArray();
                                for (String pCode : pBatchGatherOne.getJSONObject("gather_map").keySet()){
                                    Object codeContactDuration = pBatchGatherOne.getJSONObject("gather_map").getJSONObject(pCode).get("contact_duration");
                                    if (newContactDurationMap.getJSONObject(pCode).getInteger("has_valid_value") == 1){
                                        if (!newContactDurationMap.getJSONObject(pCode).getJSONArray("lis").contains(codeContactDuration)){
                                            Random random = new Random();
                                            JSONArray lis = newContactDurationMap.getJSONObject(pCode).getJSONArray("lis");
                                            planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(pCode).put("contact_duration", lis.getString(random.nextInt(lis.size())));
                                        }
                                    }else {
                                        planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(pCode).put("contact_duration", "");
                                    }
//                                    dayDustContactDurationLis.add(planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(pCode).get("contact_duration"));
                                }
                            }
                            String theTotalDustId = StringUtils.isNotBlank(itemSub.getString("total_dust_id")) ? itemSub.getString("total_dust_id") : substanceId;
                            for (int a = 0; a < resultDictLis.size(); a++){
                                JSONObject itemRes = resultDictLis.getJSONObject(a);
                                if (!pointDustContactDurationMap.containsKey(itemRes.getString("pfn_id"))){
                                    pointDustContactDurationMap.put(itemRes.getString("pfn_id"), new JSONObject());
                                }
                                if (!pointDustContactDurationMap.getJSONObject(itemRes.getString("pfn_id")).containsKey(theTotalDustId)){
                                    pointDustContactDurationMap.getJSONObject(itemRes.getString("pfn_id")).put(theTotalDustId, new JSONArray());
                                }
                                for (String rCode : itemRes.getJSONArray("batch_sample_lis").getJSONObject(0).getJSONObject("gather_map").keySet()){
                                    pointDustContactDurationMap.getJSONObject(itemRes.getString("pfn_id")).getJSONArray(theTotalDustId).add(itemRes.getJSONArray("batch_sample_lis").getJSONObject(0).getJSONObject("gather_map").getJSONObject(rCode).get("contact_duration"));
                                }
                            }
                            projectHasChemistry = 1;
                        }else if (itemSub.getInteger("s_type") == 3){
                            int hasCode = 2;
                            maxSampleCode = dealsubPlan(hasCode, maxSampleCode, planDict, oldPlan, sampleDays, sampleNum, projectInfo.getCompanyOrder());
                            JSONArray result = getNoiseResultTmpl(planDict, itemSub, pfnMap, oldResultMap, nowTime);
                            resultDictLis = result.getJSONArray(0);
                            newContactDurationMap = result.getJSONObject(1);
                            for (int dayI = 0; dayI < planDict.getJSONArray("batch_gather_lis").size(); dayI++ ){
                                JSONObject pBatchGatherOne = planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI);
                                for (String pCode : pBatchGatherOne.getJSONObject("gather_map").keySet()){
                                    Object codeContactDuration = pBatchGatherOne.getJSONObject("gather_map").getJSONObject(pCode).get("contact_duration");
                                    if (newContactDurationMap.getJSONObject(pCode).getInteger("has_valid_value") == 1){
                                        if (!newContactDurationMap.getJSONObject(pCode).getJSONArray("lis").contains(codeContactDuration)){
                                            Random random = new Random();
                                            JSONArray lis = newContactDurationMap.getJSONObject(pCode).getJSONArray("lis");
                                            planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(pCode).put("contact_duration", lis.getString(random.nextInt(lis.size())));
                                        }
                                    }else {
                                        planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(pCode).put("contact_duration", "");
                                    }
                                }
                            }
                            projectHasPhysic = 1;
                        }else {
                            int hasCode = 2;
                            maxSampleCode = dealsubPlan(hasCode, maxSampleCode, planDict, oldPlan, sampleDays, sampleNum, projectInfo.getCompanyOrder());
                            if(itemSub.getInteger("s_type") == 4){
                                JSONArray result = getHtResultTmpl(planDict, itemSub, pfnMap, oldResultMap, nowTime, htLimitMap);
                                resultDictLis = result.getJSONArray(0);
                                newContactDurationMap = result.getJSONObject(1);
                                projectHasPhysic = 1;
                            }else if(itemSub.getInteger("s_type") == 5){
                                JSONArray result = getUtResultTmpl(planDict, itemSub, pfnMap, oldResultMap, nowTime);
                                resultDictLis = result.getJSONArray(0);
                                newContactDurationMap = result.getJSONObject(1);
                                projectHasPhysic = 1;
                            }else if(itemSub.getInteger("s_type") == 6){
                                JSONArray result = getHvResultTmpl(planDict, itemSub, pfnMap, oldResultMap, nowTime);
                                resultDictLis = result.getJSONArray(0);
                                newContactDurationMap = result.getJSONObject(1);
                                projectHasPhysic = 1;
                            }else if(itemSub.getInteger("s_type") == 7){
                                JSONArray result = getEleResultTmpl(planDict, itemSub, pfnMap, oldResultMap, nowTime);
                                resultDictLis = result.getJSONArray(0);
                                newContactDurationMap = result.getJSONObject(1);
                                projectHasPhysic = 1;
                            }else if(itemSub.getInteger("s_type") == 8){
                                JSONArray result = getEmResultTmpl(planDict, itemSub, pfnMap, oldResultMap, nowTime);
                                resultDictLis = result.getJSONArray(0);
                                newContactDurationMap = result.getJSONObject(1);
                                projectHasPhysic = 1;
                            }else if(itemSub.getInteger("s_type") == 9){
                                JSONArray result = getUhfResultTmpl(planDict, itemSub, pfnMap, oldResultMap, nowTime);
                                resultDictLis = result.getJSONArray(0);
                                newContactDurationMap = result.getJSONObject(1);
                                projectHasPhysic = 1;
                            }else if(itemSub.getInteger("s_type") == 10){
                                JSONArray result = getMvResultTmpl(planDict, itemSub, pfnMap, oldResultMap, nowTime);
                                resultDictLis = result.getJSONArray(0);
                                newContactDurationMap = result.getJSONObject(1);
                                projectHasPhysic = 1;
                            }else if(itemSub.getInteger("s_type") == 11){
                                JSONArray result = getWspResultTmpl(planDict, itemSub, pfnMap, oldResultMap, nowTime);
                                resultDictLis = result.getJSONArray(0);
                                newContactDurationMap = result.getJSONObject(1);
                                projectHasPhysic = 1;
                            }else if(itemSub.getInteger("s_type") == 12){
                                JSONArray result = getIlmnResultTmpl(planDict, itemSub, pfnMap, oldResultMap, nowTime);
                                resultDictLis = result.getJSONArray(0);
                                newContactDurationMap = result.getJSONObject(1);
                                projectHasPhysic = 1;
                            }else if(itemSub.getInteger("s_type") == 13){
                                JSONArray result = getLaserResultTmpl(planDict, itemSub, pfnMap, oldResultMap, nowTime);
                                resultDictLis = result.getJSONArray(0);
                                newContactDurationMap = result.getJSONObject(1);
                                projectHasPhysic = 1;
                            }else {
                                resultDictLis = new JSONArray();
                                newContactDurationMap = new JSONObject();
                            }
                            for (int dayI = 0; dayI < planDict.getJSONArray("batch_gather_lis").size(); dayI++){
                                JSONObject pBatchGatherOne = planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI);
                                for (String pCode : pBatchGatherOne.getJSONObject("gather_map").keySet()){
                                    Object codeContactDuration = pBatchGatherOne.getJSONObject("gather_map").getJSONObject(pCode).get("contact_duration");
                                    if (newContactDurationMap.getJSONObject(pCode).getInteger("has_valid_value") == 1){
                                        if (!newContactDurationMap.getJSONObject(pCode).getJSONArray("lis").contains(codeContactDuration)){
                                            Random random = new Random();
                                            JSONArray lis = newContactDurationMap.getJSONObject(pCode).getJSONArray("lis");
                                            planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(pCode).put("contact_duration", lis.get(random.nextInt(lis.size())));
                                        }
                                    }else {
                                        planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(pCode).put("contact_duration", "");
                                    }
                                }
                            }
                        }
                        addLis.add(planDict);
                        addResultLis.addAll(resultDictLis);
                    }
                    wsUpdate.set("point_map." + pointObj.getString("point_id") + ".max_sample_code", maxSampleCode);
                    wsReduction.set("point_map." + pointObj.getString("point_id") + ".max_sample_code", pointObj.getString("max_sample_code"));
                }
                hasGenerateWsIds.add(postIdStr);
                MongoUpdateDto wfu = new MongoUpdateDto();
                Query idMap = new Query().addCriteria(Criteria.where("_id").is(wsObj.getString("_id")));
                wfu.setFlt(idMap);
                wfu.setUpdate(wsUpdate);
                wfu.setReduction(wsReduction);
                wsFltUpdate.add(wfu);
            }
            // TODO 处理个体采样
            Update pfnUpdate = new Update().set("update_time", nowTime);
            Update pfnReduction = new Update().set("update_time", itemPfn.get("update_time"));
            if (itemPfn.get("test_num") == null){
                continue;
            }
            JSONArray newCodeSortLis = new JSONArray();
            JSONArray oldCodeSortLis = new JSONArray();
            JSONObject pfnOldPlanMap = oldSoloPlanMap.containsKey(pfnIdStr) ? oldSoloPlanMap.getJSONObject(pfnIdStr).getJSONObject("point_map") : new JSONObject();
            JSONObject relatedArea = areaMap.getJSONObject(itemPfn.getString("area_id"));
            Integer pfnPeopleNum = itemPfn.getInteger("people_num");
            Integer pfnOperatorsNum = itemPfn.getInteger("shift_num");
            for (int a = 0; a < itemPfn.getJSONArray("code_sort").size(); a++){
                JSONObject pointObj = itemPfn.getJSONArray("code_sort").getJSONObject(a);
                Integer maxSampleCode = pointObj.getInteger("max_sample_code");
                JSONArray result = dealPfnPoint(pointObj, maxPointCode);
                Integer pointCode = (Integer) result.get(0);
                String pointCodeStr =  result.get(1) + "";
                maxPointCode = (Integer) result.get(2);
                JSONObject pointOldPlanMap = pfnOldPlanMap.containsKey(pointObj.getString("point_id")) ? pfnOldPlanMap.getJSONObject(pointObj.getString("point_id")).getJSONObject("sub_map") : new JSONObject();
                JSONObject pointOldResultMap = oldSoloResultMap.containsKey(pointObj.getString("point_id")) ? oldSoloResultMap.getJSONObject(pointObj.getString("point_id")) : new JSONObject();
                JSONObject pointDustContactDurationMap = new JSONObject();
                // 处理物质
                JSONArray result2 = dealPfnHazards(itemPfn.getJSONObject("solo_hazards"), soloSstMap);
//                hasMergeSub = (Integer) result2.get(0);
                JSONObject mergeSubMap = result2.getJSONObject(1);
                JSONArray subLis = result2.getJSONArray(2);
                for (Object o1 : subLis){
                    JSONArray resultDictLis = new JSONArray();
                    JSONObject itemSub = JSONObject.parseObject(JSON.toJSONString(o1));
                    String substanceId = itemSub.getString("substance_id");
                    JSONObject subData = soloSstMap.get(Integer.parseInt(substanceId));
                    Integer sampleDays = itemPfn.getInteger("sample_days");
                    Integer sampleNum = itemPfn.getInteger("sample_num");
                    if (sampleDays == null || sampleNum == null){
                        continue;
                    }
                    sampleDays = 1;
                    sampleNum = 1;
                    ObjectId planId = new ObjectId();
                    JSONObject oldResultMap = pointOldResultMap.containsKey(substanceId) ? pointOldResultMap.getJSONObject(substanceId) : new JSONObject();
                    JSONObject oldPlan = new JSONObject();
                    JSONObject operationMap = new JSONObject();
                    operationMap.put("people_num", 0);
                    operationMap.put("equip_name", "");
                    operationMap.put("equip_lis", new JSONArray());
                    operationMap.put("work_content", "");
                    operationMap.put("equip_working_lis", new JSONArray());
                    operationMap.put("epe_working_lis", new JSONArray());
                    operationMap.put("ppe_working_lis", new JSONArray());
                    oldPlan.put("operation", operationMap);
                    oldPlan.put("m_id", planId);
                    oldPlan.put("batch_gather_lis", new JSONArray());
                    oldPlan.put("sample_days", 0);
                    oldPlan.put("sample_num", 0);
                    if (pfnOldPlanMap.containsKey(substanceId)){
                        JSONObject oldPlanOperation = pointOldPlanMap.getJSONObject(substanceId).getJSONObject("operation");
                        oldPlan.put("m_id", pointOldPlanMap.getJSONObject(substanceId).get("m_id"));
                        oldPlan.put("batch_gather_lis", pointOldPlanMap.getJSONObject(substanceId).get("batch_gather_lis"));
                        oldPlan.put("sample_days", pointOldPlanMap.getJSONObject(substanceId).get("sample_num"));
                        oldPlan.put("sample_num", pointOldPlanMap.getJSONObject(substanceId).get("m_id"));
                        JSONObject operationOldMap = new JSONObject();
                        operationOldMap.put("people_num", oldPlanOperation.get("people_num") != null ? oldPlanOperation.get("people_num") : 0);
                        operationOldMap.put("equip_name", oldPlanOperation.get("equip_name") != null ? oldPlanOperation.get("equip_name") : "");
                        operationOldMap.put("equip_lis", new JSONArray());
                        operationOldMap.put("work_content", "");
                        operationOldMap.put("equip_working_lis", new JSONArray());
                        operationOldMap.put("epe_working_lis", new JSONArray());
                        operationOldMap.put("ppe_working_lis", new JSONArray());
                        oldPlan.put("operation", operationOldMap);
                    }
                    JSONObject planDict = getPlanTmpl(planId, relatedArea, itemPfn, pointObj, subData, itemSub, mergeSubMap, nowTime);
                    planDict.put("identifier", comIdentifier);
                    planDict.put("m_id", oldPlan.get("m_id"));
                    planDict.put("project_id", projectId);
                    planDict.put("point_code", pointCodeStr);
                    planDict.put("point_code_num", pointCode);
                    JSONObject operationMap1 = new JSONObject();
                    operationMap1.put("people_num", pfnPeopleNum);
                    operationMap1.put("equip_name", oldPlan.getJSONObject("operation").get("equip_name"));
                    operationMap1.put("equip_lis", pfnOperationMap.getJSONObject(pfnIdStr).get("equip_lis"));
                    operationMap1.put("work_content", String.join("；", (List<String>) pfnOperationMap.getJSONObject(pfnIdStr).get("work_content_lis")));
                    planDict.put("operation", operationMap1);
                    oldPlan.getJSONObject("operation").put("operators_num", pfnOperatorsNum);
                    oldPlan.getJSONObject("operation").put("work_content", String.join("；", (List<String>) pfnOperationMap.getJSONObject(pfnIdStr).get("work_content_lis")));
                    oldPlan.getJSONObject("operation").put("equip_working_lis", pfnOperationMap.getJSONObject(pfnIdStr).get("equip_lis"));
                    oldPlan.getJSONObject("operation").put("epe_working_lis", pfnOperationMap.getJSONObject(pfnIdStr).get("epe_lis"));
                    oldPlan.getJSONObject("operation").put("ppe_working_lis", pfnOperationMap.getJSONObject(pfnIdStr).get("ppe_lis"));
                    if (itemSub.getInteger("s_type") == 1){
                        int hasCode = 1;
                        if (StringUtils.isNotBlank(subData.getString("total_merge_name"))){
                            JSONObject mergePlanTmpl = new JSONObject();
                            mergePlanTmpl.put("batch_gather_lis", new JSONArray());
                            mergePlanTmpl.put("sample_days", 0);
                            mergePlanTmpl.put("sample_num", 0);
                            if (oldPlan.get("batch_gather_lis") != null && mergePlanTmpl.get("batch_gather_lis") == null){
                                for (int b = 0; b < oldPlan.getJSONArray("batch_gather_lis").size(); b++){
                                    JSONObject batchGatherDict = oldPlan.getJSONArray("batch_gather_lis").getJSONObject(b);
                                    for (String bglCodeOne : batchGatherDict.getJSONObject("gather_map").keySet()){
                                        batchGatherDict.getJSONObject("gather_map").getJSONObject(bglCodeOne).put("result", new JSONObject());
                                    }
                                    for (String bglKbCodeOne : batchGatherDict.getJSONObject("gather_kb_map").keySet()){
                                        batchGatherDict.getJSONObject("gather_kb_map").getJSONObject(bglKbCodeOne).put("result", new JSONObject());
                                    }
                                    mergePlanTmpl.getJSONArray("batch_gather_lis").add(batchGatherDict);
                                }
                            }
                            mergePlanTmpl.put("sample_days", sampleDays);
                            mergePlanTmpl.put("sample_num", sampleNum);
                            JSONObject newContactDurationMap = new JSONObject();
                            JSONObject mergeContactDurationMapTpl = new JSONObject();
                            JSONObject planNewContactDurationMap = new JSONObject();
                            for (Object o2 : itemSub.getJSONArray("merge_sub_lis")){
                                JSONObject itemSubM = JSONObject.parseObject(JSON.toJSONString(o2));
                                ObjectId mPlanId = new ObjectId();
                                substanceId = itemSubM.getString("substance_id");
                                planDict = getPlanTmpl(mPlanId, relatedArea, itemPfn, pointObj, subData, itemSubM, mergeSubMap, nowTime);
                                oldPlan = new JSONObject();
                                oldPlan.put("m_id", mPlanId);
                                oldPlan.put("batch_gather_lis", new JSONArray());
                                oldPlan.put("sample_days", 0);
                                oldPlan.put("sample_num", 0);
                                JSONObject operationMap2 = new JSONObject();
                                operationMap2.put("people_num", 0);
                                operationMap2.put("equip_name", "");
                                operationMap2.put("equip_lis", new JSONArray());
                                operationMap2.put("work_content", "");
                                operationMap2.put("equip_working_lis", new JSONArray());
                                operationMap2.put("epe_working_lis", new JSONArray());
                                operationMap2.put("ppe_working_lis", new JSONArray());
                                oldPlan.put("operation", operationMap2);
                                if (pointOldPlanMap.containsKey(substanceId)){
                                    JSONObject oldPlanOperation = pointOldPlanMap.getJSONObject(substanceId).getJSONObject("operation");
                                    oldPlan = new JSONObject();
                                    oldPlan.put("m_id", pointOldPlanMap.getJSONObject(substanceId).get("m_id"));
                                    oldPlan.put("batch_gather_lis", pointOldPlanMap.getJSONObject(substanceId).get("batch_gather_lis"));
                                    oldPlan.put("sample_days", pointOldPlanMap.getJSONObject(substanceId).get("sample_days"));
                                    oldPlan.put("sample_num", pointOldPlanMap.getJSONObject(substanceId).get("sample_num"));
                                    JSONObject operationMap3 = new JSONObject();
                                    operationMap3.put("people_num", oldPlanOperation.get("people_num") != null ? oldPlanOperation.get("people_num") : 0);
                                    operationMap3.put("equip_name", oldPlanOperation.get("equip_name") != null ? oldPlanOperation.get("equip_name") : "");
                                    operationMap3.put("equip_lis", new JSONArray());
                                    operationMap3.put("work_content", "");
                                    operationMap3.put("equip_working_lis", new JSONArray());
                                    operationMap3.put("epe_working_lis", new JSONArray());
                                    operationMap3.put("ppe_working_lis", new JSONArray());
                                    oldPlan.put("operation", operationMap3);
                                }
                                planDict.put("identifier", comIdentifier);
                                planDict.put("m_id", oldPlan.get("m_id"));
                                planDict.put("project_id", projectId);
                                planDict.put("point_code", pointCodeStr);
                                planDict.put("point_code_num", pointCode);
                                JSONObject operationMap4 = new JSONObject();
                                operationMap4.put("people_num", pfnPeopleNum);
                                operationMap4.put("equip_name", oldPlan.getJSONObject("operation").get("equip_name"));
                                operationMap4.put("equip_lis", pfnOperationMap.getJSONObject(pfnIdStr).get("equip_lis"));
                                operationMap4.put("work_content", String.join("；", (List<String>) pfnOperationMap.getJSONObject(pfnIdStr).get("work_content_lis")));
                                planDict.put("operation", operationMap4);
                                oldPlan.getJSONObject("operation").put("operators_num", pfnOperatorsNum);
                                oldPlan.getJSONObject("operation").put("work_content", String.join("；", (List<String>) pfnOperationMap.getJSONObject(pfnIdStr).get("work_content_lis")));
                                oldPlan.getJSONObject("operation").put("equip_working_lis", pfnOperationMap.getJSONObject(pfnIdStr).get("equip_lis"));
                                oldPlan.getJSONObject("operation").put("epe_working_lis", pfnOperationMap.getJSONObject(pfnIdStr).get("epe_lis"));
                                oldPlan.getJSONObject("operation").put("ppe_working_lis", pfnOperationMap.getJSONObject(pfnIdStr).get("ppe_lis"));
                                maxSampleCode = dealMergeSubPlan(hasCode, maxSampleCode, planDict, oldPlan, sampleDays, sampleNum, mergePlanTmpl, projectInfo.getCompanyOrder());
                                JSONObject hazard = itemSub;
                                hazard.put("pfn_ids", itemPfn.get("_id"));
                                JSONArray result3 = getChemistryResultTmpl(planDict, hazard, pfnMap, oldResultMap, nowTime, mergeContactDurationMapTpl, null);
                                resultDictLis = result3.getJSONArray(0);
                                JSONObject mergeNewContactDurationMap = result3.getJSONObject(1);
                                if (newContactDurationMap == null){
                                    newContactDurationMap = mergeNewContactDurationMap;
                                    for (int dayI = 0; dayI < planDict.getJSONArray("batch_gather_lis").size(); dayI++){
                                        JSONObject pBatchGatherOne = planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI);
                                        for (String pCode : pBatchGatherOne.getJSONObject("gather_map").keySet()){
                                            Object codeContactDuration = pBatchGatherOne.getJSONObject("gather_map").getJSONObject(pCode).get("contact_duration");
                                            if (newContactDurationMap.getJSONObject(pCode).getInteger("has_valid_value") == 1){
                                                if (!newContactDurationMap.getJSONObject(pCode).getJSONArray("lis").contains(codeContactDuration)){
                                                    Random random = new Random();
                                                    int anInt = random.nextInt(newContactDurationMap.getJSONObject(pCode).getJSONArray("lis").size());
                                                    planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(pCode).put("contact_duration",newContactDurationMap.getJSONObject(pCode).getJSONArray("lis").getString(anInt));
                                                }
                                                planNewContactDurationMap.put(pCode, planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(pCode).get("contact_duration"));
                                            }else {
                                                planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(pCode).put("contact_duration", "");
                                                planNewContactDurationMap.put(pCode, "");
                                            }
                                        }
                                    }
                                    for (Object o4 : resultDictLis){
                                        JSONObject itemRes = JSONObject.parseObject(JSON.toJSONString(o4));
                                        mergeContactDurationMapTpl.put(itemRes.getString("pfn_id"), new JSONArray());
                                        for (String resCode : itemRes.getJSONArray("batch_sample_lis").getJSONObject(0).getJSONObject("gather_map").keySet()){
                                            mergeContactDurationMapTpl.getJSONArray(itemRes.getString("pfn_id")).add(itemRes.getJSONArray("batch_sample_lis").getJSONObject(0).getJSONObject("gather_map").getJSONObject(resCode).get("contact_duration"));
                                        }
                                    }
                                }else {
                                    for (int dayI = 0; dayI < planDict.getJSONArray("batch_gather_lis").size(); dayI++){
                                        JSONObject pBatchGatherOne = planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI);
                                        for (String pCode : pBatchGatherOne.getJSONObject("gather_map").keySet()){
                                            planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(pCode).put("contact_duration", planNewContactDurationMap.get(pCode));
                                        }
                                    }
                                }
                                addLis.add(planDict);
                                addResultLis.addAll(resultDictLis);
                                projectHasChemistry = 1;
                            }
                            continue;
                        }else {
                            maxSampleCode = dealsubPlan(hasCode, maxSampleCode, planDict, oldPlan, sampleDays, sampleNum, projectInfo.getCompanyOrder());
                            JSONObject hazard = itemPfn;
                            hazard.put("pfn_ids", itemPfn.get("_id"));
                            JSONArray result3 = getChemistryResultTmpl(planDict, hazard, pfnMap, oldResultMap, nowTime, null, null);
                            resultDictLis = result3.getJSONArray(0);
                            JSONObject newContactDurationMap = result3.getJSONObject(1);
                            for (int dayI = 0; dayI < planDict.getJSONArray("batch_gather_lis").size(); dayI++){
                                JSONObject pBatchGatherOne = planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI);
                                for (String pCode : pBatchGatherOne.getJSONObject("gather_map").keySet()){
                                    Object codeContactDuration = pBatchGatherOne.getJSONObject("gather_map").getJSONObject(pCode).get("contact_duration");
                                    if (newContactDurationMap.getJSONObject(pCode).getInteger("has_valid_value") == 1){
                                        if (!newContactDurationMap.getJSONObject(pCode).getJSONArray("lis").contains(codeContactDuration)){
                                            Random random = new Random();
                                            int anInt = random.nextInt(newContactDurationMap.getJSONObject(pCode).getJSONArray("lis").size());
                                            planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(pCode).put("contact_duration", newContactDurationMap.getJSONObject(pCode).getJSONArray("lis").getString(anInt));
                                        }
                                    }else {
                                        planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(pCode).put("contact_duration", "");
                                    }
                                }
                            }
                            projectHasChemistry = 1;
                        }
                    }else if (itemSub.getInteger("s_type") == 2){
                        int hasCode = 1;
                        maxSampleCode = dealsubPlan(hasCode, maxSampleCode, planDict, oldPlan, sampleDays, sampleNum, projectInfo.getCompanyOrder());
                        JSONObject hazard = itemPfn;
                        hazard.put("pfn_ids", itemPfn.get("_id"));
                        JSONArray result3 = getChemistryResultTmpl(planDict, hazard, pfnMap, oldResultMap, nowTime, null, null);
                        resultDictLis = result3.getJSONArray(0);
                        JSONObject newContactDurationMap = result3.getJSONObject(1);
                        for (int dayI = 0; dayI < planDict.getJSONArray("batch_gather_lis").size(); dayI++){
                            JSONObject pBatchGatherOne = planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI);
//                            JSONArray dayDustContactDurationLis = new JSONArray();
                            for (String pCode : pBatchGatherOne.getJSONObject("gather_map").keySet()){
                                Object codeContactDuration = pBatchGatherOne.getJSONObject("gather_map").getJSONObject(pCode).get("contact_duration");
                                if (newContactDurationMap.getJSONObject(pCode).getInteger("has_valid_value") == 1){
                                    if (!newContactDurationMap.getJSONObject(pCode).getJSONArray("lis").contains(codeContactDuration)){
                                        Random random = new Random();
                                        int anInt = random.nextInt(newContactDurationMap.getJSONObject(pCode).getJSONArray("lis").size());
                                        planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(pCode).put("contact_duration", newContactDurationMap.getJSONObject(pCode).getJSONArray("lis").getString(anInt));
                                    }
                                }else {
                                    planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(pCode).put("contact_duration", "");
                                }
//                                dayDustContactDurationLis.add(planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(pCode).get("contact_duration"));
                            }
                        }
                        String theTotalDustId = StringUtils.isNotBlank(itemSub.getString("total_dust_id")) ? itemSub.getString("total_dust_id") : substanceId;
                        for (Object o2 : resultDictLis){
                            JSONObject itemRes = JSONObject.parseObject(JSON.toJSONString(o2));
                            if (!pointDustContactDurationMap.containsKey(itemRes.getString("pfn_id"))){
                                pointDustContactDurationMap.put(itemRes.getString("pfn_id"), new JSONObject());
                            }
                            if (!pointDustContactDurationMap.getJSONObject(itemRes.getString("pfn_id")).containsKey(theTotalDustId)){
                                pointDustContactDurationMap.getJSONObject(itemRes.getString("pfn_id")).put(theTotalDustId, new JSONArray());
                            }
                            for (String rCode : itemRes.getJSONArray("batch_sample_lis").getJSONObject(0).getJSONObject("gather_map").keySet()){
                                pointDustContactDurationMap.getJSONObject(itemRes.getString("pfn_id")).getJSONArray(theTotalDustId).add(itemRes.getJSONArray("batch_sample_lis").getJSONObject(0).getJSONObject("gather_map").getJSONObject(rCode).get("contact_duration"));
                            }
                        }
                        projectHasChemistry = 1;
                    }else if (itemSub.getInteger("s_type") == 3){
                        int hasCode = 2;
                        maxSampleCode = dealsubPlan(hasCode, maxSampleCode, planDict, oldPlan, sampleDays, sampleNum, projectInfo.getCompanyOrder());
                        JSONObject hazard = itemPfn;
                        hazard.put("pfn_ids", itemPfn.get("_id"));
                        JSONArray result3 = getNoiseResultTmpl(planDict, hazard, pfnMap, oldResultMap, nowTime);
                        resultDictLis = result3.getJSONArray(0);
                        JSONObject newContactDurationMap = result3.getJSONObject(1);
                        for (int dayI = 0; dayI < planDict.getJSONArray("batch_gather_lis").size(); dayI++){
                            JSONObject pBatchGatherOne = planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI);
                            for (String pCode : pBatchGatherOne.getJSONObject("gather_map").keySet()){
                                Object codeContactDuration = pBatchGatherOne.getJSONObject("gather_map").getJSONObject(pCode).get("contact_duration");
                                if (newContactDurationMap.getJSONObject(pCode).getInteger("has_valid_value") == 1){
                                    if (!newContactDurationMap.getJSONObject(pCode).getJSONArray("lis").contains(codeContactDuration)){
                                        Random random = new Random();
                                        int anInt = random.nextInt(newContactDurationMap.getJSONObject(pCode).getJSONArray("lis").size());
                                        planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(pCode).put("contact_duration", newContactDurationMap.getJSONObject(pCode).getJSONArray("lis").getString(anInt));
                                    }
                                }else {
                                    planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(pCode).put("contact_duration", "");
                                }
                            }
                        }
                        projectHasPhysic = 1;
                    }
                    addLis.add(planDict);
                    addResultLis.addAll(resultDictLis);
                }
                JSONObject oldCodeSort = pointObj;
                oldCodeSortLis.add(oldCodeSort);
                JSONObject newCodeSort = getPfnNewCodeSort(pointObj, pointCode, pointCode, maxSampleCode);
                newCodeSortLis.add(newCodeSort);
            }
            pfnUpdate.set("code_sort", newCodeSortLis);
            pfnReduction.set("code_sort", oldCodeSortLis);
            MongoUpdateDto outMap = new MongoUpdateDto();
            Query idMap = new Query().addCriteria(Criteria.where("_id").is(itemPfn.get("_id")));
            outMap.setFlt(idMap);
            outMap.setUpdate(pfnUpdate);
            outMap.setReduction(pfnReduction);
            pfnFltUpdate.add(outMap);
        }
        Integer isTime = null;
        if (projectHasChemistry == 1 && projectHasPhysic == 1){
            isTime = 1;
        }else if(projectHasChemistry == 1) {
            isTime = 2;
        }else if(projectHasPhysic == 1) {
            isTime = 3;
        }
        // 记录方案提交时间
        List<ProjectCountEntity> countLis = projectCountService.list(new QueryWrapper<ProjectCountEntity>().eq("project_id", projectId));
        if (countLis.size() == 0){
            ProjectCountEntity addObj = new ProjectCountEntity();
            addObj.setProjectId((long) projectId);
            addObj.setPlanCommit(nowTime);
            projectCountService.save(addObj);
        }else {
            ProjectCountEntity countInfo = countLis.get(0);
            if (countInfo.getPlanCommit() != null){
                countInfo.setPlanCommitLast(nowTime);
            }else {
                countInfo.setPlanCommit(nowTime);
            }
            projectCountService.updateById(countInfo);
        }
        try {
            Query deleteQuery = new Query().addCriteria(Criteria.where("project_id").is(projectId)).addCriteria(Criteria.where("create_time").ne(nowTime));
            mongoTemplate.remove(deleteQuery, "zj_plan_record");
            mongoTemplate.remove(deleteQuery, "zj_result");
            mongoTemplate.insert(distinst(addLis), "zj_plan_record");
            mongoTemplate.insert(distinst(addResultLis), "zj_result");
            for (MongoUpdateDto ws : wsFltUpdate){
                mongoTemplate.updateFirst(ws.getFlt(), ws.getUpdate(),"zj_workspace");
            }
            for (MongoUpdateDto pfn : pfnFltUpdate){
                mongoTemplate.updateFirst(pfn.getFlt(), pfn.getUpdate(),"zj_post_pfn");
            }
        }catch (Exception e){
            Query deleteQuery = new Query().addCriteria(Criteria.where("project_id").is(projectId)).addCriteria(Criteria.where("create_time").is(nowTime));
            mongoTemplate.remove(deleteQuery, "zj_plan_record");
            mongoTemplate.remove(deleteQuery, "zj_result");
            for (MongoUpdateDto ws : wsFltUpdate){
                mongoTemplate.updateFirst(ws.getFlt(), ws.getReduction(),"zj_workspace");
            }
            for (MongoUpdateDto pfn : pfnFltUpdate){
                mongoTemplate.updateFirst(pfn.getFlt(), pfn.getReduction(),"zj_post_pfn");
            }
            mongoTemplate.insert(distinst(oldPlanLis), "zj_plan_record");
            mongoTemplate.insert(distinst(oldResultLis), "zj_result");
            e.printStackTrace();
            return Result.error(500,"采样计划生成失败",null);
        }
        comSurveyInfo.setMaxPointCode(maxPointCode);
        List<String> list = new ArrayList<>();
        for (Object chara : countWspLis){
            list.add(chara.toString());
        }
        String testPlaceLisStr = String.join("、", list);
        comSurveyInfo.setTestPlace(testPlaceLisStr);
        projectInfo.setIsTime(isTime);
//        companySurveyService.updateById(comSurveyInfo);
//        projectService.updateById(projectInfo);
        return Result.ok("采样方案生成成功", null);
    }

    /**
     * 结果去重
     */
    private JSONArray distinst(JSONArray list){
        Set<String> uniqueKeys = new HashSet<>();
        JSONArray deduplicatedArray = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            JSONObject add = list.getJSONObject(i);
            String key = add.getString("_id");
            if (!uniqueKeys.contains(key)) {
                uniqueKeys.add(key);
                deduplicatedArray.add(add);
            }
        }
        return deduplicatedArray;
    }

    /**
     * 返回一个 岗位id:对应信息的Map
     */
    private JSONObject getEqLayoutPostIdMapByPjtId(int projectId){
        JSONObject resMap = new JSONObject();
        List<EquipmentLayoutEntity> lis = equipmentLayoutService.list(new QueryWrapper<EquipmentLayoutEntity>().eq("project_id", projectId));
        for (EquipmentLayoutEntity item : lis){
            if (StringUtils.isNotBlank(item.getPostId())){
                if (!resMap.containsKey(item.getPostId())){
                    resMap.put(item.getPostId(), new JSONArray());
                }
                if (StringUtils.isNotBlank(item.getDevice())){
                    JSONObject map = new JSONObject();
                    map.put("name",item.getDevice());
                    map.put("total_num",item.getTotalNum() != null ? item.getTotalNum() : "");
                    map.put("run_num",item.getRunNum() != null ? item.getRunNum() : "");
                    resMap.getJSONArray(item.getPostId()).add(map);
                }
            }
        }
        return resMap;
    }

    /**
     * 返回高温接触限值map
     */
    private JSONObject getHtLimit(){
        List<EvalHtLimitEntity> limitLis = evalHtLimitService.list();
        JSONObject limitMap = new JSONObject();
        for (EvalHtLimitEntity itemL : limitLis){
            String key = itemL.getContactRate().toString();
            if (!limitMap.containsKey(key)){
                limitMap.put(key, new HashMap<>());
            }
            JSONObject valueMap = limitMap.getJSONObject(key);
            if(!valueMap.containsKey(itemL.getLaborIntensity())){
                valueMap.put(itemL.getLaborIntensity(), itemL.getLimitValue());
            }
        }
        return limitMap;
    }

    /**
     * 获取采样计划需要的物质信息字段 (根据这里调整采样计划中物质信息字段)
     */
    private List<Map<Integer, JSONObject>> samplePlanSubstanceMap(String companyOrder){
        List<Integer> noIds = Arrays.asList(648, 651, 652, 653, 628, 637, 640, 634, 635, 636);
        QueryWrapper<Object> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("s.s_dept",1);
        queryWrapper.eq("sd.mark_num",1);
        queryWrapper.notIn("s.id",noIds);
        queryWrapper.eq("sd.lab_source", companyOrder);
        List<JSONObject> lis = substanceMapper.getSamplePlanSubstanceMap(queryWrapper);
        Map<Integer, JSONObject> fixedMap = new HashMap<>();
        fixedMap.put(0, new JSONObject());
        Map<Integer, JSONObject> soloMap = new HashMap<>();
        soloMap.put(0, new JSONObject());
        Map<Integer, JSONObject> mainDataMap = new HashMap<>();
        List<Integer> mainIds = new ArrayList<>();
        for (JSONObject item : lis){
            JSONObject substanceInfo = (JSONObject) item.clone();
            substanceInfo.put("substance", item.getString("name"));
            substanceInfo.put("limited_range", "");
            substanceInfo.put("detect_num", "");
            substanceInfo.put("detect_name", "");
            substanceInfo.put("is_default_st", 2);
            if (!mainIds.contains(substanceInfo.getInteger("main_data_id"))){
                mainIds.add(substanceInfo.getInteger("main_data_id"));
                JSONObject piece = new JSONObject();
                JSONArray zeroList = new JSONArray();
                zeroList.add(0);
                piece.put("fixed", zeroList);
                piece.put("solo", zeroList);
                mainDataMap.put(substanceInfo.getInteger("main_data_id"), piece);
            }
            JSONObject fixedInfoDict = new JSONObject();
            List<String> fixedNeedDelKeys = Arrays.asList("ind_sample", "ind_equipment", "ind_flow", "ind_test_time", "ind_test_time_note");
            for (String keyOne : substanceInfo.keySet()){
                if (!fixedNeedDelKeys.contains(keyOne)){
                    fixedInfoDict.put(keyOne, substanceInfo.get(keyOne));
                }
            }
            fixedInfoDict.put("sample_mode", 1);
            fixedMap.put(fixedInfoDict.getInteger("substance_id"), substanceInfo);
            mainDataMap.get(fixedInfoDict.getInteger("main_data_id")).getJSONArray("fixed").add(fixedInfoDict.get("substance_id"));
            if (substanceInfo.getInteger("ind_sample") == 1){
                JSONObject soloInfoDict = new JSONObject();
                List<String> soloNeedDelKeys = Arrays.asList("ind_sample", "ind_equipment", "ind_flow", "ind_test_time", "ind_test_time_note");
                List<String> soloNeedChangeKeys = Arrays.asList("equipment", "flow", "test_time", "test_time_note");
                for (String keyOne : substanceInfo.keySet()){
                    if (!soloNeedDelKeys.contains(keyOne)){
                        soloInfoDict.put(keyOne, substanceInfo.get(keyOne));
                    }
                }
                for (String keyU : soloNeedChangeKeys){
                    soloInfoDict.put(keyU, substanceInfo.get("ind_" + keyU));
                }
                soloInfoDict.put("sample_mode", 2);
                if ("噪声".equals(soloInfoDict.getString("name"))){
                    soloInfoDict.put("sample_tablename", "noiseIndividual");
                }
                soloMap.put(soloInfoDict.getInteger("substance_id"), soloInfoDict);
                mainDataMap.get(soloInfoDict.getInteger("main_data_id")).getJSONArray("solo").add(soloInfoDict.get("substance_id"));
            }
        }
        QueryWrapper<MainDataEntity> mainDataQuery = new QueryWrapper<>();
        mainDataQuery.in("id", mainIds);
        mainDataQuery.eq("is_default", 1);
        mainDataQuery.eq("data_belong", companyOrder);
        List<MainDataEntity> mainDataLis = mainDataService.list(mainDataQuery);
        for (MainDataEntity mainObj : mainDataLis){
            String limitedRange = mainObj.getLimitedRange();
            List<String> strings = Arrays.asList("工作场所物理因素", "工作场所通风工程");
            if (strings.contains(mainObj.getCategory()) && StringUtils.isBlank(mainObj.getLimitedRange())) {
                limitedRange = "直读法";
            }
            JSONArray fixedSubIds = mainDataMap.get(mainObj.getId()).getJSONArray("fixed");
            fillFixedAndSoloMap(fixedMap, mainObj, limitedRange, fixedSubIds);
            JSONArray soloSubIds = mainDataMap.get(mainObj.getId()).getJSONArray("solo");
            fillFixedAndSoloMap(soloMap, mainObj, limitedRange, soloSubIds);
        }
        List<Map<Integer, JSONObject>> returnList = new ArrayList<>();
        returnList.add(fixedMap);
        returnList.add(soloMap);
        return returnList;
    }

    /**
     * 上级方法调用填充
     */
    private void fillFixedAndSoloMap(Map<Integer,JSONObject> fixedMap, MainDataEntity mainObj, String limitedRange, JSONArray soloSubIds) {
        for (Object soloSubId : soloSubIds){
            Integer numId = new Integer(String.valueOf(soloSubId));
            if (!fixedMap.containsKey(numId)){
                fixedMap.put(numId, new JSONObject());
            }
            fixedMap.get(numId).put("limited_range", limitedRange);
            fixedMap.get(numId).put("detect_num", mainObj.getDetectNum());
            fixedMap.get(numId).put("detect_name", mainObj.getDetectName());
            fixedMap.get(numId).put("is_default_st", 1);
        }
    }

    private JSONArray getProjectPlanMap(int projectId){
        List<JSONObject> planLis = mongoTemplate.find(new Query().addCriteria(Criteria.where("project_id").is(projectId)), JSONObject.class, "zj_plan_record");
        JSONObject fixedMap = new JSONObject();
        JSONObject soloMap = new JSONObject();
        JSONObject oldKbMap = getKbMap();
        for (JSONObject item : planLis){
            if (item.getInteger("is_fixed") == 1){
                if (item.getJSONArray("sample_kb_code_lis").size()>0){
                    if (item.getJSONObject("substance").getJSONObject("substance_info").getInteger("s_type") == 1){
                        oldKbMap.getJSONArray("chem_ids").add(item.getJSONObject("substance").get("id"));
                    }else if(item.getJSONObject("substance").getJSONObject("substance_info").getInteger("s_type") == 2){
                        String dustKbKey = StringUtils.isNotBlank(item.getJSONObject("substance").getJSONObject("substance_info").getString("total_dust_id")) ? "breath" : "total";
                        oldKbMap.getJSONObject("dust_fixed_kb").put(dustKbKey, 1);
                    }
                }
                if (!fixedMap.containsKey(item.getJSONObject("place").getString("post_id"))){
                    JSONObject map = new JSONObject();
                    map.put("post_id", item.getJSONObject("place").getString("post_id"));
                    map.put("point_map", new JSONObject());
                    fixedMap.put(item.getJSONObject("place").getString("post_id"), map);
                }
                if (!fixedMap.getJSONObject(item.getJSONObject("place").getString("post_id")).getJSONObject("point_map").containsKey(item.getString("point_id"))){
                    JSONObject map = new JSONObject();
                    map.put("point_id", item.getString("point_id"));
                    map.put("sub_map", new JSONObject());
                    fixedMap.getJSONObject(item.getJSONObject("place").getString("post_id")).getJSONObject("point_map").put(item.getString("point_id"), map);
                }
                fixedMap.getJSONObject(item.getJSONObject("place").getString("post_id")).getJSONObject("point_map").getJSONObject(item.getString("point_id"))
                        .getJSONObject("sub_map").put(item.getJSONObject("substance").getString("id"), item);
            }else {
                if (item.getJSONArray("sample_kb_code_lis").size() > 0){
                    if (item.getJSONObject("substance").getJSONObject("substance_info").getInteger("s_type") == 1){
                        oldKbMap.getJSONArray("chem_ids").add(item.getJSONObject("substance").get("id"));
                    }else if(item.getJSONObject("substance").getJSONObject("substance_info").getInteger("s_type") == 2){
                        String dustKbKey = StringUtils.isNotBlank(item.getJSONObject("substance").getJSONObject("substance_info").getString("total_dust_id")) ? "breath" : "total";
                        oldKbMap.getJSONObject("dust_fixed_kb").put(dustKbKey, 1);
                    }
                }
                if (!soloMap.containsKey(item.getString("pfn_id"))){
                    JSONObject map = new JSONObject();
                    map.put("pfn_id", item.getString("pfn_id"));
                    map.put("point_map", new JSONObject());
                    soloMap.put(item.getString("pfn_id"), map);
                }
                if (!soloMap.getJSONObject(item.getString("pfn_id")).getJSONObject("point_map").containsKey(item.getString("point_id"))){
                    JSONObject map = new JSONObject();
                    map.put("point_id", item.getString("point_id"));
                    map.put("sub_map", new JSONObject());
                    soloMap.getJSONObject(item.getString("pfn_id")).getJSONObject("point_map").put(item.getString("point_id"), map);
                }
                soloMap.getJSONObject(item.getString("pfn_id")).getJSONObject("point_map").getJSONObject(item.getString("point_id")).
                        getJSONObject("sub_map").put(item.getJSONObject("substance").getString("id"), item);
            }
        }
        JSONArray returnList = new JSONArray();
        returnList.add(fixedMap);
        returnList.add(soloMap);
        returnList.add(oldKbMap);
        returnList.add(planLis);
        return returnList;
    }

    private JSONArray getProjectResultMap(int projectId){
        List<JSONObject> resultLis = mongoTemplate.find(new Query().addCriteria(Criteria.where("project_id").is(projectId)), JSONObject.class, "zj_result");
        JSONObject fixedMap = new JSONObject();
        JSONObject soloMap = new JSONObject();
        for (JSONObject item : resultLis){
            if (item.getInteger("is_fixed") == 1){
                fillFixedOrSoloMap(fixedMap, item);
            }else {
                fillFixedOrSoloMap(soloMap, item);
            }
        }
        JSONArray returnList = new JSONArray();
        returnList.add(fixedMap);
        returnList.add(soloMap);
        returnList.add(resultLis);
        return returnList;
    }

    private void fillFixedOrSoloMap(JSONObject soloMap, JSONObject item) {
        String pointId = item.getString("point_id");
        if(!soloMap.containsKey(pointId)){
            soloMap.put(pointId, new JSONObject());
        }
        String substanceId = item.getJSONObject("substance").getString("id");
        if (!soloMap.getJSONObject(pointId).containsKey(substanceId)){
            soloMap.getJSONObject(pointId).put(substanceId, new JSONObject());
        }
        soloMap.getJSONObject(pointId).getJSONObject(substanceId).put(item.getString("pfn_id"), item);
    }

    /**
     * 处理合并采样的物质 并把合并后物质的名称处理出来
     */
    private JSONArray dealWorkspaceHazards(JSONArray jsonArray, Map<Integer,JSONObject> fixedSstMap){
        int hasMergeSub = 2;
        JSONObject mergeSubMap = new JSONObject();
        JSONArray subLis = new JSONArray();
        for (int i = 0; i < jsonArray.size(); i++){
            JSONObject subObj = jsonArray.getJSONObject(i);
            JSONObject substance = fixedSstMap.get(subObj.getInteger("substance_id"));
            String totalMergeName = substance.getString("total_merge_name");
            JSONObject hazardInfo = new JSONObject(subObj);
            if (StringUtils.isNotBlank(totalMergeName)){
                hasMergeSub = 1;
                hazardInfo.put("merge_name", substance.get("merge_name"));
                hazardInfo.put("merge_sort", substance.get("merge_sort"));
                if (!mergeSubMap.containsKey(totalMergeName)){
                    JSONObject map = new JSONObject();
                    map.put("lis_index", subLis.size());
                    map.put("merge_name_lis", new JSONArray());
                    map.put("sub_ids", new JSONArray());
                    map.put("name_lis", new JSONArray());
                    mergeSubMap.put(totalMergeName, map);
                    JSONArray one = new JSONArray();
                    one.add(hazardInfo);
                    hazardInfo.put("merge_sub_lis", one);
                    subLis.add(hazardInfo);
                }else {
                    int index = mergeSubMap.getJSONObject(totalMergeName).getInteger("lis_index");
                    subLis.getJSONObject(index).getJSONArray("merge_sub_lis").add(hazardInfo);
                }
            }else {
                subLis.add(hazardInfo);
            }
        }
        for (String totalMergeName : mergeSubMap.keySet()){
            Integer index = mergeSubMap.getJSONObject(totalMergeName).getInteger("lis_index");
            JSONArray newMergeSubLis = subLis.getJSONObject(index).getJSONArray("merge_sub_lis");
            if (newMergeSubLis.size() > 1){
                // 重构发现这一段有排序 但是上段代码只放了一个list=1的列表
                Comparator<Object> comparator = (o1, o2) -> {
                    try {
                        // 获取id属性进行比较
                        int id1 = JSONObject.parseObject(JSON.toJSONString(o1)).getInteger("merge_sort");
                        int id2 = JSONObject.parseObject(JSON.toJSONString(o2)).getInteger("merge_sort");
                        return Integer.compare(id1, id2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return 0;
                };
                newMergeSubLis.sort(comparator);
            }
            List<String> mergeNameLis = new ArrayList<>();
            List<String> mergeSubIds = new ArrayList<>();
            List<String> nameLis = new ArrayList<>();
            for (int i = 0; i < newMergeSubLis.size(); i++){
                JSONObject subDict = newMergeSubLis.getJSONObject(i);
                if (!mergeNameLis.contains(subDict.getString("merge_name"))){
                    mergeNameLis.add(subDict.getString("merge_name"));
                }
                mergeSubIds.add(subDict.getString("substance_id"));
                nameLis.add(subDict.getString("name"));
            }
            mergeSubMap.getJSONObject(totalMergeName).put("merge_name_lis", mergeNameLis);
            mergeSubMap.getJSONObject(totalMergeName).put("sub_ids", mergeSubIds);
            mergeSubMap.getJSONObject(totalMergeName).put("name_lis", nameLis);
            subLis.getJSONObject(index).put("merge_sub_lis", newMergeSubLis);
        }
        JSONArray returnList = new JSONArray();
        returnList.add(hasMergeSub);
        returnList.add(mergeSubMap);
        returnList.add(subLis);
        return returnList;
    }

    private JSONArray dealWorkspacePoint(JSONObject pointObj, Integer maxPointCode,Update wsUpdate,Update wsReduction){
        Integer pointCode = 0;
        if (StringUtils.isNotBlank(pointObj.getString("code"))){
            pointCode = pointObj.getInteger("code");
        }else {
            maxPointCode += 1;
            pointCode = maxPointCode;
            wsUpdate.set("point_map." + pointObj.getString("point_id") + ".code", pointCode);
            wsUpdate.set("point_map." + pointObj.getString("point_id") + ".sort", pointCode);
            wsReduction.set("point_map." + pointObj.getString("point_id") + ".code", pointObj.get("code"));
            wsReduction.set("point_map." + pointObj.getString("point_id") + ".sort", pointObj.get("sort"));
        }
        String pointCodeStr = pointCode.toString();
        int codeStrLength = pointCodeStr.length();
        StringBuilder forwardStr = new StringBuilder();
        if (codeStrLength<3){
            for (int i=0 ; i<3-codeStrLength; i++){
                forwardStr.append("0");
            }
        }
        pointCodeStr = forwardStr + pointCodeStr;
        JSONArray returnList = new JSONArray();
        returnList.add(pointCode);
        returnList.add(pointCodeStr);
        returnList.add(maxPointCode);
        returnList.add(wsUpdate);
        returnList.add(wsReduction);
        return returnList;
    }

    private JSONObject getPlanTmpl(ObjectId planId, JSONObject wsObj, JSONObject pfnObj, JSONObject pointObj,JSONObject subData,JSONObject hazard,JSONObject mergeSubMap,Date nowTime){
        String substanceShow = StringUtils.isNotBlank(hazard.getString("alias")) ? hazard.getString("name") + "(" + hazard.getString("alias") + ")" : hazard.getString("name");
        String totalMN = subData.getString("total_merge_name");
        int multiSubstance = 2;
        String multiSubName = "";
        String multiSubTag = "";
        String multiSubType = "";
        if (StringUtils.isNotBlank(totalMN) && mergeSubMap.getJSONObject(totalMN).getJSONArray("name_lis").size() > 1){
            multiSubstance = 1;
            List<String> nameList1 = new ArrayList<>();
            for (int a = 0; a < mergeSubMap.getJSONObject(totalMN).getJSONArray("name_lis").size(); a++){
                nameList1.add(mergeSubMap.getJSONObject(totalMN).getJSONArray("name_lis").getString(a));
            }
            multiSubName = String.join("、", nameList1);
            List<String> nameList2 = new ArrayList<>();
            for (int a = 0; a < mergeSubMap.getJSONObject(totalMN).getJSONArray("merge_name_lis").size(); a++){
                nameList2.add(mergeSubMap.getJSONObject(totalMN).getJSONArray("merge_name_lis").getString(a));
            }
            multiSubTag = String.join("、", nameList2);
            multiSubType = totalMN;
        }
        JSONObject substance = new JSONObject();
        substance.put("id", hazard.getString("substance_id"));
        substance.put("name", hazard.getString("name"));
        substance.put("alias", hazard.getString("alias"));
        substance.put("sample_id", subData.getString("sample_id"));
        substance.put("main_data_id", subData.getString("main_data_id"));
        substance.put("substance_show", substanceShow);
        substance.put("substance_info", subData);
        substance.put("final_id", hazard.getString("substance_id"));
        substance.put("final_name", hazard.getString("name"));
        substance.put("final_alias", hazard.getString("alias"));
        substance.put("final_sample_id", subData.getString("sample_id"));
        substance.put("final_main_data_id", subData.getString("main_data_id"));
        substance.put("final_substance_show", substanceShow);
        substance.put("final_substance_info", subData);
        JSONObject operation = new JSONObject();
        operation.put("people_num", 0);
        operation.put("equip_name", "");
        operation.put("equip_lis", new JSONArray());
        operation.put("work_content", "");
        JSONObject tmpl = new JSONObject();
        tmpl.put("_id", planId);
        tmpl.put("m_id", "");
        tmpl.put("project_id", 0);
        tmpl.put("identifier", "");
        tmpl.put("is_fixed", 1);
        tmpl.put("retest_num", 0);
        tmpl.put("relation_pfn_ids", new JSONArray());
        tmpl.put("pfn_id", "");
        tmpl.put("pfn", "");
        tmpl.put("place", new JSONObject());
        tmpl.put("point_id", pointObj.getString("point_id"));
        tmpl.put("point", pointObj.getString("point"));
        tmpl.put("point_code", "");
        tmpl.put("point_code_num", "");
        tmpl.put("test_place", "");
        tmpl.put("substance", substance);
        tmpl.put("test_type", hazard.getString("test_type"));
        tmpl.put("multi_substance", multiSubstance);
        tmpl.put("multi_sub_name", multiSubName);
        tmpl.put("multi_sub_tag", multiSubTag);
        tmpl.put("multi_sub_type", multiSubType);
        tmpl.put("sample_code_lis", new JSONArray());
        tmpl.put("sample_kb_code_lis", new JSONArray());
        tmpl.put("print_bar_code_lis", new JSONArray());
        tmpl.put("total_time_frame", "");
        tmpl.put("sample_days", 0);
        tmpl.put("sample_num", 0);
        tmpl.put("batch_gather_lis", new JSONArray());
        tmpl.put("operation", operation);
        tmpl.put("sample_note", "");
        tmpl.put("is_suspend", 2);
        tmpl.put("is_existed", 1);
        tmpl.put("create_time", nowTime);
        tmpl.put("update_time", nowTime);
        tmpl.put("_class", "may.yuntian.jianping.mongoentity.PlanRecordEntity");
        if (pfnObj == null ) {
            tmpl.put("is_fixed", 1);
            JSONObject wsObj1 = wsObj;
            JSONObject place = new JSONObject();
            String workshopId = wsObj1.getString("workshop_id");
            place.put("workshop_id", workshopId);
            place.put("workshop", wsObj1.getString("workshop"));
            String areaId = wsObj1.getString("area_id");
            place.put("area_id", areaId);
            place.put("area", wsObj1.getString("area"));
            place.put("workshop_area", StringUtils.isNotBlank(wsObj1.getString("area")) ? wsObj1.getString("workshop") + "/" + wsObj1.getString("area") : wsObj1.getString("workshop"));
            place.put("post_id", wsObj1.getString("_id"));
            place.put("post", wsObj1.getString("post"));
            tmpl.put("place", place);
            tmpl.put("relation_pfn_ids", hazard.getString("pfn_ids"));
            String testPlace = tmpl.getJSONObject("place").getString("post")!=null
                    ? tmpl.getJSONObject("place").getString("workshop_area") + "/" + tmpl.getJSONObject("place").getString("post")
                    : tmpl.getJSONObject("place").getString("workshop_area");
            tmpl.put("test_place", pointObj.getString("point")!=null ? testPlace + "/" + pointObj.getString("point") : testPlace);
        }else {
            tmpl.put("is_fixed", 2);
            JSONObject pfnWsDict = wsObj;
            JSONObject place = new JSONObject();
            String workshopId = pfnWsDict.getString("workshop_id");
            place.put("workshop_id", workshopId);
            place.put("workshop", pfnWsDict.getString("workshop"));
            String areaId = pfnWsDict.getString("area_id");
            place.put("area_id", areaId);
            place.put("area", pfnWsDict.getString("area"));
            place.put("workshop_area", StringUtils.isNotBlank(pfnWsDict.getString("area")) ? pfnWsDict.getString("workshop") + "/" + pfnWsDict.getString("area") : pfnWsDict.getString("workshop"));
            place.put("post_id", "");
            place.put("post", "");
            tmpl.put("place", place);
            tmpl.put("pfn_id", pfnObj.getString("_id"));
            tmpl.put("pfn", pfnObj.getString("pfn"));
            tmpl.put("relation_pfn_ids", tmpl.getString("pfn_id"));
            String testPlace = tmpl.getString("pfn");
            int testNum = pfnObj.getInteger("test_num")!=null ? pfnObj.getInteger("test_num") : 0;
            tmpl.put("test_place", testNum > 1 && pointObj.getString("point")!=null ? testPlace + "/" + pointObj.getString("point") : testPlace);
        }
        return tmpl;
    }

    private Integer dealsubPlan(int hasCode, Integer maxSampleCode, JSONObject planDict, JSONObject oldPlan, int sampleDays, int sampleNum, String companyOrder){
        for (int dayI = 0; dayI < sampleDays; dayI++){
            int batchNum = dayI + 1;
            JSONObject bglParams = new JSONObject();
            Integer oldSampleNum = 0;
            JSONObject oldGatherKbMap = new JSONObject();
            JSONArray oldGampleKbCodeLis = new JSONArray();
            if (dayI < oldPlan.getInteger("sample_days")){
                bglParams = oldPlan.getJSONArray("batch_gather_lis").getJSONObject(dayI);
                if (bglParams.get("operators_num") == null){
                    bglParams.put("operators_num", oldPlan.getJSONObject("operation").get("operators_num"));
                }
                bglParams.put("equip_working_lis", oldPlan.getJSONObject("operation").get("equip_working_lis"));
                bglParams.put("epe_working_lis", oldPlan.getJSONObject("operation").get("epe_working_lis"));
                bglParams.put("ppe_working_lis", oldPlan.getJSONObject("operation").get("ppe_working_lis"));
                oldSampleNum = oldPlan.getInteger("sample_num");
                oldGatherKbMap = oldPlan.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_kb_map");
                oldGampleKbCodeLis = oldPlan.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONArray("sample_kb_code_lis");
            }else {
                oldSampleNum = 0;
                bglParams.put("operators_num", oldPlan.getJSONObject("operation").get("operators_num"));
                bglParams.put("working_equip", 0);
                bglParams.put("equip_working_lis", oldPlan.getJSONObject("operation").get("equip_working_lis"));
                bglParams.put("epe_working_lis", oldPlan.getJSONObject("operation").get("epe_working_lis"));
                bglParams.put("ppe_working_lis", oldPlan.getJSONObject("operation").get("ppe_working_lis"));
            }
            planDict.getJSONArray("batch_gather_lis").add(getBatchGatherLisTmpl(batchNum, bglParams));
            planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).put("gather_kb_map", oldGatherKbMap);
            String batchMinCode = null;
            JSONArray sampleCodeLis = new JSONArray();
            JSONArray printBarCodeLis = new JSONArray();
            JSONArray sampleKbCodeLis = oldGampleKbCodeLis;
            for (int numI = 0; numI < sampleNum; numI++){
                JSONObject gatherOneParams;
                String sampleCode, gatherOneKey;
                if (hasCode == 1){
                    if (numI < oldSampleNum){
                        sampleCode = oldPlan.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONArray("sample_code_lis").getString(numI);
                        Integer printNum = oldPlan.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(sampleCode).getInteger("print_num");
                        Object barCode = oldPlan.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(sampleCode).get("bar_code");
                        gatherOneParams = bglParams.getJSONObject("gather_map").getJSONObject(sampleCode);
                        gatherOneParams.put("bar_code", barCode);
                        gatherOneParams.put("print_num", printNum);
                        gatherOneParams.put("print_time", oldPlan.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(sampleCode).get("print_time"));
                        gatherOneParams.put("sample_code", sampleCode);
                        gatherOneParams.put("before_flow", planDict.getJSONObject("substance").getJSONObject("final_substance_info").get("flow"));
                        gatherOneParams.put("after_flow", planDict.getJSONObject("substance").getJSONObject("final_substance_info").get("flow"));
                        if (oldPlan.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(sampleCode).get("before_flow") != null){
                            gatherOneParams.put("before_flow", oldPlan.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(sampleCode).get("before_flow"));
                            gatherOneParams.put("after_flow", oldPlan.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(sampleCode).get("before_flow"));
                        }
                        gatherOneParams.put("test_duration", planDict.getJSONObject("substance").getJSONObject("final_substance_info").get("test_time") != null ? planDict.getJSONObject("substance").getJSONObject("final_substance_info").getInteger("test_time") : 0);
                        if (oldPlan.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(sampleCode).get("test_duration") != null){
                            gatherOneParams.put("test_duration", oldPlan.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(sampleCode).getInteger("test_duration"));
                        }
                        if (printNum != null && printNum != 0){
                           printBarCodeLis.add(barCode);
                        }
                    }else {
                        gatherOneParams = new JSONObject();
                        maxSampleCode += 1;
                        Integer sampleCodeNum = maxSampleCode;
                        sampleCode = sampleCodeNum > 9 ? sampleCodeNum + "" : "0" + sampleCodeNum;
                        List<String> barCodeLis = getBarCodes(companyOrder, 1);
                        gatherOneParams.put("bar_code", barCodeLis.get(0));
                        gatherOneParams.put("sample_code", sampleCode);
                        gatherOneParams.put("before_flow", planDict.getJSONObject("substance").getJSONObject("final_substance_info").get("flow"));
                        gatherOneParams.put("after_flow", planDict.getJSONObject("substance").getJSONObject("final_substance_info").get("flow"));
                        gatherOneParams.put("test_duration", planDict.getJSONObject("substance").getJSONObject("final_substance_info").get("test_time") != null ? planDict.getJSONObject("substance").getJSONObject("final_substance_info").getInteger("test_time") : 0);

                    }
                    sampleCodeLis.add(sampleCode);
                    gatherOneKey = sampleCode;
                }else {
                    sampleCode = "";
                    gatherOneKey = numI + "";
                    gatherOneParams = numI < oldSampleNum ? bglParams.getJSONObject("gather_map").getJSONObject(gatherOneKey) : new JSONObject();
                    gatherOneParams.put("bar_code", "");
                    gatherOneParams.put("sample_code", sampleCode);
                    gatherOneParams.put("before_flow", planDict.getJSONObject("substance").getJSONObject("final_substance_info").get("flow"));
                    gatherOneParams.put("after_flow", planDict.getJSONObject("substance").getJSONObject("final_substance_info").get("flow"));
                    gatherOneParams.put("test_duration", planDict.getJSONObject("substance").getJSONObject("final_substance_info").get("test_time") != null ? planDict.getJSONObject("substance").getJSONObject("final_substance_info").get("test_time") : 0);
                }
                if (batchMinCode == null){
                    batchMinCode = sampleCode + "";
                }
                JSONObject gatherOne = getGatherOneTmpl(gatherOneParams);
                planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").put(gatherOneKey, gatherOne);
            }
            planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).put("sample_code_lis", sampleCodeLis);
            planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).put("sample_kb_code_lis", sampleKbCodeLis);
            planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).put("print_bar_code_lis", printBarCodeLis);
            planDict.getJSONArray("sample_code_lis").addAll(sampleCodeLis);
            planDict.getJSONArray("sample_kb_code_lis").addAll(sampleKbCodeLis);
            planDict.getJSONArray("print_bar_code_lis").addAll(printBarCodeLis);
        }
        planDict.put("sample_days",sampleDays);
        planDict.put("sample_num",sampleNum);
        return maxSampleCode;
    }

    private JSONObject getBatchGatherLisTmpl(int batchNum, JSONObject params){
        JSONObject returnMap = new JSONObject();
        JSONObject sceneMap = new JSONObject();
        JSONObject scene = params.getJSONObject("scene") != null ? params.getJSONObject("scene") : new JSONObject();
        returnMap.put("batch_num", batchNum);
        returnMap.put("gather_date", params.get("gather_date") != null ? params.get("gather_date") : "");
        returnMap.put("status", params.get("status") != null ? params.get("status") : 1);
        returnMap.put("has_kb_code", params.get("has_kb_code") != null ? params.get("has_kb_code") : 2);
        returnMap.put("sample_code_lis", new JSONArray());
        returnMap.put("sample_kb_code_lis", new JSONArray());
        returnMap.put("print_bar_code_lis", new JSONArray());
        sceneMap.put("temp", scene != null && scene.get("temp") != null ? scene.get("temp") : "");
        sceneMap.put("humidity", scene != null && scene.get("humidity") != null ? scene.get("humidity") : "");
        sceneMap.put("pressure", scene != null && scene.get("pressure") != null ? scene.get("pressure") : "");
        sceneMap.put("wind_speed", scene != null && scene.get("wind_speed") != null ? scene.get("wind_speed") : "");
        returnMap.put("scene", sceneMap);
        returnMap.put("operators_num", params.get("operators_num") != null ? params.get("operators_num") : 0);
        returnMap.put("working_equip", params.get("working_equip") != null ? params.get("working_equip") : 0);
        returnMap.put("epe_working", params.get("epe_working") != null ? params.get("epe_working") : "");
        returnMap.put("ppe_working", params.get("ppe_working") != null ? params.get("ppe_working") : "");
        returnMap.put("equip_working_lis", params.get("equip_working_lis") != null ? params.get("equip_working_lis") :  new JSONArray());
        returnMap.put("epe_working_lis", params.get("epe_working_lis") != null ? params.get("epe_working_lis") :  new JSONArray());
        returnMap.put("ppe_working_lis", params.get("ppe_working_lis") != null ? params.get("ppe_working_lis") :  new JSONArray());
        returnMap.put("gather_map", new JSONObject());
        returnMap.put("gather_kb_map", new JSONObject());
        return returnMap;
    }

    private JSONObject getGatherOneTmpl(JSONObject params){
        Object printTime = params.get("print_time");
        JSONObject sampleEquip = params.getJSONObject("sample_equip") != null ? params.getJSONObject("sample_equip") : new JSONObject();
        JSONObject sampleEquipDict = new JSONObject();
        sampleEquipDict.put("name_model_id",sampleEquip.get("name_model_id") != null ? sampleEquip.get("name_model_id") : "");
        sampleEquipDict.put("instrument_code",sampleEquip.get("instrument_code") != null ? sampleEquip.get("instrument_code") : "");
        sampleEquipDict.put("name_model_id1",sampleEquip.get("name_model_id1") != null ? sampleEquip.get("name_model_id1") : "");
        sampleEquipDict.put("instrument_code1",sampleEquip.get("instrument_code1") != null ? sampleEquip.get("instrument_code1") : "");
        sampleEquipDict.put("calibration_info",sampleEquip.get("calibration_info") != null ? sampleEquip.get("calibration_info") : "");
        sampleEquipDict.put("calibration_name",sampleEquip.get("calibration_name") != null ? sampleEquip.get("calibration_name") : "");
        sampleEquipDict.put("calib_value",sampleEquip.get("calib_value") != null ? sampleEquip.get("calib_value") : "");
        sampleEquipDict.put("correct_factor",sampleEquip.get("correct_factor") != null ? sampleEquip.get("correct_factor") : "");
        JSONObject resultDict = params.getJSONObject("result") !=null ? params.getJSONObject("result") : new JSONObject();
        if (resultDict == null){
            resultDict = getResultTmpl();
        }
        JSONObject returnMap = new JSONObject();
        returnMap.put("sample_code", params.get("sample_code") != null ? params.get("sample_code") : "");
        returnMap.put("time_frame", params.get("time_frame") != null ? params.get("time_frame") : "");
        returnMap.put("contact_duration", params.get("contact_duration") != null ? params.getString("contact_duration") : "");
        returnMap.put("people", params.get("people") != null ? params.get("people") : "");
        returnMap.put("test_duration", StringUtils.isNotBlank(params.getString("test_duration")) ? params.getInteger("test_duration") : 0);
        returnMap.put("before_flow", params.get("before_flow") != null ? params.get("before_flow") : "");
        returnMap.put("after_flow", params.get("after_flow") != null ? params.get("after_flow") : "");
        returnMap.put("begin_time", params.get("begin_time") != null ? params.get("begin_time") : "");
        returnMap.put("end_time", params.get("end_time") != null ? params.get("end_time") : "");
        returnMap.put("sample_volume", params.get("") != null ? params.get("") : "");
        returnMap.put("sample_equip", sampleEquipDict);
        returnMap.put("result", resultDict);
        returnMap.put("bar_code", params.get("bar_code") != null ? params.get("bar_code") : "");
        returnMap.put("print_num", params.get("print_num") != null ? params.get("print_num") : 0);
        returnMap.put("print_time", printTime);
        return returnMap;
    }

    private JSONObject getResultTmpl(){
        JSONObject returnMap = new JSONObject();
        returnMap.put("lt_detection", 2);
        returnMap.put("result1", "");
        returnMap.put("result2", "");
        returnMap.put("result3", "");
        returnMap.put("result4", "");
        returnMap.put("result5", "");
        returnMap.put("result6", "");
        returnMap.put("result7", "");
        returnMap.put("result8", "");
        returnMap.put("result9", "");
        returnMap.put("result_avg", "");
        returnMap.put("result_time_avg", "");
        returnMap.put("result", "");
        returnMap.put("result_str", "");
        returnMap.put("fruit1", "");
        returnMap.put("fruit2", "");
        returnMap.put("fruit3", "");
        returnMap.put("fruit4", "");
        returnMap.put("fruit5", "");
        returnMap.put("fruit6", "");
        returnMap.put("fruit7", "");
        returnMap.put("fruit8", "");
        returnMap.put("fruit9", "");
        returnMap.put("fruit_avg", "");
        returnMap.put("fruit_avg1", "");
        returnMap.put("fruit_avg2", "");
        returnMap.put("fruit_avg3", "");
        returnMap.put("about1", "");
        returnMap.put("about2", "");
        returnMap.put("about3", "");
        returnMap.put("about4", "");
        returnMap.put("about5", "");
        returnMap.put("testing_instrument", "");
        return returnMap;
    }

    /**
     * 毒物(包括co、co2、游离二氧化硅)   粉尘   结果计算
     */
    private JSONArray getChemistryResultTmpl(JSONObject planDict, JSONObject hazard, JSONObject pfnMap, JSONObject oldResultMap, Date nowTime, JSONObject mergeContactDurationMapTpl, JSONObject dustContactDurationMapTpl){
        if (mergeContactDurationMapTpl == null){
            mergeContactDurationMapTpl = new JSONObject();
        }
        if (dustContactDurationMapTpl == null){
            dustContactDurationMapTpl = new JSONObject();
        }
        String key = "chem";
        JSONArray resultDictLis = new JSONArray();
        JSONObject substanceInfo = planDict.getJSONObject("substance").getJSONObject("substance_info");
        String subId = planDict.getJSONObject("substance").getString("id");
        String postIdStr = planDict.getInteger("is_fixed") == 1 ? planDict.getJSONObject("place").getString("post_id") : "";
        JSONObject newContactDurationMap = new JSONObject();
        for (String planCode : planDict.getJSONArray("batch_gather_lis").getJSONObject(0).getJSONObject("gather_map").keySet()){
            JSONObject piece = new JSONObject();
            piece.put("lis", new ArrayList<>());
            piece.put("has_valid_value", 2);
            newContactDurationMap.put(planCode, piece);
        }
        for (Object object : hazard.getJSONArray("pfn_ids")){
            String pfnIdStr = object.toString();
            JSONObject pfnObj = pfnMap.getJSONObject(pfnIdStr);
            Object laborIntensity = pfnObj.get("labor_intensity");
            if(planDict.getInteger("is_fixed") == 1){
                // 定点采样  如果这个物质对应的工种对这个物质的采样天数或数量为0则不生成计算结果
                Integer sPfnSampleNum = pfnObj.getJSONObject("work_track").getJSONObject(postIdStr).getJSONObject("touch_hazards").getJSONObject(subId).getInteger("sample_num");
                Integer sPfnSampleDays = pfnObj.getJSONObject("work_track").getJSONObject(postIdStr).getJSONObject("touch_hazards").getJSONObject(subId).getInteger("sample_days");
                if (sPfnSampleDays == null || sPfnSampleNum == null){
                    continue;
                }
            }
            JSONObject oldResult = oldResultMap.containsKey(pfnIdStr) ? oldResultMap.getJSONObject(pfnIdStr) : new JSONObject();
            JSONObject resultDist = generateResultInfoTmpl(planDict, pfnObj, nowTime);
            JSONArray contactDurationLis = new JSONArray();
            if (StringUtils.isNotBlank(planDict.getString("multi_sub_type")) && mergeContactDurationMapTpl.size() > 0){
                contactDurationLis = mergeContactDurationMapTpl.getJSONArray(pfnIdStr);
            }else {
                if (substanceInfo.getInteger("s_type") == 2){
                    String theSubId = substanceInfo.getString("total_dust_id") != null ? substanceInfo.getString("total_dust_id") : substanceInfo.getString("substance_id");
                    if (!dustContactDurationMapTpl.containsKey(pfnIdStr)){
                        dustContactDurationMapTpl.put(pfnIdStr, new JSONObject());
                    }
                    if (dustContactDurationMapTpl.getJSONObject(pfnIdStr).containsKey(theSubId)){
                        contactDurationLis = dustContactDurationMapTpl.getJSONObject(pfnIdStr).getJSONArray(theSubId);
                    }else {
                        contactDurationLis = getContactDurationLis(oldResult, resultDist.getInteger("sample_num"), resultDist.getString("touch_time"));
                    }
                }else {
                    contactDurationLis = getContactDurationLis(oldResult, resultDist.getInteger("sample_num"), resultDist.getString("touch_time"));
                }
            }
            int contactDurationLisLen = contactDurationLis.size();
            if (contactDurationLisLen < planDict.getInteger("sample_num")){
                int range = planDict.getInteger("sample_num") - contactDurationLisLen;
                for (int cdsI = 0; cdsI < range; cdsI++){
                    contactDurationLis.add("");
                }
            }
            int accuracy = 0;
            String mac = "/", pcTwa = "/", pcTwaF = "/", pcStel = "/", rf = "/", pe = "/";
            if (substanceInfo.get("mac") != null){
                mac = formatNumOrStrToNfloatKeepInt(substanceInfo.getString("mac"), 6);
                accuracy = accuracyMath(mac);
            }else if (substanceInfo.get("pc_twa") != null){
                pcTwa = formatNumOrStrToNfloatKeepInt(substanceInfo.getString("pc_twa"), 6);
                accuracy = accuracyMath(pcTwa);
                if (substanceInfo.get("pc_stel") != null){
                    pcStel = formatNumOrStrToNfloatKeepInt(substanceInfo.getString("pc_stel"), 6);
                }else {
                    pe = formatNumOrStrToNfloatKeepInt((substanceInfo.getInteger("pc_twa") * 3) +"", 6);
                }
            }
            JSONObject oldConclusion = oldResult.getJSONObject("conclusion")!= null ? oldResult.getJSONObject("conclusion"): new JSONObject();
            JSONObject conclusionMap = new JSONObject();
            conclusionMap.put("accuracy", accuracy);
            conclusionMap.put("limit_v", "");
            conclusionMap.put("is_noise", 1);
            conclusionMap.put("contact_rate", 0);
            conclusionMap.put("labor_intensity", laborIntensity);
            conclusionMap.put("max_v", oldConclusion.get("max_v") != null ? oldConclusion.get("max_v") : "");
            conclusionMap.put("min_v", oldConclusion.get("min_v") != null ? oldConclusion.get("min_v") : "");
            conclusionMap.put("test_range", oldConclusion.get("test_range") != null ? oldConclusion.get("test_range") : "");
            conclusionMap.put("conclusion", oldConclusion.get("conclusion") != null ? oldConclusion.get("conclusion") : "");
            resultDist.put("conclusion", conclusionMap);
            resultDist.put("is_exist", oldResult.get("is_exist") != null ? oldResult.get("is_exist") : 1);
            resultDist.put("is_exceed", oldResult.get("is_exceed") != null ? oldResult.get("is_exceed") : 2);

            JSONArray oldBatchGatherLis = oldResult.getJSONArray("batch_sample_lis") != null ? oldResult.getJSONArray("batch_sample_lis") : new JSONArray();
            int oldBatchGatherLisLen = oldBatchGatherLis.size();
            JSONArray newBatchGatherLis = new JSONArray();
            for (int iB = 0; iB < planDict.getJSONArray("batch_gather_lis").size(); iB++){
                JSONObject batchGatherOne = planDict.getJSONArray("batch_gather_lis").getJSONObject(iB);
                JSONObject oldBatchGatherOne = iB < oldBatchGatherLisLen ? oldBatchGatherLis.getJSONObject(iB) : new JSONObject();
//                JSONObject oldGatherMap = oldBatchGatherOne.getJSONObject("gather_map") != null ? oldBatchGatherOne.getJSONObject("gather_map") : new JSONObject();
                JSONObject oldBatchConclusion = oldBatchGatherOne.size() > 0 && oldBatchGatherOne.getJSONObject("conclusion").getJSONObject(key) != null ? oldBatchGatherOne.getJSONObject("conclusion").getJSONObject(key) : new JSONObject();
                JSONObject gatherMap = new JSONObject();
                int sortNum = 0;
                for (String code : batchGatherOne.getJSONObject("gather_map").keySet()){
                    JSONObject codeMap = new JSONObject();
                    codeMap.put("sample_code", code);
                    codeMap.put("contact_duration", contactDurationLis.getString(sortNum));
                    gatherMap.put(code, codeMap);
                    if(contactDurationLis.get(sortNum)!=null){
                        newContactDurationMap.getJSONObject(code).getJSONArray("lis").add(contactDurationLis.get(sortNum));
                        newContactDurationMap.getJSONObject(code).put("has_valid_value", 1);
                    }
                    sortNum+=1;
                }
                JSONObject o = new JSONObject();
                JSONObject conclusion1 = new JSONObject();
                JSONObject keyMap = new JSONObject();
                o.put("batch_num", batchGatherOne.get("batch_num"));
                o.put("gather_date", batchGatherOne.get("gather_date"));
                o.put("result", oldBatchGatherOne.get("result") != null ? oldBatchGatherOne.get("result") : "");
                o.put("gather_map", gatherMap);
                o.put("conclusion_key", key);
                keyMap.put("c_m", oldBatchConclusion.get("c_m") != null ? oldBatchConclusion.get("c_m") : "/");
                keyMap.put("c_twa", oldBatchConclusion.get("c_twa") != null ? oldBatchConclusion.get("c_twa") : "/");
                keyMap.put("c_ste", oldBatchConclusion.get("c_ste") != null ? oldBatchConclusion.get("c_ste") : "/");
                keyMap.put("c_pe", oldBatchConclusion.get("c_pe") != null ? oldBatchConclusion.get("c_pe") : "/");
                keyMap.put("mac", mac);
                keyMap.put("pc_twa", pcTwa);
                keyMap.put("pc_twa_f", oldBatchConclusion.get("pc_twa_f") != null ? oldBatchConclusion.get("pc_twa_f") : pcTwaF);
                keyMap.put("pc_stel", pcStel);
                keyMap.put("rf", oldBatchConclusion.get("rf") != null ? oldBatchConclusion.get("rf") : rf);
                keyMap.put("pe", pe);
                keyMap.put("max_v", oldBatchConclusion.get("max_v") != null ? oldBatchConclusion.get("max_v") : "");
                keyMap.put("min_v", oldBatchConclusion.get("min_v") != null ? oldBatchConclusion.get("min_v") : "");
                keyMap.put("test_range", oldBatchConclusion.get("test_range") != null ? oldBatchConclusion.get("test_range") : "");
                keyMap.put("conclusion", oldBatchConclusion.get("conclusion") != null ? oldBatchConclusion.get("conclusion") : "");
                conclusion1.put(key, keyMap);
                o.put("conclusion", conclusion1);
                newBatchGatherLis.add(o);
            }
            resultDist.put("batch_sample_lis", newBatchGatherLis);
            resultDictLis.add(resultDist);
        }
        JSONArray returnList = new JSONArray();
        returnList.add(resultDictLis);
        returnList.add(newContactDurationMap);
        return returnList;
    }

    private JSONObject generateResultInfoTmpl(JSONObject planDict, JSONObject pfnObj, Date nowTime){
        String substanceIdStr = planDict.getJSONObject("substance").getString("id");
        int totalDustId = 0;
        Object touchTime, touchDays, sampleNum, sampleDays;
        if (planDict.getJSONObject("substance").getJSONObject("substance_info").getInteger("s_type") == 2){
            if (planDict.getJSONObject("substance").getJSONObject("substance_info").getInteger("total_dust_id") == 0){
                totalDustId = planDict.getJSONObject("substance").getJSONObject("substance_info").getInteger("substance_id");
            }else {
                totalDustId = planDict.getJSONObject("substance").getJSONObject("substance_info").getInteger("total_dust_id");
            }
        }
        if (planDict.getInteger("is_fixed") == 1){
            String postIdStr = planDict.getJSONObject("place").getString("post_id");
            JSONObject body = pfnObj.getJSONObject("work_track").getJSONObject(postIdStr).getJSONObject("touch_hazards").getJSONObject(substanceIdStr);
            touchTime = body.get("touch_time");
            touchDays = body.get("touch_days");
            sampleNum = body.get("sample_num");
            sampleDays = body.get("sample_days");
        }else {
            JSONObject body = pfnObj.getJSONObject("solo_hazards").getJSONObject(substanceIdStr);
            touchTime = body.get("touch_time");
            touchDays = body.get("touch_days");
            sampleNum = body.get("sample_num");
            sampleDays = body.get("sample_days");
        }
        JSONObject resultTmpl = new JSONObject();
        resultTmpl.put("_id", new ObjectId());
        resultTmpl.put("project_id", planDict.get("project_id"));
        resultTmpl.put("identifier", planDict.get("identifier"));
        resultTmpl.put("is_fixed", planDict.get("is_fixed"));
        resultTmpl.put("record_id", planDict.getString("_id"));
        resultTmpl.put("pfn_id", pfnObj.getString("_id"));
        resultTmpl.put("pfn", pfnObj.get("pfn"));
        resultTmpl.put("place", planDict.get("place"));
        resultTmpl.put("point_id", planDict.get("point_id"));
        resultTmpl.put("point", planDict.get("point"));
        resultTmpl.put("point_code", planDict.get("point_code"));
        resultTmpl.put("test_place", planDict.get("test_place"));
        JSONObject substanceMap = new JSONObject();
        JSONObject substance = planDict.getJSONObject("substance");
        substanceMap.put("id", substance.get("id"));
        substanceMap.put("name", substance.get("name"));
        substanceMap.put("alias", substance.get("alias"));
        substanceMap.put("sample_id", substance.get("sample_id"));
        substanceMap.put("main_data_id", substance.get("main_data_id"));
        substanceMap.put("substance_show", substance.get("substance_show"));
        substanceMap.put("s_type", substance.getJSONObject("substance_info").get("s_type"));
        substanceMap.put("sample_tablename", substance.getJSONObject("substance_info").get("sample_tablename"));
        substanceMap.put("mark", substance.getJSONObject("substance_info").get("mark"));
        substanceMap.put("highly_toxic", substance.getJSONObject("substance_info").get("highly_toxic"));
        substanceMap.put("is_silica", substance.getJSONObject("substance_info").get("is_silica"));
        substanceMap.put("total_dust_id", substance.getJSONObject("substance_info").get("total_dust_id"));
        substanceMap.put("final_id", substance.get("final_id"));
        substanceMap.put("final_name", substance.get("final_name"));
        substanceMap.put("final_alias", substance.get("final_alias"));
        substanceMap.put("final_sample_id", substance.get("final_sample_id"));
        substanceMap.put("final_main_data_id", substance.get("final_main_data_id"));
        substanceMap.put("final_substance_show", substance.get("final_substance_show"));
        resultTmpl.put("substance", substanceMap);
        resultTmpl.put("multi_substance", planDict.get("multi_substance"));
        resultTmpl.put("multi_sub_name", planDict.get("multi_sub_name"));
        resultTmpl.put("multi_sub_tag", planDict.get("multi_sub_tag"));
        resultTmpl.put("multi_sub_type", planDict.get("multi_sub_type"));
        resultTmpl.put("total_dust_id", totalDustId);
        resultTmpl.put("test_type", planDict.get("test_type"));
        resultTmpl.put("is_exist", 1);
        resultTmpl.put("is_exceed", 2);
        resultTmpl.put("touch_time", touchTime);
        resultTmpl.put("touch_days", touchDays);
        resultTmpl.put("sample_num", sampleNum);
        resultTmpl.put("sample_days", sampleDays);
        resultTmpl.put("batch_sample_lis", new ArrayList<>());
        JSONObject conclusion = new JSONObject();
        conclusion.put("accuracy", 0);
        conclusion.put("limit_v", "");
        conclusion.put("is_noise", 1);
        conclusion.put("contact_rate", 0);
        conclusion.put("labor_intensity", "");
        conclusion.put("max_v", "");
        conclusion.put("min_v", "");
        conclusion.put("test_range", "");
        conclusion.put("conclusion", "");
        resultTmpl.put("conclusion", conclusion);
        resultTmpl.put("create_time", nowTime);
        resultTmpl.put("update_time", nowTime);
        resultTmpl.put("_class", "may.yuntian.jianping.mongoentity.ResultEntity");
        return resultTmpl;
    }

    private JSONArray getContactDurationLis(JSONObject oldResult, Integer sampleNum, String touchTimeStr){
        JSONArray contactDurationLis = new JSONArray();
        if (StringUtils.isBlank(touchTimeStr)){
            for (int numI=0; numI<sampleNum; numI++){
                contactDurationLis.add("0");
            }
            return contactDurationLis;
        }
        if (sampleNum == 1){
            contactDurationLis.add(touchTimeStr);
            return contactDurationLis;
        }
        float touchTime = Float.parseFloat(touchTimeStr);
        int sumOldContactDuration = 0;
        JSONObject oldContactDurationMap = new JSONObject();
        JSONArray oldContactDurationLis = new JSONArray();
        int oldContactDurationNum = 0;
        if (oldResult.size() > 0){
            JSONObject gatherMap = oldResult.getJSONArray("batch_sample_lis").getJSONObject(0).getJSONObject("gather_map");
            for (String codeStr : gatherMap.keySet()){
                Object theContactDuration = gatherMap.getJSONObject(codeStr).get("contact_duration");
                oldContactDurationMap.put(codeStr, theContactDuration);
                oldContactDurationLis.add(theContactDuration);
                if (theContactDuration != null){
                    sumOldContactDuration += Float.parseFloat(theContactDuration.toString());
                    oldContactDurationNum += 1;
                }
            }
        }
        if (sumOldContactDuration == touchTime && oldContactDurationNum == sampleNum){
            return oldContactDurationLis;
        }
        int integerPart = Math.round(touchTime);
        float floatPart = touchTime-integerPart;
        if(sampleNum == 2){
            int remainder = integerPart % 2;
            float remainderAndFloat = remainder + floatPart;
            int divisionRes = integerPart / 2;
            if(remainderAndFloat < 1){
                contactDurationLis = new JSONArray();
                BigDecimal quotient = new BigDecimal(remainderAndFloat).divide(new BigDecimal(2), 2, RoundingMode.HALF_UP);
                contactDurationLis.add((divisionRes + quotient.floatValue()) + "");
                BigDecimal numKeepTwo = new BigDecimal(divisionRes + remainderAndFloat - quotient.floatValue()).setScale(2, RoundingMode.HALF_UP);
                contactDurationLis.add(numKeepTwo.toString());
            }else if (remainderAndFloat < 1.5){
                contactDurationLis = new JSONArray();
                contactDurationLis.add((divisionRes + 0.5) + "");
                BigDecimal numKeepTwo = new BigDecimal(divisionRes + remainderAndFloat - 0.5).setScale(2, RoundingMode.HALF_UP);
                contactDurationLis.add(numKeepTwo.toString());
            }else {
                contactDurationLis = new JSONArray();
                contactDurationLis.add((divisionRes + 1) + "");
                BigDecimal numKeepTwo = new BigDecimal(divisionRes + remainderAndFloat - 1).setScale(2, RoundingMode.HALF_UP);
                contactDurationLis.add(numKeepTwo.toString());
            }
        }else if (sampleNum == 3){
            if (touchTime > 9){
                int remainder = integerPart % 3;
                float remainderAndFloat = remainder + floatPart;
                int divisionRes = integerPart / 3;
                if(remainderAndFloat < 0.5){
                    contactDurationLis = new JSONArray();
                    contactDurationLis.add(divisionRes + "");
                    contactDurationLis.add(divisionRes + "");
                    BigDecimal numKeepTwo = new BigDecimal(divisionRes + remainderAndFloat).setScale(2, RoundingMode.HALF_UP);
                    contactDurationLis.add(numKeepTwo.toString());
                }else if (remainderAndFloat < 1){
                    contactDurationLis = new JSONArray();
                    contactDurationLis.add(divisionRes + "");
                    contactDurationLis.add((divisionRes+0.5) + "");
                    BigDecimal numKeepTwo = new BigDecimal(divisionRes + remainderAndFloat - 0.5).setScale(2, RoundingMode.HALF_UP);
                    contactDurationLis.add(numKeepTwo.toString());
                }else if (remainderAndFloat < 2){
                    contactDurationLis = new JSONArray();
                    contactDurationLis.add((divisionRes+0.5) + "");
                    contactDurationLis.add((divisionRes+0.5) + "");
                    BigDecimal numKeepTwo = new BigDecimal(divisionRes + remainderAndFloat - 1).setScale(2, RoundingMode.HALF_UP);
                    contactDurationLis.add(numKeepTwo.toString());
                }else {
                    contactDurationLis = new JSONArray();
                    contactDurationLis.add((divisionRes+1) + "");
                    contactDurationLis.add((divisionRes+1) + "");
                    BigDecimal numKeepTwo = new BigDecimal(divisionRes + remainderAndFloat - 2).setScale(2, RoundingMode.HALF_UP);
                    contactDurationLis.add(numKeepTwo.toString());
                }
            }else if (touchTime > 8){
                contactDurationLis = new JSONArray();
                contactDurationLis.add("3");
                contactDurationLis.add("3");
                BigDecimal numKeepTwo = new BigDecimal(touchTime - 6).setScale(2, RoundingMode.HALF_UP);
                contactDurationLis.add(numKeepTwo.toString());
            }else if (touchTime > 6){
                contactDurationLis = new JSONArray();
                contactDurationLis.add("2");
                contactDurationLis.add("2");
                BigDecimal numKeepTwo = new BigDecimal(touchTime - 4).setScale(2, RoundingMode.HALF_UP);
                contactDurationLis.add(numKeepTwo.toString());
            }else {
                int remainder = integerPart % 3;
                float remainderAndFloat = remainder + floatPart;
                int divisionRes = integerPart / 3;
                if (remainderAndFloat < 1.5){
                    contactDurationLis = new JSONArray();
                    BigDecimal quotient = new BigDecimal(remainderAndFloat).divide(new BigDecimal(3), 2, RoundingMode.HALF_UP);
                    contactDurationLis.add((divisionRes + quotient.floatValue()) + "");
                    contactDurationLis.add((divisionRes + quotient.floatValue()) + "");
                    BigDecimal numKeepTwo = new BigDecimal(divisionRes + remainderAndFloat - 2 * quotient.floatValue()).setScale(2, RoundingMode.HALF_UP);
                    contactDurationLis.add(numKeepTwo.toString());
                }else if (remainderAndFloat < 2){
                    contactDurationLis = new JSONArray();
                    contactDurationLis.add((divisionRes + 0.5) + "");
                    contactDurationLis.add((divisionRes + 0.5) + "");
                    BigDecimal numKeepTwo = new BigDecimal(divisionRes + remainderAndFloat - 1).setScale(2, RoundingMode.HALF_UP);
                    contactDurationLis.add(numKeepTwo.toString());
                }else {
                    contactDurationLis = new JSONArray();
                    contactDurationLis.add((divisionRes + 0.5) + "");
                    contactDurationLis.add((divisionRes + 1) + "");
                    BigDecimal numKeepTwo = new BigDecimal(divisionRes + remainderAndFloat - 1.5).setScale(2, RoundingMode.HALF_UP);
                    contactDurationLis.add(numKeepTwo.toString());
                }
            }
        }else {
            int remainder = integerPart % 3;
            float remainderAndFloat = remainder + floatPart;
            int divisionRes = integerPart / 3;
            double halfValue = 0.5 * sampleNum;
            if (remainderAndFloat < halfValue){
                contactDurationLis = new JSONArray();
                for (int i=0; i<sampleNum; i++){
                    double dValue = remainderAndFloat - 0.5 * (i + 1);
                    if (dValue > 0.5){
                        contactDurationLis.add((divisionRes + 0.5) + "");
                    }else if (dValue > 0){
                        contactDurationLis.add(new BigDecimal(divisionRes + dValue).setScale(2, RoundingMode.HALF_UP).toString());
                    }else {
                        contactDurationLis.add(divisionRes + "");
                    }
                }
            }else {
                contactDurationLis = new JSONArray();
                for (int i=0; i<sampleNum; i++){
                    double dValue = remainderAndFloat - 0.5 - halfValue;
                    if (dValue > 0.5){
                        contactDurationLis.add((divisionRes + 1) + "");
                    }else if (dValue > 0){
                        contactDurationLis.add(new BigDecimal(divisionRes + dValue + 0.5).setScale(2, RoundingMode.HALF_UP).toString());
                    }else {
                        contactDurationLis.add((divisionRes + 0.5) + "");
                    }
                }
            }
        }
        return contactDurationLis;
    }

    /**
     * 设置数字位数并格式化文字
     */
    private String formatNumOrStrToNfloatKeepInt(String num, int n){
        return new BigDecimal(num).setScale(n, RoundingMode.HALF_UP).toString();
    }

    /**
     *
     */
    private int accuracyMath(String numStr) {
        int accuracy = 0;
        String[] valueLis = numStr.split("\\.");
        if (valueLis.length > 1) {
            accuracy = valueLis[1].length();
        }
        return accuracy;
    }

    private Integer dealMergeSubPlan(int hasCode, Integer maxSampleCode, JSONObject planDict, JSONObject oldPlan, Integer sampleDays, Integer sampleNum, JSONObject mergePlanTmpl,String  companyOrder){
        for (int dayI = 0 ; dayI < sampleDays; dayI++){
            int batchNum = dayI + 1;
            int oldSampleNum;
            JSONArray oldSampleCodeLis = new JSONArray(), oldSampleKbCodeLis = new JSONArray();
            JSONObject oldGatherMap = new JSONObject(), oldGatherKbMap = new JSONObject(), bglParams = new JSONObject();
            if (dayI < oldPlan.getInteger("sample_days")){
                bglParams = oldPlan.getJSONArray("batch_gather_lis").getJSONObject(dayI);
                bglParams.put("operators_num", oldPlan.getJSONObject("operation").get("operators_num"));
                bglParams.put("equip_working_lis", oldPlan.getJSONObject("operation").get("equip_working_lis"));
                bglParams.put("epe_working_lis", oldPlan.getJSONObject("operation").get("epe_working_lis"));
                bglParams.put("ppe_working_lis", oldPlan.getJSONObject("operation").get("ppe_working_lis"));
                oldSampleNum = oldPlan.getInteger("sample_num");
                oldSampleCodeLis = bglParams.getJSONArray("sample_code_lis");
                oldGatherMap = bglParams.getJSONObject("gather_map");
                oldGatherKbMap = bglParams.getJSONObject("gather_kb_map");
                oldSampleKbCodeLis = bglParams.getJSONArray("sample_kb_code_lis");
            }else {
                if (dayI < mergePlanTmpl.getInteger("sample_days") && dayI < mergePlanTmpl.getJSONArray("batch_gather_lis").size()){
                    bglParams = mergePlanTmpl.getJSONArray("batch_gather_lis").getJSONObject(dayI);
                    bglParams.put("operators_num", oldPlan.getJSONObject("operation").get("operators_num"));
                    bglParams.put("equip_working_lis", oldPlan.getJSONObject("operation").get("equip_working_lis"));
                    bglParams.put("epe_working_lis", oldPlan.getJSONObject("operation").get("epe_working_lis"));
                    bglParams.put("ppe_working_lis", oldPlan.getJSONObject("operation").get("ppe_working_lis"));
                    oldSampleNum = mergePlanTmpl.getInteger("sample_num");
                    oldSampleCodeLis = bglParams.getJSONArray("sample_code_lis");
                    oldGatherMap = bglParams.getJSONObject("gather_map");
                    oldGatherKbMap = bglParams.getJSONObject("gather_kb_map");
                    oldSampleKbCodeLis = bglParams.getJSONArray("sample_kb_code_lis");
                }else {
                    oldSampleNum = 0;
                    bglParams.put("operators_num", oldPlan.getJSONObject("operation").get("operators_num"));
                    bglParams.put("working_equip", 0);
                    bglParams.put("equip_working_lis", oldPlan.getJSONObject("operation").get("equip_working_lis"));
                    bglParams.put("epe_working_lis", oldPlan.getJSONObject("operation").get("epe_working_lis"));
                    bglParams.put("ppe_working_lis", oldPlan.getJSONObject("operation").get("ppe_working_lis"));
                    JSONObject piece = new JSONObject();
                    piece.put("sample_code_lis", new JSONArray());
                    piece.put("sample_kb_code_lis", new JSONArray());
                    piece.put("gather_map", new JSONObject());
                    piece.put("gather_kb_map", new JSONObject());
                    mergePlanTmpl.getJSONArray("batch_gather_lis").add(piece);
                }
            }
            planDict.getJSONArray("batch_gather_lis").add(getBatchGatherLisTmpl(batchNum, bglParams));
            planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).put("gather_kb_map", oldGatherKbMap);
            String batchMinCode = null;
            JSONArray sampleCodeLis = new JSONArray();
            JSONArray sampleKbCodeLis = oldSampleKbCodeLis;
            JSONArray printBarCodeLis = new JSONArray();
            for (int numI = 0; numI < sampleNum; numI++){
                JSONObject gatherOneParams;
                String gatherOneKey, sampleCode;
                if (hasCode == 1){
                    if (numI < oldSampleNum && numI < oldPlan.getJSONArray("batch_gather_lis").size()){
                        gatherOneKey = oldPlan.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONArray("sample_code_lis").getString(numI);
                        gatherOneParams = bglParams.getJSONObject("gather_map").getJSONObject(gatherOneKey);
                        sampleCode = oldSampleCodeLis.getString(numI);
                        Integer printNum = oldPlan.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(sampleCode).getInteger("print_num");
                        String barCode = oldGatherMap.getJSONObject(sampleCode).getString("bar_code");
                        gatherOneParams.put("bar_code", barCode);
                        gatherOneParams.put("print_num", printNum);
                        gatherOneParams.put("print_time", oldPlan.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(sampleCode).get("print_time"));
                        gatherOneParams.put("sample_code", sampleCode);
                        gatherOneParams.put("before_flow", planDict.getJSONObject("substance").getJSONObject("final_substance_info").get("flow"));
                        gatherOneParams.put("after_flow", planDict.getJSONObject("substance").getJSONObject("final_substance_info").get("flow"));
                        Object beforeFlow = oldPlan.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(sampleCode).get("before_flow");
                        if (beforeFlow!= null){
                            gatherOneParams.put("before_flow", beforeFlow);
                            gatherOneParams.put("after_flow", beforeFlow);
                        }
                        gatherOneParams.put("test_duration", planDict.getJSONObject("substance").getJSONObject("final_substance_info").get("test_time") != null ?  planDict.getJSONObject("substance").getJSONObject("final_substance_info").getInteger("test_time") : 0);
                        Integer testDuration = oldPlan.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(sampleCode).getInteger("test_duration");
                        if(testDuration != null){
                            gatherOneParams.put("test_duration", testDuration);
                        }
                        if (printNum != null){
                            printBarCodeLis.add(barCode);
                        }
                    }else {
                        if (numI < mergePlanTmpl.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONArray("sample_code_lis").size()){
                            gatherOneKey = mergePlanTmpl.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONArray("sample_code_lis").getString(numI);
                            gatherOneParams = bglParams.getJSONObject("gather_map").getJSONObject(gatherOneKey);
                            sampleCode = mergePlanTmpl.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONArray("sample_code_lis").getString(numI);
                            Integer printNum = oldGatherMap.getJSONObject(sampleCode).getInteger("print_num");
                            String barCode = oldGatherMap.getJSONObject(sampleCode).getString("bar_code");
                            gatherOneParams.put("bar_code", barCode);
                            gatherOneParams.put("print_num", printNum);
                            gatherOneParams.put("print_time", oldGatherMap.getJSONObject(sampleCode).getOrDefault("print_time", null));
                            gatherOneParams.put("sample_code", sampleCode);
                            gatherOneParams.put("before_flow", planDict.getJSONObject("substance").getJSONObject("final_substance_info").get("flow"));
                            gatherOneParams.put("after_flow", planDict.getJSONObject("substance").getJSONObject("final_substance_info").get("flow"));
                            Object beforeFlow = mergePlanTmpl.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").getJSONObject(sampleCode).get("before_flow");
                            if (beforeFlow != null){
                                gatherOneParams.put("before_flow", beforeFlow);
                                gatherOneParams.put("after_flow", beforeFlow);
                            }
                            if (printNum != null){
                                printBarCodeLis.add(barCode);
                            }
                        }else {
                            gatherOneParams = new JSONObject();
                            maxSampleCode += 1;
                            Integer sampleCodeNum = maxSampleCode;
                            sampleCode = sampleCodeNum > 9 ? sampleCodeNum+"" : "0" + sampleCodeNum;
                            List<String> barCodeList = getBarCodes(companyOrder, 1);
                            gatherOneParams.put("bar_code", barCodeList.get(0));
                            gatherOneParams.put("sample_code", sampleCode);
                            gatherOneParams.put("before_flow", planDict.getJSONObject("substance").getJSONObject("final_substance_info").get("flow"));
                            gatherOneParams.put("after_flow", planDict.getJSONObject("substance").getJSONObject("final_substance_info").get("flow"));
                            gatherOneParams.put("test_duration", planDict.getJSONObject("substance").getJSONObject("final_substance_info").get("test_time") != null ? planDict.getJSONObject("substance").getJSONObject("final_substance_info").getInteger("test_time") : 0);
                        }
                    }
                    sampleCodeLis.add(sampleCode);
                    gatherOneKey = sampleCode;
                }else {
                    sampleCode = "";
                    gatherOneKey = numI + "";
                    gatherOneParams = numI < oldSampleNum ? bglParams.getJSONObject("gather_map").getJSONObject(gatherOneKey) : new JSONObject();
                    gatherOneParams.put("bar_code", "");
                    gatherOneParams.put("sample_code", sampleCode);
                    gatherOneParams.put("before_flow", planDict.getJSONObject("substance").getJSONObject("final_substance_info").get("flow"));
                    gatherOneParams.put("after_flow", planDict.getJSONObject("substance").getJSONObject("final_substance_info").get("flow"));
                    gatherOneParams.put("test_duration", planDict.getJSONObject("substance").getJSONObject("final_substance_info").get("test_time") != null ? planDict.getJSONObject("substance").getJSONObject("final_substance_info").getInteger("test_time") : 0);

                }
                if (batchMinCode == null){
                    batchMinCode = sampleCode + "";
                }
                JSONObject gatherOne = getGatherOneTmpl(gatherOneParams);
                planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").put(gatherOneKey, gatherOne);
                mergePlanTmpl.getJSONArray("batch_gather_lis").getJSONObject(dayI).getJSONObject("gather_map").put(gatherOneKey, gatherOne);
            }
            mergePlanTmpl.getJSONArray("batch_gather_lis").getJSONObject(dayI).put("sample_code_lis", sampleCodeLis);
            mergePlanTmpl.getJSONArray("batch_gather_lis").getJSONObject(dayI).put("sample_kb_code_lis", sampleKbCodeLis);
            planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).put("sample_code_lis", sampleCodeLis);
            planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).put("sample_kb_code_lis", sampleKbCodeLis);
            planDict.getJSONArray("batch_gather_lis").getJSONObject(dayI).put("print_bar_code_lis", printBarCodeLis);
            planDict.put("sample_code_lis", sampleCodeLis);
            planDict.put("sample_kb_code_lis", sampleKbCodeLis);
            planDict.put("print_bar_code_lis", printBarCodeLis);
        }
        planDict.put("sample_days", sampleDays);
        planDict.put("sample_num", sampleNum);
        return maxSampleCode;
    }

    /**
     * 噪声结果计算
     */
    private JSONArray getNoiseResultTmpl(JSONObject planDict, JSONObject hazard, JSONObject pfnMap, JSONObject oldResultMap, Date nowTime){
        String key = "noise";
        Integer accuracy = 0;
        JSONObject substanceInfo = planDict.getJSONObject("substance").getJSONObject("substance_info");
        Integer limitV = substanceInfo.getInteger("mark");
        JSONArray resultDictLis = new JSONArray();
        JSONObject newContactDurationMap = new JSONObject();
        for (String planCode : planDict.getJSONArray("batch_gather_lis").getJSONObject(0).getJSONObject("gather_map").keySet()){
            Map<String, Object> map = new HashMap<>();
            map.put("lis", new JSONArray());
            map.put("has_valid_value", 2);
            newContactDurationMap.put(planCode, map);
        }
        for (Object o : hazard.getJSONArray("pfn_ids")){
            String pfnIdStr = o.toString();
            JSONObject pfnObj = pfnMap.getJSONObject(pfnIdStr);
            Object laborIntensity = pfnObj.get("labor_intensity");
            JSONObject oldResult = oldResultMap.containsKey(pfnIdStr) ? oldResultMap.getJSONObject(pfnIdStr) : new JSONObject();
            JSONObject resultDict = generateResultInfoTmpl(planDict, pfnObj, nowTime);
            JSONArray contactDurationLis = getContactDurationLis(oldResult, resultDict.getInteger("sample_num"), resultDict.getString("touch_time"));
            Integer contactDurationLisLen = contactDurationLis.size();
            if (contactDurationLisLen < planDict.getInteger("sample_num")){
                for (int cdsI = 0; cdsI < planDict.getInteger("sample_num") - contactDurationLisLen; cdsI++){
                    contactDurationLis.add("");
                }
            }
            JSONObject oldConclusion = oldResult.getJSONObject("conclusion") != null ? oldResult.getJSONObject("conclusion") : new JSONObject();
            JSONObject conclusion = new JSONObject();
            conclusion.put("accuracy", accuracy);
            conclusion.put("limit_v", limitV);
            conclusion.put("is_noise", limitV == 85 ? 1 : 2);
            conclusion.put("contact_rate", 0);
            conclusion.put("labor_intensity", laborIntensity);
            conclusion.put("max_v", oldConclusion.get("max_v") != null ? oldConclusion.get("max_v") : "");
            conclusion.put("min_v", oldConclusion.get("min_v") != null ? oldConclusion.get("min_v") : "");
            conclusion.put("test_range", oldConclusion.get("test_range") != null ? oldConclusion.get("test_range") : "");
            conclusion.put("conclusion", oldConclusion.get("conclusion") != null ? oldConclusion.get("conclusion") : "");
            resultDict.put("conclusion", conclusion);
            resultDict.put("is_exist", oldResult.get("is_exist") != null ? oldResult.get("is_exist") : 1);
            resultDict.put("is_exceed", oldResult.get("is_exceed") != null ? oldResult.get("is_exceed") : 2);
            JSONArray resultInfoBatchSampleLis = getResultInfoBatchSampleLis(oldResult.getJSONArray("batch_sample_lis") != null ? oldResult.getJSONArray("batch_sample_lis") : new JSONArray(), planDict.getJSONArray("batch_gather_lis"), key, newContactDurationMap, contactDurationLis);
            resultDict.put("batch_sample_lis", resultInfoBatchSampleLis.get(0));
            newContactDurationMap = resultInfoBatchSampleLis.getJSONObject(1);
            resultDictLis.add(resultDict);
        }
        JSONArray returnList = new JSONArray();
        returnList.add(resultDictLis);
        returnList.add(newContactDurationMap);
        return returnList;
    }

    private JSONArray getResultInfoBatchSampleLis(JSONArray oldBatchGatherLis, JSONArray planBatchGatherLis, String key, JSONObject newContactDurationMap, JSONArray contactDurationLis){
        int oldBatchGatherLisLen = oldBatchGatherLis.size();
        JSONArray newBatchGatherLis = new JSONArray();
        for (int iB = 0; iB < planBatchGatherLis.size(); iB++){
            JSONObject batchGatherOne = planBatchGatherLis.getJSONObject(iB);
            JSONObject oldBatchGatherOne = iB < oldBatchGatherLisLen ? oldBatchGatherLis.getJSONObject(iB) : new JSONObject();
            JSONObject oldBatchConclusion = oldBatchGatherOne != null ? (oldBatchGatherOne.getJSONObject(key) != null ? oldBatchGatherOne.getJSONObject(key) : new JSONObject()) : new JSONObject();
            JSONObject gatherMap = new JSONObject();
            int sortNum = 0;
            for (String code : batchGatherOne.getJSONObject("gather_map").keySet()){
                JSONObject codeMap = new JSONObject();
                codeMap.put("sample_code", code);
                codeMap.put("contact_duration", contactDurationLis.size() > 0 ? contactDurationLis.getString(sortNum) : "");
                gatherMap.put(code, codeMap);
                if (contactDurationLis.size() > 0 && contactDurationLis.get(sortNum) != null){
                    newContactDurationMap.getJSONObject(code).getJSONArray("lis").add(contactDurationLis.get(sortNum));
                    newContactDurationMap.getJSONObject(code).put("has_valid_value", 1);
                }
                sortNum += 1;
            }
            JSONObject piece = new JSONObject();
            JSONObject keyMap = new JSONObject();
            piece.put("batch_num", batchGatherOne.get("batch_num"));
            piece.put("gather_date", batchGatherOne.get("gather_date"));
            piece.put("result", batchGatherOne.get("result") != null ? batchGatherOne.get("result") : "");
            piece.put("gather_map", gatherMap);
            piece.put("conclusion_key", key);
            keyMap.put("max_v", oldBatchConclusion.get("max_v") != null ? oldBatchConclusion.get("max_v") : "");
            keyMap.put("min_v", oldBatchConclusion.get("min_v") != null ? oldBatchConclusion.get("min_v") : "");
            keyMap.put("test_range", oldBatchConclusion.get("test_range") != null ? oldBatchConclusion.get("test_range") : "");
            keyMap.put("conclusion", oldBatchConclusion.get("conclusion") != null ? oldBatchConclusion.get("conclusion") : "");
            piece.put("conclusion", keyMap);
            newBatchGatherLis.add(piece);
        }
        JSONArray returnList = new JSONArray();
        returnList.add(newBatchGatherLis);
        returnList.add(newContactDurationMap);
        return returnList;
    }

    /**
     * 高温 注：以下内容除了高温以外 key不同别无区别 因为python代码以解耦形式编写 所以这边也先这么编写 后续优化如有需要可以提出公有方法
     */
    private JSONArray getHtResultTmpl(JSONObject planDict,JSONObject hazard,JSONObject pfnMap, JSONObject oldResultMap, Date nowTime,JSONObject htLimitMap){
        String key = "kt";
        Integer accuracy = 0;
        JSONArray resultDictLis = new JSONArray();
        JSONObject newContactDurationMap = new JSONObject();
        for (String planCode : planDict.getJSONArray("batch_gather_lis").getJSONObject(0).getJSONObject("gather_map").keySet()){
            Map<String, Object> map = new HashMap<>();
            map.put("lis", new JSONArray());
            map.put("has_valid_value", 2);
            newContactDurationMap.put(planCode, map);
        }
        for (Object o : hazard.getJSONArray("pfn_ids")){
            String pfnIdStr = o.toString();
            JSONObject pfnObj = pfnMap.getJSONObject(pfnIdStr);
            Object laborIntensity = pfnObj.get("labor_intensity");
            JSONObject oldResult = oldResultMap.containsKey(pfnIdStr) ? oldResultMap.getJSONObject(pfnIdStr) : new JSONObject();
            JSONObject resultDict = generateResultInfoTmpl(planDict, pfnObj, nowTime);
            JSONArray contactDurationLis = getContactDurationLis(oldResult, resultDict.getInteger("sample_num"), resultDict.getString("touch_time"));
            Integer contactDurationLisLen = contactDurationLis.size();
            if (contactDurationLisLen < planDict.getInteger("sample_num")){
                for (int cdsI = 0; cdsI < planDict.getInteger("sample_num") - contactDurationLisLen; cdsI++){
                    contactDurationLis.add("");
                }
            }
            Float touchTime = resultDict.getFloat("touch_time");
            int contactRate = 0;
            Integer limitV = 0;
            if (touchTime != null){
                if (touchTime < 3){
                    contactRate = 25;
                }else if (touchTime < 5){
                    contactRate = 50;
                }else if (touchTime < 7){
                    contactRate = 75;
                }else {
                    contactRate = 100;
                }
                if (laborIntensity != null){
                    limitV = htLimitMap.getJSONObject(Integer.toString(contactRate)).getInteger(laborIntensity.toString());
                }
            }
            JSONObject oldConclusion = oldResult.getJSONObject("conclusion") != null ? oldResult.getJSONObject("conclusion") : new JSONObject();
            JSONObject conclusion = new JSONObject();
            conclusion.put("accuracy", accuracy);
            conclusion.put("limit_v", limitV);
            conclusion.put("is_noise", 1);
            conclusion.put("contact_rate", contactRate);
            conclusion.put("labor_intensity", laborIntensity);
            conclusion.put("max_v", oldConclusion.get("max_v") != null ? oldConclusion.get("max_v") : "");
            conclusion.put("min_v", oldConclusion.get("min_v") != null ? oldConclusion.get("min_v") : "");
            conclusion.put("test_range", oldConclusion.get("test_range") != null ? oldConclusion.get("test_range") : "");
            conclusion.put("conclusion", oldConclusion.get("conclusion") != null ? oldConclusion.get("conclusion") : "");
            resultDict.put("conclusion", conclusion);
            resultDict.put("is_exist", oldResult.get("is_exist") != null ? oldResult.get("is_exist") : 1);
            resultDict.put("is_exceed", oldResult.get("is_exceed") != null ? oldResult.get("is_exceed") : 2);
            JSONArray resultInfoBatchSampleLis = getResultInfoBatchSampleLis(oldResult.getJSONArray("batch_sample_lis") != null ? oldResult.getJSONArray("batch_sample_lis") : new JSONArray(), planDict.getJSONArray("batch_gather_lis"), key, newContactDurationMap, contactDurationLis);
            resultDict.put("batch_sample_lis", resultInfoBatchSampleLis.get(0));
            newContactDurationMap = resultInfoBatchSampleLis.getJSONObject(1);
            resultDictLis.add(resultDict);
        }
        JSONArray returnList = new JSONArray();
        returnList.add(resultDictLis);
        returnList.add(newContactDurationMap);
        return returnList;
    }

    /**
     * 紫外辐射
     */
    private JSONArray getUtResultTmpl(JSONObject planDict,JSONObject hazard, JSONObject pfnMap, JSONObject oldResultMap, Date nowTime){
        String key = "ut";
        JSONArray resultDictLis = new JSONArray();
        JSONObject newContactDurationMap = new JSONObject();
        for (String planCode : planDict.getJSONArray("batch_gather_lis").getJSONObject(0).getJSONObject("gather_map").keySet()){
            Map<String, Object> map = new HashMap<>();
            map.put("lis", new JSONArray());
            map.put("has_valid_value", 2);
            newContactDurationMap.put(planCode, map);
        }
        for (Object o : hazard.getJSONArray("pfn_ids")){
            String pfnIdStr = o.toString();
            JSONObject pfnObj = pfnMap.getJSONObject(pfnIdStr);
            Object laborIntensity = pfnObj.get("labor_intensity");
            JSONObject oldResult = oldResultMap.containsKey(pfnIdStr) ? oldResultMap.getJSONObject(pfnIdStr) : new JSONObject();
            JSONObject resultDict = generateResultInfoTmpl(planDict, pfnObj, nowTime);
            JSONArray contactDurationLis = getContactDurationLis(oldResult, resultDict.getInteger("sample_num"), resultDict.getString("touch_time"));
            Integer contactDurationLisLen = contactDurationLis.size();
            if (contactDurationLisLen < planDict.getInteger("sample_num")){
                for (int cdsI = 0; cdsI < planDict.getInteger("sample_num") - contactDurationLisLen; cdsI++){
                    contactDurationLis.add("");
                }
            }
            JSONObject substanceInfo = planDict.getJSONObject("substance").getJSONObject("substance_info");
            String limitV = substanceInfo.getString("mark");
            Integer accuracy = accuracyMath(limitV);
            JSONObject oldConclusion = oldResult.getJSONObject("conclusion") != null ? oldResult.getJSONObject("conclusion") : new JSONObject();
            JSONObject conclusion = new JSONObject();
            conclusion.put("accuracy", accuracy);
            conclusion.put("limit_v", limitV);
            conclusion.put("is_noise", 1);
            conclusion.put("contact_rate", 0);
            conclusion.put("labor_intensity", laborIntensity);
            conclusion.put("max_v", oldConclusion.get("max_v") != null ? oldConclusion.get("max_v") : "");
            conclusion.put("min_v", oldConclusion.get("min_v") != null ? oldConclusion.get("min_v") : "");
            conclusion.put("test_range", oldConclusion.get("test_range") != null ? oldConclusion.get("test_range") : "");
            conclusion.put("conclusion", oldConclusion.get("conclusion") != null ? oldConclusion.get("conclusion") : "");
            resultDict.put("conclusion", conclusion);
            resultDict.put("is_exist", oldResult.get("is_exist") != null ? oldResult.get("is_exist") : 1);
            resultDict.put("is_exceed", oldResult.get("is_exceed") != null ? oldResult.get("is_exceed") : 2);
            JSONArray resultInfoBatchSampleLis = getResultInfoBatchSampleLis(oldResult.getJSONArray("batch_sample_lis") != null ? oldResult.getJSONArray("batch_sample_lis") : new JSONArray(), planDict.getJSONArray("batch_gather_lis"), key, newContactDurationMap, contactDurationLis);
            resultDict.put("batch_sample_lis", resultInfoBatchSampleLis.get(0));
            newContactDurationMap = resultInfoBatchSampleLis.getJSONObject(1);
            resultDictLis.add(resultDict);
        }
        JSONArray returnList = new JSONArray();
        returnList.add(resultDictLis);
        returnList.add(newContactDurationMap);
        return returnList;
    }

    /**
     * 手传振动
     */
    private JSONArray getHvResultTmpl(JSONObject planDict,JSONObject hazard, JSONObject pfnMap, JSONObject oldResultMap, Date nowTime){
        String key = "hv";
        JSONArray resultDictLis = new JSONArray();
        JSONObject newContactDurationMap = new JSONObject();
        for (String planCode : planDict.getJSONArray("batch_gather_lis").getJSONObject(0).getJSONObject("gather_map").keySet()){
            Map<String, Object> map = new HashMap<>();
            map.put("lis", new JSONArray());
            map.put("has_valid_value", 2);
            newContactDurationMap.put(planCode, map);
        }
        for (Object o : hazard.getJSONArray("pfn_ids")){
            String pfnIdStr = o.toString();
            JSONObject pfnObj = pfnMap.getJSONObject(pfnIdStr);
            Object laborIntensity = pfnObj.get("labor_intensity");
            JSONObject oldResult = oldResultMap.containsKey(pfnIdStr) ? oldResultMap.getJSONObject(pfnIdStr) : new JSONObject();
            JSONObject resultDict = generateResultInfoTmpl(planDict, pfnObj, nowTime);
            JSONArray contactDurationLis = getContactDurationLis(oldResult, resultDict.getInteger("sample_num"), resultDict.getString("touch_time"));
            Integer contactDurationLisLen = contactDurationLis.size();
            if (contactDurationLisLen < planDict.getInteger("sample_num")){
                for (int cdsI = 0; cdsI < planDict.getInteger("sample_num") - contactDurationLisLen; cdsI++){
                    contactDurationLis.add("");
                }
            }
            JSONObject substanceInfo = planDict.getJSONObject("substance").getJSONObject("substance_info");
            String limitV = substanceInfo.getString("mark");
            Integer accuracy = accuracyMath(limitV);
            JSONObject oldConclusion = oldResult.getJSONObject("conclusion") != null ? oldResult.getJSONObject("conclusion") : new JSONObject();
            JSONObject conclusion = new JSONObject();
            conclusion.put("accuracy", accuracy);
            conclusion.put("limit_v", limitV);
            conclusion.put("is_noise", 1);
            conclusion.put("contact_rate", 0);
            conclusion.put("labor_intensity", laborIntensity);
            conclusion.put("max_v", oldConclusion.get("max_v") != null ? oldConclusion.get("max_v") : "");
            conclusion.put("min_v", oldConclusion.get("min_v") != null ? oldConclusion.get("min_v") : "");
            conclusion.put("test_range", oldConclusion.get("test_range") != null ? oldConclusion.get("test_range") : "");
            conclusion.put("conclusion", oldConclusion.get("conclusion") != null ? oldConclusion.get("conclusion") : "");
            resultDict.put("conclusion", conclusion);
            resultDict.put("is_exist", oldResult.get("is_exist") != null ? oldResult.get("is_exist") : 1);
            resultDict.put("is_exceed", oldResult.get("is_exceed") != null ? oldResult.get("is_exceed") : 2);
            JSONArray resultInfoBatchSampleLis = getResultInfoBatchSampleLis(oldResult.getJSONArray("batch_sample_lis") != null ? oldResult.getJSONArray("batch_sample_lis") : new JSONArray(), planDict.getJSONArray("batch_gather_lis"), key, newContactDurationMap, contactDurationLis);
            resultDict.put("batch_sample_lis", resultInfoBatchSampleLis.get(0));
            newContactDurationMap = resultInfoBatchSampleLis.getJSONObject(1);
            resultDictLis.add(resultDict);
        }
        JSONArray returnList = new JSONArray();
        returnList.add(resultDictLis);
        returnList.add(newContactDurationMap);
        return returnList;
    }

    /**
     * 工频电场
     */
    private JSONArray getEleResultTmpl(JSONObject planDict,JSONObject hazard,JSONObject pfnMap, JSONObject oldResultMap, Date nowTime){
        String key = "ele";
        JSONArray resultDictLis = new JSONArray();
        JSONObject newContactDurationMap = new JSONObject();
        for (String planCode : planDict.getJSONArray("batch_gather_lis").getJSONObject(0).getJSONObject("gather_map").keySet()){
            Map<String, Object> map = new HashMap<>();
            map.put("lis", new JSONArray());
            map.put("has_valid_value", 2);
            newContactDurationMap.put(planCode, map);
        }
        for (Object o : hazard.getJSONArray("pfn_ids")){
            String pfnIdStr = o.toString();
            JSONObject pfnObj = pfnMap.getJSONObject(pfnIdStr);
            Object laborIntensity = pfnObj.get("labor_intensity");
            JSONObject oldResult = oldResultMap.containsKey(pfnIdStr) ? oldResultMap.getJSONObject(pfnIdStr) : new JSONObject();
            JSONObject resultDict = generateResultInfoTmpl(planDict, pfnObj, nowTime);
            JSONArray contactDurationLis = getContactDurationLis(oldResult, resultDict.getInteger("sample_num"), resultDict.getString("touch_time"));
            Integer contactDurationLisLen = contactDurationLis.size();
            if (contactDurationLisLen < planDict.getInteger("sample_num")){
                for (int cdsI = 0; cdsI < planDict.getInteger("sample_num") - contactDurationLisLen; cdsI++){
                    contactDurationLis.add("");
                }
            }
            JSONObject substanceInfo = planDict.getJSONObject("substance").getJSONObject("substance_info");
            String limitV = substanceInfo.getString("mark");
            Integer accuracy = accuracyMath(limitV);
            JSONObject oldConclusion = oldResult.getJSONObject("conclusion") != null ? oldResult.getJSONObject("conclusion") : new JSONObject();
            JSONObject conclusion = new JSONObject();
            conclusion.put("accuracy", accuracy);
            conclusion.put("limit_v", limitV);
            conclusion.put("is_noise", 1);
            conclusion.put("contact_rate", 0);
            conclusion.put("labor_intensity", laborIntensity);
            conclusion.put("max_v", oldConclusion.get("max_v") != null ? oldConclusion.get("max_v") : "");
            conclusion.put("min_v", oldConclusion.get("min_v") != null ? oldConclusion.get("min_v") : "");
            conclusion.put("test_range", oldConclusion.get("test_range") != null ? oldConclusion.get("test_range") : "");
            conclusion.put("conclusion", oldConclusion.get("conclusion") != null ? oldConclusion.get("conclusion") : "");
            resultDict.put("conclusion", conclusion);
            resultDict.put("is_exist", oldResult.get("is_exist") != null ? oldResult.get("is_exist") : 1);
            resultDict.put("is_exceed", oldResult.get("is_exceed") != null ? oldResult.get("is_exceed") : 2);
            JSONArray resultInfoBatchSampleLis = getResultInfoBatchSampleLis(oldResult.getJSONArray("batch_sample_lis") != null ? oldResult.getJSONArray("batch_sample_lis") : new JSONArray(), planDict.getJSONArray("batch_gather_lis"), key, newContactDurationMap, contactDurationLis);
            resultDict.put("batch_sample_lis", resultInfoBatchSampleLis.get(0));
            newContactDurationMap = resultInfoBatchSampleLis.getJSONObject(1);
            resultDictLis.add(resultDict);
        }
        JSONArray returnList = new JSONArray();
        returnList.add(resultDictLis);
        returnList.add(newContactDurationMap);
        return returnList;
    }

    /**
     * 高频电磁场  (限值不确定)
     */
    private JSONArray getEmResultTmpl(JSONObject planDict,JSONObject hazard,JSONObject pfnMap, JSONObject oldResultMap, Date nowTime){
        String key = "em";
        JSONArray resultDictLis = new JSONArray();
        JSONObject newContactDurationMap = new JSONObject();
        for (String planCode : planDict.getJSONArray("batch_gather_lis").getJSONObject(0).getJSONObject("gather_map").keySet()){
            Map<String, Object> map = new HashMap<>();
            map.put("lis", new JSONArray());
            map.put("has_valid_value", 2);
            newContactDurationMap.put(planCode, map);
        }
        for (Object o : hazard.getJSONArray("pfn_ids")){
            String pfnIdStr = o.toString();
            JSONObject pfnObj = pfnMap.getJSONObject(pfnIdStr);
            Object laborIntensity = pfnObj.get("labor_intensity");
            JSONObject oldResult = oldResultMap.containsKey(pfnIdStr) ? oldResultMap.getJSONObject(pfnIdStr) : new JSONObject();
            JSONObject resultDict = generateResultInfoTmpl(planDict, pfnObj, nowTime);
            JSONArray contactDurationLis = getContactDurationLis(oldResult, resultDict.getInteger("sample_num"), resultDict.getString("touch_time"));
            Integer contactDurationLisLen = contactDurationLis.size();
            if (contactDurationLisLen < planDict.getInteger("sample_num")){
                for (int cdsI = 0; cdsI < planDict.getInteger("sample_num") - contactDurationLisLen; cdsI++){
                    contactDurationLis.add("");
                }
            }
            JSONObject substanceInfo = planDict.getJSONObject("substance").getJSONObject("substance_info");
            String limitV = substanceInfo.getString("mark");
            Integer accuracy = accuracyMath(limitV);
            JSONObject oldConclusion = oldResult.getJSONObject("conclusion") != null ? oldResult.getJSONObject("conclusion") : new JSONObject();
            JSONObject conclusion = new JSONObject();
            conclusion.put("accuracy", accuracy);
            conclusion.put("limit_v", limitV);
            conclusion.put("is_noise", 1);
            conclusion.put("contact_rate", 0);
            conclusion.put("labor_intensity", laborIntensity);
            conclusion.put("max_v", oldConclusion.get("max_v") != null ? oldConclusion.get("max_v") : "");
            conclusion.put("min_v", oldConclusion.get("min_v") != null ? oldConclusion.get("min_v") : "");
            conclusion.put("test_range", oldConclusion.get("test_range") != null ? oldConclusion.get("test_range") : "");
            conclusion.put("conclusion", oldConclusion.get("conclusion") != null ? oldConclusion.get("conclusion") : "");
            resultDict.put("conclusion", conclusion);
            resultDict.put("is_exist", oldResult.get("is_exist") != null ? oldResult.get("is_exist") : 1);
            resultDict.put("is_exceed", oldResult.get("is_exceed") != null ? oldResult.get("is_exceed") : 2);
            JSONArray resultInfoBatchSampleLis = getResultInfoBatchSampleLis(oldResult.getJSONArray("batch_sample_lis") != null ? oldResult.getJSONArray("batch_sample_lis") : new JSONArray(), planDict.getJSONArray("batch_gather_lis"), key, newContactDurationMap, contactDurationLis);
            resultDict.put("batch_sample_lis", resultInfoBatchSampleLis.get(0));
            newContactDurationMap = resultInfoBatchSampleLis.getJSONObject(1);
            resultDictLis.add(resultDict);
        }
        JSONArray returnList = new JSONArray();
        returnList.add(resultDictLis);
        returnList.add(newContactDurationMap);
        return returnList;
    }

    /**
     * 超高频辐射  (限值不确定)
     */
    private JSONArray getUhfResultTmpl(JSONObject planDict,JSONObject hazard,JSONObject pfnMap, JSONObject oldResultMap, Date nowTime){
        String key = "uhf";
        JSONArray resultDictLis = new JSONArray();
        JSONObject newContactDurationMap = new JSONObject();
        for (String planCode : planDict.getJSONArray("batch_gather_lis").getJSONObject(0).getJSONObject("gather_map").keySet()){
            Map<String, Object> map = new HashMap<>();
            map.put("lis", new JSONArray());
            map.put("has_valid_value", 2);
            newContactDurationMap.put(planCode, map);
        }
        for (Object o : hazard.getJSONArray("pfn_ids")){
            String pfnIdStr = o.toString();
            JSONObject pfnObj = pfnMap.getJSONObject(pfnIdStr);
            Object laborIntensity = pfnObj.get("labor_intensity");
            JSONObject oldResult = oldResultMap.containsKey(pfnIdStr) ? oldResultMap.getJSONObject(pfnIdStr) : new JSONObject();
            JSONObject resultDict = generateResultInfoTmpl(planDict, pfnObj, nowTime);
            JSONArray contactDurationLis = getContactDurationLis(oldResult, resultDict.getInteger("sample_num"), resultDict.getString("touch_time"));
            Integer contactDurationLisLen = contactDurationLis.size();
            if (contactDurationLisLen < planDict.getInteger("sample_num")){
                for (int cdsI = 0; cdsI < planDict.getInteger("sample_num") - contactDurationLisLen; cdsI++){
                    contactDurationLis.add("");
                }
            }
            JSONObject substanceInfo = planDict.getJSONObject("substance").getJSONObject("substance_info");
            String limitV = substanceInfo.getString("mark");
            Integer accuracy = accuracyMath(limitV);
            JSONObject oldConclusion = oldResult.getJSONObject("conclusion") != null ? oldResult.getJSONObject("conclusion") : new JSONObject();
            JSONObject conclusion = new JSONObject();
            conclusion.put("accuracy", accuracy);
            conclusion.put("limit_v", limitV);
            conclusion.put("is_noise", 1);
            conclusion.put("contact_rate", 0);
            conclusion.put("labor_intensity", laborIntensity);
            conclusion.put("max_v", oldConclusion.get("max_v") != null ? oldConclusion.get("max_v") : "");
            conclusion.put("min_v", oldConclusion.get("min_v") != null ? oldConclusion.get("min_v") : "");
            conclusion.put("test_range", oldConclusion.get("test_range") != null ? oldConclusion.get("test_range") : "");
            conclusion.put("conclusion", oldConclusion.get("conclusion") != null ? oldConclusion.get("conclusion") : "");
            resultDict.put("conclusion", conclusion);
            resultDict.put("is_exist", oldResult.get("is_exist") != null ? oldResult.get("is_exist") : 1);
            resultDict.put("is_exceed", oldResult.get("is_exceed") != null ? oldResult.get("is_exceed") : 2);
            JSONArray resultInfoBatchSampleLis = getResultInfoBatchSampleLis(oldResult.getJSONArray("batch_sample_lis") != null ? oldResult.getJSONArray("batch_sample_lis") : new JSONArray(), planDict.getJSONArray("batch_gather_lis"), key, newContactDurationMap, contactDurationLis);
            resultDict.put("batch_sample_lis", resultInfoBatchSampleLis.get(0));
            newContactDurationMap = resultInfoBatchSampleLis.getJSONObject(1);
            resultDictLis.add(resultDict);
        }
        JSONArray returnList = new JSONArray();
        returnList.add(resultDictLis);
        returnList.add(newContactDurationMap);
        return returnList;
    }

    /**
     * 微波辐射 (限值不确定)
     */
    private JSONArray getMvResultTmpl(JSONObject planDict,JSONObject hazard,JSONObject pfnMap, JSONObject oldResultMap, Date nowTime){
        String key = "mv";
        JSONArray resultDictLis = new JSONArray();
        JSONObject newContactDurationMap = new JSONObject();
        for (String planCode : planDict.getJSONArray("batch_gather_lis").getJSONObject(0).getJSONObject("gather_map").keySet()){
            Map<String, Object> map = new HashMap<>();
            map.put("lis", new JSONArray());
            map.put("has_valid_value", 2);
            newContactDurationMap.put(planCode, map);
        }
        for (Object o : hazard.getJSONArray("pfn_ids")){
            String pfnIdStr = o.toString();
            JSONObject pfnObj = pfnMap.getJSONObject(pfnIdStr);
            Object laborIntensity = pfnObj.get("labor_intensity");
            JSONObject oldResult = oldResultMap.containsKey(pfnIdStr) ? oldResultMap.getJSONObject(pfnIdStr) : new JSONObject();
            JSONObject resultDict = generateResultInfoTmpl(planDict, pfnObj, nowTime);
            JSONArray contactDurationLis = getContactDurationLis(oldResult, resultDict.getInteger("sample_num"), resultDict.getString("touch_time"));
            Integer contactDurationLisLen = contactDurationLis.size();
            if (contactDurationLisLen < planDict.getInteger("sample_num")){
                for (int cdsI = 0; cdsI < planDict.getInteger("sample_num") - contactDurationLisLen; cdsI++){
                    contactDurationLis.add("");
                }
            }
            JSONObject substanceInfo = planDict.getJSONObject("substance").getJSONObject("substance_info");
            String limitV = substanceInfo.getString("mark");
            Integer accuracy = accuracyMath(limitV);
            JSONObject oldConclusion = oldResult.getJSONObject("conclusion") != null ? oldResult.getJSONObject("conclusion") : new JSONObject();
            JSONObject conclusion = new JSONObject();
            conclusion.put("accuracy", accuracy);
            conclusion.put("limit_v", limitV);
            conclusion.put("is_noise", 1);
            conclusion.put("contact_rate", 0);
            conclusion.put("labor_intensity", laborIntensity);
            conclusion.put("max_v", oldConclusion.get("max_v") != null ? oldConclusion.get("max_v") : "");
            conclusion.put("min_v", oldConclusion.get("min_v") != null ? oldConclusion.get("min_v") : "");
            conclusion.put("test_range", oldConclusion.get("test_range") != null ? oldConclusion.get("test_range") : "");
            conclusion.put("conclusion", oldConclusion.get("conclusion") != null ? oldConclusion.get("conclusion") : "");
            resultDict.put("conclusion", conclusion);
            resultDict.put("is_exist", oldResult.get("is_exist") != null ? oldResult.get("is_exist") : 1);
            resultDict.put("is_exceed", oldResult.get("is_exceed") != null ? oldResult.get("is_exceed") : 2);
            JSONArray resultInfoBatchSampleLis = getResultInfoBatchSampleLis(oldResult.getJSONArray("batch_sample_lis") != null ? oldResult.getJSONArray("batch_sample_lis") : new JSONArray(), planDict.getJSONArray("batch_gather_lis"), key, newContactDurationMap, contactDurationLis);
            resultDict.put("batch_sample_lis", resultInfoBatchSampleLis.get(0));
            newContactDurationMap = resultInfoBatchSampleLis.getJSONObject(1);
            resultDictLis.add(resultDict);
        }
        JSONArray returnList = new JSONArray();
        returnList.add(resultDictLis);
        returnList.add(newContactDurationMap);
        return returnList;
    }

    /**
     * 风速
     */
    private JSONArray getWspResultTmpl(JSONObject planDict,JSONObject hazard,JSONObject pfnMap, JSONObject oldResultMap, Date nowTime){
        String key = "wsp";
        JSONArray resultDictLis = new JSONArray();
        JSONObject newContactDurationMap = new JSONObject();
        for (String planCode : planDict.getJSONArray("batch_gather_lis").getJSONObject(0).getJSONObject("gather_map").keySet()){
            Map<String, Object> map = new HashMap<>();
            map.put("lis", new JSONArray());
            map.put("has_valid_value", 2);
            newContactDurationMap.put(planCode, map);
        }
        for (Object o : hazard.getJSONArray("pfn_ids")){
            String pfnIdStr = o.toString();
            JSONObject pfnObj = pfnMap.getJSONObject(pfnIdStr);
            Object laborIntensity = pfnObj.get("labor_intensity");
            JSONObject oldResult = oldResultMap.containsKey(pfnIdStr) ? oldResultMap.getJSONObject(pfnIdStr) : new JSONObject();
            JSONObject resultDict = generateResultInfoTmpl(planDict, pfnObj, nowTime);
            JSONArray contactDurationLis = getContactDurationLis(oldResult, resultDict.getInteger("sample_num"), resultDict.getString("touch_time"));
            Integer contactDurationLisLen = contactDurationLis.size();
            if (contactDurationLisLen < planDict.getInteger("sample_num")){
                for (int cdsI = 0; cdsI < planDict.getInteger("sample_num") - contactDurationLisLen; cdsI++){
                    contactDurationLis.add("");
                }
            }
            JSONObject substanceInfo = planDict.getJSONObject("substance").getJSONObject("substance_info");
            String limitV = substanceInfo.getString("mark");
            Integer accuracy = accuracyMath(limitV);
            JSONObject oldConclusion = oldResult.getJSONObject("conclusion") != null ? oldResult.getJSONObject("conclusion") : new JSONObject();
            JSONObject conclusion = new JSONObject();
            conclusion.put("accuracy", accuracy);
            conclusion.put("limit_v", limitV);
            conclusion.put("is_noise", 1);
            conclusion.put("contact_rate", 0);
            conclusion.put("labor_intensity", laborIntensity);
            conclusion.put("max_v", oldConclusion.get("max_v") != null ? oldConclusion.get("max_v") : "");
            conclusion.put("min_v", oldConclusion.get("min_v") != null ? oldConclusion.get("min_v") : "");
            conclusion.put("test_range", oldConclusion.get("test_range") != null ? oldConclusion.get("test_range") : "");
            conclusion.put("conclusion", oldConclusion.get("conclusion") != null ? oldConclusion.get("conclusion") : "");
            resultDict.put("conclusion", conclusion);
            resultDict.put("is_exist", oldResult.get("is_exist") != null ? oldResult.get("is_exist") : 1);
            resultDict.put("is_exceed", oldResult.get("is_exceed") != null ? oldResult.get("is_exceed") : 2);
            JSONArray resultInfoBatchSampleLis = getResultInfoBatchSampleLis(oldResult.getJSONArray("batch_sample_lis") != null ? oldResult.getJSONArray("batch_sample_lis") : new JSONArray(), planDict.getJSONArray("batch_gather_lis"), key, newContactDurationMap, contactDurationLis);
            resultDict.put("batch_sample_lis", resultInfoBatchSampleLis.get(0));
            newContactDurationMap = resultInfoBatchSampleLis.getJSONObject(1);
            resultDictLis.add(resultDict);
        }
        JSONArray returnList = new JSONArray();
        returnList.add(resultDictLis);
        returnList.add(newContactDurationMap);
        return returnList;
    }

    /**
     * 照度
     */
    private JSONArray getIlmnResultTmpl(JSONObject planDict,JSONObject hazard,JSONObject pfnMap, JSONObject oldResultMap, Date nowTime){
        String key = "ilmn";
        JSONArray resultDictLis = new JSONArray();
        JSONObject newContactDurationMap = new JSONObject();
        for (String planCode : planDict.getJSONArray("batch_gather_lis").getJSONObject(0).getJSONObject("gather_map").keySet()){
            Map<String, Object> map = new HashMap<>();
            map.put("lis", new JSONArray());
            map.put("has_valid_value", 2);
            newContactDurationMap.put(planCode, map);
        }
        for (Object o : hazard.getJSONArray("pfn_ids")){
            String pfnIdStr = o.toString();
            JSONObject pfnObj = pfnMap.getJSONObject(pfnIdStr);
            Object laborIntensity = pfnObj.get("labor_intensity");
            JSONObject oldResult = oldResultMap.containsKey(pfnIdStr) ? oldResultMap.getJSONObject(pfnIdStr) : new JSONObject();
            JSONObject resultDict = generateResultInfoTmpl(planDict, pfnObj, nowTime);
            JSONArray contactDurationLis = getContactDurationLis(oldResult, resultDict.getInteger("sample_num"), resultDict.getString("touch_time"));
            Integer contactDurationLisLen = contactDurationLis.size();
            if (contactDurationLisLen < planDict.getInteger("sample_num")){
                for (int cdsI = 0; cdsI < planDict.getInteger("sample_num") - contactDurationLisLen; cdsI++){
                    contactDurationLis.add("");
                }
            }
            JSONObject substanceInfo = planDict.getJSONObject("substance").getJSONObject("substance_info");
            String limitV = substanceInfo.getString("mark");
            Integer accuracy = accuracyMath(limitV);
            JSONObject oldConclusion = oldResult.getJSONObject("conclusion") != null ? oldResult.getJSONObject("conclusion") : new JSONObject();
            JSONObject conclusion = new JSONObject();
            conclusion.put("accuracy", accuracy);
            conclusion.put("limit_v", limitV);
            conclusion.put("is_noise", 1);
            conclusion.put("contact_rate", 0);
            conclusion.put("labor_intensity", laborIntensity);
            conclusion.put("max_v", oldConclusion.get("max_v") != null ? oldConclusion.get("max_v") : "");
            conclusion.put("min_v", oldConclusion.get("min_v") != null ? oldConclusion.get("min_v") : "");
            conclusion.put("test_range", oldConclusion.get("test_range") != null ? oldConclusion.get("test_range") : "");
            conclusion.put("conclusion", oldConclusion.get("conclusion") != null ? oldConclusion.get("conclusion") : "");
            resultDict.put("conclusion", conclusion);
            resultDict.put("is_exist", oldResult.get("is_exist") != null ? oldResult.get("is_exist") : 1);
            resultDict.put("is_exceed", oldResult.get("is_exceed") != null ? oldResult.get("is_exceed") : 2);
            JSONArray resultInfoBatchSampleLis = getResultInfoBatchSampleLis(oldResult.getJSONArray("batch_sample_lis") != null ? oldResult.getJSONArray("batch_sample_lis") : new JSONArray(), planDict.getJSONArray("batch_gather_lis"), key, newContactDurationMap, contactDurationLis);
            resultDict.put("batch_sample_lis", resultInfoBatchSampleLis.get(0));
            newContactDurationMap = resultInfoBatchSampleLis.getJSONObject(1);
            resultDictLis.add(resultDict);
        }
        JSONArray returnList = new JSONArray();
        returnList.add(resultDictLis);
        returnList.add(newContactDurationMap);
        return returnList;
    }

    /**
     * 激光辐射 (限值不确定)
     */
    private JSONArray getLaserResultTmpl(JSONObject planDict,JSONObject hazard,JSONObject pfnMap, JSONObject oldResultMap, Date nowTime){
        String key = "laser";
        JSONArray resultDictLis = new JSONArray();
        JSONObject newContactDurationMap = new JSONObject();
        for (String planCode : planDict.getJSONArray("batch_gather_lis").getJSONObject(0).getJSONObject("gather_map").keySet()){
            Map<String, Object> map = new HashMap<>();
            map.put("lis", new JSONArray());
            map.put("has_valid_value", 2);
            newContactDurationMap.put(planCode, map);
        }
        for (Object o : hazard.getJSONArray("pfn_ids")){
            String pfnIdStr = o.toString();
            JSONObject pfnObj = pfnMap.getJSONObject(pfnIdStr);
            Object laborIntensity = pfnObj.get("labor_intensity");
            JSONObject oldResult = oldResultMap.containsKey(pfnIdStr) ? oldResultMap.getJSONObject(pfnIdStr) : new JSONObject();
            JSONObject resultDict = generateResultInfoTmpl(planDict, pfnObj, nowTime);
            JSONArray contactDurationLis = getContactDurationLis(oldResult, resultDict.getInteger("sample_num"), resultDict.getString("touch_time"));
            Integer contactDurationLisLen = contactDurationLis.size();
            if (contactDurationLisLen < planDict.getInteger("sample_num")){
                for (int cdsI = 0; cdsI < planDict.getInteger("sample_num") - contactDurationLisLen; cdsI++){
                    contactDurationLis.add("");
                }
            }
            JSONObject substanceInfo = planDict.getJSONObject("substance").getJSONObject("substance_info");
            String limitV = substanceInfo.getString("mark");
            Integer accuracy = accuracyMath(limitV);
            JSONObject oldConclusion = oldResult.getJSONObject("conclusion") != null ? oldResult.getJSONObject("conclusion") : new JSONObject();
            JSONObject conclusion = new JSONObject();
            conclusion.put("accuracy", accuracy);
            conclusion.put("limit_v", limitV);
            conclusion.put("is_noise", 1);
            conclusion.put("contact_rate", 0);
            conclusion.put("labor_intensity", laborIntensity);
            conclusion.put("max_v", oldConclusion.get("max_v") != null ? oldConclusion.get("max_v") : "");
            conclusion.put("min_v", oldConclusion.get("min_v") != null ? oldConclusion.get("min_v") : "");
            conclusion.put("test_range", oldConclusion.get("test_range") != null ? oldConclusion.get("test_range") : "");
            conclusion.put("conclusion", oldConclusion.get("conclusion") != null ? oldConclusion.get("conclusion") : "");
            resultDict.put("conclusion", conclusion);
            resultDict.put("is_exist", oldResult.get("is_exist") != null ? oldResult.get("is_exist") : 1);
            resultDict.put("is_exceed", oldResult.get("is_exceed") != null ? oldResult.get("is_exceed") : 2);
            JSONArray resultInfoBatchSampleLis = getResultInfoBatchSampleLis(oldResult.getJSONArray("batch_sample_lis") != null ? oldResult.getJSONArray("batch_sample_lis") : new JSONArray(), planDict.getJSONArray("batch_gather_lis"), key, newContactDurationMap, contactDurationLis);
            resultDict.put("batch_sample_lis", resultInfoBatchSampleLis.get(0));
            newContactDurationMap = resultInfoBatchSampleLis.getJSONObject(1);
            resultDictLis.add(resultDict);
        }
        JSONArray returnList = new JSONArray();
        returnList.add(resultDictLis);
        returnList.add(newContactDurationMap);
        return returnList;
    }

    private JSONArray dealPfnPoint(JSONObject pointObj, Integer maxPointCode){
        Integer pointCode;
        if (pointObj.get("code") != null){
            pointCode = pointObj.getInteger("code");
        }else {
            maxPointCode += 1;
            pointCode = maxPointCode;
        }
        String pointCodeStr = pointCode + "";
        int codeStrLength = pointCodeStr.length();
        if (codeStrLength < 3){
            StringBuilder zeroString = new StringBuilder();
            for (int a = 0 ;a < (3-codeStrLength); a++){
                zeroString.append("0");
            }
            pointCodeStr = zeroString + pointCodeStr;
        }
        JSONArray returnList = new JSONArray();
        returnList.add(pointCode);
        returnList.add(pointCodeStr);
        returnList.add(maxPointCode);
        return returnList;
    }

    /**
     * 处理合并采样的物质 并把合并后物质的名称处理出来
     */
    private JSONArray dealPfnHazards(JSONObject soloHazards, Map<Integer,JSONObject> soloSstMap){
        JSONObject soloSstMap1 = JSONObject.parseObject(JSON.toJSONString(soloSstMap));
        int hasMergeSub = 2;
        JSONObject mergeSubMap = new JSONObject();
        JSONArray subLis = new JSONArray();
        for (String subIdStr : soloHazards.keySet()){
            JSONObject subObj = soloHazards.getJSONObject(subIdStr);
            String totalMergeName = soloSstMap1.getJSONObject(subObj.getString("substance_id")).getString("total_merge_name");
            JSONObject hazardInfo = subObj;
            if (StringUtils.isNotBlank(totalMergeName)){
                hasMergeSub = 1;
                hazardInfo.put("merge_name", soloSstMap1.getJSONObject(subObj.getString("substance_id")).get("merge_name"));
                hazardInfo.put("merge_sort", soloSstMap1.getJSONObject(subObj.getString("substance_id")).get("merge_sort"));
                if (!mergeSubMap.containsKey(totalMergeName)){
                    JSONObject map = new JSONObject();
                    map.put("lis_index", subLis.size());
                    map.put("merge_name_lis", new JSONArray());
                    map.put("sub_ids", new JSONArray());
                    map.put("name_lis", new JSONArray());
                    mergeSubMap.put(totalMergeName, map);
                    JSONArray one = new JSONArray();
                    one.add(hazardInfo);
                    hazardInfo.put("merge_sub_lis", one);
                    subLis.add(hazardInfo);
                }else {
                    Integer index = mergeSubMap.getJSONObject(totalMergeName).getInteger("lis_index");
                    subLis.getJSONObject(index).getJSONArray("merge_sub_lis").add(hazardInfo);
                }
            }else {
                subLis.add(hazardInfo);
            }
        }
        for (String totalMergeName : mergeSubMap.keySet()){
            Integer index = mergeSubMap.getJSONObject(totalMergeName).getInteger("lis_index");
            // 创建一个比较器来排序 merge_sub_lis 列表中的元素
            Comparator<JSONObject> comparator = (obj1, obj2) -> {
                // 获取需要排序的属性值并进行比较
                int value1 = obj1.getInteger("merge_sort");
                int value2 = obj2.getInteger("merge_sort");
                return Integer.compare(value1, value2);
            };
            // 对 merge_sub_lis 列表进行排序
            JSONArray new_merge_sub_lis = subLis.getJSONObject(index).getJSONArray("merge_sub_lis");
            List<JSONObject> newMergeSubLis = new ArrayList<>();
            for (int i = 0; i < new_merge_sub_lis.size(); i++) {
                JSONObject obj = new_merge_sub_lis.getJSONObject(i);
                newMergeSubLis.add(obj);
            }
            newMergeSubLis.sort(comparator);
            JSONArray mergeNameLis = new JSONArray();
            JSONArray mergeSubIds = new JSONArray();
            JSONArray nameLis = new JSONArray();
            for (JSONObject subDict : newMergeSubLis){
                if (!mergeNameLis.contains(subDict.get("merge_name"))){
                    mergeNameLis.add(subDict.get("merge_name"));
                }
                mergeSubIds.add(subDict.get("substance_id"));
                nameLis.add(subDict.get("name"));
            }
            mergeSubMap.getJSONObject(totalMergeName).put("merge_name_lis", mergeNameLis);
            mergeSubMap.getJSONObject(totalMergeName).put("sub_ids", mergeSubIds);
            mergeSubMap.getJSONObject(totalMergeName).put("name_lis", nameLis);
            subLis.getJSONObject(index).put("merge_sub_lis", newMergeSubLis);
        }
        JSONArray returnList = new JSONArray();
        returnList.add(hasMergeSub);
        returnList.add(mergeSubMap);
        returnList.add(subLis);
        return returnList;
    }

    private JSONObject getPfnNewCodeSort(JSONObject oldCodeSortObj, Integer code, Integer sort, Integer maxSampleCode){
        JSONObject newCodeSort = oldCodeSortObj;
        newCodeSort.put("code", code);
        newCodeSort.put("sort", sort);
        newCodeSort.put("max_sample_code", maxSampleCode);
        return newCodeSort;
    }

    /**
     * 修复result为空
     */
    public void fillResult(Long projectId){
        Query query = new Query();
        query.addCriteria(Criteria.where("project_id").is(projectId));
        query.with(Sort.by(Sort.Direction.ASC, "_id"));
        List<JSONObject> planList = mongoTemplate.find(query, JSONObject.class, "zj_plan_record");
        for (JSONObject plan : planList){
            String id = plan.getString("_id");
            Query query1 = new Query().addCriteria(Criteria.where("_id").is(id));
            JSONObject gatherMap = plan.getJSONArray("batch_gather_lis").getJSONObject(0).getJSONObject("gather_map");
            JSONObject gatherKbMap = plan.getJSONArray("batch_gather_lis").getJSONObject(0).getJSONObject("gather_kb_map");
            for (String key : gatherMap.keySet()){
                JSONObject result = gatherMap.getJSONObject(key).getJSONObject("result");
                if (result.size() == 0){
                    ResultDto r = new ResultDto();
                    r.setLtDetection(2);
                    Update update = new Update();
                    String mapKey = "batch_gather_lis.0.gather_map." + key +".result";
                    update.set(mapKey, r);
                    mongoTemplate.updateFirst(query1, update, "zj_plan_record");
                }
            }

            for (String key : gatherKbMap.keySet()){
                JSONObject result = gatherKbMap.getJSONObject(key).getJSONObject("result");
                if (result.size() == 0){
                    ResultDto r = new ResultDto();
                    r.setLtDetection(2);
                    Update update = new Update();
                    String mapKey = "batch_gather_lis.0.gather_kb_map." + key +".result";
                    update.set(mapKey, r);
                    mongoTemplate.updateFirst(query1, update, "zj_plan_record");
                }
            }
        }
    }


    public void findChongFu(){
//        Query query = new Query();
//        query.addCriteria(Criteria.where("point_code").is("000"));
//        List<JSONObject> planList = mongoTemplate.find(query, JSONObject.class, "zj_plan_record");
//        Set<String> clean = new HashSet<>();
//        for (JSONObject plan : planList){
//            String projectId = plan.getString("project_id");
//            if (!clean.contains(projectId)){
//                clean.add(projectId);
//                System.err.println(projectId);
//            }
//        }

//        Query query1 = new Query();
//        Date startTime = DateUtil.parse("2023-08-31","yyyy-MM-dd");
//        query1.addCriteria(Criteria.where("create_time").gt(startTime));
//        List<JSONObject> plans = mongoTemplate.find(query1, JSONObject.class, "zj_plan_record");
//        Map<Integer, List<JSONObject>> map = plans.stream().collect(Collectors.groupingBy(jsonObject -> jsonObject.getInteger("project_id")));
//        for (Map.Entry<Integer, List<JSONObject>> entry : map.entrySet()) {
//            Integer key = entry.getKey();
//            List<JSONObject> value = entry.getValue();
//            Set<Integer> pointnums = new HashSet<>();
//            //根据pointid分组
//            Map<String, List<JSONObject>> map1 = value.stream().collect(Collectors.groupingBy(jsonObject -> jsonObject.getString("point_id")));
//            for (Map.Entry<String, List<JSONObject>> entry1 : map1.entrySet()){
//                String key1 = entry1.getKey();
//                List<JSONObject> value1 = entry1.getValue();
//                //一个点位下point_code_num都是一样的
//                Integer num = value1.get(0).getInteger("point_code_num");
//                if (!pointnums.contains(num)){
//                    pointnums.add(num);
//                }else {
//                    System.err.println(key);
//                    break;
//                }
//            }
//        }
        // 处理多余字段
        Query query = new Query();
//        query.addCriteria(Criteria.where("project_id").is(64877));
        query.addCriteria(Criteria.where("operation.ppe_working_lis").exists(true));
        List<JSONObject> planList = mongoTemplate.find(query, JSONObject.class, "zj_plan_record");
        for (JSONObject plan : planList){
            String id = plan.getString("_id");
            Query query1 = new Query().addCriteria(Criteria.where("_id").is(id));
            Update update = new Update();
            update.unset("operation.ppe_working_lis");
            update.unset("operation.epe_working_lis");
            update.unset("operation.equip_working_lis");
            update.unset("operation.operators_num");
            update.unset("batch_gather_lis.0.epe_working_lis");
            update.unset("batch_gather_lis.0.ppe_working_lis");
//            mongoTemplate.updateFirst(query1,update,"zj_plan_record");
        }
    }
}
