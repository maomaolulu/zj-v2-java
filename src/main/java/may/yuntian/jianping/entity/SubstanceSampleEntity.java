package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 检测法规依据数据
 * 
 * @author LiXin
 * @email ''
 * @date 2022-03-07 09:39:33
 */
@Data
@TableName("al_substance_sample")
public class SubstanceSampleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 * 检测物质ID
	 */
	private Long substanceId;
	/**
	 * 采样方式(0:无,1定点,2个人)
	 */
	private Integer sampleMode;
	/**
	 * 此采样数据记录在哪张表中
	 */
	private String sampleTablename;
	/**
	 * 采样及检测依据
	 */
	private String basis;
	/**
	 * 检测依据的名称
	 */
	private String basisName;
	/**
	 * 旧的采样及检测依据
	 */
	private String basisOld;
	/**
	 * 旧的标准名称
	 */
	private String oldBasisName;
	/**
	 * 检测依据启用日期
	 */
	private Date enableDate;
	/**
	 * 检测依据失效日期
	 */
	private Date invalidDate;
	/**
	 * 采样设备
	 */
	private String equipment;
	/**
	 * 采样流量(L/min)
	 */
	private BigDecimal flow;
	/**
	 * 采样时间(min)
	 */
	private String testTime;
	/**
	 * 收集器
	 */
	private String collector;
	/**
	 * 保存/运输方式
	 */
	private String preserveTraffic;
	/**
	 * 样品保存要求
	 */
	private String preserveRequire;
	/**
	 * 样品保存期限(天)
	 */
	private Integer shelfLife;
	/**
	 * 空白样要求
	 */
	private String blankSample;
	/**
	 * 空白样数量
	 */
	private Integer blankNum;
	/**
	 * 是否通过计量认证(空,A,D,B(18.7))
	 */
	private String authentication;
	/**
	 * 通过认证的检测方法序号
	 */
	private String basisNum;
	/**
	 * 备注
	 */
	private String remarks;

    /** 是否默认 */
    private Integer isDefault;

    /** 仅作识别不检测 */
    private Integer discernTest;

    /** 采样时长说明 */
    private String testTimeNote;

    /** lab_main_data表id */
    private Long mainDataId;

	/**
	 * 采样数量
	 */
	private Integer sampleNumber;
	/**
	 * 数据入库时间
	 */
	private Date createtime;
	/**
	 * 修改时间
	 */
	private Date updatetime;

}
