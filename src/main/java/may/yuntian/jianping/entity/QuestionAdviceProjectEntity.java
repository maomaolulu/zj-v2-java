package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 存在的问题及建议对应项目关系
 * 
 * @author LiXin
 * @date 2020-12-30
 */
@Data
@TableName("zj_question_advice_project")
public class QuestionAdviceProjectEntity implements Serializable {
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
     * 报告信息表ID
     */
    private Long reportImproveId;
	/**
	 * 存在的问题及建议表ID
	 */
	private Long questionAdviceId;
	/**
	 * 存在的问题
	 */
	private String openQuestion;
	/**
	 * 建议
	 */
	private String advice;
	/**
	 * 是否自定义（0否，1是）
	 */
	private Integer location;

}
