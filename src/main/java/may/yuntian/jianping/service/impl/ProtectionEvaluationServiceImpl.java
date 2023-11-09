package may.yuntian.jianping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;
import may.yuntian.common.utils.SpringContextUtils;
import may.yuntian.jianping.entity.ProjectProtectionEntity;
import may.yuntian.jianping.entity.ProtectionEntity;
import may.yuntian.jianping.entity.ProtectionEvaluationEntity;
import may.yuntian.jianping.mapper.ProtectionEvaluationMapper;
import may.yuntian.jianping.mongoentity.PlanRecordEntity;
import may.yuntian.jianping.mongoentity.ResultEntity;
import may.yuntian.jianping.mongoservice.PlanRecordService;
import may.yuntian.jianping.mongoservice.PostPfnService;
import may.yuntian.jianping.mongoservice.ResultService;
import may.yuntian.jianping.service.ProjectProtectionService;
import may.yuntian.jianping.service.ProtectionEvaluationService;
import may.yuntian.jianping.service.ProtectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 个人防护用品有效性评价
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @date 2020-12-18
 */
@Service("protectionEvaluationService")
public class ProtectionEvaluationServiceImpl extends ServiceImpl<ProtectionEvaluationMapper, ProtectionEvaluationEntity> implements ProtectionEvaluationService {

    @Autowired
    private ProjectProtectionService projectProtectionService;
    @Autowired
    private ProtectionService protectionService;
    @Autowired
    private PostPfnService postPfnService;
    @Autowired
    private PlanRecordService planRecordService;
    @Autowired
    private ResultService resultService;


	public PageUtils queryPage(Map<String, Object> map) {
		
		QueryWrapper<ProtectionEvaluationEntity> queryWrapper = queryWrapperByParams(map);
    	
        IPage<ProtectionEvaluationEntity> page = this.page(new Query<ProtectionEvaluationEntity>().getPage(map),queryWrapper);
				
		return new PageUtils(page);
		
	}
	
	/**
  	 * 根据项目ID获取个人防护用品有效性评价
    * @param
    * @return
    */
   public List<ProtectionEvaluationEntity> listByProjectId(Map<String, Object> map) {
   	
	   List<ProtectionEvaluationEntity> list = this.list(queryWrapperByParams(map));
    	
	   	
   	return list;
   	
   }
   
   
   /**
    * 不分页查询条件
    * @param map
    * @return
    */
   public QueryWrapper<ProtectionEvaluationEntity> queryWrapperByParams(Map<String, Object> map){
	
	   String projectId = (String)map.get("projectId");
	   String protectEquip = (String)map.get("protectEquip");
	   String model = (String)map.get("model");
	   String params = (String)map.get("params");
	   String hazardFactors = (String)map.get("hazardFactors");
	   String evaluate = (String)map.get("evaluate");
	   
	   QueryWrapper<ProtectionEvaluationEntity> queryWrapper = new QueryWrapper<ProtectionEvaluationEntity>()
			   .eq(StringUtils.checkValNotNull(projectId),"project_id", projectId)
			   .like(StringUtils.isNotBlank(protectEquip),"protectEquip", protectEquip)
			   .like(StringUtils.isNotBlank(model),"model", model)
			   .like(StringUtils.isNotBlank(params),"params", params)
			   .like(StringUtils.isNotBlank(hazardFactors),"hazard_factors", hazardFactors)
			   .like(StringUtils.isNotBlank(evaluate),"evaluate", evaluate);
	   
	   
	   return queryWrapper;
	   
   }

    /**
     * 根据项目ID获取列表
     * @param projectId
     * @return
     */
   public List<ProtectionEvaluationEntity> getListByprojectId(Long projectId){
       List<ProtectionEvaluationEntity> protectionEvaluationEntityList = baseMapper.selectList(new QueryWrapper<ProtectionEvaluationEntity>()
               .eq("project_id",projectId)
       );
       return protectionEvaluationEntityList;
   }



