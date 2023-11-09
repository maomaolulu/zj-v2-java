package may.yuntian.jianping.mongoservice;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mongodb.client.result.UpdateResult;
import may.yuntian.anlian.constant.IntellectConstants;
import may.yuntian.anlian.entity.ProjectDateEntity;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.entity.ProjectProceduresEntity;
import may.yuntian.anlian.service.CategoryService;
import may.yuntian.anlian.service.ProjectDateService;
import may.yuntian.anlian.service.ProjectProceduresService;
import may.yuntian.anlian.service.ProjectService;
import may.yuntian.anlian.utils.DateUtils;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.anlian.vo.CommissionTimeNodeVo;
import may.yuntian.jianping.dto.*;
import may.yuntian.jianping.entity.*;
import may.yuntian.jianping.mapper.MainDataMapper;
import may.yuntian.jianping.mapper.ProjectCountMapper;
import may.yuntian.jianping.mongodto.*;
import may.yuntian.jianping.mongoentity.PlanRecordEntity;
import may.yuntian.jianping.mongoentity.PostPfnEntity;
import may.yuntian.jianping.mongoentity.ResultEntity;
import may.yuntian.jianping.service.*;
import may.yuntian.jianping.vo.MathResultVo;
import may.yuntian.jianping.vo.SampleEquipVo;
import may.yuntian.jianping.vo.SubstanceSampleCodeVo;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.sys.utils.Result;
import may.yuntian.sys.utils.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SuppressWarnings("all")
@Service
public class PlanRecordService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private SubstanceService substanceService;
    @Autowired
    private SubstanceSampleService substanceSampleService;
    @Autowired
    private MainDataMapper mainDataMapper;
    @Autowired
    private ResultService resultService;
    @Autowired
    private PostPfnService postPfnService;
    @Autowired
    private ProjectDateService projectDateService;
    @Autowired
    private LabAcceptProjectService labAcceptProjectService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectProceduresService projectProceduresService;
    @Autowired
    private SubstanceDetectionService substanceDetectionService;
    @Autowired
    private ProjectCountMapper projectCountMapper;
    @Autowired
    private MessageService messageService;
    @Autowired
    private CategoryService categoryService;


    /**
     * 测试接口
     *
     * @param id
     * @return
     */
    public PlanRecordEntity getOneById(String id) {
        ObjectId o = new ObjectId(id);
        Query query = new Query(Criteria.where("_id").is(o));
        query.addCriteria(Criteria.where("project_id").is(36564));
        PlanRecordEntity planRecordEntity = mongoTemplate.findOne(query, PlanRecordEntity.class);
//        Update update = new Update();
//        update.set("batch_gather_lis.$[].batch_num",2);
//        mongoTemplate.updateFirst(query,update,PlanRecordEntity.class,"zj_plan_record");

        return planRecordEntity;
    }


    /**
     * 实验室结果填写页面--
     *
     * @param projectId
     * @return
     */
    public List<MathResultVo> getResultList(Long projectId) {
        Query query = new Query(Criteria.where("project_id").is(projectId)).addCriteria(Criteria.where("substance.substance_info.s_type").lte(2));
        List<PlanRecordEntity> lsit = mongoTemplate.find(query, PlanRecordEntity.class);
//        lsit.forEach(System.out::println);
        List<MathResultVo> mathResultVoList = new ArrayList<>();
        for (PlanRecordEntity planRecordEntity : lsit) {
            MathResultVo mathResultVo = new MathResultVo();
            List<SubstanceSampleCodeVo> codeList = new ArrayList<>();
            mathResultVo.setId(planRecordEntity.getId());
            mathResultVo.setProjectId(planRecordEntity.getProjectId());
            mathResultVo.setPointId(planRecordEntity.getPointId());
            mathResultVo.setTestPlace(planRecordEntity.getTestPlace());
            SubstanceDto substanceDto = planRecordEntity.getSubstance();
            PlaceDto placeDto = planRecordEntity.getPlace();
            mathResultVo.setPostId(placeDto.getPostId());
            mathResultVo.setSubstanceId(substanceDto.getId());
            mathResultVo.setSubstanceName(substanceDto.getSubstanceShow());
            BatchGatherDto batchGatherDto = planRecordEntity.getBatchGatherLis().get(0);
            List<String> sampleCodeLis = batchGatherDto.getSampleCodeLis();
            for (String code : sampleCodeLis) {
                SubstanceSampleCodeVo substanceSampleCodeVo = new SubstanceSampleCodeVo();
                GatherMapDto gatherMapDto = batchGatherDto.getGatherMap().get(code);
                ResultDto resultDto = gatherMapDto.getResult();
                substanceSampleCodeVo.setSampleCode(gatherMapDto.getSampleCode());
                String showCode = planRecordEntity.getIdentifier() + planRecordEntity.getPointCode() + "-" + gatherMapDto.getSampleCode();
                substanceSampleCodeVo.setShowCode(showCode);
                substanceSampleCodeVo.setLtDetection(resultDto.getLtDetection());
                substanceSampleCodeVo.setResult(resultDto.getResult());
                codeList.add(substanceSampleCodeVo);
            }
            mathResultVo.setSubstanceSampleCodeList(codeList);
            mathResultVoList.add(mathResultVo);
        }
        return mathResultVoList;
    }


    /**
     * 空气有害物质批量修改检测结果
     *
     * @param mathResultVoList
     */
    public void updateMathResult(List<MathResultVo> mathResultVoList) {
        for (MathResultVo mathResultVo : mathResultVoList) {
            if (mathResultVo.getSubstanceName().contains("一氧化碳") || mathResultVo.getSubstanceName().contains("二氧化碳")) {
                continue;
            } else {
                for (SubstanceSampleCodeVo substanceSampleCodeVo : mathResultVo.getSubstanceSampleCodeList()) {
//                    ObjectId objectId = new ObjectId(mathResultVo.getId());
                    Query endQuery = new Query(Criteria.where("_id").is(mathResultVo.getId())).addCriteria(Criteria.where("project_id").is(mathResultVo.getProjectId()));
//                    .and("project_id").is(mathResultVo.getProjectId())
                    Update update = new Update();
                    update.set("batch_gather_lis.$[].gather_map." + substanceSampleCodeVo.getSampleCode() + ".result.lt_detection", substanceSampleCodeVo.getLtDetection());
                    update.set("batch_gather_lis.$[].gather_map." + substanceSampleCodeVo.getSampleCode() + ".result.result", substanceSampleCodeVo.getResult());
                    update.set("update_time", new Date());
                    mongoTemplate.updateFirst(endQuery, update, PlanRecordEntity.class);
                }
            }
        }
    }


    /**
     * 游离二氧化硅批量修改结果
     *
     * @param mathResultVoList
     */
    public void updateSilica(List<MathResultVo> mathResultVoList) {
        Long projectId = mathResultVoList.get(0).getProjectId();
        ProjectEntity project = projectService.getById(projectId);
        for (MathResultVo mathResultVo : mathResultVoList) {
            SubstanceSampleCodeVo substanceSampleCodeVo = mathResultVo.getSubstanceSampleCodeList().get(0);
            Query query = new Query(Criteria.where("place.post_id").is(mathResultVo.getPostId()));
            List<PlanRecordEntity> planRecordEntities = mongoTemplate.find(query, PlanRecordEntity.class);
            for (PlanRecordEntity planRecordEntity : planRecordEntities) {
                SubstanceDto substanceDto = planRecordEntity.getSubstance();
                SubstanceInfoDto substanceInfoDto = substanceDto.getSubstanceInfo();
                String result = StringUtils.isNotBlank(substanceSampleCodeVo.getResult()) ? substanceSampleCodeVo.getResult() : "0";
                if (substanceInfoDto.getIsSilica() == 1) {
//                    Integer sampleMode = substanceInfoDto.getSampleMode();
                    Integer sampleMode = planRecordEntity.getIsFixed();
                    if (substanceDto.getName().contains("其他粉尘")) {
                        if (Double.valueOf(result) > 0 && Double.valueOf(result) < 10) {
                            this.mathIsSilica(Long.valueOf("624"), sampleMode, planRecordEntity.getId(), project.getCompanyOrder());
                        }
                    } else {
                        if (substanceDto.getName().contains("（总尘）")) {
                            if (Double.valueOf(result) >= 10 && Double.valueOf(result) <= 50) {
                                this.mathIsSilica(Long.valueOf("605"), sampleMode, planRecordEntity.getId(), project.getCompanyOrder());
                            } else if (Double.valueOf(result) > 50 && Double.valueOf(result) <= 80) {
                                this.mathIsSilica(Long.valueOf("607"), sampleMode, planRecordEntity.getId(), project.getCompanyOrder());
                            } else if (Double.valueOf(result) > 80) {
                                this.mathIsSilica(Long.valueOf("609"), sampleMode, planRecordEntity.getId(), project.getCompanyOrder());
                            }

                        } else if (substanceDto.getName().contains("（呼尘）")) {
                            if (Double.valueOf(result) >= 10 && Double.valueOf(result) <= 50) {
                                this.mathIsSilica(Long.valueOf("606"), sampleMode, planRecordEntity.getId(), project.getCompanyOrder());
                            } else if (Double.valueOf(result) > 50 && Double.valueOf(result) <= 80) {
                                this.mathIsSilica(Long.valueOf("608"), sampleMode, planRecordEntity.getId(), project.getCompanyOrder());
                            } else if (Double.valueOf(result) > 80) {
                                this.mathIsSilica(Long.valueOf("610"), sampleMode, planRecordEntity.getId(), project.getCompanyOrder());
                            }
                        }
                    }
                }
            }
            ObjectId oldId = new ObjectId(mathResultVo.getId());
            Query oldQuery = new Query(Criteria.where("_id").is(oldId).and("project_id").is(mathResultVo.getProjectId()));
            Update update = new Update();
            update.set("batch_gather_lis.$[].gather_map." + substanceSampleCodeVo.getSampleCode() + ".result.lt_detection", substanceSampleCodeVo.getLtDetection());
            update.set("batch_gather_lis.$[].gather_map." + substanceSampleCodeVo.getSampleCode() + ".result.result", substanceSampleCodeVo.getResult());
            update.set("update_time", new Date());
            mongoTemplate.updateFirst(oldQuery, update, PlanRecordEntity.class);

        }
        Map<String, List<MathResultVo>> map = mathResultVoList.stream().collect(Collectors.groupingBy(MathResultVo::getPostId));
        Query query = new Query(Criteria.where("is_fixed").is(2).and("project_id").is(mathResultVoList.get(0).getProjectId()));
        query.addCriteria(Criteria.where("substance.substance_info.s_type").is(2).and("substance.substance_info.is_silica").is(1));
        List<PlanRecordEntity> planRecordEntities = mongoTemplate.find(query, PlanRecordEntity.class);
        if (planRecordEntities.size() > 0) {
            for (PlanRecordEntity planRecordEntity : planRecordEntities) {
                PostPfnEntity postPfnEntity = postPfnService.getOneById(planRecordEntity.getPfnId());
                List<String> postIds = postPfnEntity.getPostIds();
                List<MathResultVo> mlist = new ArrayList<>();
//                System.out.println(postIds);
                for (String postId : postIds) {
                    if (map.get(postId) != null) {
                        List<MathResultVo> list = map.get(postId);
                        if (list != null && list.size() > 0) {
                            mlist.addAll(list);
                        }
                    }

                }
                if (mlist.size() > 0) {
                    for (MathResultVo m : mlist) {
                        if (StringUtils.isBlank(m.getSubstanceSampleCodeList().get(0).getResult())) {
                            m.getSubstanceSampleCodeList().get(0).setResult("0");
                        }
                    }

                    Double maxResult = Double.valueOf(mlist.get(0).getSubstanceSampleCodeList().get(0).getResult());
                    for (int i = 0; i < mlist.size(); i++) {
                        Double result = Double.valueOf(mlist.get(i).getSubstanceSampleCodeList().get(0).getResult());
                        if (result > maxResult) {
                            maxResult = result;
                        }
                    }
                    SubstanceDto substanceDto = planRecordEntity.getSubstance();
                    SubstanceInfoDto substanceInfoDto = substanceDto.getSubstanceInfo();
                    if (substanceInfoDto.getIsSilica() == 1) {
                        Integer sampleMode = substanceInfoDto.getSampleMode();
                        if (substanceDto.getName().contains("其他粉尘")) {
                            if (maxResult > 0 && maxResult < 10) {
                                this.mathIsSilica(Long.valueOf("624"), sampleMode, planRecordEntity.getId(), project.getCompanyOrder());
                            }
                        } else {
                            if (substanceDto.getName().contains("（总尘）")) {
                                if (maxResult >= 10 && maxResult <= 50) {
                                    this.mathIsSilica(Long.valueOf("605"), sampleMode, planRecordEntity.getId(), project.getCompanyOrder());
                                } else if (maxResult > 50 && maxResult <= 80) {
                                    this.mathIsSilica(Long.valueOf("607"), sampleMode, planRecordEntity.getId(), project.getCompanyOrder());
                                } else if (maxResult > 80) {
                                    this.mathIsSilica(Long.valueOf("609"), sampleMode, planRecordEntity.getId(), project.getCompanyOrder());
                                }

                            } else if (substanceDto.getName().contains("（呼尘）")) {
                                if (maxResult >= 10 && maxResult <= 50) {
                                    this.mathIsSilica(Long.valueOf("606"), sampleMode, planRecordEntity.getId(), project.getCompanyOrder());
                                } else if (maxResult > 50 && maxResult <= 80) {
                                    this.mathIsSilica(Long.valueOf("608"), sampleMode, planRecordEntity.getId(), project.getCompanyOrder());
                                } else if (maxResult > 80) {
                                    this.mathIsSilica(Long.valueOf("610"), sampleMode, planRecordEntity.getId(), project.getCompanyOrder());
                                }
                            }
                        }
                    }
                }

            }
        }

    }


    /**
     * 游里二氧化硅计算
     *
     * @param substanceId
     * @param sampleMode
     * @param objectId
     */
    public void mathIsSilica(Long substanceId, Integer sampleMode, String objectId, String companyOrder) {
        SubstanceEntity substanceEntity = substanceService.getById(substanceId);
//        SubstanceSampleEntity substanceSampleEntity = substanceSampleService.selectBySubstanceIdAndType(substanceId,sampleMode);
        SubstanceDetectionEntity substanceDetection = substanceDetectionService.getInfoBySubstanceIdAndCompanyOrder(substanceId, companyOrder);
        MainDataEntity mainDataEntity = mainDataMapper.selectById(substanceDetection.getMainDataId());
        ObjectId id = new ObjectId(objectId);
        Query updateQuery = new Query(Criteria.where("_id").is(id));
        Update update = new Update();
        SubstanceInfoDto finalSubstanceInfo = new SubstanceInfoDto();
        finalSubstanceInfo.setSubstanceId(substanceId);
        finalSubstanceInfo.setTotalDustId(substanceEntity.getTotalDustId());
        finalSubstanceInfo.setName(substanceEntity.getName());
        finalSubstanceInfo.setNameEn(substanceEntity.getNameEn());
        finalSubstanceInfo.setCasNo(substanceEntity.getCasNo());
        finalSubstanceInfo.setMac(substanceEntity.getMac());
        finalSubstanceInfo.setPcTwa(substanceEntity.getPcTwa());
        finalSubstanceInfo.setPcStel(substanceEntity.getPcStel());
        finalSubstanceInfo.setReaction(substanceEntity.getReaction());
        finalSubstanceInfo.setDeduction(substanceEntity.getDeduction());
        finalSubstanceInfo.setRemaks(substanceEntity.getRemaks());
        finalSubstanceInfo.setSType(substanceEntity.getSType());
        finalSubstanceInfo.setComputeMode(substanceEntity.getComputeMode());
        finalSubstanceInfo.setMark(substanceEntity.getMark());
        finalSubstanceInfo.setIsSilica(substanceEntity.getIsSilica());
        finalSubstanceInfo.setRemarksNote(substanceEntity.getRemarksNote());
        finalSubstanceInfo.setSDept(substanceEntity.getSDept());
        finalSubstanceInfo.setHighlyToxic(substanceEntity.getHighlyToxic());
        finalSubstanceInfo.setMergeName(substanceEntity.getMergeName());
        finalSubstanceInfo.setTotalMergeName(substanceEntity.getTotalMergeName());
        finalSubstanceInfo.setMergSort(substanceEntity.getMergeSort());
        finalSubstanceInfo.setIndicatorId(substanceEntity.getIndicatorId());
        finalSubstanceInfo.setSampleId(substanceDetection.getId());
//        finalSubstanceInfo.setSampleMode(substanceSampleEntity.getSampleMode());
        finalSubstanceInfo.setSampleTablename(substanceDetection.getSampleTablename());
        finalSubstanceInfo.setBasis(substanceDetection.getStandard());
        finalSubstanceInfo.setBasisName(substanceDetection.getStandardName());
//        finalSubstanceInfo.setEnableDate(substanceSampleEntity.getEnableDate());
//        finalSubstanceInfo.setInvalidDate(substanceSampleEntity.getInvalidDate());
        if (sampleMode == 2) {
            finalSubstanceInfo.setEquipment(substanceDetection.getIndEquipment());
            finalSubstanceInfo.setFlow(substanceDetection.getIndFlow());
            finalSubstanceInfo.setTestTime(substanceDetection.getIndTestTime());
            finalSubstanceInfo.setTestTimeNote(substanceDetection.getIndTestTimeNote());
        } else {
            finalSubstanceInfo.setEquipment(substanceDetection.getEquipment());
            finalSubstanceInfo.setFlow(substanceDetection.getFlow());
            finalSubstanceInfo.setTestTime(substanceDetection.getTestTime());
            finalSubstanceInfo.setTestTimeNote(substanceDetection.getTestTimeNote());
        }
        finalSubstanceInfo.setIndSample(substanceDetection.getIndSample());
        finalSubstanceInfo.setIndEquipment(substanceDetection.getIndEquipment());
        finalSubstanceInfo.setIndFlow(substanceDetection.getIndFlow());
        finalSubstanceInfo.setIndTestTime(substanceDetection.getIndTestTime());
        finalSubstanceInfo.setIndTestTimeNote(substanceDetection.getIndTestTimeNote());
        //ind_sample  ind_equipment  ind_flow  ind_test_time  ind_test_time_note
        finalSubstanceInfo.setCollector(substanceDetection.getCollector());
        finalSubstanceInfo.setPreserveTraffic(substanceDetection.getPreserveTraffic());
        finalSubstanceInfo.setPreserveRequire(substanceDetection.getPreserveRequire());
        finalSubstanceInfo.setShelfLife(substanceDetection.getShelfLife());

//        finalSubstanceInfo.setBlankSample(substanceSampleEntity.getBlankSample());
//        finalSubstanceInfo.setBlankNum(substanceSampleEntity.getBlankNum());
        finalSubstanceInfo.setAuthentication(substanceDetection.getQualification());
        finalSubstanceInfo.setBasisNum(substanceDetection.getDetectionMethodNum());
//        finalSubstanceInfo.setRemarks(substanceSampleEntity.getRemarks());
        finalSubstanceInfo.setIsDefault(1);
//        finalSubstanceInfo.setSampleNumber(substanceSampleEntity.getSampleNumber());
        finalSubstanceInfo.setMainDataId(substanceDetection.getMainDataId());
        finalSubstanceInfo.setSubstance(substanceEntity.getName());
        finalSubstanceInfo.setLimitedRange(mainDataEntity.getLimitedRange());
        finalSubstanceInfo.setDetectNum(mainDataEntity.getDetectNum());
        finalSubstanceInfo.setDetectName(mainDataEntity.getDetectName());
        finalSubstanceInfo.setIsDefaultSt(mainDataEntity.getIsDefault());

        update.set("substance.final_substance_info", finalSubstanceInfo);
        update.set("substance.final_id", substanceEntity.getId());
        update.set("substance.final_name", substanceEntity.getName());
        update.set("substance.final_alias", "");
        update.set("substance.final_sample_id", substanceDetection.getId());
        update.set("substance.final_main_data_id", substanceDetection.getMainDataId());
        update.set("substance.final_substance_show", substanceEntity.getName());
        update.set("update_time", new Date());
        mongoTemplate.updateFirst(updateQuery, update, PlanRecordEntity.class);
        ResultEntity resultEntity = resultService.getOneByRecordId(objectId);
        System.out.println(resultEntity);
        BatchSampleDto batchSampleDto = resultEntity.getBatchSampleLis().get(0);
        if (batchSampleDto.getConclusionKey().equals("chem")) {
            Query query = new Query(Criteria.where("record_id").is(objectId));
            Update resultUpdate = new Update();
            BigDecimal pef = new BigDecimal(String.valueOf(substanceEntity.getPcTwa())).multiply(new BigDecimal("3"));
            String pe = String.valueOf(pef);
            resultUpdate.set("substance.total_dust_id", substanceEntity.getTotalDustId());
            resultUpdate.set("substance.final_id", substanceEntity.getId());
            resultUpdate.set("substance.final_name", substanceEntity.getName());
            resultUpdate.set("substance.final_alias", "");
            resultUpdate.set("substance.final_sample_id", substanceDetection.getId());
            resultUpdate.set("substance.final_main_data_id", substanceDetection.getMainDataId());
            resultUpdate.set("substance.final_substance_show", substanceEntity.getName());
            resultUpdate.set("batch_sample_lis.$[].conclusion.chem.pc_twa", substanceEntity.getPcTwa());
            resultUpdate.set("batch_sample_lis.$[].conclusion.chem.pe", pe);
            mongoTemplate.updateFirst(query, resultUpdate, ResultEntity.class);
        }
    }


    /**
     * 获取该项目的所有采样计划记录信息
     *
     * @param projectId
     * @return
     */
    public List<SampleEquipVo> getListByProjectId(Long projectId) {
        List<SampleEquipVo> sampleEquipVoList = new ArrayList<>();
        Query query = new Query(Criteria.where("project_id").is(projectId));
        List<PlanRecordEntity> planRecordEntities = mongoTemplate.find(query, PlanRecordEntity.class);
        for (PlanRecordEntity planRecordEntity : planRecordEntities) {
            SampleEquipVo sampleEquipVo = new SampleEquipVo();
            sampleEquipVo.setId(planRecordEntity.getId());
            sampleEquipVo.setProjectId(planRecordEntity.getProjectId());
            if (planRecordEntity.getSubstance().getSubstanceInfo().getSType() > 2 || planRecordEntity.getSubstance().getSubstanceInfo().getName().equals("一氧化碳") || planRecordEntity.getSubstance().getSubstanceInfo().getName().equals("二氧化碳")) {
                sampleEquipVo.setSubstanceId(planRecordEntity.getSubstance().getSubstanceInfo().getSubstanceId());
                sampleEquipVo.setSubstanceName(planRecordEntity.getSubstance().getSubstanceInfo().getName());
                sampleEquipVo.setTestBasis(planRecordEntity.getSubstance().getSubstanceInfo().getDetectNum());
                sampleEquipVo.setBasisName(planRecordEntity.getSubstance().getSubstanceInfo().getDetectName());
                sampleEquipVo.setSType(planRecordEntity.getSubstance().getSubstanceInfo().getSType());
                sampleEquipVo.setTestMethod("");
                List<String> equipList = new ArrayList<>();
                for (BatchGatherDto batchGatherDto : planRecordEntity.getBatchGatherLis()) {
                    for (String key : batchGatherDto.getGatherMap().keySet()) {
                        GatherMapDto gatherMapDto = batchGatherDto.getGatherMap().get(key);
                        String equip = "";
                        if (StringUtils.isNotBlank(gatherMapDto.getSampleEquip().getNameModelId())) {
                            equip = gatherMapDto.getSampleEquip().getNameModelId();
                            if (StringUtils.isNotBlank(gatherMapDto.getSampleEquip().getInstrumentCode())) {
                                equip = equip + "(" + gatherMapDto.getSampleEquip().getInstrumentCode() + ")";
                            }
                        }
                        equipList.add(equip);
                    }
                }
                String equipment = equipList.stream().distinct().collect(Collectors.joining(","));
                sampleEquipVo.setEquip(equipment);
                sampleEquipVoList.add(sampleEquipVo);
            } else {
                sampleEquipVo.setSubstanceId(planRecordEntity.getSubstance().getSubstanceInfo().getSubstanceId());
                sampleEquipVo.setSubstanceName(planRecordEntity.getSubstance().getSubstanceInfo().getName());
                sampleEquipVo.setTestBasis(planRecordEntity.getSubstance().getSubstanceInfo().getDetectNum());
                sampleEquipVo.setBasisName(planRecordEntity.getSubstance().getSubstanceInfo().getDetectName());
                sampleEquipVo.setTestMethod(planRecordEntity.getSubstance().getSubstanceInfo().getLimitedRange());
                sampleEquipVo.setSType(planRecordEntity.getSubstance().getSubstanceInfo().getSType());
                sampleEquipVo.setEquip("");
                sampleEquipVoList.add(sampleEquipVo);
            }
        }

        return sampleEquipVoList;
    }

    /**
     * 根据ID修改检测仪器
     *
     * @param id
     */
    public void updateEquipment(String id, String equip) {
        Query query = new Query(Criteria.where("_id").is(id));
        PlanRecordEntity planRecordEntity = mongoTemplate.findOne(query, PlanRecordEntity.class);
        List<String> sampleCodeList = planRecordEntity.getSampleCodeLis();
        Update update = new Update();
        for (String code : sampleCodeList) {
            update.set("batch_gather_lis.$[].gather_map." + code + ".result.testing_instrument", equip);
        }
        mongoTemplate.updateFirst(query, update, PlanRecordEntity.class);
    }


    public List<PlanRecordEntity> getListByProtectionId(List<String> pfnIdList) {
        List<PlanRecordEntity> planRecordEntities = new ArrayList<>();
        for (String pfnId : pfnIdList) {
            Query query = new Query(Criteria.where("relation_pfn_ids").regex(".*?" + pfnId + ".*"));
            query.addCriteria(Criteria.where("substance.substance_info.sample_tablename").is("noiseFixed"));
            List<PlanRecordEntity> planRecordEntityList = mongoTemplate.find(query, PlanRecordEntity.class);
            planRecordEntities.addAll(planRecordEntityList);
        }
        return planRecordEntities;
    }


    public List<PlanRecordEntity> findAllList(Long projectId) {
        Query query = new Query(Criteria.where("project_id").is(projectId));
        List<PlanRecordEntity> list = mongoTemplate.find(query, PlanRecordEntity.class);
        return list;
    }


    /**
     * 提交物理因素验证数据
     *
     * @param projectId
     * @return
     */
    public Result physicalSend(CommissionTimeNodeVo commissionTimeNodeVo, HttpServletRequest httpRequest){
        Long projectId = commissionTimeNodeVo.getProjectId();
        List<PlanRecordEntity> list = this.findAllList(projectId);
        String gatherDate = "";
//        System.out.println(list.size());
        if (list.size()>0){
            //噪声定点
            List<PlanRecordEntity> noiseFixedList = list.stream().filter(i->i.getSubstance().getSubstanceInfo().getSampleTablename().equals("noiseFixed")).collect(Collectors.toList());
            if (noiseFixedList.size()>0){
                for (PlanRecordEntity planRecordEntity:noiseFixedList){
                    if (StringUtils.checkValNull(planRecordEntity.getTestPlace())){
                        return Result.error("噪声定点的测试地点为空,无法提交物理因素记录");
                    }
                    if (planRecordEntity.getBatchGatherLis().size()>0){
                        for (BatchGatherDto batchGatherDto:planRecordEntity.getBatchGatherLis()){
                            gatherDate = batchGatherDto.getGatherDate();
                            Map<String,GatherMapDto> gatherMap = batchGatherDto.getGatherMap();
                            for (String key:gatherMap.keySet()){
                                GatherMapDto gatherMapDto = gatherMap.get(key);
                                if (StringUtils.checkValNull(gatherMapDto.getSampleEquip().getInstrumentCode())){
                                    return Result.error("噪声定点的仪器编号为空,无法提交物理因素记录");
                                }
                                if (planRecordEntity.getIsFixed()==1 && (StringUtils.checkValNull(gatherMapDto.getResult().getResult1())||StringUtils.checkValNull(gatherMapDto.getResult().getResult2())||StringUtils.checkValNull(gatherMapDto.getResult().getResult3())||StringUtils.checkValNull(gatherMapDto.getResult().getResultAvg()))){
                                    return Result.error("噪声定点的测量结果为空,无法提交物理因素记录");
                                }
                                if (planRecordEntity.getIsFixed()!=1 && StringUtils.checkValNull(gatherMapDto.getResult().getResult())){
                                    return Result.error("噪声个人的测量平均结果为空,无法提交物理因素记录");
                                }
                                if (StringUtils.checkValNull(gatherMapDto.getResult().getResultTimeAvg())){
                                    return Result.error("噪声定点的LAeq,Te[dB(A)]为空,无法提交物理因素记录");
                                }
                            }
                        }
                    }else {
                        return Result.error("噪声定点的测量结果为空,无法提交物理因素记录");
                    }

                }
            }
            //噪声个体
            List<PlanRecordEntity> noiseIndividualList = list.stream().filter(i->i.getSubstance().getSubstanceInfo().getSampleTablename().equals("noiseIndividual")).collect(Collectors.toList());
            if (noiseIndividualList.size()>0){
                for (PlanRecordEntity planRecordEntity:noiseIndividualList){
                    if (StringUtils.checkValNull(planRecordEntity.getTestPlace())){
                        return Result.error("噪声个体的车间名称及岗位（工种）为空,无法提交物理因素记录");
                    }
                    if (planRecordEntity.getBatchGatherLis().size()>0){
                        for (BatchGatherDto batchGatherDto:planRecordEntity.getBatchGatherLis()){
                            gatherDate = batchGatherDto.getGatherDate();
                            Map<String,GatherMapDto> gatherMap = batchGatherDto.getGatherMap();
                            for (String key:gatherMap.keySet()){
                                GatherMapDto gatherMapDto = gatherMap.get(key);
                                if (StringUtils.checkValNull(gatherMapDto.getSampleEquip().getInstrumentCode())){
                                    return Result.error("噪声个体的仪器编号为空,无法提交物理因素记录");
                                }
                                if (StringUtils.checkValNull(gatherMapDto.getTestDuration())){
                                    return Result.error("噪声个体的持续时间为空,无法提交物理因素记录");
                                }
                                if (StringUtils.checkValNull(gatherMapDto.getResult().getResultTimeAvg())){
                                    return Result.error("噪声个体的LAeqdB(A)为空,无法提交物理因素记录");
                                }
                            }
                        }
                    }else {
                        return Result.error("噪声个体的测量结果为空,无法提交物理因素记录");
                    }
                }
            }
            //高温
            List<PlanRecordEntity> temperatureStableList = list.stream().filter(i->i.getSubstance().getSubstanceInfo().getSampleTablename().equals("temperatureStable")).collect(Collectors.toList());
            if (temperatureStableList.size()>0){
                for (PlanRecordEntity planRecordEntity:temperatureStableList){
                    if (StringUtils.checkValNull(planRecordEntity.getTestPlace())){
                        return Result.error("高温的测试地点为空,无法提交物理因素记录");
                    }
                    if (planRecordEntity.getBatchGatherLis().size()>0){
                        for (BatchGatherDto batchGatherDto:planRecordEntity.getBatchGatherLis()){
                            gatherDate = batchGatherDto.getGatherDate();
                            Map<String,GatherMapDto> gatherMap = batchGatherDto.getGatherMap();
                            for (String key:gatherMap.keySet()){
                                GatherMapDto gatherMapDto = gatherMap.get(key);
                                if (StringUtils.checkValNull(gatherMapDto.getSampleEquip().getInstrumentCode())){
                                    return Result.error("高温的仪器编号为空,无法提交物理因素记录");
                                }
                                if (StringUtils.checkValNull(gatherMapDto.getResult().getResult1())||StringUtils.checkValNull(gatherMapDto.getResult().getResult2())||StringUtils.checkValNull(gatherMapDto.getResult().getResult3())||StringUtils.checkValNull(gatherMapDto.getResult().getResultAvg())){
                                    return Result.error("高温的测量结果为空,无法提交物理因素记录");
                                }
                                if (StringUtils.checkValNull(gatherMapDto.getResult().getResultTimeAvg())){
                                    return Result.error("高温的WBGT加权值为空,无法提交物理因素记录");
                                }
                            }
                        }
                    }else {
                        return Result.error("高温的测量结果为空,无法提交物理因素记录");
                    }
                }
            }
            //手传震动
            List<PlanRecordEntity> vibrationHandList = list.stream().filter(i->i.getSubstance().getSubstanceInfo().getSampleTablename().equals("vibrationHand")).collect(Collectors.toList());
            if (vibrationHandList.size()>0){
                for (PlanRecordEntity planRecordEntity:vibrationHandList){
                    if (StringUtils.checkValNull(planRecordEntity.getTestPlace())){
                        return Result.error("手传震动的测试地点为空,无法提交物理因素记录");
                    }
                    if (planRecordEntity.getBatchGatherLis().size()>0){
                        for (BatchGatherDto batchGatherDto:planRecordEntity.getBatchGatherLis()){
                            gatherDate = batchGatherDto.getGatherDate();
                            Map<String,GatherMapDto> gatherMap = batchGatherDto.getGatherMap();
                            for (String key:gatherMap.keySet()){
                                GatherMapDto gatherMapDto = gatherMap.get(key);
                                if (StringUtils.checkValNull(gatherMapDto.getSampleEquip().getInstrumentCode())){
                                    return Result.error("手传振动的仪器编号为空,无法提交物理因素记录");
                                }
                                if (StringUtils.checkValNull(gatherMapDto.getResult().getResult1())||StringUtils.checkValNull(gatherMapDto.getResult().getResult2())||StringUtils.checkValNull(gatherMapDto.getResult().getResult3())){
                                    return Result.error("手传震动的计权振动加速度（axh，ayh，azh）为空,无法提交物理因素记录");
                                }
                                if (StringUtils.checkValNull(gatherMapDto.getResult().getResult())){
                                    return Result.error("手传震动的最大值为空,无法提交物理因素记录");
                                }
                            }
                        }
                    }else {
                        return Result.error("手传震动的测量结果为空,无法提交物理因素记录");
                    }
                }
            }
            //高频电磁场
            List<PlanRecordEntity> electromagneticList = list.stream().filter(i->i.getSubstance().getSubstanceInfo().getSampleTablename().equals("electromagnetic")).collect(Collectors.toList());
            if (electromagneticList.size()>0){
                for (PlanRecordEntity planRecordEntity:electromagneticList){
                    if (StringUtils.checkValNull(planRecordEntity.getTestPlace())){
                        return Result.error("高频电磁场的测试地点为空,无法提交物理因素记录");
                    }
                    if (planRecordEntity.getBatchGatherLis().size()>0){
                        for (BatchGatherDto batchGatherDto:planRecordEntity.getBatchGatherLis()){
                            gatherDate = batchGatherDto.getGatherDate();
                            Map<String,GatherMapDto> gatherMap = batchGatherDto.getGatherMap();
                            for (String key:gatherMap.keySet()){
                                GatherMapDto gatherMapDto = gatherMap.get(key);
                                if (StringUtils.checkValNull(gatherMapDto.getSampleEquip().getInstrumentCode())){
                                    return Result.error("高频电磁场的仪器编号为空,无法提交物理因素记录");
                                }
                                if (StringUtils.checkValNull(gatherMapDto.getResult().getResult1())||StringUtils.checkValNull(gatherMapDto.getResult().getResult2())||StringUtils.checkValNull(gatherMapDto.getResult().getResult3())||StringUtils.checkValNull(gatherMapDto.getResult().getResultAvg())){
                                    return Result.error("高频电磁场的电场强度（测试值1~3，平均值）为空,无法提交物理因素记录");
                                }
                                if (StringUtils.checkValNull(gatherMapDto.getResult().getFruit1())||StringUtils.checkValNull(gatherMapDto.getResult().getFruit2())||StringUtils.checkValNull(gatherMapDto.getResult().getFruit3())||StringUtils.checkValNull(gatherMapDto.getResult().getFruitAvg())){
                                    return Result.error("高频电磁场的磁场强度（测试值1~3，平均值）为空,无法提交物理因素记录");
                                }
                            }
                        }
                    }else {
                        return Result.error("高频电磁场的测量结果为空,无法提交物理因素记录");
                    }
                }
            }
            //紫外辐射
            List<PlanRecordEntity> ultravioletList = list.stream().filter(i->i.getSubstance().getSubstanceInfo().getSampleTablename().equals("ultraviolet")).collect(Collectors.toList());
            if (ultravioletList.size()>0){
                for (PlanRecordEntity planRecordEntity:ultravioletList){
                    if (StringUtils.checkValNull(planRecordEntity.getTestPlace())){
                        return Result.error("紫外辐射的测试地点为空,无法提交物理因素记录");
                    }
                    if (planRecordEntity.getBatchGatherLis().size()>0){
                        for (BatchGatherDto batchGatherDto:planRecordEntity.getBatchGatherLis()){
                            gatherDate = batchGatherDto.getGatherDate();
                            Map<String,GatherMapDto> gatherMap = batchGatherDto.getGatherMap();
                            for (String key:gatherMap.keySet()){
                                GatherMapDto gatherMapDto = gatherMap.get(key);
                                if (StringUtils.checkValNull(gatherMapDto.getSampleEquip().getInstrumentCode())){
                                    return Result.error("紫外辐射的仪器编号为空,无法提交物理因素记录");
                                }
                                if (StringUtils.checkValNull(gatherMapDto.getResult().getResult1())||StringUtils.checkValNull(gatherMapDto.getResult().getResult2())||StringUtils.checkValNull(gatherMapDto.getResult().getResult3())||StringUtils.checkValNull(gatherMapDto.getResult().getResultAvg())){
                                    return Result.error("紫外辐射的眼、面部罩内（长波、中波、短波、有效辐照度）为空,无法提交物理因素记录");
                                }
                                if (StringUtils.checkValNull(gatherMapDto.getResult().getFruit1())||StringUtils.checkValNull(gatherMapDto.getResult().getFruit2())||StringUtils.checkValNull(gatherMapDto.getResult().getFruit3())||StringUtils.checkValNull(gatherMapDto.getResult().getFruitAvg())){
                                    return Result.error("紫外辐射的眼、面部罩外（长波、中波、短波、有效辐照度）为空,无法提交物理因素记录");
                                }
                            }
                        }
                    }else {
                        return Result.error("紫外辐射的测量结果为空,无法提交物理因素记录");
                    }
                }
            }
            //照度
            List<PlanRecordEntity> illuminationList = list.stream().filter(i->i.getSubstance().getSubstanceInfo().getSampleTablename().equals("illumination")).collect(Collectors.toList());
            if (illuminationList.size()>0){
                for (PlanRecordEntity planRecordEntity:illuminationList){
                    if (StringUtils.checkValNull(planRecordEntity.getTestPlace())){
                        return Result.error("照度的测试地点为空,无法提交物理因素记录");
                    }
                    if (planRecordEntity.getBatchGatherLis().size()>0){
                        for (BatchGatherDto batchGatherDto:planRecordEntity.getBatchGatherLis()){
                            gatherDate = batchGatherDto.getGatherDate();
                            Map<String,GatherMapDto> gatherMap = batchGatherDto.getGatherMap();
                            for (String key:gatherMap.keySet()){
                                GatherMapDto gatherMapDto = gatherMap.get(key);
                                if (StringUtils.checkValNull(gatherMapDto.getSampleEquip().getInstrumentCode())){
                                    return Result.error("照度的仪器编号为空,无法提交物理因素记录");
                                }
                                if (StringUtils.checkValNull(gatherMapDto.getResult().getResult1())||StringUtils.checkValNull(gatherMapDto.getResult().getResult2())||StringUtils.checkValNull(gatherMapDto.getResult().getResult3())||StringUtils.checkValNull(gatherMapDto.getResult().getFruit1())||StringUtils.checkValNull(gatherMapDto.getResult().getFruit2())||StringUtils.checkValNull(gatherMapDto.getResult().getFruit3())){
                                    return Result.error("照度的照度（1~6个值）为空,无法提交物理因素记录");
                                }
                                if (StringUtils.checkValNull(gatherMapDto.getResult().getResultAvg())){
                                    return Result.error("照度的平均值为空,无法提交物理因素记录");
                                }
                            }
                        }
                    }else {
                        return Result.error("照度的测量结果为空,无法提交物理因素记录");
                    }
                }
            }
            //一氧化碳
            List<PlanRecordEntity> coList = list.stream().filter(i->i.getSubstance().getSubstanceInfo().getSampleTablename().equals("co")).collect(Collectors.toList());
            if (coList.size()>0){
                for (PlanRecordEntity planRecordEntity:coList){
                    if (StringUtils.checkValNull(planRecordEntity.getTestPlace())){
                        return Result.error("一氧化碳的测试地点为空,无法提交物理因素记录");
                    }
                    if (planRecordEntity.getBatchGatherLis().size()>0){
                        for (BatchGatherDto batchGatherDto:planRecordEntity.getBatchGatherLis()){
                            gatherDate = batchGatherDto.getGatherDate();
                            Map<String,GatherMapDto> gatherMap = batchGatherDto.getGatherMap();
                            for (String key:gatherMap.keySet()){
                                GatherMapDto gatherMapDto = gatherMap.get(key);
                                if (StringUtils.checkValNull(gatherMapDto.getSampleEquip().getInstrumentCode())){
                                    return Result.error("一氧化碳的仪器编号为空,无法提交物理因素记录");
                                }
                                if (StringUtils.checkValNull(gatherMapDto.getResult().getResult1())||StringUtils.checkValNull(gatherMapDto.getResult().getResult2())||StringUtils.checkValNull(gatherMapDto.getResult().getResult3())){
                                    return Result.error("一氧化碳的读数（1~3）为空,无法提交物理因素记录");
                                }
                                if (StringUtils.checkValNull(gatherMapDto.getResult().getResultAvg())){
                                    return Result.error("一氧化碳的平均值为空,无法提交物理因素记录");
                                }
                                if (StringUtils.checkValNull(gatherMapDto.getResult().getResult())){
                                    return Result.error("一氧化碳的结果浓度为空,无法提交物理因素记录");
                                }
                            }
                        }
                    }else {
                        return Result.error("一氧化碳的测量结果为空,无法提交物理因素记录");
                    }
                }
            }
            //二氧化碳
            List<PlanRecordEntity> co2List = list.stream().filter(i->i.getSubstance().getSubstanceInfo().getSampleTablename().equals("co2")).collect(Collectors.toList());
            if (co2List.size()>0){
                for (PlanRecordEntity planRecordEntity:co2List){
                    if (StringUtils.checkValNull(planRecordEntity.getTestPlace())){
                        return Result.error("二氧化碳的测试地点为空,无法提交物理因素记录");
                    }
                    if (planRecordEntity.getBatchGatherLis().size()>0){
                        for (BatchGatherDto batchGatherDto:planRecordEntity.getBatchGatherLis()){
                            gatherDate = batchGatherDto.getGatherDate();
                            Map<String,GatherMapDto> gatherMap = batchGatherDto.getGatherMap();
                            for (String key:gatherMap.keySet()){
                                GatherMapDto gatherMapDto = gatherMap.get(key);
                                if (StringUtils.checkValNull(gatherMapDto.getSampleEquip().getInstrumentCode())){
                                    return Result.error("二氧化碳的仪器编号为空,无法提交物理因素记录");
                                }
                                if (StringUtils.checkValNull(gatherMapDto.getResult().getResult1())||StringUtils.checkValNull(gatherMapDto.getResult().getResult2())||StringUtils.checkValNull(gatherMapDto.getResult().getResult3())){
                                    return Result.error("二氧化碳的读数（1~3）为空,无法提交物理因素记录");
                                }
                                if (StringUtils.checkValNull(gatherMapDto.getResult().getResultAvg())){
                                    return Result.error("二氧化碳的平均值为空,无法提交物理因素记录");
                                }
                                if (StringUtils.checkValNull(gatherMapDto.getResult().getResult())){
                                    return Result.error("二氧化碳的结果浓度为空,无法提交物理因素记录");
                                }
                            }
                        }
                    }else {
                        return Result.error("二氧化碳的测量结果为空,无法提交物理因素记录");
                    }
                }
            }
            //工频电场
            List<PlanRecordEntity> electricList = list.stream().filter(i->i.getSubstance().getSubstanceInfo().getSampleTablename().equals("electric")).collect(Collectors.toList());
            if (electricList.size()>0){
                for (PlanRecordEntity planRecordEntity:electricList){
                    if (StringUtils.checkValNull(planRecordEntity.getTestPlace())){
                        return Result.error("工频电场的测试地点为空,无法提交物理因素记录");
                    }
                    if (planRecordEntity.getBatchGatherLis().size()>0){
                        for (BatchGatherDto batchGatherDto:planRecordEntity.getBatchGatherLis()){
                            gatherDate = batchGatherDto.getGatherDate();
                            Map<String,GatherMapDto> gatherMap = batchGatherDto.getGatherMap();
                            for (String key:gatherMap.keySet()){
                                GatherMapDto gatherMapDto = gatherMap.get(key);
                                if (StringUtils.checkValNull(gatherMapDto.getSampleEquip().getInstrumentCode())){
                                    return Result.error("工频电场的仪器编号为空,无法提交物理因素记录");
                                }
                                if (StringUtils.checkValNull(gatherMapDto.getResult().getResult1())||StringUtils.checkValNull(gatherMapDto.getResult().getResult4())||StringUtils.checkValNull(gatherMapDto.getResult().getResult7())
                                        || StringUtils.checkValNull(gatherMapDto.getResult().getFruit1())||StringUtils.checkValNull(gatherMapDto.getResult().getFruit4())||StringUtils.checkValNull(gatherMapDto.getResult().getFruit7())||StringUtils.checkValNull(gatherMapDto.getResult().getFruitAvg1())){
                                    return Result.error("工频电场的头部测量值或修正值或修正平均值为空,无法提交物理因素记录");
                                }
                                if (StringUtils.checkValNull(gatherMapDto.getResult().getResult2())||StringUtils.checkValNull(gatherMapDto.getResult().getResult5())||StringUtils.checkValNull(gatherMapDto.getResult().getResult8())
                                        || StringUtils.checkValNull(gatherMapDto.getResult().getFruit2())||StringUtils.checkValNull(gatherMapDto.getResult().getFruit5())||StringUtils.checkValNull(gatherMapDto.getResult().getFruit8())||StringUtils.checkValNull(gatherMapDto.getResult().getFruitAvg2())){
                                    return Result.error("工频电场的胸部测量值或修正值或修正平均值为空,无法提交物理因素记录");
                                }
                                if (StringUtils.checkValNull(gatherMapDto.getResult().getResult3())||StringUtils.checkValNull(gatherMapDto.getResult().getResult6())||StringUtils.checkValNull(gatherMapDto.getResult().getResult9())
                                        || StringUtils.checkValNull(gatherMapDto.getResult().getFruit3())||StringUtils.checkValNull(gatherMapDto.getResult().getFruit6())||StringUtils.checkValNull(gatherMapDto.getResult().getFruit9())||StringUtils.checkValNull(gatherMapDto.getResult().getFruitAvg3())){
                                    return Result.error("工频电场的腹部测量值或修正值或修正平均值为空,无法提交物理因素记录");
                                }
                            }
                        }
                    }else {
                        return Result.error("工频电场的测量结果为空,无法提交物理因素记录");
                    }
                }
            }
        }else {
            return Result.error("无数据无法提交物理因素");
        }
        ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
        projectDateEntity.setPhysicalSendDate(commissionTimeNodeVo.getPhysicalSendDate());
        projectDateEntity.setPhysicalAcceptDate(commissionTimeNodeVo.getPhysicalSendDate());
        ProjectEntity project = projectService.getById(projectId);
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String sendDateStr = simpleDateFormat.format(commissionTimeNodeVo.getPhysicalSendDate());
        Date gatherDate2 = null;
        try {
            gatherDate2 = simpleDateFormat.parse(gatherDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (project!=null){
            List<LabAcceptProjectEntity> labAcceptProjectList = labAcceptProjectService.getListByProjectId(projectId);
            if (StringUtils.isNotEmpty(labAcceptProjectList)) {
                if (project.getIsTime()==1){
//                LabAcceptProjectEntity labAcceptProjectEntity = labAcceptProjectService.getOneByProjectId(projectId);
                    for(LabAcceptProjectEntity labAcceptProjectEntity:labAcceptProjectList){
                        if (labAcceptProjectEntity.getProjectType()==1){
                            labAcceptProjectEntity.setIsExistPhysics(1);
                            labAcceptProjectService.updateById(labAcceptProjectEntity);
                        }
                    }
                }else if (project.getIsTime()==3){
                    List<Integer> types = labAcceptProjectList.stream().map(LabAcceptProjectEntity::getProjectType).collect(Collectors.toList());
                    if (!types.contains(3)){
                        LabAcceptProjectEntity labAcceptProject = new LabAcceptProjectEntity();
                        SysUserEntity userEntity = ShiroUtils.getUserEntity();
                        labAcceptProject = new LabAcceptProjectEntity();
                        labAcceptProject.setProjectId(projectId);
                        labAcceptProject.setProjectType(3);
                        labAcceptProject.setSampleDeliveryBatch(project.getIdentifier()+"-1-"+sendDateStr);
                        labAcceptProject.setGatherDate(gatherDate2);
                        labAcceptProject.setSampleDeliveryTime(commissionTimeNodeVo.getPhysicalSendDate());
                        labAcceptProject.setSampleCollectionTime(commissionTimeNodeVo.getPhysicalSendDate());
                        labAcceptProject.setSampleDeliveryStatus(4);
                        labAcceptProject.setCreateTime(new Date());
                        labAcceptProject.setCreateBy(userEntity.getUsername());
                        labAcceptProject.setDataBelong(project.getIdentifier().contains("JX")?"嘉兴安联":(project.getIdentifier().contains("NB")?"宁波安联":"杭州安联"));
                        labAcceptProject.setSampleDataSource(1);
                        labAcceptProject.setIsExistPhysics(1);
                        labAcceptProjectService.save(labAcceptProject);
                    }
                }
                if (project != null && project.getStatus() < 20) {
                    project.setStatus(20);
                    projectService.updateById(project);
                    if ("集团发展".equals(project.getBusinessSource())){
                        if (null!=project.getSalesmenid()){
                            AbuSendNoteDTO abuSendNoteDTO = new AbuSendNoteDTO();
                            abuSendNoteDTO.setProjectId(project.getId());
                            abuSendNoteDTO.setIdentifier(project.getIdentifier());
                            abuSendNoteDTO.setCompany(project.getCompany());
                            abuSendNoteDTO.setEntrustCompany(project.getEntrustCompany());
                            abuSendNoteDTO.setSalesmanId(project.getSalesmenid());
                            abuSendNoteDTO.setSalesman(project.getSalesmen());
                            abuSendNoteDTO.setStatus(project.getStatus());
                            Map<String,Object> msgMap = BeanUtil.beanToMap(abuSendNoteDTO);
                            cn.hutool.json.JSONObject josmmap = JSONUtil.parseObj(msgMap);
                            try {
                                String object = HttpRequest.post(IntellectConstants.SEND_MESSAGE_URL)
                                        .header("Content-Type", "application/json")
                                        .header("token",httpRequest.getHeader("token"))
                                        .body(josmmap.toString())
                                        .execute().body();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                    ProjectProceduresEntity proceduresEntity = new ProjectProceduresEntity();
                    proceduresEntity.setProjectId(project.getId());
                    proceduresEntity.setStatus(project.getStatus());
                    projectProceduresService.save(proceduresEntity);
                }
            }else {
                LabAcceptProjectEntity labAcceptProject = new LabAcceptProjectEntity();
                SysUserEntity userEntity = ShiroUtils.getUserEntity();
                labAcceptProject = new LabAcceptProjectEntity();
                labAcceptProject.setProjectId(projectId);
                labAcceptProject.setProjectType(3);
                labAcceptProject.setSampleDeliveryBatch(project.getIdentifier()+"-1-"+sendDateStr);
                labAcceptProject.setGatherDate(gatherDate2);
                labAcceptProject.setSampleDeliveryTime(commissionTimeNodeVo.getPhysicalSendDate());
                labAcceptProject.setSampleCollectionTime(commissionTimeNodeVo.getPhysicalSendDate());
                labAcceptProject.setSampleDeliveryStatus(4);
                labAcceptProject.setCreateTime(new Date());
                labAcceptProject.setCreateBy(userEntity.getUsername());
                labAcceptProject.setDataBelong(project.getIdentifier().contains("JX")?"嘉兴安联":(project.getIdentifier().contains("NB")?"宁波安联":"杭州安联"));
                labAcceptProject.setSampleDataSource(1);
                labAcceptProject.setIsExistPhysics(1);
                labAcceptProjectService.save(labAcceptProject);
            }
        }
        projectDateService.updateById(projectDateEntity);

        //2023/08/04 采样人员送样成功 发送消息
        if ("检评".equals(project.getType()) || "职卫监督".equals(project.getType())){
            ProjectDateEntity date = projectDateService.getOneByProjetId(projectId);
            MessageEntity entity = new MessageEntity();
            entity.setTitle("送样提醒");
            String nowTime = DateUtil.format(new Date(),"yyyy-MM-dd HH:mm");
            String cyTime = date.getPhysicalSendDate() != null ? DateUtil.format(date.getPhysicalSendDate(), "yyyy-MM-dd") : DateUtil.format(new Date(), "yyyy-MM-dd");
            entity.setContent("【"+ project.getIdentifier() + " " + project.getCompany() +"】已送样（采样日期: "+ cyTime +"送样日期:"+ nowTime +"）");
            entity.setBusinessType(0);
            entity.setSenderType(0);
            SysUserEntity nowUser = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
            entity.setSenderId(nowUser.getUserId());
            entity.setSenderName(nowUser.getUsername());
            messageService.newMessage(entity, Arrays.asList(project.getChargeId()));
        }
        return Result.ok();
    }



    private List<Map<String, Object>> sortAndRemoveDuplicates(List<Map<String, Object>> timeList) {

        List<Map<String, Object>> newTimeList = new ArrayList<>(timeList);
        Collections.sort(newTimeList, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> x1, Map<String, Object> x2) {
                Comparable time1 = (Comparable) x1.get("time");
                Comparable time2 = (Comparable) x2.get("time");
                int result = time1.compareTo(time2);
                if (result == 0) {
                    Integer tNum1 = (Integer) x1.get("t_num");
                    Integer tNum2 = (Integer) x2.get("t_num");
                    return tNum1.compareTo(tNum2);
                }
                return result;
            }
        });
        return newTimeList;
    }




    /**
     * 设置统计信息
     *
     * @param project_id
     * @param tableColumn
     * @param dateTime
     * @return
     */
    private Boolean setProjectCount(Long project_id, String tableColumn, String dateTime) {
        List<ProjectCountEntity> countLis = projectCountMapper.selectList(new QueryWrapper<ProjectCountEntity>().eq(project_id != null, "project_id", project_id));
        if (countLis.size() == 0) {
            ProjectCountEntity addObj = new ProjectCountEntity();
            addObj.setId(null);
            addObj.setProjectId(project_id);
            projectCountMapper.insert(addObj);
        } else {
            ProjectCountEntity countInfo = countLis.get(0);
            if (tableColumn.equals("report_generate")) {
                Date date = DateUtils.parseDate(dateTime);
                if (countInfo.getReportGenerate() != null) {
                    countInfo.setReportGenerateLast(date);
                } else {
                    countInfo.setReportGenerate(date);
                }
                projectCountMapper.updateById(countInfo);
            }
            // 如果需要更新其他列，可以相应地添加代码
        }
        return true;
    }


    /**
     * 采样记录信息显示（pc端）
     *
     * @param recoedType
     * @param project_id
     * @param gatherDate
     * @return
     */
    public Result pageShowRecord1(Integer recoedType, Long project_id, String gatherDate) {
        Query query = new Query();

        if (gatherDate != null) {
            query.addCriteria(Criteria.where("project_id").is(project_id));
            query.addCriteria(Criteria.where("batch_gather_lis.gather_date").is(gatherDate));
        } else {
            query.addCriteria(Criteria.where("project_id").is(project_id));
        }

        if (recoedType == 1) {
            query.addCriteria(Criteria.where("substance.substance_info.sample_tablename").is("airFixed"));
        } else if (recoedType == 2) {
            query.addCriteria(Criteria.where("substance.substance_info.s_type").is(3));
            query.addCriteria(Criteria.where("is_fixed").is(1));
        } else if (recoedType == 3) {
            query.addCriteria(Criteria.where("substance.substance_info.s_type").is(3));
            query.addCriteria(Criteria.where("is_fixed").is(2));
        } else if (recoedType == 4) {
            query.addCriteria(Criteria.where("substance.substance_info.s_type").is(4));
        } else if (recoedType == 5) {
            query.addCriteria(Criteria.where("substance.substance_info.s_type").is(5));
        } else if (recoedType == 6) {
            query.addCriteria(Criteria.where("substance.substance_info.sample_tablename").is("co"));
        } else if (recoedType == 7) {
            query.addCriteria(Criteria.where("substance.substance_info.sample_tablename").is("co2"));
        } else if (recoedType == 8) {
            query.addCriteria(Criteria.where("substance.substance_info.s_type").is(8));
        } else if (recoedType == 9) {
            query.addCriteria(Criteria.where("substance.substance_info.s_type").is(7));
        } else if (recoedType == 10) {
            query.addCriteria(Criteria.where("substance.substance_info.s_type").is(6));
        } else if (recoedType == 11) {
            query.addCriteria(Criteria.where("substance.substance_info.s_type").is(12));
        } else if (recoedType == 12) {
            query.addCriteria(Criteria.where("substance.substance_info.sample_tablename").is("windSpeed"));
        } else if (recoedType == 13) {
            query.addCriteria(Criteria.where("substance.substance_info.sample_tablename").is("microwave"));
        } else if (recoedType == 14) {
            query.addCriteria(Criteria.where("substance.substance_info.sample_tablename").is("laserRadiation"));
        } else if (recoedType == 15) {
            query.addCriteria(Criteria.where("substance.substance_info.sample_tablename").is("uhfRadiation"));
        } else {
            return Result.error(501, "参数错误，信息展示类型错误");
        }

        query.with(Sort.by(Sort.Order.asc("batch_gather_lis.0.gather_date"))); //查询结果按照batch字段升序
        query.with(Sort.by(Sort.Order.asc("place.workshop_id")));
        query.with(Sort.by(Sort.Order.asc("place.area_id")));
        query.with(Sort.by(Sort.Order.asc("place.post_id")));
        query.with(Sort.by(Sort.Order.asc("point_id")));
        query.with(Sort.by(Sort.Order.asc("_id")));
        List<ZjPlanRecord> recordList = mongoTemplate.find(query, ZjPlanRecord.class);
        if (recordList.isEmpty()) {
            return Result.ok("采样信息获取成功", new ArrayList<>());
        }

        List<List<ZjPlanRecord>> roundRecordList = new ArrayList<>();
        Map<String, List<ZjPlanRecord>> placePostPoint = new HashMap<>();
        int idLabel = 0;

        for (ZjPlanRecord itemInfo : recordList) {
            String rid = itemInfo.get_id();
            Integer is_fixed = itemInfo.getIs_fixed();
            BatchGatherLis batchGatherLis = itemInfo.getBatch_gather_lis().get(0);

            if (is_fixed == 1) {
                Integer multi_substance = itemInfo.getMulti_substance();
                if (multi_substance == 1) {
                    String testPlace = itemInfo.getTest_place();
                    String multiSubType = itemInfo.getMulti_sub_type();
                    String signSub = testPlace + multiSubType;
                    placePostPoint.computeIfAbsent(signSub, k -> new ArrayList<>()).add(itemInfo);
                } else {
                    List<ZjPlanRecord> list = new ArrayList<>();
                    list.add(itemInfo);
                    roundRecordList.add(list);
                }
            } else {
                Integer multi_substance = itemInfo.getMulti_substance();
                if (multi_substance == 1) {
                    String testPlace = itemInfo.getTest_place();
                    String multiSubType = itemInfo.getMulti_sub_type();
                    String signSub = testPlace + multiSubType;
                    placePostPoint.computeIfAbsent(signSub, k -> new ArrayList<>()).add(itemInfo);
                } else {
                    List<ZjPlanRecord> list = new ArrayList<>();
                    list.add(itemInfo);
                    roundRecordList.add(list);
                }
            }

            itemInfo.setShow_type(recoedType);
            itemInfo.set_id(rid);
        }

        placePostPoint.values().forEach(roundRecordList::add);

        List<List<ZjPlanRecord>> sortRecordList = new ArrayList<>(roundRecordList);
        sortRecordList.sort((a, b) -> {
            ZjPlanRecord itemA = a.get(0);
            ZjPlanRecord itemB = b.get(0);
            String workshopIdA = itemA.getPlace().getWorkshop_id();
            String workshopIdB = itemB.getPlace().getWorkshop_id();
            String postIdA = itemA.getPlace().getPost_id();
            String postIdB = itemB.getPlace().getPost_id();
            String pointIdA = itemA.getPoint_id();
            String pointIdB = itemB.getPoint_id();

            if (!workshopIdA.equals(workshopIdB)) {
                return workshopIdA.compareTo(workshopIdB);
            } else if (!postIdA.equals(postIdB)) {
                return postIdA.compareTo(postIdB);
            } else {
                return pointIdA.compareTo(pointIdB);
            }
        });
        return Result.ok("采样信息获取成功", sortRecordList);
    }


    /**
     * 采样记录信息内层保存(pc端)  # 保存 作业内容 ppe  epe 等
     *
     * @param params
     * @return
     */
    public Result pageSetInnerRecord1(List<List<ZjPlanRecord>> params) {
        for (ZjPlanRecord i : params.get(0)) {
            Long project_id = i.getProject_id();
            Integer show_type = i.getShow_type();
            if (!(show_type >= 1 && show_type <= 15)) {
                return Result.error(501, "参数错误，信息展示类型错误");
            }
            String r_id = i.get_id();
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(r_id));
            List<ZjPlanRecord> recordLis = mongoTemplate.find(query, ZjPlanRecord.class);
            if (recordLis.size() == 0) {
                return Result.error(501, "采样信息不存在");

            }
            String formattedDateTime = DateUtils.dateTimeNow("yyyy-MM-dd HH:mm:ss");

            // 记录需要统计的日期---开始----------------------
            setProjectCount(project_id, "set_record_one", formattedDateTime);
            // 记录需要统计的日期---结束----------------------
            List<MongoUpdateDto> fltUpdateLis = new ArrayList<>();
            List<Object> instrumentCodeList = new ArrayList<>();  // 仪器编号列表
            ZjPlanRecord kVDic = new ZjPlanRecord();
            BeanUtils.copyProperties(i, kVDic);

            Update reduction = new Update();
            Update update = new Update();
            List<Map<String, Object>> sampleInfo = new ArrayList<>();
            List<String> change2KeyLis = Arrays.asList("operators_num", "working_equip", "epe_working", "ppe_working",
                    "equip_working_lis", "epe_working_lis", "ppe_working_lis");
            List<String> change1KeyLis = Arrays.asList("work_content", "people_num", "equip_name", "equip_lis");
            List<String> change4KeyLis = Arrays.asList("people");
            List<String> change5KeyLis = Arrays.asList("name", "total_num", "run_num");
            ZjPlanRecord itemInfo = recordLis.get(0);
            if (show_type == 1) {
                int batchNum = 0;
                int gatherNum = 0;
                List<String> change3KeyLis = Arrays.asList("begin_time", "end_time", "time_frame");
                Operation operationMap = itemInfo.getOperation();
                String workContent = operationMap.getWork_content();
                Integer peopleNum = operationMap.getPeople_num();
                String equipName = operationMap.getEquip_name();
                List<Map<String, String>> equipLis = operationMap.getEquip_lis();
//                List<EquipLis> newEquipLis = new ArrayList<>();
//                if (CollectionUtil.isNotEmpty(equipLis)) {
//                    for (EquipLis itemEqp : equipLis) {
//                        EquipLis newEquip = new EquipLis();
//                        newEquip.setName(itemEqp.getName());
//                        newEquip.setTotal_num(itemEqp.getTotal_num());
//                        newEquip.setRun_num("3");
//                        newEquipLis.add(newEquip);
//                    }
//                }
                if (itemInfo.getIs_fixed() == 1) {
                    List<BatchGatherLis> batchGatherLis = itemInfo.getBatch_gather_lis();
                    for (BatchGatherLis batchIndex : batchGatherLis) {
                        //对非空白样和空白样列表合并采样不同批次信息分类
                        List<String> sampleCodeList = (List<String>) batchIndex.getSample_code_lis();
                        int sampleKbNum = batchIndex.getSample_kb_code_lis().size();
                        String gatherDate = batchIndex.getGather_date();
                        int operatorsNum = batchIndex.getOperators_num();
                        Integer workingEquip = batchIndex.getWorking_equip();
                        String epeWorking = batchIndex.getEpe_working();
                        String ppeWorking = batchIndex.getPpe_working();
                        List<Map<String, String>> equipWorkingLis = batchIndex.getEquip_working_lis();
                        List<Map<String, String>> epeWorkingLis = batchIndex.getEpe_working_lis();
                        List<Map<String, String>> ppeWorkingLis = batchIndex.getPpe_working_lis();
                        Scene scene = batchIndex.getScene();
                        String temp = scene.getTemp();
                        String pressure = scene.getPressure();
                        for (String gatherInfo : sampleCodeList) {
                            List<String> sampleKbCodeLis = itemInfo.getSample_kb_code_lis();
                            if (sampleKbCodeLis.contains(gatherInfo)) {
                                Map<String, GatherLis> gather_kb_map = batchIndex.getGather_kb_map();
                                GatherLis gatherKbMap = gather_kb_map.get(gatherInfo);
                                String beforeFlow = gatherKbMap.getBefore_flow();
                                String afterFlow = gatherKbMap.getAfter_flow();
                                int testDuration = gatherKbMap.getTest_duration();
                                String beginTime = gatherKbMap.getBegin_time();
                                String endTime = gatherKbMap.getEnd_time();
                                String timeFrame = gatherKbMap.getTime_frame();

                                SampleEquip sampleEquip = gatherKbMap.getSample_equip();
                                String nameModelId = sampleEquip.getName_model_id();
                                String instrumentCode = sampleEquip.getInstrument_code();
                                if (!instrumentCodeList.contains(instrumentCode)) {
                                    instrumentCodeList.add(instrumentCode);
                                }
                                String calibrationInfo = sampleEquip.getCalibration_info();

                                Map<String, Object> sampleInfoItem = new HashMap<>();
                                sampleInfoItem.put("test_duration", testDuration);
                                sampleInfoItem.put("begin_time", beginTime);
                                sampleInfoItem.put("end_time", endTime);
                                sampleInfoItem.put("time_frame", timeFrame);
                                sampleInfoItem.put("before_flow", beforeFlow);
                                sampleInfoItem.put("after_flow", afterFlow);
                                sampleInfoItem.put("name_model_id", nameModelId);
                                sampleInfoItem.put("instrument_code", instrumentCode);
                                sampleInfoItem.put("calibration_info", calibrationInfo);
                                sampleInfoItem.put("operators_num", operatorsNum);
                                sampleInfoItem.put("working_equip", workingEquip);
                                sampleInfoItem.put("equip_lis", equipLis);
                                sampleInfoItem.put("epe_working", epeWorking);
                                sampleInfoItem.put("ppe_working", ppeWorking);
                                sampleInfoItem.put("equip_working_lis", equipWorkingLis);
                                sampleInfoItem.put("epe_working_lis", epeWorkingLis);
                                sampleInfoItem.put("ppe_working_lis", ppeWorkingLis);
                                sampleInfoItem.put("gather_date", gatherDate);
                                sampleInfoItem.put("work_content", workContent);
                                sampleInfoItem.put("people_num", peopleNum);
                                sampleInfoItem.put("equip_name", equipName);
                                sampleInfoItem.put("temp", temp);
                                sampleInfoItem.put("pressure", pressure);

                                sampleInfo.add(sampleInfoItem);

                            } else {
                                Map<String, GatherLis> gatherMap = batchIndex.getGather_map();
                                GatherLis gatherMapGatherInfo = gatherMap.get(gatherInfo);
                                SampleEquip sampleEquip = gatherMapGatherInfo.getSample_equip();
                                String instrumentCode = sampleEquip.getInstrument_code();  // 仪器编号
                                if (!instrumentCodeList.contains(instrumentCode)) {
                                    instrumentCodeList.add(instrumentCode);
                                }
                                String calibrationInfo = sampleEquip.getCalibration_info();  // 校准仪器名称 / 编号 - -声校准器型号 / 编号
                                String nameModelId = sampleEquip.getName_model_id();  // 仪器名称
                                String beforeFlow = gatherMapGatherInfo.getBefore_flow();  // 采样前流量
                                String afterFlow = gatherMapGatherInfo.getAfter_flow();  // 采样后流量
                                int testDuration = gatherMapGatherInfo.getTest_duration();  // 采样时长/测量时间
                                String beginTime = gatherMapGatherInfo.getBegin_time();  // 采样时长/测量时间
                                String endTime = gatherMapGatherInfo.getEnd_time();  // 采样时长/测量时间
                                String timeFrame = gatherMapGatherInfo.getTime_frame();  // 采样时长/测量时间

                                Map<String, Object> sampleInfoItem = new HashMap<>();
                                sampleInfoItem.put("test_duration", testDuration);
                                sampleInfoItem.put("before_flow", beforeFlow);
                                sampleInfoItem.put("after_flow", afterFlow);
                                sampleInfoItem.put("begin_time", beginTime);
                                sampleInfoItem.put("end_time", endTime);
                                sampleInfoItem.put("time_frame", timeFrame);
                                sampleInfoItem.put("name_model_id", nameModelId);
                                sampleInfoItem.put("instrument_code", instrumentCode);
                                sampleInfoItem.put("calibration_info", calibrationInfo);
                                sampleInfoItem.put("operators_num", operatorsNum);
                                sampleInfoItem.put("working_equip", workingEquip);
                                sampleInfoItem.put("epe_working", epeWorking);
                                sampleInfoItem.put("ppe_working", ppeWorking);
                                sampleInfoItem.put("equip_lis", equipLis);
                                sampleInfoItem.put("equip_working_lis", equipWorkingLis);
                                sampleInfoItem.put("epe_working_lis", epeWorkingLis);
                                sampleInfoItem.put("ppe_working_lis", ppeWorkingLis);
                                sampleInfoItem.put("gather_date", gatherDate);
                                sampleInfoItem.put("work_content", workContent);
                                sampleInfoItem.put("people_num", peopleNum);
                                sampleInfoItem.put("equip_name", equipName);
                                sampleInfoItem.put("temp", temp);
                                sampleInfoItem.put("pressure", pressure);

                                sampleInfo.add(sampleInfoItem);
                            }
                        }
                        Map<String, GatherLis> gatherMap = batchIndex.getGather_map();
                        for (Map.Entry<String, GatherLis> entry : gatherMap.entrySet()) {
                            String gatherIndex = entry.getKey();
                            Map<String, Object> gatherNum1 = sampleInfo.get(gatherNum);
                            List<BatchGatherLis> batch_gather_lis = kVDic.getBatch_gather_lis();
                            Operation operation = kVDic.getOperation();
                            Map<String, Object> operationMap1 = new HashMap<>();
                            Map<String, Object> batch_gatherMap1 = new HashMap<>();
                            Map<String, Object> gatherLisMap1 = new HashMap<>();
                            BatchGatherLis batch_gather = batch_gather_lis.get(batchNum);
                            Map<String, GatherLis> gather_map = batch_gather.getGather_map();
                            GatherLis gatherLis = gather_map.get(gatherIndex);
                            try {
                                operationMap1 = this.convert(operation);
                                batch_gatherMap1 = this.convert(batch_gather);
                                gatherLisMap1 = this.convert(gatherLis);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            for (String change1Key : change1KeyLis) {
                                String updateKey = "operation." + change1Key;
                                update.set(updateKey, operationMap1.get(change1Key));
                                reduction.set(updateKey, gatherNum1.get(change1Key));
                            }
                            for (String change2Key : change2KeyLis) {
                                String updateKey = "batch_gather_lis." + batchNum + "." + change2Key;
                                update.set(updateKey, batch_gatherMap1.get(change2Key));
                                reduction.set(updateKey, gatherNum1.get(change2Key));
                            }
                            for (String change3Key : change3KeyLis) {
                                String updateKey = "batch_gather_lis." + batchNum + ".gather_map." + gatherIndex + "." + change3Key;
                                update.set(updateKey, gatherLisMap1.get(change3Key));
                                reduction.set(updateKey, gatherNum1.get(change3Key));
                            }
                            gatherNum++;
                        }
                        batchNum++;
                    }
                } else {
                    List<BatchGatherLis> batchGatherLis = itemInfo.getBatch_gather_lis();
                    for (BatchGatherLis batchIndex : batchGatherLis) {
                        //对非空白样和空白样列表合并采样不同批次信息分类
                        List<String> sampleCodeList = batchIndex.getSample_code_lis();
                        int sampleKbNum = batchIndex.getSample_kb_code_lis().size();
                        String gatherDate = batchIndex.getGather_date();
                        int operatorsNum = batchIndex.getOperators_num();
                        Integer workingEquip = batchIndex.getWorking_equip();
                        String epeWorking = batchIndex.getEpe_working();
                        String ppeWorking = batchIndex.getPpe_working();
                        List<Map<String, String>> equipWorkingLis = batchIndex.getEquip_working_lis();
                        List<Map<String, String>> epeWorkingLis = batchIndex.getEpe_working_lis();
                        List<Map<String, String>> ppeWorkingLis = batchIndex.getPpe_working_lis();
                        Scene scene = batchIndex.getScene();
                        String temp = scene.getTemp();
                        String pressure = scene.getPressure();
                        for (String gatherInfo : sampleCodeList) {
                            List<String> sampleKbCodeLis = itemInfo.getSample_kb_code_lis();
                            if (sampleKbCodeLis.contains(gatherInfo)) {
                                Map<String, GatherLis> gather_kb_map = batchIndex.getGather_kb_map();
                                GatherLis gatherKbMap = gather_kb_map.get(gatherInfo);

                                String beforeFlow = gatherKbMap.getBefore_flow();
                                String afterFlow = gatherKbMap.getAfter_flow();
                                int testDuration = gatherKbMap.getTest_duration();
                                String people = gatherKbMap.getPeople();
                                String beginTime = gatherKbMap.getBegin_time();
                                String endTime = gatherKbMap.getEnd_time();
                                String timeFrame = gatherKbMap.getTime_frame();
                                SampleEquip sampleEquip = gatherKbMap.getSample_equip();
                                String nameModelId = sampleEquip.getName_model_id();
                                String instrumentCode = sampleEquip.getInstrument_code();
                                if (!instrumentCodeList.contains(instrumentCode)) {
                                    instrumentCodeList.add(instrumentCode);
                                }
                                String calibrationInfo = sampleEquip.getCalibration_info();
                                Map<String, Object> sampleInfoItem = new HashMap<>();
                                sampleInfoItem.put("test_duration", testDuration);
                                sampleInfoItem.put("begin_time", beginTime);
                                sampleInfoItem.put("end_time", endTime);
                                sampleInfoItem.put("time_frame", timeFrame);
                                sampleInfoItem.put("before_flow", beforeFlow);
                                sampleInfoItem.put("after_flow", afterFlow);
                                sampleInfoItem.put("name_model_id", nameModelId);
                                sampleInfoItem.put("instrument_code", instrumentCode);
                                sampleInfoItem.put("calibration_info", calibrationInfo);
                                sampleInfoItem.put("operators_num", operatorsNum);
                                sampleInfoItem.put("working_equip", workingEquip);
                                sampleInfoItem.put("equip_lis", equipLis);
                                sampleInfoItem.put("epe_working", epeWorking);
                                sampleInfoItem.put("ppe_working", ppeWorking);
                                sampleInfoItem.put("equip_working_lis", equipWorkingLis);
                                sampleInfoItem.put("epe_working_lis", epeWorkingLis);
                                sampleInfoItem.put("ppe_working_lis", ppeWorkingLis);
                                sampleInfoItem.put("gather_date", gatherDate);
                                sampleInfoItem.put("work_content", workContent);
                                sampleInfoItem.put("people_num", peopleNum);
                                sampleInfoItem.put("equip_name", equipName);
                                sampleInfoItem.put("temp", temp);
                                sampleInfoItem.put("pressure", pressure);
                                sampleInfoItem.put("people", people);
                                sampleInfo.add(sampleInfoItem);
                            } else {
                                //对批次内的样品列表循环依次获取单个样品信息
                                GatherLis gatherMapGatherInfo = batchIndex.getGather_map().get(gatherInfo);
                                SampleEquip sampleEquip = gatherMapGatherInfo.getSample_equip();
                                String instrumentCode = sampleEquip.getInstrument_code();  // 仪器编号
                                if (!instrumentCodeList.contains(instrumentCode)) {
                                    instrumentCodeList.add(instrumentCode);
                                }
                                String calibrationInfo = sampleEquip.getCalibration_info();  // 校准仪器名称 / 编号 - -声校准器型号 / 编号
                                String nameModelId = sampleEquip.getName_model_id();  // 仪器名称
                                String people = gatherMapGatherInfo.getPeople();
                                String beforeFlow = gatherMapGatherInfo.getBefore_flow();  // 采样前流量
                                String afterFlow = gatherMapGatherInfo.getAfter_flow();  // 采样后流量
                                int testDuration = gatherMapGatherInfo.getTest_duration();  // 采样时长/测量时间
                                String beginTime = gatherMapGatherInfo.getBegin_time();  // 采样时长/测量时间
                                String endTime = gatherMapGatherInfo.getEnd_time();  // 采样时长/测量时间
                                String timeFrame = gatherMapGatherInfo.getTime_frame();  // 采样时长/测量时间

                                Map<String, Object> sampleInfoItem = new HashMap<>();
                                sampleInfoItem.put("test_duration", testDuration);
                                sampleInfoItem.put("before_flow", beforeFlow);
                                sampleInfoItem.put("after_flow", afterFlow);
                                sampleInfoItem.put("begin_time", beginTime);
                                sampleInfoItem.put("end_time", endTime);
                                sampleInfoItem.put("time_frame", timeFrame);
                                sampleInfoItem.put("name_model_id", nameModelId);
                                sampleInfoItem.put("instrument_code", instrumentCode);
                                sampleInfoItem.put("calibration_info", calibrationInfo);
                                sampleInfoItem.put("operators_num", operatorsNum);
                                sampleInfoItem.put("working_equip", workingEquip);
                                sampleInfoItem.put("epe_working", epeWorking);
                                sampleInfoItem.put("ppe_working", ppeWorking);
                                sampleInfoItem.put("equip_lis", equipLis);
                                sampleInfoItem.put("equip_working_lis", equipWorkingLis);
                                sampleInfoItem.put("epe_working_lis", epeWorkingLis);
                                sampleInfoItem.put("ppe_working_lis", ppeWorkingLis);
                                sampleInfoItem.put("gather_date", gatherDate);
                                sampleInfoItem.put("work_content", workContent);
                                sampleInfoItem.put("people_num", peopleNum);
                                sampleInfoItem.put("equip_name", equipName);
                                sampleInfoItem.put("temp", temp);
                                sampleInfoItem.put("pressure", pressure);
                                sampleInfoItem.put("people", people);

                                sampleInfo.add(sampleInfoItem);
                            }

                        }
                        change3KeyLis = Arrays.asList("begin_time", "end_time", "time_frame", "people");
                        Map<String, GatherLis> gatherMap = batchIndex.getGather_map();
                        for (Map.Entry<String, GatherLis> entry : gatherMap.entrySet()) {
                            String gatherIndex = entry.getKey();
                            Map<String, Object> gatherNum1 = sampleInfo.get(gatherNum);
                            List<BatchGatherLis> batch_gather_lis = kVDic.getBatch_gather_lis();
                            Operation operation = kVDic.getOperation();
                            Map<String, Object> operationMap1 = new HashMap<>();
                            Map<String, Object> batch_gatherMap1 = new HashMap<>();
                            Map<String, Object> gatherLisMap1 = new HashMap<>();
                            BatchGatherLis batch_gather = batch_gather_lis.get(batchNum);
                            Map<String, GatherLis> gather_map = batch_gather.getGather_map();
                            GatherLis gatherLis = gather_map.get(gatherIndex);
                            try {
                                operationMap1 = this.convert(operation);
                                batch_gatherMap1 = this.convert(batch_gather);
                                gatherLisMap1 = this.convert(gatherLis);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            for (String change1Key : change1KeyLis) {
                                String updateKey = "operation." + change1Key;
                                update.set(updateKey, operationMap1.get(change1Key));
                                reduction.set(updateKey, gatherNum1.get(change1Key));
                            }
                            for (String change2Key : change2KeyLis) {
                                String updateKey = "batch_gather_lis." + batchNum + "." + change2Key;
                                update.set(updateKey, batch_gatherMap1.get(change2Key));
                                reduction.set(updateKey, gatherNum1.get(change2Key));
                            }
                            for (String change3Key : change3KeyLis) {
                                String updateKey = "batch_gather_lis." + batchNum + ".gather_map." + gatherIndex + "." + change3Key;
                                update.set(updateKey, gatherLisMap1.get(change3Key));
                                reduction.set(updateKey, gatherNum1.get(change3Key));
                            }
                            gatherNum++;
                            batchNum++;
                        }
                    }

                }

            } else {
                int batchNum = 0;
                int gatherNum = 0;
                Operation operation = itemInfo.getOperation();
                String workContent = operation.getWork_content();
                int peopleNum = operation.getPeople_num();
                String equipName = operation.getEquip_name();
                List<Map<String, String>> equipLis = operation.getEquip_lis();
                List<BatchGatherLis> batchGatherLis = itemInfo.getBatch_gather_lis();
                for (BatchGatherLis batchIndex : batchGatherLis) {
                    int operatorsNum = batchIndex.getOperators_num();
                    Integer workingEquip = batchIndex.getWorking_equip();
                    String epeWorking = batchIndex.getEpe_working();
                    String ppeWorking = batchIndex.getPpe_working();
                    List<Map<String, String>> equipWorkingLis = batchIndex.getEquip_working_lis();
                    List<Map<String, String>> epeWorkingLis = batchIndex.getEpe_working_lis();
                    List<Map<String, String>> ppeWorkingLis = batchIndex.getPpe_working_lis();
                    Scene scene = batchIndex.getScene();
                    String temp = scene.getTemp();
                    String humidity = scene.getHumidity();
                    List<String> sample_kb_code_lis = batchIndex.getSample_kb_code_lis();
                    int sampleKbNum = sample_kb_code_lis.size();

                    Map<String, GatherLis> gatherMap = batchIndex.getGather_map();
                    for (Map.Entry<String, GatherLis> entry : gatherMap.entrySet()) {
                        // 对批次内的样品列表循环依次获取单个样品信息
                        String gatherIndex = entry.getKey();
                        GatherLis gatherInfo = entry.getValue();
                        SampleEquip sampleEquip = gatherInfo.getSample_equip();
                        String nameModelId = sampleEquip.getName_model_id();//仪器名称
                        String instrumentCode = sampleEquip.getInstrument_code();//仪器编号
                        String people = gatherInfo.getPeople();//个体佩戴人
                        if (!instrumentCodeList.contains(instrumentCode)) {
                            instrumentCodeList.add(instrumentCode);
                        }
                        may.yuntian.jianping.mongodto.Result result = gatherInfo.getResult();
                        String labourLevel = result.getAbout2();//劳动强度等级
                        Map<String, Object> sampleInfoItem = new HashMap<>();
                        sampleInfoItem.put("work_content", workContent);
                        sampleInfoItem.put("people_num", peopleNum);
                        sampleInfoItem.put("equip_name", equipName);
                        sampleInfoItem.put("equip_lis", equipLis);
                        sampleInfoItem.put("equip_working_lis", equipWorkingLis);
                        sampleInfoItem.put("epe_working_lis", epeWorkingLis);
                        sampleInfoItem.put("ppe_working_lis", ppeWorkingLis);
                        sampleInfoItem.put("operators_num", operatorsNum);
                        sampleInfoItem.put("working_equip", workingEquip);
                        String result_time_avg = result.getResult_time_avg();
                        if (StringUtils.isNoneBlank(result_time_avg)) {
                            double resultTimeAvg = Double.parseDouble(result_time_avg);//wbgt
                            sampleInfoItem.put("result_time_avg", resultTimeAvg);
                        } else {
                            sampleInfoItem.put("result_time_avg", null);
                        }
                        sampleInfoItem.put("epe_working", epeWorking);
                        sampleInfoItem.put("ppe_working", ppeWorking);
                        sampleInfoItem.put("temp", temp);
                        sampleInfoItem.put("humidity", humidity);
                        sampleInfoItem.put("about2", labourLevel);
                        sampleInfoItem.put("instrument_code", instrumentCode);
                        sampleInfoItem.put("people", people);
                        sampleInfoItem.put("name_model_id", nameModelId);
                        sampleInfo.add(sampleInfoItem);
                        Map<String, Object> gatherNum1 = sampleInfo.get(gatherNum);
                        List<BatchGatherLis> batch_gather_lis = kVDic.getBatch_gather_lis();
                        Operation operation1 = kVDic.getOperation();
                        Map<String, Object> operationMap1 = new HashMap<>();
                        Map<String, Object> batch_gatherMap1 = new HashMap<>();
                        Map<String, Object> gatherLisMap1 = new HashMap<>();
                        BatchGatherLis batch_gather = batch_gather_lis.get(batchNum);
                        Map<String, GatherLis> gather_map = batch_gather.getGather_map();
                        GatherLis gatherLis = gather_map.get(gatherIndex);
                        try {
                            operationMap1 = this.convert(operation1);
                            batch_gatherMap1 = this.convert(batch_gather);
                            gatherLisMap1 = this.convert(gatherLis);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        for (String change1Key : change1KeyLis) {
                            String updateKey = "operation." + change1Key;
                            update.set(updateKey, operationMap1.get(change1Key));
                            reduction.set(updateKey, gatherNum1.get(change1Key));
                        }
                        for (String change2Key : change2KeyLis) {
                            String updateKey = "batch_gather_lis." + batchNum + "." + change2Key;
                            update.set(updateKey, batch_gatherMap1.get(change2Key));
                            reduction.set(updateKey, gatherNum1.get(change2Key));
                        }
                        if (show_type == 3) {
                            String updateKey = "batch_gather_lis." + batchNum + ".gather_map." + gatherIndex + "." + change4KeyLis.get(0);
                            update.set(updateKey, gatherLisMap1.get(change4KeyLis.get(0)));
                            reduction.set(updateKey, gatherNum1.get(change4KeyLis.get(0)));
                        }
                        gatherNum++;
                    }
                    batchNum++;
                }
            }
            Query query1 = new Query();
            query1.addCriteria(Criteria.where("_id").is(r_id));
            MongoUpdateDto mongoUpdateDto = new MongoUpdateDto();
            mongoUpdateDto.setFlt(query1);
            mongoUpdateDto.setUpdate(update);
            mongoUpdateDto.setReduction(reduction);
            fltUpdateLis.add(mongoUpdateDto);
            try {
                for (MongoUpdateDto itemFu : fltUpdateLis) {
                    Query flt = itemFu.getFlt();
                    Update update1 = itemFu.getUpdate();

                    UpdateResult res0 = mongoTemplate.updateFirst(flt, update1, "zj_plan_record");
                }
            } catch (Exception e) {
                for (MongoUpdateDto itemFu : fltUpdateLis) {
                    Query flt = itemFu.getFlt();
                    Update reduction1 = itemFu.getReduction();

                    UpdateResult res0 = mongoTemplate.updateFirst(flt, reduction1, "zj_plan_record");
                }
                return Result.error(500, "采样记录保存失败");
            }
        }
        return Result.ok("采样记录保存成功");
    }


    /**
     * 将对象转为map
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    private Map<String, Object> convert(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object fieldValue = field.get(obj);
            map.put(fieldName, fieldValue);
        }
        return map;
    }


    /**
     * 采样设备间隔判断
     *
     * @param params
     * @return
     */
    public Result equipTimeCheck1(List<List<ZjPlanRecord>> params) {

        List<ZjPlanRecord> pList = params.get(0);
        for (ZjPlanRecord p : pList) {
            int showType = p.getShow_type();
            if (showType == 1) {
                return airEquipCheck1(params);
            } else if (showType == 2 || showType == 3) {
                return noiseIndividualCheck1(params);
            } else if (showType == 5) {
                return ultravioletCheck1(params);
            } else if (Arrays.asList(4, 6, 7, 8, 9, 10, 11, 12).contains(showType)) {
                return phyCheck1(params, showType);
            } else {
                return Result.ok("采样仪器使用时间校验通过");
            }
        }
        return null;
    }


    /**
     * 空气采样设备间隔判断
     *
     * @param params
     * @return
     */
    public Result airEquipCheck1(List<List<ZjPlanRecord>> params) {
        List<String> existId = new ArrayList<>();
        List<String> modifiedId = new ArrayList<>();
        List<String> modified_id_new = new ArrayList<>();
        Map<String, Map<String, List<Map<String, Object>>>> dtMap = new HashMap<>();
        Map<String, List<BatchGatherLis>> comparisonInfo = new HashMap<>();
        Map<String, List<BatchGatherLis>> existedInfo = new HashMap<>();
        Map<String, String> pointInfo = new HashMap<>();
        Map<String, Substance> substanceInfo = new HashMap<>();
        Map<String, List<Substance>> existSubstanceInfo = new HashMap<>();
        Map<String, Map<String, List<Map<String, Object>>>> dtMapNew = new HashMap<>();
        List<ZjPlanRecord> params_lis = new ArrayList<>();
        List<Long> except_dust_id = new ArrayList<>();

        List<ZjPlanRecord> list = params.get(0);
        Long project_id = null;
        String identifier = "";
        for (ZjPlanRecord p : list) {
            project_id = p.getProject_id();
            identifier = p.getIdentifier();
            Integer showType = p.getShow_type();
            if (showType != 1) {
                return Result.ok("采样仪器使用时间校验通过", null);
            }
        }
        for (List<ZjPlanRecord> i : params) {
            for (ZjPlanRecord j : i) {
                List<BatchGatherLis> batchGatherLis = j.getBatch_gather_lis();
                String id = j.get_id();
                if (!modifiedId.contains(id)) {
                    modifiedId.add(id);
                    params_lis.add(j);
                }
                comparisonInfo.put(id, batchGatherLis);
                substanceInfo.put(id, j.getSubstance());
            }
        }
        Query query = new Query().addCriteria(Criteria.where("substance.substance_info.sample_tablename").is("airFixed"))
                .addCriteria(Criteria.where("project_id").is(project_id));
        List<ZjPlanRecord> planLis = mongoTemplate.find(query, ZjPlanRecord.class);

        Query query1 = new Query().addCriteria(Criteria.where("substance.substance_info.sample_tablename").is("airFixed"))
                .addCriteria(Criteria.where("substance.substance_info.s_type").is(2))
                .addCriteria(Criteria.where("project_id").is(project_id));
        List<ZjPlanRecord> planLis_ = mongoTemplate.find(query1, ZjPlanRecord.class);

        for (ZjPlanRecord p : planLis_) {
            String plan_id_str_ = p.get_id();
            if (!modifiedId.contains(plan_id_str_)) {
                params_lis.add(p);
                //params_lis是传参以及数据库原有的粉尘数据
            }
        }
        Map<String, Map<String, Map<Long, List<Map<String, Object>>>>> plan_point_map = new HashMap<>();

        for (ZjPlanRecord d_p : params_lis) {
            String d_p_id = d_p.get_id();
            BatchGatherLis batch_gather_lis = d_p.getBatch_gather_lis().get(0);
            String gather_date = batch_gather_lis.getGather_date();
            String point = d_p.getPoint_code();
            Substance substance = d_p.getSubstance();
            SubstanceInfo final_substance_info = substance.getFinal_substance_info();
            Long total_dust_id = final_substance_info.getTotal_dust_id();
            if (!plan_point_map.containsKey(gather_date)) {
                plan_point_map.put(gather_date, new HashMap<>());
            }
            if (!plan_point_map.get(gather_date).containsKey(point)) {
                plan_point_map.get(gather_date).put(point, new HashMap<>());
            }
            if (total_dust_id == 0) {
                Long dust_id = substance.getId();
                if (!plan_point_map.get(gather_date).get(point).containsKey(dust_id)) {
                    plan_point_map.get(gather_date).get(point).put(dust_id, new ArrayList<>());
                }
                Map<String, Object> map = new HashMap<>();
                map.put("id", d_p_id);
                map.put("batch_gather_lis", d_p.getBatch_gather_lis());
                plan_point_map.get(gather_date).get(point).get(dust_id).add(map);
            } else {
                if (!plan_point_map.get(gather_date).get(point).containsKey(total_dust_id)) {
                    plan_point_map.get(gather_date).get(point).put(total_dust_id, new ArrayList<>());
                }
                Map<String, Object> map = new HashMap<>();
                map.put("id", d_p_id);
                map.put("batch_gather_lis", d_p.getBatch_gather_lis());
                plan_point_map.get(gather_date).get(point).get(total_dust_id).add(map);
            }
        }

        for (Map.Entry<String, Map<String, Map<Long, List<Map<String, Object>>>>> entry : plan_point_map.entrySet()) {
            String gather_date = entry.getKey();
            Map<String, Map<Long, List<Map<String, Object>>>> point_map = entry.getValue();

            for (Map.Entry<String, Map<Long, List<Map<String, Object>>>> entry_1 : point_map.entrySet()) {
                String point = entry_1.getKey();
                Map<Long, List<Map<String, Object>>> dust_map = entry_1.getValue();

                for (Map.Entry<Long, List<Map<String, Object>>> entry_1_1 : dust_map.entrySet()) {
                    Long dust_id = entry_1_1.getKey();
                    List<Map<String, Object>> dust_info = entry_1_1.getValue();

                    if (dust_info.size() == 2) {
                        Map<String, List<Map<String, Object>>> dust_map_inner = new HashMap<>();
                        for (Map<String, Object> info : dust_info) {
                            String dust_id_ = info.get("id").toString();
                            BatchGatherLis batch_gather_lis_inner = ((List<BatchGatherLis>) info.get("batch_gather_lis")).get(0);
                            Map<String, GatherLis> gather_map = batch_gather_lis_inner.getGather_map();

                            for (String code : gather_map.keySet()) {
                                GatherLis gather_info = gather_map.get(code);
                                String begin_time = gather_info.getBegin_time();
                                String end_time = gather_info.getEnd_time();
                                SampleEquip sample_equip = gather_info.getSample_equip();
                                String name_model_id = sample_equip.getName_model_id();
                                String instrument_code = sample_equip.getInstrument_code();

                                if (name_model_id.startsWith("双路")) {
                                    if (!dust_map_inner.containsKey(dust_id_)) {
                                        dust_map_inner.put(dust_id_, new ArrayList<>());
                                    }
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("instrument_code", instrument_code);
                                    map.put("begin_time", begin_time);
                                    map.put("end_time", end_time);
                                    dust_map_inner.get(dust_id_).add(map);
                                }
                            }
                        }
                        List<List<Map<String, Object>>> time_lis = new ArrayList<>();
                        for (List<Map<String, Object>> v : dust_map_inner.values()) {
                            if (!time_lis.contains(v)) {
                                time_lis.add(v);
                            } else {
                                except_dust_id.add(dust_id);
                            }
                        }
                        if (time_lis.size() == 2) {
                            return Result.error(501, "同类粉尘在同一双路粉尘采样仪器测量时间不一致", null);
                        }
                    }
                }
            }
        }

        for (ZjPlanRecord pLis : planLis) {
            String planIdStr = pLis.get_id();
            if (!existId.contains(planIdStr) && !except_dust_id.contains(planIdStr)) {
                existId.add(planIdStr);
            }
            BatchGatherLis map = pLis.getBatch_gather_lis().get(0);
            List<BatchGatherLis> maps = new ArrayList<>();
            maps.add(map);
            String point_code = pLis.getPoint_code();

            Substance map2 = pLis.getSubstance();
            ArrayList<Substance> maps2 = new ArrayList<>();
            maps2.add(map2);
            existedInfo.put(planIdStr, maps);
            pointInfo.put(planIdStr, point_code);
            existSubstanceInfo.put(planIdStr, maps2);
        }
        for (String m_id_ : modifiedId) {
            if (!except_dust_id.contains(m_id_)) {
                modified_id_new.add(m_id_);
            }
        }

        for (String eId : existId) {
            String pointCode = pointInfo.get(eId).toString();
            Map<String, GatherLis> gatherInfo = new HashMap<>();
            String gatherDate;
            SubstanceInfo substanceInfo_;
            if (modified_id_new.contains(eId)) {
                BatchGatherLis map = comparisonInfo.get(eId).get(0);
                gatherInfo = map.getGather_map();
                gatherDate = map.getGather_date();
                substanceInfo_ = substanceInfo.get(eId).getSubstance_info();
            } else {
                BatchGatherLis map = existedInfo.get(eId).get(0);
                gatherInfo = map.getGather_map();
                gatherDate = map.getGather_date();
                substanceInfo_ = existSubstanceInfo.get(eId).get(0).getSubstance_info();
            }
            if (!dtMap.containsKey(gatherDate)) {
                dtMap.put(gatherDate, new HashMap<>());
            }
            String collector = substanceInfo_.getCollector();
            if (substanceInfo_.getSubstance_id() != 649L && !Arrays.asList("采气袋", "采集袋", "注射器").contains(collector)) {
                for (String code : gatherInfo.keySet()) {
                    String sampleCode = identifier + pointCode + '-' + code;
                    GatherLis gather = gatherInfo.get(code);
                    SampleEquip map2 = gather.getSample_equip();
                    String instrumentCode = map2.getInstrument_code();
                    int testDuration = gather.getTest_duration();
                    String beginTime = gather.getBegin_time();
                    String endTime = "";
                    if (beginTime != null && !beginTime.isEmpty()) {
                        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                        Date beginTimeDate = null;
                        try {
                            beginTimeDate = formatter.parse(beginTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(beginTimeDate);
                        cal.add(Calendar.MINUTE, testDuration);
                        endTime = formatter.format(cal.getTime());
                        beginTime = formatter.format(beginTimeDate);

                        if (!dtMap.get(gatherDate).containsKey(instrumentCode)) {
                            dtMap.get(gatherDate).put(instrumentCode, new ArrayList<>());
                        }
                        Map<String, Object> time1 = new HashMap<>();
                        time1.put("sample_code", sampleCode);
                        time1.put("begin_time", beginTime);
                        time1.put("time", beginTime);
                        time1.put("t_num", 2);
                        Map<String, Object> time2 = new HashMap<>();
                        time2.put("sample_code", sampleCode);
                        time2.put("end_time", endTime);
                        time2.put("time", endTime);
                        time2.put("t_num", 1);
                        dtMap.get(gatherDate).get(instrumentCode).add(time1);
                        dtMap.get(gatherDate).get(instrumentCode).add(time2);
                    }
                }
            }
        }

        for (String gatherDate : dtMap.keySet()) {
            if (!dtMapNew.containsKey(gatherDate)) {
                dtMapNew.put(gatherDate, new HashMap<>());
            }
            for (String instrumentCode : dtMap.get(gatherDate).keySet()) {
                if (!dtMapNew.get(gatherDate).containsKey(instrumentCode)) {
                    dtMapNew.get(gatherDate).put(instrumentCode, new ArrayList<>());
                }
                List<Map<String, Object>> newLst = new ArrayList<>();
                HashSet<Map<String, Object>> uniqueSet = new HashSet<>(dtMap.get(gatherDate).get(instrumentCode));

                for (Map<String, Object> item : uniqueSet) {
                    newLst.add(item);
                }
                newLst.sort((a, b) -> {
                    if (a.get("time").equals(b.get("time"))) {
                        return ((Integer) a.get("t_num")).compareTo((Integer) b.get("t_num"));
                    } else {
                        return ((String) a.get("time")).compareTo((String) b.get("time"));
                    }
                });

                dtMapNew.get(gatherDate).put(instrumentCode, newLst);
            }
        }

        for (String date : dtMapNew.keySet()) {
            for (String instrumentCode : dtMapNew.get(date).keySet()) {
                List<Map<String, Object>> newOne = dtMapNew.get(date).get(instrumentCode);
                String time_1;
                String time_2;
                String point_1;
                String point_2;

                if (newOne != null) {
                    for (int index = 0; index < newOne.size() / 2; index++) {
                        String sample_code1 = newOne.get(2 * index).get("sample_code").toString();
                        String sample_code2 = newOne.get(2 * index + 1).get("sample_code").toString();
                        if (!sample_code1.equals(sample_code2)) {
                            point_1 = (String) newOne.get(2 * index).get("sample_code");
                            point_2 = (String) newOne.get(2 * index + 1).get("sample_code");
                            return Result.error(501, String.format("样品%s与样品%s采样仪器使用时间冲突", point_1, point_2));
                        } else {
                            if (newOne.size() == 2) {
                                if (index == 0) {
                                    time_1 = (String) newOne.get(2 * index).get("begin_time");
                                    time_2 = (String) newOne.get(2 * index + 1).get("end_time");
                                    point_1 = (String) newOne.get(2 * index).get("sample_code");
                                    point_2 = (String) newOne.get(2 * index + 1).get("sample_code");
                                    continue;
                                }
                            } else {
                                if (index == 0) {
                                    index = 1;
                                    time_1 = (String) newOne.get(2 * index - 1).get("end_time");
                                    time_2 = (String) newOne.get(2 * index).get("begin_time");
                                    point_1 = (String) newOne.get(2 * index - 1).get("sample_code");
                                    point_2 = (String) newOne.get(2 * index).get("sample_code");
                                } else {
                                    time_1 = (String) newOne.get(2 * index - 1).get("end_time");
                                    time_2 = (String) newOne.get(2 * index).get("begin_time");
                                    point_1 = (String) newOne.get(2 * index - 1).get("sample_code");
                                    point_2 = (String) newOne.get(2 * index).get("sample_code");
                                }

                                if (time_1 != null && time_2 != null) {
                                    if (time_1.equals(time_2)) {
                                        return Result.error(501, String.format("样品%s与样品%s采样仪器使用间隔小于2分钟", point_1, point_2));
                                    } else {
                                        // calculate result
                                        int result = Integer.parseInt(time_2.split(":")[0]) * 60
                                                + Integer.parseInt(time_2.split(":")[1])
                                                - Integer.parseInt(time_1.split(":")[0]) * 60
                                                - Integer.parseInt(time_1.split(":")[1]);

                                        if (result < 2) {
                                            return Result.error(501, String.format("样品%s与样品%s采样仪器使用间隔小于2分钟", point_1, point_2));
                                        } else {
//                                            index += 1;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return Result.ok("采样仪器使用时间校验通过", null);
    }


    /**
     * 噪声采样设备间隔判断
     *
     * @param params
     * @return
     */
    public Result noiseIndividualCheck1(List<List<ZjPlanRecord>> params) {
        List<String> existId = new ArrayList<>();
        List<String> modifiedId = new ArrayList<>();
        Map<String, Map<String, List<Map<String, Object>>>> dtMapNI = new HashMap<>();
        Map<String, List<BatchGatherLis>> comparisonInfo = new HashMap<>();
        Map<String, List<BatchGatherLis>> existedInfo = new HashMap<>();
        Map<String, Object> placeInfo = new HashMap<>();
        Map<String, String> modifiedPointInfo = new HashMap<>();
        Map<String, Map<String, List<Map<String, Object>>>> dtMapNew = new HashMap<>();

        List<ZjPlanRecord> list = params.get(0);
        ZjPlanRecord p = list.get(0);
        Long project_id = p.getProject_id();

        String idStr = null;
        for (List<ZjPlanRecord> i : params) {
            for (ZjPlanRecord j : i) {
                List<BatchGatherLis> batchGatherLis = j.getBatch_gather_lis();
                idStr = j.get_id();
                Integer isFixed = j.getIs_fixed();
                if (!modifiedId.contains(idStr)) {
                    modifiedId.add(idStr);
                }
                comparisonInfo.put(idStr, batchGatherLis);
                String testPlace = j.getTest_place();
                Place place = j.getPlace();
                if (isFixed == 2) {
                    modifiedPointInfo.put(idStr, place.getWorkshop_area() + "/" + testPlace);
                } else {
                    modifiedPointInfo.put(idStr, testPlace);
                }
            }
        }

        Query query = new Query().addCriteria(Criteria.where("substance.substance_info.s_type").is(3))
                .addCriteria(Criteria.where("project_id").is(project_id));
        List<ZjPlanRecord> planLis = mongoTemplate.find(query, ZjPlanRecord.class);


        for (ZjPlanRecord pLis : planLis) {
            String planIdStr = pLis.get_id();
            if (!existId.contains(planIdStr)) {
                existId.add(planIdStr);
            }
            BatchGatherLis map = pLis.getBatch_gather_lis().get(0);
            List<BatchGatherLis> maps = new ArrayList<>();
            maps.add(map);
            existedInfo.put(planIdStr, maps);
            placeInfo.put(planIdStr, pLis.getTest_place());
            placeInfo.put(planIdStr, pLis.getPlace());
            if (pLis.getIs_fixed() == 2) {
                Place place = pLis.getPlace();
                placeInfo.put(planIdStr, place.getWorkshop_area() + "/" + pLis.getTest_place());
            } else {
                placeInfo.put(planIdStr, pLis.getTest_place());
            }
        }

        for (String eId : existId) {
            String id;
            String gatherDate;
            String profession;
            Map<String, GatherLis> gatherInfo = new HashMap<>();
            if (modifiedId.contains(eId)) {
                id = idStr;
                BatchGatherLis map = comparisonInfo.get(eId).get(0);
                gatherInfo = map.getGather_map();
                gatherDate = map.getGather_date();
                profession = modifiedPointInfo.get(eId);
            } else {
                id = eId;
                BatchGatherLis map = existedInfo.get(eId).get(0);
                gatherInfo = map.getGather_map();
                gatherDate = map.getGather_date();
                profession = placeInfo.get(eId).toString();
            }
            if (!dtMapNI.containsKey(gatherDate)) {
                dtMapNI.put(gatherDate, new HashMap<>());
            }
            for (String code : gatherInfo.keySet()) {
                GatherLis gather = gatherInfo.get(code);
                SampleEquip map2 = gather.getSample_equip();
                String instrumentCode = map2.getInstrument_code();
                int testDuration = gather.getTest_duration();
                String beginTime = gather.getBegin_time();
                String endTime = "";
                if (beginTime != null && !beginTime.isEmpty()) {
                    Date beginTimeDate = null;
                    try {
                        beginTimeDate = new SimpleDateFormat("HH:mm").parse(beginTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(beginTimeDate);
                    cal.add(Calendar.MINUTE, testDuration);
                    endTime = new SimpleDateFormat("HH:mm").format(cal.getTime());
                    beginTime = new SimpleDateFormat("HH:mm").format(beginTimeDate);

                    if (!dtMapNI.get(gatherDate).containsKey(instrumentCode)) {
                        dtMapNI.get(gatherDate).put(instrumentCode, new ArrayList<>());
                    }
                    String finalBeginTime = beginTime;
                    dtMapNI.get(gatherDate).get(instrumentCode).add(new HashMap<String, Object>() {{
                        put("_id", id);
                        put("profession", profession);
                        put("begin_time", finalBeginTime);
                        put("time", finalBeginTime);
                        put("t_num", 2);
                    }});
                    String finalEndTime = endTime;
                    dtMapNI.get(gatherDate).get(instrumentCode).add(new HashMap<String, Object>() {{
                        put("_id", id);
                        put("profession", profession);
                        put("end_time", finalEndTime);
                        put("time", finalEndTime);
                        put("t_num", 1);
                    }});
                }
            }
        }

        for (String gatherDate : dtMapNI.keySet()) {
            if (!dtMapNew.containsKey(gatherDate)) {
                dtMapNew.put(gatherDate, new HashMap<>());
            }
            for (String instrumentCode : dtMapNI.get(gatherDate).keySet()) {
                if (!dtMapNew.get(gatherDate).containsKey(instrumentCode)) {
                    dtMapNew.get(gatherDate).put(instrumentCode, new ArrayList<>());
                }
                List<Map<String, Object>> timeList = dtMapNI.get(gatherDate).get(instrumentCode);
                Collections.sort(timeList, new Comparator<Map<String, Object>>() {
                    public int compare(Map<String, Object> m1, Map<String, Object> m2) {
                        String t1 = (String) m1.get("time");
                        String t2 = (String) m2.get("time");
                        int num1 = (int) m1.get("t_num");
                        int num2 = (int) m2.get("t_num");
                        if (t1 == null || t2 == null) {
                            return 0;
                        }
                        if (t1.equals(t2)) {
                            return num1 - num2;
                        }
                        return t1.compareTo(t2);
                    }
                });
                for (int i = 0; i < timeList.size(); i++) {
                    Map<String, Object> timeEntry = timeList.get(i);
                    if (timeEntry.get("time") != null) {
                        dtMapNew.get(gatherDate).get(instrumentCode).add(timeEntry);
                    }
                }
            }
        }

        for (String date : dtMapNew.keySet()) {
            for (String i : dtMapNew.get(date).keySet()) {
                if (i != null) {
                    List<Map<String, Object>> newOne = dtMapNew.get(date).get(i);
                    for (int index = 0; index < newOne.size() / 2; index++) {
                        String time1;
                        String time2;
                        String profession1;
                        String profession2;
                        if (!newOne.get(2 * index).get("_id").equals(newOne.get(2 * index + 1).get("_id"))) {
                            profession1 = (String) newOne.get(2 * index).get("profession");
                            profession2 = (String) newOne.get(2 * index + 1).get("profession");
                            return Result.error(501, profession1 + "与" + profession2 + "仪器使用时间冲突");
                        } else {
                            if (newOne.size() == 2) {
                                if (index == 0) {
                                    time1 = (String) newOne.get(2 * index).get("time");
                                    time2 = (String) newOne.get(2 * index + 1).get("time");
                                    profession1 = (String) newOne.get(2 * index).get("profession");
                                    profession2 = (String) newOne.get(2 * index + 1).get("profession");
                                    continue;
                                }
                            } else {
                                if (index == 0) {
                                    index = 1;
                                    time1 = (String) newOne.get(2 * index - 1).get("time");
                                    time2 = (String) newOne.get(2 * index).get("time");
                                    profession1 = (String) newOne.get(2 * index - 1).get("profession");
                                    profession2 = (String) newOne.get(2 * index).get("profession");
                                } else {
                                    time1 = (String) newOne.get(2 * index - 1).get("time");
                                    time2 = (String) newOne.get(2 * index).get("time");
                                    profession1 = (String) newOne.get(2 * index - 1).get("profession");
                                    profession2 = (String) newOne.get(2 * index).get("profession");
                                }
                                if (time1 != null && time2 != null) {
                                    if (time1.equals(time2)) {
                                        return Result.error(501, profession1 + "与" + profession2 + "仪器使用间隔小于2分钟");
                                    } else {
                                        String[] timeParts = time2.split(":");
                                        int result_h = Integer.parseInt(timeParts[0]) - Integer.parseInt(time1.split(":")[0]);
                                        int result_m = Integer.parseInt(timeParts[1]) - Integer.parseInt(time1.split(":")[1]);
                                        int result_ = result_h * 60 + result_m;
                                        if (result_ < 2) {
                                            return Result.error(501, profession1 + "与" + profession2 + "仪器使用间隔小于2分钟");
                                        } else {
//                                            index += 1;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return Result.ok("采样仪器使用时间校验通过");
    }


    /**
     * 紫外线采样设备间隔判断
     *
     * @param params
     * @return
     */
    public Result ultravioletCheck1(List<List<ZjPlanRecord>> params) {
        List<String> existId = new ArrayList<>();
        List<String> modifiedId = new ArrayList<>();
        Map<String, Map<String, List<Map<String, Object>>>> dtMap = new HashMap<>();
        Map<String, List<BatchGatherLis>> comparisonInfo = new HashMap<>();
        Map<String, List<BatchGatherLis>> existedInfo = new HashMap<>();
        Map<String, String> pointInfo = new HashMap<>();
        Map<String, String> modifiedPointInfo = new HashMap<>();
        Map<String, Map<String, List<Map<String, Object>>>> dtMapNew = new HashMap<>();

        List<ZjPlanRecord> list = params.get(0);
        ZjPlanRecord p = list.get(0);
        Long project_id = p.getProject_id();
        Integer showType = p.getShow_type();

        Query query = new Query();
        List<ZjPlanRecord> planLis;
        if (showType == 5) {
            query.addCriteria(Criteria.where("project_id").is(project_id));
            query.addCriteria(Criteria.where("substance.substance_info.sample_tablename").is("ultraviolet"));
            planLis = mongoTemplate.find(query, ZjPlanRecord.class);

        } else {
            return Result.ok("采样仪器使用时间校验通过");
        }

        for (List<ZjPlanRecord> i : params) {
            for (ZjPlanRecord j : i) {
                List<BatchGatherLis> batchGatherLis = j.getBatch_gather_lis();
                String id = j.get_id();
                if (!modifiedId.contains(id)) {
                    modifiedId.add(id);
                }
                comparisonInfo.put(id, batchGatherLis);
                modifiedPointInfo.put(id, j.getTest_place());
            }
        }

        for (ZjPlanRecord pLis : planLis) {
            String planIdStr = pLis.get_id();
            if (!existId.contains(planIdStr)) {
                existId.add(planIdStr);
            }
            BatchGatherLis map = pLis.getBatch_gather_lis().get(0);
            List<BatchGatherLis> maps = new ArrayList<>();
            maps.add(map);
            String testPlace = pLis.getTest_place();

            existedInfo.put(planIdStr, maps);
            pointInfo.put(planIdStr, testPlace);
        }

        for (String eId : existId) {
            Map<String, GatherLis> gatherInfo = new HashMap<>();
            String gatherDate;
            String detectPoint;

            if (modifiedId.contains(eId)) {
                BatchGatherLis map = comparisonInfo.get(eId).get(0);
                gatherInfo = map.getGather_map();
                gatherDate = map.getGather_date();
                detectPoint = modifiedPointInfo.get(eId);
            } else {
                BatchGatherLis map = existedInfo.get(eId).get(0);
                gatherInfo = map.getGather_map();
                gatherDate = map.getGather_date();
                detectPoint = pointInfo.get(eId);
            }
            if (!dtMap.containsKey(gatherDate)) {
                dtMap.put(gatherDate, new HashMap<>());
            }
            for (String code : gatherInfo.keySet()) {
                GatherLis gather = gatherInfo.get(code);
                SampleEquip map2 = gather.getSample_equip();
                String instrumentCode = map2.getInstrument_code();
                String beginTime = gather.getBegin_time();
                String endTime = "";

                if (beginTime != null && !beginTime.isEmpty()) {
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                    try {
                        endTime = new Date(format.parse(beginTime).getTime() + TimeUnit.MINUTES.toMillis(2)).toString();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        beginTime = format.parse(beginTime).toString();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (!dtMap.containsKey(gatherDate)) {
                        dtMap.put(gatherDate, new HashMap<>());
                    }
                    if (!dtMap.get(gatherDate).containsKey(instrumentCode)) {
                        dtMap.get(gatherDate).put(instrumentCode, new ArrayList<>());
                    }
                    Map<String, Object> startInfo = new HashMap<>();
                    startInfo.put("detect_point", detectPoint);
                    startInfo.put("begin_time", beginTime);
                    startInfo.put("time", beginTime);
                    startInfo.put("t_num", 2);  // 标识开始时间是2，结束时间是1

                    Map<String, Object> endInfo = new HashMap<>();
                    endInfo.put("detect_point", detectPoint);
                    endInfo.put("end_time", endTime);
                    endInfo.put("time", endTime);
                    endInfo.put("t_num", 1);  // 标识开始时间是2，结束时间是1

                    dtMap.get(gatherDate).get(instrumentCode).add(startInfo);
                    dtMap.get(gatherDate).get(instrumentCode).add(endInfo);
                }

            }
        }

        for (String gatherDate : dtMap.keySet()) {
            if (!dtMapNew.containsKey(gatherDate)) {
                dtMapNew.put(gatherDate, new HashMap<>());
            }
            for (String instrumentCode : dtMap.get(gatherDate).keySet()) {
                if (!dtMapNew.get(gatherDate).containsKey(instrumentCode)) {
                    dtMapNew.get(gatherDate).put(instrumentCode, new ArrayList<>());
                }
                List<Map<String, Object>> timeList = dtMap.get(gatherDate).get(instrumentCode);
                List<Map<String, Object>> newTimeList = sortAndRemoveDuplicates(timeList);
                dtMapNew.get(gatherDate).put(instrumentCode, newTimeList);
            }
        }

        for (String date : dtMapNew.keySet()) {
            for (String instrumentCode : dtMapNew.get(date).keySet()) {
                List<Map<String, Object>> newOne = dtMapNew.get(date).get(instrumentCode);

                if (instrumentCode != null && newOne.size() > 0) {
                    for (int index = 0; index < newOne.size() / 2; index++) {
                        Map<String, Object> entry1 = newOne.get(2 * index);
                        Map<String, Object> entry2 = newOne.get(2 * index + 1);

                        if (!entry1.get("detect_point").equals(entry2.get("detect_point"))) {
                            String detectPoint1 = (String) entry1.get("detect_point");
                            String detectPoint2 = (String) entry2.get("detect_point");
                            return Result.error(501, detectPoint1 + "与" + detectPoint2 + "仪器使用时间冲突");
                        } else {
                            if (newOne.size() == 2) {
                                // Skip this iteration as it is not required to validate time_1 and time_2
                            } else {
                                if (index == 0) {
                                    index = 1;
                                }
                                String begin_time = (String) entry1.get("begin_time");
                                String end_time = (String) entry2.get("end_time");
                                Date time1 = null;
                                Date time2 = null;
                                if (StringUtils.isNotBlank(begin_time) && StringUtils.isNotBlank(end_time)) {
                                    time1 = new Date(begin_time);
                                    time2 = new Date(end_time);
                                }
                                String detectPoint1 = (String) entry1.get("detect_point");
                                String detectPoint2 = (String) entry2.get("detect_point");

                                if (time1 != null && time2 != null) {
                                    long diffMinutes = (time2.getTime() - time1.getTime()) / (60 * 1000);

                                    if (diffMinutes < 2) {
                                        return Result.error(501, detectPoint1 + "与" + detectPoint2 + "仪器使用间隔小于2分钟");
                                    } else {
//                                        index++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return Result.ok("采样仪器使用时间校验通过");
    }


    /**
     * 化学采样设备间隔判断
     *
     * @param params
     * @param showType
     * @return
     */
    public Result phyCheck1(List<List<ZjPlanRecord>> params, int showType) {
        List<String> existId = new ArrayList<>();  // 数据库中已存的数据_id
        List<String> modifiedId = new ArrayList<>();  // 传递的参数的数据_id
        Map<String, Map<String, List<Map<String, Object>>>> dtMap = new HashMap<>();  // 仪器与采样时间map
        Map<String, List<BatchGatherLis>> comparisonInfo = new HashMap<>();
        Map<String, List<BatchGatherLis>> existedInfo = new HashMap<>();
        Map<String, String> pointInfo = new HashMap<>();  //  测试地点
        Map<String, String> modifiedPointInfo = new HashMap<>();  //  传递参数的测试地点
        Map<String, Map<String, List<Map<String, Object>>>> dtMapNew = new HashMap<>();  // 排序后的仪器与采样时间map

        List<ZjPlanRecord> list = params.get(0);
        ZjPlanRecord p = list.get(0);
        Long project_id = p.getProject_id();

        Query query = new Query();
        List<String> co_co2_lis = Arrays.asList("co", "co2");
        if (showType == 4) {
            query.addCriteria(Criteria.where("project_id").is(project_id));
            query.addCriteria(Criteria.where("substance.substance_info.sample_tablename").is("temperatureStable"));

        } else if (showType == 6) {
            query.addCriteria(Criteria.where("project_id").is(project_id));
            query.addCriteria(Criteria.where("substance.substance_info.sample_tablename").in(co_co2_lis));

        } else if (showType == 7) {
            query.addCriteria(Criteria.where("project_id").is(project_id));
            query.addCriteria(Criteria.where("substance.substance_info.sample_tablename").in(co_co2_lis));

        } else if (showType == 8) {
            query.addCriteria(Criteria.where("project_id").is(project_id));
            query.addCriteria(Criteria.where("substance.substance_info.sample_tablename").is("electromagnetic"));

        } else if (showType == 9) {
            query.addCriteria(Criteria.where("project_id").is(project_id));
            query.addCriteria(Criteria.where("substance.substance_info.sample_tablename").is("electric"));

        } else if (showType == 10) {
            query.addCriteria(Criteria.where("project_id").is(project_id));
            query.addCriteria(Criteria.where("substance.substance_info.sample_tablename").is("vibrationHand"));

        } else if (showType == 11) {
            query.addCriteria(Criteria.where("project_id").is(project_id));
            query.addCriteria(Criteria.where("substance.substance_info.sample_tablename").is("illumination"));

        } else if (showType == 12) {
            query.addCriteria(Criteria.where("project_id").is(project_id));
            query.addCriteria(Criteria.where("substance.substance_info.sample_tablename").is("laserRadiation"));

        } else {
            return Result.ok("采样仪器使用时间校验通过");
        }

        List<ZjPlanRecord> planLis = mongoTemplate.find(query, ZjPlanRecord.class);

        for (List<ZjPlanRecord> i : params) {
            for (ZjPlanRecord j : i) {
                List<BatchGatherLis> batchGatherLis = j.getBatch_gather_lis();
                String id = j.get_id();
                if (!modifiedId.contains(id)) {
                    modifiedId.add(id);
                }
                comparisonInfo.put(id, batchGatherLis);
                modifiedPointInfo.put(id, j.getTest_place());
            }
        }

        for (ZjPlanRecord pLis : planLis) {
            String planIdStr = pLis.get_id();
            if (!existId.contains(planIdStr)) {
                existId.add(planIdStr);
            }
            BatchGatherLis map = pLis.getBatch_gather_lis().get(0);
            List<BatchGatherLis> maps = new ArrayList<>();
            maps.add(map);
            String testPlace = pLis.getTest_place();

            existedInfo.put(planIdStr, maps);
            pointInfo.put(planIdStr, testPlace);
        }

        for (String eId : existId) {
            Map<String, GatherLis> gatherInfo = new HashMap<>();
            String gatherDate;
            String detectPoint;

            if (modifiedId.contains(eId)) {
                BatchGatherLis map = comparisonInfo.get(eId).get(0);
                gatherInfo = map.getGather_map();
                gatherDate = map.getGather_date();
                detectPoint = modifiedPointInfo.get(eId);
            } else {
                BatchGatherLis map = existedInfo.get(eId).get(0);
                gatherInfo = map.getGather_map();
                gatherDate = map.getGather_date();
                detectPoint = pointInfo.get(eId);
            }
            if (!dtMap.containsKey(gatherDate)) {
                dtMap.put(gatherDate, new HashMap<>());
            }
            for (String code : gatherInfo.keySet()) {
                GatherLis gather = gatherInfo.get(code);
                SampleEquip map2 = gather.getSample_equip();
                String instrumentCode = map2.getInstrument_code();
                String beginTime = gather.getBegin_time();

                String endTime = gather.getEnd_time();
                if (beginTime != null && !beginTime.isEmpty()) {
                    if (!dtMap.get(gatherDate).containsKey(instrumentCode)) {
                        dtMap.get(gatherDate).put(instrumentCode, new ArrayList<>());
                    }
                    Map<String, Object> timeEntry = new HashMap<>();
                    timeEntry.put("detect_point", detectPoint);
                    timeEntry.put("begin_time", beginTime);
                    timeEntry.put("time", beginTime);
                    timeEntry.put("t_num", 2);  // 标识开始时间是2，结束时间是1
                    dtMap.get(gatherDate).get(instrumentCode).add(timeEntry);
                }

            }
        }

        for (String gatherDate : dtMap.keySet()) {
            if (!dtMapNew.containsKey(gatherDate)) {
                dtMapNew.put(gatherDate, new HashMap<>());
            }
            for (String instrumentCode : dtMap.get(gatherDate).keySet()) {
                if (!dtMapNew.get(gatherDate).containsKey(instrumentCode)) {
                    dtMapNew.get(gatherDate).put(instrumentCode, new ArrayList<>());
                }
                List<Map<String, Object>> timeList = dtMap.get(gatherDate).get(instrumentCode);
                List<Map<String, Object>> newTimeList = sortAndRemoveDuplicates(timeList);
                //对timeList进行去重和排序
                for (Map<String, Object> timeInfo : newTimeList) {
                    if (timeInfo.get("time") != null) {
                        dtMapNew.get(gatherDate).get(instrumentCode).add(timeInfo);
                    }
                }
            }
        }

        for (String date : dtMapNew.keySet()) {
            Map<String, List<Map<String, Object>>> instrumentData = dtMapNew.get(date);
            for (String instrumentCode : instrumentData.keySet()) {
                List<Map<String, Object>> newOneList = instrumentData.get(instrumentCode);

                if (newOneList.size() <= 1) {
                    return Result.ok("采样仪器使用时间校验通过");
                }

                for (int index = 1; index < newOneList.size(); index++) {
                    Map<String, Object> newOnePrev = newOneList.get(index - 1);
                    Map<String, Object> newOneCurr = newOneList.get(index);

                    String time1 = (String) newOnePrev.get("begin_time");
                    String time2 = (String) newOneCurr.get("begin_time");
                    String detectPoint1 = (String) newOnePrev.get("detect_point");
                    String detectPoint2 = (String) newOneCurr.get("detect_point");

                    if (time1 != null && time2 != null) {
                        if (time1.equals(time2)) {
                            return Result.error(501, detectPoint1 + "与" + detectPoint2 + "仪器使用间隔小于2分钟");
                        } else {
                            LocalTime t1 = LocalTime.parse(time1, DateTimeFormatter.ofPattern("HH:mm"));
                            LocalTime t2 = LocalTime.parse(time2, DateTimeFormatter.ofPattern("HH:mm"));
                            int diffMinutes = (int) t2.until(t1, java.time.temporal.ChronoUnit.MINUTES);
                            if (diffMinutes < 2) {
                                Result.error(501, detectPoint1 + "与" + detectPoint2 + "仪器使用间隔小于2分钟");
                            } else {
//                                index += 1;
                            }
                        }
                    }
                }
            }
        }

        return Result.ok("采样仪器使用时间校验通过");
    }


    /**
     * pc样品信息外层进行保存
     *
     * @param object
     * @return Result
     */
    public Result pageSetOutRecord1(List<List<ZjPlanRecord>> params) {
        for (List<ZjPlanRecord> i : params) {
            for (ZjPlanRecord j : i) {
                Long project_id = j.getProject_id();
                Integer show_type = j.getShow_type();
                if (show_type < 1 || show_type > 15) {
                    return Result.error(501, "参数错误，信息展示类型错误").put("data", null);
                }
                String r_id = j.get_id();
                List<ZjPlanRecord> record_lis = mongoTemplate.find(new Query().addCriteria(Criteria.where("_id").is(r_id)), ZjPlanRecord.class);
                if (record_lis.size() == 0) {
                    return Result.error(501, "采样信息不存在").put("data", null);
                }
                List<MongoUpdateDto> flt_update_lis = new ArrayList<>();
                List<String> instrument_code_list = new ArrayList<>();
                ZjPlanRecord k_v_dic = new ZjPlanRecord();
                k_v_dic = j;
                Update reduction = new Update();
                Update update = new Update();
                List<Object> sample_info = new ArrayList<>();
                ZjPlanRecord item_info = record_lis.get(0);
                r_id = item_info.get_id();
                // 页面展示类型  1：空气中有害物质  2：噪声定点  3：噪声个体  4：高温  5：紫外  6：co  7：co2  8：高频电磁场  9:工频电场  10：手传振动  11：照度  12:：风速 ;
                int need_range = 2; // 记录是否需要随机生成同车间的温度气压湿度
                Map<String, Float> need_range_map = new HashMap<>();
                Map<String, Object> need_range_point = new HashMap<>();
                switch (show_type) {
                    case 1: {
                        // 1.空气中有害物质
                        int batch_num = 0;
                        int gather_num = 0;
                        List<String> change_key_lis = Arrays.asList("test_duration", "before_flow", "after_flow", "begin_time", "end_time");
                        List<String> change2_key_lis = Arrays.asList("name_model_id", "instrument_code", "calibration_info", "calibration_name", "calibration_code");
                        List<String> change3_key_lis = Arrays.asList("temp", "humidity", "pressure");
                        BatchGatherLis batch_index = null;
                        Operation operation = item_info.getOperation();
                        String work_content = operation.getWork_content();
                        Integer people_num = operation.getPeople_num();
                        String equip_name = operation.getEquip_name();
                        List<Map<String, String>> equip_lis = operation.getEquip_lis();
                        for (BatchGatherLis batchIndex : item_info.getBatch_gather_lis()) {
                            batch_index = batchIndex;
                            // 对非空白样和空白样列表合并采样不同批次信息分类
                            // 样品编号列表
                            List<String> sample_code_lis = batch_index.getSample_code_lis();
                            int sample_kb_num = sample_code_lis.size();
                            String gather_date = batch_index.getGather_date();
                            Integer operators_num = batch_index.getOperators_num();
                            Integer working_equip = batch_index.getWorking_equip();
                            String epe_working = batch_index.getEpe_working();
                            String ppe_working = batch_index.getPpe_working();
                            Scene scene = batch_index.getScene();
                            String temp = scene.getTemp();
                            String humidity = scene.getHumidity();
                            String pressure = scene.getPressure();
                            for (String change3_key : change3_key_lis) {
                                String update_key = "batch_gather_lis." + batch_num + ".scene." + change3_key;
                                Scene scene1 = k_v_dic.getBatch_gather_lis().get(batch_num).getScene();
                                Map<String, Object> scenceMap = null;
                                Map<String, Object> scenceMap1 = null;
                                try {
                                    scenceMap = this.convert(scene);
                                    scenceMap1 = this.convert(scene1);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                Object change3_value = scenceMap1.get(change3_key);
                                update.set(update_key, change3_value);
                                if (change3_value != null && !change3_value.equals("")) {
                                    need_range = 1;
                                    need_range_map.put(change3_key, Float.parseFloat(change3_value.toString()));
                                }
                                need_range_point.put(change3_key, change3_value);
                                reduction.set(update_key, scenceMap.get(change3_key));
                            }

                            for (String gather_info : sample_code_lis) {
                                if (item_info.getSample_kb_code_lis().contains(gather_info)) {
                                    GatherLis gatherInfo = batch_index.getGather_kb_map().get(gather_info);
                                    SampleEquip sampleEquip = gatherInfo.getSample_equip();
                                    String before_flow = gatherInfo.getBefore_flow();
                                    String after_flow = gatherInfo.getAfter_flow();
                                    String end_time = gatherInfo.getEnd_time();
                                    String begin_time = gatherInfo.getBegin_time();
                                    Integer test_duration = gatherInfo.getTest_duration();
                                    String name_model_id = sampleEquip.getName_model_id();
                                    String instrument_code = sampleEquip.getInstrument_code();
                                    String calibration_info = sampleEquip.getCalibration_info();
                                    String calibration_name = sampleEquip.getCalibration_name();
                                    String calibration_code = sampleEquip.getCalibration_code();

                                    if (!instrument_code_list.contains(instrument_code)) {
                                        instrument_code_list.add(instrument_code);
                                    }

                                    Map<String, Object> map = new HashMap<>();
                                    map.put("test_duration", test_duration);
                                    map.put("before_flow", before_flow);
                                    map.put("after_flow", after_flow);
                                    map.put("name_model_id", name_model_id);
                                    map.put("instrument_code", instrument_code);
                                    map.put("calibration_info", calibration_info);
                                    map.put("operators_num", operators_num);
                                    map.put("working_equip", working_equip);
                                    map.put("epe_working", epe_working);
                                    map.put("ppe_working", ppe_working);
                                    map.put("gather_date", gather_date);
                                    map.put("work_content", work_content);
                                    map.put("people_num", people_num);
                                    map.put("equip_name", equip_name);
                                    map.put("temp", temp);
                                    map.put("humidity", humidity);
                                    map.put("calibration_code", calibration_code);
                                    map.put("calibration_name", calibration_name);
                                    map.put("pressure", pressure);
                                    sample_info.add(map);
                                } else {
                                    GatherLis gatherInfo = batch_index.getGather_map().get(gather_info);

                                    SampleEquip sampleEquip = gatherInfo.getSample_equip();
                                    String before_flow = gatherInfo.getBefore_flow();
                                    String after_flow = gatherInfo.getAfter_flow();
                                    String end_time = gatherInfo.getEnd_time();
                                    String begin_time = gatherInfo.getBegin_time();
                                    Integer test_duration = gatherInfo.getTest_duration();
                                    String name_model_id = sampleEquip.getName_model_id();
                                    String instrument_code = sampleEquip.getInstrument_code();
                                    String calibration_info = sampleEquip.getCalibration_info();
                                    String calibration_name = sampleEquip.getCalibration_name();
                                    String calibration_code = sampleEquip.getCalibration_code();

                                    if (!instrument_code_list.contains(instrument_code)) {
                                        instrument_code_list.add(instrument_code);
                                    }
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("test_duration", test_duration);
                                    map.put("before_flow", before_flow);
                                    map.put("after_flow", after_flow);
                                    map.put("begin_time", begin_time);
                                    map.put("end_time", end_time);
                                    map.put("name_model_id", name_model_id);
                                    map.put("instrument_code", instrument_code);
                                    map.put("calibration_info", calibration_info);
                                    map.put("operators_num", operators_num);
                                    map.put("working_equip", working_equip);
                                    map.put("epe_working", epe_working);
                                    map.put("ppe_working", ppe_working);
                                    map.put("gather_date", gather_date);
                                    map.put("work_content", work_content);
                                    map.put("people_num", people_num);
                                    map.put("equip_name", equip_name);
                                    map.put("temp", temp);
                                    map.put("humidity", humidity);
                                    map.put("calibration_code", calibration_code);
                                    map.put("calibration_name", calibration_name);
                                    map.put("pressure", pressure);
                                    sample_info.add(map);
                                }
                            }
                        }
                        for (String gather_index : batch_index.getGather_map().keySet()) {
                            for (String change2_key : change2_key_lis) {
                                String update_key = "batch_gather_lis." + batch_num + ".gather_map." + gather_index + ".sample_equip." + change2_key;
                                SampleEquip sample_equip = k_v_dic.getBatch_gather_lis().get(batch_num).getGather_map().get(gather_index).getSample_equip();
                                Map<String, Object> sampleEquipMap = null;
                                try {
                                    sampleEquipMap = this.convert(sample_equip);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                update.set(update_key, sampleEquipMap.get(change2_key));
                                reduction.set(update_key, JSONObject.parseObject(JSON.toJSONString(sample_info.get(gather_num))).get(change2_key));
                            }
                            for (String change_key : change_key_lis) {
                                String update_key = "batch_gather_lis." + batch_num + ".gather_map." + gather_index + "." + change_key;
                                GatherLis gatherLis = k_v_dic.getBatch_gather_lis().get(batch_num).getGather_map().get(gather_index);
                                Map<String, Object> gatherLisMap = null;
                                try {
                                    gatherLisMap = this.convert(gatherLis);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                update.set(update_key, gatherLisMap.get(change_key));
                                reduction.set(update_key, JSONObject.parseObject(JSON.toJSONString(sample_info.get(gather_num))).get(change_key));
                            }
                            gather_num += 1;
                        }
                        batch_num += 1;
                        break;
                    }
                    case 2: {
                        int batch_num = 0;
                        int gather_num = 0;
                        List<String> change_key_lis = Arrays.asList("result1", "result2", "result3", "result_avg", "result_time_avg", "result", "about2");
                        List<String> change2_key_lis = Arrays.asList("begin_time", "end_time");
                        List<String> change3_key_lis = Arrays.asList("name_model_id", "instrument_code", "calibration_info", "calibration_name", "calibration_code");
                        List<String> change5_key_lis = Arrays.asList("temp", "humidity", "pressure");
                        // 对非空白样和空白样列表合并采样不同批次信息分类
                        BatchGatherLis batch_index = null;
                        Operation operation = item_info.getOperation();
                        String work_content = operation.getWork_content();
                        Integer people_num = operation.getPeople_num();
                        String equip_name = operation.getEquip_name();
                        List<Map<String, String>> equip_lis = operation.getEquip_lis();
                        for (BatchGatherLis batchIndex : item_info.getBatch_gather_lis()) {
                            batch_index = batchIndex;
                            String gather_date = batch_index.getGather_date();
                            Integer operators_num = batch_index.getOperators_num();
                            Integer working_equip = batch_index.getWorking_equip();
                            String epe_working = batch_index.getEpe_working();
                            String ppe_working = batch_index.getPpe_working();
                            Integer sample_kb_num = batch_index.getSample_kb_code_lis().size();
                            Scene scene = batch_index.getScene();
                            for (String change5_key : change5_key_lis) {
                                Scene scene1 = k_v_dic.getBatch_gather_lis().get(batch_num).getScene();
                                Map<String, Object> scenceMap = null;
                                Map<String, Object> scenceMap1 = null;
                                try {
                                    scenceMap = this.convert(scene);
                                    scenceMap1 = this.convert(scene1);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                Object change5_value = scenceMap1.get(change5_key);
                                if (change5_value != null && !change5_value.equals("")) {
                                    need_range = 1;
                                    need_range_map.put(change5_key, Float.valueOf(change5_value.toString()));
                                }
                                need_range_point.put(change5_key, change5_value);
                                String update_key = "batch_gather_lis." + batch_num + ".scene." + change5_key;
                                update.set(update_key, change5_value);
                                reduction.set(update_key, scenceMap.get(change5_key));
                            }
                            for (String key : batch_index.getGather_map().keySet()) {
                                GatherLis gatherInfo = batch_index.getGather_map().get(key);
                                SampleEquip sampleEquip = gatherInfo.getSample_equip();
                                String name_model_id = sampleEquip.getName_model_id();
                                String instrument_code = sampleEquip.getInstrument_code();
                                String calibration_info = sampleEquip.getCalibration_info();
                                String calibration_name = sampleEquip.getCalibration_name();
                                String calibration_code = sampleEquip.getCalibration_code();
                                may.yuntian.jianping.mongodto.Result resultAll = gatherInfo.getResult();
                                String result1 = resultAll.getResult1();
                                String result2 = resultAll.getResult2();
                                String result3 = resultAll.getResult3();
                                String begin_time = gatherInfo.getBegin_time();
                                String end_time = gatherInfo.getEnd_time();
                                String result_avg = resultAll.getResult_avg();
                                String test_place = resultAll.getAbout2();
                                String result_time_avg = resultAll.getResult_time_avg();
                                String result = resultAll.getResult();
                                Map<String, Object> map = new HashMap<>();
                                map.put("end_time", end_time);
                                map.put("begin_time", begin_time);
                                map.put("about2", test_place);
                                map.put("result1", result1);
                                map.put("result2", result2);
                                map.put("result3", result3);
                                map.put("result_avg", result_avg);
                                map.put("result_time_avg", result_time_avg);
                                map.put("resultAll", result);
                                map.put("operators_num", operators_num);
                                map.put("working_equip", working_equip);
                                map.put("epe_working", epe_working);
                                map.put("ppe_working", ppe_working);
                                map.put("gather_date", gather_date);
                                map.put("work_content", work_content);
                                map.put("people_num", people_num);
                                map.put("equip_name", equip_name);
                                map.put("calibration_code", calibration_code);
                                map.put("calibration_name", calibration_name);
                                map.put("instrument_code", instrument_code);
                                map.put("calibration_info", calibration_info);
                                map.put("name_model_id", name_model_id);
                                sample_info.add(map);
                            }
                            for (String gather_index : batch_index.getGather_map().keySet()) {
                                for (String change_key : change_key_lis) {
                                    String update_key = "batch_gather_lis." + batch_num + ".gather_map." + gather_index + ".result." + change_key;
                                    may.yuntian.jianping.mongodto.Result result = k_v_dic.getBatch_gather_lis().get(batch_num).getGather_map().get(gather_index).getResult();
                                    Map<String, Object> resultMap = null;
                                    try {
                                        resultMap = this.convert(result);
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                    update.set(update_key, resultMap.get(change_key));
                                    reduction.set(update_key, JSONObject.parseObject(JSON.toJSONString(sample_info.get(gather_num))).get(change_key));
                                }
                                for (String change2_key : change2_key_lis) {
                                    String update_key = "batch_gather_lis." + batch_num + ".gather_map." + gather_index + "." + change2_key;
                                    GatherLis gatherLis = k_v_dic.getBatch_gather_lis().get(batch_num).getGather_map().get(gather_index);
                                    Map<String, Object> gatherLisMap = null;
                                    try {
                                        gatherLisMap = this.convert(gatherLis);
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                    update.set(update_key, gatherLisMap.get(change2_key));
                                    reduction.set(update_key, JSONObject.parseObject(JSON.toJSONString(sample_info.get(gather_num))).get(change2_key));
                                }
                                for (String change3_key : change3_key_lis) {
                                    String update_key = "batch_gather_lis." + batch_num + ".gather_map." + gather_index + ".sample_equip." + change3_key;
                                    SampleEquip sample_equip = k_v_dic.getBatch_gather_lis().get(batch_num).getGather_map().get(gather_index).getSample_equip();
                                    Map<String, Object> sampleEquipMap = null;
                                    try {
                                        sampleEquipMap = this.convert(sample_equip);
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                    update.set(update_key, sampleEquipMap.get(change3_key));
                                    reduction.set(update_key, JSONObject.parseObject(JSON.toJSONString(sample_info.get(gather_num))).get(change3_key));
                                }
                                gather_num += 1;
                            }
                        }
                        batch_num += 1;
                        break;
                    }
                    case 3: {
                        // 3：噪声个体  测量时间应该是begin_time(采样开始时间)  持续时间应该是测试时长
                        int gather_num = 0;
                        int batch_num = 0;
                        List<String> change_key_lis = Arrays.asList("result_time_avg", "result");
                        List<String> change2_key_lis = Arrays.asList("name_model_id", "instrument_code", "calibration_info", "calib_value", "calibration_name", "calibration_code");
                        List<String> change3_key_lis = Arrays.asList("people", "begin_time", "end_time");
                        List<String> change6_key_lis = Arrays.asList("temp", "humidity", "pressure");
                        BatchGatherLis batch_index = null;
                        Operation operation = item_info.getOperation();
                        String work_content = operation.getWork_content();
                        Integer people_num = operation.getPeople_num();
                        String equip_name = operation.getEquip_name();
                        List<Map<String, String>> equip_lis = operation.getEquip_lis();
                        for (BatchGatherLis batchIndex : item_info.getBatch_gather_lis()) {
                            batch_index = batchIndex;
                            Scene scene = batch_index.getScene();
                            String temp = scene.getTemp();
                            String humidity = scene.getHumidity();
                            String pressure = scene.getPressure();
                            Integer operators_num = batch_index.getOperators_num();
                            Integer working_equip = batch_index.getWorking_equip();
                            String epe_working = batch_index.getEpe_working();
                            String ppe_working = batch_index.getPpe_working();
                            for (String change6_key : change6_key_lis) {
                                String update_key = "batch_gather_lis." + batch_num + ".scene." + change6_key;
                                Scene scene1 = k_v_dic.getBatch_gather_lis().get(batch_num).getScene();
                                Map<String, Object> scenceMap = null;
                                Map<String, Object> scenceMap1 = null;
                                try {
                                    scenceMap = this.convert(scene);
                                    scenceMap1 = this.convert(scene1);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                Object change6_value = scenceMap1.get(change6_key);
                                if (change6_value != null && !change6_value.equals("")) {
                                    need_range = 1;
                                    need_range_map.put(change6_key, Float.valueOf(change6_value.toString()));
                                }
                                update.set(update_key, change6_value);
                                need_range_point.put(change6_key, change6_value);
                                reduction.set(update_key, scenceMap.get(change6_key));
                            }
                            for (String key : batch_index.getGather_map().keySet()) {
                                GatherLis gatherInfo = batch_index.getGather_map().get(key);
                                SampleEquip sampleEquip = gatherInfo.getSample_equip();
                                String name_model_id = sampleEquip.getName_model_id();
                                String instrument_code = sampleEquip.getInstrument_code();
                                String calibration_info = sampleEquip.getCalibration_info();
                                String calibration_name = sampleEquip.getCalibration_name();
                                String calibration_code = sampleEquip.getCalibration_code();
                                String calib_value = sampleEquip.getCalib_value();
                                if (!instrument_code_list.contains(instrument_code)) {
                                    instrument_code_list.add(instrument_code);
                                }
                                String end_time = gatherInfo.getEnd_time();
                                String begin_time = gatherInfo.getBegin_time();
                                String people = gatherInfo.getPeople();
                                may.yuntian.jianping.mongodto.Result resultAll = gatherInfo.getResult();
                                String test_place = resultAll.getAbout2();
                                String result_time_avg = resultAll.getResult_time_avg();
                                String result = resultAll.getResult();
                                Map<String, Object> map = new HashMap<>();
                                map.put("end_time", end_time);
                                map.put("begin_time", begin_time);
                                map.put("about2", test_place);
                                map.put("people", people);
                                map.put("name_model_id", name_model_id);
                                map.put("temp", temp);
                                map.put("pressure", pressure);
                                map.put("operators_num", operators_num);
                                map.put("result_time_avg", result_time_avg);
                                map.put("result", result);
                                map.put("instrument_code", instrument_code);
                                map.put("calibration_info", calibration_info);
                                map.put("calib_value", calib_value);
                                map.put("working_equip", working_equip);
                                map.put("epe_working", epe_working);
                                map.put("ppe_working", ppe_working);
                                map.put("work_content", work_content);
                                map.put("people_num", people_num);
                                map.put("calibration_code", calibration_code);
                                map.put("calibration_name", calibration_name);
                                map.put("equip_name", equip_name);
                                sample_info.add(map);
                            }
                            for (String gather_index : batch_index.getGather_map().keySet()) {
                                for (String change_key : change_key_lis) {
                                    String update_key = "batch_gather_lis." + batch_num + ".gather_map." + gather_index + ".result." + change_key;
                                    may.yuntian.jianping.mongodto.Result result = k_v_dic.getBatch_gather_lis().get(batch_num).getGather_map().get(gather_index).getResult();
                                    Map<String, Object> resultMap = null;
                                    try {
                                        resultMap = this.convert(result);
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                    update.set(update_key, resultMap.get(change_key));
                                    reduction.set(update_key, JSONObject.parseObject(JSON.toJSONString(sample_info.get(gather_num))).get(change_key));
                                }
                                for (String change2_key : change2_key_lis) {
                                    String update_key = "batch_gather_lis." + batch_num + ".gather_map." + gather_index + ".sample_equip." + change2_key;
                                    SampleEquip sample_equip = k_v_dic.getBatch_gather_lis().get(batch_num).getGather_map().get(gather_index).getSample_equip();
                                    Map<String, Object> sampleEquipMap = null;
                                    try {
                                        sampleEquipMap = this.convert(sample_equip);
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                    update.set(update_key, sampleEquipMap.get(change2_key));
                                    reduction.set(update_key, JSONObject.parseObject(JSON.toJSONString(sample_info.get(gather_num))).get(change2_key));
                                }
                                for (String change3_key : change3_key_lis) {
                                    String update_key = "batch_gather_lis." + batch_num + ".gather_map." + gather_index + "." + change3_key;
                                    GatherLis gatherLis = k_v_dic.getBatch_gather_lis().get(batch_num).getGather_map().get(gather_index);
                                    Map<String, Object> gatherLisMap = null;
                                    try {
                                        gatherLisMap = this.convert(gatherLis);
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                    update.set(update_key, gatherLisMap.get(change3_key));
                                    reduction.set(update_key, JSONObject.parseObject(JSON.toJSONString(sample_info.get(gather_num))).get(change3_key));
                                }
                            }
                            gather_num += 1;
                        }
                        batch_num += 1;
                        break;
                    }
                    case 4: {
                        // 4：高温
                        int batch_num = 0;
                        int gather_num = 0;
                        List<Object> test_frame_union_lis = new ArrayList<>();
                        List<String> change_key_lis = Arrays.asList("result", "result_time_avg", "result_avg", "about1", "result1", "result2", "result3");
                        List<String> change2_key_lis = Arrays.asList("begin_time", "end_time");
                        List<String> change6_key_lis = Arrays.asList("name_model_id", "instrument_code");
                        List<Object> change3_key_lis = new ArrayList<>();
                        List<Object> change4_key_lis = new ArrayList<>();
                        List<String> change5_key_lis = Arrays.asList("temp", "humidity", "pressure");
                        BatchGatherLis batch_index = null;
                        Operation operation = item_info.getOperation();
                        String work_content = operation.getWork_content();
                        Integer people_num = operation.getPeople_num();
                        String equip_name = operation.getEquip_name();
                        List<Map<String, String>> equip_lis = operation.getEquip_lis();
                        for (BatchGatherLis batchIndex : item_info.getBatch_gather_lis()) {
                            batch_index = batchIndex;
                            Scene scene = batch_index.getScene();
                            String temp = scene.getTemp();
                            String humidity = scene.getHumidity();
                            String pressure = scene.getPressure();
                            Integer operators_num = batch_index.getOperators_num();
                            Integer working_equip = batch_index.getWorking_equip();
                            String epe_working = batch_index.getEpe_working();
                            String ppe_working = batch_index.getPpe_working();
                            Integer sample_kb_num = batch_index.getSample_kb_code_lis().size();
                            for (String change5_key : change5_key_lis) {
                                Scene scene1 = k_v_dic.getBatch_gather_lis().get(batch_num).getScene();
                                Map<String, Object> scenceMap1 = null;
                                try {
                                    scenceMap1 = this.convert(scene1);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                Object change5_value = scenceMap1.get(change5_key);
                                if (change5_value != null && !change5_value.equals("")) {
                                    need_range = 1;
                                    need_range_map.put(change5_key, Float.parseFloat(change5_value.toString()));
                                }
                                need_range_point.put(change5_key, change5_value);
                            }

                            for (String key : batch_index.getGather_map().keySet()) {
                                GatherLis gatherInfo = batch_index.getGather_map().get(key);
                                SampleEquip sampleEquip = gatherInfo.getSample_equip();
                                String name_model_id = sampleEquip.getName_model_id();
                                String instrument_code = sampleEquip.getInstrument_code();
                                if (!instrument_code_list.contains(instrument_code)) {
                                    instrument_code_list.add(instrument_code);
                                }
                                may.yuntian.jianping.mongodto.Result resultAll = gatherInfo.getResult();
                                String labour_level = resultAll.getAbout1();
                                String result_time_avg = resultAll.getResult_time_avg();
                                String result_avg = resultAll.getResult_avg();
                                String result = resultAll.getResult();
                                String result1 = resultAll.getResult1();
                                String result2 = resultAll.getResult2();
                                String result3 = resultAll.getResult3();
                                String begin_time = gatherInfo.getBegin_time();
                                String end_time = gatherInfo.getEnd_time();
                                Map<String, Object> map = new HashMap<>();
                                map.put("work_content", work_content);
                                map.put("people_num", people_num);
                                map.put("equip_name", equip_name);
                                map.put("operators_num", operators_num);
                                map.put("working_equip", working_equip);
                                map.put("result_time_avg", result_time_avg);
                                map.put("result_avg", result_avg);
                                map.put("epe_working", epe_working);
                                map.put("ppe_working", ppe_working);
                                map.put("temp", temp);
                                map.put("humidity", humidity);
                                map.put("pressure", pressure);
                                map.put("labour_level", labour_level);
                                map.put("instrument_code", instrument_code);
                                map.put("name_model_id", name_model_id);
                                map.put("result", result);
                                map.put("result1", result1);
                                map.put("result2", result2);
                                map.put("result3", result3);
                                map.put("begin_time", begin_time);
                                map.put("end_time", end_time);
                                sample_info.add(map);
                            }
                            update_info_pc1(batch_index, change_key_lis, change2_key_lis, change5_key_lis, change6_key_lis, batch_num, update, reduction, k_v_dic, sample_info);
                        }
                        batch_num += 1;
                        break;
                    }
                    case 5: {
                        // 5：紫外    测量时间应该是begin_time(采样开始时间)
                        int batch_num = 0;
                        List<String> change_key_lis = Arrays.asList("result1", "result4", "result2", "result5", "result3", "result6", "result_avg",
                                "fruit1", "fruit4", "fruit2", "fruit5", "fruit3", "fruit6", "fruit_avg", "about1");
                        List<String> change2_key_lis = Arrays.asList("begin_time", "end_time");
                        List<String> change5_key_lis = Arrays.asList("temp", "humidity", "pressure");
                        List<String> change6_key_lis = Arrays.asList("name_model_id", "instrument_code", "name_model_id1", "instrument_code1");
                        BatchGatherLis batch_index = null;
                        Operation operation = item_info.getOperation();
                        String work_content = operation.getWork_content();
                        Integer people_num = operation.getPeople_num();
                        String equip_name = operation.getEquip_name();
                        List<Map<String, String>> equip_lis = operation.getEquip_lis();

                        for (BatchGatherLis batchIndex : item_info.getBatch_gather_lis()) {
                            batch_index = batchIndex;
                            Scene scene = batch_index.getScene();
                            String temp = scene.getTemp();
                            String humidity = scene.getHumidity();
                            String pressure = scene.getPressure();
                            Integer operators_num = batch_index.getOperators_num();
                            Integer working_equip = batch_index.getWorking_equip();
                            String epe_working = batch_index.getEpe_working();
                            String ppe_working = batch_index.getPpe_working();
                            Integer sample_kb_num = batch_index.getSample_kb_code_lis().size();
                            for (String change5_key : change5_key_lis) {
                                Scene scene1 = k_v_dic.getBatch_gather_lis().get(batch_num).getScene();
                                Map<String, Object> scenceMap1 = null;
                                try {
                                    scenceMap1 = this.convert(scene1);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                Object change5_value = scenceMap1.get(change5_key);
                                if (change5_value != null && !change5_value.equals("")) {
                                    need_range = 1;
                                    need_range_map.put(change5_key, Float.parseFloat(change5_value.toString()));
                                }
                                need_range_point.put(change5_key, change5_value);
                            }
                            for (String key : batch_index.getGather_map().keySet()) {
                                GatherLis gatherInfo = batch_index.getGather_map().get(key);
                                Integer test_duration = gatherInfo.getTest_duration();
                                String begin_time = gatherInfo.getBegin_time();
                                String end_time = gatherInfo.getEnd_time();
                                String test_frame = gatherInfo.getTime_frame();
                                SampleEquip sampleEquip = gatherInfo.getSample_equip();
                                String name_model_id = sampleEquip.getName_model_id();
                                String correct_factor = sampleEquip.getCorrect_factor();
                                String instrument_code = sampleEquip.getInstrument_code();
                                String name_model_id1 = sampleEquip.getName_model_id1();
                                String instrument_code1 = sampleEquip.getInstrument_code1();
                                if (!instrument_code_list.contains(instrument_code)) {
                                    instrument_code_list.add(instrument_code);
                                }
                                may.yuntian.jianping.mongodto.Result resultAll = gatherInfo.getResult();
                                String result1 = resultAll.getResult1();
                                String result2 = resultAll.getResult2();
                                String result3 = resultAll.getResult3();
                                String fruit1 = resultAll.getFruit1();
                                String fruit2 = resultAll.getFruit2();
                                String fruit3 = resultAll.getFruit3();
                                String result4 = resultAll.getResult4();
                                String result5 = resultAll.getResult5();
                                String result6 = resultAll.getResult6();
                                String fruit4 = resultAll.getFruit4();
                                String fruit5 = resultAll.getFruit5();
                                String fruit6 = resultAll.getFruit6();
                                String result_avg = resultAll.getResult_avg();
                                String fruit_avg = resultAll.getFruit_avg();
                                String about1 = resultAll.getAbout1();
                                Map<String, Object> map = new HashMap<>();
                                map.put("test_duration", test_duration);
                                map.put("test_frame", test_frame);
                                map.put("result1", result1);
                                map.put("result2", result2);
                                map.put("result3", result3);
                                map.put("result4", result4);
                                map.put("result5", result5);
                                map.put("result6", result6);
                                map.put("result_avg", result_avg);
                                map.put("fruit1", fruit1);
                                map.put("fruit2", fruit2);
                                map.put("fruit3", fruit3);
                                map.put("fruit4", fruit4);
                                map.put("fruit5", fruit5);
                                map.put("fruit6", fruit6);
                                map.put("fruit_avg", fruit_avg);
                                map.put("about1", about1);
                                map.put("correct_factor", correct_factor);
                                map.put("temp", temp);
                                map.put("humidity", humidity);
                                map.put("pressure", pressure);
                                map.put("operators_num", operators_num);
                                map.put("instrument_code", instrument_code);
                                map.put("name_model_id", name_model_id);
                                map.put("instrument_code1", instrument_code1);
                                map.put("name_model_id1", name_model_id1);
                                map.put("working_equip", working_equip);
                                map.put("epe_working", epe_working);
                                map.put("ppe_working", ppe_working);
                                map.put("work_content", work_content);
                                map.put("people_num", people_num);
                                map.put("equip_name", equip_name);
                                map.put("begin_time", begin_time);
                                map.put("end_time", end_time);
                                sample_info.add(map);
                            }
                            update_info_pc1(batch_index, change_key_lis, change2_key_lis, change5_key_lis, change6_key_lis, batch_num, update, reduction, k_v_dic, sample_info);
                        }
                        batch_num += 1;
                        break;
                    }
                    case 6: {
                        // 6：co
                        int batch_num = 0;
                        List<String> change_key_lis = Arrays.asList("result", "result1", "result2", "result3", "result_avg");
                        List<String> change2_key_lis = Arrays.asList("begin_time", "end_time");
                        List<String> change5_key_lis = Arrays.asList("temp", "humidity", "pressure");
                        List<String> change6_key_lis = Arrays.asList("name_model_id", "instrument_code");
                        BatchGatherLis batch_index = null;
                        Operation operation = item_info.getOperation();
                        String work_content = operation.getWork_content();
                        Integer people_num = operation.getPeople_num();
                        String equip_name = operation.getEquip_name();
                        List<Map<String, String>> equip_lis = operation.getEquip_lis();
                        for (BatchGatherLis batchIndex : item_info.getBatch_gather_lis()) {
                            batch_index = batchIndex;
                            Scene scene = batch_index.getScene();
                            String temp = scene.getTemp();
                            String humidity = scene.getHumidity();
                            String pressure = scene.getPressure();
                            Integer operators_num = batch_index.getOperators_num();
                            Integer working_equip = batch_index.getWorking_equip();
                            String epe_working = batch_index.getEpe_working();
                            String ppe_working = batch_index.getPpe_working();
                            Integer sample_kb_num = batch_index.getSample_kb_code_lis().size();
                            for (String change5_key : change5_key_lis) {
                                Scene scene1 = k_v_dic.getBatch_gather_lis().get(batch_num).getScene();
                                Map<String, Object> scenceMap1 = null;
                                try {
                                    scenceMap1 = this.convert(scene1);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                Object change5_value = scenceMap1.get(change5_key);
                                if (change5_value != null && !change5_value.equals("")) {
                                    need_range = 1;
                                    need_range_map.put(change5_key, Float.parseFloat(change5_value.toString()));
                                }
                                need_range_point.put(change5_key, change5_value);
                            }
                            for (String key : batch_index.getGather_map().keySet()) {
                                GatherLis gatherInfo = batch_index.getGather_map().get(key);
                                Integer test_duration = gatherInfo.getTest_duration();
                                String begin_time = gatherInfo.getBegin_time();
                                String end_time = gatherInfo.getEnd_time();
                                String test_frame = gatherInfo.getTime_frame();
                                SampleEquip sampleEquip = gatherInfo.getSample_equip();
                                String name_model_id = sampleEquip.getName_model_id();
                                String instrument_code = sampleEquip.getInstrument_code();
                                if (!instrument_code_list.contains(instrument_code)) {
                                    instrument_code_list.add(instrument_code);
                                }
                                may.yuntian.jianping.mongodto.Result resultAll = gatherInfo.getResult();
                                String result1 = resultAll.getResult1();
                                String result2 = resultAll.getResult2();
                                String result3 = resultAll.getResult3();
                                String result_avg = resultAll.getResult_avg();
                                String result = resultAll.getResult();
                                Map<String, Object> map = new HashMap<>();
                                map.put("instrument_code", instrument_code);
                                map.put("name_model_id", name_model_id);
                                map.put("pressure", pressure);
                                map.put("temp", temp);
                                map.put("humidity", humidity);
                                map.put("operators_num", operators_num);
                                map.put("working_equip", working_equip);
                                map.put("epe_working", epe_working);
                                map.put("ppe_working", ppe_working);
                                map.put("work_content", work_content);
                                map.put("people_num", people_num);
                                map.put("equip_name", equip_name);
                                map.put("result", result);
                                map.put("result1", result1);
                                map.put("result2", result2);
                                map.put("result3", result3);
                                map.put("result_avg", result_avg);
                                map.put("begin_time", begin_time);
                                map.put("end_time", end_time);
                                sample_info.add(map);
                            }
                            update_info_pc1(batch_index, change_key_lis, change2_key_lis, change5_key_lis, change6_key_lis, batch_num, update, reduction, k_v_dic, sample_info);
                        }
                        batch_num += 1;
                        break;
                    }
                    case 7: {
                        // 7：co2
                        int batch_num = 0;
                        List<String> change_key_lis = Arrays.asList("result", "result1", "result2", "result3", "result_avg");
                        List<String> change2_key_lis = Arrays.asList("begin_time", "end_time");
                        List<String> change5_key_lis = Arrays.asList("temp", "humidity", "pressure");
                        List<String> change6_key_lis = Arrays.asList("name_model_id", "instrument_code");
                        BatchGatherLis batch_index = null;
                        Operation operation = item_info.getOperation();
                        String work_content = operation.getWork_content();
                        Integer people_num = operation.getPeople_num();
                        String equip_name = operation.getEquip_name();
                        List<Map<String, String>> equip_lis = operation.getEquip_lis();
                        for (BatchGatherLis batchIndex : item_info.getBatch_gather_lis()) {
                            batch_index = batchIndex;
                            Scene scene = batch_index.getScene();
                            String temp = scene.getTemp();
                            String humidity = scene.getHumidity();
                            String pressure = scene.getPressure();
                            Integer operators_num = batch_index.getOperators_num();
                            Integer working_equip = batch_index.getWorking_equip();
                            String epe_working = batch_index.getEpe_working();
                            String ppe_working = batch_index.getPpe_working();
                            Integer sample_kb_num = batch_index.getSample_kb_code_lis().size();
                            for (String change5_key : change5_key_lis) {
                                Scene scene1 = k_v_dic.getBatch_gather_lis().get(batch_num).getScene();
                                Map<String, Object> scenceMap1 = null;
                                try {
                                    scenceMap1 = this.convert(scene1);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                Object change5_value = scenceMap1.get(change5_key);
                                if (change5_value != null && !change5_value.equals("")) {
                                    need_range = 1;
                                    need_range_map.put(change5_key, Float.parseFloat(change5_value.toString()));
                                }
                                need_range_point.put(change5_key, change5_value);
                            }
                            for (String key : batch_index.getGather_map().keySet()) {
                                GatherLis gatherInfo = batch_index.getGather_map().get(key);
                                Integer test_duration = gatherInfo.getTest_duration();
                                String begin_time = gatherInfo.getBegin_time();
                                String end_time = gatherInfo.getEnd_time();
                                String time_frame = gatherInfo.getTime_frame();
                                SampleEquip sampleEquip = gatherInfo.getSample_equip();
                                String name_model_id = sampleEquip.getName_model_id();
                                String instrument_code = sampleEquip.getInstrument_code();
                                if (!instrument_code_list.contains(instrument_code)) {
                                    instrument_code_list.add(instrument_code);
                                }
                                may.yuntian.jianping.mongodto.Result resultAll = gatherInfo.getResult();
                                String result1 = resultAll.getResult1();
                                String result2 = resultAll.getResult2();
                                String result3 = resultAll.getResult3();
                                String result_avg = resultAll.getResult_avg();
                                String result = resultAll.getResult();
                                Map<String, Object> map = new HashMap<>();
                                map.put("instrument_code", instrument_code);
                                map.put("name_model_id", name_model_id);
                                map.put("pressure", pressure);
                                map.put("temp", temp);
                                map.put("humidity", humidity);
                                map.put("operators_num", operators_num);
                                map.put("working_equip", working_equip);
                                map.put("epe_working", epe_working);
                                map.put("ppe_working", ppe_working);
                                map.put("work_content", work_content);
                                map.put("people_num", people_num);
                                map.put("equip_name", equip_name);
                                map.put("result", result);
                                map.put("result1", result1);
                                map.put("result2", result2);
                                map.put("result3", result3);
                                map.put("result_avg", result_avg);
                                map.put("begin_time", begin_time);
                                map.put("end_time", end_time);
                                sample_info.add(map);
                            }
                            update_info_pc1(batch_index, change_key_lis, change2_key_lis, change5_key_lis, change6_key_lis, batch_num, update, reduction, k_v_dic, sample_info);
                        }
                        batch_num += 1;
                        break;
                    }
                    case 8: {
                        // 8：高频电磁场
                        int batch_num = 0;
                        List<String> change_key_lis = Arrays.asList("about1", "about2", "result", "result1", "result2", "result3", "result_avg",
                                "fruit1", "fruit2", "fruit3", "fruit_avg");
                        List<String> change2_key_lis = Arrays.asList("begin_time", "end_time");
                        List<String> change5_key_lis = Arrays.asList("temp", "humidity", "pressure");
                        List<String> change6_key_lis = Arrays.asList("name_model_id", "instrument_code");
                        BatchGatherLis batch_index = null;
                        Operation operation = item_info.getOperation();
                        String work_content = operation.getWork_content();
                        Integer people_num = operation.getPeople_num();
                        String equip_name = operation.getEquip_name();
                        List<Map<String, String>> equip_lis = operation.getEquip_lis();
                        for (BatchGatherLis batchIndex : item_info.getBatch_gather_lis()) {
                            batch_index = batchIndex;
                            Scene scene = batch_index.getScene();
                            String temp = scene.getTemp();
                            String humidity = scene.getHumidity();
                            String pressure = scene.getPressure();
                            Integer operators_num = batch_index.getOperators_num();
                            Integer working_equip = batch_index.getWorking_equip();
                            String epe_working = batch_index.getEpe_working();
                            String ppe_working = batch_index.getPpe_working();
                            Integer sample_kb_num = batch_index.getSample_kb_code_lis().size();
                            for (String change5_key : change5_key_lis) {
                                Scene scene1 = k_v_dic.getBatch_gather_lis().get(batch_num).getScene();
                                Map<String, Object> scenceMap1 = null;
                                try {
                                    scenceMap1 = this.convert(scene1);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                Object change5_value = scenceMap1.get(change5_key);
                                if (change5_value != null && !change5_value.equals("")) {
                                    need_range = 1;
                                    need_range_map.put(change5_key, Float.parseFloat(change5_value.toString()));
                                }
                                need_range_point.put(change5_key, change5_value);
                            }
                            for (String key : batch_index.getGather_map().keySet()) {
                                GatherLis gatherInfo = batch_index.getGather_map().get(key);
                                Integer test_duration = gatherInfo.getTest_duration();
                                String begin_time = gatherInfo.getBegin_time();
                                String end_time = gatherInfo.getEnd_time();
                                String time_frame = gatherInfo.getTime_frame();
                                SampleEquip sampleEquip = gatherInfo.getSample_equip();
                                String name_model_id = sampleEquip.getName_model_id();
                                String instrument_code = sampleEquip.getInstrument_code();
                                String correct_factor = sampleEquip.getCorrect_factor();
                                if (!instrument_code_list.contains(instrument_code)) {
                                    instrument_code_list.add(instrument_code);
                                }
                                may.yuntian.jianping.mongodto.Result resultAll = gatherInfo.getResult();
                                String result1 = resultAll.getResult1();
                                String result2 = resultAll.getResult2();
                                String result3 = resultAll.getResult3();
                                String fruit1 = resultAll.getFruit1();
                                String fruit2 = resultAll.getFruit2();
                                String fruit3 = resultAll.getFruit3();
                                String result_avg = resultAll.getResult_avg();
                                String fruit_avg = resultAll.getFruit_avg();
                                String result = resultAll.getResult();
                                String equipment = resultAll.getAbout1();
                                String about2 = resultAll.getAbout2();
                                Map<String, Object> map = new HashMap<>();
                                map.put("name_model_id", name_model_id);
                                map.put("temp", temp);
                                map.put("humidity", humidity);
                                map.put("pressure", pressure);
                                map.put("operators_num", operators_num);
                                map.put("working_equip", working_equip);
                                map.put("epe_working", epe_working);
                                map.put("ppe_working", ppe_working);
                                map.put("work_content", work_content);
                                map.put("people_num", people_num);
                                map.put("equip_name", equip_name);
                                map.put("about1", equipment);
                                map.put("about2", about2);
                                map.put("instrument_code", instrument_code);
                                map.put("result", result);
                                map.put("result1", result1);
                                map.put("result2", result2);
                                map.put("result3", result3);
                                map.put("result_avg", result_avg);
                                map.put("fruit1", fruit1);
                                map.put("fruit2", fruit2);
                                map.put("fruit3", fruit3);
                                map.put("fruit_avg", fruit_avg);
                                map.put("begin_time", begin_time);
                                map.put("end_time", end_time);
                                sample_info.add(map);
                            }
                            update_info_pc1(batch_index, change_key_lis, change2_key_lis, change5_key_lis, change6_key_lis, batch_num, update, reduction, k_v_dic, sample_info);
                        }
                        batch_num += 1;
                        break;
                    }
                    case 9: {
                        //  9:工频电场
                        int batch_num = 0;
                        List<String> change_key_lis = Arrays.asList("result1", "result2", "result3", "result4", "result5", "result6", "result7", "result8", "result9",
                                "fruit1", "fruit2", "fruit3", "fruit4", "fruit5", "fruit6", "fruit7", "fruit8", "fruit9", "fruit_avg1", "fruit_avg2", "fruit_avg3", "about1");
                        List<String> change2_key_lis = Arrays.asList("begin_time", "end_time", "contact_duration");
                        List<String> change5_key_lis = Arrays.asList("temp", "humidity", "pressure");
                        List<String> change6_key_lis = Arrays.asList("correct_factor", "instrument_code", "name_model_id");
                        BatchGatherLis batch_index = null;
                        Operation operation = item_info.getOperation();
                        String work_content = operation.getWork_content();
                        Integer people_num = operation.getPeople_num();
                        String equip_name = operation.getEquip_name();
                        List<Map<String, String>> equip_lis = operation.getEquip_lis();
                        for (BatchGatherLis batchIndex : item_info.getBatch_gather_lis()) {
                            batch_index = batchIndex;
                            Scene scene = batch_index.getScene();
                            String temp = scene.getTemp();
                            String humidity = scene.getHumidity();
                            String pressure = scene.getPressure();
                            Integer operators_num = batch_index.getOperators_num();
                            Integer working_equip = batch_index.getWorking_equip();
                            String epe_working = batch_index.getEpe_working();
                            String ppe_working = batch_index.getPpe_working();
                            Integer sample_kb_num = batch_index.getSample_kb_code_lis().size();
                            for (String change5_key : change5_key_lis) {
                                Scene scene1 = k_v_dic.getBatch_gather_lis().get(batch_num).getScene();
                                Map<String, Object> scenceMap1 = null;
                                try {
                                    scenceMap1 = this.convert(scene1);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                Object change5_value = scenceMap1.get(change5_key);
                                if (change5_value != null && !change5_value.equals("")) {
                                    need_range = 1;
                                    need_range_map.put(change5_key, Float.parseFloat(change5_value.toString()));
                                }
                                need_range_point.put(change5_key, change5_value);
                            }
                            for (String key : batch_index.getGather_map().keySet()) {
                                GatherLis gatherInfo = batch_index.getGather_map().get(key);
                                Integer test_duration = gatherInfo.getTest_duration();
                                String begin_time = gatherInfo.getBegin_time();
                                String end_time = gatherInfo.getEnd_time();
                                String time_frame = gatherInfo.getTime_frame();
                                String contact_duration = gatherInfo.getContact_duration();
                                SampleEquip sampleEquip = gatherInfo.getSample_equip();
                                String name_model_id = sampleEquip.getName_model_id();
                                String instrument_code = sampleEquip.getInstrument_code();
                                String correct_factor = sampleEquip.getCorrect_factor();
                                if (!instrument_code_list.contains(instrument_code)) {
                                    instrument_code_list.add(instrument_code);
                                }
                                may.yuntian.jianping.mongodto.Result resultAll = gatherInfo.getResult();
                                String result1 = resultAll.getResult1();
                                String result2 = resultAll.getResult2();
                                String result3 = resultAll.getResult3();
                                String result4 = resultAll.getResult4();
                                String result5 = resultAll.getResult5();
                                String result6 = resultAll.getResult6();
                                String result7 = resultAll.getResult7();
                                String result8 = resultAll.getResult8();
                                String result9 = resultAll.getResult9();
                                String fruit_avg1 = resultAll.getFruit_avg1();
                                String fruit_avg2 = resultAll.getFruit_avg2();
                                String fruit_avg3 = resultAll.getFruit_avg3();
                                String fruit1 = resultAll.getFruit1();
                                String fruit2 = resultAll.getFruit2();
                                String fruit3 = resultAll.getFruit3();
                                String fruit4 = resultAll.getFruit4();
                                String fruit5 = resultAll.getFruit5();
                                String fruit6 = resultAll.getFruit6();
                                String fruit7 = resultAll.getFruit7();
                                String fruit8 = resultAll.getFruit8();
                                String fruit9 = resultAll.getFruit9();
                                String about1 = resultAll.getAbout1();
                                Map<String, Object> map = new HashMap<>();
                                map.put("result1", result1);
                                map.put("result2", result2);
                                map.put("result3", result3);
                                map.put("result4", result4);
                                map.put("result5", result5);
                                map.put("result6", result6);
                                map.put("result7", result7);
                                map.put("result8", result8);
                                map.put("result9", result9);
                                map.put("fruit1", fruit1);
                                map.put("fruit2", fruit2);
                                map.put("fruit3", fruit3);
                                map.put("fruit4", fruit4);
                                map.put("fruit5", fruit5);
                                map.put("fruit6", fruit6);
                                map.put("fruit7", fruit7);
                                map.put("fruit8", fruit8);
                                map.put("fruit9", fruit9);
                                map.put("fruit_avg1", fruit_avg1);
                                map.put("fruit_avg2", fruit_avg2);
                                map.put("fruit_avg3", fruit_avg3);
                                map.put("instrument_code", instrument_code);
                                map.put("name_model_id", name_model_id);
                                map.put("correct_factor", correct_factor);
                                map.put("about1", about1);
                                map.put("begin_time", begin_time);
                                map.put("end_time", end_time);
                                map.put("contact_duration", contact_duration);
                                map.put("temp", temp);
                                map.put("humidity", humidity);
                                map.put("pressure", pressure);
                                map.put("operators_num", operators_num);
                                map.put("working_equip", working_equip);
                                map.put("epe_working", epe_working);
                                map.put("ppe_working", ppe_working);
                                map.put("work_content", work_content);
                                map.put("people_num", people_num);
                                map.put("equip_name", equip_name);
                                sample_info.add(map);
                            }
                            update_info_pc1(batch_index, change_key_lis, change2_key_lis, change5_key_lis, change6_key_lis, batch_num, update, reduction, k_v_dic, sample_info);
                        }
                        batch_num += 1;
                        break;
                    }
                    case 10: {
                        // 10：手传振动 x/y/z 3个方向的值为 result1 result2 result3  测量时间为 fruit1  fruit2  fruit3
                        int batch_num = 0;
                        List<String> change_key_lis = Arrays.asList("result1", "result2", "result3", "result", "about1", "about2");
                        List<String> change2_key_lis = Arrays.asList("begin_time", "end_time");
                        List<String> change5_key_lis = Arrays.asList("temp", "humidity", "pressure");
                        List<String> change6_key_lis = Arrays.asList("name_model_id", "instrument_code");
                        BatchGatherLis batch_index = null;
                        Operation operation = item_info.getOperation();
                        String work_content = operation.getWork_content();
                        Integer people_num = operation.getPeople_num();
                        String equip_name = operation.getEquip_name();
                        List<Map<String, String>> equip_lis = operation.getEquip_lis();
                        for (BatchGatherLis batchIndex : item_info.getBatch_gather_lis()) {
                            batch_index = batchIndex;
                            Scene scene = batch_index.getScene();
                            String temp = scene.getTemp();
                            String humidity = scene.getHumidity();
                            String pressure = scene.getPressure();
                            Integer operators_num = batch_index.getOperators_num();
                            Integer working_equip = batch_index.getWorking_equip();
                            String epe_working = batch_index.getEpe_working();
                            String ppe_working = batch_index.getPpe_working();
                            Integer sample_kb_num = batch_index.getSample_kb_code_lis().size();
                            for (String change5_key : change5_key_lis) {
                                Scene scene1 = k_v_dic.getBatch_gather_lis().get(batch_num).getScene();
                                Map<String, Object> scenceMap1 = null;
                                try {
                                    scenceMap1 = this.convert(scene1);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                Object change5_value = scenceMap1.get(change5_key);
                                if (change5_value != null && !change5_value.equals("")) {
                                    need_range = 1;
                                    need_range_map.put(change5_key, Float.parseFloat(change5_value.toString()));
                                }
                                need_range_point.put(change5_key, change5_value);
                            }
                            for (String key : batch_index.getGather_map().keySet()) {
                                GatherLis gatherInfo = batch_index.getGather_map().get(key);
                                Integer test_duration = gatherInfo.getTest_duration();
                                String begin_time = gatherInfo.getBegin_time();
                                String end_time = gatherInfo.getEnd_time();
                                String test_frame = gatherInfo.getTime_frame();
                                SampleEquip sampleEquip = gatherInfo.getSample_equip();
                                String name_model_id = sampleEquip.getName_model_id();
                                String instrument_code = sampleEquip.getInstrument_code();
                                if (!instrument_code_list.contains(instrument_code)) {
                                    instrument_code_list.add(instrument_code);
                                }
                                may.yuntian.jianping.mongodto.Result resultAll = gatherInfo.getResult();
                                String result1 = resultAll.getResult1();
                                String result2 = resultAll.getResult2();
                                String result3 = resultAll.getResult3();
                                String result = resultAll.getResult();
                                String about1 = resultAll.getAbout1();
                                String about2 = resultAll.getAbout2();
                                Map<String, Object> map = new HashMap<>();
                                map.put("test_frame", test_frame);
                                map.put("begin_time", begin_time);
                                map.put("end_time", end_time);
                                map.put("result1", result1);
                                map.put("result2", result2);
                                map.put("result3", result3);
                                map.put("result", result);
                                map.put("about1", about1);
                                map.put("about2", about2);
                                map.put("temp", temp);
                                map.put("humidity", humidity);
                                map.put("pressure", pressure);
                                map.put("name_model_id", name_model_id);
                                map.put("instrument_code", instrument_code);
                                map.put("operators_num", operators_num);
                                map.put("working_equip", working_equip);
                                map.put("epe_working", epe_working);
                                map.put("ppe_working", ppe_working);
                                map.put("work_content", work_content);
                                map.put("people_num", people_num);
                                map.put("equip_name", equip_name);
                                sample_info.add(map);
                            }
                            update_info_pc1(batch_index, change_key_lis, change2_key_lis, change5_key_lis, change6_key_lis, batch_num, update, reduction, k_v_dic, sample_info);
                        }
                        batch_num += 1;
                        break;
                    }
                    case 11: {
                        // 11：照度
                        int batch_num = 0;
                        List<String> change_key_lis = Arrays.asList("result1", "result2", "result3", "fruit1", "fruit2", "fruit3", "result_avg",
                                "about1", "about2", "result");
                        List<String> change2_key_lis = new ArrayList<>();
                        List<String> change3_key_lis = new ArrayList<>();
                        List<String> change4_key_lis = new ArrayList<>();
                        List<String> change5_key_lis = new ArrayList<>();
                        List<String> change6_key_lis = Arrays.asList("name_model_id", "instrument_code");
                        BatchGatherLis batch_index = null;
                        Operation operation = item_info.getOperation();
                        List<Map<String, String>> equip_lis = operation.getEquip_lis();
                        for (BatchGatherLis batchIndex : item_info.getBatch_gather_lis()) {
                            batch_index = batchIndex;
                            Integer sample_kb_num = batch_index.getSample_kb_code_lis().size();
                            for (String change5_key : change5_key_lis) {
                                Scene scene1 = k_v_dic.getBatch_gather_lis().get(batch_num).getScene();
                                Map<String, Object> scenceMap1 = null;
                                try {
                                    scenceMap1 = this.convert(scene1);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                Object change5_value = scenceMap1.get(change5_key);
                                if (change5_value != null && !change5_value.equals("")) {
                                    need_range = 1;
                                    need_range_map.put(change5_key, Float.parseFloat(change5_value.toString()));
                                }
                                need_range_point.put(change5_key, change5_value);
                            }

                            for (String key : batch_index.getGather_map().keySet()) {
                                GatherLis gatherInfo = batch_index.getGather_map().get(key);
                                String test_frame = gatherInfo.getTime_frame();
                                SampleEquip sampleEquip = gatherInfo.getSample_equip();
                                String name_model_id = sampleEquip.getName_model_id();
                                String instrument_code = sampleEquip.getInstrument_code();
                                String correct_factor = sampleEquip.getCorrect_factor();
                                if (!instrument_code_list.contains(instrument_code)) {
                                    instrument_code_list.add(instrument_code);
                                }
                                may.yuntian.jianping.mongodto.Result resultAll = gatherInfo.getResult();
                                String result1 = resultAll.getResult1();
                                String result2 = resultAll.getResult2();
                                String result3 = resultAll.getResult3();
                                String fruit1 = resultAll.getFruit1();
                                String fruit2 = resultAll.getFruit2();
                                String fruit3 = resultAll.getFruit3();
                                String result_avg = resultAll.getResult_avg();
                                String result = resultAll.getResult();
                                String sun_area = resultAll.getAbout1();
                                String test_place = resultAll.getAbout2();
                                Map<String, Object> map = new HashMap<>();
                                map.put("about2", test_place);
                                map.put("test_frame", test_frame);
                                map.put("result", result);
                                map.put("result1", result1);
                                map.put("result2", result2);
                                map.put("result3", result3);
                                map.put("result_avg", result_avg);
                                map.put("result", result);
                                map.put("fruit1", fruit1);
                                map.put("fruit2", fruit2);
                                map.put("fruit3", fruit3);
                                map.put("about1", sun_area);
                                map.put("name_model_id", name_model_id);
                                map.put("instrument_code", instrument_code);
                                map.put("correct_factor", correct_factor);
                                sample_info.add(map);
                            }
                            update_info_pc1(batch_index, change_key_lis, change2_key_lis, change5_key_lis, change6_key_lis, batch_num, update, reduction, k_v_dic, sample_info);
                        }
                        batch_num += 1;
                        break;
                    }
                    case 13: {
                        // 13:微波辐射
                        int batch_num = 0;
                        List<String> change_key_lis = Arrays.asList("result1", "result2", "result3", "result4", "result5", "result6", "result7", "result8", "result9",
                                "fruit1", "fruit2", "fruit3", "fruit4", "fruit5", "fruit6", "about2", "about1");
                        List<String> change2_key_lis = Arrays.asList("begin_time", "end_time", "contact_duration");
                        List<String> change5_key_lis = Arrays.asList("temp", "humidity", "pressure");
                        List<String> change6_key_lis = Arrays.asList("instrument_code", "name_model_id");
                        BatchGatherLis batch_index = null;
                        Operation operation = item_info.getOperation();
                        String work_content = operation.getWork_content();
                        Integer people_num = operation.getPeople_num();
                        String equip_name = operation.getEquip_name();
                        List<Map<String, String>> equip_lis = operation.getEquip_lis();
                        for (BatchGatherLis batchIndex : item_info.getBatch_gather_lis()) {
                            batch_index = batchIndex;
                            Scene scene = batch_index.getScene();
                            String temp = scene.getTemp();
                            String humidity = scene.getHumidity();
                            String pressure = scene.getPressure();
                            Integer operators_num = batch_index.getOperators_num();
                            Integer working_equip = batch_index.getWorking_equip();
                            String epe_working = batch_index.getEpe_working();
                            String ppe_working = batch_index.getPpe_working();
                            Integer sample_kb_num = batch_index.getSample_kb_code_lis().size();
                            for (String change5_key : change5_key_lis) {
                                Scene scene1 = k_v_dic.getBatch_gather_lis().get(batch_num).getScene();
                                Map<String, Object> scenceMap1 = null;
                                try {
                                    scenceMap1 = this.convert(scene1);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                Object change5_value = scenceMap1.get(change5_key);
                                if (change5_value != null && !change5_value.equals("")) {
                                    need_range = 1;
                                    need_range_map.put(change5_key, Float.parseFloat(change5_value.toString()));
                                }
                                need_range_point.put(change5_key, change5_value);
                            }
                            for (String key : batch_index.getGather_map().keySet()) {
                                GatherLis gatherInfo = batch_index.getGather_map().get(key);
                                Integer test_duration = gatherInfo.getTest_duration();
                                String begin_time = gatherInfo.getBegin_time();
                                String end_time = gatherInfo.getEnd_time();
                                String time_frame = gatherInfo.getTime_frame();
                                String contact_duration = gatherInfo.getContact_duration();
                                SampleEquip sampleEquip = gatherInfo.getSample_equip();
                                String name_model_id = sampleEquip.getName_model_id();
                                String instrument_code = sampleEquip.getInstrument_code();
                                if (!instrument_code_list.contains(instrument_code)) {
                                    instrument_code_list.add(instrument_code);
                                }
                                may.yuntian.jianping.mongodto.Result resultAll = gatherInfo.getResult();
                                String result1 = resultAll.getResult1();
                                String result2 = resultAll.getResult2();
                                String result3 = resultAll.getResult3();
                                String result4 = resultAll.getResult4();
                                String result5 = resultAll.getResult5();
                                String result6 = resultAll.getResult6();
                                String result7 = resultAll.getResult7();
                                String result8 = resultAll.getResult8();
                                String result9 = resultAll.getResult9();
                                String fruit1 = resultAll.getFruit1();
                                String fruit2 = resultAll.getFruit2();
                                String fruit3 = resultAll.getFruit3();
                                String fruit4 = resultAll.getFruit4();
                                String fruit5 = resultAll.getFruit5();
                                String fruit6 = resultAll.getFruit6();
                                String about1 = resultAll.getAbout1();
                                String about2 = resultAll.getAbout2();
                                Map<String, Object> map = new HashMap<>();
                                map.put("result1", result1);
                                map.put("result2", result2);
                                map.put("result3", result3);
                                map.put("result4", result4);
                                map.put("result5", result5);
                                map.put("result6", result6);
                                map.put("result7", result7);
                                map.put("result8", result8);
                                map.put("result9", result9);
                                map.put("fruit1", fruit1);
                                map.put("fruit2", fruit2);
                                map.put("fruit3", fruit3);
                                map.put("fruit4", fruit4);
                                map.put("fruit5", fruit5);
                                map.put("fruit6", fruit6);
                                map.put("instrument_code", instrument_code);
                                map.put("name_model_id", name_model_id);
                                map.put("about1", about1);
                                map.put("about2", about2);
                                map.put("begin_time", begin_time);
                                map.put("end_time", end_time);
                                map.put("contact_duration", contact_duration);
                                map.put("temp", temp);
                                map.put("humidity", humidity);
                                map.put("pressure", pressure);
                                map.put("operators_num", operators_num);
                                map.put("working_equip", working_equip);
                                map.put("epe_working", epe_working);
                                map.put("ppe_working", ppe_working);
                                map.put("work_content", work_content);
                                map.put("people_num", people_num);
                                map.put("equip_name", equip_name);
                                sample_info.add(map);
                            }
                            update_info_pc1(batch_index, change_key_lis, change2_key_lis, change5_key_lis, change6_key_lis, batch_num, update, reduction, k_v_dic, sample_info);
                        }
                        batch_num += 1;
                        break;
                    }
                    case 14: {
                        // 14:激光辐射
                        int batch_num = 0;
                        List<String> change_key_lis = Arrays.asList("result1", "result2", "result7", "about2", "result8", "result9",
                                "fruit1", "fruit2", "fruit", "result", "about1", "about5", "about4");
                        List<String> change2_key_lis = Arrays.asList("begin_time", "end_time");
                        List<String> change5_key_lis = Arrays.asList("temp", "humidity", "pressure");
                        List<String> change6_key_lis = Arrays.asList("correct_factor", "instrument_code", "name_model_id");
                        BatchGatherLis batch_index = null;
                        Operation operation = item_info.getOperation();
                        String work_content = operation.getWork_content();
                        Integer people_num = operation.getPeople_num();
                        String equip_name = operation.getEquip_name();
                        List<Map<String, String>> equip_lis = operation.getEquip_lis();
                        for (BatchGatherLis batchIndex : item_info.getBatch_gather_lis()) {
                            batch_index = batchIndex;
                            Scene scene = batch_index.getScene();
                            String temp = scene.getTemp();
                            String humidity = scene.getHumidity();
                            String pressure = scene.getPressure();
                            Integer operators_num = batch_index.getOperators_num();
                            Integer working_equip = batch_index.getWorking_equip();
                            String epe_working = batch_index.getEpe_working();
                            String ppe_working = batch_index.getPpe_working();
                            Integer sample_kb_num = batch_index.getSample_kb_code_lis().size();
                            for (String change5_key : change5_key_lis) {
                                Scene scene1 = k_v_dic.getBatch_gather_lis().get(batch_num).getScene();
                                Map<String, Object> scenceMap1 = null;
                                try {
                                    scenceMap1 = this.convert(scene1);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                Object change5_value = scenceMap1.get(change5_key);
                                if (change5_value != null && !change5_value.equals("")) {
                                    need_range = 1;
                                    need_range_map.put(change5_key, Float.parseFloat(change5_value.toString()));
                                }
                                need_range_point.put(change5_key, change5_value);
                            }
                            for (String key : batch_index.getGather_map().keySet()) {
                                GatherLis gatherInfo = batch_index.getGather_map().get(key);
                                Integer test_duration = gatherInfo.getTest_duration();
                                String begin_time = gatherInfo.getBegin_time();
                                String end_time = gatherInfo.getEnd_time();
                                String test_frame = gatherInfo.getTime_frame();
                                SampleEquip sampleEquip = gatherInfo.getSample_equip();
                                String name_model_id = sampleEquip.getName_model_id();
                                String instrument_code = sampleEquip.getInstrument_code();
                                String correct_factor = sampleEquip.getCorrect_factor();
                                if (!instrument_code_list.contains(instrument_code)) {
                                    instrument_code_list.add(instrument_code);
                                }
                                may.yuntian.jianping.mongodto.Result resultAll = gatherInfo.getResult();
                                String result1 = resultAll.getResult1();
                                String result2 = resultAll.getResult2();
                                String result7 = resultAll.getResult7();
                                String result8 = resultAll.getResult8();
                                String result9 = resultAll.getResult9();
                                String fruit = resultAll.getFruit();
                                Object result = resultAll.getResult();
                                String fruit1 = resultAll.getFruit1();
                                String fruit2 = resultAll.getFruit2();
                                String about1 = resultAll.getAbout1();
                                String about5 = resultAll.getAbout5();
                                String about4 = resultAll.getAbout4();
                                String about2 = resultAll.getAbout2();
                                Map<String, Object> map = new HashMap<>();
                                map.put("result1", result1);
                                map.put("result2", result2);
                                map.put("result7", result7);
                                map.put("result8", result8);
                                map.put("result9", result9);
                                map.put("fruit1", fruit1);
                                map.put("fruit2", fruit2);
                                map.put("fruit", fruit);
                                map.put("result", result);
                                map.put("about5", about5);
                                map.put("about4", about4);
                                map.put("about1", about1);
                                map.put("about2", about2);
                                map.put("begin_time", begin_time);
                                map.put("end_time", end_time);
                                map.put("instrument_code", instrument_code);
                                map.put("name_model_id", name_model_id);
                                map.put("correct_factor", correct_factor);
                                map.put("temp", temp);
                                map.put("humidity", humidity);
                                map.put("pressure", pressure);
                                map.put("operators_num", operators_num);
                                map.put("working_equip", working_equip);
                                map.put("epe_working", epe_working);
                                map.put("ppe_working", ppe_working);
                                map.put("work_content", work_content);
                                map.put("people_num", people_num);
                                map.put("equip_name", equip_name);
                                sample_info.add(map);
                            }
                            update_info_pc1(batch_index, change_key_lis, change2_key_lis, change5_key_lis, change6_key_lis, batch_num, update, reduction, k_v_dic, sample_info);
                        }
                        batch_num += 1;
                        break;
                    }
                    case 15: {
                        // 15：超高频辐射
                        int batch_num = 0;
                        List<String> change_key_lis = Arrays.asList("result1", "result2", "result3", "result4", "result5", "result6", "result7", "result8", "result9",
                                "fruit1", "fruit2", "fruit3", "fruit4", "fruit5", "fruit6", "fruit7", "fruit8", "fruit9", "fruit_avg1", "fruit_avg2", "fruit_avg3", "about1", "about2", "about3");
                        List<String> change2_key_lis = Arrays.asList("begin_time", "end_time", "contact_duration");
                        List<String> change5_key_lis = Arrays.asList("temp", "humidity", "pressure");
                        List<String> change6_key_lis = Arrays.asList("correct_factor", "instrument_code", "name_model_id");
                        BatchGatherLis batch_index = null;
                        Operation operation = item_info.getOperation();
                        String work_content = operation.getWork_content();
                        Integer people_num = operation.getPeople_num();
                        String equip_name = operation.getEquip_name();
                        List<Map<String, String>> equip_lis = operation.getEquip_lis();
                        for (BatchGatherLis batchIndex : item_info.getBatch_gather_lis()) {
                            batch_index = batchIndex;
                            Scene scene = batch_index.getScene();
                            String temp = scene.getTemp();
                            String humidity = scene.getHumidity();
                            String pressure = scene.getPressure();
                            Integer operators_num = batch_index.getOperators_num();
                            Integer working_equip = batch_index.getWorking_equip();
                            String epe_working = batch_index.getEpe_working();
                            String ppe_working = batch_index.getPpe_working();
                            Integer sample_kb_num = batch_index.getSample_kb_code_lis().size();
                            for (String change5_key : change5_key_lis) {
                                Scene scene1 = k_v_dic.getBatch_gather_lis().get(batch_num).getScene();
                                Map<String, Object> scenceMap1 = null;
                                try {
                                    scenceMap1 = this.convert(scene1);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                Object change5_value = scenceMap1.get(change5_key);
                                if (change5_value != null && !change5_value.equals("")) {
                                    need_range = 1;
                                    need_range_map.put(change5_key, Float.parseFloat(change5_value.toString()));
                                }
                                need_range_point.put(change5_key, change5_value);
                            }
                            for (String key : batch_index.getGather_map().keySet()) {
                                GatherLis gatherInfo = batch_index.getGather_map().get(key);
                                Integer test_duration = gatherInfo.getTest_duration();
                                String begin_time = gatherInfo.getBegin_time();
                                String end_time = gatherInfo.getEnd_time();
                                String test_frame = gatherInfo.getTime_frame();
                                String contact_duration = gatherInfo.getContact_duration();
                                SampleEquip sampleEquip = gatherInfo.getSample_equip();
                                String name_model_id = sampleEquip.getName_model_id();
                                String instrument_code = sampleEquip.getInstrument_code();
                                String correct_factor = sampleEquip.getCorrect_factor();
                                if (!instrument_code_list.contains(instrument_code)) {
                                    instrument_code_list.add(instrument_code);
                                }
                                may.yuntian.jianping.mongodto.Result resultAll = gatherInfo.getResult();
                                String result1 = resultAll.getResult1();
                                String result2 = resultAll.getResult2();
                                String result3 = resultAll.getResult3();
                                String result4 = resultAll.getResult4();
                                String result5 = resultAll.getResult5();
                                String result6 = resultAll.getResult6();
                                String result7 = resultAll.getResult7();
                                String result8 = resultAll.getResult8();
                                String result9 = resultAll.getResult9();
                                String fruit_avg1 = resultAll.getFruit_avg1();
                                String fruit_avg2 = resultAll.getFruit_avg2();
                                String fruit_avg3 = resultAll.getFruit_avg3();
                                String fruit1 = resultAll.getFruit1();
                                String fruit2 = resultAll.getFruit2();
                                String fruit3 = resultAll.getFruit3();
                                String fruit4 = resultAll.getFruit4();
                                String fruit5 = resultAll.getFruit5();
                                String fruit6 = resultAll.getFruit6();
                                String fruit7 = resultAll.getFruit7();
                                String fruit8 = resultAll.getFruit8();
                                String fruit9 = resultAll.getFruit9();
                                String about1 = resultAll.getAbout1();
                                String about2 = resultAll.getAbout2();
                                String about3 = resultAll.getAbout3();
                                Map<String, Object> map = new HashMap<>();
                                map.put("result1", result1);
                                map.put("result2", result2);
                                map.put("result3", result3);
                                map.put("result4", result4);
                                map.put("result5", result5);
                                map.put("result6", result6);
                                map.put("result7", result7);
                                map.put("result8", result8);
                                map.put("result9", result9);
                                map.put("fruit1", fruit1);
                                map.put("fruit2", fruit2);
                                map.put("fruit3", fruit3);
                                map.put("fruit4", fruit4);
                                map.put("fruit5", fruit5);
                                map.put("fruit6", fruit6);
                                map.put("fruit7", fruit7);
                                map.put("fruit8", fruit8);
                                map.put("fruit9", fruit9);
                                map.put("fruit_avg1", fruit_avg1);
                                map.put("fruit_avg2", fruit_avg2);
                                map.put("fruit_avg3", fruit_avg3);
                                map.put("instrument_code", instrument_code);
                                map.put("name_model_id", name_model_id);
                                map.put("correct_factor", correct_factor);
                                map.put("about1", about1);
                                map.put("about2", about2);
                                map.put("about3", about3);
                                map.put("about1", about1);
                                map.put("about2", about2);
                                map.put("begin_time", begin_time);
                                map.put("end_time", end_time);
                                map.put("contact_duration", contact_duration);
                                map.put("temp", temp);
                                map.put("humidity", humidity);
                                map.put("pressure", pressure);
                                map.put("operators_num", operators_num);
                                map.put("working_equip", working_equip);
                                map.put("epe_working", epe_working);
                                map.put("ppe_working", ppe_working);
                                map.put("work_content", work_content);
                                map.put("people_num", people_num);
                                map.put("equip_name", equip_name);
                                sample_info.add(map);
                            }
                            update_info_pc1(batch_index, change_key_lis, change2_key_lis, change5_key_lis, change6_key_lis, batch_num, update, reduction, k_v_dic, sample_info);
                        }
                        batch_num += 1;
                        break;
                    }
                    default:
                        return Result.error("参数错误，信息展示类型错误").put("data", null).put("code", 501);
                }
                Map<String, Object> fitMap = new HashMap<>();
                Map<String, Object> idMap = new HashMap<>();
                idMap.put("_id", r_id);
                Query query0 = new Query();
                query0.addCriteria(Criteria.where("_id").is(r_id));
                MongoUpdateDto mongoUpdateDto = new MongoUpdateDto();
                mongoUpdateDto.setFlt(query0);
                mongoUpdateDto.setUpdate(update);
                mongoUpdateDto.setReduction(reduction);
                flt_update_lis.add(mongoUpdateDto);

                List<MongoUpdateDto> sm_flt_update_lis = new ArrayList<>();
                Map<String, Object> point_map = new HashMap<>();
                point_map.put(item_info.getPoint_code_num().toString(), need_range_point);
                Query query = new Query();
                if (need_range == 1) {
                    if (item_info.getIs_fixed() == 1) {
                        query.addCriteria(Criteria.where("is_fixed").is(1));
                        String workshop_id = item_info.getPlace().getWorkshop_id();
                        query.addCriteria(Criteria.where("place.workshop_id").is(workshop_id));
                    } else {
                        query.addCriteria(Criteria.where("is_fixed").is(2));
                        query.addCriteria(Criteria.where("pfn_id").is(item_info.getPfn_id()));
                    }
                } else {
                    query.addCriteria(Criteria.where("point_id").is(item_info.getPoint_id()));
                }
                List<ZjPlanRecord> sm_record_lis = mongoTemplate.find(query, ZjPlanRecord.class);
                DecimalFormat df = new DecimalFormat("#0.0");
                for (ZjPlanRecord sm_r_one : sm_record_lis) {
                    Update sm_update = new Update();
                    Update sm_reduction = new Update();
                    if (sm_r_one.getPoint_id().equals(record_lis.get(0).getPoint_id())) {
                        if (sm_r_one.get_id().equals(record_lis.get(0).get_id())) {
                            continue;
                        } else {
                            for (int batch_i = 0; batch_i < sm_r_one.getBatch_gather_lis().size(); batch_i++) {
                                for (String range_key : JSONObject.parseObject(JSON.toJSONString(point_map.get(sm_r_one.getPoint_code_num().toString()))).keySet()) {
                                    String up_k = "batch_gather_lis." + batch_i + ".scene." + range_key;
                                    sm_update.set(up_k, JSONObject.parseObject(JSON.toJSONString(point_map.get(sm_r_one.getPoint_code_num().toString()))).get(range_key));
                                    Scene scene = sm_r_one.getBatch_gather_lis().get(batch_i).getScene();
                                    Map<String, Object> sceneMap = null;
                                    try {
                                        sceneMap = this.convert(scene);
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                    sm_reduction.set(up_k, sceneMap.get(range_key));
                                }
                            }
                        }
                    } else {
                        if (!point_map.containsKey(sm_r_one.getPoint_code_num().toString())) {
                            point_map.put(sm_r_one.getPoint_code_num().toString(), new HashMap<>());
                            for (String range_key : need_range_map.keySet()) {
                                if ("temp".equals(range_key)) {
                                    if (need_range_map.get("temp") >= 5.5 && need_range_map.get("temp") <= 34.5) {
                                        float range_v = need_range_map.get("temp") + ((float) Math.random() * 10 - 5) / 10;
                                        JSONObject.parseObject(JSON.toJSONString(point_map.get(sm_r_one.getPoint_code_num().toString()))).put("temp", df.format(range_v));
                                    } else {
                                        JSONObject.parseObject(JSON.toJSONString(point_map.get(sm_r_one.getPoint_code_num().toString()))).put("temp", df.format(need_range_map.get("temp")));
                                    }
                                } else if ("pressure".equals(range_key)) {
                                    if (need_range_map.get("pressure") >= 98.6 && need_range_map.get("pressure") <= 103.6) {
                                        float range_v = need_range_map.get("temp") + ((float) Math.random() * 4 - 2) / 10;
                                        JSONObject.parseObject(JSON.toJSONString(point_map.get(sm_r_one.getPoint_code_num().toString()))).put("pressure", df.format(range_v));
                                    } else {
                                        JSONObject.parseObject(JSON.toJSONString(point_map.get(sm_r_one.getPoint_code_num().toString()))).put("pressure", df.format(need_range_map.get("pressure")));
                                    }
                                } else if ("humidity".equals(range_key)) {
                                    float range_v = need_range_map.get("temp") + ((float) Math.random() * 100 - 50) / 10;
                                    JSONObject.parseObject(JSON.toJSONString(point_map.get(sm_r_one.getPoint_code_num().toString()))).put("humidity", df.format(range_v));
                                }
                            }
                        }
                        for (int batch_i = 0; batch_i < sm_r_one.getBatch_gather_lis().size(); batch_i++) {
                            for (String range_key : JSONObject.parseObject(JSON.toJSONString(point_map.get(sm_r_one.getPoint_code_num().toString()))).keySet()) {
                                String up_k = "batch_gather_lis." + batch_i + ".scene." + range_key;
                                sm_update.set(up_k, JSONObject.parseObject(JSON.toJSONString(point_map.get(sm_r_one.getPoint_code_num().toString()))).get(range_key));
                                Scene scene = sm_r_one.getBatch_gather_lis().get(batch_i).getScene();
                                Map<String, Object> sceneMap = null;
                                try {
                                    sceneMap = this.convert(scene);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                sm_reduction.set(up_k, sceneMap.get(range_key));
                            }
                        }
                    }
                    if (sm_update == null || sm_update.equals(new Update())) {
                        continue;
                    }
                    Query query1 = new Query();
                    query1.addCriteria(Criteria.where("_id").is(sm_r_one.get_id()));
                    MongoUpdateDto mongoUpdateDto1 = new MongoUpdateDto();
                    mongoUpdateDto1.setFlt(query1);
                    mongoUpdateDto1.setUpdate(sm_update);
                    mongoUpdateDto1.setReduction(sm_reduction);
                    sm_flt_update_lis.add(mongoUpdateDto1);
                }
                try {
                    for (MongoUpdateDto item_fu : flt_update_lis) {
                        Query flt = item_fu.getFlt();
                        Update update1 = item_fu.getUpdate();
                        UpdateResult updateResult = mongoTemplate.updateFirst(flt, update1, PlanRecordEntity.class);
                    }
                    for (MongoUpdateDto sm_item_fu : sm_flt_update_lis) {
                        Query flt = sm_item_fu.getFlt();
                        Update update1 = sm_item_fu.getUpdate();
                        UpdateResult updateResult = mongoTemplate.updateFirst(flt, update1, PlanRecordEntity.class);
                    }
                } catch (Exception e) {
                    for (MongoUpdateDto item_fu : flt_update_lis) {
                        Query flt = item_fu.getFlt();
                        Update reduction1 = item_fu.getReduction();
                        UpdateResult updateResult = mongoTemplate.updateFirst(flt, reduction1, PlanRecordEntity.class);
                    }
                    for (MongoUpdateDto sm_item_fu : sm_flt_update_lis) {
                        Query flt = sm_item_fu.getFlt();
                        Update reduction1 = sm_item_fu.getReduction();
                        UpdateResult updateResult = mongoTemplate.updateFirst(flt, reduction1, PlanRecordEntity.class);
                    }
                    e.printStackTrace();
                    return Result.error("采样记录保存失败").put("data", null);
                }
                ProjectEntity thisProject = projectService.getById(project_id);
                if (thisProject.getStatus() < 5) {
                    thisProject.setStatus(5);
                    projectService.updateById(thisProject);
                } else {
//                    pass;
                }
                ProjectProceduresEntity projectProceduresEntity = new ProjectProceduresEntity();
                projectProceduresEntity.setProjectId(project_id.longValue());
                projectProceduresEntity.setStatus(5);
                List<ProjectProceduresEntity> list = projectProceduresService.list(new QueryWrapper<>(projectProceduresEntity));
                if (list.size() == 0 || list == null) {
                    projectProceduresEntity.setCreatetime(new Date());
                    projectProceduresService.save(projectProceduresEntity);
                } else {
//                    pass
                }
            }
        }
        return Result.ok("采样记录保存成功", null);
    }


    /**
     * # 修改列表1为测量结果库，修改列表2为采样样品信息库，修改列表3为采样设备库
     * # 层级关系为：采样方案信息库->批次采样信息库->采样样品信息库->采样设备库/测量结果库
     * param sample_kb_num: 空白采样的数量
     * param batch_index:对非空白样和空白样列表合并采样不同批次信息分类
     * param change_key_lis: 需要修改的列表
     * param change2_key_lis:
     * param batch_num: 采样列表下标
     * param update: 修改文本字典
     * param reduction: 回溯文本字典
     * param k_v_dic: 用户回传参数字典
     * param sample_info: 数据库查询值
     * return: update，reduction
     */
    private void update_info_pc1(BatchGatherLis batch_index, List<String> change_key_lis,
                                 List<String> change2_key_lis, List<String> change5_key_lis, List<String> change6_key_lis, Integer batch_num,
                                 Update update, Update reduction, ZjPlanRecord k_v_dic, List<Object> sample_info) {
        int gather_num = 0;
        for (String gather_index : batch_index.getGather_map().keySet()) {
            if (change_key_lis.size() != 0) {
                for (String change_key : change_key_lis) {
                    String update_key = "batch_gather_lis." + batch_num + ".gather_map." + gather_index + ".result." + change_key;

                    may.yuntian.jianping.mongodto.Result result = k_v_dic.getBatch_gather_lis().get(batch_num).getGather_map().get(gather_index).getResult();
                    Map<String, Object> resultMap = null;
                    try {
                        resultMap = this.convert(result);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    update.set(update_key, resultMap.get(change_key));
                    reduction.set(update_key, JSONObject.parseObject(JSON.toJSONString(sample_info.get(gather_num))).get(change_key));
                }
            }
            if (change2_key_lis.size() != 0) {
                for (String change2_key : change2_key_lis) {
                    String update_key = "batch_gather_lis." + batch_num + ".gather_map." + gather_index + "." + change2_key;
                    GatherLis gatherLis = k_v_dic.getBatch_gather_lis().get(batch_num).getGather_map().get(gather_index);
                    Map<String, Object> gatherLisMap = null;
                    try {
                        gatherLisMap = this.convert(gatherLis);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    update.set(update_key, gatherLisMap.get(change2_key));
                    reduction.set(update_key, JSONObject.parseObject(JSON.toJSONString(sample_info.get(gather_num))).get(change2_key));
                }
            }
            if (change5_key_lis.size() != 0) {
                for (String change5_key : change5_key_lis) {
                    String update_key = "batch_gather_lis." + batch_num + ".scene." + change5_key;
                    Scene scene1 = k_v_dic.getBatch_gather_lis().get(batch_num).getScene();
                    Map<String, Object> scenceMap1 = null;
                    try {
                        scenceMap1 = this.convert(scene1);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    update.set(update_key, scenceMap1.get(change5_key));
                    reduction.set(update_key, JSONObject.parseObject(JSON.toJSONString(sample_info.get(gather_num))).get(change5_key));
                }
            }
            if (change6_key_lis.size() != 0) {
                for (String change6_key : change6_key_lis) {
                    String update_key = "batch_gather_lis." + batch_num + ".gather_map." + gather_index + ".sample_equip." + change6_key;
                    SampleEquip sample_equip = k_v_dic.getBatch_gather_lis().get(batch_num).getGather_map().get(gather_index).getSample_equip();
                    Map<String, Object> sampleEquipMap = null;
                    try {
                        sampleEquipMap = this.convert(sample_equip);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    update.set(update_key, sampleEquipMap.get(change6_key));
                    reduction.set(update_key, JSONObject.parseObject(JSON.toJSONString(sample_info.get(gather_num))).get(change6_key));
                }
            }
            gather_num += 1;
        }
        batch_num += 1;
    }







    /**
     * 获取项目采样记录类型
     * @param projectId
     * @return
     */
    public Result getRecordType(Long projectId) {

        Query query = new Query().addCriteria(Criteria.where("project_id").is(projectId));

        List<ZjPlanRecord> recordEntityList = mongoTemplate.find(query, ZjPlanRecord.class);

        List<Integer> recordTypeList = new ArrayList<>();
        for (ZjPlanRecord record : recordEntityList) {

            if ("airFixed".equals(record.getSubstance().getSubstance_info().getSample_tablename())) {
                //空气中有害物质
                recordTypeList.add(1);
            } else if (record.getSubstance().getSubstance_info().getS_type() == 3 && record.getIs_fixed() == 1) {
                //噪声定点
                recordTypeList.add(2);
            } else if (record.getSubstance().getSubstance_info().getS_type() == 3 && record.getIs_fixed() == 2) {
                //噪声个体
                recordTypeList.add(3);
            } else if (record.getSubstance().getSubstance_info().getS_type() == 4) {
                //高温（热源稳定）
                recordTypeList.add(4);
            } else if (record.getSubstance().getSubstance_info().getS_type() == 5) {
                //紫外辐射
                recordTypeList.add(5);
            } else if ("co".equals(record.getSubstance().getSubstance_info().getSample_tablename())) {
                //一氧化碳
                recordTypeList.add(6);
            } else if ("co2".equals(record.getSubstance().getSubstance_info().getSample_tablename())) {
                //二氧化碳
                recordTypeList.add(7);
            } else if (record.getSubstance().getSubstance_info().getS_type() == 8) {
                //高频电磁场
                recordTypeList.add(8);
            } else if (record.getSubstance().getSubstance_info().getS_type() == 7) {
                //工频电场
                recordTypeList.add(9);
            } else if (record.getSubstance().getSubstance_info().getS_type() == 6) {
                //手传振动
                recordTypeList.add(10);
            } else if (record.getSubstance().getSubstance_info().getS_type() == 12) {
                //照明照度
                recordTypeList.add(11);
            } else if ("windSpeed".equals(record.getSubstance().getSubstance_info().getSample_tablename())) {
                //风速
                recordTypeList.add(12);
            } else if ("microwave".equals(record.getSubstance().getSubstance_info().getSample_tablename())) {
                //微波辐射
                recordTypeList.add(13);
            } else if ("laserRadiation".equals(record.getSubstance().getSubstance_info().getSample_tablename())) {
                //激光辐射
                recordTypeList.add(14);
            } else if ("uhfRadiation".equals(record.getSubstance().getSubstance_info().getSample_tablename())) {
                //超高频辐射
                recordTypeList.add(15);
            } else {
                return Result.error(501, "参数错误，信息展示类型错误");
            }
        }
        List<Integer> recordTypeList1 = recordTypeList.stream().distinct().sorted().collect(Collectors.toList());
        return Result.ok("查询成功" , recordTypeList1);
    }
}
