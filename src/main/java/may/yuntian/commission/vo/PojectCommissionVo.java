package may.yuntian.commission.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PojectCommissionVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 绩效分配id
     */
    private Long id;
    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 提成人隶属公司
     */
    private String subjection;
    /**
     * 是否提成(1未提成 2已提成)
     */
    private Integer state;
    /**
     * 提成人
     */
    private String personnel;
    /**
     * 提成类型
     */
    private String type;
    /**
     * 提成金额
     */
    private BigDecimal cmsAmount;
    /**
     * 提成日期
     */
    private Date commissionDate;
    /**
     * 核算日期
     */
    private Date countDate;
    /**
     * 受检单位
     */
    private String company;
    /**
     * 项目类型
     */
    private String projectType;
    /**
     * 项目编号
     */
    private String identifier;
    /**
     * 项目状态
     */
    private Integer status;
    /**
     * 业务员
     */
    private String salesmen;
    /**
     * 负责人
     */
    private String charge;
    /**
     * 项目隶属公司
     */
    private String companyOrder;
    /**
     * 项目金额
     */
    private BigDecimal totalMoney;
    /**
     * 项目净值
     */
    private BigDecimal netvalue;
    /**
     * 已收款
     */
    private BigDecimal receiptMoney;
    /**
     * 未结算
     */
    private BigDecimal totalMoneyOutstanding;
    /**
     * 业务费
     */
    private BigDecimal commission;
    /**
     * 评审费
     */
    private BigDecimal evaluationFee;
    /**
     * 中介费
     */
    private BigDecimal serviceCharge;
    /**
     * 分包费
     */
    private BigDecimal subprojectFee;
    /**
     * 其他费用
     */
    private BigDecimal otherExpenses;
    /**
     * 收款日期
     */
    private Date receiveAmount;
    /**
     * 签订日期
     */
    private Date signDate;
    /**
     * 报告装订日期
     */
    private Date reportBinding;
    /**
     * 归档日期
     */
    private Date reportFiling;
    /**
     * 委托类型
     */
    private String entrustType;
    /**
     * 备注
     */
    private String remarks;

    private String deptName;







}
