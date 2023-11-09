package may.yuntian.commission.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PerformanceAllocationVo implements Serializable {
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
     * 项目编号
     */
    private String identifier;
    /**
     * 受检单位
     */
    private String company;
    /**
     * 项目净值
     */
    private BigDecimal netvalue;
    /**
     * 提成金额(总)
     */
    private BigDecimal performanceMoney;
    /**
     * 提成类型
     */
    private String types;
    /**
     * 提成日期
     */
    private Date performanceDate;
    /**
     * 提成金额(包含提成人){name:money}
     */
    private String commissionMoney;
    /**
     * 是否计入产出(1是,2否)
     */
    private Integer includedOutput;
    /**
     * 负责人
     */
    private String charge;

    /**
     * 采样人员--统计用
     */
    private String samplePeople;





}
