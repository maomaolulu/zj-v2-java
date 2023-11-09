package may.yuntian.external.datainterface.service;

import may.yuntian.external.datainterface.pojo.vo.ProBasicInfoVO;
import may.yuntian.external.datainterface.pojo.vo.ProParticipantTableVO;
import may.yuntian.external.datainterface.pojo.vo.ResultVo;

import java.util.List;
import java.util.Map;

/**
 * 检评 Service 层
 *
 * @author cwt
 * @Create 2023-4-12 17:18:47
 */
public interface EvaluationService {


    /**
     * 检评-基本信息
     *
     * @param projectId 项目id
     * @return 结果
     */
    ProBasicInfoVO getEvGeneralInfo(Long projectId);

    /**
     * 检评-参与人员
     *
     * @param projectId 项目id
     * @return 结果
     */
    List<ProParticipantTableVO> getEvParticipantInfo(Long projectId);

    /**
     * 检评-结果
     *
     * @param projectId 项目id
     * @return 结果
     */
    Map<Integer, Map<String, List<ResultVo>>> getResultMap(Long projectId);

}
