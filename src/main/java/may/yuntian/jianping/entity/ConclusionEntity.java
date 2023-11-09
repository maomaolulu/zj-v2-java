package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 结论
 * 
 * @author LiXin
 * @email ''
 * @date 2022-11-15 08:54:28
 */
@TableName("zj_conclusion")
public class ConclusionEntity implements Serializable {
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
	 * 检测结果id(mongo表)
	 */
	private String resultId;
	/**
	 * 采样记录id(mongo表)
	 */
	private String recordId;
	/**
	 * 检测点ID(mongo表)
	 */
	private String pointId;
	/**
	 * 岗位ID(mongo表)
	 */
	private String postId;
	/**
	 * 采样地点
	 */
	private String testPlace;
	/**
	 * 车间
	 */
	private String workshop;
	/**
	 * 岗位
	 */
	private String post;
	/**
	 * 工种ID
	 */
	private String pfnId;
	/**
	 * 工种
	 */
	private String pfn;
	/**
	 * 检测点编号数值
	 */
	private Integer pointCodeNum;
	/**
	 * 检测点编号
	 */
	private String pointCode;
	/**
	 * 检测物质ID
	 */
	private Long substanceId;
	/**
	 * 检测物质
	 */
	private String substance;
	/**
	 * 粉尘对应总尘的ID
	 */
	private Integer totalDustId;
	/**
	 * 1:毒物，2.粉尘, 3.噪声, 4.高温(同al_substance表)
	 */
	private Integer sType;
	/**
	 * 检测物质全称
	 */
	private String testItem;
	/**
	 * 作业人数
	 */
	private Integer workerNum;
	/**
	 * 检测结果 (结果值)
	 */
	private String testResult;
	/**
	 * 限值
	 */
	private String limitV;
	/**
	 * 检测结果 (符合/不符合)
	 */
	private String result;
	/**
	 * 补救措施
	 */
	private String measures;
	/**
	 * 评价结论
	 */
	private String conclusion;
	/**
	 * 是否存在高毒物品（2不存在，1存在）
	 */
	private Integer highlyToxic;
	/**
	 * 数据入库时间
	 */
	private Date createtime;
	/**
	 * 修改时间
	 */
	private Date updatetime;

