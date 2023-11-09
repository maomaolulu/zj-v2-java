package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 实验室-主数据
 * 
 * @author LiXin
 * @email ''
 * @date 2022-09-13 16:54:23
 */
@Data
@TableName("lab_main_data")
public class MainDataEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键，自增
	 */
	@TableId
	private Integer id;
	/**
	 * 序号1-废除
	 */
	private String serialNumber1;
	/**
	 * 产品/检测对象
	 */
	private String category;
	/**
	 * 序号2-废除
	 */
	private String serialNumber2;
	/**
	 * 检测项目名称
	 */
	private String subName;
	/**
	 * 废除
	 */
	private String subNames;
	/**
	 * 检测标准（方法）编号
	 */
	private String detectNum;
	/**
	 * 检测标准（方法）名称
	 */
	private String detectName;
	/**
	 * 限制范围
	 */
	private String limitedRange;
	/**
	 * 样品类型-废除
	 */
	private String sampleType;
	/**
	 * 方法适用范围-废除
	 */
	private String availableRange;
	/**
	 * 所属机构-废除
	 */
	private Long deptId;
	/**
	 * 
	 */
	private String createBy;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private String updateBy;
	/**
	 * 
	 */
	private Date updateTime;
	/**
	 * 对照表id-（anlian_indicator）（全集团所有检测项目数据表）
	 */
	private Long indicatorId;
	/**
	 * 是否有资质 1 有  ； 2 无（默认）
	 */
	private Integer isQualification;
	/**
	 * 是否有检测能力 1 有  ； 2 无（默认）
	 */
	private Integer isDetectionAbility;
	/**
	 * 批准日期
	 */
	private Date approvalDate;
	/**
	 * 变更通过日期
	 */
	private Date changeDateApproval;
	/**
	 * 授权签字人id集合
	 */
	private String authorizedSignatoryIds;
	/**
	 * 授权签字人
	 */
	private String authorizedSignatory;
	/**
	 * 是否可用：1 可用；2 不可用
	 */
	private Integer isAvailable;
	/**
	 * 是否默认-待定(当仅有限制范围不同的时候，选其中一条默认)： 1 默认；2：不默认
	 */
	private Integer isDefault;
	/**
	 * 数据所属公司
	 */
	private String dataBelong;

}
