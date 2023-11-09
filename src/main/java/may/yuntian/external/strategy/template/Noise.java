package may.yuntian.external.strategy.template;

import may.yuntian.common.exception.RRException;
import may.yuntian.external.datainterface.constant.ChineseCharacterConstants;
import may.yuntian.external.datainterface.constant.NumConstants;
import may.yuntian.external.datainterface.entity.EvalResultEntity;
import may.yuntian.external.datainterface.pojo.dto.InBatchGatherDTO;
import may.yuntian.external.datainterface.pojo.vo.ProResultItemVO;
import may.yuntian.external.strategy.template.base.Strategy;
import may.yuntian.jianping.dto.BatchSampleDto;
import may.yuntian.jianping.mongoentity.ResultEntity;

/**
 * 噪声
 *
 * @author cwt
 * @date 2023-4-19 17:42:44
 */
public class Noise implements Strategy {

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
        if (!NumConstants.NUMBER_THREE.equals(sType)) {
            return;
        }
        proResultItemVO.setUnit(NumConstants.ONE_THOUSAND_AND_SIXTEEN);
        // 结果
        proResultItemVO.setResult(batchSampleDto.getResult());
        // 结果项编码:code
        Integer resultType = resultEntity.getResultType();
        getInfo(proResultItemVO, resultType);
        // 接触限值:limit
        proResultItemVO.setLimit(resultEntity.getConclusion().getLimitV());

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
        // 3:噪声 计量单位编码:unit
        Integer sType = eREntity.getSType();
        if (!NumConstants.NUMBER_THREE.equals(sType)) {
            return;
        }
        proResultItemVO.setUnit(NumConstants.ONE_THOUSAND_AND_SIXTEEN);
        // 结果
        proResultItemVO.setResult(inBatchGatherDTO.getMathResult());
        // 结果项编码:code
        Integer resultType = eREntity.getResultType();
        // 结果项编码+特殊标识抽离
        getInfo(proResultItemVO, resultType);
        // 接触限值:limit
        proResultItemVO.setLimit(eREntity.getLimitV());
    }

    /**
     * 结果项编码+特殊标识抽离
     *
     * @param proResultItemVO
     * @param resultType
     */
    public void getInfo(ProResultItemVO proResultItemVO, Integer resultType) {
        if (resultType == null) {
            // Todo：resultType为空时的默认值处理
            resultType = 0;
        //    throw new RRException("缺少有效结果类型");
        }
        switch (resultType) {
            case 0:
                // 声压级峰值
                proResultItemVO.setCode(NumConstants.ONE_HUNDRED_AND_SEVENTEEN);
                //  特殊标识:specialIdentityFields 先都给0
                proResultItemVO.setSpecialIdentityFields(NumConstants.NUMBER_THREE);
                break;
            case 1:
                proResultItemVO.setCode(NumConstants.ONE_HUNDRED_AND_FIFTEEN);
                //  特殊标识:specialIdentityFields 先都给0
                proResultItemVO.setSpecialIdentityFields(NumConstants.NUMBER_ONE);
                break;
            case 2:
                //  特殊标识:specialIdentityFields 先都给0
                proResultItemVO.setCode(NumConstants.ONE_HUNDRED_AND_SIXTEEN);
                proResultItemVO.setSpecialIdentityFields(NumConstants.NUMBER_TWO);
                break;
            default:
                break;
        }
    }
}
