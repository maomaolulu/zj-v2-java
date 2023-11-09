package may.yuntian.external.datainterface.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.common.exception.RRException;
import may.yuntian.external.datainterface.constant.ChineseCharacterConstants;
import may.yuntian.external.datainterface.constant.NumConstants;
import may.yuntian.external.datainterface.dao.EvaluationMapper;
import may.yuntian.external.datainterface.dao.SubstanceContrastResultMapper;
import may.yuntian.external.datainterface.entity.SubstanceContrastResult;
import may.yuntian.external.datainterface.pojo.bo.ProParticipantTableBO;
import may.yuntian.external.datainterface.pojo.vo.*;
import may.yuntian.external.datainterface.service.EvaluationService;
import may.yuntian.jianping.dto.BatchSampleDto;
import may.yuntian.jianping.dto.ChemDto;
import may.yuntian.jianping.dto.ConclusionDto;
import may.yuntian.jianping.mongoentity.ResultEntity;
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
 * 检评 Service实现层
 *
 * @author cwt
 * @Create 2023-4-12 17:19:07
 */
@Service
@Slf4j
public class EvaluationServiceImpl implements EvaluationService {

    @Resource
    private EvaluationMapper evaluationMapper;
    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private SubstanceContrastResultMapper resultMapper;

    /**
     * 检评-基本信息
     */
    @Override
    public ProBasicInfoVO getEvGeneralInfo(Long projectId) {
        ProBasicInfoVO proBasicInfoVO = evaluationMapper.getEvGeneralInfo(projectId);
        String industryCategoryCode = proBasicInfoVO.getIndustryCategoryCode();
        if (industryCategoryCode == null || proBasicInfoVO.getIndustryCategory() == null) {
            industryCategoryCode = ChineseCharacterConstants.EMPTY;
            proBasicInfoVO.setIndustryCategory(ChineseCharacterConstants.EMPTY);
        }
        String substring;
        int length = industryCategoryCode.length();
        if (length <= NumConstants.NUMBER_FIVE) {
            substring = StringUtils.substring(industryCategoryCode, 1);
        } else {
            substring = StringUtils.substring(industryCategoryCode, 1, 5);
        }
        proBasicInfoVO.setIndustryCategoryCode(substring);
        // 报告签发人
        String company = ShiroUtils.getUserEntity().getSubjection();
        if ("杭州安联".equals(company)) {
            proBasicInfoVO.setIssuer("王勇");
        } else if ("宁波安联".equals(company)) {
            proBasicInfoVO.setIssuer("钟狄阳");
        } else if ("嘉兴安联".equals(company)) {
            proBasicInfoVO.setIssuer("张袁金");
        }
        String checkType = proBasicInfoVO.getCheckType();
        // 检测类型
        if (ChineseCharacterConstants.EVALUATION.equals(checkType)) {
            proBasicInfoVO.setCheckType(NumConstants.TEN);
        } else {
            throw new RRException("该项目类型暂不允许申报!!!");
        }
        // 委托机构统一社会信用代码
        if (proBasicInfoVO.getEmpName().equals(proBasicInfoVO.getEntrustOrgName())) {
            proBasicInfoVO.setEntrustCreditCode(proBasicInfoVO.getEmpCreditCode());
        } else {
            proBasicInfoVO.setEntrustCreditCode(ChineseCharacterConstants.EMPTY);
        }
        // 经济类型
        proBasicInfoVO.setEconomicType(ChineseCharacterConstants.EMPTY);
        // 经济类型编码
        proBasicInfoVO.setEconomicTypeCode(ChineseCharacterConstants.EMPTY);
        // 企业规模编码
        proBasicInfoVO.setScaleCode(ChineseCharacterConstants.EMPTY);
        // 技术服务地区编码
        proBasicInfoVO.setServiceAreaCode(ChineseCharacterConstants.EMPTY);
        // 技术服务领域编码
        proBasicInfoVO.setFieldCode(ChineseCharacterConstants.EMPTY);
        //
        String employeesTotalNum = proBasicInfoVO.getEmployeesTotalNum();
        if (NumConstants.ZERO.equals(employeesTotalNum) || CharSequenceUtil.isBlank(employeesTotalNum)) {
            proBasicInfoVO.setEmployeesTotalNum(ChineseCharacterConstants.EMPTY);
            proBasicInfoVO.setScaleCode(NumConstants.FIVE);
        } else {
            int integer = Integer.parseInt(employeesTotalNum);
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
        proBasicInfoVO.setSysUserVo(new SysUserVo().setUsername(proBasicInfoVO.getProjectDirectorName()));
        return proBasicInfoVO;
    }

    /**
     * 检评-参与人员
     */
    @Override
    public List<ProParticipantTableVO> getEvParticipantInfo(Long projectId) {
        List<ProParticipantTableBO> list = evaluationMapper.getParticipantInfo(projectId);
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
     * 检评-结果
     */
    @Override
    public Map<Integer, Map<String, List<ResultVo>>> getResultMap(Long projectId) {
        Map<Integer, Map<String, List<ResultVo>>> map = new HashMap<>();
        // 根据13种危害因素类型，默认初始化map
        for (int i = 1; i <= 13; i++) {
            map.put(i, Collections.emptyMap());
        }
        List<ResultEntity> resultEntityList = null;
        try {
            resultEntityList = mongoTemplate.find(Query.query(Criteria.where("project_id").is(projectId).and("is_exist").is(1)), ResultEntity.class);
        } catch (Exception e) {
            log.error("检评-结果查询，异常信息：" + e.getMessage());
            return map;
        }
        if (CollUtil.isEmpty(resultEntityList)) {
            return map;
        }
        // 检测物质subMap
        Set<Long> subIdSet = new HashSet<>();
        resultEntityList.forEach(result -> subIdSet.add(Long.valueOf(result.getSubstance().getFinalId())) );
        Map<Long, List<SubstanceContrastResult>> subMap = resultMapper.selectList(new LambdaQueryWrapper<SubstanceContrastResult>().in(SubstanceContrastResult::getSubId, subIdSet)).stream().collect(Collectors.groupingBy(SubstanceContrastResult::getSubId));
        // 结果数据封装
        List<ResultVo> resultVoList = new ArrayList<>();
        for (ResultEntity resultEntity : resultEntityList) {
            Integer sType = resultEntity.getSubstance().getSType();
            ConclusionDto physicalResultDto = resultEntity.getConclusion();
            Long subId = Long.valueOf(resultEntity.getSubstance().getFinalId());
            List<SubstanceContrastResult> contrastList = subMap.get(subId);
            if (CollUtil.isEmpty(contrastList)) {
                continue;
            }
            contrastList.sort(Comparator.comparing(SubstanceContrastResult::getResultItemCode));
            // 结果推送基本信息（结果、检测日期：需另行处理）
            ResultVo resultVo = new ResultVo();
            resultVo.setProjectId(resultEntity.getProjectId());
            resultVo.setSubId(subId);
            resultVo.setSubName(resultEntity.getSubstance().getFinalName());
            resultVo.setCheckItemCode(contrastList.get(0).getCheckItemCode());
            resultVo.setItemName(contrastList.get(0).getItemName());
            resultVo.setWorkArea(resultEntity.getWorkShopDTO().getWorkshop());
            resultVo.setDetectionArea(resultEntity.getIsFixed() == 1 ? resultEntity.getWorkShopDTO().getPost().concat("/").concat(resultEntity.getPfn()) : resultEntity.getPfn());
            resultVo.setDailyContactTime(resultEntity.getDailyContactTime());
            resultVo.setWeekWorkDay(resultEntity.getWeekWorkDay());
            resultVo.setPointName(resultEntity.getPoint());
            resultVo.setFactorType(sType);
            StringBuilder unit = new StringBuilder();
            StringBuilder code = new StringBuilder();
            if (sType == 2) {
                unit.append("1001,1001,1001,1001").append(",");
                code.append("127,").append("104,").append("128,").append("105").append(",");
            } else {
                contrastList.forEach(contrast -> {
                    unit.append(contrast.getUnit()).append(",");
                    code.append(contrast.getResultItemCode()).append(",");
                });
            }
            resultVo.setUnit(unit.substring(0, unit.length() - 1));
            resultVo.setCode(code.substring(0, code.length() - 1));
            // 每个批次有对应的检测日期与结果
            resultEntity.getBatchSampleLis().forEach(batchSampleDto -> {
                ResultVo resultVoA = new ResultVo();
                BeanUtils.copyProperties(resultVo, resultVoA);
                resultVoA.setDetectionDate(batchSampleDto.getGatherDate());
                ChemDto chemDto = batchSampleDto.getConclusion().get(batchSampleDto.getConclusionKey());
                // conclusion：符合；不符合；默认值""。
                resultVoA.setConclusion(chemDto.getConclusion());
                // Attention: 结果、编码、计量单位，需要一一对应！！！
                StringBuilder result = new StringBuilder();
                String[] codeArray = code.toString().split(",");

                String resultStr = getDetectResultStr(sType, physicalResultDto, batchSampleDto, chemDto, result, codeArray);
                resultVoA.setResult(CharSequenceUtil.isBlank(resultStr) ? "" : resultStr.substring(0 , result.length() - 1));

                resultVoList.add(resultVoA);
            });
        }
        resultVoList.stream().collect(Collectors.groupingBy(ResultVo::getFactorType)).forEach((type, valueList) -> {
            if (CollUtil.isNotEmpty(valueList)) {
                Map<String, List<ResultVo>> codeMap = valueList.stream().sorted((Comparator.comparingInt(o -> o.getCode().split(",").length))).collect(Collectors.groupingBy(ResultVo::getCode));
                map.put(type, codeMap);
            }
        });
        return map;
    }

    /**
     * 检测结果字符串拼接，分类处理
     * @param sType 危害因素类型
     * @param physicalResultDto 物理结果dto
     * @param batchSampleDto 样品批次dto
     * @param chemDto 化学、粉尘结果dto
     * @param result 待拼接字符串变量
     * @param codeArray 结果项编码数组
     * @return 处理后的result
     */
    private String getDetectResultStr(Integer sType, ConclusionDto physicalResultDto, BatchSampleDto batchSampleDto, ChemDto chemDto, StringBuilder result, String[] codeArray) {
        for (String s : codeArray) {
            if (sType == 1) {
                // 化学
                if ("101".equals(s)) {
                    result.append(chemDto.getCM()).append(",");
                }
                if ("102".equals(s)) {
                    result.append(chemDto.getCTwa()).append(",");
                }
                if ("103".equals(s)) {
                    result.append(chemDto.getCSte()).append(",");
                }
                if ("129".equals(s)) {
                    result.append(chemDto.getCPe()).append(",");
                }
            } else if (sType == 2) {
                // 粉尘
                if ("104".equals(s)) {
                    result.append(chemDto.getCTwa()).append(",");
                }
                if ("127".equals(s)) {
                    result.append(chemDto.getCPe()).append(",");
                }
                if ("105".equals(s)) {
                    result.append(chemDto.getCTwa()).append(",");
                }
                if ("128".equals(s)) {
                    result.append(chemDto.getCPe()).append(",");
                }
            } else if (sType == 3) {
                // 噪声
                if ("115".equals(s)) {
                    result.append("/").append(",");
                }
                if ("116".equals(s)) {
                    // 检评-噪声结果同报告固定为：40h等效声级！
                    result.append(batchSampleDto.getResult()).append(",");
                }
                if ("117".equals(s)) {
                    result.append("/").append(",");
                }
            } else if (sType == 4) {
                // 高温
                if ("114".equals(s)) {
                    result.append(batchSampleDto.getResult()).append(",");
                }
                if ("130".equals(s)) {
                    result.append(physicalResultDto.getContactRate()).append(",");
                }
                if ("131".equals(s)) {
                    result.append(physicalResultDto.getLaborIntensity()).append(",");
                }
                if ("132".equals(s)) {
                    result.append(physicalResultDto.getLimitV()).append(",");
                }
            } else if (sType == 5) {
                // 紫外辐射：只测109辐照度，照射量108不测默认值为/
                if ("109".equals(s)) {
                    result.append(batchSampleDto.getResult()).append(",");
                }
                if ("108".equals(s)) {
                    result.append("/").append(",");
                }
            } else {
                // Todo 手传振动等...... （检评和评价暂未完成此部分结果计算功能！）
                //  throw new RRException("其它危害因素类型的结果待开发中！");
            }
        }
        return result.toString();
    }

}
