package may.yuntian.jianping.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.common.utils.R;
import may.yuntian.jianping.entity.SubstanceEntity;
import may.yuntian.jianping.service.SubstanceService;
import may.yuntian.jianping.vo.SubstanceNewVo;
import may.yuntian.jianping.vo.SubstanceVo;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 检测物质数据
 *
 * @author zhanghao
 * @date 2022-03-09
 * @menu 检测物质数据
 */

@RestController
@Api(tags = "检测物质数据")
@RequestMapping("/substance")
public class SubstanceController {
    @Autowired
    private SubstanceService substanceService;

//    /**
//     * 查询全部物质信息
//     */
//    @GetMapping("/listAll")
//    @ApiOperation("检测物质数据下拉框列表")
////    @RequiresPermissions("anlian:plan:list")
//    public Result listAll() {
//        List<SubstanceVo> list = substanceService.getListAll();
//        return Result.ok().put("data", list);
//    }


    /**
     * 分流xin查询物质信息
     *
     * @param projectId
     * @return
     */
    @GetMapping("/listAll")
    @ApiOperation("检测物质数据下拉框列表")
//    @RequiresPermissions("anlian:plan:list")
    public Result listNewAll(@RequestParam Long projectId) {
        // 项目对应的公司
        String companyName = substanceService.getProjectCompany(projectId);
        List<SubstanceNewVo> list = substanceService.getListNewAll(companyName);
        return Result.ok().put("data", list);
    }


    /**
     * 获取固定岗位物质
     */
    @GetMapping("/listByShortcutKey")
    @ApiOperation("获取固定岗位物质")
//    @RequiresPermissions("anlian:plan:list")
    public Result listByShortcutKey(String name) {
        List<SubstanceVo> list = substanceService.getListByShortcutKey(name);
        return Result.ok().put("data", list);
    }


}
