package may.yuntian.jianping.controller;

import io.swagger.annotations.ApiOperation;
import may.yuntian.jianping.entity.IndustryEntity;
import may.yuntian.jianping.service.IndustryService;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 项目类别
 */
@RestController
@RequestMapping("/industry")
public class IndustryController {
    @Autowired
    private IndustryService industryService;

    @GetMapping("/list")
    @ApiOperation("根据joint显示项目类别")
    public Result list(String joint){
        List<IndustryEntity> list = industryService.listJoint(joint);

        return Result.ok().put("list", list);
    }
}
