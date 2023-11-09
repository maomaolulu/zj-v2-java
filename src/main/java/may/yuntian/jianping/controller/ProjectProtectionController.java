package may.yuntian.jianping.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.jianping.entity.ProtectionEntity;
import may.yuntian.jianping.service.ProjectProtectionService;
import may.yuntian.jianping.vo.ProtectionVo;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *项目防护用品信息
 * @author zhanghao
 * @date 2022-03-10
 * @menu 项目防护用品信息
 */

@RestController
@Api(tags="项目防护用品信息")
@RequestMapping("/projectProtection")
public class ProjectProtectionController {

	@Autowired
	private ProjectProtectionService projectProtectionService;

    /**
     * 批量保存对因对应关系
     */
    @PostMapping("/saveBatch")
    @SysLog("批量保存对因对应关系")
    @ApiOperation("批量保存对因对应关系")
    public Result save(@RequestBody List<ProtectionVo> protectionVos){
        projectProtectionService.saveProjectProtection(protectionVos);
        return Result.ok();
    }

    /**
     * 根据名称获取个人防护用品列表
     * @param projectId
     * @return
     */
    @GetMapping("/getProtectionList/{projectId}")
    @ApiOperation("根据名称获取个人防护用品列表")
    public Result getProtectionList(@PathVariable("projectId") Long projectId){
        List<ProtectionVo> list = projectProtectionService.getProtectionList(projectId);
        return Result.data(list);
    }

}
