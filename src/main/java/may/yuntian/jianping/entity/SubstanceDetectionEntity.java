package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 物质检测信息表（职卫）
 * 
 * @author LiXin
 * @email ''
 * @date 2023-06-28 15:02:08
 */
@TableName("al_substance_detection")
public class SubstanceDetectionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键
	 */
	@TableId
	private Long id;
	/**
	 * 物质 id
	 */
	private Long substanceId;
	/**
	 * 实验室检测能力 id
	 */
	private Long mainDataId;
	/**
	 * 资质（1：有, 2：无）
	 */
	private Integer qualification;
	/**
	 * 物质名称（物质表信息，此表可冗余）
	 */
	private String name;
	/**
	 * 原表中区分物质字段
	 */
	private String sampleTablename;
	/**
	 * 职业卫生检测标准  standard = standard_serial_num  +  detection_method_num
	 */
	private String standard;
	/**
	 * 职业卫生检测标准编号
	 */
	private String standardSerialNum;
	/**
	 * 通过认证的检测方法序号
	 */
	private String detectionMethodNum;
	/**
	 * 职业卫生检测标准名
	 */
	private String standardName;
	/**
	 * 检测方法（已更新）
	 */
	private String detectionMethod;
	/**
	 * 物质含有多种检测标准时的标识字段（默认值 1）
	 */
	private Integer markNum;
	/**
	 * 采样设备
	 */
	private String equipment;
	/**
	 * 采样流量（L/min）
	 */
	private String flow;
	/**
	 * 采样时间（min)
	 */
	private String testTime;
	/**
	 * 采样时间说明
	 */
	private String testTimeNote;
	/**
	 * 最低检出浓度
	 */
	private String minDetectable;
	/**
	 * 能否个体采样（0：否，1：是）
	 */
	private Integer indSample;
	/**
	 * 采样设备（个体）
	 */
	private String indEquipment;
	/**
	 * 采样流量（L/min)（个体）
	 */
	private String indFlow;
	/**
	 * 采样时间（min）（个体）
	 */
	private String indTestTime;
	/**
	 * 采样时间说明（个体）
	 */
	private String indTestTimeNote;
	/**
	 * 最低检出浓度（个体）
	 */
	private String indMinDetectable;
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
	 * 样品保存期限（天）
	 */
	private Integer shelfLife;
	/**
	 * 对应实验室数据来源
	 */
	private String labSource;
	/**
	 * 数据入库时间
	 */
	private Date createtime;
	/**
	 * 创建人
	 */
	private String createBy;
	/**
	 * 更新人
	 */
	private String updateBy;
	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 设置：自增主键
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：自增主键
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：物质 id
	 */
	public void setSubstanceId(Long substanceId) {
		this.substanceId = substanceId;
	}
	/**
	 * 获取：物质 id
	 */
	public Long getSubstanceId() {
		return substanceId;
	}
	/**
	 * 设置：实验室检测能力 id
	 */
	public void setMainDataId(Long mainDataId) {
		this.mainDataId = mainDataId;
	}
	/**
	 * 获取：实验室检测能力 id
	 */
	public Long getMainDataId() {
		return mainDataId;
	}
	/**
	 * 设置：资质（1：有, 2：无）
	 */
	public void setQualification(Integer qualification) {
		this.qualification = qualification;
	}
	/**
	 * 获取：资质（1：有, 2：无）
	 */
	public Integer getQualification() {
		return qualification;
	}
	/**
	 * 设置：物质名称（物质表信息，此表可冗余）
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：物质名称（物质表信息，此表可冗余）
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：原表中区分物质字段
	 */
	public void setSampleTablename(String sampleTablename) {
		this.sampleTablename = sampleTablename;
	}
	/**
	 * 获取：原表中区分物质字段
	 */
	public String getSampleTablename() {
		return sampleTablename;
	}
	/**
	 * 设置：职业卫生检测标准  standard = standard_serial_num  +  detection_method_num
	 */
	public void setStandard(String standard) {
		this.standard = standard;
	}
	/**
	 * 获取：职业卫生检测标准  standard = standard_serial_num  +  detection_method_num
	 */
	public String getStandard() {
		return standard;
	}
	/**
	 * 设置：职业卫生检测标准编号
	 */
	public void setStandardSerialNum(String standardSerialNum) {
		this.standardSerialNum = standardSerialNum;
	}
	/**
	 * 获取：职业卫生检测标准编号
	 */
	public String getStandardSerialNum() {
		return standardSerialNum;
	}
	/**
	 * 设置：通过认证的检测方法序号
	 */
	public void setDetectionMethodNum(String detectionMethodNum) {
		this.detectionMethodNum = detectionMethodNum;
	}
	/**
	 * 获取：通过认证的检测方法序号
	 */
	public String getDetectionMethodNum() {
		return detectionMethodNum;
	}
	/**
	 * 设置：职业卫生检测标准名
	 */
	public void setStandardName(String standardName) {
		this.standardName = standardName;
	}
	/**
	 * 获取：职业卫生检测标准名
	 */
	public String getStandardName() {
		return standardName;
	}
	/**
	 * 设置：检测方法（已更新）
	 */
	public void setDetectionMethod(String detectionMethod) {
		this.detectionMethod = detectionMethod;
	}
	/**
	 * 获取：检测方法（已更新）
	 */
	public String getDetectionMethod() {
		return detectionMethod;
	}
	/**
	 * 设置：物质含有多种检测标准时的标识字段（默认值 1）
	 */
	public void setMarkNum(Integer markNum) {
		this.markNum = markNum;
	}
	/**
	 * 获取：物质含有多种检测标准时的标识字段（默认值 1）
	 */
	public Integer getMarkNum() {
		return markNum;
	}
	/**
	 * 设置：采样设备
	 */
	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}
	/**
	 * 获取：采样设备
	 */
	public String getEquipment() {
		return equipment;
	}
	/**
	 * 设置：采样流量（L/min）
	 */
	public void setFlow(String flow) {
		this.flow = flow;
	}
	/**
	 * 获取：采样流量（L/min）
	 */
	public String getFlow() {
		return flow;
	}
	/**
	 * 设置：采样时间（min)
	 */
	public void setTestTime(String testTime) {
		this.testTime = testTime;
	}
	/**
	 * 获取：采样时间（min)
	 */
	public String getTestTime() {
		return testTime;
	}
	/**
	 * 设置：采样时间说明
	 */
	public void setTestTimeNote(String testTimeNote) {
		this.testTimeNote = testTimeNote;
	}
	/**
	 * 获取：采样时间说明
	 */
	public String getTestTimeNote() {
		return testTimeNote;
	}
	/**
	 * 设置：最低检出浓度
	 */
	public void setMinDetectable(String minDetectable) {
		this.minDetectable = minDetectable;
	}
	/**
	 * 获取：最低检出浓度
	 */
	public String getMinDetectable() {
		return minDetectable;
	}
	/**
	 * 设置：能否个体采样（0：否，1：是）
	 */
	public void setIndSample(Integer indSample) {
		this.indSample = indSample;
	}
	/**
	 * 获取：能否个体采样（0：否，1：是）
	 */
	public Integer getIndSample() {
		return indSample;
	}
	/**
	 * 设置：采样设备（个体）
	 */
	public void setIndEquipment(String indEquipment) {
		this.indEquipment = indEquipment;
	}
	/**
	 * 获取：采样设备（个体）
	 */
	public String getIndEquipment() {
		return indEquipment;
	}
	/**
	 * 设置：采样流量（L/min)（个体）
	 */
	public void setIndFlow(String indFlow) {
		this.indFlow = indFlow;
	}
	/**
	 * 获取：采样流量（L/min)（个体）
	 */
	public String getIndFlow() {
		return indFlow;
	}
	/**
	 * 设置：采样时间（min）（个体）
	 */
	public void setIndTestTime(String indTestTime) {
		this.indTestTime = indTestTime;
	}
	/**
	 * 获取：采样时间（min）（个体）
	 */
	public String getIndTestTime() {
		return indTestTime;
	}
	/**
	 * 设置：采样时间说明（个体）
	 */
	public void setIndTestTimeNote(String indTestTimeNote) {
		this.indTestTimeNote = indTestTimeNote;
	}
	/**
	 * 获取：采样时间说明（个体）
	 */
	public String getIndTestTimeNote() {
		return indTestTimeNote;
	}
	/**
	 * 设置：最低检出浓度（个体）
	 */
	public void setIndMinDetectable(String indMinDetectable) {
		this.indMinDetectable = indMinDetectable;
	}
	/**
	 * 获取：最低检出浓度（个体）
	 */
	public String getIndMinDetectable() {
		return indMinDetectable;
	}
	/**
	 * 设置：收集器
	 */
	public void setCollector(String collector) {
		this.collector = collector;
	}
	/**
	 * 获取：收集器
	 */
	public String getCollector() {
		return collector;
	}
	/**
	 * 设置：保存/运输方式
	 */
	public void setPreserveTraffic(String preserveTraffic) {
		this.preserveTraffic = preserveTraffic;
	}
	/**
	 * 获取：保存/运输方式
	 */
	public String getPreserveTraffic() {
		return preserveTraffic;
	}
	/**
	 * 设置：样品保存要求
	 */
	public void setPreserveRequire(String preserveRequire) {
		this.preserveRequire = preserveRequire;
	}
	/**
	 * 获取：样品保存要求
	 */
	public String getPreserveRequire() {
		return preserveRequire;
	}
	/**
	 * 设置：样品保存期限（天）
	 */
	public void setShelfLife(Integer shelfLife) {
		this.shelfLife = shelfLife;
	}
	/**
	 * 获取：样品保存期限（天）
	 */
	public Integer getShelfLife() {
		return shelfLife;
	}
	/**
	 * 设置：对应实验室数据来源
	 */
	public void setLabSource(String labSource) {
		this.labSource = labSource;
	}
	/**
	 * 获取：对应实验室数据来源
	 */
	public String getLabSource() {
		return labSource;
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
	 * 设置：创建人
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取：创建人
	 */
	public String getCreateBy() {
		return createBy;
	}
	/**
	 * 设置：更新人
	 */
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	/**
	 * 获取：更新人
	 */
	public String getUpdateBy() {
		return updateBy;
	}
	/**
	 * 设置：更新时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
}
