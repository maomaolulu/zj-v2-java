package may.yuntian.anlian.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.anlian.constant.IntellectConstants;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.service.AgreementService;
import may.yuntian.anlian.service.ProjectService;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.anlian.vo.ExportSchedulingVo;
import may.yuntian.anlian.vo.ProjectDateUserVo;
import may.yuntian.anlian.vo.ProjectTaskVo;
import may.yuntian.anlian.vo.ProjectTimeZoneVo;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.exception.RRException;
import may.yuntian.modules.sys_v2.annotation.AuthCode;
import may.yuntian.modules.sys_v2.entity.vo.AuthCodeVo;
import may.yuntian.sys.utils.Result;
import may.yuntian.sys.utils.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


/**
 * 项目表(包含了原任务表的字段)管理
 * WEB请求处理层
 *
 * @author LiXin
 * @email ''
 * @date 2022-01-10 14:43:43
 */
@RestController
@Api(tags="项目表")
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private AgreementService agreementService;

    /**
     * ipad端 我的任务列表
     */
    @GetMapping("/myTask")
    @ApiOperation("根据条件查询我的任务列表")
//    @RequiresPermissions("anlian:project:list")
    public Result list(@RequestParam Map<String, Object> params){
        List<ProjectTaskVo> list = projectService.getMyTaskList(params);
        return Result.resultData(list);
    }

    @GetMapping("/testLink")
    @ApiOperation("运行测试接口,不需token")
//    @RequiresPermissions("anlian:project:list")
    public Result testLink(HttpServletRequest httpRequest){
        String host = httpRequest.getHeader("Host");

        return Result.ok().put("msg","测试通过,访问成功! Host是http://"+host);
    }


    /**
     * web端 项目排单列表页
     * @param params
     * @return
     */
    @GetMapping("/getListProjectDateUser")
    @ApiOperation("项目排单列表")
    @AuthCode(url = "taskScheduling",system = "zj")
    public Result getListProDateUser(@RequestParam Map<String,Object> params, AuthCodeVo authCodeVo){
        String subjection = ShiroUtils.getUserEntity().getSubjection();
        if (StringUtils.isBlank(subjection)){
            subjection = "无隶属公司";
        }
        if (IntellectConstants.GROUP_PERMISSIONS.equals(authCodeVo.getAuthCode())){
            params.put("subjection","");
        }else {
            params.put("subjection",subjection);
        }

        List<ProjectDateUserVo> listProjectDateUser = projectService.getListProjectDateUser(params);

        return Result.resultData(listProjectDateUser);
    }

    /**
     * web端 导出项目排单列表信息
     * @param params
     * @return
     */
    @GetMapping("/exportScheduling")
    @ApiOperation("导出项目排单列表信息")
    @AuthCode(url = "taskScheduling",system = "zj")
    public void exportScheduling(@RequestParam Map<String,Object> params, AuthCodeVo authCodeVo, HttpServletResponse response) throws IOException {
        String subjection = ShiroUtils.getUserEntity().getSubjection();
        if (StringUtils.isBlank(subjection)){
            subjection = "无隶属公司";
        }
        if (IntellectConstants.GROUP_PERMISSIONS.equals(authCodeVo.getAuthCode())){
            params.put("subjection","");
        }else {
            params.put("subjection",subjection);
        }

        List<ExportSchedulingVo> list = projectService.exportScheduling(params);
        if (StringUtils.isEmpty(list)){
            new RRException("无数据无法导出");
        }
        projectService.download(list,response);


    }


    /**
     * web端 实验室数据录入列表
     * @param params
     * @return
     */
    @GetMapping("/getListLab")
    @ApiOperation("实验室数据录入列表")
    @AuthCode(url = "laboratory",system = "zj")
    public Result getListLab(@RequestParam Map<String,Object> params, AuthCodeVo authCodeVo){
        String subjection = ShiroUtils.getUserEntity().getSubjection();
        if (StringUtils.isBlank(subjection)){
            subjection = "无隶属公司";
        }
        if (IntellectConstants.GROUP_PERMISSIONS.equals(authCodeVo.getAuthCode())){
            params.put("subjection","");
        }else {
            params.put("subjection",subjection);
        }

        List<ProjectDateUserVo> listProjectDateUser = projectService.getListLab(params);

        return Result.resultData(listProjectDateUser);
    }

    /**
     * web端我的任务列表
     * @param params
     * @return
     */
    @GetMapping("/getMyTaskList")
    @ApiOperation("我的任务列表")
    @AuthCode(url = "taskNavigation",system = "zj")
    public Result getMyTaskList(@RequestParam Map<String,Object> params, AuthCodeVo authCodeVo){
//        String subjection = ShiroUtils.getUserEntity().getSubjection();
        List<ProjectDateUserVo> myTaskList ;
        if (IntellectConstants.GROUP_PERMISSIONS.equals(authCodeVo.getAuthCode())){
            myTaskList = projectService.planLit(params);
        }else{
            myTaskList = projectService.getMytaskList(params);
        }

        if (StringUtils.isNotEmpty(myTaskList)){
            for (ProjectDateUserVo projectDateUserVo:myTaskList){
                Integer a = agreementService.selectListByProjectId(projectDateUserVo.getId());
                projectDateUserVo.setIsProtocol(a);
            }
        }
//
//        if (isPermitted){
//            myTaskList = projectService.planLit(params);
//        }else if (isPermitted2){
//            myTaskList = projectService.planDeptLit(params);
//        }else {
//            myTaskList = projectService.getMytaskList(params);
//        }

        return Result.resultData(myTaskList);
    }


    /**
     * web端时间节点
     * @param params
     * @return
     */
    @GetMapping("/getTimeZoneList")
    @ApiOperation("时间节点")
    @AuthCode(url = "timeZone",system = "zj")
    public Result getTimeZoneList(@RequestParam Map<String,Object> params, AuthCodeVo authCodeVo){
        String subjection = ShiroUtils.getUserEntity().getSubjection();
        if (StringUtils.isBlank(subjection)){
            subjection = "无隶属公司";
        }
        if (IntellectConstants.GROUP_PERMISSIONS.equals(authCodeVo.getAuthCode())){
            params.put("subjection","");
        }else {
            params.put("subjection",subjection);
        }
        List<ProjectTimeZoneVo> list = projectService.getTimeList(params);

        return Result.resultData(list);
    }

    /**
     * 获取节点填写详情
     */
    @GetMapping("/getOneById/{id}")
    @ApiOperation("根据ID获取节点填写详情")
