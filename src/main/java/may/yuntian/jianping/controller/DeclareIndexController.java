package may.yuntian.jianping.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.jianping.mongoentity.DeclareIndexEntity;
import may.yuntian.jianping.mongoservice.DeclareIndexService;
import may.yuntian.jianping.vo.StandardsVo;
import may.yuntian.sys.utils.Result;
import may.yuntian.untils.FreemarkerDocxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URL;


/**
 *申报索引
 * WEB请求处理层
 *
 * @author LiXin
 * @data 2022-11-15
 */
@Slf4j
@RestController
@Api(tags="申报索引")
@RequestMapping("/declareIndex")
public class DeclareIndexController {
    @Autowired
    private DeclareIndexService declareIndexService;

    /**
     * 信息
     */
    @GetMapping("/info/{projectId}")
    @ApiOperation("申报索引")
//    @RequiresPermissions("anlian:declareIndex:evInfo")
    public Result info(@PathVariable("projectId") Long projectId){
    	DeclareIndexEntity declareIndexEntity = declareIndexService.getOneByProjectId(projectId);

        return Result.data(declareIndexEntity);
    }

    /**
     * 保存
     */
    @PostMapping("/initialization")
    @ApiOperation("初始化申报索引")
//    @RequiresPermissions("anlian:declareIndex:save")
    public Result save(@RequestBody StandardsVo standardsVo){
        declareIndexService.initialization(standardsVo.getProjectId());

        return Result.ok();
    }


    @PostMapping("/exportWord")
    @ApiOperation("导出申报索引word")
    public void exportWord(HttpServletResponse response,@RequestBody DeclareIndexEntity declareIndexEntity) {
//        System.out.println("4444444444");
//        if (declareIndexEntity==null){
//            return Result.error("未生成数据无法下载");
//        }else if (StringUtils.checkValNull(declareIndexEntity.getInformation())){
//            return Result.error("未生成数据无法下载");
//        }else {
//
//        }
        declareIndexService.exportWord(response,declareIndexEntity);

    }



}
