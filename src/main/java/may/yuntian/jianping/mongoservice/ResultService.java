package may.yuntian.jianping.mongoservice;

import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.jianping.mongoentity.ResultEntity;
import may.yuntian.jianping.vo.VerificationsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class ResultService {
    @Autowired
    private MongoTemplate mongoTemplate;


    public ResultEntity getOneByRecordId(String recordId){
//        ObjectId objectId = new ObjectId(recordId);
        Query query = new Query(Criteria.where("record_id").is(recordId));
        ResultEntity resultEntity = mongoTemplate.findOne(query,ResultEntity.class);
        return resultEntity;
    }


    /**
     * 获取该项目验证性检测所有结果信息
     * @param projectId
     * @return
     */
    public List<VerificationsVo> getList(Long projectId,Integer isExist){
        Query query = new Query(Criteria.where("project_id").is(projectId));
        query.addCriteria(Criteria.where("substance.sample_tablename").is("airFixed"));
        if (StringUtils.checkValNotNull(isExist)){
            query.addCriteria(Criteria.where("test_type").is(isExist));
        }else {
            query.addCriteria(Criteria.where("test_type").is(2));
        }
        List<VerificationsVo> verificationsVoList = new ArrayList<>();
        List<ResultEntity> resultEntityList = mongoTemplate.find(query,ResultEntity.class);
        for (ResultEntity resultEntity : resultEntityList){
            VerificationsVo verificationsVo = new VerificationsVo();
            verificationsVo.setPlace(resultEntity.getTestPlace());
            verificationsVo.setTestItem(resultEntity.getSubstance().getName());
            verificationsVo.setMinLimit(resultEntity.getIsExist());
            verificationsVoList.add(verificationsVo);
        }
        return verificationsVoList;
    }


    /**
     * 根据条件获取超标不超标物质检测结果
     * @param projectId
     * @param sampleTable
     * @param isExceed
     * @return
     */
    public List<ResultEntity> getTestItemResult(Long projectId,String sampleTable,Integer isExceed){
        Query query = new Query(Criteria.where("project_id").is(projectId));
        query.addCriteria(Criteria.where("substance.sample_tablename").is(sampleTable));
        if (StringUtils.checkValNotNull(isExceed)){
            query.addCriteria(Criteria.where("is_exceed").is(isExceed));
        }
        List<ResultEntity> resultEntityList = mongoTemplate.find(query,ResultEntity.class);
        return resultEntityList;
    }


    public Map<String,List<ResultEntity>> getResultLsit(Long projectId){
        Query query = new Query(Criteria.where("project_id").is(projectId));
        List<ResultEntity> resultEntityList = mongoTemplate.find(query,ResultEntity.class);
        Map<String,List<ResultEntity>> map = resultEntityList.stream().collect(Collectors.groupingBy(ResultEntity::getPointId));
        return map;
    }

    /**
     * 获取所对应工种噪声结果
     * @param pfnIdList
     * @return
     */
    public List<ResultEntity> getListByProtection(List<String> pfnIdList){
        Query query = new Query(Criteria.where("pfn_id").in(pfnIdList));
        List<ResultEntity> resultEntityList = mongoTemplate.find(query,ResultEntity.class);
        return resultEntityList;
    }

}