//    @RequiresPermissions("zj:project:getOneById")
//    @RequiresPermissions("anlian:project:evInfo")
    public Result getOneById(@PathVariable("id") Long id){
        ProjectEntity project = projectService.getTimeZoneById(id);

        return Result.data(project);
    }


    /**
     * 修改时间节点
     */
    @PostMapping("/updateTimeZone")
    @SysLog("修改时间节点")
    @ApiOperation("修改时间节点")
//    @RequiresPermissions("zj:project:updateTimeZone")
//    @RequiresPermissions("anlian:project:update")
    public Result updateTimeZone(@RequestBody ProjectEntity project,HttpServletRequest httpRequest){
        Long projectId = project.getId();
        ProjectEntity projectEntity = projectService.getById(projectId);
        if (StringUtils.isBlank(projectEntity.getCharge())){
            return Result.error("未填写负责人");
        }
        projectService.updateTimeZone(httpRequest,project);
        return Result.ok();
    }


    /**
     * 任务排单
     */
    @PostMapping("/scheduling")
    @SysLog("任务排单")
    @ApiOperation("任务排单")
//    @RequiresPermissions("zj:project:scheduling")
//    @RequiresPermissions("anlian:project:save")
    public Result scheduling(@RequestBody ProjectDateUserVo projectDateUserVo){

        projectService.saveOrUpdateProjectUser(projectDateUserVo);

        return Result.ok();
    }


    /**
     * 单条任务信息
     */
    @GetMapping("/getOneInfo/{projectId}")
    @ApiOperation("单条任务信息")
//    @RequiresPermissions("anlian:project:evInfo")
    public Result getOneInfo(@PathVariable("projectId") Long projectId){
        ProjectDateUserVo projectDateUserVo = projectService.getOneByProjectId(projectId);
        return Result.data(projectDateUserVo);
    }

}
