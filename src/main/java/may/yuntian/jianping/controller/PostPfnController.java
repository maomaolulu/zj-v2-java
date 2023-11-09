package may.yuntian.jianping.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.anlian.vo.ProjectPutVo;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.jianping.mongoentity.WorkspaceEntity;
import may.yuntian.jianping.mongoservice.PostPfnService;
import may.yuntian.jianping.mongoservice.WorkspaceService;
import may.yuntian.jianping.vo.*;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 作业情况
 *
 * @author LiXin
 * @date 2022-08-14
 * @menu 作业情况
 */

@RestController
@Api(tags = "作业情况")
@RequestMapping("/postPfn")
public class PostPfnController {
    @Autowired
    private PostPfnService postPfnService;
    @Autowired
    private WorkspaceService workspaceService;


    /**
     * 分页列表
     */
    @GetMapping("/list/{projectId}")
    @ApiOperation("分页列表")
//    @RequiresPermissions("anlian:project:evInfo")
    public Result info(@PathVariable("projectId") Long projectId) {
        List<PostPfnVo> list = postPfnService.getAllListByProjectId(projectId);
        return Result.data(list);
    }


    /**
     * 新增  无ID时调用
     */
    @PostMapping("/insert")
    @SysLog("新增作业情况")
    @ApiOperation("新增作业情况")
//    @RequiresPermissions("anlian:project:save")
    public Result insert(@RequestBody PostPfnVo postPfnVo) {
        //判断危害因素是否缺少游离二氧化硅
        if(!postPfnService.isLackOfSilica(postPfnVo)){
            return Result.error("该工种涉及危害因素需要根据游离二氧化硅判定，危害因素中缺少[游离二氧化硅]");
        }
        // 判断送样状态
        if (postPfnService.sampleSendingStatus(postPfnVo)==1) {
            return Result.error("已送样,不允许新增作业信息");
        }else {
            PostPfnVo pfnVo = postPfnService.insertPostPfn(postPfnVo);
            return Result.ok().put("postPfnVo", pfnVo);
        }
    }


    /**
     * 新增  无ID时调用
     */
    @PostMapping("/insertBatch")
    @SysLog("批量新增作业情况")
    @ApiOperation("批量新增作业情况")
//    @RequiresPermissions("anlian:project:save")
    public Result insertBatch(@RequestBody List<WorkShopListVo> WorkShopListVos) {
        //判断危害因素是否缺少游离二氧化硅
        for (WorkShopListVo workShopListVo : WorkShopListVos) {
            List<PostPfnVo> postPfnVo = workShopListVo.getPostPfnVo();
            for (PostPfnVo pfnVo : postPfnVo) {
                if(!postPfnService.isLackOfSilica(pfnVo)){
                    return Result.error(workShopListVo + "车间/"+ pfnVo.getPfn() +"工种涉及危害因素需要根据游离二氧化硅判定，危害因素中缺少[游离二氧化硅]");
                }
            }
        }
        PostPfnVo postPfnVo = new PostPfnVo();
        Long projectId = WorkShopListVos.get(0).getPostPfnVo().get(0).getProjectId();
        postPfnVo.setProjectId(projectId);
        if (postPfnService.sampleSendingStatus(postPfnVo)==1){
            return Result.error("已送样,不允许新增作业信息");
        }
        List<PostPfnVo> postPfnVos1 = postPfnService.insertBatchPostPfn(WorkShopListVos);
        return Result.ok().put("postPfnVo", postPfnVos1);
    }

    /**
     * 保存  有ID时调用
     * 里面物质保存
     */
    @PostMapping("/save")
    @SysLog("保存作业情况")
    @ApiOperation("保存作业情况")
//    @RequiresPermissions("anlian:project:save")
    public Result save(@RequestBody PostPfnVo postPfnVo) {
        //判断危害因素是否缺少游离二氧化硅
        if(!postPfnService.isLackOfSilica(postPfnVo)){
            return Result.error("该工种涉及危害因素需要根据游离二氧化硅判定，危害因素中缺少[游离二氧化硅]");
        }
        // 判断送样状态
        switch (postPfnService.sample(postPfnVo)) {
            case 1:
                return Result.error("已送样,不允许修改车间信息");
            case 2:
                return Result.error("已送样,不允许修改点位信息");
            case 3:
                return Result.error("已送样,不允许物质信息");
            case 4:
                return Result.error("已送样,不允许个体采样信息");
            case 5:
                return Result.error("已送样,不允许样品数信息");
            case 6:
                return Result.error("已送样并且存在个体信息,不允许工种信息");
            default:
                break;
        }
        PostPfnVo pfnVo = postPfnService.savePostPfn(postPfnVo);
        return Result.ok().put("postPfnVo", pfnVo);

    }


