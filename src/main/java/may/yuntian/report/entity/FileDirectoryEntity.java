package may.yuntian.report.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件目录表
 * 
 * @author LiXin
 * @email ''
 * @date 2022-04-14 20:45:49
 */
@Data
@TableName("t_file_directory")
public class FileDirectoryEntity implements Serializable {
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
	 * 责任者
	 */
	private String sedgewick;
	/**
	 * 文号
	 */
	private String documentNum;
	/**
	 * 题名
	 */
	private String title;
	/**
	 * 日期
	 */
	private Date dateStr;
	/**
	 * 页码
	 */
	private String pagination;
	/**
	 * 备注
	 */
	private String remarks;
	/**
	 * 案卷名称
	 */
	private String fileName;
	/**
	 * 档案编号
	 */
	private String fileNum;
	/**
	 * 回填类型(1实际调查日期 2实际采样日期 3实验室报告发送日期 4报告封面日期 5不需回填)
	 */
	private Integer backfillType;

}
