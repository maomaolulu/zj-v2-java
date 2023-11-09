package may.yuntian.anlian.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 *项目隶属来源表
 * 实体类
 * @author LiXin
 * @data 2021-03-22
 */
@Data
@TableName("t_order_source")
public class OrderSourceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 * 项目隶属来源
	 */
	private String orderSource;
	/**
	 * 类型（3项目隶属，4业务来源）
	 */
	private Integer type;
	


}
