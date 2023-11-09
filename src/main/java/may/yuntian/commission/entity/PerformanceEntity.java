package may.yuntian.commission.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 人员绩效表--检评
 * 
 * @author LiXin
 * @email ''
 * @date 2022-06-17 09:54:17
 */
@Data
@TableName("co_performance")
public class PerformanceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 * 人员ID
	 */
	private Long userid;
	/**
	 * 人员姓名
	 */
	private String username;
	/**
	 * 等级表ID
	 */
	private Long levelId;
	/**
	 * 目标产出级别
	 */
	private String level;
	/**
	 * 目标产出类别
	 */
	private String type;
	/**
	 * 目标产出金额(万元)
	 */
	private BigDecimal targetOutput;
	/**
	 * 累计产出金额(万元)
	 */
	private BigDecimal cumulativeOutput;
	/**
	 * 数据修改时间
	 */
	private Date updatetime;
    /**
     * 晋升后指标额
     */
	private BigDecimal promotionOutput;
    /**
     * 晋升日期
     */
	private Date promotionDate;

    /**
     * 完成百分比
     */
    @TableField(exist = false)
    private BigDecimal percentComplete;


}
