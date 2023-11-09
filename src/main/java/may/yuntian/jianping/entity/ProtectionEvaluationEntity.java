package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 *个人防护用品有效性评价实体类
 * 
 * @author LiXin
 * @data 2020-12-18
 */
@Data
@TableName("zj_protection_evaluation")
public class ProtectionEvaluationEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 *自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 *项目ID
	 */
	private Long projectId;
	/**
	 *防护用品
	 */
	private String protectEquip;
	/**
	 *型号
	 */
	private String model;
	/**
	 *参数
	 */
	private String params;
	/**
	 *危害因素
	 */
	private String hazardFactors;
	/**
	 *有效性评价
	 */
	private String evaluate;
	/**
	 *数据入库时间
	 */
	private Date createtime;
	/**
	 *修改时间
	 */
	private Date updatetime;


}
