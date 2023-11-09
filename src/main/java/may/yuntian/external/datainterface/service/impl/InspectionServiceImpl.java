package may.yuntian.external.datainterface.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.common.exception.RRException;
import may.yuntian.external.datainterface.constant.ChineseCharacterConstants;
import may.yuntian.external.datainterface.constant.NumConstants;
import may.yuntian.external.datainterface.constant.SymbolicConstants;
import may.yuntian.external.datainterface.dao.InspectionMapper;
import may.yuntian.external.datainterface.dao.SubstanceContrastResultMapper;
import may.yuntian.external.datainterface.entity.EvalPlanRecordReviseEntity;
import may.yuntian.external.datainterface.entity.EvalResultEntity;
import may.yuntian.external.datainterface.entity.SubstanceContrastResult;
import may.yuntian.external.datainterface.pojo.bo.ProParticipantTableBO;
import may.yuntian.external.datainterface.pojo.vo.ProBasicInfoVO;
import may.yuntian.external.datainterface.pojo.vo.ProParticipantTableVO;
import may.yuntian.external.datainterface.pojo.vo.ResultVo;
import may.yuntian.external.datainterface.service.InspectionService;
import may.yuntian.jianping.dto.ResultSubstanceDto;
import may.yuntian.jianping.entity.IndustryEntity;
import may.yuntian.sys.utils.ShiroUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 评价 Service实现层
 *
 * @author cwt
 * @Create 2023-4-12 17:22:06
 */
@Service
@Slf4j
public class InspectionServiceImpl implements InspectionService {

    @Resource
    private InspectionMapper inspectionMapper;
    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private SubstanceContrastResultMapper resultMapper;

    /**
     * 自定义危害因素类型常量：1化学，2粉尘，3噪声，4高温、5紫外辐射。
     */
    private static final List<Integer> CHEMICAL_WAIT_TYPES = Arrays.asList(1, 2, 3, 4, 5);

    /**
     * 评价-基本信息
     */
    @Override
    public ProBasicInfoVO getInGeneralInfo(Long projectId) {
        ProBasicInfoVO proBasicInfoVO = inspectionMapper.getInGeneralInfo(projectId);
        switch (ShiroUtils.getUserEntity().getSubjection()) {
            case "杭州安联":
                proBasicInfoVO.setIssuer("王勇");
                break;
            case "宁波安联":
                proBasicInfoVO.setIssuer("钟狄阳");
                break;
            case "嘉兴安联":
                proBasicInfoVO.setIssuer("张袁金");
                break;
            default:
                break;
        }
        String checkType = proBasicInfoVO.getCheckType();
        // 检测类型
        if (ChineseCharacterConstants.PRESENT_SITUATION.equals(checkType)) {
            proBasicInfoVO.setCheckType(NumConstants.THIRTY_ONE);
        } else if (ChineseCharacterConstants.CONTROL_EVALUATION.equals(checkType)) {
            proBasicInfoVO.setCheckType(NumConstants.THIRTY_TWO);
        } else {
            throw new RRException("该项目类型暂不允许申报!!!");
        }
        // 委托机构统一社会信用代码
        if (proBasicInfoVO.getEmpName().equals(proBasicInfoVO.getEntrustOrgName())) {
            proBasicInfoVO.setEntrustCreditCode(proBasicInfoVO.getEmpCreditCode());
        } else {
            proBasicInfoVO.setEntrustCreditCode(ChineseCharacterConstants.EMPTY);
        }
        String substring = "";
        String industryCategory = proBasicInfoVO.getIndustryCategory();
        if (industryCategory == null) {
            industryCategory = ChineseCharacterConstants.EMPTY;
        } else if (industryCategory.contains(SymbolicConstants.Et)) {
            int index = industryCategory.indexOf(SymbolicConstants.Et);
            substring = StringUtils.substring(industryCategory, 0, index);
        } else {
            substring = industryCategory;
        }
        // 行业类别+行业类别编码
        IndustryEntity industryEntity = inspectionMapper.getLetterAndCodeAndNameByJoint(substring);
        if (industryEntity == null) {
            proBasicInfoVO.setIndustryCategory(ChineseCharacterConstants.EMPTY);
            proBasicInfoVO.setIndustryCategoryCode(ChineseCharacterConstants.EMPTY);
        } else {
            proBasicInfoVO.setIndustryCategory(industryEntity.getName());
            String string = industryEntity.getLetter() + industryEntity.getCode();
            String industryCategoryCode = "";
            int length = string.length();
            if (length <= NumConstants.NUMBER_FIVE) {
                industryCategoryCode = StringUtils.substring(string, 1);
            } else {
                industryCategoryCode = StringUtils.substring(string, 1, 5);
            }
            proBasicInfoVO.setIndustryCategoryCode(industryCategoryCode);
        }
        proBasicInfoVO.setEconomicType(ChineseCharacterConstants.EMPTY);
        // 经济类型编码
        proBasicInfoVO.setEconomicTypeCode(ChineseCharacterConstants.EMPTY);
        // 企业规模编码
        proBasicInfoVO.setScaleCode(ChineseCharacterConstants.EMPTY);
        // 技术服务地区编码
        proBasicInfoVO.setServiceAreaCode(ChineseCharacterConstants.EMPTY);
        // 技术服务领域编码
        proBasicInfoVO.setFieldCode(ChineseCharacterConstants.EMPTY);
        String employeesTotalNum = proBasicInfoVO.getEmployeesTotalNum();
        if (NumConstants.ZERO.equals(employeesTotalNum) || StrUtil.isBlank(employeesTotalNum)) {
            proBasicInfoVO.setEmployeesTotalNum(ChineseCharacterConstants.EMPTY);
            proBasicInfoVO.setScaleCode(NumConstants.FIVE);
        } else {
            Integer integer = Integer.valueOf(employeesTotalNum);
            if (integer > NumConstants.NUMBER_ZERO && integer < NumConstants.NUMBER_TWENTY) {
                proBasicInfoVO.setScaleCode(NumConstants.FOUR);
            } else if (integer >= NumConstants.NUMBER_TWENTY && integer < NumConstants.NUMBER_THREE_HUNDRED) {
                proBasicInfoVO.setScaleCode(NumConstants.THREE);
            } else if (integer >= NumConstants.NUMBER_THREE_HUNDRED && integer < NumConstants.NUMBER_ONE_THOUSAND) {
                proBasicInfoVO.setScaleCode(NumConstants.TWO);
            } else if (integer >= NumConstants.NUMBER_ONE_THOUSAND) {
                proBasicInfoVO.setScaleCode(NumConstants.ONE);
            }
        }
        return proBasicInfoVO;
    }

