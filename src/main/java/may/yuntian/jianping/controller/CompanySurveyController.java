package may.yuntian.jianping.controller;

import io.swagger.annotations.ApiOperation;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.service.ProjectService;
import may.yuntian.jianping.entity.CompanySurveyEntity;
import may.yuntian.jianping.service.CompanySurveyService;
import may.yuntian.jianping.vo.CompanySurveyVo;
import may.yuntian.sys.utils.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author mi
 */
@RestController
@RequestMapping("/companySurvey")
public class CompanySurveyController {

    @Autowired
    private CompanySurveyService companySurveyService;
    @Autowired
    private ProjectService projectService;

    /**
     * 根据项目id获取企业详细信息
     *
     * @param projectId 项目id
     * @return
     */
    @GetMapping("/getById/{projectId}")
    @ApiOperation("根据项目id获取当前企业详细信息")
    public Result getOne(@PathVariable("projectId") Long projectId) {

        CompanySurveyEntity companyEntity = companySurveyService.getOne(projectId);

        return Result.ok().put("data", companyEntity);
    }

    /**
     * 根据id修改企业信息
     *
     * @param companyEntity 企业实体类
     * @return
     */
    @PostMapping("/update")
    @ApiOperation("修改企业信息")
    public Result updateCompany(@RequestBody CompanySurveyEntity companyEntity) {
        boolean b = companySurveyService.updateById(companyEntity);
        if (b) {
            return Result.ok();
        } else {
            return Result.error("保存失败");
        }
    }

    /**
     * 检评需要公示列表 // 项目是检评的  加删除
     */
    @GetMapping("/pageListJp")
    @ApiOperation("检评项目需要公示列表（主管）")
    public Result pageListJp(@RequestParam Map<String, Object> params) {
        List<CompanySurveyVo> companySurveyVos = companySurveyService.pageListJp(params);
        return Result.resultData(companySurveyVos);
    }

    /**
     * 检评公示项目删除
     */
    @PostMapping("/deletePublicity")
    @ApiOperation("删除需要公示的项目（主管）")
    @RequiresPermissions("zj:companysurvey:deletePublicity")
    public Result deletePublicity(@RequestBody ProjectEntity projectEntity) {
        projectEntity.setHideStatus(1);
        projectEntity.setUpdatetime(new Date());
        projectService.updateById(projectEntity);
        return Result.ok("删除成功");
    }
}
