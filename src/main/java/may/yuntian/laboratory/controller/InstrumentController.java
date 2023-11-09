package may.yuntian.laboratory.controller;

import io.swagger.annotations.ApiOperation;
import may.yuntian.laboratory.entity.InstrumentEntity;
import may.yuntian.laboratory.service.InstrumentService;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/instrument")
public class InstrumentController {
    @Autowired
    private InstrumentService instrumentService;

    @GetMapping("/link")
    @ApiOperation("实验室仪器列表")
    public Result link(String name){
        List<InstrumentEntity> instrumentEntities = instrumentService.selectAllAndSplicing(name);
        return Result.ok().put("instrumentEntities",instrumentEntities);
    }
}
