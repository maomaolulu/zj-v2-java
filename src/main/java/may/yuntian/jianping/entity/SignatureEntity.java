package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 电子签名路径表
 * 
 * @author LiXin
 * @date 2021-01-29
 */
@TableName("t_signature")
public class SignatureEntity implements Serializable {
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
	 *  电子签名图路径
	 */
	private String path;
	/**
	 *  用处  (签在现场调查、采样计划、采样记录)
	 */
	private String useList;
    /**
     * 人员类型
     */
    private Integer peopleType;
	/**
	 * 用处数组【】
	 */
	@TableField(exist=false)
	private List<String> useSpitList;
	/**
	 * 数据入库时间
	 */
	private Date createtime;
	/**
	 * 修改时间
	 */
	private Date updatetime;
    /**
     * minio路径
     */
    private String minioPath;
	

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
	/**
	 * 设置：项目ID
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	/**
	 * 获取：项目ID
	 */
	public Long getProjectId() {
		return projectId;
	}
	/**
	 * 设置：电子签名图路径
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * 获取：电子签名图路径
	 */
	public String getPath() {
		return path;
	}
	/**
	 * 设置：数据入库时间
	 */
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	/**
	 * 获取：数据入库时间
	 */
	public Date getCreatetime() {
		return createtime;
	}
	/**
	 * 设置：修改时间
	 */
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getUpdatetime() {
		return updatetime;
	}
	public String getUseList() {
		return useList;
	}
	public void setUseList(String useList) {
		this.useList = useList;
	}
	public List<String> getUseSpitList() {
		return useSpitList;
	}
	public void setUseSpitList(List<String> useSpitList) {
		this.useSpitList = useSpitList;
	}

    public Integer getPeopleType() {
        return peopleType;
    }

    public void setPeopleType(Integer peopleType) {
        this.peopleType = peopleType;
    }

    public String getMinioPath() {
        return minioPath;
    }

    public void setMinioPath(String minioPath) {
        this.minioPath = minioPath;
    }
}
