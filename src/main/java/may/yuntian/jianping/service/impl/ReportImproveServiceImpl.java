package may.yuntian.jianping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.service.ProjectService;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.jianping.entity.CompanySurveyEntity;
import may.yuntian.jianping.entity.QuestionAdviceProjectEntity;
import may.yuntian.jianping.entity.ReportImprove;
import may.yuntian.jianping.mapper.ReportImproveMapper;
import may.yuntian.jianping.mongoentity.ResultEntity;
import may.yuntian.jianping.mongoentity.WorkspaceEntity;
import may.yuntian.jianping.mongoservice.PostPfnService;
import may.yuntian.jianping.mongoservice.ResultService;
import may.yuntian.jianping.mongoservice.WorkspaceService;
import may.yuntian.jianping.service.CompanySurveyService;
import may.yuntian.jianping.service.QuestionAdviceProjectService;
import may.yuntian.jianping.service.ReportImproveService;
import may.yuntian.jianping.vo.VerificationsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 报告信息完善记录
 *
 * @author LiXin
 * @date 2020-09-29
 */
@Service("reportImproveService")
public class ReportImproveServiceImpl extends ServiceImpl<ReportImproveMapper, ReportImprove> implements ReportImproveService {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private CompanySurveyService companySurveyService;
    @Autowired
    private WorkspaceService workspaceService;
    @Autowired
    private PostPfnService postPfnService;
    @Autowired
    private ResultService resultService;
    @Autowired
    private QuestionAdviceProjectService questionAdviceProjectService;