    /**
     * 评价-参与人员
     */
    @Override
    public List<ProParticipantTableVO> getInParticipantInfo(Long projectId) {
        List<ProParticipantTableBO> list = inspectionMapper.getInParticipantInfo(projectId);
        List<ProParticipantTableVO> voList = new ArrayList<>();
        list.forEach(participantTableBO -> {
            ProParticipantTableVO proParticipantTableVO = new ProParticipantTableVO();
            proParticipantTableVO.setProjectId(projectId);
            proParticipantTableVO.setName(participantTableBO.getName());
            proParticipantTableVO.setUserId(participantTableBO.getUserId());
            // 人员类型
            Integer types = participantTableBO.getTypes();
            switch (types) {
                case 110:
                    proParticipantTableVO.setItemCode(NumConstants.ONE);
                    proParticipantTableVO.setItemName(ChineseCharacterConstants.FIELD_INVESTIGATION);
                    voList.add(proParticipantTableVO);
                    break;
                case 120:
                    proParticipantTableVO.setItemCode(NumConstants.TWO);
                    proParticipantTableVO.setItemName(ChineseCharacterConstants.ON_SITE_SAMPLING_TESTING);
                    voList.add(proParticipantTableVO);
                    break;
                case 130:
                    proParticipantTableVO.setItemCode(NumConstants.THREE);
                    proParticipantTableVO.setItemName(ChineseCharacterConstants.LABORATORY_TESTING);
                    voList.add(proParticipantTableVO);
                    break;
                case 140:
                    proParticipantTableVO.setItemCode(NumConstants.FOUR);
                    proParticipantTableVO.setItemName(ChineseCharacterConstants.APPRAISE);
                    voList.add(proParticipantTableVO);
                    break;
                default:
                    break;
            }
        });
        return voList;
    }


