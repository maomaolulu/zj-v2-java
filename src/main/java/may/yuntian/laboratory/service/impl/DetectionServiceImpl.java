package may.yuntian.laboratory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.service.ProjectService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;
import may.yuntian.jianping.entity.CompanySurveyEntity;
import may.yuntian.jianping.service.CompanySurveyService;
import may.yuntian.laboratory.entity.DetectionEntity;
import may.yuntian.laboratory.mapper.DetectionMapper;
import may.yuntian.laboratory.service.DetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 检测情况
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @date 2020-12-17
 */
@Service("detectionService")
public class DetectionServiceImpl extends ServiceImpl<DetectionMapper, DetectionEntity> implements DetectionService {

	@Autowired
	private ProjectService projectService;
	@Autowired
	private CompanySurveyService companySurveyService;
	
	public PageUtils queryPage(Map<String, Object> params) {
		IPage<DetectionEntity> page = this.page(
				new Query<DetectionEntity>().getPage(params),
				new QueryWrapper<DetectionEntity>()
				);
				
		return new PageUtils(page);
		
	}
	
	/**
  	 * 根据项目ID获取检测情况
    * @param projectId 项目ID
    * @return
    */
   public DetectionEntity getByProjectId(Long projectId) {
   	
   	DetectionEntity detectionEntity = baseMapper.selectOne(new QueryWrapper<DetectionEntity>().eq("project_id", projectId));
//   	DetectionEntity detectionEntity = new DetectionEntity();
   	
   	ProjectEntity project = projectService.getById(projectId);
   	CompanySurveyEntity companySurvey = companySurveyService.getOne(projectId);
   	
   	if(detectionEntity != null) {
   		detectionEntity.setCompany(project.getCompany());
   		detectionEntity.setOfficeAddress(project.getOfficeAddress());
   		detectionEntity.setEntrustCompany(project.getEntrustCompany());
   		detectionEntity.setEntrustAddress(project.getEntrustOfficeAddress());
   		detectionEntity.setDetectionType(companySurvey.getDetectionType());
   		detectionEntity.setTestNature(companySurvey.getTestNature());
   	}else {
   		detectionEntity = new DetectionEntity();
		detectionEntity.setReceiptDate("");
		detectionEntity.setTestDate("");
		detectionEntity.setReportDate(null);
		detectionEntity.setAuditName("赵鑫");
		detectionEntity.setPosition("总经理");
		detectionEntity.setSampleCharacter("");
		detectionEntity.setSampleDate("");
		detectionEntity.setSampleName("工作场所空气");
		detectionEntity.setSampleNum(0);
		detectionEntity.setCompany(project.getCompany());
   		detectionEntity.setOfficeAddress(project.getOfficeAddress());
   		detectionEntity.setEntrustCompany(project.getEntrustCompany());
   		detectionEntity.setEntrustAddress(project.getEntrustOfficeAddress());
   		detectionEntity.setDetectionType(companySurvey.getDetectionType());
   		detectionEntity.setTestNature(companySurvey.getTestNature());
	}
   	return detectionEntity;
   	
   }
   
   /**
    * 重构新增或修改接口
    * @param detectionEntity
    */
   public void saveOrUpdateByprojectId(DetectionEntity detectionEntity) {
	   Long projectId = detectionEntity.getProjectId();
	   DetectionEntity detection = baseMapper.selectOne(new QueryWrapper<DetectionEntity>()
			   .eq("project_id", projectId)
			   );
	   if(detection!=null) {
		   detectionEntity.setId(detection.getId());
		   this.updateById(detectionEntity);
	   }else {
		   detectionEntity.setId(null);
		   this.save(detectionEntity);
	   }
   }
	
}
