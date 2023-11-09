package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 存在的问题及建议
 * 
 * @author LiXin
 * @date 2020-12-30
 */
@TableName("zj_question_advice")
public class QuestionAdviceEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 * 存在的问题
	 */
	private String openQuestion;
	/**
	 * 建议
	 */
	private String advice;
	/**
	 * 默认选择(0:无,1选中)
	 */
	private Integer defvalue;
	
	/**
	 * 已经选择的值(0:无,1选中)
	 */
	@TableField(exist=false)
	private Integer selectValue;
	/**
	 * 项目ID
	 */
	@TableField(exist=false)
	private Long projectId;


	
	/**
	 * 设置自增主键ID
	 */
	public void setId (Long id) {
		this.id = id;
	}
	/**
	 * 获取自增主键ID
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置存在的问题
	 */
	public void setOpenQuestion(String openQuestion) {
		this.openQuestion = openQuestion;
	}
	/**
	 * 获取存在的问题
	 */
	public String getOpenQuestion() {
		return openQuestion;
	}
	/**
	 * 设置建议
	 */
	public void setAdvice(String advice) {
		this.advice = advice;
	}
	/**
	 * 获取建议
	 */
	public String getAdvice() {
		return advice;
	}
	
	public Integer getDefvalue() {
		return defvalue;
	}
	public void setDefvalue(Integer defvalue) {
		this.defvalue = defvalue;
	}
	public Integer getSelectValue() {
		return selectValue;
	}
	public void setSelectValue(Integer selectValue) {
		this.selectValue = selectValue;
	}
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}


	
	
	
}
