package may.yuntian.external.strategy.template;

import may.yuntian.external.datainterface.constant.NumConstants;
import may.yuntian.external.datainterface.entity.EvalResultEntity;
import may.yuntian.external.datainterface.pojo.dto.InBatchGatherDTO;
import may.yuntian.external.datainterface.pojo.vo.ProResultItemVO;
import may.yuntian.external.strategy.template.base.Strategy;
import may.yuntian.jianping.dto.BatchSampleDto;
import may.yuntian.jianping.mongoentity.ResultEntity;

/**
 * 高频电磁场
 *
 * @author cwt
 * @date 2023-4-19 17:42:44
 */
public class HighFrequencyElectromagneticField implements Strategy {

    /**
     * 检评结果项
     *
     * @param proResultItemVO
     * @param batchSampleDto
     * @param resultEntity
     */
    @Override
    public void strategyInterfaceEv(ProResultItemVO proResultItemVO, BatchSampleDto batchSampleDto, ResultEntity resultEntity) {
        Integer sType = resultEntity.getSubstance().getSType();
        if (!NumConstants.NUMBER_EIGHT.equals(sType)) {
            return;
        }
        // 8.高频电磁场  计量单位编码:unit
        proResultItemVO.setUnit(NumConstants.ONE_THOUSAND_NINE + ";" + NumConstants.ONE_THOUSAND_ELEVEN);
        // 结果
        proResultItemVO.setResult(batchSampleDto.getResult());
        //  todo proResultItemVO.setResult2();
        // 结果项编码:code
        proResultItemVO.setCode(NumConstants.ONE_HUNDRED_AND_SIX + ";" + NumConstants.ONE_HUNDRED_AND_SEVEN);
        // 接触限值:limit
        proResultItemVO.setLimit(resultEntity.getConclusion().getLimitV());
        //  特殊标识:specialIdentityFields 先都给0
        proResultItemVO.setSpecialIdentityFields(NumConstants.NUMBER_ZERO);
    }

    /**
     * 评价结果项
     *
     * @param proResultItemVO
     * @param eREntity
     * @param inBatchGatherDTO
     */
    @Override
    public void strategyInterfaceIn(ProResultItemVO proResultItemVO, EvalResultEntity eREntity, InBatchGatherDTO inBatchGatherDTO) {
        // 8.高频电磁场  计量单位编码:unit
        Integer sType = eREntity.getSType();
        if (!NumConstants.NUMBER_EIGHT.equals(sType)) {
            return;
        }
        proResultItemVO.setUnit(NumConstants.ONE_THOUSAND_NINE + ";" + NumConstants.ONE_THOUSAND_ELEVEN);
        // 结果
        proResultItemVO.setResult(inBatchGatherDTO.getMathResult());
        //  todo proResultItemVO.setResult2();
        // 结果项编码:code
        proResultItemVO.setCode(NumConstants.ONE_HUNDRED_AND_SIX + ";" + NumConstants.ONE_HUNDRED_AND_SEVEN);
        // 接触限值:limit
        proResultItemVO.setLimit(eREntity.getLimitV());
        //  特殊标识:specialIdentityFields 先都给0
        proResultItemVO.setSpecialIdentityFields(NumConstants.NUMBER_ZERO);
    }

}
