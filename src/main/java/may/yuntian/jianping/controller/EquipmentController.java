package may.yuntian.jianping.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.common.utils.R;
import may.yuntian.jianping.dto.UsableDto;
import may.yuntian.jianping.service.EquipmentService;
import may.yuntian.modules.sys_v2.utils.ShiroUtilsV2;
import may.yuntian.sys.utils.Result;
import may.yuntian.sys.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author ZhuYiCheng
 * @date 2023/7/13 10:31
 */
@RestController
@Api(tags="仪器设备")
@RequestMapping("/sample_equip")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;


    /**
     * 空气(毒物和粉尘)中有害物质检测可用仪器列表
     *
     * @return
     */
    @GetMapping("/air_eq_lis")
    @ApiOperation("空气(毒物和粉尘)中有害物质检测可用仪器列表")
    public Result airEqLis(Long project_id) {

        Map<String, Object> rst = equipmentService.airEqLis();
        return Result.ok("采样设备信息获取成功",rst);
    }

    /**
     *  根据采样记录中物质信息获取可用采样设备名称
     */
    @GetMapping("/usable_lis")
    @ApiOperation("根据采样记录中物质信息获取可用采样设备名称")
    public Result usableLis(UsableDto dto){
        Map<String, Object> rst = equipmentService.usableLis(dto);
        return Result.ok("采样设备信息获取成功", rst);
    }


}
