package may.yuntian.external.datainterface.controller;

import may.yuntian.external.datainterface.pojo.vo.ProBasicInfoVO;
import may.yuntian.external.datainterface.pojo.vo.ProParticipantTableVO;
import may.yuntian.external.datainterface.service.EvaluationService;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 检评 controller层
 *
 * @author cwt
 * @Create 2023-4-12 17:17:04
 */
@RestController
@RequestMapping("/data/evaluations")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    /**
     * 检评-基本信息
     */
    @GetMapping("/getEvGeneralInfo")
    public Result getEvGeneralInfo(@RequestParam Long projectId) {
        ProBasicInfoVO proBasicInfoVO = evaluationService.getEvGeneralInfo(projectId);
        return Result.data(proBasicInfoVO);
    }

    /**
     * 检评-参与人员
     */
    @GetMapping("/getEvParticipantInfo")
    public Result getEvParticipantInfo(@RequestParam Long projectId) {
        List<ProParticipantTableVO> list = evaluationService.getEvParticipantInfo(projectId);
        return Result.data(list);
    }

    /**
     * 检评-结果
     */
    @GetMapping("/result")
    public Result getResultMap(Long projectId) {
        return Result.data(evaluationService.getResultMap(projectId));
    }

}
