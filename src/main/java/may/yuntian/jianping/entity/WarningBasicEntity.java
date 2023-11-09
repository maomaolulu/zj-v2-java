package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 *职业危害警示标识设置基本信息
 * 实体类
 * @author LiXin
 * @data 2021-03-18
 */
@Data
@TableName("zj_warning_basic")
public class WarningBasicEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 * 警示标识
	 */
	private String warningLabels;
	/**
	 * 指令标识
	 */
	private String instructLogo;

}
