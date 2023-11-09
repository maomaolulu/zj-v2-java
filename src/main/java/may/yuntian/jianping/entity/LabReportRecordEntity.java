package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 报告编制记录表
 * 
 * @author LiXin
 * @email ''
 * @date 2023-02-03 16:54:59
 */
@Data
@TableName("lab_report_record")
public class LabReportRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 项目id
	 */
	private Long projectId;
	/**
	 * 项目编号
	 */
	private String identifier;
	/**
	 * 受检单位名称
	 */
	private String company;
	/**
	 * 报告类型（Z：职业卫生，H：环境，G：公共卫生，L：来样检测，J：洁净室，X：学校卫生，M：消毒与灭菌，Y：一次性用品用具。）
	 */
	private String reportType;
	/**
	 * 报告编号
	 */
	private String reportNumber;
	/**
	 * 报告编制人
	 */
	private String compileName;
	/**
	 * 报告编制用户id
	 */
	private Long userId;
	/**
	 * 报告日期
	 */
	private Date reportDate;
	/**
	 * 报告状态（1：待提交，2：待审核，3：待签发，4：待质控签章）
	 */
	private Integer reportStatus;
	/**
	 * 可编制批次（多个以英文逗号分隔）
	 */
	private String preparedBatches;
	/**
	 * 已勾选批次（多个以英文逗号分隔）
	 */
	private String selectBatches;
	/**
	 * 检测日期范围
	 */
	private String detectDateRange;
	/**
	 * 审核人ids
	 */
	private String reviewIds;
	/**
	 * 审核人姓名（多个以英文逗号分隔）
	 */
	private String reviewNames;
	/**
	 * 签发人ids
	 */
	private String issuerIds;
	/**
	 * 签发人姓名（多个以英文逗号分隔）
	 */
	private String issuerNames;
	/**
	 * 职务
	 */
	private String position;
	/**
	 * 签发日期
	 */
	private Date issuerDate;
	/**
	 * 数据源（1检评，2评价，3环境，4公卫）
	 */
	private Integer dataSource;
	/**
	 * 物理因素（按模块来，多个以英文逗号分隔）
	 */
	private String physicalFactory;
	/**
	 * 已选化学因素（保存sample_record_id，以英文逗号分隔）
	 */
	private String chemicalFactory;
	/**
	 * 所属公司
	 */
	private String belongCompany;
	/**
	 * 主业务项目类型：1：化学物理；2：纯化学；3：纯物理；
	 */
	private Integer projectType;
	/**
	 * 创建者
	 */
	private String createBy;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新者
	 */
	private String updateBy;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 备注
	 */
	private String remark;

}
