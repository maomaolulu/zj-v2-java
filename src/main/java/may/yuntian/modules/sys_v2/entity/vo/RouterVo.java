package may.yuntian.modules.sys_v2.entity.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * 路由配置信息
 *
 * @author ruoyi
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVo {
    /**
     * 菜单id
     */
    private Long menuId;
    /**
     * 路由名字
     */
    private String name;
    /**
     * 设置该路由的图标，对应路径src/assets/icons/svg
     */
    private String icon;
    /**
     * 路由地址
     */
    private String url;
    /**
     * 菜单类型
     */
    private Integer type;
    /**
     * 是否隐藏路由，当设置 true 的时候该路由不会再侧边栏出现
     */
    private boolean hidden;
    /**
     * 系统模块名称
     */
    private String moduleName;

    /**
     * 子路由
     */
    private List<RouterVo> list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean getHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public List<RouterVo> getList() {
        return list;
    }

    public void setList(List<RouterVo> list) {
        this.list = list;
    }
}
