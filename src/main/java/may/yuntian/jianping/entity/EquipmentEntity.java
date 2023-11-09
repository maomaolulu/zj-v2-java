package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhuYiCheng
 * @date 2023/7/13 10:19
 */
@Data
@TableName("al_equipment")
public class EquipmentEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId
    private Long id;
    /**
     * 所属公司id
     */
    private Long companyId;
    /**
     * 所属公司名称
     */
    private String company;
    /**
     * 所属部门id
     */
    private Long deptId;
    /**
     * 所属部门名称
     */
    private String dept;
    /**
     * 固定资产名称
     */
    private String name;
    /**
     * 规格、型号
     */
    private String model;
    /**
     * 资产编号;质控部定义编号规则）
     */
    private String assetSn;
    /**
     * 状态:1在用;2准用,3待修,4维修中,5送检中,6报废,7外借,8封存9退货
     */
    private Integer state;
    /**
     * 上次检定时间;仪器）
     */
    private Date inspectTime;
    /**
     * 下次检定时间;仪器）
     */
    private Date nextInspectTime;
    /**
     * 上次检定结果;仪器）
     */
    private String inspectResult;
    /**
     * 测量范围
     */
    private String measureScope;
    /**
     * 测量范围最小值
     */
    private Float msMin;
    /**
     * 测量范围最大值
     */
    private Float msMax;
    /**
     * 固定资产录入时间
     */
    private Date createTime;
    /**
     * 仪器分类id
     */
    private Integer categoryId;
    /**
     * 仪器分类名称
     */
    private String categoryName;
    /**
     * 系统编号
     */
    private String systemNumber;
    /**
     * 修正系数/校准值
     */
    private String correctionFactor;
    /**
     * 维护信息创建时间
     */
    private Date maintainInfoCreateTime;
    /**
     * 维护信息更新时间
     */
    private Date maintainInfoUpdateTime;
}
