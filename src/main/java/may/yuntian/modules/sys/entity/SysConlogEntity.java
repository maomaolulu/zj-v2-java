package may.yuntian.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户登录信息统计
 * 
 * @author LiXin
 * @date 2021-04-30
 */
@TableName("sys_conlog")
public class SysConlogEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@TableId
	private Long id;
	//用户id
	private Long userid;
	//周登录次数
	private Integer weekLoginTotal;
	//月登录次数
	private Integer monthLoginTotal;
	//数据入库时间
	private Date createtime;
	// 修改时间
	private Date updatetime;
	//用户名
	@TableField(exist=false)
	private String username;


	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：用户id
	 */
	public Long getUserid() {
		return userid;
	}
	/**
	 * 获取：用户id
	 */
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	/**
	 * 设置：周登录次数
	 */
	public Integer getWeekLoginTotal() {
		return weekLoginTotal;
	}
	/**
	 * 获取：周登录次数
	 */
	public void setWeekLoginTotal(Integer weekLoginTotal) {
		this.weekLoginTotal = weekLoginTotal;
	}
	/**
	 * 设置：月登录次数
	 */
	public Integer getMonthLoginTotal() {
		return monthLoginTotal;
	}
	/**
	 * 获取：月登录次数
	 */
	public void setMonthLoginTotal(Integer monthLoginTotal) {
		this.monthLoginTotal = monthLoginTotal;
	}
	/**
	 * 设置：数据入库时间
	 */
	public Date getCreatetime() {
		return createtime;
	}
	/**
	 * 获取：数据入库时间
	 */
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	/**
	 * 设置：修改时间
	 */
	public Date getUpdatetime() {
		return updatetime;
	}
	/**
	 * 获取：修改时间
	 */
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

}