    /**
     * 初始化报告信息完善信息
     *
     * @param projectId
     */
    public void initializeReport(Long projectId) {

        Integer rpcount = baseMapper.selectCount(new QueryWrapper<ReportImprove>().eq("project_id", projectId));
        if (rpcount > 0) {
            baseMapper.delete(new QueryWrapper<ReportImprove>()
                    .eq("project_id", projectId)
            );
        }
        questionAdviceProjectService.deleteReportImproveId(projectId);
        List<QuestionAdviceProjectEntity> questionAdviceProjectEntityList = new ArrayList<>();

        ReportImprove reportImprove = new ReportImprove();
        CompanySurveyEntity companySurvey = companySurveyService.getOne(projectId);
        ProjectEntity project = projectService.getById(projectId);
        reportImprove.setProjectId(projectId);

        //业务来源
        reportImprove.setSource("受"+project.getEntrustCompany()+"委托");

        //检测类别
        reportImprove.setTestType("工作场所职业病危害因素"+companySurvey.getDetectionType());

        //检测范围
        reportImprove.setTestRange(companySurvey.getCompany() + "位于" + companySurvey.getOfficeAddress() + "的" + companySurvey.getTestPlace());

        //劳动定员及工种（岗位）设置情况
        Map<String,Double> map = postPfnService.getPeopleNumByProjectId(projectId);
        Integer day = 0;

        if (map.get("day")==4){
            day = 200;
        }else if (map.get("day")==5){
            day = 240;
        }else if (map.get("day")==6){
            day = 280;
        }
//        switch (String.valueOf() map.get("day")) {
//            case 4:
//                day = 200;
//                break;
//            case 5:
//                day = 240;
//                break;
//            case 6:
//                day = 280;
//                break;
//            default:
//                break;
//        }
        reportImprove.setPostSetting("用人单位劳动总定员" + companySurvey.getLaborQuota() + "人,其中本次评价范围内涉及的生产作业人员共" + map.get("num") + "人，其余为行政，后勤，销售人员，年工作日" + day + "天。");

        //检测频次说明
        List<WorkspaceEntity> workspaceEntityList = workspaceService.getIdentifyHazardsByprojectId(projectId);
        String production = "";
        for (WorkspaceEntity workspaceEntity:workspaceEntityList){
            String identifyItem = "";
            String places = "";
            if (workspaceEntity.getPointIds().size()>0){
                String[] identifyHazards = workspaceEntity.getOnlyIdentify().split("、");
                identifyItem = String.join(",",identifyHazards);
                List<String> placeList = new ArrayList<>();
                for (String pointId : workspaceEntity.getPointIds()){
                    String point = workspaceEntity.getPointMap().get(pointId).getPoint();
                    String place =workspaceEntity.getWorkshop()+"/"+workspaceEntity.getPost()+"/"+point+",";
                    placeList.add(place);
                }
                places = placeList.stream().distinct().collect(Collectors.joining(","));
            }
            production = production + places + "的" + identifyItem + "的作业频率低，且现场检则时未作业,故本次对该岗位接触的职业病危害因素仅作识别不对其进行检测与评价，工人作业时需做好个人防护。" + "\n";
        }
        reportImprove.setProduction(production);

        //验证性检测及评价小结中验证性检测内容
        //验证性检测
        String confirmatory="";
        //验证性检测小结
        String confirmatory2="";
        //------验证不检测
        String s1="|苯|";
        String s2="|正己烷|";
        String s3="|1,2-二氯乙烷|";
        String s4="|三氯甲烷（氯仿）|";
        //获取验证性检测物质列表
        List<VerificationsVo> verificationsVos = resultService.getList(projectId,null);
        if(verificationsVos!=null&&verificationsVos.size()>0){
            List<String> collect = verificationsVos.stream().distinct().map(VerificationsVo::getTestItem).distinct().collect(Collectors.toList());
            List<Integer> collect2 = verificationsVos.stream().distinct().map(VerificationsVo::getMinLimit).distinct().collect(Collectors.toList());
            String join = String.join("|", collect);
            String join2 = String.join( collect2.toString());
            join="|"+join+"|";
            if(join.contains(s1)&&join.contains(s2)&&join.contains(s3)&&join.contains(s4)) {
                if (collect.size() > 4) {//类型2
                    //四个物质
                    ArrayList<VerificationsVo> verificationsVos3 = new ArrayList<>();
                    // 其他
                    ArrayList<VerificationsVo> verificationsVos4 = new ArrayList<>();

                    for (VerificationsVo verificationsVo : verificationsVos) {
                        if (verificationsVo.getTestItem().equals(s1) || verificationsVo.getTestItem().equals(s2) || verificationsVo.getTestItem().equals(s3) || verificationsVo.getTestItem().equals(s4)) {
                            verificationsVos3.add(verificationsVo);
                        } else {
                            verificationsVos4.add(verificationsVo);
                        }
                    }
                    //其他 部门
                    List<String> stringList2 = verificationsVos4.stream().distinct().map(VerificationsVo::getPlace).distinct().collect(Collectors.toList());
                    String join22 = String.join(",", stringList2);
                    //其他 物质
                    List<String> stringList3 = verificationsVos4.stream().distinct().map(VerificationsVo::getTestItem).distinct().collect(Collectors.toList());
                    String join3 = String.join(",", stringList3);
                    confirmatory = "根据《建设项目职业病危害风险分类管理目录》国卫办职健发〔2021〕5号的要求，" +
                            "本次检测针对相关岗位可能存在的苯、正己烷、1，2-二氯乙烷、三氯甲烷进行了验证性检测；根据企业提供的MSDS，还针对" + join22 + "可能存在的" + join3 + "进行了验证性检测。";
                    //四个物质未检出
                    List<String> stringLista = verificationsVos3.stream().filter(i -> i.getMinLimit() == 2).map(VerificationsVo::getTestItem).distinct().collect(Collectors.toList());
                    //四个物质已检出
                    List<String> stringListb = verificationsVos3.stream().filter(i -> i.getMinLimit() == 1).map(VerificationsVo::getTestItem).distinct().collect(Collectors.toList());
                    String stringA = "";
                    String stringB = "";
                    String sC = "一般";
                    if (stringLista != null && stringLista.size() > 0) {
                        stringA = String.join(",", stringLista) + ",";

                    }
                    if (stringListb != null && stringListb.size() > 0) {
                        stringB = String.join(",", stringListb) + ",";
                        sC = "严重";
                    }
                    //其他未检出
                    List<String> stringListaa = verificationsVos4.stream().filter(i -> i.getMinLimit() == 2).map(VerificationsVo::getTestItem).distinct().collect(Collectors.toList());
                    //其他已检出
                    List<String> stringListbb = verificationsVos4.stream().filter(i -> i.getMinLimit() == 1).map(VerificationsVo::getTestItem).distinct().collect(Collectors.toList());
                    String stringAa = "";
                    String stringBb = "";

                    if (stringListaa != null && stringListaa.size() > 0) {
                        stringAa = String.join(",", stringListaa) + ",";

                    }
                    if (stringListbb != null && stringListbb.size() > 0) {
                        stringBb = String.join(",", stringListbb) + ",";
                    }

                    confirmatory2 = "验证性检测项目" + stringA + stringB + "故本项目按职业病危害" + sC + "进行管理；" + stringAa + stringBb + "。";

                }else {//类型1
                    confirmatory="根据《建设项目职业病危害风险分类管理目录》国卫办职健发〔2021〕5号的要求，本次检测还针对相关岗位可能存在的苯、正己烷、1，2-二氯乙烷、三氯甲烷进行了验证性检测。";
                    if(join2.contains("1")&&join2.contains("2")){//检出，未检出都存在
                        List<VerificationsVo> verificationsVos1 = resultService.getList(projectId, 1);
                        List<VerificationsVo> verificationsVos2 = resultService.getList(projectId, 2);
                        //检测
                        List<String> strings1 = verificationsVos1.stream().distinct().map(VerificationsVo::getTestItem).collect(Collectors.toList());
                        String join1 = String.join(",", strings1);
                        //未检测
                        List<String> strings2 = verificationsVos2.stream().distinct().map(VerificationsVo::getTestItem).collect(Collectors.toList());
                        String join3 = String.join(",", strings2);

                        confirmatory2="验证性检测项目"+join3+"未检出，"+join1+"已检出，故本项目按职业病危害严重进行管理。";
                    }else if(join2.contains("1")) {//检出存在
//                        List<VerificationsVo> verificationsVos1 = baseMapper.selectVerificationsVo2(projectId, 1);
                        confirmatory2="验证性检测项目苯、正己烷、1，2-二氯乙烷、三氯甲烷均检出，故本项目按职业病危害严重进行管理。";
                    }else {//检出不存在
                        confirmatory2="验证性检测项目苯、正己烷、1，2-二氯乙烷、三氯甲烷均未检出，故本项目按职业病危害一般进行管理。";
                    }

                }
            }else {//类型3
                //所有物质，部门
                List<String> collectc = verificationsVos.stream().map(VerificationsVo::getTestItem).distinct().collect(Collectors.toList());
                List<String> collectd = verificationsVos.stream().map(VerificationsVo::getPlace).distinct().collect(Collectors.toList());
                String join1 = String.join("、", collectc);
                String join2a = String.join("、", collectd);
                confirmatory="根据企业提供的MSDS，还针对"+join2a+"可能存在的"+join1+"进行了验证性检测。";
                //未检出
                List<String> collecta = verificationsVos.stream().filter(i->i.getMinLimit()==2).map(VerificationsVo::getTestItem).distinct().collect(Collectors.toList());
                //已检出
                List<String> collect2b = verificationsVos.stream().filter(i->i.getMinLimit()==1).map(VerificationsVo::getTestItem).distinct().collect(Collectors.toList());
                String sa="";
                String sb="";
                if(collecta!=null&&collecta.size()>0){
                    sa = String.join("、", collecta)+",";
                }
                if(collect2b!=null&&collect2b.size()>0){
                    sb = String.join("、", collect2b)+",";
                }
                confirmatory2="验证性检测项目"+sa+sb+"。";
            }

        }
        reportImprove.setConfirmatory(confirmatory);//验证性检测

        //检测评价小结
        String summary = "";
        //化学物质-------------------------------------------------------------------------------
        //不合格
        List<ResultEntity> noList = resultService.getTestItemResult(projectId,"airFixed",1);
        if (noList.size()>0){
            //获取化学点位数量   .filter(i->i.getIsFixed()==1)
            List<String> ids = noList.stream().map(ResultEntity::getTestPlace).distinct().collect(Collectors.toList());
            int count = ids.size();

            //获取各监测点的检测物质
            Map<String,List<ResultEntity>> map1 = noList.stream().collect(Collectors.groupingBy(ResultEntity::getTestPlace));
            String pc = "";
            for (String key:map1.keySet()){
                List<ResultEntity> testItemList = map1.get(key);
                String substance = "";
                for (ResultEntity resultEntity:testItemList){
                    substance = substance+resultEntity.getSubstance().getName();
                }
                pc = pc + key + "的" + substance + ",";
            }
            //是否有粉尘
            Integer dsutType = 2;
            List<ResultEntity> dustList = noList.stream().filter(i->dsutType.equals(i.getSubstance().getSType())).collect(Collectors.toList());
            //是否有毒物
            Integer airType = 1;
            List<ResultEntity> airList = noList.stream().filter(i->airType.equals(i.getSubstance().getSType())).collect(Collectors.toList());
            String str = "";
            if (airList.size()>0 && dustList.size()<=0) {
                str = "化学有害因素浓度";
            } else if (airList.size()<=0 && dustList.size()>0) {
                str = "粉尘浓度";
            } else if (airList.size()>0 && dustList.size()>0) {
                str = "化学有害因素及粉尘浓度";
            }
            //超标物质
            summary = "本次检测作业场所" + count + "个岗位的" + str + "，其中" + pc
                    + "浓度检测结果不符合GBZ2.1-2019《工作场所有害因素职业接触限值 第1部分：化学有害因素》的职业接触限值要求，其余检测项目检测结果均符合GBZ2.1-2019的职业限值要求。"
                    + "\n" + "超标原因分析：" + "\n";

            String openQuestion = "";
            String advice = pc+"不符合《工作场所有害因素职业接触限值 第1部分：化学有害因素》GBZ 2.1-2019的要求，用人单位应引起重视，可采取如下措施:";
            QuestionAdviceProjectEntity questionAdviceProjectEntity = new QuestionAdviceProjectEntity();
            questionAdviceProjectEntity.setProjectId(projectId);
            questionAdviceProjectEntity.setQuestionAdviceId(Long.valueOf(0));
            questionAdviceProjectEntity.setOpenQuestion(openQuestion);
            questionAdviceProjectEntity.setAdvice(advice);
            questionAdviceProjectEntity.setLocation(1);
            questionAdviceProjectEntityList.add(questionAdviceProjectEntity);


        }

        //合格
        List<ResultEntity> okList = resultService.getTestItemResult(projectId,"airFixed",2);
        if (okList.size()>0){
            //获取化学点位数量  .filter(i->i.getIsFixed()==1)
            List<String> ids = okList.stream().map(ResultEntity::getTestPlace).distinct().collect(Collectors.toList());
            int count = ids.size();

            //是否有粉尘
            Integer dsutType = 2;
            List<ResultEntity> dustList = noList.stream().filter(i->dsutType.equals(i.getSubstance().getSType())).collect(Collectors.toList());
            //是否有毒物
            Integer airType = 1;
            List<ResultEntity> airList = noList.stream().filter(i->airType.equals(i.getSubstance().getSType())).collect(Collectors.toList());
            String str = "";
            if (airList.size()>0 && dustList.size() <= 0) {
                str = "化学有害因素浓度";
            } else if (airList.size() <= 0 && dustList.size()>0) {
                str = "粉尘浓度";
            } else if (airList.size()>0 && dustList.size()>0) {
                str = "化学有害因素及粉尘浓度";
            }
            summary = summary + "本次检测作业场所" + count + "个岗位的" + str + "，所检测岗位检测结果均符合GBZ2.1-2019《工作场所有害因素职业接触限值 第1部分：化学有害因素》的职业限值要求。";
        }
        summary=summary+"\n"+confirmatory2;

        //-------------------------- 噪声 --------------------------
        List<ResultEntity> allNoiseList = new ArrayList<>();
        List<ResultEntity> noiseOkList = resultService.getTestItemResult(projectId,"noiseFixed",2);
        List<ResultEntity> noiseIndividualOkList = resultService.getTestItemResult(projectId,"noiseIndividual",2);
        noiseOkList.addAll(noiseIndividualOkList);
        allNoiseList.addAll(noiseOkList);
        List<ResultEntity> noiseNoList = resultService.getTestItemResult(projectId,"noiseFixed",1);
        List<ResultEntity> noiseIndividualNoList = resultService.getTestItemResult(projectId,"noiseIndividual",1);
        noiseNoList.addAll(noiseIndividualNoList);
        allNoiseList.addAll(noiseNoList);
        String str = "";
        //获取点位数量   .filter(i->i.getIsFixed()==1)
        List<String> ids = allNoiseList.stream().map(ResultEntity::getTestPlace).distinct().collect(Collectors.toList());
        int count = ids.size();
        str="本次共检测作业场所"+count+"个岗位（工种）噪声强度，";

        String substring = "";
        //超标作业地点
        if (noiseNoList.size()>0){
            Map<String,List<ResultEntity>> map2 = noiseNoList.stream().collect(Collectors.groupingBy(ResultEntity::getTestPlace));
            String cb = "";
            for (String key:map2.keySet()){
                cb = cb+key+"、";
            }
            substring = cb.substring(0, cb.length() - 1);
            str=str+"其中"+substring+"的作业人员在工作时间内接触的噪声强度不符合GBZ2.2-2007《工作场所有害因素职业接触限值 第2部分：物理因素》所规定的限值要求，其余";

            String openQuestion = "本次检测结果中"+substring+"的噪声检测结果不符合GBZ2.2-2007所规定的限值要求。";
            String advice = "本次检测结果中"+substring+"的噪声检测结果不符合GBZ2.2-2007所规定的限值要求，用人单位应引起重视。正确佩戴防噪声耳塞是对噪声作业岗位最有效的补偿性措施，用人单位目前配发的 XXX 可达到有效防护。因此用人单位应加强对噪声作业人员正确佩戴防护耳塞的培训和监督管理，确保在佩戴符合要求的防噪声耳塞后方可从事噪声作业。另外，在生产允许的前提下，优先选择低噪声的XXXX或对车间内使用的高噪声设备采取相应的吸声、隔声等噪声控制措施，降低作业场所噪声强度；并定期对高噪声设备进行维护保养，避免异响发生；并合理安排工人作业时间，尽量缩短噪声持续作业时间。";
            QuestionAdviceProjectEntity questionAdviceProjectEntity = new QuestionAdviceProjectEntity();
            questionAdviceProjectEntity.setProjectId(projectId);
            questionAdviceProjectEntity.setQuestionAdviceId(Long.valueOf(0));
            questionAdviceProjectEntity.setOpenQuestion(openQuestion);
            questionAdviceProjectEntity.setAdvice(advice);
            questionAdviceProjectEntity.setLocation(1);
            questionAdviceProjectEntityList.add(questionAdviceProjectEntity);

        }
        str=str+"各岗位的作业人员在工作时间内接触的噪声符合GBZ2.2-2007的限值要求";
        //非噪声
        List<ResultEntity> notNoiseList = noiseOkList.stream().filter(i->Double.valueOf(StringUtils.isNotBlank(i.getBatchSampleLis().get(0).getResult()) ? i.getBatchSampleLis().get(0).getResult():"0")<80).collect(Collectors.toList());
        if (notNoiseList.size()>0){
            String fzs=",且";
            Map<String,List<ResultEntity>> map3 = notNoiseList.stream().collect(Collectors.groupingBy(ResultEntity::getTestPlace));
            for (String key:map3.keySet()){
                fzs = fzs+key+"、";
            }
            str=str+fzs+"的噪声40h等效声级均小于80dB(A),不属于噪声作业岗位。";
        }
        //非噪声作业岗位
        List<ResultEntity> notNoistPostList = noiseOkList.stream().filter(i->Double.valueOf(StringUtils.isNotBlank(i.getConclusion().getLimitV()) ? i.getConclusion().getLimitV():"0") < 80).collect(Collectors.toList());
        if (notNoistPostList.size()>0){
            //非噪声工作地点噪声超标的超标
            String fzsgw="本次检测"+notNoistPostList.size()+"个点非噪声工作地点的噪声，检测结果除 XXX/ XXX  / XXX  不符合GBZ1-2010《工业企业设计卫生标准》规定的非噪声工作地点噪声声级要求，其余点位检测结果均符合GBZ1-2010《工业企业设计卫生标准》规定的非噪声工作地点噪声声级要求。\n" +
                    "非噪声工作地点噪声超标原因分析：  XXX/ XXX  / XXX  运行产生的噪声影响较大，且XXX 隔声状况较差，未能有效隔声，致使XXX的噪声不符合GBZ1-2010《工业企业设计卫生标准》规定的非噪声工作地点噪声声级要求。";
            str =str+fzsgw;
        }
        if (noiseNoList.size()>0){
            str =str+"\n噪声超标原因分析："+substring+"的   XXX 、XXX  、 XXX  为高噪声设备，运行时会产生高噪声，且设备之间未设置有效的减振、隔声、吸声等降噪措施，致使噪声相互叠加，工人接噪时间又长，致使以上岗位该噪声超标\n" +
                    "补救措施：用人单位已为噪声超标岗位的作业人员配发了 XXXXXX ，若作业人员能正确佩戴防噪耳塞可有效防护。\n";
        }
        summary=summary+str;

        //--------------------------------------紫外辐射-----------------------------------------------  ultraviolet
        List<ResultEntity> ultravioletList = resultService.getTestItemResult(projectId,"ultraviolet",null);

        //--------------------------------------高温岗位-----------------------------------------------temperatureStable
        List<ResultEntity> temperatureStableList = resultService.getTestItemResult(projectId,"temperatureStable",null);

        //--------------------------------------工频电场-----------------------------------------------electric
        List<ResultEntity> electricList = resultService.getTestItemResult(projectId,"electric",null);

        //--------------------------------------手传振动-----------------------------------------------vibrationHand
        List<ResultEntity> vibrationHandList = resultService.getTestItemResult(projectId,"vibrationHand",null);


        if(ultravioletList!=null&&ultravioletList.size()>0){
            summary=summary+"本次共检测"+ultravioletList.size()+"个点紫外辐射，所有岗位的紫外辐射符合GBZ2.2-2007《工作场所有害因素职业接触限值 第2部分：物理因素》的限值要求。";
        }
        if(temperatureStableList!=null&&temperatureStableList.size()>0){
            summary=summary+"本次检测共"+temperatureStableList.size()+"个高温岗位的WBGT指数，所有岗位作业人员在工作时间内接触的高温WBGT指数均符合GBZ2.2-2007的职业限值要求。";
        }
        if(electricList!=null&&electricList.size()>0){
            summary=summary+"本次检测共"+electricList.size()+"个点工频电场，检测结果符合GBZ2.2-2007《工作场所有害因素职业接触限值 第2部分：物理因素》的职业限值要求。";
        }
        if(vibrationHandList!=null&&vibrationHandList.size()>0){
            summary=summary+"本次检测作业场所"+vibrationHandList.size()+"个点位的手传振动，所检测岗位工人接触的手传振动均符合GBZ2.2-2007《工作场所有害因素职业接触限值 第2部分：物理因素》所规定的限值要求。";
        }
        reportImprove.setSummary(summary);
        this.save(reportImprove);
        questionAdviceProjectService.saveBatch(questionAdviceProjectEntityList);

    }



}
