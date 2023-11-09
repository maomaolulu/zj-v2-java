package may.yuntian.jianping.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.R;
import may.yuntian.jianping.entity.ProtectionEntity;
import may.yuntian.jianping.service.ProtectionService;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 *个人防护用品信息
 * @author zhanghao
 * @date 2022-03-10
 * @menu 个人防护用品信息
 */

@RestController
@Api(tags="个人防护用品信息")
@RequestMapping("/protection")
public class ProtectionController {
	@Autowired
	private ProtectionService protectionService;



    @GetMapping("/getProtection")
    @ApiOperation("根据名称获取个人防护用品列表")
    public Result getProtectionList(String name){
        List<ProtectionEntity> list = protectionService.getProtectionList(name);
        return Result.data(list);
    }

//
//    /**
//     * 保存
//     */
//    @PostMapping("/save")
//    @SysLog("新增个人防护用品")
//    @ApiOperation("新增个人防护用品")
////    @RequiresPermissions("anlian:operation:save")
//    public R save(@RequestBody ProtectionEntity protectionEntity){
//
//        protectionService.save(protectionEntity);
//
//        return R.ok().put("protectionEntity",protectionEntity);
//    }
//
//
//    /**
//     * 修改
//     */
//    @PostMapping("/update")
//    @SysLog("修改个人防护用品")
//    @ApiOperation("修改个人防护用品")
////    @RequiresPermissions("anlian:operation:save")
//    public R update(@RequestBody ProtectionEntity protectionEntity){
//
//        protectionService.updateById(protectionEntity);
//
//        return R.ok().put("protectionEntity",protectionEntity);
//    }
//
//
//    /**
//     * 删除
//     */
//    @PostMapping("/delete")
//    @SysLog("删除劳动者作业情况调查")
//    @ApiOperation("删除劳动者作业情况调查")
////    @RequiresPermissions("anlian:operation:delete")
//    public R delete(@RequestBody Long[] ids){
//        protectionService.removeByIds(Arrays.asList(ids));
//
//        return R.ok();
//    }


}
