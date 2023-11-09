package may.yuntian.jianping.mongoservice;

import lombok.extern.slf4j.Slf4j;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.service.ProjectService;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.jianping.dto.DetectionDto;
import may.yuntian.jianping.dto.HazardCategoryDto;
import may.yuntian.jianping.dto.Hazards.*;
import may.yuntian.jianping.dto.HazardsTestDto;
import may.yuntian.jianping.dto.InformationMapDto;
import may.yuntian.jianping.dto.category.*;
import may.yuntian.jianping.entity.CompanySurveyEntity;
import may.yuntian.jianping.entity.ConclusionEntity;
import may.yuntian.jianping.entity.ProductOutputEntity;
import may.yuntian.jianping.mongoentity.DeclareIndexEntity;
import may.yuntian.jianping.service.CompanySurveyService;
import may.yuntian.jianping.service.ConclusionService;
import may.yuntian.jianping.service.ProductOutputService;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.sys.utils.ShiroUtils;
import may.yuntian.untils.FreemarkerDocxUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@SuppressWarnings("all")
@Slf4j
public class DeclareIndexService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private CompanySurveyService companySurveyService;
    @Autowired
    private ProductOutputService productOutputService;
    @Autowired
    private ConclusionService conclusionService;
    @Autowired
    private PostPfnService postPfnService;
    @Autowired
    private ProjectService projectService;


    /**
     * 根据项目ID获取申报索引信息
     * @param projectId
     * @return
     */
    public DeclareIndexEntity getOneByProjectId(Long projectId){
        Query query = new Query(Criteria.where("project_id").is(projectId));
        DeclareIndexEntity declareIndexEntity = mongoTemplate.findOne(query,DeclareIndexEntity.class);
        if (declareIndexEntity!=null){
            List<ProductOutputEntity> list = productOutputService.getListByProjectId(projectId);
            declareIndexEntity.setProductOutputList(list);
        }else {
            declareIndexEntity = new DeclareIndexEntity();
        }
        return declareIndexEntity;
    }


    /**
     * 初始化接口
     * @param projectId
     */
    public void initialization(Long projectId){
        ProjectEntity project = projectService.getById(projectId);
        deleteByProjecrId(projectId);
        DeclareIndexEntity declareIndexEntity = new DeclareIndexEntity();
        String id = new ObjectId().toString();
        declareIndexEntity.setId(id);
        declareIndexEntity.setProjectId(projectId);//项目ID
        InformationMapDto information = new InformationMapDto();//基本信息
        CompanySurveyEntity companySurveyEntity = companySurveyService.getOne(projectId);
        DetectionDto detection = new DetectionDto();
        detection.setDetection("已检测");//本年度检测情况
        if ("杭州安联".equals(project.getCompanyOrder())){
            detection.setCompanyName("浙江安联检测技术服务有限公司");//检测机构名称
        }else {
            detection.setCompanyName(project.getCompanyOrder() + "检测技术服务有限公司");//检测机构名称
        }

        if (companySurveyEntity!=null){
            information.setCompany(companySurveyEntity.getCompany());//用人单位名称
            information.setCode(StringUtils.checkValNotNull(companySurveyEntity.getUnifiedCode())?companySurveyEntity.getUnifiedCode():" ");
            information.setRegisteredAddress(StringUtils.checkValNotNull(companySurveyEntity.getRegisteredAddress())?companySurveyEntity.getRegisteredAddress():" ");
            information.setWorkAddress(StringUtils.checkValNotNull(companySurveyEntity.getOfficeAddress())?companySurveyEntity.getOfficeAddress():" ");
            information.setIndustry(StringUtils.checkValNotNull(companySurveyEntity.getIndustryCategory())?companySurveyEntity.getIndustryCategory():" ");
            information.setEconomy(StringUtils.checkValNotNull(companySurveyEntity.getEconomy())?companySurveyEntity.getEconomy():" ");
            Integer laborQuota = companySurveyEntity.getLaborQuota();
            information.setIncumbencyNums(StringUtils.checkValNotNull(laborQuota)?laborQuota:0);
            if(null==laborQuota||laborQuota<20){//微
                information.setScale("微");//企业规模
            }else if(laborQuota<300){//小
                information.setScale("小");//企业规模
            }else if(laborQuota<1000){//中
                information.setScale("中");//企业规模
            }else {
                information.setScale("大");//企业规模
            }
            information.setTotalNum(StringUtils.checkValNotNull(companySurveyEntity.getHazardNum())?companySurveyEntity.getHazardNum():0);   //接害总人数（含外委）);
            information.setManagement(StringUtils.checkValNotNull(companySurveyEntity.getContact())?companySurveyEntity.getContact():" ");
            information.setPhone(StringUtils.checkValNotNull(companySurveyEntity.getTelephone())?companySurveyEntity.getTelephone():" ");
            detection.setReportNumber(StringUtils.checkValNotNull(companySurveyEntity.getIdentifier())?companySurveyEntity.getIdentifier():" ");//检测报告编号
        }else {

        }
        declareIndexEntity.setDetection(detection);
        declareIndexEntity.setInformation(information);

        List<ConclusionEntity> list = conclusionService.getListByProjectId(projectId);
        HazardCategoryDto hazardCategory = new HazardCategoryDto();//申报索引职业病危害种类
        DustCategoryDto dustCategory = new DustCategoryDto();//粉尘
        ChemistryCategoryDto chemistryCategory = new ChemistryCategoryDto();//化学
        PhysicsCategoryDto physicsCategory = new PhysicsCategoryDto();//物理
        RadiationCategoryDto radiationCategory = new RadiationCategoryDto();//放射性
        BiologyCategoryDto biologyCategory = new BiologyCategoryDto();//生物
        OtherCategoryDto otherCategory = new OtherCategoryDto();//其他
        //
        HazardsTestDto hazardsTestDto = new HazardsTestDto();//危害因素检测
        Dust dust = new Dust();//粉尘
        Chemistry chemistry = new Chemistry();//化学
        Physics physics = new Physics();//物理
        Radiation radiation = new Radiation();//放射性
        Biology biology = new Biology();//生物
//        Other other = new Other();//其他

        if (list.size()>0){
            //粉尘
            List<ConclusionEntity> dustList = list.stream().filter(i->i.getSType()==2).collect(Collectors.toList());//所有粉尘
            if (dustList.size()>0){
                List<ConclusionEntity> dustExceedList = dustList.stream().filter(i->i.getResult().equals("不符合")).collect(Collectors.toList());

                List<String> dustPointList = dustList.stream().map(ConclusionEntity::getPointId).distinct().collect(Collectors.toList());//所有粉尘检测点位
                List<String> dustExceedPointList = dustExceedList.stream().map(ConclusionEntity::getPointId).distinct().collect(Collectors.toList());//所有超标粉尘检测点位
                Map<String,List<ConclusionEntity>> dustMapList = dustList.stream().collect(Collectors.groupingBy(ConclusionEntity::getPfnId));//所有粉尘根据工种ID进行分组
                Integer dustTotal = 0;//初始化粉尘总接害人数
                for (String key:dustMapList.keySet()){
                    ConclusionEntity conclusionEntity = dustMapList.get(key).get(0);//获取同工种第一条数据的人数相加（同工种接害人数是一样的）
                    dustTotal = dustTotal+conclusionEntity.getWorkerNum();
                }

//                Integer dustTotal = dustList.stream().mapToInt(i->i.getWorkerNum()).sum();//粉尘总接害人数
                dustCategory.setName("粉尘");//类型名称
                dustCategory.setIsHave("有");
                dustCategory.setTotalNum(dustTotal);//接触人数
                dust.setDetection("已检测");//是否检测
                dust.setTestNum(dustPointList.size());//检测点位数
                dust.setExceedNum(dustExceedPointList.size());//超标点数
                //矽尘
                List<ConclusionEntity> silicaList = dustList.stream().filter(i->i.getSubstance().contains("矽尘")).collect(Collectors.toList());
                if (silicaList.size()>0){
                    List<ConclusionEntity> silicaExceedList = silicaList.stream().filter(i->i.getResult().equals("不符合")).collect(Collectors.toList());//矽尘超标点位

                    List<String> silicaPointList = silicaList.stream().map(ConclusionEntity::getPointId).distinct().collect(Collectors.toList());//所有矽尘检测点位
                    List<String> silicaExceedPointList = silicaExceedList.stream().map(ConclusionEntity::getPointId).distinct().collect(Collectors.toList());//所有超标矽尘检测点位
                    Map<String,List<ConclusionEntity>> silicaMapList = silicaList.stream().collect(Collectors.groupingBy(ConclusionEntity::getPfnId));//所有矽尘根据工种ID进行分组
                    Integer silicaTotal = 0;//初始化矽尘总接害人数
                    for (String key:silicaMapList.keySet()){
                        ConclusionEntity conclusionEntity = silicaMapList.get(key).get(0);//获取同工种第一条数据的人数相加（同工种接害人数是一样的）
                        silicaTotal = silicaTotal+conclusionEntity.getWorkerNum();
                    }
//                    Integer silicaTotal = silicaList.stream().mapToInt(i->i.getWorkerNum()).sum();//矽尘总接害人数
                    dustCategory.setSilicaNum(silicaTotal);//矽尘接触人数
                    dust.setSilicaTestNum(silicaPointList.size());//矽尘检测点数
                    dust.setSilicaExceedNum(silicaExceedPointList.size());//矽尘超标点数
                }
                else {
                    dustCategory.setSilicaNum(0);//矽尘接触人数
                    dust.setSilicaTestNum(0);//矽尘检测点数
                    dust.setSilicaExceedNum(0);//矽尘超标点数
                }
                //煤尘
                List<ConclusionEntity> coalList = dustList.stream().filter(i->i.getSubstance().contains("煤尘")).collect(Collectors.toList());
                if (coalList.size()>0){
                    List<ConclusionEntity> coalExceedList = coalList.stream().filter(i->i.getResult().equals("不符合")).collect(Collectors.toList());//煤尘超标点位

                    List<String> coalPointList = coalList.stream().map(ConclusionEntity::getPointId).distinct().collect(Collectors.toList());//所有煤尘检测点位
                    List<String> coalExceedPointList = coalExceedList.stream().map(ConclusionEntity::getPointId).distinct().collect(Collectors.toList());//所有超标煤尘检测点位
                    Map<String,List<ConclusionEntity>> coalMapList = coalList.stream().collect(Collectors.groupingBy(ConclusionEntity::getPfnId));//所有煤尘根据工种ID进行分组
                    Integer coalTotal = 0;//初始化煤尘总接害人数
                    for (String key:coalMapList.keySet()){
                        ConclusionEntity conclusionEntity = coalMapList.get(key).get(0);//获取同工种第一条数据的人数相加（同工种接害人数是一样的）
                        coalTotal = coalTotal+conclusionEntity.getWorkerNum();
                    }
//                    Integer coalTotal = coalList.stream().mapToInt(i->i.getWorkerNum()).sum();//粉尘总接害人数
                    dustCategory.setCoalNum(coalTotal);//煤尘接触人数
                    dust.setCoalTestNum(coalPointList.size());//煤尘检测点数
                    dust.setCoalExceedNum(coalExceedPointList.size());//煤尘超标点数
                }
                else {
                    dustCategory.setCoalNum(0);//煤尘接触人数
                    dust.setCoalTestNum(0);//煤尘检测点数
                    dust.setCoalExceedNum(0);//煤尘超标点数
                }
                //石棉粉尘
                List<ConclusionEntity> asbestosList = dustList.stream().filter(i->i.getSubstance().contains("石棉粉尘")).collect(Collectors.toList());
                if (asbestosList.size()>0){
                    List<ConclusionEntity> asbestosExceedList = asbestosList.stream().filter(i->i.getResult().equals("不符合")).collect(Collectors.toList());//石棉粉尘超标点位

                    List<String> asbestosPointList = asbestosList.stream().map(ConclusionEntity::getPointId).distinct().collect(Collectors.toList());//所有石棉粉尘检测点位
                    List<String> asbestosExceedPointList = asbestosExceedList.stream().map(ConclusionEntity::getPointId).distinct().collect(Collectors.toList());//所有超标石棉粉尘检测点位
                    Map<String,List<ConclusionEntity>> asbestosMapList = asbestosList.stream().collect(Collectors.groupingBy(ConclusionEntity::getPfnId));//所有石棉粉尘根据工种ID进行分组
                    Integer asbestosTotal = 0;//初始化石棉粉尘总接害人数
                    for (String key:asbestosMapList.keySet()){
                        ConclusionEntity conclusionEntity = asbestosMapList.get(key).get(0);//获取同工种第一条数据的人数相加（同工种接害人数是一样的）
                        asbestosTotal = asbestosTotal+conclusionEntity.getWorkerNum();
                    }
//                    Integer asbestosTotal = asbestosList.stream().mapToInt(i->i.getWorkerNum()).sum();//粉尘总接害人数
                    dustCategory.setAsbestosNum(asbestosTotal);//石棉粉尘接触人数
                    dust.setAsbestosTestNum(asbestosPointList.size());//石棉粉尘检测点数
                    dust.setAsbestosExceedNum(asbestosExceedPointList.size());//石棉粉尘超标点数
                }
                else {
                    dustCategory.setAsbestosNum(0);//石棉粉尘接触人数
                    dust.setAsbestosTestNum(0);//石棉粉尘检测点数
                    dust.setAsbestosExceedNum(0);//石棉粉尘超标点数
                }
                hazardCategory.setDustCategory(dustCategory);
                hazardsTestDto.setDust(dust);
            }
            else {
                dustCategory.setName("粉尘");//类型名称
                dustCategory.setIsHave("无");
                dustCategory.setTotalNum(0);//接触人数
                dustCategory.setSilicaNum(0);//矽尘接触人数
                dustCategory.setCoalNum(0);//煤尘接触人数
                dustCategory.setAsbestosNum(0);//石棉粉尘接触人数
                hazardCategory.setDustCategory(dustCategory);
                dust.setDetection("/");//是否检测
                dust.setTestNum(0);//检测点位数
                dust.setExceedNum(0);//超标点数
                dust.setSilicaTestNum(0);//矽尘检测点数
                dust.setSilicaExceedNum(0);//矽尘超标点数
                dust.setCoalTestNum(0);//煤尘检测点数
                dust.setCoalExceedNum(0);//煤尘超标点数
                dust.setAsbestosTestNum(0);//石棉粉尘检测点数
                dust.setAsbestosExceedNum(0);//石棉粉尘超标点数
                hazardsTestDto.setDust(dust);
            }
            //化学
            List<ConclusionEntity> chemistryList = list.stream().filter(i->i.getSType()==1).collect(Collectors.toList());//所有化学物质
            if (chemistryList.size()>0){
                List<ConclusionEntity> chemistryExceedList = chemistryList.stream().filter(i->i.getResult().equals("不符合")).collect(Collectors.toList());

                List<String> chemistryPointList = chemistryList.stream().map(ConclusionEntity::getPointId).distinct().collect(Collectors.toList());//所有化学检测点位
                List<String> chemistryExceedPointList = chemistryExceedList.stream().map(ConclusionEntity::getPointId).distinct().collect(Collectors.toList());//所有超标化学检测点位
                Map<String,List<ConclusionEntity>> chemistryMapList = chemistryList.stream().collect(Collectors.groupingBy(ConclusionEntity::getPfnId));//所有化学根据工种ID进行分组
                Integer chemistryTotal = 0;//初始化化学总接害人数
                for (String key:chemistryMapList.keySet()){
                    ConclusionEntity conclusionEntity = chemistryMapList.get(key).get(0);//获取同工种第一条数据的人数相加（同工种接害人数是一样的）
                    chemistryTotal = chemistryTotal+conclusionEntity.getWorkerNum();
                }

//                Integer chemistryTotal = chemistryList.stream().mapToInt(i->i.getWorkerNum()).sum();
                chemistryCategory.setName("化学物质");//类型名称
                chemistryCategory.setIsHave("有");
                chemistryCategory.setTotalNum(chemistryTotal);//接触总人数
                chemistry.setDetection("已检测");//是否检测
                chemistry.setTestNum(chemistryPointList.size());//检测点位数
                chemistry.setExceedNum(chemistryExceedPointList.size());//超标点位数
                //铅
                List<ConclusionEntity> leadList = chemistryList.stream().filter(i->i.getSubstance().contains("铅")).collect(Collectors.toList());
                if (leadList.size()>0){
                    List<ConclusionEntity> leadExceedList = leadList.stream().filter(i->i.getResult().equals("不符合")).collect(Collectors.toList());

                    List<String> leadPointList = leadList.stream().map(ConclusionEntity::getPointId).distinct().collect(Collectors.toList());//所有铅检测点位
                    List<String> leadExceedPointList = leadExceedList.stream().map(ConclusionEntity::getPointId).distinct().collect(Collectors.toList());//所有超标铅检测点位
                    Map<String,List<ConclusionEntity>> leadMapList = leadList.stream().collect(Collectors.groupingBy(ConclusionEntity::getPfnId));//所有铅根据工种ID进行分组
                    Integer leadTotal = 0;//初始化铅总接害人数
                    for (String key:leadMapList.keySet()){
                        ConclusionEntity conclusionEntity = leadMapList.get(key).get(0);//获取同工种第一条数据的人数相加（同工种接害人数是一样的）
                        leadTotal = leadTotal+conclusionEntity.getWorkerNum();
                    }

//                    Integer leadTotal = leadList.stream().mapToInt(i->i.getWorkerNum()).sum();
                    chemistryCategory.setLeadNum(leadTotal);//接触铅人数
                    chemistry.setLeadTestNum(leadPointList.size());//铅检测点数
                    chemistry.setLeadExceedNum(leadExceedPointList.size());//铅超标点数
                }
                else {
                    chemistryCategory.setLeadNum(0);//接触铅人数
                    chemistry.setLeadTestNum(0);//铅检测点数
                    chemistry.setLeadExceedNum(0);//铅超标点数
                }
                //苯
                List<ConclusionEntity> benzeneList = chemistryList.stream().filter(i->i.getSubstance().equals("苯")).collect(Collectors.toList());
                if (benzeneList.size()>0){
                    List<ConclusionEntity> benzeneExceedList = benzeneList.stream().filter(i->i.getResult().equals("不符合")).collect(Collectors.toList());

                    List<String> benzenePointList = benzeneList.stream().map(ConclusionEntity::getPointId).distinct().collect(Collectors.toList());//所有苯检测点位
                    List<String> benzeneExceedPointList = benzeneExceedList.stream().map(ConclusionEntity::getPointId).distinct().collect(Collectors.toList());//所有超标苯检测点位
                    Map<String,List<ConclusionEntity>> benzeneMapList = benzeneList.stream().collect(Collectors.groupingBy(ConclusionEntity::getPfnId));//所有苯根据工种ID进行分组
                    Integer benzeneTotal = 0;//初始化苯总接害人数
                    for (String key:benzeneMapList.keySet()){
                        ConclusionEntity conclusionEntity = benzeneMapList.get(key).get(0);//获取同工种第一条数据的人数相加（同工种接害人数是一样的）
                        benzeneTotal = benzeneTotal+conclusionEntity.getWorkerNum();
                    }

//                    Integer benzeneTotal = benzeneList.stream().mapToInt(i->i.getWorkerNum()).sum();
                    chemistryCategory.setBenzeneNum(benzeneTotal);//接触苯人数
                    chemistry.setBenzeneTestNum(benzenePointList.size());//苯检测点数
                    chemistry.setBenzeneExceedNum(benzeneExceedPointList.size());//苯超标点数
                }
                else {
                    chemistryCategory.setBenzeneNum(0);//接触苯人数
                    chemistry.setBenzeneTestNum(0);//苯检测点数
                    chemistry.setBenzeneExceedNum(0);//苯超标点数
                }
                hazardCategory.setChemistryCategory(chemistryCategory);
                hazardsTestDto.setChemistry(chemistry);
            }
            else {
                chemistryCategory.setName("化学物质");//类型名称
                chemistryCategory.setIsHave("无");
                chemistryCategory.setTotalNum(0);//接触总人数
                chemistryCategory.setLeadNum(0);//接触铅人数
                chemistryCategory.setBenzeneNum(0);//接触苯人数
                hazardCategory.setChemistryCategory(chemistryCategory);
                chemistry.setDetection("/");//是否检测
                chemistry.setTestNum(0);//检测点位数
                chemistry.setExceedNum(0);//超标点位数
                chemistry.setLeadTestNum(0);//铅检测点数
                chemistry.setLeadExceedNum(0);//铅超标点数
                chemistry.setBenzeneTestNum(0);//苯检测点数
                chemistry.setBenzeneExceedNum(0);//苯超标点数
                hazardsTestDto.setChemistry(chemistry);
            }
            //物理
            List<ConclusionEntity> physicsList = list.stream().filter(i->i.getSType()>2).collect(Collectors.toList());//所有物理因素
            if (physicsList.size()>0){
                List<ConclusionEntity> physicsExceedList = physicsList.stream().filter(i->i.getResult().equals("不符合")).collect(Collectors.toList());

                List<String> physicsPointList = physicsList.stream().map(ConclusionEntity::getPointId).distinct().collect(Collectors.toList());//所有物理检测点位
                List<String> physicsExceedPointList = physicsExceedList.stream().map(ConclusionEntity::getPointId).distinct().collect(Collectors.toList());//所有超标物理检测点位
                Map<String,List<ConclusionEntity>> physicsMapList = physicsList.stream().collect(Collectors.groupingBy(ConclusionEntity::getPfnId));//所有物理根据工种ID进行分组
                Integer physicsTotal = 0;//初始化苯物理接害人数
                for (String key:physicsMapList.keySet()){
                    ConclusionEntity conclusionEntity = physicsMapList.get(key).get(0);//获取同工种第一条数据的人数相加（同工种接害人数是一样的）
                    physicsTotal = physicsTotal+conclusionEntity.getWorkerNum();
                }

//                Integer physicsTotal = physicsList.stream().mapToInt(i->i.getWorkerNum()).sum();
                physicsCategory.setName("物理因素");//类型名称
                physicsCategory.setIsHave("有");
                physicsCategory.setTotalNum(physicsTotal);//接触总人数
                physics.setDetection("已检测");//是否检测
                physics.setTestNum(physicsPointList.size());//检测点位数
                physics.setExceedNum(physicsExceedPointList.size());//超标点位数
                //噪声
                List<ConclusionEntity> noiseList = physicsList.stream().filter(i->i.getSubstance().contains("噪声")).collect(Collectors.toList());
                if (noiseList.size()>0){
                    List<ConclusionEntity> noiseExceedList = noiseList.stream().filter(i->i.getResult().equals("不符合")).collect(Collectors.toList());

                    List<String> noisePointList = noiseList.stream().map(ConclusionEntity::getPointId).distinct().collect(Collectors.toList());//所有噪声检测点位
                    List<String> noiseExceedPointList = noiseExceedList.stream().map(ConclusionEntity::getPointId).distinct().collect(Collectors.toList());//所有超标噪声检测点位
                    Map<String,List<ConclusionEntity>> noiseMapList = noiseList.stream().collect(Collectors.groupingBy(ConclusionEntity::getPfnId));//所有噪声根据工种ID进行分组
                    Integer noiseTotal = 0;//初始化噪声总接害人数
                    for (String key:noiseMapList.keySet()){
                        ConclusionEntity conclusionEntity = noiseMapList.get(key).get(0);//获取同工种第一条数据的人数相加（同工种接害人数是一样的）
                        noiseTotal = noiseTotal+conclusionEntity.getWorkerNum();
                    }

//                    Integer noiseTotal = noiseList.stream().mapToInt(i->i.getWorkerNum()).sum();
                    physicsCategory.setNoiseNum(noiseTotal);//接触噪声人数
                    physics.setNoiseTestNum(noisePointList.size());//噪声检测点数
                    physics.setNoiseExceedNum(noiseExceedPointList.size());//噪声超标点数
                }
                else {
                    physicsCategory.setNoiseNum(0);//接触噪声人数
                    physics.setNoiseTestNum(0);//噪声检测点数
                    physics.setNoiseExceedNum(0);//噪声超标点数
                }
                hazardCategory.setPhysicsCategory(physicsCategory);
                hazardsTestDto.setPhysics(physics);
            }
            else {
                physicsCategory.setName("物理因素");//类型名称
                physicsCategory.setIsHave("无");
                physicsCategory.setTotalNum(0);//接触总人数
                physicsCategory.setNoiseNum(0);//接触噪声人数
                hazardCategory.setPhysicsCategory(physicsCategory);
                physics.setDetection("/");//是否检测
                physics.setTestNum(0);//检测点位数
                physics.setExceedNum(0);//超标点位数
                physics.setNoiseTestNum(0);//噪声检测点数
                physics.setNoiseExceedNum(0);//噪声超标点数
                hazardsTestDto.setPhysics(physics);
            }
        }
        else {
            dustCategory.setName("粉尘");//类型名称
            dustCategory.setIsHave("无");
            dustCategory.setTotalNum(0);//接触人数
            dustCategory.setSilicaNum(0);//矽尘接触人数
            dustCategory.setCoalNum(0);//煤尘接触人数
            dustCategory.setAsbestosNum(0);//石棉粉尘接触人数
            hazardCategory.setDustCategory(dustCategory);
            chemistryCategory.setName("化学物质");//类型名称
            chemistryCategory.setIsHave("无");
            chemistryCategory.setTotalNum(0);//接触总人数
            chemistryCategory.setLeadNum(0);//接触铅人数
            chemistryCategory.setBenzeneNum(0);//接触苯人数
            hazardCategory.setChemistryCategory(chemistryCategory);
            physicsCategory.setName("物理因素");//类型名称
            physicsCategory.setIsHave("无");
            physicsCategory.setTotalNum(0);//接触总人数
            physicsCategory.setNoiseNum(0);//接触噪声人数
            hazardCategory.setPhysicsCategory(physicsCategory);
            dust.setDetection("/");//是否检测
            dust.setTestNum(0);//检测点位数
            dust.setExceedNum(0);//超标点数
            dust.setSilicaTestNum(0);//矽尘检测点数
            dust.setSilicaExceedNum(0);//矽尘超标点数
            dust.setCoalTestNum(0);//煤尘检测点数
            dust.setCoalExceedNum(0);//煤尘超标点数
            dust.setAsbestosTestNum(0);//石棉粉尘检测点数
            dust.setAsbestosExceedNum(0);//石棉粉尘超标点数
            hazardsTestDto.setDust(dust);
            chemistry.setDetection("/");//是否检测
            chemistry.setTestNum(0);//检测点位数
            chemistry.setExceedNum(0);//超标点位数
            chemistry.setLeadTestNum(0);//铅检测点数
            chemistry.setLeadExceedNum(0);//铅超标点数
            chemistry.setBenzeneTestNum(0);//苯检测点数
            chemistry.setBenzeneExceedNum(0);//苯超标点数
            hazardsTestDto.setChemistry(chemistry);
            physics.setDetection("/");//是否检测
            physics.setTestNum(0);//检测点位数
            physics.setExceedNum(0);//超标点位数
            physics.setNoiseTestNum(0);//噪声检测点数
            physics.setNoiseExceedNum(0);//噪声超标点数
            hazardsTestDto.setPhysics(physics);
        }
        radiationCategory.setName("放射性物质");//类型名称
        radiationCategory.setIsHave("无");
        radiationCategory.setTotalNum(0);//接触总人数
        hazardCategory.setRadiationCategory(radiationCategory);
        biologyCategory.setName("生物因素");//类型名称
        biologyCategory.setIsHave("无");
        biologyCategory.setTotalNum(0);//接触总人数
        hazardCategory.setBiologyCategory(biologyCategory);
        otherCategory.setName("其他因素");//类型名称
        otherCategory.setIsHave("无");
        otherCategory.setTotalNum(0);//接触总人数
        hazardCategory.setOtherCategory(otherCategory);
        radiation.setDetection("/");//是否检测
        radiation.setTestNum(0);//检测点位数
        radiation.setExceedNum(0);//超标点位数
        hazardsTestDto.setRadiation(radiation);
        biology.setDetection("/");//是否检测
        biology.setTestNum(0);//检测点位数
        biology.setExceedNum(0);//超标点位数
        hazardsTestDto.setBiology(biology);
//        other.setDetection("/");//是否检测
//        other.setTestNum(0);//检测点位数
//        other.setExceedNum(0);//超标点位数
//        hazardsTestDto.setOther(other);
        declareIndexEntity.setHazardCategory(hazardCategory);
        declareIndexEntity.setHazardsTest(hazardsTestDto);


        declareIndexEntity.setCreateTime(new Date());
        mongoTemplate.insert(declareIndexEntity);

//        return declareIndexEntity;
    }



    public void deleteByProjecrId(Long projectId){
        Query query = new Query(Criteria.where("project_id").is(projectId));
        mongoTemplate.remove(query, DeclareIndexEntity.class);
    }

    /**
     * 导出word
     * @param response
     */
    public void exportWord(HttpServletResponse response, DeclareIndexEntity declareIndexEntity) {
        ProjectEntity project = projectService.getById(declareIndexEntity.getProjectId());
        Map<String,Object> map = new HashMap<>();
        map.put("information",declareIndexEntity.getInformation());
        map.put("detection",declareIndexEntity.getDetection());
        map.put("productOutputList",declareIndexEntity.getProductOutputList());
        map.put("dustCategory",declareIndexEntity.getHazardCategory().getDustCategory());
        map.put("chemistryCategory",declareIndexEntity.getHazardCategory().getChemistryCategory());
        map.put("physicsCategory",declareIndexEntity.getHazardCategory().getPhysicsCategory());
        map.put("radiationCategory",declareIndexEntity.getHazardCategory().getRadiationCategory());
        map.put("biologyCategory",declareIndexEntity.getHazardCategory().getBiologyCategory());
        map.put("otherCategory",declareIndexEntity.getHazardCategory().getOtherCategory());
        map.put("dust",declareIndexEntity.getHazardsTest().getDust());
        map.put("chemistry",declareIndexEntity.getHazardsTest().getChemistry());
        map.put("physics",declareIndexEntity.getHazardsTest().getPhysics());
        map.put("radiation",declareIndexEntity.getHazardsTest().getRadiation());
        map.put("biology",declareIndexEntity.getHazardsTest().getBiology());

        String fileName = project.getIdentifier()+project.getCompany()+"申报索引.docx";
        try {
            String filePath =  FreemarkerDocxUtil.freemarkerDocxTest("DeclareIndex.ftl", "DeclareIndex.zip", map, null, null,"DeclareFooter.ftl");

            FreemarkerDocxUtil.returnResponse(fileName,filePath,response);

        } catch (Exception e ){
            log.error("生成word失败",e);
        }
    }
}
