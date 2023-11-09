package may.yuntian.anlian.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author LiXin
 * @email ''
 * @date 2023-05-08 09:09:26
 */
@TableName("a_link_test")
public class LinkTestEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增ID
	 */
	@TableId
	private Integer id;
	/**
	 * 响应数据名称
	 */
	private String name;

	/**
	 * 设置：自增ID
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：自增ID
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：响应数据名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：响应数据名称
	 */
	public String getName() {
		return name;
	}
}
