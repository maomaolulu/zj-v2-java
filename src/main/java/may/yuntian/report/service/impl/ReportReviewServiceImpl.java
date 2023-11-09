package may.yuntian.report.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.service.ProjectService;
import may.yuntian.anlian.utils.DateUtils;
import may.yuntian.anlian.vo.ReportSelectDictVo;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;
import may.yuntian.jianping.entity.ProjectUserEntity;
import may.yuntian.jianping.service.ProjectUserService;
import may.yuntian.laboratory.entity.DetectionEntity;
import may.yuntian.laboratory.service.DetectionService;
import may.yuntian.modules.sys.entity.SysDictEntity;
import may.yuntian.modules.sys.service.SysDictService;
import may.yuntian.report.entity.ReportReviewDictEntity;
import may.yuntian.report.entity.ReportReviewEntity;
import may.yuntian.report.mapper.ReportReviewMapper;
import may.yuntian.report.service.ReportReviewDictService;
import may.yuntian.report.service.ReportReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 报告技术审核记录表
 * 业务逻辑层实现类
 *
 * @author LinXin
 * @date 2022-04-14
 */  
@Service("reportReviewService")
public class ReportReviewServiceImpl extends ServiceImpl<ReportReviewMapper, ReportReviewEntity> implements ReportReviewService {

	@Autowired
	private SysDictService sysDictService;//数据字典中存储的技术审核记录
    @Autowired
    private ProjectService projectService;
	@Autowired
	private ReportReviewDictService reportReviewDictService;
	@Autowired
    private DetectionService detectionService;
    @Autowired
    private ProjectUserService projectUserService;

	
	private static String TYPE_NAME = "reportReview";//参数类型
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
    	String projectId = (String)params.get("projectId");		//项目ID
        IPage<ReportReviewEntity> page = this.page(
                new Query<ReportReviewEntity>().getPage(params),
                new QueryWrapper<ReportReviewEntity>()
                .eq(StringUtils.isNotBlank(projectId),"project_id", projectId)
        );

