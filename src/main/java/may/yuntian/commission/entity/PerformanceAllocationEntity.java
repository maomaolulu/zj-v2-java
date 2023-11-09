package may.yuntian.commission.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 绩效分配表
 * 
 * @author LiXin
 * @email ''
 * @date 2022-06-17 09:54:17
 */

@Data
@TableName("co_performance_allocation")
public class PerformanceAllocationEntity implements Serializable {
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
	 * 绩效提成类型
	 */
	private String types;
	/**
	 * 绩效提成日期
	 */
	private Date performanceDate;
	/**
	 * 绩效提成金额(总)
	 */
	private BigDecimal performanceMoney;
//	/**
//	 * 是否计入产值(1,是 2 否)
//	 */
//	private Integer includedOutput;
	/**
	 * 状态(1初始生成,2修改)
	 */
	private Integer status;

	@TableField(exist = false)
    private List<PerCommissionEntity> perCommissionEntityList;
}