    /**
     * 初始化个人防护用品
     * @param projectId
     */
    public void initializationProtectionEvaluation(Long projectId) {
        this.remove(new QueryWrapper<ProtectionEvaluationEntity>().eq("project_id",projectId));
        List<ProjectProtectionEntity> ProjectProtectionEntitys = projectProtectionService.list(new QueryWrapper<ProjectProtectionEntity>().eq("project_id", projectId));
        //个人防护id
        List<Long> collect = ProjectProtectionEntitys.stream().distinct().map(ProjectProtectionEntity::getProtectionId).collect(Collectors.toList());
        //个人防护
        if(collect!=null&&collect.size()>0){
            List<ProtectionEntity> protectionEntities = protectionService.listByIds(collect);
            ArrayList<ProtectionEvaluationEntity> protectionEvaluationEntities1 = new ArrayList<>();
            for (ProtectionEntity protectionEntity:protectionEntities) {
                List<String> pfnIdList = postPfnService.getPfnList(projectId, protectionEntity.getId());
                ProtectionEvaluationEntity protectionEvaluationEntity = new ProtectionEvaluationEntity();
                if (protectionEntity.getType().contains("防噪声耳塞")||protectionEntity.getType().contains("防噪声耳罩")) {
                    protectionEvaluationEntity.setProjectId(projectId);
                    protectionEvaluationEntity.setProtectEquip(protectionEntity.getType());
                    protectionEvaluationEntity.setModel(protectionEntity.getName());
                    protectionEvaluationEntity.setParams(protectionEntity.getIllustrate());
                    protectionEvaluationEntity.setHazardFactors("噪声");
                    //噪声有效性评价的计算

                    if (pfnIdList.size() > 0) {
//                        List<ResultEntity> resultEntityList = resultService.getListByProtection(pfnIdList);
                        List<PlanRecordEntity> planRecordEntities = planRecordService.getListByProtectionId(pfnIdList);

                        Integer nrr = protectionEntity.getNrr();
                        Float result = (Float.valueOf(nrr) - 7) / 2;
                        Float max = Float.valueOf(planRecordEntities.get(0).getBatchGatherLis().get(0).getGatherMap().get("0").getResult().getResult1());
                        for (int i = 0; i < planRecordEntities.size(); i++) {
                            if (new BigDecimal(max).compareTo(new BigDecimal(planRecordEntities.get(i).getBatchGatherLis().get(0).getGatherMap().get("0").getResult().getResult1())) == -1) {
                                max = Float.valueOf(planRecordEntities.get(i).getBatchGatherLis().get(0).getGatherMap().get("0").getResult().getResult1());
                            }
                            if (new BigDecimal(max).compareTo(new BigDecimal(planRecordEntities.get(i).getBatchGatherLis().get(0).getGatherMap().get("0").getResult().getResult2())) == -1) {
                                max = Float.valueOf(planRecordEntities.get(i).getBatchGatherLis().get(0).getGatherMap().get("0").getResult().getResult2());
                            }
                            if (new BigDecimal(max).compareTo(new BigDecimal(planRecordEntities.get(i).getBatchGatherLis().get(0).getGatherMap().get("0").getResult().getResult3())) == -1) {
                                max = Float.valueOf(planRecordEntities.get(i).getBatchGatherLis().get(0).getGatherMap().get("0").getResult().getResult3());
                            }
                        }
                        Float resultMax = max - result;
                        String fuhao;
                        String l;
                        if (resultMax < 85) {
                            l = "正确佩戴时防护有效";
                            fuhao = "<";
                        } else {
                            l = "正确佩戴时防护无效";
                            fuhao = ">";
                        }
                        protectionEvaluationEntity.setEvaluate(l + "（本次检测噪声最大值：" + max + "-" + result + "=" + resultMax + fuhao + "85dB（A））");
                    }
                    protectionEvaluationEntities1.add(protectionEvaluationEntity);
                }else if (protectionEntity.getType().contains("电焊面罩")){
                    protectionEvaluationEntity.setProjectId(projectId);
                    protectionEvaluationEntity.setProtectEquip(protectionEntity.getType());
                    protectionEvaluationEntity.setModel(protectionEntity.getName());
                    protectionEvaluationEntity.setParams(protectionEntity.getIllustrate());
                    protectionEvaluationEntity.setHazardFactors("电焊弧光");
                    protectionEvaluationEntity.setEvaluate("正确佩戴时防护有效");
                    protectionEvaluationEntities1.add(protectionEvaluationEntity);
                }else {
                    protectionEvaluationEntity.setProjectId(projectId);
                    protectionEvaluationEntity.setProtectEquip(protectionEntity.getType());
                    protectionEvaluationEntity.setModel(protectionEntity.getName());
                    protectionEvaluationEntity.setParams(protectionEntity.getIllustrate());

                    List<ResultEntity> resultEntityList = resultService.getListByProtection(pfnIdList);
                    List<String> testItemList = new ArrayList<>();
                    for (ResultEntity resultEntity:resultEntityList){
                        testItemList.add(resultEntity.getSubstance().getName());
                    }
                    String testItems = testItemList.stream().distinct().collect(Collectors.joining("、"));
                    protectionEvaluationEntity.setHazardFactors(testItems);
                    protectionEvaluationEntity.setEvaluate("正确佩戴时防护有效");
                    protectionEvaluationEntities1.add(protectionEvaluationEntity);
                }
            }
            this.saveBatch(protectionEvaluationEntities1);

        }

    }
   
	
}
