package may.yuntian.modules.sys_v2.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


/**
 * 菜单实体
 *
 * @author hjy
 * @date 2023/03/31 9:08
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_menu")
public class SysMenu extends BaseEntity {
    /**
     * 菜单ID
     */
    @TableId
    private Long menuId;
    /**
     * 父菜单ID，一级菜单为0
     */
    private Long parentId;
    /**
     * 父菜单名称
     */
    @TableField(exist = false)
    private String parentName;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 菜单URL(路由地址)
     */
    private String url;
    /**
     * 权限标识
     */
    private String perms;
    /**
     * 类型 1：菜单   2：按钮   3：职能
     */
    private Integer type;
    /**
     * 权限验证码(职能相关 )
     */
    private String authCode;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 排序
     */
    private Integer orderNum;
    /**
     * 系统名称 sys；pj；zj；
     */
    private String moduleName;
    /**
     * 显示状态（0显示 1隐藏）
     */
    private String visible;

    /**
     * 菜单状态（0显示 1隐藏）
     */
    private String status;

    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<SysMenu>();

    @NotBlank(message = "菜单名称不能为空")
    @Size(min = 0, max = 50, message = "菜单名称长度不能超过50个字符")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(message = "显示顺序不能为空")
    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    @Size(min = 0, max = 200, message = "路由地址不能超过200个字符")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @NotBlank(message = "菜单类型不能为空")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Size(min = 0, max = 100, message = "权限标识长度不能超过100个字符")
    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    @NotBlank(message = "系统类型不能为空：sys；pj；zj；")
    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
