package may.yuntian.external.strategy.template;

import may.yuntian.external.datainterface.constant.NumConstants;
import may.yuntian.external.datainterface.entity.EvalResultEntity;
import may.yuntian.external.datainterface.pojo.dto.InBatchGatherDTO;
import may.yuntian.external.datainterface.pojo.vo.ProResultItemVO;
import may.yuntian.external.strategy.template.base.Strategy;
import may.yuntian.jianping.dto.BatchSampleDto;
import may.yuntian.jianping.mongoentity.ResultEntity;

/**
 * @author cwt
 * @description 照度
 * @date 2023-4-23 11:50:17
 */
public class Illuminance implements Strategy {

    /**
     * @param proResultItemVO
     * @param batchSampleDto
     * @param resultEntity
     * @description 检评结果项
     */
    @Override
    public void strategyInterfaceEv(ProResultItemVO proResultItemVO, BatchSampleDto batchSampleDto, ResultEntity resultEntity) {
        Integer sType = resultEntity.getSubstance().getSType();
        if (!NumConstants.NUMBER_SEVEN.equals(sType)) {
            return;
        }
        // 7.工频电场  计量单位编码:unit
        proResultItemVO.setUnit(NumConstants.ONE_THOUSAND_TEN);
        // 结果
        proResultItemVO.setResult(batchSampleDto.getResult());
        // 结果项编码:code
        proResultItemVO.setCode(NumConstants.ONE_HUNDRED_AND_SIX);
        // 接触限值:limit
        proResultItemVO.setLimit(resultEntity.getConclusion().getLimitV());
        //  特殊标识:specialIdentityFields 先都给0
        proResultItemVO.setSpecialIdentityFields(NumConstants.NUMBER_ZERO);
    }

    /**
     * @param proResultItemVO
     * @param eREntity
     * @param inBatchGatherDTO
     * @description 评价结果项
     */
    @Override
    public void strategyInterfaceIn(ProResultItemVO proResultItemVO, EvalResultEntity eREntity, InBatchGatherDTO inBatchGatherDTO) {
        Integer sType = eREntity.getSType();
        if (!NumConstants.NUMBER_SEVEN.equals(sType)) {
            return;
        }
        // 7.工频电场  计量单位编码:unit
        proResultItemVO.setUnit(NumConstants.ONE_THOUSAND_TEN);
        // 结果
        proResultItemVO.setResult(inBatchGatherDTO.getMathResult());
        // 结果项编码:code
        proResultItemVO.setCode(NumConstants.ONE_HUNDRED_AND_SIX);
        // 接触限值:limit
        proResultItemVO.setLimit(eREntity.getLimitV());
        //  特殊标识:specialIdentityFields 先都给0
        proResultItemVO.setSpecialIdentityFields(NumConstants.NUMBER_ZERO);

    }
}
