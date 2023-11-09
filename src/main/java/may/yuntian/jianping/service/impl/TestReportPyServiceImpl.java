package may.yuntian.jianping.service.impl;

import may.yuntian.jianping.entity.ConclusionPyEntity;
import may.yuntian.jianping.mapper.TestReportPyMapper;
import may.yuntian.jianping.service.TestReportPyService;
import may.yuntian.modules.sys_v2.utils.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检测报告模块-业务处理
 *
 * @author hjy
 * @description python代码迁移，保持原python风格
 * @date 2023/7/12 15:00
 */
@Service
public class TestReportPyServiceImpl implements TestReportPyService {

    @Autowired
    private TestReportPyMapper pyMapper;
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 结论列表
     *
     * @param projectId 项目id
     * @return 结论
     */
    @Override
    public List<ConclusionPyEntity> getConclusionList(Long projectId) {
        return pyMapper.getConclusionList(projectId);
    }

    /**
     * 批量修改 结论
     *
     * @param conclusions 结论信息集合
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateConclusions(List<ConclusionPyEntity> conclusions) {
        for (ConclusionPyEntity temp : conclusions) {
            pyMapper.updateConclusion(temp);
        }
    }

    /**
     * 结论信息删除
     *
     * @param projectId     项目id
     * @param conclusionIds 结论id集合
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteConclusions(Long projectId, List<Long> conclusionIds) {
        pyMapper.deleteConclusions(projectId, conclusionIds);
    }


    /**
     * 结果计算列表(业务逻辑参考python  详情见py代码   ZJ：def result_lis(self, params))
     *
     * @param projectId 项目id
     * @param sType     类型：1 ：化学；3  噪声；4 高温；
     * @return 计算列表
     */
    @Override
    @SuppressWarnings("all")
    public Map<String, Object> getResultList(Long projectId, Integer sType) {
        //返回值
        Map<String, Object> resultMap = new HashMap<>();
        //普通物质查询条件
        Query query = new Query(Criteria.where("project_id").is(projectId));
        if (sType < 3) {
            query.addCriteria(Criteria.where("substance.s_type").in(1, 2));
        } else {
            query.addCriteria(Criteria.where("substance.s_type").is(sType));
        }
        //普通物质
        List<Map> resultList = mongoTemplate.find(query, Map.class, "zj_result");
        //获取采样记录
        List<Map> planRecordList = mongoTemplate.find(new Query(Criteria.where("project_id").is(projectId)), Map.class, "zj_plan_record");
        Map<String, Map> recordMap = new HashMap<>();
        planRecordList.forEach(map -> {
            recordMap.put(String.valueOf(map.get("_id")), map);
        });
        //遍历并填充数据
        for (Map result : resultList) {
            //赋值id
            result.put("id", String.valueOf(result.get("_id")));
            //结果表样品批次
            List<Map<String, Object>> batchSampleLis = (List<Map<String, Object>>) result.get("batch_sample_lis");
            //采样记录  用于填充采样计划时间及结果
            Map planRecordTemp = recordMap.get(result.get("record_id"));
            Map<Integer, Map<String, Object>> gatherMap = new HashMap<>();
            //提起采样记录数据
            List<Map<String, Object>> batchGatherLis = (List<Map<String, Object>>) planRecordTemp.get("batch_gather_lis");
            for (Map<String, Object> mapTemp : batchGatherLis) {
                gatherMap.put(Convert.toInt(mapTemp.get("batch_num")), mapTemp);
            }
            //填充数据   采样时间   计算结果
            for (Map<String, Object> batchSample : batchSampleLis) {
                //获取对应采样记录
                Map<String, Object> batchGatherDto = gatherMap.get(batchSample.get("batch_num"));
                //填充采样时间
                batchSample.put("gather_date", batchGatherDto.get("gather_date"));
                //填充结果
                Map<String, Map<String, Object>> sourceMap = (Map<String, Map<String, Object>>) batchGatherDto.get("gather_map");
                //需要补充result的map集合
                Map<String, Map<String, Object>> tempMap = (Map<String, Map<String, Object>>) batchSample.get("gather_map");
                //遍历填充
                tempMap.forEach((key, val) -> {
                    val.put("result", sourceMap.get(key).get("result"));
                });
//                batchSample.put("gather_map", tempMap);
            }
//            result.put("batch_sample_lis", batchSampleLis);
        }
//        for (ResultEntity result : resultList) {
//            //结果表样品批次
//            List<BatchSampleDto> batchSampleLis = result.getBatchSampleLis();
//            //采样记录  用于填充采样计划时间及结果
//            PlanRecordEntity planRecordTemp = recordMap.get(result.getRecordId());
//            Map<Integer, BatchGatherDto> gatherMap = planRecordTemp.getBatchGatherLis().stream().collect(Collectors.toMap(BatchGatherDto::getBatchNum, Function.identity()));
//            for (BatchSampleDto batchSample : batchSampleLis) {
//                //获取对应采样记录
//                BatchGatherDto batchGatherDto = gatherMap.get(batchSample.getBatchNum());
//                //填充采样时间
//                batchSample.setGatherDate(batchGatherDto.getGatherDate());
//                //填充结果
//                Map<String, GatherMapDto> sourceMap = batchGatherDto.getGatherMap();
//                //需要补充result的map集合
//                Map<String, ResultGatherMapDto> tempMap = batchSample.getGatherMap();
//                //遍历填充
//                tempMap.forEach((key, val) -> {
//                    val.setResult(sourceMap.get(key).getResult());
//                });
//            }
//        }
        //普通物质
        resultMap.put("new_result_lis", resultList);
        //游离二氧化硅查询条件
        Query sio2Query = new Query(Criteria.where("project_id").is(projectId));
        sio2Query.addCriteria(Criteria.where("substance.substance_info.s_type").is(1));
        sio2Query.addCriteria(Criteria.where("substance.id").is(649));
        //游离二氧化硅
        resultMap.put("sio2_lis", mongoTemplate.find(sio2Query, Map.class, "zj_plan_record"));
        return resultMap;
    }

}
