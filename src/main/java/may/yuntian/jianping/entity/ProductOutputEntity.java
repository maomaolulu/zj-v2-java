package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 主要产品与年产量
 * 
 * @author LiXin
 * @email ''
 * @date 2022-11-10 15:52:36
 */
@Data
@TableName("zj_product_output")
public class ProductOutputEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增ID
	 */
	@TableId
	private Long id;
	/**
	 * 项目ID
	 */
	private Long projectId;
	/**
	 * 主要产品
	 */
	private String majorProduct;
	/**
	 * 年产量
	 */
	private String annualYield;
}
