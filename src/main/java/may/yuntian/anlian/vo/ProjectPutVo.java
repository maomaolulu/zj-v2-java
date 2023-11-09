package may.yuntian.anlian.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import may.yuntian.jianping.entity.ProjectUserEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 项目表(包含了原任务表的字段)
 * 
 * @author LiXin
 * @email
 * @date 2022-01-10 14:43:43
 */
@Data
public class ProjectPutVo implements Serializable {
	private static final long serialVersionUID = 1L;


	/**
	 * 项目编号
	 */
	private String identifier;
	/**
	 * 受检企业名称
	 */
	private String company;
	/**
	 * 受检详细地址
	 */
	private String officeAddress;
	/**
	 * 项目状态(1项目录入，2任务分配，5采样，10收样，20检测报告，30报告装订，40质控签发，50报告寄送，60项目归档，70项目结束，98任务挂起，99项目中止)
	 */
	private Integer status;
	/**
	 * 项目类型
	 */
	private String type;
	/**
	 * 负责人
	 */
	private String charge;
	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 * 委托类型
	 */
	private String entrustType;
	/**
	 * 委托单位名称
	 */
	private String entrustCompany;
	/**
	 * 委托单位详细地址
	 */
	private String entrustOfficeAddress;
	/**
	 * 项目隶属公司
	 */
	private String companyOrder;
	/**
	 * 杭州隶属(业务来源)
	 */
	private String businessSource;
	/**
	 * 业务员
	 */
	private String salesmen;
	/**
	 * 项目金额(元)
	 */
	private BigDecimal totalMoney;
	/**
	 * 项目净值(元)
	 */
	private BigDecimal netvalue;
	/**
	 * 备注
	 */
	private String remarks;

    /**
     * 任务下发日期
     */
    private Date taskReleaseDate;

    /**
     * 送样日期
     */
    private Date deliverDate;

    /**
     * 报告签发日期
     */
    private Date reportIssue;

}
