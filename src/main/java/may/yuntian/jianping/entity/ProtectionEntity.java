package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 *个人防护用品
 * 
 * @author zhanghao
 * @data 2022-03-10
 */
@Data
@TableName("al_protection")
public class ProtectionEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 *自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 *类型
	 */
	private String type;
	/**
	 *个人防护用品
	 */
	private String name;
	/**
	 *说明
	 */
	private String illustrate;
	/**
	 *snr
	 */
	private Integer snr;
	/**
	 *nrr
	 */
	private Integer nrr;

	/**
	 *删除标识
	 */
	private Integer isDel;
	/**
	 *创建时间
	 */
	private Date createTime;
	
	/**
	 *修改时间
	 */
	private Date updateTime;

}
