package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 *采样人员人员实体类
 *
 * @author LiXin
 * @data 2022-02-21
 */
@TableName("al_project_user")
public class ProjectUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *自增主键ID
     */
    @TableId
    private Long id;
    /**
     *项目ID
     */
    private Long projectId;
    /**
     *技术人员记录ID
     */
    private Long artisanId;
    /**
     *人员类型(1:实际采样组员  2报告封面采样人员   3报告签字人员 4组长)
     */
    private Integer types;
    /**
     *任务工作类型(组员，采样，调查，组长)
     */
    private String jobType;
    /**
     *人员用户ID
     */
    private Long userId;
    /**
     *人员用户名
     */
    private String username;
    /**
     *人员工号
     */
    private String jobNum;

    /**
     * 省申报字段：true 删除 / false 未删除
     * @return
     */
    @TableField(exist = false)
    private boolean delete;

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getArtisanId() {
        return artisanId;
    }

    public void setArtisanId(Long artisanId) {
        this.artisanId = artisanId;
    }

    public Integer getTypes() {
        return types;
    }

    public void setTypes(Integer types) {
        this.types = types;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJobNum() {
        return jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    @Override
    public String toString() {
        return "ProjectUserEntity{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", artisanId=" + artisanId +
                ", types=" + types +
                ", jobType='" + jobType + '\'' +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", jobNum='" + jobNum + '\'' +
                '}';
    }
}
