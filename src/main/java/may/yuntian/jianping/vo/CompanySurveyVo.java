package may.yuntian.jianping.vo;

import lombok.Data;
import may.yuntian.jianping.entity.SampleImgEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 检评公示信息
 * @author zhanghao
 * @date 2022-04-14
 */
@Data
public class CompanySurveyVo implements Serializable {
	private static final long serialVersionUID = 1L;
//ap.id,cs.company,cs.contact,cs.office_address,cs.detection_type,apd.report_issue,cs.accompany,cs.survey_date
	/**
	 * 自增主键ID
	 */
	private Long id;
	/**
	 * projectId
	 */
	private Long projectId;
	/**
	 * 项目编号
	 */
	private String identifier;
	/**
	 * 单位名称
	 */
	private String company;
	/**
	 * 联系人
	 */
	private String contact;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 项目名称pj
	 */
	private String projectName;
	/**
	 * 技术负责人pj
	 */
	private String techCharge;
	/**
	 * 质控负责人pj
	 */
	private String qcCharge;
	/**
	 * 项目负责人pj
	 */
	private String pCharge;
	/**
	 * 评价报告编制人PJ
	 */
	private String pPreparePerson;

	/**
	 * 现场调查人员陪同人pj
	 */
	private String pSurveyCompanion;
	/**
	 * 现场调查人员时间pj
	 */
	private Date pSurveyDate;
	/**
	 * 现场采样、现场检测人员陪同人pj
	 */
	private String pSampleCompanion;
	/**
	 * 现场采样、现场检测人员时间pj
	 */
	private String pSampleDate;
	/**
	 * 现场调查人员pj
	 */
	private String fieldInvestigatorsPj;

	/**
	 * 现场检测。现场检测人员pj
	 */
	private String fieldSamplingPj;
	/**
	 * 报告封面日期
	 */
	private Date reportCoverDate;


	/**
	 * 单位地址
	 */
	private String officeAddress;
	/**
	 * 检测类型
	 */
	private String detectionType;
	/**
	 * 报告签发日期/评价报告提交时间PJ
	 */
	private Date reportIssue;
	/**
	 * 陪同人
	 */
	private String accompany;
	/**
	 * 陪同时间
	 */
	private Date surveyDate;
	/**
	 * 现场调查人员
	 */
	private String fieldInvestigators;
	
	/**
	 * 现场检测。现场检测人员
	 */
	private String fieldSampling;

	/**
	 * 现场检测图/pj
	 */
	private List<SampleImgEntity> imgs;
	/**
	 * 0：未公司，1:已公示
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
	 * 项目采样时间(项目采样陪同时间)
	 */
	private String samplingDate;
	/**
	 * 项目采样开始时间
	 */
	private Date reportStartDate;
	/**
	 * 项目采样结束时间
	 */
	private Date reportEndDate;

	/**
	 * 0：待申请 1：主管审核，2：质控审核 3：主管驳回 4：质控驳回 5：已公示
	 */
	private Integer applyPublicityStatus;
	/**
	 * 主管驳回
	 * @return
	 */
	private String directorReject;
	/**
	 * 质控驳回
	 * @return
	 */
	private String controlReject;
	/**
	 * 项目不公示状态（0：正常，1：不公示）
	 */
	private Integer hideStatus;
	/**
	 * 项目不公示原因
	 */
	private String hideRemark;

	/**
	 * pdf 参数
	 * @return
	 */
	private Map<String,Object> map;
	/**
	 * pdf 路径
	 * @return
	 */
	private String path;/**
	 * 项目负责人
	 * @return
	 */
	private String charge;
	/**
	 *0：项目公示最后操作时间
	 */
	private Date publicityLastTime;
	/**
	 * 实验室人员（用于项目公示）
	 */
	private String laboratoryPerson;
	/**
	 * 0:待申请，1：主管驳回
	 */
	private Integer declareStatus;
	/**
	 * 公示申请备注
	 */
	private String publicityRemark;
	/**
	 * 隶属公司搜索
	 */
	private String subjection;
    /**
     *修改时间
     */
    private Date updatetime;
    /**
     *入库时间
     */
    private Date addtime;

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public String getSubjection() {
		return subjection;
	}

	public void setSubjection(String subjection) {
		this.subjection = subjection;
	}

	public String getPublicityRemark() {
		return publicityRemark;
	}

	public void setPublicityRemark(String publicityRemark) {
		this.publicityRemark = publicityRemark;
	}

	public Integer getDeclareStatus() {
		return declareStatus;
	}

	public void setDeclareStatus(Integer declareStatus) {
		this.declareStatus = declareStatus;
	}

	public Integer getHideStatus() {
		return hideStatus;
	}

	public void setHideStatus(Integer hideStatus) {
		this.hideStatus = hideStatus;
	}

	public String getHideRemark() {
		return hideRemark;
	}

