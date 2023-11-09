package may.yuntian.anlian.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
 *类型信息记录实体类
 * 
 * @author LiXin
 * @data 2020-12-10
 */
@Data
@TableName("t_category")
public class CategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 *自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 * 父级类别ID，一级类别为0
	 */
	private Long pid;
	/**
	 * 类型名称
	 */
	private String name;
	/**
	 * 英文缩写
	 */
	private String nameEn;
	/**
	 * 模块
	 */
	private String module;
	/**
	 * 排序
	 */
	private Integer orderNum;
	/**
	 * 是否删除  -1：已删除  0：正常
	 */
	private Integer delFlag;
	
	/**
	 * 是否是老数据 1是 2不是
	 */
	private Integer isOld;


}
