package may.yuntian.external.datainterface.controller;

import may.yuntian.external.datainterface.pojo.vo.ProBasicInfoVO;
import may.yuntian.external.datainterface.pojo.vo.ProParticipantTableVO;
import may.yuntian.external.datainterface.service.InspectionService;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 评价 controller层
 *
 * @author cwt
 * @Create 2023-4-12 17:15:26
 */
@RestController
@RequestMapping("/data/inspections")
public class InspectionController {

    @Autowired
    private InspectionService inspectionService;

    /**
     * 评价-基本信息
     */
    @GetMapping("/getInGeneralInfo")
    public Result getInGeneralInfo(@RequestParam Long projectId) {
        ProBasicInfoVO proBasicInfoVO = inspectionService.getInGeneralInfo(projectId);
        return Result.data(proBasicInfoVO);
    }


    /**
     * 评价-参与人员
     */
    @GetMapping("/getInParticipantInfo")
    public Result getInParticipantInfo(@RequestParam Long projectId) {
        List<ProParticipantTableVO> list = inspectionService.getInParticipantInfo(projectId);
        return Result.data(list);
    }

    // 2023年6月26日18:09:15 lyq 结果代码重构！

    /**
     * 评价-结果
     */
    @GetMapping("/result")
    public Result getResultMap(Long projectId) {
        return Result.data(inspectionService.getResultMap(projectId));
    }

}
