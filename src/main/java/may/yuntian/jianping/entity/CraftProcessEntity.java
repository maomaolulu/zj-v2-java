package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@TableName("zj_craft_process")
@Data
public class CraftProcessEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键ID
     */
    @TableId
    private Long id;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 工艺流程名称
     */
    private String name;

    /**
     * 工艺流程图路径
     */
    private String path;

    /**
     * 工艺流程图文本描述
     */
    private String description;

    /**
     * 部门名称
     */
    private String department;

    /**
     * 人数
     */
    private Long peopleNum;

    /**
     * 职业健康检查机构名称
     */
    private String inspectionOrg;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 最近一次职业健康检查时间
     */
    private Date lastTestTime;

    /**
     *数据入库时间
     */
    private Date createtime;
    /**
     *修改时间
     */
    private Date updatetime;

    /**
     * minio路径
     */
    private String minioPath;

    /**
     *所有流程图路径集合
     */
    @TableField(exist=false)
    private List<String> pathAllList;


}
