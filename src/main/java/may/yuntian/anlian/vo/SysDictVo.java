package may.yuntian.anlian.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 数据字典
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-09-26
 */
@Data
public class SysDictVo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 字典码
	 */
	private String code;
	/**
	 * 字典值
	 */
	private String value;


}
