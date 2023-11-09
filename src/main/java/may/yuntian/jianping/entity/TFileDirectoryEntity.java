package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件目录表
 * 
 * @author LiXin
 * @email ''
 * @date 2022-04-14 20:45:49
 */
@TableName("zj_file_directory")
public class TFileDirectoryEntity implements Serializable {
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

	/**
	 * 设置：自增主键ID
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：自增主键ID
	 */
	public Long getId() {
		return id;
	}

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    /**
	 * 设置：责任者
	 */
	public void setSedgewick(String sedgewick) {
		this.sedgewick = sedgewick;
	}
	/**
	 * 获取：责任者
	 */
	public String getSedgewick() {
		return sedgewick;
	}
	/**
	 * 设置：文号
	 */
	public void setDocumentNum(String documentNum) {
		this.documentNum = documentNum;
	}
	/**
	 * 获取：文号
	 */
	public String getDocumentNum() {
		return documentNum;
	}
	/**
	 * 设置：题名
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * 获取：题名
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * 设置：日期
	 */
	public void setDateStr(Date dateStr) {
		this.dateStr = dateStr;
	}
	/**
	 * 获取：日期
	 */
	public Date getDateStr() {
		return dateStr;
	}
	/**
	 * 设置：页码
	 */
	public void setPagination(String pagination) {
		this.pagination = pagination;
	}
	/**
	 * 获取：页码
	 */
	public String getPagination() {
		return pagination;
	}
	/**
	 * 设置：备注
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * 获取：备注
	 */
	public String getRemarks() {
		return remarks;
	}
	/**
	 * 设置：案卷名称
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * 获取：案卷名称
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * 设置：档案编号
	 */
	public void setFileNum(String fileNum) {
		this.fileNum = fileNum;
	}
	/**
	 * 获取：档案编号
	 */
	public String getFileNum() {
		return fileNum;
	}
	/**
	 * 设置：回填类型(1实际调查日期 2实际采样日期 3实验室报告发送日期 4报告封面日期 5不需回填)
	 */
	public void setBackfillType(Integer backfillType) {
		this.backfillType = backfillType;
	}
	/**
	 * 获取：回填类型(1实际调查日期 2实际采样日期 3实验室报告发送日期 4报告封面日期 5不需回填)
	 */
	public Integer getBackfillType() {
		return backfillType;
	}
}
