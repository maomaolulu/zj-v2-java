package may.yuntian.jianping.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 检测物质数据
 * 
 * @author LiXin
 * @email ''
 * @date 2022-03-07 09:39:33
 */
@Data
public class SubstanceVo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 * 总尘ID
	 */
	private Long totalDustId;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 英文名
	 */
	private String nameEn;
	/**
	 * 化学文摘号CAS No.
	 */
	private String casNo;
	/**
	 * 最高容许浓度(mg/ m³)
	 */
	private Float mac;
	/**
	 * 时间加权平均容许浓度(mg/ m³)
	 */
	private Float pcTwa;
	/**
	 * 短时间接触容许浓度(mg/ m³)
	 */
	private Float pcStel;
	/**
	 * 临界不良反应
	 */
	private String reaction;
	/**
	 * 是否折算(2否,1是)
	 */
	private Integer deduction;
	/**
	 * 备注
	 */
	private String remaks;
	/**
	 * 物质类型(1.毒物2.粉尘3.噪声4.高温5.紫外辐射6.手传震动7工频电场8.高频电磁场)
	 */
	private Integer sType;
	/**
	 * 计算方式(1.mac2.pc/twa3.pc/twa+pc/stel)
	 */
	private Integer computeMode;
	/**
	 * 采样物质所属部门(1.职卫2.环境公卫3.辐射)
	 */
	private Integer sDept;
	/**
	 * 是否高毒(1毒物非高毒2.毒物高毒)
	 */
	private Integer highlyToxic;
    /**
     * 是否根据游离二氧化硅判定
     */
    private Integer isSilica;
    /**
     * 检测项目对照ID
     */
    private Long indicatorId;

    /**
     *标识(选择物质时区分同物质的参数)
     */
    private String mark;

    /**
     *是否可以测个体(1可以 2不可以)
     */
    private Integer hasSolo;

    /**
     * 所有物质合并之后的名称  例如五苯两酯   乙酸之类(不是五苯两酯的那两个酯类)
     */
    private String totalMergeName;

	/**
	 * 数据入库时间
	 */
	private Date createtime;
	/**
	 * 修改时间
	 */
	private Date updatetime;

    /**
     * 是否通过计量认证(空,A,D,B(18.7))
     */
	private String authentication;

}
