package may.yuntian.external.datainterface.pojo.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目基本信息
 *
 * @author cwt
 * @Create 2023-4-12 16:29:00
 */
@Data
public class ProBasicInfoVO implements Serializable {

    private static final long serialVersionUID = 42L;

    /**
     * 项目id  al_project.id
     */
    private Long projectId;

    /**
     * 项目编号  al_project.identifier
     */
    private String code;

    /**
     * 检测类型  (10：定期检测；20：监督检测；30：评价检测；31：评价现状；32：控制效果评价；33：预评价。)
     * 检评项目 如果项目类型为:检评 检测类型 as:10 否则 as:20
     * 评价项目 如果项目类型为:预评 检测类型 as:33 如果项目类型为: 控评 检测类型 as:32 如果项目类型为:现状 检测类型 as:31 如果项目类型为: ?
     * al_project.type
     */
    private String checkType;

    /**
     * 项目负责人  al_project.charge
     */
    private String projectDirectorName;

    /**
     * 报告出具日期  yyyy-MM-dd  al_project_date.report_cover_date
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date reportDate;

    /**
     * 报告签发人  ?
     */
    private String issuer;

    /**
     * 填表人  ?
     */
    private String preparer;

    /**
     * 现场调查开始日期  yyyy-MM-dd  ?
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginSurveyDate;

    /**
     * 现场调查结束日期  yyyy-MM-dd  ?
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endSurveyDate;

    /**
     * 现场采样、测量开始日期  yyyy-MM-dd  al_project_date.start_date
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginSamplingDate;

    /**
     * 现场采样、测量结束日期  yyyy-MM-dd  al_project_date.end_date
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endSamplingDate;

    /**
     * 委托机构名称  al_project.entrust_company
     */
    private String entrustOrgName;

    /**
     * 委托机构统一社会信用代码  18位定长  ? 没有
     */
    private String entrustCreditCode;

    /**
     * 委托时间  al_project_date.entrust_date
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date entrustDate;

    /**
     * 受检单位名称  ?  al_project.company
     */
    private String empName;


    /**
     * 受检单位统一社会信用代码  18位定长
     */
    private String empCreditCode;

    /**
     * 经济类型
     */
    private String economicType;

    /**
     * 经济类型编码  参考-数据字典1
     */
    private String economicTypeCode;

    /**
     * 行业类别
     */
    private String industryCategory;

    /**
     * 行业类别编码  4位数字字符，定长；多个行业只给一种！ 参考国家标准：2017年国民经济行业分类与代码(GB/T 4754-2017)
     */
    private String industryCategoryCode;

    /**
     * 企业规模编码  1：大；2：中；3：小；4：微型；5：不详
     */
    private String scaleCode;

    /**
     * 注册地区名称  pro_area_code.full_name（必须：地区编码表全称！）
     */
    private String areaName;

    /**
     * 注册地区编码  与上述对应
     */
    private String areaCode;

    /**
     * 注册地址
     */
    private String regAddress;

    /**
     * 在职职工总人数
     */
    private String employeesTotalNum;

    /**
     * 接触危害因素员工总人数
     */
    private String contactHazardNum;

    /**
     * 技术服务地区编码  9位数字字符，定长。pro_area_code
     */
    private String serviceAreaCode;

    /**
     * 技术服务地址
     */
    private String serviceAddress;

    /**
     * 技术服务领域编码  1：采矿业；2：化工、石化及医药；3：冶金、建材；4：机械制造、电力、纺织、建筑和交通运输等行业领域；5：核设施；6：核技术工业应用
     */
    private String fieldCode;

    /**
     * 通讯地址  非必填
     */
    private String address;

    /**
     * 邮政编码  非必填
     */
    private String postalCode;

    /**
     * 法人姓名  非必填
     */
    private String legalPerson;

    /**
     * 法人联系电话  非必填
     */
    private String legalPhone;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话  格式为区号-号码或者手机号
     */
    private String contactPhone;

    /**
     * 技术服务地区
     */
    private String serviceArea;

    /**
     * al_project_user表数据回填字段
     **/
    private SysUserVo sysUserVo;
}