    /**
     * 评价-结果
     */
    @Override
    public Map<Integer, Map<String, List<ResultVo>>> getResultMap(Long projectId) {
        // 1.根据13种危害因素类型，初始化map
        Map<Integer, Map<String, List<ResultVo>>> map = new HashMap<>(20);
        for (int i = 1; i <= 13; i++) {
            map.put(i, Collections.emptyMap());
        }
        long start = System.currentTimeMillis();
        // 2.采样记录表(终版)eval_plan_record_revise ：除噪声、高温、紫外辐射之外的物理因素结果。（batchGatherLis）
        List<EvalPlanRecordReviseEntity> planRecordReviseList = mongoTemplate.find(Query.query(Criteria.where("project_id").is(projectId).and("is_existed").is(1)), EvalPlanRecordReviseEntity.class);
        long mid = System.currentTimeMillis();
        log.info("评价采样记录表mongo查询耗时：" + (mid - start) + "ms");
        //   评价结果表eval_result：化学、粉尘、噪声、高温、紫外辐射，在此表查询结果。（batchGatherLis）
        List<EvalResultEntity> evalResultList = mongoTemplate.find(Query.query(Criteria.where("project_id").is(projectId).and("is_exist").is(1)), EvalResultEntity.class);
        long end = System.currentTimeMillis();
        log.info("评价结果表mongo查询耗时：" + (end - mid) + "ms ======> " + "projectId = " + projectId);
        if (CollUtil.isEmpty(planRecordReviseList) || CollUtil.isEmpty(evalResultList)) {
            return map;
        }
        // 3.得到subMap、recordIdMap
        Set<Long> subIdSet = new HashSet<>();
        planRecordReviseList.forEach(planRecordRevise -> subIdSet.add(Long.valueOf(planRecordRevise.getSubstance().getFinalId())));
        Map<Long, List<SubstanceContrastResult>> subMap = resultMapper.selectList(new LambdaQueryWrapper<SubstanceContrastResult>().in(SubstanceContrastResult::getSubId, subIdSet)).stream().collect(Collectors.groupingBy(SubstanceContrastResult::getSubId));
        Map<String, List<EvalPlanRecordReviseEntity>> recordIdMap = planRecordReviseList.stream().collect(Collectors.groupingBy(EvalPlanRecordReviseEntity::getId));
        // 4.结果数据处理
        List<ResultVo> resultVoList = new ArrayList<>();
        for (EvalResultEntity resultEntity : evalResultList) {
            Integer sType = resultEntity.getSType();
            String recordId = resultEntity.getRecordId();

            ResultVo resultVo = new ResultVo();
            // eval_result：日接触、周工作、危害因素类型、record_id
            resultVo.setFactorType(sType);
            resultVo.setProjectId(projectId);
            resultVo.setWeekWorkDay(resultEntity.getTouchDays());
            resultVo.setDailyContactTime(resultEntity.getTouchTime());
            // eval_plan_record_revise ：物质信息、车间、岗位、点位、isFixed（1流动采样，2个体采样）
            EvalPlanRecordReviseEntity recordReviseEntity = recordIdMap.get(recordId).get(0);
            resultVo.setWorkArea(recordReviseEntity.getWorkArea());
            resultVo.setPointName(recordReviseEntity.getPointName());
            resultVo.setDetectionArea(recordReviseEntity.getIsFixed() == 1 ? recordReviseEntity.getWorkArea() + "/" + recordReviseEntity.getProfession() : recordReviseEntity.getProfession());

            ResultSubstanceDto substance = recordReviseEntity.getSubstance();
            resultVo.setSubName(substance.getFinalName());
            resultVo.setSubId(Long.valueOf(substance.getFinalId()));

            List<SubstanceContrastResult> substanceContrastResults = subMap.get(Long.valueOf(substance.getFinalId()));
            if (CollUtil.isEmpty(substanceContrastResults)) {
                continue; // 抛异常页面excel导入功能无法正常使用，特跳出本次循环！
            //    throw new RRException("物质【subId=" + substance.getFinalId() + "，subName=" + substance.getFinalName() + "】未匹配到省检测项目编码，请联系管理员！");
            }
            substanceContrastResults.sort(Comparator.comparing(SubstanceContrastResult::getResultItemCode));
            resultVo.setItemName(substanceContrastResults.get(0).getItemName());
            resultVo.setCheckItemCode(substanceContrastResults.get(0).getCheckItemCode());

            StringBuilder unit = new StringBuilder();
            StringBuilder code = new StringBuilder();
            if (sType == 2) {
                unit.append("1001,1001,1001,1001").append(",");
                code.append("127,").append("104,").append("128,").append("105").append(",");
            } else {
                substanceContrastResults.forEach(contrastResult -> {
                    unit.append(contrastResult.getUnit()).append(",");
                    code.append(contrastResult.getResultItemCode()).append(",");
                });
            }
            resultVo.setUnit(unit.substring(0, unit.length() - 1));
            resultVo.setCode(code.substring(0, code.length() - 1));

            // 分源获取：结果、检测日期、单项结论
            if (CHEMICAL_WAIT_TYPES.contains(sType)) {
                fromEvalResultGetData(resultEntity, sType, resultVo, code, resultVoList);
            } else {
                // Todo: 6.手传振动x  7工频电场x  8.高频电磁场x  9:超高频辐射x  10:微波辐射x  11:风速x   12:照度x  13:激光辐射x （检评和评价暂未完成此部分结果计算功能！）
            }
        }

        resultVoList.stream().collect(Collectors.groupingBy(ResultVo::getFactorType)).forEach((type, valueList) -> {
            if (CollUtil.isNotEmpty(valueList)) {
                Map<String, List<ResultVo>> codeMap = valueList.stream().sorted((Comparator.comparingInt(o -> o.getCode().split(",").length))).collect(Collectors.groupingBy(ResultVo::getCode));
                map.put(type, codeMap);
            }
        });
        log.info("评价结果数据处理耗时：" + (System.currentTimeMillis() - end) + "ms");
        return map;
    }

