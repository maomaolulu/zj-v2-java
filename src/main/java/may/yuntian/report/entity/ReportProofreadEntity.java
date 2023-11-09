package may.yuntian.report.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import may.yuntian.anlian.vo.ReportSelectDictVo;
import may.yuntian.anlian.vo.SysDictVo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 报告技术校核记录表
 *
 * @author LinXin
 * @date 2022-04-14
 */

@Data
@TableName("zj_report_proofread")
public class ReportProofreadEntity implements Serializable {
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
	 * 负责人ID
	 */
	private Long chargerId;
	/**
	 * 负责人
	 */
	private String charger;
	/**
	 * 编写人ID
	 */
	private Long writerId;
	/**
	 * 编写人
	 */
	private String writer;
	/**
	 * 其它审核内容
	 */
	private String otherContent;
	/**
	 * 问题描述
	 */
	private String problem;
	/**
	 * 校核人ID
	 */
	private Long checkerId;
	/**
	 * 校核人
	 */
	private String checker;
	/**
	 * 校核时间
	 */
	private Date checkDate;
	/**
	 * 修改情况
	 */
	private String modification;
	/**
	 * 修改人ID
	 */
	private Long reviserId;
	/**
	 * 修改人
	 */
	private String reviser;
	/**
	 * 修改人签字日期
	 */
	private Date reviseDate;
	/**
	 * 审核意见
	 */
	private String opinion;
	/**
	 * 质控负责人ID
	 */
	private Long directorId;
	/**
	 * 质控负责人
	 */
	private String director;
	/**
	 * 质控签字日期
	 */
	private Date overTime;
	/**
	 * 签发意见
	 */
	private String suggestion;
	/**
	 * 签发人ID
	 */
	private Long issuerId;
	/**
	 * 签发人
	 */
	private String issuer;
	/**
	 * 签发日期
	 */
	private Date issuerTime;
	/**
	 * 数据入库时间
	 */
	private Date createtime;
	/**
	 * 修改时间
	 */
	private Date updatetime;

    /**
     * 项目编号
     */
    private String identifier;
    /**
     * 受检企业名称
     */
    private String company;
    /**
     * 委托单位名称
     */
    private String entrustCompany;

    /** 文件路径 */
    private String path;
    /**
     * minio路径
     */
    private String minioPath;



    /**
	 * 数据字典
	 */
	@TableField(exist=false)
	private List<SysDictVo> sysDictList;
	/**
	 * 报告技术审核项对应关系,已选中的数据字典项
	 */
	@TableField(exist=false)
	private List<ReportSelectDictVo> selectDictList;
	
}