	/**
	 * 设置：自增主键ID
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：自增主键ID
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：项目ID
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	/**
	 * 获取：项目ID
	 */
	public Long getProjectId() {
		return projectId;
	}
	/**
	 * 设置：检测结果id(mongo表)
	 */
	public void setResultId(String resultId) {
		this.resultId = resultId;
	}
	/**
	 * 获取：检测结果id(mongo表)
	 */
	public String getResultId() {
		return resultId;
	}
	/**
	 * 设置：采样记录id(mongo表)
	 */
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	/**
	 * 获取：采样记录id(mongo表)
	 */
	public String getRecordId() {
		return recordId;
	}
	/**
	 * 设置：检测点ID(mongo表)
	 */
	public void setPointId(String pointId) {
		this.pointId = pointId;
	}
	/**
	 * 获取：检测点ID(mongo表)
	 */
	public String getPointId() {
		return pointId;
	}
	/**
	 * 设置：岗位ID(mongo表)
	 */
	public void setPostId(String postId) {
		this.postId = postId;
	}
	/**
	 * 获取：岗位ID(mongo表)
	 */
	public String getPostId() {
		return postId;
	}
	/**
	 * 设置：采样地点
	 */
	public void setTestPlace(String testPlace) {
		this.testPlace = testPlace;
	}
	/**
	 * 获取：采样地点
	 */
	public String getTestPlace() {
		return testPlace;
	}
	/**
	 * 设置：车间
	 */
	public void setWorkshop(String workshop) {
		this.workshop = workshop;
	}
	/**
	 * 获取：车间
	 */
	public String getWorkshop() {
		return workshop;
	}
	/**
	 * 设置：岗位
	 */
	public void setPost(String post) {
		this.post = post;
	}
	/**
	 * 获取：岗位
	 */
	public String getPost() {
		return post;
	}
	/**
	 * 设置：工种ID
	 */
	public void setPfnId(String pfnId) {
		this.pfnId = pfnId;
	}
	/**
	 * 获取：工种ID
	 */
	public String getPfnId() {
		return pfnId;
	}
	/**
	 * 设置：工种
	 */
	public void setPfn(String pfn) {
		this.pfn = pfn;
	}
	/**
	 * 获取：工种
	 */
	public String getPfn() {
		return pfn;
	}
	/**
	 * 设置：检测点编号数值
	 */
	public void setPointCodeNum(Integer pointCodeNum) {
		this.pointCodeNum = pointCodeNum;
	}
	/**
	 * 获取：检测点编号数值
	 */
	public Integer getPointCodeNum() {
		return pointCodeNum;
	}
	/**
	 * 设置：检测点编号
	 */
	public void setPointCode(String pointCode) {
		this.pointCode = pointCode;
	}
	/**
	 * 获取：检测点编号
	 */
	public String getPointCode() {
		return pointCode;
	}
	/**
	 * 设置：检测物质ID
	 */
	public void setSubstanceId(Long substanceId) {
		this.substanceId = substanceId;
	}
	/**
	 * 获取：检测物质ID
	 */
	public Long getSubstanceId() {
		return substanceId;
	}
	/**
	 * 设置：检测物质
	 */
	public void setSubstance(String substance) {
		this.substance = substance;
	}
	/**
	 * 获取：检测物质
	 */
	public String getSubstance() {
		return substance;
	}
	/**
	 * 设置：粉尘对应总尘的ID
	 */
	public void setTotalDustId(Integer totalDustId) {
		this.totalDustId = totalDustId;
	}
	/**
	 * 获取：粉尘对应总尘的ID
	 */
	public Integer getTotalDustId() {
		return totalDustId;
	}
	/**
	 * 设置：1:毒物，2.粉尘, 3.噪声, 4.高温(同al_substance表)
	 */
	public void setSType(Integer sType) {
		this.sType = sType;
	}
	/**
	 * 获取：1:毒物，2.粉尘, 3.噪声, 4.高温(同al_substance表)
	 */
	public Integer getSType() {
		return sType;
	}
	/**
	 * 设置：检测物质全称
	 */
	public void setTestItem(String testItem) {
		this.testItem = testItem;
	}
	/**
	 * 获取：检测物质全称
	 */
	public String getTestItem() {
		return testItem;
	}
	/**
	 * 设置：作业人数
	 */
	public void setWorkerNum(Integer workerNum) {
		this.workerNum = workerNum;
	}
	/**
	 * 获取：作业人数
	 */
	public Integer getWorkerNum() {
		return workerNum;
	}
	/**
	 * 设置：检测结果 (结果值)
	 */
	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}
	/**
	 * 获取：检测结果 (结果值)
	 */
	public String getTestResult() {
		return testResult;
	}
	/**
	 * 设置：限值
	 */
	public void setLimitV(String limitV) {
		this.limitV = limitV;
	}
	/**
	 * 获取：限值
	 */
	public String getLimitV() {
		return limitV;
	}
	/**
	 * 设置：检测结果 (符合/不符合)
	 */
	public void setResult(String result) {
		this.result = result;
	}
	/**
	 * 获取：检测结果 (符合/不符合)
	 */
	public String getResult() {
		return result;
	}
	/**
	 * 设置：补救措施
	 */
	public void setMeasures(String measures) {
		this.measures = measures;
	}
	/**
	 * 获取：补救措施
	 */
	public String getMeasures() {
		return measures;
	}
	/**
	 * 设置：评价结论
	 */
	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}
	/**
	 * 获取：评价结论
	 */
	public String getConclusion() {
		return conclusion;
	}
	/**
	 * 设置：是否存在高毒物品（2不存在，1存在）
	 */
	public void setHighlyToxic(Integer highlyToxic) {
		this.highlyToxic = highlyToxic;
	}
	/**
	 * 获取：是否存在高毒物品（2不存在，1存在）
	 */
	public Integer getHighlyToxic() {
		return highlyToxic;
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
	 * 设置：修改时间
	 */
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getUpdatetime() {
		return updatetime;
	}
}
