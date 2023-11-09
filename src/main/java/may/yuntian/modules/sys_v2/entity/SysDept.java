package may.yuntian.modules.sys_v2.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门表
 *
 * @author hjy
 * @date 2022/7/22 13:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysDept extends BaseEntity {
    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 父部门ID
     */
    private Long parentId;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 父部门名称
     */
    private String parentName;

    /**
     * 子部门
     */
    private List<SysDept> children = new ArrayList<SysDept>();

}
