package may.yuntian.commission.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import may.yuntian.anlian.entity.ProjectEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: ANLIAN-JAVA
 * @description:   提成表实体类
 * @author: liyongqiang
 * @create: 2022-06-06 20:03
 */
@Data
@TableName("co_commission")
@AllArgsConstructor
@NoArgsConstructor
public class PerCommissionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键ID
     */
    @TableId
    private Long id;
    /**
     * 项目id
     */
    private Long projectId;
    /**
     * 提成类型：签发提成，归档提成，采样提成..
     */
    private String type;
    /**
     * 状态:1未提成 2已提成 3异常/数据被修改过 4软删除
     */
    private Integer state;
    /**
     * 提成金额
     */
    private BigDecimal cmsAmount;
    /**
     * 提成人
     */
    private String personnel;
    /**
     * 隶属公司
     */
    private String subjection;
    /**
     * 提成日期
     */
    private Date commissionDate;
    /**
     * 统计日期
     */
    private Date countDate;
    /**
     * 临时-项目类型
     */
    private String pType;
    /**
     *数据入库时间
     */
    private Date createtime;
    /**
     *修改时间
     */
    private Date updatetime;

    /**
     * 任务ID(环境)
     */
    private Long planId;

    /**
     * 检评绩效分配ID
     */
    private Long performanceAllocationId;

    /**
     * 人员部门ID
     */
    private Long deptId;
    /**
     * 1负责人,2组长 ,3组员
     */
    private Integer humenType;

    /**
     * 提成次数
     */
    private Integer commissionTimes;

    private String deptName;

    /**
     * 绩效总提成
     */
    @TableField(exist = false)
    private BigDecimal performanceMoney;

    @TableField(exist = false)
    private ProjectEntity project;

    @TableField(exist = false)
    private Date startDate;
    @TableField(exist = false)
    private Date endDate;

}