        return new PageUtils(page);
    }
    
    /**
     * 将项目信息赋值到报告技术中作为初始化数据
     * @param projectId
     * @return
     */
    public boolean initialize(Long projectId){
    	if(notExistByProject(projectId)) {
            ProjectEntity projectEntity = projectService.getById(projectId);
            DetectionEntity detectionEntity = detectionService.getByProjectId(projectId);
            ProjectUserEntity projectUserEntity = projectUserService.getJiShu(projectId);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            if (detectionEntity.getReportDate()!=null){
                try {
                    String dateStr = DateUtils.plusDay(1,format.format(detectionEntity.getReportDate()));
                    date = format.parse(dateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            //将项目信息赋值到报告技术中作为初始化数据
			ReportReviewEntity entity = new ReportReviewEntity();
			entity.setProjectId(projectId);		//项目ID
            entity.setCharger(projectEntity.getCharge());
            entity.setChargerId(projectEntity.getChargeId());
            entity.setIdentifier(projectEntity.getIdentifier());
            entity.setCompany(projectEntity.getCompany());
            entity.setEntrustCompany(projectEntity.getEntrustCompany());
			entity.setReviewDate(date);
            if (projectUserEntity!=null){
                entity.setReviserId(projectUserEntity.getUserId());
                entity.setReviser(projectUserEntity.getUsername());
            }
            entity.setReviseDate(date);
            entity.setDirectorId(Long.valueOf(24));
            entity.setDirector("赵鑫");
            entity.setOverTime(date);

			//初始化默认勾选中12个选项
			//数据字典中存储的技术审核记录
	    	List<SysDictEntity> sysDictList = sysDictService.listByType(TYPE_NAME);
	    	List<ReportReviewDictEntity> selectDictEntityList = new ArrayList<>();
	    	
	    	ReportReviewDictEntity selectDictEntity;
	    	for(SysDictEntity sysDict : sysDictList) {
	    		selectDictEntity = new ReportReviewDictEntity();
	    		selectDictEntity.setProjectId(projectId);
	    		selectDictEntity.setType(TYPE_NAME);
	    		selectDictEntity.setSysDictCode(sysDict.getCode());
	    		selectDictEntity.setName(sysDict.getValue());
	    		selectDictEntity.setValue(1);//默认选是

	    		selectDictEntityList.add(selectDictEntity);
	    	}
	    	reportReviewDictService.saveBatch(selectDictEntityList);//批量保存初始化的对应关系值
	    	baseMapper.insert(entity);
			return true;
    	}else {
			System.err.println("将项目信息赋值到报告技术中作为初始化数据时，项目ID="+projectId+" 已经存在！");
			return false;
		}
    }
    
    /**
     * 修改报告技术审核信息
     * @param entity
     */
    public boolean saveOrUpdate(ReportReviewEntity entity) {
    	Long projectId = entity.getProjectId();
    	System.out.println("根据项目ID修改报告技术审核："+entity.getId()+" projectId="+entity.getProjectId());
    	//已经选中的技术审核记录
    	QueryWrapper<ReportReviewDictEntity> querySelectDictWrapper = new QueryWrapper<ReportReviewDictEntity>()
    			.eq("project_id", projectId)
    			.eq("type", TYPE_NAME);
    	reportReviewDictService.remove(querySelectDictWrapper);//删除旧的选中的数据
    	
    	Collection<ReportReviewDictEntity> entityList = new ArrayList<ReportReviewDictEntity>();
    	List<ReportSelectDictVo> selectDictList = entity.getSelectDictList();
    	ReportReviewDictEntity rrd ;
    	for(ReportSelectDictVo vo : selectDictList) {
    		rrd = new ReportReviewDictEntity();
    		rrd.setProjectId(projectId);
    		rrd.setSysDictCode(vo.getSysDictCode());
    		rrd.setValue(vo.getValue());
    		rrd.setType(TYPE_NAME);
    		rrd.setName(vo.getName());
    		entityList.add(rrd);
    	}
    	reportReviewDictService.saveBatch(entityList);
    	baseMapper.updateById(entity);
    	
    	return true;
    }


    /**
     * 新版修改
     * @param entity
     */
    public void updateInfo(ReportReviewEntity entity){
        entity.setReviseDate(entity.getReviewDate());
        entity.setOverTime(entity.getReviewDate());
        baseMapper.updateById(entity);
    }
    
    
    /**
     * 根据项目ID获取报告技术审核信息
     * @param projectId
     * @return
     */
    public ReportReviewEntity getByProjectId(Long projectId) {
        if (notExistByProject(projectId)){
            this.initialize(projectId);
        }
    	QueryWrapper<ReportReviewEntity> queryWrapper = new QueryWrapper<ReportReviewEntity>().eq("project_id", projectId);
    	ReportReviewEntity reportReview = baseMapper.selectOne(queryWrapper);
    	
    	//数据字典中存储的技术审核记录
//    	List<SysDictEntity> sysDictList = sysDictService.listByType(TYPE_NAME);
//    	List<SysDictVo> sysDictVoList = ObjectConversion.copy(sysDictList, SysDictVo.class);
//    	reportReview.setSysDictList(sysDictVoList);
    	//已经选中的技术审核记录
    	List<ReportSelectDictVo> selectDictList = reportReviewDictService.selectSysDictByProjectIdAndType(projectId,TYPE_NAME);
    	reportReview.setSelectDictList(selectDictList);

    	
    	return reportReview;
    }
    
    /**
     * 根据项目ID查询是否已经存在
     * @param projectId 项目ID
     * @return boolean
     */
    public Boolean notExistByProject(Long projectId) {
    	Integer count = baseMapper.selectCount(new QueryWrapper<ReportReviewEntity>().eq("project_id", projectId));
    	if(count>0)
    		return false;
    	else
    		return true;
    }
    
}
