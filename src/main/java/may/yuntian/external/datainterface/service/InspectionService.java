package may.yuntian.external.datainterface.service;

import may.yuntian.external.datainterface.pojo.vo.ProBasicInfoVO;
import may.yuntian.external.datainterface.pojo.vo.ProParticipantTableVO;
import may.yuntian.external.datainterface.pojo.vo.ResultVo;

import java.util.List;
import java.util.Map;

/**
 * 评价 Service层
 *
 * @author cwt
 * @Create 2023-4-12 17:21:16
 */
public interface InspectionService {

    /**
     * 评价-基本信息
     *
     * @param projectId 项目id
     * @return 结果
     */
    ProBasicInfoVO getInGeneralInfo(Long projectId);

    /**
     * 评价-参与人员
     *
     * @param projectId 项目id
     * @return 结果
     */
    List<ProParticipantTableVO> getInParticipantInfo(Long projectId);

    /**
     * 评价-结果
     *
     * @param projectId 项目id
     * @return 结果
     */
    Map<Integer, Map<String, List<ResultVo>>> getResultMap(Long projectId);

}
