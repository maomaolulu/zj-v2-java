package may.yuntian.modules.sys_v2.entity.vo;

import lombok.Data;
import may.yuntian.modules.sys_v2.entity.SysRole;

import java.util.List;

/**
 * 权限接受临时载体
 *
 * @author hjy
 * @date 2023/4/7 18:22
 */
@Data
public class SysRoleVo {
    /**
     * 权限数据来源
     */
    private SysRole role;
    /**
     * 部门数据权限
     */
    private List<Long> deptIdList;
    /**
     * 项目类型权限
     */
    private List<Long> projectTypeList;
    /**
     * 项目隶属权限
     */
    private List<Long> orderList;
    /**
     * 业务来源权限
     */
    private List<Long> sourceList;
}
