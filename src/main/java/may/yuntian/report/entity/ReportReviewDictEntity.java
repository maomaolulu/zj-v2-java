package may.yuntian.report.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 报告技术审核项对应关系表
 *
 * @author LinXin
 * @date 2022-04-14
 */
@Data
@TableName("zj_report_review_dict")
public class ReportReviewDictEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 * 项目ID
	 */
	private Long projectId;
	/**
	 * 技术审核项对应字典code
	 */
	private String sysDictCode;
	/**
	 * 选中的值(0否、1是)
	 */
	private Integer value;
	/**
	 * 审核reportReview；校核reportProofread
	 */
	private String type;

    /**
     *审核校核对应项名称
     */
	private String name;

}
