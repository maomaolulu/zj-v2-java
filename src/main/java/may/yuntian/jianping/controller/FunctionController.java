package may.yuntian.jianping.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.jianping.service.PublicityService;
import may.yuntian.jianping.vo.PublicityPageVo;
import may.yuntian.modules.sys.service.ShiroService;
import may.yuntian.sys.utils.Result;
import may.yuntian.sys.utils.ShiroUtils;
import may.yuntian.untils.FunctionAuthorityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author mi
 */
@RestController
@RequestMapping("/functions")
@Api(tags = "职能-控制器类")
public class FunctionController {
    @Autowired
    private PublicityService publicityService;

    @Autowired
    private ShiroService shiroService;

    /**
     * 胶装列表/事业部内勤人员/项目负责人/主管开放查询
     *
     * @param params
     * @return
     */
    @GetMapping("/getBindingPageNewList")
    @ApiOperation("胶装列表/事业部内勤人员/项目负责人/主管开放查询")
    public Result getBindingPageNewList(@RequestParam Map<String, Object> params) {
        // 登录人 是否是项目负责人
        Long userId = ShiroUtils.getUserEntity().getUserId();
        String username = ShiroUtils.getUserEntity().getUsername();
        Set<String> userPermissions = shiroService.getUserPermissions(userId);
        if (userPermissions.contains(FunctionAuthorityUtil.PROJECT_LEADER)) {
            // 项目负责人专属
            params.put("username", username);
            List<PublicityPageVo> list = publicityService.getBindingPageListForLeader(params);
            return Result.resultData(list);
        }
        List<PublicityPageVo> list = publicityService.getBindingPageList(params);
        return Result.resultData(list);
    }
}
