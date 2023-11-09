package may.yuntian.jianping.mongoservice;

import com.github.pagehelper.PageInfo;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.common.utils.ObjectConversion;
import may.yuntian.jianping.dto.PointMapDto;
import may.yuntian.jianping.mongoentity.WorkspaceEntity;
import may.yuntian.jianping.vo.MathResultVo;
import may.yuntian.jianping.vo.MeasureLayoutVo;
import may.yuntian.jianping.vo.WorkspaceVo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class WorkspaceService {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 新增定岗信息
     *
     * @param workspaceEntity
     */
    public void insertWorkspace(WorkspaceEntity workspaceEntity) {
        mongoTemplate.insert(workspaceEntity);
    }

    /**
     * 修改定岗信息
     *
     * @param workspaceEntity
     */
    public void updateWorkspace(WorkspaceEntity workspaceEntity) {
        mongoTemplate.save(workspaceEntity);
    }

    /**
     * 查询出所有定岗信息 以供选择
     *
     * @return
     */
    public List<WorkspaceVo> findAllList(Long projectId) {
        List<WorkspaceVo> workspaceVoList = new ArrayList<>();
        Query query = new Query(Criteria.where("project_id").is(projectId));
        List<WorkspaceEntity> workspaceEntityList = mongoTemplate.find(query, WorkspaceEntity.class);
        for (WorkspaceEntity workspaceEntity : workspaceEntityList) {
            WorkspaceVo workspaceVo = ObjectConversion.copy(workspaceEntity, WorkspaceVo.class);
            Map<String, PointMapDto> pointMapDtoMap = workspaceEntity.getPointMap();
            List<PointMapDto> pointMapList = new ArrayList<>();
            if (StringUtils.checkValNotNull(pointMapDtoMap)) {
                for (String key : pointMapDtoMap.keySet()) {
                    pointMapList.add(pointMapDtoMap.get(key));
                }
            }
            workspaceVo.setFixedHazards(workspaceEntity.getFixedHazards());
            workspaceVo.setIdentifyHazards(workspaceEntity.getIdentifyHazards());
            workspaceVo.setSilicaShare(workspaceEntity.getSilicaShare());
            workspaceVo.setPointMapList(pointMapList);
            workspaceVoList.add(workspaceVo);
        }
        return workspaceVoList;
    }


    public Long getFiexdCount(String workshopId) {
        ObjectId objectId = new ObjectId(workshopId);
        Query query = new Query(Criteria.where("workshop_id").is(objectId));
        query.addCriteria(Criteria.where("post").ne(""));
        Long count = mongoTemplate.count(query, WorkspaceEntity.class);
        return count;
    }


    public WorkspaceEntity getNeedDelete(String workshopId) {
        ObjectId objectId = new ObjectId(workshopId);
        Query query = new Query(Criteria.where("workshop_id").is(objectId));
        query.addCriteria(Criteria.where("post").is(""));
        query.addCriteria(Criteria.where("pfn_ids").is(new ArrayList<>()));
        WorkspaceEntity workspaceEntity = mongoTemplate.findOne(query, WorkspaceEntity.class);
        return workspaceEntity;
    }


    /**
     * 根据车间ID获取1条数据
     */
    public WorkspaceEntity getOneByWorkShopId(String workshopId) {
        ObjectId objectId = new ObjectId(workshopId);
        Query query = new Query(Criteria.where("workshop_id").is(objectId));
        WorkspaceEntity workspaceEntity = mongoTemplate.findOne(query, WorkspaceEntity.class);
        return workspaceEntity;
    }


    /**
     * 根据id删除一条定岗信息
     *
     * @param id
     */
    public void deleteWorkspace(String id) {
        WorkspaceEntity workspaceEntity = mongoTemplate.findById(id, WorkspaceEntity.class);
        if (workspaceEntity != null) {
            mongoTemplate.remove(workspaceEntity);
        }
    }

    /**
     * 根据绑定得定岗ID获取一条信息
     *
     * @param fixedPfnId
     * @return
     */
    public WorkspaceEntity getOneByFixedPostId(String fixedPfnId) {
        Query query = new Query(Criteria.where("fixed_pfn_id").is(fixedPfnId));
        WorkspaceEntity workspaceEntity = mongoTemplate.findOne(query, WorkspaceEntity.class);

        return workspaceEntity;
    }

    /**
     * 根据绑定得定岗ID获取多条信息
     *
     * @param
     * @return
     */
    public List<WorkspaceEntity> getListByFixedPostId(String id) {
        Criteria criteria1 = Criteria.where("pfn_ids").regex(".*?" + id + ".*");//条件1
        Criteria criteria2 = Criteria.where("pfn_ids").is(new ArrayList<>()).and("fixed_pfn_id").is(id);//条件2
        Criteria criteria = new Criteria();
        criteria.orOperator(criteria1, criteria2);//or查询添加条件
        Query query = new Query();
        query.addCriteria(criteria);
//        Query query = new Query(Criteria.where("pfn_ids").regex(".*?" + id + ".*").orOperator(Criteria.where("pfn_ids").is(new ArrayList<>()).and("fixed_pfn_id").is(id)));

        List<WorkspaceEntity> workspaceEntityList = mongoTemplate.find(query, WorkspaceEntity.class);

        return workspaceEntityList;
    }


    /**
     * 根据ID获取一条信息
     *
     * @param id
     * @return
     */
    public WorkspaceEntity getOneById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        WorkspaceEntity workspaceEntity = mongoTemplate.findOne(query, WorkspaceEntity.class);
        return workspaceEntity;
    }


    public void updateWorkShop(String workshopId, String workshop) {
        ObjectId objectId = new ObjectId(workshopId);
        Query query = new Query(Criteria.where("workshop_id").is(objectId));
        List<WorkspaceEntity> workspaceEntityList = mongoTemplate.find(query, WorkspaceEntity.class);
        if (workspaceEntityList.size() > 0) {
            for (WorkspaceEntity workspaceEntity : workspaceEntityList) {
                workspaceEntity.setWorkshop(workshop);
                workspaceEntity.setWorkshopArea(workshop);
                mongoTemplate.save(workspaceEntity);
            }
        }
    }


    /**
     * 通过项目ID获取布局布点列表
     *
     * @param params
     * @return
     */
    public Map<String, Object> getMeasureByProjectId(Map<String, Object> params) {
        String projectId = (String) params.get("projectId");
        String pageNum = (String) params.get("pageIndex");
        String pageSize = (String) params.get("pageSize");
        List<WorkspaceEntity> workspaceList = new ArrayList<>();
        List<MeasureLayoutVo> measureLayoutVoList = new ArrayList<>();
        Query query = new Query(Criteria.where("project_id").is(Long.valueOf(projectId)));
        query.addCriteria(Criteria.where("test_hazards").ne(""));
        Long count = mongoTemplate.count(query, WorkspaceEntity.class);
//        System.out.println("33333333333333333333333="+count);
        List<WorkspaceEntity> workspaceEntityList = this.listDesc(query, Integer.valueOf(pageNum), Integer.valueOf(pageSize));
//        System.out.println("5656565656555555555556666="+workspaceEntityList.size());
//        List<WorkspaceEntity> workspaceEntityList = mongoTemplate.find(query,WorkspaceEntity.class);
//        for (WorkspaceEntity workspaceEntity:workspaceEntityList){
//            if (workspaceEntity.getFixedHazards().size()>0){
//                workspaceList.add(workspaceEntity);
//            }
//        }
        for (WorkspaceEntity workspaceEntity : workspaceEntityList) {
            MeasureLayoutVo measureLayoutVo = new MeasureLayoutVo();
            Map<String, PointMapDto> pointMapDtoMap = workspaceEntity.getPointMap();
            for (String key : pointMapDtoMap.keySet()) {
                PointMapDto pointMapDto = pointMapDtoMap.get(key);
                measureLayoutVo.setProjectId(workspaceEntity.getProjectId());
                measureLayoutVo.setSubstances(workspaceEntity.getTestHazards());
                measureLayoutVo.setPointId(pointMapDto.getPointId());
                String place = workspaceEntity.getWorkshop() + "/" + workspaceEntity.getPost() + "/" + pointMapDto.getPoint();
                measureLayoutVo.setPlace(place);
                measureLayoutVo.setSort(pointMapDto.getSort());
                measureLayoutVoList.add(measureLayoutVo);
            }
        }
        List<MeasureLayoutVo> list = new ArrayList<>();
        if (measureLayoutVoList.size() > 0) {
            list = measureLayoutVoList.stream().sorted(Comparator.comparing(MeasureLayoutVo::getSort)).collect(Collectors.toList());
        }


        PageInfo<?> pageInfo = new PageInfo(list);
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("list", list);
        m.put("currPage", Integer.valueOf(pageNum));
        m.put("pageSize", Integer.valueOf(pageSize));
        m.put("totalCount", workspaceList.size());
        m.put("totalPage", pageInfo.getPages());


        return m;
    }

    /**
     * 修改点位排序
     *
     * @param measureLayoutVoList
     */
    public void updateSort(List<MeasureLayoutVo> measureLayoutVoList) {
        for (MeasureLayoutVo measureLayoutVo : measureLayoutVoList) {
            Long projectId = measureLayoutVo.getProjectId();
            String pointId = measureLayoutVo.getPointId();
            Query query = new Query(Criteria.where("project_id").is(projectId)).addCriteria(Criteria.where("point_map." + pointId + ".point_id").is(pointId));
            WorkspaceEntity workspaceEntity = mongoTemplate.findOne(query, WorkspaceEntity.class);
            Map<String, PointMapDto> pointMapDtoMap = workspaceEntity.getPointMap();
            PointMapDto pointMapDto = pointMapDtoMap.get(pointId);
            pointMapDto.setSort(measureLayoutVo.getSort());
            Update update = new Update();
            update.set("point_map", pointMapDtoMap);
            mongoTemplate.upsert(query, update, WorkspaceEntity.class);
        }
    }


    private List<WorkspaceEntity> listDesc(Query query, Integer pageNum, Integer pageSize) {
        List<WorkspaceEntity> titleList;

//        Query query = new Query();

        // 通过 _id 来排序
        query.with(Sort.by(Sort.Direction.ASC, "_id"));

        if (pageNum != 1) {
            // number 参数是为了查上一页的最后一条数据
            int number = (pageNum - 1) * pageSize;
            query.limit(number);

            List<WorkspaceEntity> titles = mongoTemplate.find(query, WorkspaceEntity.class);
            // 取出最后一条
            WorkspaceEntity title = titles.get(titles.size() - 1);

            // 取到上一页的最后一条数据 id，当作条件查接下来的数据
            String id = title.getId();

            // 从上一页最后一条开始查（大于不包括这一条）
            query.addCriteria(Criteria.where("_id").gt(id));
        }
        // 页大小重新赋值，覆盖 number 参数
        query.limit(pageSize);
        // 即可得到第n页数据
        titleList = mongoTemplate.find(query, WorkspaceEntity.class);

        return titleList;
    }


    /**
     * 获取所有检测物质中含有游里二氧化硅的岗位
     *
     * @param projectId
     * @return
     */
    public List<WorkspaceEntity> getSilicaList(Long projectId) {
        Query query = new Query(Criteria.where("project_id").is(projectId));
        Pattern pattern = Pattern.compile("^.*?游离二氧化硅.*$", Pattern.CASE_INSENSITIVE);
        query.addCriteria(Criteria.where("test_hazards").regex(pattern));
        List<WorkspaceEntity> list = mongoTemplate.find(query, WorkspaceEntity.class);
        return list;
    }


    public void updateSo2ToChose(String silicaShare) {
        Query query = new Query(Criteria.where("_id").is(silicaShare));
        WorkspaceEntity workspaceEntity = mongoTemplate.findOne(query, WorkspaceEntity.class);
        if (workspaceEntity.getSilicaShare().equals("0") || StringUtils.isBlank(workspaceEntity.getSilicaShare())) {
            workspaceEntity.setSilicaShare("-1");
            mongoTemplate.save(workspaceEntity);
        }
    }

    public void updateSo2ToCancle(String cancelSo2, String id) {
        Query query = new Query(Criteria.where("silica_share").is(cancelSo2));
        List<WorkspaceEntity> workspaceEntityList = mongoTemplate.find(query, WorkspaceEntity.class);
        boolean ret = false;
//        int a = 0;
        for (WorkspaceEntity workspaceEntity : workspaceEntityList) {
            if (workspaceEntity.getId().equals(id)) {
                ret = true;
            }
        }
        if (ret && workspaceEntityList.size() == 1) {
            Query query2 = new Query(Criteria.where("_id").is(cancelSo2));
            WorkspaceEntity workspaceEntity = mongoTemplate.findOne(query2, WorkspaceEntity.class);
            if (workspaceEntity.getSilicaShare().equals("-1")) {
                workspaceEntity.setSilicaShare("0");
                mongoTemplate.save(workspaceEntity);
            }
        }

    }


    /**
     * 获取识别不检测岗位信息
     *
     * @param projectId
     * @return
     */
    public List<WorkspaceEntity> getIdentifyHazardsByprojectId(Long projectId) {
        Query query = new Query(Criteria.where("project_id").is(projectId));
        List<WorkspaceEntity> workspaceEntityList = mongoTemplate.find(query, WorkspaceEntity.class);
        List<WorkspaceEntity> workspaceEntityList1 = new ArrayList<>();
        for (WorkspaceEntity workspaceEntity : workspaceEntityList) {
            if (workspaceEntity.getIdentifyHazards().size() > 0) {
                workspaceEntityList1.add(workspaceEntity);
            }
        }
        return workspaceEntityList1;
    }


    public Long getListByWorkShopId(String workshopId) {
        ObjectId objectId = new ObjectId(workshopId);
        Query query = new Query(Criteria.where("workshop_id").is(objectId));
        Long count = mongoTemplate.count(query, WorkspaceEntity.class);
        return count;
    }


}