    /**
     * sType：1化学、2粉尘、3噪声、4高温、5紫外辐射，从eval_result表获取结果、检测日期、单项结论。
     * @param resultEntity
     * @param sType
     * @param resultVo
     * @param code
     */
    private void fromEvalResultGetData(EvalResultEntity resultEntity, Integer sType, ResultVo resultVo, StringBuilder code, List<ResultVo> resultVoList) {
        resultEntity.getBatchGatherLis().forEach(inBatchGatherDTO -> {
            ResultVo resultVoA = new ResultVo();
            BeanUtils.copyProperties(resultVo, resultVoA);
            resultVoA.setDetectionDate(inBatchGatherDTO.getSampleDate());
            resultVoA.setConclusion(inBatchGatherDTO.getConclusion());
            StringBuilder result = new StringBuilder();
            String[] codeArray = code.toString().split(",");
            for (String codeStr : codeArray) {
                if (sType == 1) {
                    switch (codeStr) {
                        case "101":
                            result.append(inBatchGatherDTO.getCM()).append(",");
                            break;
                        case "102":
                            result.append(inBatchGatherDTO.getCTwa()).append(",");
                            break;
                        case "103":
                            result.append(inBatchGatherDTO.getCSte()).append(",");
                            break;
                        case "129":
                            result.append(inBatchGatherDTO.getCPe()).append(",");
                            break;
                        default:
                            break;
                    }
                } else if (sType == 2) {
                    switch (codeStr) {
                        case "104":
                        case "105":
                            result.append(inBatchGatherDTO.getCTwa()).append(",");
                            break;
                        case "127":
                        case "128":
                            result.append(inBatchGatherDTO.getCPe()).append(",");
                            break;
                        default:
                            break;
                    }
                } else if (sType == 3) {
                    switch (codeStr) {
                        case "115":
                            result = resultEntity.getResultType() == 1 ? result.append(inBatchGatherDTO.getMathResult()).append(",") : result.append("/").append(",");
                            break;
                        case "116":
                            result = resultEntity.getResultType() == 2 ? result.append(inBatchGatherDTO.getMathResult()).append(",") : result.append("/").append(",");
                            break;
                        case "117":
                            result = resultEntity.getResultType() == 0 ? result.append(inBatchGatherDTO.getMathResult()).append(",") : result.append("/").append(",");
                            break;
                        default:
                            break;
                    }
                } else if (sType == 4) {
                    switch (codeStr) {
                        case "114":
                            result.append(inBatchGatherDTO.getMathResult()).append(",");
                            break;
                        case "130":
                            result.append(resultEntity.getContactRate()).append(",");
                            break;
                        case "131":
                            result.append(resultEntity.getLaborIntensity()).append(",");
                            break;
                        case "132":
                            result.append(resultEntity.getLimitV()).append(",");
                            break;
                        default:
                            break;
                    }
                } else {  // 5紫外辐射：只测109辐照度，照射量108不测默认值为/
                    switch (codeStr) {
                        case "108":
                            result.append("/").append(",");
                            break;
                        case "109":
                            result.append(inBatchGatherDTO.getMathResult()).append(",");
                            break;
                        default:
                            break;
                    }
                }
            }
            resultVoA.setResult(result.toString().substring(0, result.length() - 1));
            resultVoList.add(resultVoA);
        });
    }

}
