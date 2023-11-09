package may.yuntian.jianping.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;


/**
 *个人防护用品
 * 
 * @author zhanghao
 * @data 2022-03-10
 */
@Data
public class EngineDto implements Serializable {
	private static final long serialVersionUID = 1L;
	

	/**
	 *工程防护
	 */
	@Field(name = "engineering_protection")
	private String engineeringProtection;
	/**
	 *运行情况
	 */
	@Field(name = "run_condition")
	private String runCondition;
}
