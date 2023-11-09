package may.yuntian.jianping.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import may.yuntian.anlian.entity.ProjectDateEntity;
import may.yuntian.anlian.service.ProjectDateService;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.jianping.dto.SignatureDto;
import may.yuntian.jianping.entity.ProjectUserEntity;
import may.yuntian.jianping.entity.SignatureEntity;
import may.yuntian.jianping.entity.SignatureSampleDateEntity;
import may.yuntian.jianping.mongodto.BatchGatherLis;
import may.yuntian.jianping.mongodto.ZjPlanRecord;
import may.yuntian.jianping.service.DateSignatureService;
import may.yuntian.jianping.service.ProjectUserService;
import may.yuntian.jianping.service.SignatureSampleDateService;
import may.yuntian.jianping.service.SignatureService;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author gy
 * @date 2023-07-19 14:12
 */
@Service("DateSignatureServiceImpl")
public class DateSignatureServiceImpl implements DateSignatureService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ProjectUserService projectUserService;

    @Autowired
    private ProjectDateService projectDateService;

    @Autowired
    private SignatureService signatureService;

    @Autowired
    private SignatureSampleDateService signatureSampleDateService;


    @Override
    public Result signatureSaveSp(JSONObject param) {
        int projectId = param.getInteger("project_id");
        int showType = param.getInteger("type");
        List<ProjectDateEntity> PD = projectDateService.list(new QueryWrapper<ProjectDateEntity>().eq("project_id", projectId));
        String surveyDate = null;
        if (PD.size() == 0) {
            return null;
        }
        for (ProjectDateEntity pd : PD) {
            surveyDate = DateUtil.format(pd.getSurveyDate(), "yyyy-MM-dd");
        }
        JSONArray dataLis = param.getJSONArray("data") != null ? param.getJSONArray("data") : new JSONArray();
        JSONArray attendant = new JSONArray();
        long attendantId;
        List<SignatureEntity> signatureInfo = signatureService.list(new QueryWrapper<SignatureEntity>().eq("project_id", projectId));
        if (signatureInfo.size() > 0) {
            for (SignatureEntity sig : signatureInfo) {
                if (sig.getPeopleType() == 1 && sig.getPath() != null && StringUtils.isNotBlank(sig.getUseList())) {
                    attendantId = sig.getId();
                    attendant.add(attendantId);
                }
            }
        }
        for (int num = 0; num < dataLis.size(); num++) {
            List<SignatureSampleDateEntity> addSigSampleDate = new ArrayList<>();
            JSONObject i = dataLis.getJSONObject(num);
            JSONArray sampler = i.getJSONArray("sampler");
            JSONArray samplerId = i.getJSONArray("sampler_id");
            JSONArray checker = i.getJSONArray("checker");
            JSONArray checkerId = i.getJSONArray("checker_id");
            List<SignatureSampleDateEntity> existSigInfo = signatureSampleDateService.list(new QueryWrapper<SignatureSampleDateEntity>()
                    .eq("project_id", projectId).eq("type_", showType).eq("gather_date", surveyDate));
            StringBuilder samplerString = new StringBuilder(), samplerIdString = new StringBuilder(), checkerString = new StringBuilder(), checkerIdString = new StringBuilder(), attendantString = new StringBuilder();
            fillString(sampler, samplerString);
            fillString(samplerId, samplerIdString);
            fillString(checker, checkerString);
            fillString(checkerId, checkerIdString);
            fillString(attendant, attendantString);
            SignatureSampleDateEntity sigSampleDate = new SignatureSampleDateEntity();
            sigSampleDate.setProjectId(projectId);
            sigSampleDate.setType_(showType);
            sigSampleDate.setGatherDate(surveyDate);
            sigSampleDate.setSampler(samplerString.toString());
            sigSampleDate.setSamplerId(samplerIdString.toString());
            sigSampleDate.setChecker(checkerString.toString());
            sigSampleDate.setCheckerId(checkerIdString.toString());
            sigSampleDate.setAttendant(attendantString.toString());
            if (existSigInfo.size() > 0) {
                sigSampleDate.setUpdateTime(new Date());
            } else {
                sigSampleDate.setCreateTime(new Date());
            }
            addSigSampleDate.add(sigSampleDate);
            try {
                if (existSigInfo.size() == 0) {
                    signatureSampleDateService.saveBatch(addSigSampleDate);
                } else {
                    for (SignatureSampleDateEntity entity : addSigSampleDate) {
                        signatureSampleDateService.update(entity, new QueryWrapper<SignatureSampleDateEntity>()
                                .eq("project_id", projectId).eq("type_", showType).eq("gather_date", surveyDate));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return Result.error("签名保存失败").put("data", null);
            }
        }
        return Result.ok("签名保存成功", null);
    }

    private void fillString(JSONArray array, StringBuilder samplerString) {
        if (array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                samplerString.append(array.getString(i));
                if (i < array.size() - 1) {
                    samplerString.append(",");
                }
            }
        }
    }


    @Override
    public Result signatureSave(JSONObject param) {
        int projectId = param.getInteger("project_id");
        int showType = param.getInteger("type");
        // 保存的数据有 采样人id 复核人id 采样日期 采样物质类型
        //1.空气有害物  2.噪声定点  3.噪声个体  4.高温  5.紫外辐射  67.co .co2  8.高频电磁场  9.工频电场   10.手传振动  11.照度  14.激光辐射  13.微波辐射  15.超高频辐射
        JSONArray dataLis = param.getJSONArray("data") != null ? param.getJSONArray("data") : new JSONArray();
        JSONArray attendant = new JSONArray();
        long attendantId;
        List<SignatureEntity> signatureInfo = signatureService.list(new QueryWrapper<SignatureEntity>().eq("project_id", projectId));
        if (signatureInfo.size() > 0) {
            for (SignatureEntity sig : signatureInfo) {
                if (sig.getPeopleType() == 1 && sig.getPath() != null && StringUtils.isNotBlank(sig.getUseList())) {
                    attendantId = sig.getId();
                    attendant.add(attendantId);
                }
            }
        }
        for (int num = 0; num < dataLis.size(); num++) {
            List<SignatureSampleDateEntity> addSigSampleDate = new ArrayList<>();
            JSONObject i = dataLis.getJSONObject(num);
            String sample_date = i.getString("gather_date");
            if (StringUtils.isBlank(sample_date)) {
                return Result.error( "采样日期不能为空");
            }
            JSONArray sampler = i.getJSONArray("sampler");
            JSONArray samplerId = i.getJSONArray("sampler_id");
            JSONArray checker = i.getJSONArray("checker");
            JSONArray checkerId = i.getJSONArray("checker_id");

            List<SignatureSampleDateEntity> existSigInfo = new ArrayList<>();
            List<SignatureSampleDateEntity> existSigInfo1 = new ArrayList<>();
            if (showType == 6 || showType == 7) {
                existSigInfo = signatureSampleDateService.list(new QueryWrapper<SignatureSampleDateEntity>()
                        .eq("project_id", projectId).eq("type_", 16).eq("gather_date", sample_date));
                StringBuilder samplerString = new StringBuilder(), samplerIdString = new StringBuilder(), checkerString = new StringBuilder(), checkerIdString = new StringBuilder(), attendantString = new StringBuilder();
                fillString(sampler, samplerString);
                fillString(samplerId, samplerIdString);
                fillString(checker, checkerString);
                fillString(checkerId, checkerIdString);
                fillString(attendant, attendantString);
                SignatureSampleDateEntity sigSampleDate = new SignatureSampleDateEntity();
                sigSampleDate.setProjectId(projectId);
                sigSampleDate.setType_(16);
                sigSampleDate.setGatherDate(sample_date);
                sigSampleDate.setSampler(samplerString.toString());
                sigSampleDate.setSamplerId(samplerIdString.toString());
                sigSampleDate.setChecker(checkerString.toString());
                sigSampleDate.setCheckerId(checkerIdString.toString());
                sigSampleDate.setAttendant(attendantString.toString());
                if (existSigInfo.size() > 0) {
                    sigSampleDate.setUpdateTime(new Date());
                } else {
                    sigSampleDate.setCreateTime(new Date());
                }
                addSigSampleDate.add(sigSampleDate);
            } else {
                existSigInfo1 = signatureSampleDateService.list(new QueryWrapper<SignatureSampleDateEntity>()
                        .eq("project_id", projectId).eq("type_", showType).eq("gather_date", sample_date));
                StringBuilder samplerString = new StringBuilder(), samplerIdString = new StringBuilder(), checkerString = new StringBuilder(), checkerIdString = new StringBuilder(), attendantString = new StringBuilder();
                fillString(sampler, samplerString);
                fillString(samplerId, samplerIdString);
                fillString(checker, checkerString);
                fillString(checkerId, checkerIdString);
                fillString(attendant, attendantString);
                SignatureSampleDateEntity sigSampleDate = new SignatureSampleDateEntity();
                sigSampleDate.setProjectId(projectId);
                sigSampleDate.setType_(showType);
                sigSampleDate.setGatherDate(sample_date);
                sigSampleDate.setSampler(samplerString.toString());
                sigSampleDate.setSamplerId(samplerIdString.toString());
                sigSampleDate.setChecker(checkerString.toString());
                sigSampleDate.setCheckerId(checkerIdString.toString());
                sigSampleDate.setAttendant(attendantString.toString());
                if (existSigInfo1.size() > 0) {
                    sigSampleDate.setUpdateTime(new Date());
                } else {
                    sigSampleDate.setCreateTime(new Date());
                }
                addSigSampleDate.add(sigSampleDate);
            }

            try {
                if (showType == 6 || showType == 7) {
                    if (existSigInfo.size() == 0) {
                        signatureSampleDateService.saveBatch(addSigSampleDate);
                    } else {
                        for (SignatureSampleDateEntity entity : addSigSampleDate) {
                            signatureSampleDateService.update(entity, new QueryWrapper<SignatureSampleDateEntity>()
                                    .eq("project_id", projectId).eq("type_", 16).eq("gather_date", sample_date));
                        }
                    }
                } else {
                    if (existSigInfo1.size() == 0) {
                        signatureSampleDateService.saveBatch(addSigSampleDate);
                    } else {
                        for (SignatureSampleDateEntity entity : addSigSampleDate) {
                            signatureSampleDateService.update(entity, new QueryWrapper<SignatureSampleDateEntity>()
                                    .eq("project_id", projectId).eq("type_", showType).eq("gather_date", sample_date));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return Result.error("签名保存失败").put("data", null);
            }
        }
        return Result.ok("签名保存成功", null);
    }


    /**
     * 获取签名下采样人和复核人等数据
     *
     * @param dto
     */
    @Override
    public Result signatureLis1(SignatureDto dto) {
        List<ZjPlanRecord> substanceRecords = new ArrayList<>();
        Query query = Query.query(Criteria.where("project_id").is(dto.getProject_id()));
        String sampleTableName = "";
        // 1.空气有害物  2.噪声定点  3.噪声个体  4.高温  5.紫外辐射  6.co  7.co2  8.高频电磁场
        // 9.工频电场  10.手传振动  11.照度   14.激光辐射  13.微波辐射  15.超高频辐射  20.采样计划
        switch (dto.getType()) {
            case 1:
                sampleTableName = "airFixed";
                break;
            case 2:
                sampleTableName = "noiseFixed";
                break;
            case 3:
                sampleTableName = "noiseIndividual";
                break;
            case 4:
                sampleTableName = "temperatureStable";
                break;
            case 5:
                sampleTableName = "ultraviolet";
                break;
            case 6:
                sampleTableName = "co";
                break;
            case 7:
                sampleTableName = "co2";
                break;
            case 8:
                sampleTableName = "electromagnetic";
                break;
            case 9:
                sampleTableName = "electric";
                break;
            case 10:
                sampleTableName = "vibrationHand";
                break;
            case 11:
                sampleTableName = "illumination";
                break;
            case 14:
                sampleTableName = "laserRadiation";
                break;
            case 13:
                sampleTableName = "microwave";
                break;
            case 15:
                sampleTableName = "uhfRadiation";
                break;
            default:
                break;
        }
        if (StringUtils.isNotBlank(sampleTableName)) {
            query.addCriteria(Criteria.where("substance.substance_info.sample_tablename").is(sampleTableName));
            substanceRecords = mongoTemplate.find(query, ZjPlanRecord.class);
        }
        Set<String> gatherLis = new HashSet<>();
        for (ZjPlanRecord entity : substanceRecords) {
            List<BatchGatherLis> batchGatherLis = entity.getBatch_gather_lis();
            for (BatchGatherLis gatherDto : batchGatherLis) {
                if (gatherDto.getGather_date() != null) {
                    gatherLis.add(gatherDto.getGather_date());
                } else {
                    return Result.error("采样日期为空，请设置采样日期");
                }
            }
        }
        List<Map<String, Object>> userInnerLis = new ArrayList<>();
        List<ProjectUserEntity> userInnerInfo = projectUserService.list(new QueryWrapper<ProjectUserEntity>()
                .eq("project_id", dto.getProject_id())
                .eq("types", 120)
                .orderByAsc("id"));
        for (ProjectUserEntity info : userInnerInfo) {
            Map<String, Object> map = new HashMap<>(2);
            map.put("user_id", info.getUserId());
            map.put("user_name", info.getUsername());
            userInnerLis.add(map);
        }

        QueryWrapper<SignatureSampleDateEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("project_id", dto.getProject_id());
        if (dto.getType() == 6 || dto.getType() == 7) {
            wrapper.eq("type_", 16);
        } else {
            wrapper.eq("type_", dto.getType());
        }
        List<SignatureSampleDateEntity> selectSigInfo = signatureSampleDateService.list(wrapper);
        List<Map<String, Object>> selectSigLis = new ArrayList<>();
        List<Object> nullList = new ArrayList<>();
        for (SignatureSampleDateEntity info : selectSigInfo) {
            Map<String, Object> map = new HashMap<>(5);
            map.put("gather_date", info.getGatherDate());
            if (StringUtils.isNotBlank(info.getSampler())) {
                map.put("sampler", new ArrayList<>(Arrays.asList(info.getSampler().split(","))));
            } else {
                map.put("sampler", nullList);
            }
            if (StringUtils.isNotBlank(info.getSamplerId())) {
                map.put("sampler_id", new ArrayList<>(Arrays.asList(info.getSamplerId().split(","))));
            } else {
                map.put("sampler_id", nullList);
            }
            if (StringUtils.isNotBlank(info.getChecker())) {
                map.put("checker", new ArrayList<>(Arrays.asList(info.getChecker().split(","))));
            } else {
                map.put("checker", nullList);
            }
            if (StringUtils.isNotBlank(info.getCheckerId())) {
                map.put("checker_id", new ArrayList<>(Arrays.asList(info.getCheckerId().split(","))));
            } else {
                map.put("checker_id", nullList);
            }
            selectSigLis.add(map);
        }
        Map<String, Object> returnMap = new HashMap<>(3);
        returnMap.put("gather_lis", gatherLis);
        returnMap.put("user_inner_lis", userInnerLis);
        returnMap.put("select_sig_lis", selectSigLis);
        return Result.ok("人员获取成功", returnMap);
    }
}
