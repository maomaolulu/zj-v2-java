package may.yuntian.external.datainterface.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author: liyongqiang
 * @create: 2023-04-28 15:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SysUserVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 项目id **/
    private Long projectId;

    /** 用户id **/
    private Long userId;
    /** 用户名 **/
    private String username;
    /** 员工编号 **/
    private String jobNum;
    /** 隶属公司 **/
    private String subjection;
    /** 部门id **/
    private Long deptId;
    /** 状态：0正常，1禁用，2离职 **/
    private Integer status;
    /** 备注 **/
    private String remark;

}