    /**
     * 保存  有ID时调用--web端
     * 里面物质保存
     */
    @PostMapping("/webSave")
    @SysLog("保存作业情况--web端")
    @ApiOperation("保存作业情况--web端")
//    @RequiresPermissions("anlian:project:save")
    public Result webSave(@RequestBody PostPfnVo postPfnVo) {
        //判断危害因素是否缺少游离二氧化硅
        if(!postPfnService.isLackOfSilica(postPfnVo)){
            return Result.error("该工种涉及危害因素需要根据游离二氧化硅判定，危害因素中缺少[游离二氧化硅]");
        }
        // 判断送样状态
        switch (postPfnService.sample(postPfnVo)) {
            case 1:
                return Result.error("已送样,不允许修改车间信息");
            case 2:
                return Result.error("已送样,不允许修改点位信息");
            case 3:
                return Result.error("已送样,不允许修改物质信息");
            case 4:
                return Result.error("已送样,不允许修改个体采样信息");
            case 5:
                return Result.error("已送样,不允许修改样品数信息");
            case 6:
                return Result.error("已送样并且存在个体信息,不允许修改工种信息");
            default:
                break;
        }
        PostPfnVo pfnVo = postPfnService.webSave(postPfnVo);
        return Result.ok().put("postPfnVo", pfnVo);
    }


    /**
     * 删除作业情况
     */
    @PostMapping("/delete")
    @SysLog("删除作业情况")
    @ApiOperation("删除作业情况")
    public Result delete(@RequestBody PostPfnVo postPfnVo) {
        // 判断送样状态
        switch (postPfnService.sampleSendingStatus(postPfnVo)) {
            case 1:
                return Result.error("已送样,不允许删除作业情况信息");
            default:
                break;
        }
        postPfnService.deletePostPfn(postPfnVo);
        return Result.ok();
    }

    /**
     * 删除作业情况
     */
    @PostMapping("/deleteBatch")
    @SysLog("删除作业情况")
    @ApiOperation("删除作业情况")
    public Result deleteBatch(@RequestBody List<PostPfnVo> postPfnVos) {
        for (PostPfnVo postPfnVo : postPfnVos) {
            // 判断送样状态
            switch (postPfnService.sampleSendingStatus(postPfnVo)) {
                case 1:
                    return Result.error("已送样,不允许删除作业情况信息");
                default:
                    break;
            }
            postPfnService.deleteBatchPostPfn(postPfnVos);
        }
        return Result.ok();
    }


    @GetMapping("/findAllList")
    @ApiOperation("查询出所有定岗信息")
    public Result findAllList(Long projectId) {

        List<WorkspaceVo> allList = workspaceService.findAllList(projectId);
        return Result.ok().put("data", allList);

    }


    /**
     * 获取所有检测物质中含有游里二氧化硅的岗位
     *
     * @param projectId
     * @return
     */
    @GetMapping("/getSilicaList")
    @ApiOperation("查询出所有定岗信息")
    public Result getSilicaList(Long projectId) {

        List<WorkspaceEntity> allList = workspaceService.getSilicaList(projectId);
        return Result.ok().put("data", allList);
    }


/**----------------------------布局布点--------------------------------*/
    /**
     * 根据项目ID查询布局布点岗位信息
     *
     * @param params
     * @return
     */
    @GetMapping("/getMeasureList")
    @ApiOperation("查询出所有定岗信息")
    public Result getMeasureList(@RequestParam Map<String, Object> params) {
//        System.out.println("project_id="+projectId);
        Map<String, Object> allList = workspaceService.getMeasureByProjectId(params);
        return Result.data(allList);

    }

    /**
     * 批量修改布局布点岗位信息得点位编号
     */
    @PostMapping("/updateMeasureList")
    @SysLog("批量修改布局布点岗位信息得点位编号")
    @ApiOperation("批量修改布局布点岗位信息得点位编号")
    public Result updateMeasureList(@RequestBody List<MeasureLayoutVo> measureLayoutVoList) {
        workspaceService.updateSort(measureLayoutVoList);
        return Result.ok();
    }


    /**
     * 根据项目ID查询布局布点岗位信息
     *
     * @param workshopId
     * @return
     */
    @GetMapping("/test/{workshopId}")
    @ApiOperation("查询出所有定岗信息")
    public Result test(@PathVariable("workshopId") String workshopId) {
//        System.out.println("project_id="+projectId);
        WorkspaceEntity workspaceEntity = workspaceService.getNeedDelete(workshopId);
        return Result.data(workspaceEntity);

    }


    /**
     * 导出项目
     *
     * @return
     */
    @GetMapping("/down")
    @ApiOperation("导出项目")
    public Result test() {
        List<ProjectPutVo> list = postPfnService.getallprojectId();
        return Result.data(list);

    }

    /**
     * 前端获取手否送样及物理发送
     * @param projectId
     * @return
     */
    @GetMapping("/isExist/{projectId}")
    @ApiOperation("查询出所有定岗信息")
    public Result itExist(@PathVariable("projectId") Long projectId) {

        Map<String,Boolean> map = postPfnService.isDeliverAndPhysicalSend(projectId);
        return Result.data(map);

    }


}
