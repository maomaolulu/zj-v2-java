package may.yuntian.external.strategy.template.base;

import may.yuntian.external.datainterface.entity.EvalResultEntity;
import may.yuntian.external.datainterface.pojo.dto.InBatchGatherDTO;
import may.yuntian.external.datainterface.pojo.vo.ProResultItemVO;
import may.yuntian.jianping.dto.BatchSampleDto;
import may.yuntian.jianping.mongoentity.ResultEntity;

/**
 * 抽象策略角色
 *
 * @author cwt
 * @date 2023-4-19 17:42:44
 */
public interface Strategy {

    /**
     * 检评结果项抽象策略
     * @param proResultItemVO
     * @param batchSampleDto
     * @param resultEntity
     */
    void strategyInterfaceEv(ProResultItemVO proResultItemVO, BatchSampleDto batchSampleDto, ResultEntity resultEntity);

    /**
     * 评价结果项抽象策略
     * @param proResultItemVO
     * @param eREntity
     * @param inBatchGatherDTO
     */
    void strategyInterfaceIn(ProResultItemVO proResultItemVO, EvalResultEntity eREntity, InBatchGatherDTO inBatchGatherDTO);

}