	public void setHideRemark(String hideRemark) {
		this.hideRemark = hideRemark;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getTechCharge() {
		return techCharge;
	}

	public void setTechCharge(String techCharge) {
		this.techCharge = techCharge;
	}

	public String getQcCharge() {
		return qcCharge;
	}

	public void setQcCharge(String qcCharge) {
		this.qcCharge = qcCharge;
	}

	public String getpCharge() {
		return pCharge;
	}

	public void setpCharge(String pCharge) {
		this.pCharge = pCharge;
	}

	public String getpPreparePerson() {
		return pPreparePerson;
	}

	public void setpPreparePerson(String pPreparePerson) {
		this.pPreparePerson = pPreparePerson;
	}

	public String getpSurveyCompanion() {
		return pSurveyCompanion;
	}

	public void setpSurveyCompanion(String pSurveyCompanion) {
		this.pSurveyCompanion = pSurveyCompanion;
	}

	public Date getpSurveyDate() {
		return pSurveyDate;
	}

	public void setpSurveyDate(Date pSurveyDate) {
		this.pSurveyDate = pSurveyDate;
	}

	public String getpSampleCompanion() {
		return pSampleCompanion;
	}

	public void setpSampleCompanion(String pSampleCompanion) {
		this.pSampleCompanion = pSampleCompanion;
	}

	public String getpSampleDate() {
		return pSampleDate;
	}

	public void setpSampleDate(String pSampleDate) {
		this.pSampleDate = pSampleDate;
	}

	public String getFieldInvestigatorsPj() {
		return fieldInvestigatorsPj;
	}

	public void setFieldInvestigatorsPj(String fieldInvestigatorsPj) {
		this.fieldInvestigatorsPj = fieldInvestigatorsPj;
	}

	public String getFieldSamplingPj() {
		return fieldSamplingPj;
	}

	public void setFieldSamplingPj(String fieldSamplingPj) {
		this.fieldSamplingPj = fieldSamplingPj;
	}

	public String getOfficeAddress() {
		return officeAddress;
	}

	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}

	public String getDetectionType() {
		return detectionType;
	}

	public void setDetectionType(String detectionType) {
		this.detectionType = detectionType;
	}

	public Date getReportIssue() {
		return reportIssue;
	}

	public void setReportIssue(Date reportIssue) {
		this.reportIssue = reportIssue;
	}

	public String getAccompany() {
		return accompany;
	}

	public void setAccompany(String accompany) {
		this.accompany = accompany;
	}

	public Date getSurveyDate() {
		return surveyDate;
	}

	public void setSurveyDate(Date surveyDate) {
		this.surveyDate = surveyDate;
	}

	public String getFieldInvestigators() {
		return fieldInvestigators;
	}

	public void setFieldInvestigators(String fieldInvestigators) {
		this.fieldInvestigators = fieldInvestigators;
	}

	public String getFieldSampling() {
		return fieldSampling;
	}

	public void setFieldSampling(String fieldSampling) {
		this.fieldSampling = fieldSampling;
	}

	public List<SampleImgEntity> getImgs() {
		return imgs;
	}

	public void setImgs(List<SampleImgEntity> imgs) {
		this.imgs = imgs;
	}

	public Integer getPublicityStatus() {
		return publicityStatus;
	}

	public void setPublicityStatus(Integer publicityStatus) {
		this.publicityStatus = publicityStatus;
	}

	public String getTechnicalPersons() {
		return technicalPersons;
	}

	public void setTechnicalPersons(String technicalPersons) {
		this.technicalPersons = technicalPersons;
	}

	public String getSamplingCompany() {
		return samplingCompany;
	}

	public void setSamplingCompany(String samplingCompany) {
		this.samplingCompany = samplingCompany;
	}

	public String getSamplingDate() {
		return samplingDate;
	}

	public void setSamplingDate(String samplingDate) {
		this.samplingDate = samplingDate;
	}

	public Date getReportStartDate() {
		return reportStartDate;
	}

	public void setReportStartDate(Date reportStartDate) {
		this.reportStartDate = reportStartDate;
	}

	public Date getReportEndDate() {
		return reportEndDate;
	}

	public void setReportEndDate(Date reportEndDate) {
		this.reportEndDate = reportEndDate;
	}

	public Integer getApplyPublicityStatus() {
		return applyPublicityStatus;
	}

	public void setApplyPublicityStatus(Integer applyPublicityStatus) {
		this.applyPublicityStatus = applyPublicityStatus;
	}

	public String getDirectorReject() {
		return directorReject;
	}

	public void setDirectorReject(String directorReject) {
		this.directorReject = directorReject;
	}

	public String getControlReject() {
		return controlReject;
	}

	public void setControlReject(String controlReject) {
		this.controlReject = controlReject;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public Date getPublicityLastTime() {
		return publicityLastTime;
	}

	public void setPublicityLastTime(Date publicityLastTime) {
		this.publicityLastTime = publicityLastTime;
	}

	public String getLaboratoryPerson() {
		return laboratoryPerson;
	}

	public void setLaboratoryPerson(String laboratoryPerson) {
		this.laboratoryPerson = laboratoryPerson;
	}
}
