package may.yuntian.jianping.controller;

import io.swagger.annotations.Api;
import may.yuntian.jianping.mongodto.ZjPlanRecord;
import may.yuntian.jianping.mongoservice.PlanRecordService;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/7/13 16:10
 */
@RestController
@Api(tags = "采样记录")
@RequestMapping("/zj_v2_record")
public class RecordController {

    @Autowired
    private PlanRecordService planRecordService;



    /**
     * 采样记录信息显示（pc端）
     */
    @GetMapping("/page_show_record")
    public Result pageShowRecord1(Integer recoedType, Long project_id, String gatherDate) {

        return planRecordService.pageShowRecord1(recoedType, project_id, gatherDate);
    }



    /**
     *采样记录信息内层保存(pc端)  # 保存 作业内容 ppe  epe 等
     */
    @PostMapping("/page_set_inner_record")
    public Result pageSetInnerRecord1(@RequestBody List<List<ZjPlanRecord>> params){

        return planRecordService.pageSetInnerRecord1(params);
    }


    /**
     * 采样记录信息外层保存(pc端)
     */
    @PostMapping("/page_set_out_record")
    public Result pageSetOutRecord1(@RequestBody List<List<ZjPlanRecord>> params) {

        Result res = planRecordService.equipTimeCheck1(params);

        if (res.get("code").equals(200)) {

            return planRecordService.pageSetOutRecord1(params);
        } else {
            return res;
        }

    }




    /**
     * 获取项目采样记录类型
     */
    @GetMapping("/getRecordType")
    public Result getRecordType(Long projectId){

        return planRecordService.getRecordType(projectId);
    }

}
