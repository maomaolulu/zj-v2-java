package may.yuntian.jianping.controller;

import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.jianping.entity.ConclusionPyEntity;
import may.yuntian.jianping.service.TestReportPyService;
import may.yuntian.jianping.vo.ConclusionVo;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检测报告模块
 *
 * @author hjy
 * @description python代码迁移，保持原python风格
 * @date 2023/7/12 13:49
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/test_result")
public class TestReportController {

    @Autowired
    private TestReportPyService pyService;


    //********************************** 结果计算 *************************************************

    /**
     * 结果计算列表
     */
    @GetMapping("/result_lis")
    public Result resultList(Long project_id, Integer s_type) {
        if (StringUtils.isNull(project_id) || StringUtils.isNull(s_type)) {
            return Result.error(StringUtils.format("参数不能为空：project_id：{}   s_type：{}", project_id, s_type));
        }
        try {
            return Result.data(pyService.getResultList(project_id, s_type));
        } catch (Exception e) {
            return Result.error("结果计算获取失败：" + e.getMessage());
        }
    }
    //********************************** 结论展示 (报告第7章的结论) *************************************************

    /**
     * 结论展示列表
     */
    @GetMapping("/conclusion_lis")
    public Result conclusionList(Long project_id) {
        List<ConclusionPyEntity> conclusionList = pyService.getConclusionList(project_id);
        Map<String, Object> map = new HashMap<>();
        map.put("lis", conclusionList);
        return Result.data(map);
    }

    /**
     * 结论批量修改
     */
    @PostMapping("/conclusion_edit")
    public Result conclusionEdit(@RequestBody ConclusionVo conclusionVo) {
        if (StringUtils.isEmpty(conclusionVo.getConclusion_lis())) {
            return Result.error("结论信息不能为空");
        }
        try {
            pyService.updateConclusions(conclusionVo.getConclusion_lis());
            return Result.ok("结论信息修改成功");
        } catch (Exception e) {
            return Result.error("结论信息修改失败");
        }
    }

    /**
     * 结论删除
     */
    @PostMapping("/conclusion_del")
    public Result conclusionDel(@RequestBody ConclusionVo conclusionVo) {
        try {
            pyService.deleteConclusions(conclusionVo.getProject_id(), conclusionVo.getConclusion_ids());
            return Result.ok("结论信息删除成功");
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }

}
