package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("t_company_survey")
@Data
public class CompanySurveyEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 单位名称
     */
    private String company;
    /**
     * 合同编号
     */
    private String identifier;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 单位地址
     */
    private String officeAddress;
    /**
     * 受检单位名称
     */
    private String entrustCompany;
    /**
     * 受检单位地址
     */
    private String entrustAddress;
    /**
     * 注册地址
     */
    private String registeredAddress;
    /**
     * 联系人
     */
    private String contact;
    /**
     * 联系方式
     */
    private String telephone;
    /**
     * 所属行业
     */
    private String industryCategory;
    /**
     * 行业类别编码
     */
    private String industryCategoryCode;
    /**
     * 职业病危害风险分类(0一般、1严重)
     */
    private Integer riskLevel;
    /**
     * 检测类型(评价/定期/其它)
     */
    private String detectionType;
    /**
     * 劳动定员(人数)/职工总数
     */
    private Integer laborQuota;
    /**
     * 有无卫生管理部门(0:无, 1:有)
     */
    private Integer healthSector;
    /**
     * 部门名称
     */
    private String department;
    /**
     * 人数/委托单位职业卫生管理部门人数
     */
    private Integer population;
    /**
     * (受检单位)职业病危害接触人数
     */
    private Integer hazardNum;
    /**
     * 职业健康检查机构名称
     */
    private String inspectionOrg;
    /**
     * 最近一次职业健康检查时间
     */
    private Date lastTestTime;
    /**
     * 产品
     */
    private String product;
    /**
     * 产量
     */
    private String yield;
    /**
     * 主要产品及年产量
     */
    private String productsYield;
    /**
     * 调查陪同人
     */
    private String accompany;
    /**
     * 调查日期(年月日)
     */
    private Date surveyDate;
    /**
     * 检测性质
     */
    private String testNature;
    /**
     * 检测与评价场所
     */
    private String testPlace;
    /**
     * 警示标识标语告知卡是否完整（0不完整，1完整）
     */
    private Integer warningSignCard;
    /**
     * 采样时间
     */
    private String testDate;
    /**
     * 主要检测项目
     */
    private String testItems;
    /**
     * 采样时段
     */
    private String timeFrame;
    /**
     * 报告封面日期
     */
    private Date reportCoverTime;
    /**
     * 数据入库时间
     */
    private Date createtime;
    /**
     * 修改时间
     */
    private Date updatetime;
    /**
     * 统一社会信用代码
     */
    private String unifiedCode;
    /**
     * 企业经济类型
     */
    private String economy;
    /**
     * 0：未公示，1：已公示
     */
    private Integer publicityStatus;
    /**
     * 技术服务项目组人员
     */
    private String technicalPersons;
    /**
     * 项目采样陪同人
     */
    private String samplingCompany;
    /**
     * 项目采样时间
     */
    private String samplingDate;
    /**
     * 公示项目pdf路径
     */
    private String publicityPath;
    /**
     * 实验室人员（用于项目公示）
     */
    private String laboratoryPerson;
    /**
     * 该项目最大的点位编号数
     */
    private Integer maxPointCode;
}
