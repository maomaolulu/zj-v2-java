package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 *项目防护用品
 * 
 * @author zhanghao
 * @data 2022-03-10
 */
@Data
@TableName("zj_project_protection")
public class ProjectProtectionEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 *自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 *项目id
	 */
	private Long projectId;
	/**
	 *防护用品id
	 */
	private Long protectionId;

	/**
	 *创建时间
	 */
	private Date createTime;

	/**
	 *修改时间
	 */
	private Date updateTime;

	@Override
	public String toString() {
		return "ProjectProtectionEntity{" +
				"id=" + id +
				", projectId=" + projectId +
				", protectionId=" + protectionId +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}
}
