package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 *职业危害警示标识设置一览实体类
 * 
 * @author LiXin
 * @data 2021-03-17
 */
@Data
@TableName("zj_warning_signs")
public class WarningSignsEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 *自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 * 项目ID
	 */
	private Long projectId;
	/**
	 * 关联的检测点ID
	 */
	private String pointId;
	/**
	 * 主要岗位或地点
	 */
	private String place;
	/**
	 * 职业病危害因素
	 */
	private String hazardFactors;
	/**
	 * 警示标识
	 */
	private String warning;
	/**
	 * 指令标识
	 */
	private String instruct;
	/**
	 * 告知卡
	 */
	private String card;
	/**
	 * 数据入库时间
	 */
	private Date createtime;
	/**
	 * 数据修改时间
	 */
	private Date updatetime;


}
