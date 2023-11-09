package may.yuntian.laboratory.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 *检测情况实体类
 * 
 * @author LiXin
 * @data 2020-12-17
 */
@Data
@TableName("zj_detection")
public class DetectionEntity implements Serializable {
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
	 *样品名称
	 */
	private String sampleName;
	/**
	 *样品数量 parameter
	 */
	private Integer sampleNum;
	/**
	 * 报告编号
	 */
	private String reportNumber;
	/**
	 *样品性状
	 */
	private String sampleCharacter;
	/**
	 *采样日期
	 */
	private String sampleDate;
	/**
	 *接收日期
	 */
	private String receiptDate;
	/**
	 *测试日期
	 */
	private String testDate;
	/**
	 *报告日期
	 */
	private Date reportDate;
	/**
	 *编制人
	 */
	private String compileName;
	/**
	 *审核人
	 */
	private String auditName;
	/**
	 *签发人
	 */
	private String signer;
	/**
	 *职务
	 */
	private String position;
	/**
	 *签发日期
	 */
	private Date issueDate;
	/**
	 *数据入库时间
	 */
	private Date createtime;
	/**
	 *修改时间
	 */
	private Date updatetime;
	/**
	 * 受检单位
	 */
	@TableField(exist=false)
	private String company;
	/**
	 * 受检单位地址
	 */
	@TableField(exist=false)
	private String officeAddress;
	/**
	 * 委托单位
	 */
	@TableField(exist=false)
	private String entrustCompany;
	/**
	 * 委托单位地址
	 */
	@TableField(exist=false)
	private String entrustAddress;
	/**
	 * 检测性质
	 */
	@TableField(exist=false)
	private String testNature;
	/**
	 * 检测类型(评价/定期/其它)
	 */
	@TableField(exist=false)
	private String detectionType;

	

}
