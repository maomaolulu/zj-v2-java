package may.yuntian.external.datainterface.script.controller;

import may.yuntian.external.datainterface.script.service.ScriptService;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cwt
 * @Create 2023-4-13 16:41:19
 */
@RestController
@RequestMapping("/scripts")
public class ScriptController {

    @Autowired
    private ScriptService scriptService;


    /**
     * pro_detection_substance  表数据处理
     *
     * @return
     */
    @GetMapping("/test")
    public Result script() {
        scriptService.tableDataProcessing();
        return Result.data("脚本操作");
    }


    /**
     * al_substance_copy_to_pro  表数据处理
     *
     * @return
     */
    @GetMapping("/test1")
    public Result script1() {
        scriptService.tableDataProcessing1();
        return Result.data("脚本操作");
    }

    /**
     * @description zj_workspace脏数据处理
     *
     * @return
     */
    @PostMapping("/mongoTest")
    public Result mongoTest() {
        scriptService.mongoTest();
        return Result.data("脚本操作");
    }

    /**
     * @description zj_post_pfn脏数据处理
     *
     * @return
     */
    @PostMapping("/mongoTest1")
    public Result mongoTest1() {
        scriptService.mongoTest1();
        return Result.data("脚本操作");
    }
}
