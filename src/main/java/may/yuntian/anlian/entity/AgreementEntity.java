package may.yuntian.anlian.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目对应的委托协议列表
 * 
 * @author LiXin
 * @email ''
 * @date 2023-06-27 14:25:05
 */
@TableName("al_agreement")
public class AgreementEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId
	private Integer id;
	/**
	 * 所属项目id 一个委托协议可以对应多个项目
	 */
	private Long projectId;
	/**
	 * 文件创建者ID  云盘系统用户ID
	 */
	private Integer flieCreateById;
	/**
	 * 文件创建者 
	 */
	private String flieCreateByName;
	/**
	 * 文件创建时间
	 */
	private String flieCreateTime;
	/**
	 * 是否删除  0 :未删除    1: 删除
	 */
	private String deleted;
	/**
	 * 文件id
	 */
	private Integer fileId;
	/**
	 * 文件所在目录ID
	 */
	private Integer fdId;
	/**
	 * ID
	 */
	private Integer flId;
	/**
	 * 云盘加密字符串
	 */
	private String md5;
	/**
	 * 文件名
	 */
	private String name;
	/**
	 * 文件大小
	 */
	private Integer size;
	/**
	 * 文件类型
	 */
	private String types;
	/**
	 * 文件识别码
	 */
	private String uuid;
	/**
	 * 数据入库时间
	 */
	private Date createtime;
	/**
	 * 文件同名的数量
	 */
	private Integer num;

	/**
	 * 设置：ID
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：ID
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：所属项目id 一个委托协议可以对应多个项目
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	/**
	 * 获取：所属项目id 一个委托协议可以对应多个项目
	 */
	public Long getProjectId() {
		return projectId;
	}
	/**
	 * 设置：文件创建者ID  云盘系统用户ID
	 */
	public void setFlieCreateById(Integer flieCreateById) {
		this.flieCreateById = flieCreateById;
	}
	/**
	 * 获取：文件创建者ID  云盘系统用户ID
	 */
	public Integer getFlieCreateById() {
		return flieCreateById;
	}
	/**
	 * 设置：文件创建者 
	 */
	public void setFlieCreateByName(String flieCreateByName) {
		this.flieCreateByName = flieCreateByName;
	}
	/**
	 * 获取：文件创建者 
	 */
	public String getFlieCreateByName() {
		return flieCreateByName;
	}
	/**
	 * 设置：文件创建时间
	 */
	public void setFlieCreateTime(String flieCreateTime) {
		this.flieCreateTime = flieCreateTime;
	}
	/**
	 * 获取：文件创建时间
	 */
	public String getFlieCreateTime() {
		return flieCreateTime;
	}
	/**
	 * 设置：是否删除  0 :未删除    1: 删除
	 */
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	/**
	 * 获取：是否删除  0 :未删除    1: 删除
	 */
	public String getDeleted() {
		return deleted;
	}
	/**
	 * 设置：文件id
	 */
	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}
	/**
	 * 获取：文件id
	 */
	public Integer getFileId() {
		return fileId;
	}
	/**
	 * 设置：文件所在目录ID
	 */
	public void setFdId(Integer fdId) {
		this.fdId = fdId;
	}
	/**
	 * 获取：文件所在目录ID
	 */
	public Integer getFdId() {
		return fdId;
	}
	/**
	 * 设置：ID
	 */
	public void setFlId(Integer flId) {
		this.flId = flId;
	}
	/**
	 * 获取：ID
	 */
	public Integer getFlId() {
		return flId;
	}
	/**
	 * 设置：云盘加密字符串
	 */
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	/**
	 * 获取：云盘加密字符串
	 */
	public String getMd5() {
		return md5;
	}
	/**
	 * 设置：文件名
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：文件名
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：文件大小
	 */
	public void setSize(Integer size) {
		this.size = size;
	}
	/**
	 * 获取：文件大小
	 */
	public Integer getSize() {
		return size;
	}
	/**
	 * 设置：文件类型
	 */
	public void setTypes(String types) {
		this.types = types;
	}
	/**
	 * 获取：文件类型
	 */
	public String getTypes() {
		return types;
	}
	/**
	 * 设置：文件识别码
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	/**
	 * 获取：文件识别码
	 */
	public String getUuid() {
		return uuid;
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
	 * 设置：文件同名的数量
	 */
	public void setNum(Integer num) {
		this.num = num;
	}
	/**
	 * 获取：文件同名的数量
	 */
	public Integer getNum() {
		return num;
	}
}
