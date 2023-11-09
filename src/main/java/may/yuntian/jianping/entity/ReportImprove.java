package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 报告信息完善记录表
 * 
 * @author LiXin
 * @date 2020-09-29
 */
@Data
@TableName("zj_report_improve")
public class ReportImprove implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 * 项目ID（唯一索引）
	 */
	private Long projectId;
	/**
	 * 业务来源
	 */
	private String source;
	/**
	 * 检测类别
	 */
	private String testType;
	/**
	 * 检测范围
	 */
	private String testRange;
	/**
	 * 劳动定员及工种（岗位）设置情况
	 */
	private String postSetting;
	/**
	 * 检查时的生产情况
	 */
	private String production;
	/**
	 * 检测频次
	 */
	private String  frequency;
	/**
	 * 验证性检测信息
	 */
	private String  confirmatory;
	/**
	 * 检测结构评价小结
	 */
	private String summary;
	/**
	 * 存在的问题
	 */
	private String problems;
	/**
	 * 报告名称
	 */
	private String reportName;
	/**
	 * 报告路径
	 */
	private String reportUrl;
	/**
	 * X射线分包
	 */
	private String xRay;
	/**
	 * 数据库入库时间
	 */
	private Date createtime;
	/**
	 * 修改时间
	 */
	private Date updatetime;


	@Override
	public String toString() {
		return "ReportImproveEntity [id=" + id + ", projectId=" + projectId + ", source=" + source + ", testType="
				+ testType + ", testRange=" + testRange + ", postSetting=" + postSetting + ", production=" + production
				+ ", frequency=" + frequency + ", summary=" + summary + ", problems=" + problems + ", reportName="
				+ reportName + ", reportUrl=" + reportUrl + ", createtime=" + createtime + ", updatetime=" + updatetime
				+ "]";
	}
	
}
