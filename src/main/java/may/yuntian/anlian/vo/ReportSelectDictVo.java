package may.yuntian.anlian.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 报告技术审核项对应关系VO
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-09-26
 */
@Data
public class ReportSelectDictVo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 技术审核项对应字典code
	 */
	private String sysDictCode;
	/**
	 * 选中的值(0否、1是)
	 */
	private Integer value;

    /**
     * code所对应的段落意思
     */
    private String name;


	
}
