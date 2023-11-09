package may.yuntian.jianping.mongoservice;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import may.yuntian.anlian.mapper.ProjectMapper;
import may.yuntian.anlian.service.ProjectDateService;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.anlian.vo.ProjectPutVo;
import may.yuntian.common.utils.ObjectConversion;
import may.yuntian.jianping.dto.*;
import may.yuntian.jianping.entity.AlDeliverReceived;
import may.yuntian.jianping.entity.ProjectCountEntity;
import may.yuntian.jianping.mongoentity.PostPfnEntity;
import may.yuntian.jianping.mongoentity.WorkspaceEntity;
import may.yuntian.jianping.service.AlDeliverReceivedService;
import may.yuntian.jianping.service.ProjectCountService;
import may.yuntian.jianping.vo.PostPfnVo;
import may.yuntian.jianping.vo.WorkShopListVo;
import may.yuntian.jianping.vo.WorkTrackVo;
import may.yuntian.jianping.vo.WorkspaceVo;
import may.yuntian.sys.utils.Result;
import may.yuntian.untils.ClassCompareUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.KeyValue;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("all")
@Service
public class PostPfnService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private WorkspaceService workspaceService;
    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ProjectCountService projectCountService;

    @Autowired
    private AlDeliverReceivedService alDeliverReceivedService;

    @Autowired
    private ProjectDateService projectDateService;

    /**
     * 获取列表
     *
     * @param projectId
     * @return
     */
    public List<PostPfnVo> getAllListByProjectId(Long projectId) {
        List<PostPfnVo> postPfnVos = new ArrayList<>();
        Query query = new Query(Criteria.where("project_id").is(projectId));
        List<PostPfnEntity> postPfnEntities = mongoTemplate.find(query, PostPfnEntity.class);
        if (postPfnEntities.size() > 0) {
            PostPfnVo postPfnVo = new PostPfnVo();

            for (PostPfnEntity postPfnEntity : postPfnEntities) {
                postPfnVo = ObjectConversion.copy(postPfnEntity, PostPfnVo.class);
                if (postPfnEntity.getPostIds().size() > 0) {
//                    List<PointMapDto> pointMapList = new ArrayList<>();
                    List<WorkTrackDto> touchHazardsDtos = new ArrayList<>();
                    List<WorkTrackVo> workTrackVoList = new ArrayList<>();
//                    List<TouchHazardsDto> touchHazardsList = new ArrayList<>();
//                    List<TouchHazardsDto> identifyHazardsList = new ArrayList<>();
//                    String postId = postPfnEntity.getPostIds().get(0);
                    WorkspaceEntity workspaceEntity;
//                    System.out.println(postPfnVo.getId());
                    if (postPfnEntity.getIsFixed() == 1) {
                        workspaceEntity = workspaceService.getOneByFixedPostId(postPfnVo.getId());
                        postPfnVo.setPost(workspaceEntity.getPost());
                    } else {
                        workspaceEntity = workspaceService.getOneByWorkShopId(postPfnVo.getWorkshopId());
//                        System.out.println("=========="+workspaceEntity);
                        postPfnVo.setPost("");
                    }
//                    List<WorkspaceEntity> workspaceEntityList = workspaceService.getListByFixedPostId(postPfnEntity.getId());
                    postPfnVo.setWorkshop(workspaceEntity.getWorkshop());
                    postPfnVo.setArea(workspaceEntity.getArea());
                    postPfnVo.setWorkshopArea(workspaceEntity.getWorkshopArea());
                    postPfnVo.setAreaId(workspaceEntity.getAreaId());

//                    postPfnVo.setWorkspaceEntityList(workspaceEntityList);

                    Map<String, WorkTrackDto> workTrack = postPfnEntity.getWorkTrack();
                    if (workTrack != null) {
                        for (String key : workTrack.keySet()) {
                            touchHazardsDtos.add(workTrack.get(key));
                        }
                        for (WorkTrackDto workTrackDto : touchHazardsDtos) {
                            List<PointMapDto> pointMapList = new ArrayList<>();
                            List<TouchHazardsDto> touchHazardsList = new ArrayList<>();
                            List<TouchHazardsDto> identifyHazardsList = new ArrayList<>();
                            WorkTrackVo workTrackVo = new WorkTrackVo();
                            WorkspaceEntity workspaceEntity1 = workspaceService.getOneById(workTrackDto.getPostId());
                            workTrackVo.setPostId(workTrackDto.getPostId());
                            workTrackVo.setPost(workspaceEntity1.getPost());
                            workTrackVo.setArea(workspaceEntity1.getArea());
                            workTrackVo.setWorkshop(workspaceEntity1.getWorkshop());
                            workTrackVo.setWorkshopId(workspaceEntity1.getWorkshopId());
                            workTrackVo.setWorkshopArea(workspaceEntity1.getWorkshopArea());
                            workTrackVo.setSilicaShare(StringUtils.isNotBlank(workspaceEntity1.getSilicaShare()) ? "0" : workspaceEntity1.getSilicaShare());
                            for (String key : workTrackDto.getIdentifyHazardsMap().keySet()) {
                                identifyHazardsList.add(workTrackDto.getIdentifyHazardsMap().get(key));
                            }
                            for (String key : workTrackDto.getTouchHazardsMap().keySet()) {
                                touchHazardsList.add(workTrackDto.getTouchHazardsMap().get(key));
                            }

                            Map<String, PointMapDto> pointMapDtoMap = workspaceEntity1.getPointMap();
                            Map<String, List<String>> pfnPoints = workspaceEntity1.getPfnPoints();
                            if (StringUtils.checkValNotNull(pointMapDtoMap)) {
//                                for (String key:pointMapDtoMap.keySet()){
//                                    pointMapList.add(pointMapDtoMap.get(key));
//                                }
                                if (StringUtils.checkValNotNull(pfnPoints)) {
                                    List<String> points = pfnPoints.get(postPfnEntity.getId());
                                    if (points != null && points.size() > 0) {
                                        for (String pointId : points) {
                                            pointMapList.add(pointMapDtoMap.get(pointId));
                                        }
                                    }

                                }
                            }
                            workTrackVo.setSilicaShare(workspaceEntity1.getSilicaShare());
                            workTrackVo.setHazardSource(workTrackDto.getHazardSource());
                            workTrackVo.setWorkSituation(workTrackDto.getWorkSituation());
                            workTrackVo.setEpe(workspaceEntity1.getEpe());
//                            workTrackVo.setEpeRun(workspaceEntity1.getEpeRun());
                            workTrackVo.setPpe(workTrackDto.getPpe());
//                            workTrackVo.setPpeWear(workTrackDto.getPpeWear());
                            workTrackVo.setPointMapList(pointMapList);
                            workTrackVo.setIdentifyHazardsList(identifyHazardsList);
                            workTrackVo.setTouchHazardsList(touchHazardsList);
                            workTrackVoList.add(workTrackVo);
                        }
                        postPfnVo.setWorkTrackVos(workTrackVoList);
                    }
                    postPfnVos.add(postPfnVo);
                }
            }
        }
        postPfnVos = postPfnVos.stream().sorted(Comparator.comparing(PostPfnVo::getCreateTime)).collect(Collectors.toList());
        return postPfnVos;
    }


    /**
     * 返回值赋值处理
     *
     * @param postPfnEntity
     * @param workspaceEntity
     * @return
     */
    private PostPfnVo returnPostPfn(PostPfnEntity postPfnEntity, WorkspaceEntity workspaceEntity) {
        PostPfnVo postPfnVo = new PostPfnVo();
        postPfnVo = ObjectConversion.copy(postPfnEntity, PostPfnVo.class);
        if (workspaceEntity == null) {
            // 定岗信息获取
            if (postPfnEntity.getIsFixed() == 1) {
                workspaceEntity = workspaceService.getOneByFixedPostId(postPfnVo.getId());
//                System.out.println(workspaceEntity+"=====");
                postPfnVo.setPost(workspaceEntity.getPost());
            } else {
                // 非定岗信息获取
                System.out.println(postPfnVo.getWorkshopId());
                workspaceEntity = workspaceService.getOneByWorkShopId(postPfnVo.getWorkshopId());
                System.out.println(workspaceEntity);
                postPfnVo.setPost("");
            }
        } else {
            postPfnVo.setPost(workspaceEntity.getPost());
        }
        if (postPfnEntity.getPostIds().size() > 0) {

            List<WorkTrackDto> touchHazardsDtos = new ArrayList<>();
            List<WorkTrackVo> workTrackVoList = new ArrayList<>();
//            String postId = postPfnEntity.getPostIds().get(0);
//            List<WorkspaceEntity> workspaceEntityList = workspaceService.getListByFixedPostId(postPfnEntity.getId());
            postPfnVo.setWorkshop(workspaceEntity.getWorkshop());
            postPfnVo.setArea(workspaceEntity.getArea());
            postPfnVo.setWorkshopArea(workspaceEntity.getWorkshopArea());
            postPfnVo.setAreaId(workspaceEntity.getAreaId());
//            postPfnVo.setWorkshop(StringUtils.checkValNotNull(workspaceEntity)? workspaceEntity.getWorkshop() : postPfnVo.getWorkshop());
//            postPfnVo.setArea(StringUtils.checkValNotNull(workspaceEntity)? workspaceEntity.getArea() : postPfnVo.getArea());
//            postPfnVo.setPost(StringUtils.checkValNotNull(workspaceEntity)? workspaceEntity.getPost() : postPfnVo.getPost());
//            postPfnVo.setWorkspaceEntityList(workspaceEntityList);

            Map<String, WorkTrackDto> workTrack = postPfnEntity.getWorkTrack();
            if (workTrack != null) {
                for (String key : workTrack.keySet()) {
                    touchHazardsDtos.add(workTrack.get(key));
                }
//                System.out.println("333333===3=3="+touchHazardsDtos);
                for (WorkTrackDto workTrackDto : touchHazardsDtos) {
                    List<PointMapDto> pointMapList = new ArrayList<>();
                    List<TouchHazardsDto> touchHazardsList = new ArrayList<>();
                    List<TouchHazardsDto> identifyHazardsList = new ArrayList<>();
                    WorkspaceEntity workspaceEntity1 = workspaceService.getOneById(workTrackDto.getPostId());
                    WorkTrackVo workTrackVo = new WorkTrackVo();
                    workTrackVo.setPostId(workTrackDto.getPostId());
                    workTrackVo.setPost(workspaceEntity1.getPost());
                    workTrackVo.setAreaId(workspaceEntity1.getAreaId());
                    workTrackVo.setArea(workspaceEntity1.getArea());
                    workTrackVo.setWorkshop(workspaceEntity1.getWorkshop());
                    workTrackVo.setWorkshopArea(workspaceEntity1.getWorkshopArea());
                    workTrackVo.setWorkshopId(workspaceEntity1.getWorkshopId());
                    workTrackVo.setSilicaShare(StringUtils.isNotBlank(workspaceEntity1.getSilicaShare()) ? "0" : workspaceEntity1.getSilicaShare());
                    for (String key : workTrackDto.getIdentifyHazardsMap().keySet()) {
                        identifyHazardsList.add(workTrackDto.getIdentifyHazardsMap().get(key));
                    }
                    for (String key : workTrackDto.getTouchHazardsMap().keySet()) {
                        touchHazardsList.add(workTrackDto.getTouchHazardsMap().get(key));
                    }

                    Map<String, PointMapDto> pointMapDtoMap = workspaceEntity1.getPointMap();
                    Map<String, List<String>> pfnPoints = workspaceEntity1.getPfnPoints();
                    if (StringUtils.checkValNotNull(pointMapDtoMap)) {
//                                for (String key:pointMapDtoMap.keySet()){
//                                    pointMapList.add(pointMapDtoMap.get(key));
//                                }
                        if (StringUtils.checkValNotNull(pfnPoints)) {
                            List<String> points = pfnPoints.get(postPfnEntity.getId());
                            if (points != null && points.size() > 0) {
                                for (String pointId : points) {
                                    pointMapList.add(pointMapDtoMap.get(pointId));
                                }
                            }

                        }
                    }
                    workTrackVo.setSilicaShare(workspaceEntity1.getSilicaShare());
                    workTrackVo.setHazardSource(workTrackDto.getHazardSource());
                    workTrackVo.setWorkSituation(workTrackDto.getWorkSituation());
                    workTrackVo.setEpe(workspaceEntity1.getEpe());
//                    workTrackVo.setEpeRun(workspaceEntity1.getEpeRun());
                    workTrackVo.setPpe(workTrackDto.getPpe());
//                    workTrackVo.setPpeWear(workTrackDto.getPpeWear());
                    workTrackVo.setPointMapList(pointMapList);
                    workTrackVo.setIdentifyHazardsList(identifyHazardsList);
                    workTrackVo.setTouchHazardsList(touchHazardsList);
                    if (StringUtils.isNotBlank(workTrackVo.getPost())) {
                        workTrackVoList.add(workTrackVo);
                    }

                }
            }
//            System.out.println("333333---=-=-=-3="+workTrackVoList);
            postPfnVo.setWorkTrackVos(workTrackVoList);
        }
        return postPfnVo;
    }


    /**
     * 新增岗位/工种信息 如果不是流动岗同时新增一条车间/定岗信息
     * 车间岗位/工种信息 无ID新增时调用
     *
     * @param postPfnVo
     */
    public PostPfnVo insertPostPfn(PostPfnVo postPfnVo) {
        PostPfnEntity postPfnEntity = new PostPfnEntity();
        WorkspaceEntity workspaceEntity = new WorkspaceEntity();
        String id = new ObjectId().toString();
        postPfnEntity = ObjectConversion.copy(postPfnVo, PostPfnEntity.class);
        postPfnEntity.setId(id);
        postPfnEntity.setCreateTime(new Date());
        List<String> postIds = new ArrayList<>();
        Map<String, WorkTrackDto> workTrackDtoMap = new HashMap<>();
//        if (postPfnVo.getIsFixed()!=2) {
        List<String> pfnIds = new ArrayList<>();
        pfnIds.add(id);
        workspaceEntity.setId(new ObjectId().toString());
        //项目ID
        workspaceEntity.setProjectId(postPfnVo.getProjectId());
        // 车间ID
        workspaceEntity.setWorkshopId(StringUtils.isNotBlank(postPfnVo.getWorkshopId()) ? postPfnVo.getWorkshopId() : new ObjectId().toString());
        // workshop 车间名称
        workspaceEntity.setWorkshop(postPfnVo.getWorkshop());

        workspaceEntity.setAreaId(StringUtils.isNotBlank(postPfnVo.getAreaId()) ? postPfnVo.getAreaId() : new ObjectId().toString());
        workspaceEntity.setArea("");
        workspaceEntity.setWorkshopArea(postPfnVo.getWorkshop());
        workspaceEntity.setPost(postPfnVo.getPost());
        workspaceEntity.setFixedPfnId(id);
        workspaceEntity.setOnlyIdentify("");
        workspaceEntity.setIdentifyHazards(new ArrayList<IdentifyHazardsDto>());
        workspaceEntity.setTestHazards("");
        workspaceEntity.setFixedHazards(new ArrayList<IdentifyHazardsDto>());
        // 点位数量
        workspaceEntity.setPointNum(0);
        workspaceEntity.setSilicaShare("0");
        workspaceEntity.setPfnIds(pfnIds);
        workspaceEntity.setPointMap(new LinkedHashMap<String, PointMapDto>());
        workspaceEntity.setPointIds(new ArrayList<String>());
        workspaceEntity.setPfnPoints(new LinkedHashMap<String, List<String>>());
        workspaceEntity.setEpe(new ArrayList<EngineDto>());
//            workspaceEntity.setEpeRun("");
        workspaceEntity.setCreateTime(new Date());
        workspaceService.updateWorkspace(workspaceEntity);
        postIds.add(workspaceEntity.getId());
        WorkTrackDto workTrackDto = new WorkTrackDto();
        workTrackDto.setPostId(workspaceEntity.getId());
        workTrackDto.setTouchHazardsMap(new LinkedHashMap<>());
        workTrackDto.setIdentifyHazardsMap(new LinkedHashMap<>());
        workTrackDto.setPpe(new ArrayList<ProtectionDto>());
//            workTrackDto.setPpeWear("");
        workTrackDto.setHazardSource("");
        workTrackDto.setWorkSituation("");
        workTrackDtoMap.put(workspaceEntity.getId(), workTrackDto);
//        }

        postPfnEntity.setWorkshopId(StringUtils.isNotBlank(postPfnVo.getWorkshopId()) ? postPfnVo.getWorkshopId() : workspaceEntity.getWorkshopId());
        postPfnEntity.setAreaId(StringUtils.isNotBlank(postPfnVo.getAreaId()) ? postPfnVo.getAreaId() : workspaceEntity.getAreaId());
        postPfnEntity.setPostIds(postIds);
        postPfnEntity.setWorkTrack(workTrackDtoMap);
        mongoTemplate.save(postPfnEntity);


        ProjectCountEntity projectCountEntity = projectCountService.getOneByProjectId(postPfnEntity.getProjectId());
        if (projectCountEntity != null){
            if (StringUtils.isNotNull(projectCountEntity.getSurvey())){
                projectCountEntity.setSurveyLast(new Date());
                projectCountService.updateById(projectCountEntity);
            }else {
                projectCountEntity.setSurvey(new Date());
                projectCountService.updateById(projectCountEntity);
            }
        }else {
            ProjectCountEntity projectCount = new ProjectCountEntity();
            projectCount.setProjectId(postPfnEntity.getProjectId());
            projectCount.setSurvey(new Date());
            projectCountService.save(projectCount);
        }


        PostPfnVo postPfnVo2 = this.returnPostPfn(postPfnEntity, workspaceEntity);

        return postPfnVo2;
    }

    /**
     * 批量新增
     *
     * @param postPfnVos
     */
    public List<PostPfnVo> insertBatchPostPfn(List<WorkShopListVo> WorkShopListVos) {
        List<PostPfnVo> postPfnVos1 = new ArrayList<>();
        for (WorkShopListVo workShopListVo : WorkShopListVos) {
            PostPfnVo postPfnVo2 = new PostPfnVo();
            String workshopId;
            if (StringUtils.isBlank(workShopListVo.getWorkshopId())) {
                workshopId = new ObjectId().toString();
            } else {
                workshopId = workShopListVo.getWorkshopId();
            }
            workspaceService.updateWorkShop(workshopId, workShopListVo.getWorkshop());

            List<PostPfnVo> postPfnVos = workShopListVo.getPostPfnVo();
            for (PostPfnVo postPfnVo : postPfnVos) {
                if (StringUtils.isNotBlank(postPfnVo.getId())) {
                    postPfnVo.setWorkshop(workShopListVo.getWorkshop());
                    postPfnVo.setWorkshopId(workshopId);
                    postPfnVo2 = this.savePostPfn(postPfnVo);
                    postPfnVos1.add(postPfnVo2);
                } else {
                    postPfnVo.setWorkshop(workShopListVo.getWorkshop());
                    postPfnVo.setWorkshopId(workshopId);
                    postPfnVo2 = this.insertPostPfn(postPfnVo);
                    postPfnVos1.add(postPfnVo2);
                }

            }
        }
        return postPfnVos1;
    }


    private Map<String, WorkTrackDto> updateWorkSpeace(List<WorkTrackVo> workTrackVoList, PostPfnVo postPfnVo) {
//        List<IdentifyHazardsDto> identifyHazardsDtoList = new ArrayList<>();
//        List<IdentifyHazardsDto> fixedHazardsList = new ArrayList<>();
        List<String> postIds = new ArrayList<>();
        Map<String, WorkTrackDto> workTrackDtoMap = new LinkedHashMap<>();
        for (WorkTrackVo workTrackVo : workTrackVoList) {
            WorkTrackDto newWorkTrackDto = new WorkTrackDto();
            Map<String, TouchHazardsDto> saveIdentifyHazardsMap = new LinkedHashMap<>();
            Map<String, TouchHazardsDto> saveTouchHazardsMap = new LinkedHashMap<>();
            if (StringUtils.isBlank(workTrackVo.getPostId())) {
                WorkspaceEntity workspaceEntity = new WorkspaceEntity();
                List<String> pfnIds = new ArrayList<>();
                pfnIds.add(postPfnVo.getId());
                workspaceEntity.setId(new ObjectId().toString());
                workspaceEntity.setProjectId(postPfnVo.getProjectId());
                workspaceEntity.setWorkshopId(StringUtils.isNotBlank(workTrackVo.getWorkshopId()) ? workTrackVo.getWorkshopId() : new ObjectId().toString());
                workspaceEntity.setWorkshop(workTrackVo.getWorkshop());
                workspaceEntity.setAreaId(StringUtils.isNotBlank(workTrackVo.getAreaId()) ? workTrackVo.getAreaId() : new ObjectId().toString());
                workspaceEntity.setArea("");
                workspaceEntity.setWorkshopArea(workTrackVo.getWorkshop());
                workspaceEntity.setPost(workTrackVo.getPost());
                workspaceEntity.setFixedPfnId(postPfnVo.getId());
                workspaceEntity.setOnlyIdentify("");
                workspaceEntity.setIdentifyHazards(new ArrayList<IdentifyHazardsDto>());
                workspaceEntity.setTestHazards("");
                workspaceEntity.setFixedHazards(new ArrayList<IdentifyHazardsDto>());
                workspaceEntity.setPointNum(0);
                workspaceEntity.setPfnIds(pfnIds);
                workspaceEntity.setSilicaShare(StringUtils.isNotBlank(workTrackVo.getSilicaShare()) ? workTrackVo.getSilicaShare() : "0");
                workspaceEntity.setPointMap(new LinkedHashMap<String, PointMapDto>());
                workspaceEntity.setPointIds(new ArrayList<String>());
                workspaceEntity.setPfnPoints(new LinkedHashMap<String, List<String>>());
                workspaceEntity.setEpe(new ArrayList<EngineDto>());
//                workspaceEntity.setEpeRun("");
                workspaceEntity.setCreateTime(new Date());
                workspaceService.updateWorkspace(workspaceEntity);
                workTrackVo.setPostId(workspaceEntity.getId());
            }
            WorkspaceEntity workspaceEntity = workspaceService.getOneById(workTrackVo.getPostId());
//            workspaceEntity.setWorkshop(workTrackVo.getWorkshop());
//            workspaceEntity.setWorkshopArea(workTrackVo.getWorkshop());
            if (postPfnVo.getIsFixed().equals(1) || ("1").equals(postPfnVo.getIsFixed())) {
                workspaceEntity.setPost(postPfnVo.getPost());
            } else {
                workspaceEntity.setPost(workTrackVo.getPost());
            }
            workspaceEntity.setUpdateTime(new Date());
            List<String> pfnIds = workspaceEntity.getPfnIds();
            boolean a = pfnIds.contains(postPfnVo.getId());
            if (!a) {
                pfnIds.add(postPfnVo.getId());
            }
            workspaceEntity.setPfnIds(pfnIds);
            List<IdentifyHazardsDto> newIdentifyHazardsList = new ArrayList<>();
            List<IdentifyHazardsDto> newFixedHazardsList = new ArrayList<>();
            List<IdentifyHazardsDto> removeIdentifyList = new ArrayList<>();
            List<IdentifyHazardsDto> removeFixedList = new ArrayList<>();
            List<IdentifyHazardsDto> oldIdentifyHazards = workspaceEntity.getIdentifyHazards();
            List<IdentifyHazardsDto> oldFixedHazards = workspaceEntity.getFixedHazards();
            if (workTrackVo.getIdentifyHazardsList().size() > 0) {
                List<Long> updateSubIds = new ArrayList<>();
                List<Long> removePfnSubIds = new ArrayList<>();
                List<Long> newSubIds = new ArrayList<>();
                List<Long> oldIdentifySubIds = oldIdentifyHazards.stream().map(IdentifyHazardsDto::getSubstanceId).distinct().collect(Collectors.toList());
                List<Long> newIdentifySubIds = workTrackVo.getIdentifyHazardsList().stream().map(TouchHazardsDto::getSubstanceId).distinct().collect(Collectors.toList());
                Map<Long, List<IdentifyHazardsDto>> oldIdentifyHazardsMap = oldIdentifyHazards.stream().collect(Collectors.groupingBy(IdentifyHazardsDto::getSubstanceId));
                Map<Long, List<TouchHazardsDto>> newIdentifyHazardsMap = workTrackVo.getIdentifyHazardsList().stream().collect(Collectors.groupingBy(TouchHazardsDto::getSubstanceId));
                if (newIdentifySubIds.size() > 0 && oldIdentifySubIds.size() > 0) {
                    for (Long newIdentifySubId : newIdentifySubIds) {
                        if (oldIdentifySubIds.contains(newIdentifySubId)) {
                            updateSubIds.add(newIdentifySubId);
                        } else {
                            newSubIds.add(newIdentifySubId);
                        }
                    }
                    oldIdentifySubIds.removeAll(updateSubIds);
                    removePfnSubIds = oldIdentifySubIds;
                } else if (newIdentifySubIds.size() > 0 && !(oldIdentifySubIds.size() > 0)) {
                    newSubIds.addAll(newIdentifySubIds);
                } else if (oldIdentifySubIds.size() > 0 && !(newIdentifySubIds.size() > 0)) {
                    removePfnSubIds.addAll(oldIdentifySubIds);
                }

                if (newSubIds.size() > 0) {//新增物质
                    for (Long newSubId : newSubIds) {
                        TouchHazardsDto newTouchHazardsDto = newIdentifyHazardsMap.get(newSubId).get(0);
                        IdentifyHazardsDto newIdentifyHazardsDto = ObjectConversion.copy(newTouchHazardsDto, IdentifyHazardsDto.class);
                        newIdentifyHazardsDto.setSampleDays(1);
                        newIdentifyHazardsDto.setSampleNum(StringUtils.checkValNull(newIdentifyHazardsDto.getSampleNum()) ? 0 : newIdentifyHazardsDto.getSampleNum());
                        List<String> newPfnIds = new ArrayList<>();
                        newPfnIds.add(postPfnVo.getId());
                        newIdentifyHazardsDto.setPfnIds(newPfnIds);
                        newIdentifyHazardsList.add(newIdentifyHazardsDto);
                    }
                }

                if (removePfnSubIds.size() > 0) {//删除pfnId
                    for (Long removeSubId : removePfnSubIds) {
                        IdentifyHazardsDto removeIdentifyHazardsDto = oldIdentifyHazardsMap.get(removeSubId).get(0);
                        List<String> oldpPfnIds = removeIdentifyHazardsDto.getPfnIds();
                        if (oldpPfnIds.size() > 0) {
                            if (oldpPfnIds.contains(postPfnVo.getId())) {
                                oldpPfnIds.remove(postPfnVo.getId());
                                if (oldpPfnIds.size() > 0) {
                                    newIdentifyHazardsList.add(removeIdentifyHazardsDto);
                                } else {
                                    removeIdentifyList.add(removeIdentifyHazardsDto);
                                }
                            }
                        } else {
                            removeIdentifyList.add(removeIdentifyHazardsDto);
                        }
                    }
                }
                if (updateSubIds.size() > 0) {//关联修改
                    for (Long updateSubId : updateSubIds) {
                        IdentifyHazardsDto updateIdentifyHazardsDto = oldIdentifyHazardsMap.get(updateSubId).get(0);
                        TouchHazardsDto newTouchHazard = newIdentifyHazardsMap.get(updateSubId).get(0);

                        updateIdentifyHazardsDto.setAlias(newTouchHazard.getAlias());
                        updateIdentifyHazardsDto.setSampleNum(StringUtils.checkValNull(newTouchHazard.getSampleNum()) ? 0 : newTouchHazard.getSampleNum());
                        if (!updateIdentifyHazardsDto.getPfnIds().contains(postPfnVo.getId())) {
                            List<String> ids = updateIdentifyHazardsDto.getPfnIds();
                            ids.add(postPfnVo.getId());
                            updateIdentifyHazardsDto.setPfnIds(ids);
                        }
//                        updateIdentifyHazardsDto.setPfnIds(pfnIds);
//                        pfnIds.remove(postPfnVo.getId());
                        Query query = new Query(Criteria.where("_id").in(pfnIds));
                        List<PostPfnEntity> postPfnEntityList = mongoTemplate.find(query, PostPfnEntity.class);
//                        pfnIds.add(postPfnVo.getId());
                        for (PostPfnEntity postPfnEntity : postPfnEntityList) {
                            if (postPfnEntity.getId() != postPfnVo.getId()) {
                                Map<String, WorkTrackDto> workTrack = postPfnEntity.getWorkTrack();
                                if (StringUtils.checkValNotNull(workTrack)) {
                                    WorkTrackDto workTrackDto = workTrack.get(workspaceEntity.getId());
                                    if (StringUtils.checkValNotNull(workTrackDto)) {
                                        Map<String, TouchHazardsDto> identifyHazardsMap = workTrackDto.getIdentifyHazardsMap();
                                        if (StringUtils.checkValNotNull(identifyHazardsMap)) {
                                            TouchHazardsDto oldTouchHazard = identifyHazardsMap.get(updateSubId);
                                            if (StringUtils.checkValNotNull(oldTouchHazard)) {
                                                oldTouchHazard.setAlias(newTouchHazard.getAlias());
                                                oldTouchHazard.setTestType(newTouchHazard.getTestType());
                                                if (newTouchHazard.getTestType() != 3) {
                                                    identifyHazardsMap.remove(updateSubId, oldTouchHazard);
                                                    workTrackDto.getTouchHazardsMap().put(String.valueOf(updateSubId), oldTouchHazard);
                                                    mongoTemplate.save(postPfnEntity);
                                                } else {
                                                    mongoTemplate.save(postPfnEntity);
                                                }
                                            }
                                        } else {
                                            Map<String, TouchHazardsDto> touchHazardsMap = workTrackDto.getTouchHazardsMap();
                                            if (StringUtils.checkValNotNull(touchHazardsMap)) {
                                                TouchHazardsDto oldTouchHazard = identifyHazardsMap.get(updateSubId);
                                                if (StringUtils.checkValNotNull(oldTouchHazard)) {
                                                    oldTouchHazard.setAlias(newTouchHazard.getAlias());
                                                    oldTouchHazard.setTestType(newTouchHazard.getTestType());
                                                    if (newTouchHazard.getTestType() == 3) {
                                                        touchHazardsMap.remove(updateSubId, oldTouchHazard);
                                                        workTrackDto.getIdentifyHazardsMap().put(String.valueOf(updateSubId), oldTouchHazard);
                                                        mongoTemplate.save(postPfnEntity);
                                                    } else {
                                                        mongoTemplate.save(postPfnEntity);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }
                }

                //处理PFN表中轨迹信息
                for (Long id : newIdentifySubIds) {
                    if (id.equals(0)) {
                        List<TouchHazardsDto> list = newIdentifyHazardsMap.get(id);
                        for (TouchHazardsDto touchHazardsDto : list) {
                            touchHazardsDto.setSampleDays(1);
                            touchHazardsDto.setTimeFrame("");
                            saveIdentifyHazardsMap.put(String.valueOf(id), touchHazardsDto);
                        }
                    } else {
                        TouchHazardsDto touchHazardsDto = newIdentifyHazardsMap.get(id).get(0);
                        touchHazardsDto.setSampleDays(1);
                        touchHazardsDto.setTimeFrame("");
                        saveIdentifyHazardsMap.put(String.valueOf(id), touchHazardsDto);
                    }
                }
            } else {
                for (IdentifyHazardsDto identifyHazardsDto : oldIdentifyHazards) {
                    List<String> oldpPfnIds = identifyHazardsDto.getPfnIds();
                    if (oldpPfnIds != null && oldpPfnIds.size() > 0) {
                        if (oldpPfnIds.contains(postPfnVo.getId())) {
                            oldpPfnIds.remove(postPfnVo.getId());
                            if (!(oldpPfnIds.size() > 0)) {
                                removeIdentifyList.add(identifyHazardsDto);
                            }
                        }
                    } else {
                        removeIdentifyList.add(identifyHazardsDto);
                    }
                }
            }
            if (workTrackVo.getTouchHazardsList().size() > 0) {
                List<Long> updateSubIds = new ArrayList<>();
                List<Long> removePfnSubIds = new ArrayList<>();
                List<Long> newSubIds = new ArrayList<>();

                List<Long> oldIdentifySubIds = oldFixedHazards.stream().map(IdentifyHazardsDto::getSubstanceId).distinct().collect(Collectors.toList());
                List<Long> newIdentifySubIds = workTrackVo.getTouchHazardsList().stream().map(TouchHazardsDto::getSubstanceId).distinct().collect(Collectors.toList());


                Map<Long, List<IdentifyHazardsDto>> oldIdentifyHazardsMap = oldFixedHazards.stream().collect(Collectors.groupingBy(IdentifyHazardsDto::getSubstanceId));
                Map<Long, List<TouchHazardsDto>> newIdentifyHazardsMap = workTrackVo.getTouchHazardsList().stream().collect(Collectors.groupingBy(TouchHazardsDto::getSubstanceId));
                if (newIdentifySubIds.size() > 0 && oldIdentifySubIds.size() > 0) {
                    for (Long newIdentifySubId : newIdentifySubIds) {
//                        for (Long oldIdentifySubId : oldIdentifySubIds){
//                            if (oldIdentifySubId.equals(newIdentifySubId)){
//                                updateSubIds.add(oldIdentifySubId);
//                            }else {
//                                removePfnSubIds.add(oldIdentifySubId);
//                                newSubIds.add(newIdentifySubId);
//                            }
//                        }
                        if (oldIdentifySubIds.contains(newIdentifySubId)) {
                            updateSubIds.add(newIdentifySubId);
                        } else {
                            newSubIds.add(newIdentifySubId);
                        }
                    }
                    oldIdentifySubIds.removeAll(updateSubIds);
                    removePfnSubIds = oldIdentifySubIds;
                } else if (newIdentifySubIds.size() > 0 && oldIdentifySubIds.size() <= 0) {
                    newSubIds.addAll(newIdentifySubIds);
                } else if (oldIdentifySubIds.size() > 0 && newIdentifySubIds.size() <= 0) {
                    removePfnSubIds.addAll(oldIdentifySubIds);
                }

                if (newSubIds.size() > 0) {//新增物质
                    for (Long newSubId : newSubIds) {
                        TouchHazardsDto newTouchHazardsDto = newIdentifyHazardsMap.get(newSubId).get(0);
                        IdentifyHazardsDto newIdentifyHazardsDto = ObjectConversion.copy(newTouchHazardsDto, IdentifyHazardsDto.class);
                        newIdentifyHazardsDto.setSampleDays(1);
                        newIdentifyHazardsDto.setSampleNum(StringUtils.checkValNull(newIdentifyHazardsDto.getSampleNum()) ? 0 : newIdentifyHazardsDto.getSampleNum());
                        List<String> newPfnIds = new ArrayList<>();
                        newPfnIds.add(postPfnVo.getId());
                        newIdentifyHazardsDto.setPfnIds(newPfnIds);
//                        newIdentifyHazardsDto.setPfnIds(pfnIds);
                        newFixedHazardsList.add(newIdentifyHazardsDto);
                    }
                }

                if (removePfnSubIds.size() > 0) {//删除pfnId
                    for (Long removeSubId : removePfnSubIds) {
                        IdentifyHazardsDto removeIdentifyHazardsDto = oldIdentifyHazardsMap.get(removeSubId).get(0);
                        List<String> oldpPfnIds = removeIdentifyHazardsDto.getPfnIds();
                        if (oldpPfnIds.size() > 0) {
//                            System.out.println();
                            if (oldpPfnIds.contains(postPfnVo.getId())) {
                                oldpPfnIds.remove(postPfnVo.getId());
                                if (!(oldpPfnIds.size() > 0)) {
//                                    newFixedHazardsList.add(removeIdentifyHazardsDto);
//                                }else {
                                    removeFixedList.add(removeIdentifyHazardsDto);
                                }
                            }
                        } else {
                            removeFixedList.add(removeIdentifyHazardsDto);
                        }
                    }
                }
                if (updateSubIds.size() > 0) {//关联修改
                    for (Long updateSubId : updateSubIds) {
                        IdentifyHazardsDto updateIdentifyHazardsDto = oldIdentifyHazardsMap.get(updateSubId).get(0);
                        TouchHazardsDto newTouchHazard = newIdentifyHazardsMap.get(updateSubId).get(0);
                        updateIdentifyHazardsDto.setAlias(newTouchHazard.getAlias());
                        updateIdentifyHazardsDto.setSampleNum(StringUtils.checkValNull(newTouchHazard.getSampleNum()) ? 0 : newTouchHazard.getSampleNum());
                        if (!updateIdentifyHazardsDto.getPfnIds().contains(postPfnVo.getId())) {
                            List<String> ids = updateIdentifyHazardsDto.getPfnIds();
                            ids.add(postPfnVo.getId());
                            updateIdentifyHazardsDto.setPfnIds(ids);
                        }
//                        updateIdentifyHazardsDto.setPfnIds(pfnIds);
//                        List<String> updatePfnIds = workspaceEntity.getPfnIds();
//                        pfnIds.remove(postPfnVo.getId());
                        Query query = new Query(Criteria.where("_id").in(pfnIds));
                        List<PostPfnEntity> postPfnEntityList = mongoTemplate.find(query, PostPfnEntity.class);
//                        pfnIds.add(postPfnVo.getId());
                        for (PostPfnEntity postPfnEntity : postPfnEntityList) {
                            if (postPfnEntity.getId() != postPfnVo.getId()) {
                                Map<String, WorkTrackDto> workTrack = postPfnEntity.getWorkTrack();
                                if (StringUtils.checkValNotNull(workTrack)) {
                                    WorkTrackDto workTrackDto = workTrack.get(workspaceEntity.getId());
                                    if (StringUtils.checkValNotNull(workTrackDto)) {
                                        Map<String, TouchHazardsDto> identifyHazardsMap = workTrackDto.getIdentifyHazardsMap();
                                        if (StringUtils.checkValNotNull(identifyHazardsMap)) {
                                            TouchHazardsDto oldTouchHazard = identifyHazardsMap.get(updateSubId);
                                            if (StringUtils.checkValNotNull(oldTouchHazard)) {
                                                oldTouchHazard.setAlias(newTouchHazard.getAlias());
                                                oldTouchHazard.setTestType(newTouchHazard.getTestType());
                                                if (newTouchHazard.getTestType() != 3) {
                                                    identifyHazardsMap.remove(updateSubId, oldTouchHazard);
                                                    workTrackDto.getTouchHazardsMap().put(String.valueOf(updateSubId), oldTouchHazard);
                                                    mongoTemplate.save(postPfnEntity);
                                                } else {
                                                    mongoTemplate.save(postPfnEntity);
                                                }
                                            }
                                        } else {
                                            Map<String, TouchHazardsDto> touchHazardsMap = workTrackDto.getTouchHazardsMap();
                                            if (StringUtils.checkValNotNull(touchHazardsMap)) {
                                                TouchHazardsDto oldTouchHazard = identifyHazardsMap.get(updateSubId);
                                                if (StringUtils.checkValNotNull(oldTouchHazard)) {
                                                    oldTouchHazard.setAlias(newTouchHazard.getAlias());
                                                    oldTouchHazard.setTestType(newTouchHazard.getTestType());
                                                    if (newTouchHazard.getTestType() == 3) {
                                                        touchHazardsMap.remove(updateSubId, oldTouchHazard);
                                                        workTrackDto.getIdentifyHazardsMap().put(String.valueOf(updateSubId), oldTouchHazard);
                                                        mongoTemplate.save(postPfnEntity);
                                                    } else {
                                                        mongoTemplate.save(postPfnEntity);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                //处理PFN表中轨迹信息
                for (Long id : newIdentifySubIds) {
                    if (id.equals(0)) {
                        List<TouchHazardsDto> list = newIdentifyHazardsMap.get(id);
                        for (TouchHazardsDto touchHazardsDto : list) {
                            touchHazardsDto.setSampleDays(1);
                            touchHazardsDto.setTimeFrame("");
                            saveTouchHazardsMap.put(String.valueOf(id), touchHazardsDto);
                        }
                    } else {
                        TouchHazardsDto touchHazardsDto = newIdentifyHazardsMap.get(id).get(0);
                        touchHazardsDto.setSampleDays(1);
                        touchHazardsDto.setTimeFrame("");
                        saveTouchHazardsMap.put(String.valueOf(id), touchHazardsDto);
                    }

                }
//                System.out.println("newSubIds= "+newSubIds.toString());
//                System.out.println("updateSubIds= "+updateSubIds.toString());
//                System.out.println("removePfnSubIds= "+removePfnSubIds.toString());
            } else {
                for (IdentifyHazardsDto identifyHazardsDto : oldFixedHazards) {
                    List<String> oldpPfnIds = identifyHazardsDto.getPfnIds();
                    if (oldpPfnIds.size() > 0) {
                        if (oldpPfnIds.contains(postPfnVo.getId())) {
                            oldpPfnIds.remove(postPfnVo.getId());
                            if (!(oldpPfnIds.size() > 0)) {
                                removeFixedList.add(identifyHazardsDto);
                            }
                        }
                    } else {
                        removeFixedList.add(identifyHazardsDto);
                    }
                }
            }

            oldFixedHazards.removeAll(removeFixedList);
            oldFixedHazards.addAll(newFixedHazardsList);
//            newFixedHazardsList.forEach(System.out::println);
//            System.out.println("-----------11------------");
//            oldFixedHazards.forEach(System.out::println);
            workspaceEntity.setFixedHazards(oldFixedHazards);
            List<String> fixedNameList = new ArrayList<>();
            String testHazards = "";
            for (IdentifyHazardsDto identifyHazardsDto : oldFixedHazards) {
                String substanceName = "";
                if (StringUtils.isNotBlank(identifyHazardsDto.getAlias())) {
                    substanceName = identifyHazardsDto.getName() + "(" + identifyHazardsDto.getAlias() + ")";
                    fixedNameList.add(substanceName);
                } else {
                    substanceName = identifyHazardsDto.getName();
                    fixedNameList.add(substanceName);
                }
            }
            testHazards = fixedNameList.stream().collect(Collectors.joining("、"));
            workspaceEntity.setTestHazards(testHazards);
            oldIdentifyHazards.removeAll(removeIdentifyList);
            oldIdentifyHazards.addAll(newIdentifyHazardsList);
            workspaceEntity.setIdentifyHazards(oldIdentifyHazards);
            List<String> onlyNameList = new ArrayList<>();
            String onlyIdentify = "";
            for (IdentifyHazardsDto identifyHazardsDto : newIdentifyHazardsList) {
                String substanceName = "";
                if (StringUtils.isNotBlank(identifyHazardsDto.getAlias())) {
                    substanceName = identifyHazardsDto.getName() + "(" + identifyHazardsDto.getAlias() + ")";
                    onlyNameList.add(substanceName);
                } else {
                    substanceName = identifyHazardsDto.getName();
                    onlyNameList.add(substanceName);
                }
            }
            onlyIdentify = onlyNameList.stream().collect(Collectors.joining("、"));
            workspaceEntity.setOnlyIdentify(onlyIdentify);

            Map<String, PointMapDto> pointMapDtoMap = workspaceEntity.getPointMap();
//            Map<String,PointMapDto> pointMapDtoMap = new HashMap<>();
            List<String> pointIdsLsit = workspaceEntity.getPointIds();//原先的pointIdList
            List<PointMapDto> pointMapDtoList = workTrackVo.getPointMapList();
            Map<String, List<String>> pfnPoints = workspaceEntity.getPfnPoints();
            List<String> oldPointIdList = pfnPoints.get(postPfnVo.getId());
            List<String> pointIdList = new ArrayList<>();//新点位ID数组
            List<String> removePointIds = new ArrayList<>();//需要删除工种ID得点位ID数组
            if (pointMapDtoList.size() > 0) {
                for (PointMapDto pointMapDto : pointMapDtoList) {
                    List<String> piontPfnIds = new ArrayList<>();
                    piontPfnIds.add(postPfnVo.getId());
                    if (StringUtils.isBlank(pointMapDto.getPointId())) {
                        String pointId = new ObjectId().toString();
                        pointMapDto.setPointId(pointId);
                        pointMapDto.setPfnIds(piontPfnIds);
                        pointMapDto.setSort(0);
                        pointMapDto.setMaxSampleCode(0);
                        pointMapDto.setImgId(0);
                        pointMapDto.setCode(0);
                        pointMapDtoMap.put(pointId, pointMapDto);
                        pointIdList.add(pointId);
                        pointIdsLsit.add(pointId);
                    } else {
                        if (!(pointMapDto.getPfnIds().contains(postPfnVo.getId()))) {
                            List<String> piPfnIds = pointMapDto.getPfnIds();
                            piPfnIds.add(postPfnVo.getId());
                            pointMapDto.setPfnIds(piPfnIds);
                        }
                        pointMapDtoMap.put(pointMapDto.getPointId(), pointMapDto);
//                    for (String oldPointId : oldPointIdList){
//                        if (!(oldPointId.equals(pointMapDto.getPointId()))){
                        pointIdList.add(pointMapDto.getPointId());
//                            removePointIds.add(oldPointId);
//                        }else {
//                            pointIdList.add(pointMapDto.getPointId());
//                        }
//                    }

                    }

                }
                List<String> newPointList = pointIdList.stream().distinct().collect(Collectors.toList());
                if (newPointList.size() > 0) {
                    pfnPoints.put(postPfnVo.getId(), newPointList);
                }
                if (oldPointIdList != null && oldPointIdList.size() > 0) {
                    oldPointIdList.removeAll(newPointList);
                    removePointIds.addAll(oldPointIdList);
                }
            } else {
                for (String key : pointMapDtoMap.keySet()) {
                    removePointIds.add(key);
                }
                pfnPoints.remove(postPfnVo.getId());
            }


            if (removePointIds.size() > 0) {
                for (String removePoint : removePointIds) {
                    PointMapDto removePointMapDto = pointMapDtoMap.get(removePoint);
                    List<String> pointMapPfnIds = removePointMapDto.getPfnIds();
                    if (pointMapPfnIds.contains(postPfnVo.getId())) {
                        pointMapPfnIds.remove(postPfnVo.getId());
                        if (pointMapPfnIds.size() > 0) {
                            removePointMapDto.setPfnIds(pointMapPfnIds);
                            pointMapDtoMap.put(removePoint, removePointMapDto);
                        } else {
                            pointMapDtoMap.remove(removePoint);
                            pointIdsLsit.remove(removePoint);
                        }
                    }
                }
            }
            int num = 0;
            for (String key : pointMapDtoMap.keySet()) {
                num++;
            }

            workspaceEntity.setPfnPoints(pfnPoints);
            workspaceEntity.setPointIds(pointIdsLsit);
            workspaceEntity.setPointNum(num);
            workspaceEntity.setPointMap(pointMapDtoMap);
            if (!workTrackVo.getSilicaShare().equals("0") && !workTrackVo.getSilicaShare().equals("-1")) {
                workspaceService.updateSo2ToChose(workTrackVo.getSilicaShare());
            } else if (StringUtils.isNotBlank(workTrackVo.getCancelSo2())) {
                workspaceService.updateSo2ToCancle(workTrackVo.getCancelSo2(), workTrackVo.getPostId());
            }
            workspaceEntity.setSilicaShare(workTrackVo.getSilicaShare());
//            workspaceEntity.setEpeRun(StringUtils.isNotBlank(workTrackVo.getEpeRun())?workTrackVo.getEpeRun():"");
            workspaceEntity.setEpe(workTrackVo.getEpe());
            workspaceService.updateWorkspace(workspaceEntity);

            newWorkTrackDto.setHazardSource(workTrackVo.getHazardSource());
            newWorkTrackDto.setWorkSituation(workTrackVo.getWorkSituation());
            newWorkTrackDto.setPostId(workspaceEntity.getId());
//            newWorkTrackDto.setPpeWear(workTrackVo.getPpeWear());
            newWorkTrackDto.setPpe(workTrackVo.getPpe());
            newWorkTrackDto.setIdentifyHazardsMap(saveIdentifyHazardsMap);
            newWorkTrackDto.setTouchHazardsMap(saveTouchHazardsMap);
            workTrackDtoMap.put(workspaceEntity.getId(), newWorkTrackDto);
        }
        return workTrackDtoMap;
    }


    /**
     * 新增岗位/工种信息 如果不是流动岗同时新增一条车间/定岗信息
     * 车间岗位/工种信息 有ID修改时调用
     *
     * @param postPfnVo
     */
    public PostPfnVo savePostPfn(PostPfnVo postPfnVo) {

        if (StringUtils.isBlank(postPfnVo.getId())) {
            PostPfnVo newPostPfnVo = this.insertPostPfn(postPfnVo);
            postPfnVo.setWorkshopArea(newPostPfnVo.getWorkshopArea());
            postPfnVo.setWorkshop(newPostPfnVo.getWorkshop());

            postPfnVo.setId(newPostPfnVo.getId());
//            String id = new ObjectId().toString();
//            postPfnEntity.setId(id);
            postPfnVo.setWorkshopId(newPostPfnVo.getWorkshopId());
            postPfnVo.setAreaId(newPostPfnVo.getAreaId());
            postPfnVo.setPost(postPfnVo.getPost());
            postPfnVo.setCreateTime(newPostPfnVo.getCreateTime());
        }
        PostPfnEntity postPfnEntity = new PostPfnEntity();
        postPfnEntity = ObjectConversion.copy(postPfnVo, PostPfnEntity.class);


        Integer testNum = Integer.valueOf(postPfnVo.getTestNum());
        postPfnEntity.setTestNum(testNum);
        postPfnEntity.setUpdateTime(new Date());
        if (postPfnVo.getWorkTrackVos() != null && postPfnVo.getWorkTrackVos().size() > 0) {
            List<WorkTrackVo> workTrackVoList = postPfnVo.getWorkTrackVos();
            Map<String, WorkTrackDto> workTrackDtoMap = this.updateWorkSpeace(workTrackVoList, postPfnVo);
            postPfnEntity.setWorkTrack(workTrackDtoMap);
            List<String> oldPostIds = postPfnEntity.getPostIds();
            List<String> postIds = new ArrayList<>();
            for (String key : workTrackDtoMap.keySet()) {
                postIds.add(key);
            }
            if (postIds.size() > 0) {
                oldPostIds.removeAll(postIds);
            }
            if (oldPostIds != null && oldPostIds.size() > 0) {
                for (String id : oldPostIds) {
                    WorkspaceEntity workspaceEntity = workspaceService.getOneById(id);
                    List<String> pfnIds = workspaceEntity.getPfnIds();
                    Long count = workspaceService.getFiexdCount(workspaceEntity.getWorkshopId());
                    if (pfnIds != null && pfnIds.size() > 0) {
                        pfnIds.remove(postPfnEntity.getId());
                        if (pfnIds.size() > 0) {
                            workspaceService.updateWorkspace(workspaceEntity);
                        } else if (!(count > 0)) {
                            workspaceService.updateWorkspace(workspaceEntity);
                        } else {
                            WorkspaceEntity workspaceEntity1 = workspaceService.getNeedDelete(workspaceEntity.getWorkshopId());
                            if (workspaceEntity1 != null) {
                                PostPfnEntity postPfnEntity1 = this.getOneById(workspaceEntity.getFixedPfnId());
                                postPfnEntity1.setAreaId(workspaceEntity.getAreaId());
                                mongoTemplate.save(postPfnEntity1);
                            }
                            workspaceService.deleteWorkspace(id);
                        }
                    } else if (!(count > 0)) {
                        continue;
                    } else {
                        WorkspaceEntity workspaceEntity1 = workspaceService.getNeedDelete(workspaceEntity.getWorkshopId());
                        if (workspaceEntity1 != null) {
                            PostPfnEntity postPfnEntity1 = this.getOneById(workspaceEntity1.getFixedPfnId());
                            postPfnEntity1.setAreaId(workspaceEntity.getAreaId());
                            mongoTemplate.save(postPfnEntity1);
                        }
                        workspaceService.deleteWorkspace(id);
                    }
                }
            }
            if (postIds.size() > 0) {
                for (String id : postIds) {
                    WorkspaceEntity workspaceEntity = workspaceService.getOneById(id);
                    Long count = workspaceService.getFiexdCount(workspaceEntity.getWorkshopId());
                    if (!(count > 0)) {
                        continue;
                    } else {
                        WorkspaceEntity workspaceEntity1 = workspaceService.getNeedDelete(workspaceEntity.getWorkshopId());
                        if (workspaceEntity1 != null) {
                            PostPfnEntity postPfnEntity1 = this.getOneById(workspaceEntity.getFixedPfnId());
                            postPfnEntity1.setAreaId(workspaceEntity.getAreaId());
                            mongoTemplate.save(postPfnEntity1);
                            workspaceService.deleteWorkspace(workspaceEntity1.getId());
                        }
                    }
                }
            }
            postPfnEntity.setPostIds(postIds);
        }

        List<CodeSortDto> newCodeSort = new ArrayList<>();
        List<CodeSortDto> codeSort = postPfnVo.getCodeSort();
        List<CodeSortDto> removeCodeSortDto = new ArrayList<>();
        if (testNum == 1) {
//            if (codeSort.size()>0){
            if (codeSort.size() == 1) {
                newCodeSort.add(codeSort.get(0));
//                    System.out.println(11111);
            } else {
//                    System.out.println(2222);
                CodeSortDto codeSortDto = new CodeSortDto();
                codeSortDto.setPointId(new ObjectId().toString());
                codeSortDto.setPoint("1");
                codeSortDto.setCode(0);
                codeSortDto.setMaxSampleCode(0);
                codeSortDto.setSort(0);
                newCodeSort.add(codeSortDto);
            }
//            }
        } else if (testNum > 1) {
            if (codeSort.size() >= testNum) {
                for (int j = 1; j <= codeSort.size() - postPfnEntity.getTestNum(); j++) {
//                    codeSort.remove(codeSort.size()-j);
                    removeCodeSortDto.add(codeSort.get(codeSort.size() - j));
                }
                codeSort.removeAll(removeCodeSortDto);
                newCodeSort.addAll(codeSort);
            } else if (codeSort.size() < testNum) {
                if (codeSort.size() == 0) {
                    for (int j = 1; j <= testNum; j++) {
//                        CodeSortDto lastCodeSort = codeSort.get(codeSort.size()-1);
//                        Integer post = 0;
                        CodeSortDto codeSortDto = new CodeSortDto();
                        codeSortDto.setPointId(new ObjectId().toString());
                        codeSortDto.setPoint(String.valueOf(j));
                        codeSortDto.setCode(0);
                        codeSortDto.setMaxSampleCode(0);
                        codeSortDto.setSort(0);
                        removeCodeSortDto.add(codeSortDto);
                    }
                    codeSort.addAll(removeCodeSortDto);
                    newCodeSort.addAll(codeSort);
                } else {
                    for (int j = 1; j <= testNum - codeSort.size(); j++) {
                        CodeSortDto lastCodeSort = codeSort.get(codeSort.size() - 1);
                        Integer post = Integer.valueOf(lastCodeSort.getPoint());
                        CodeSortDto codeSortDto = new CodeSortDto();
                        codeSortDto.setPointId(new ObjectId().toString());
                        codeSortDto.setPoint(String.valueOf(post + 1));
                        codeSortDto.setCode(0);
                        codeSortDto.setMaxSampleCode(0);
                        codeSortDto.setSort(0);
                        removeCodeSortDto.add(codeSortDto);
                    }
                    codeSort.addAll(removeCodeSortDto);
                    newCodeSort.addAll(codeSort);
                }

            }
        }

        postPfnEntity.setCodeSort(newCodeSort);
        mongoTemplate.save(postPfnEntity);

        ProjectCountEntity projectCountEntity = projectCountService.getOneByProjectId(postPfnEntity.getProjectId());
        if (projectCountEntity != null){
            if (StringUtils.isNotNull(projectCountEntity.getSurvey())){
                projectCountEntity.setSurveyLast(new Date());
                projectCountService.updateById(projectCountEntity);
            }else {
                projectCountEntity.setSurvey(new Date());
                projectCountService.updateById(projectCountEntity);
            }
        }else {
            ProjectCountEntity projectCount = new ProjectCountEntity();
            projectCount.setProjectId(postPfnEntity.getProjectId());
            projectCount.setSurvey(new Date());
            projectCountService.save(projectCount);
        }

        PostPfnVo postPfnVo2 = this.returnPostPfn(postPfnEntity, null);
        return postPfnVo2;
    }


    @Transactional(rollbackFor = Exception.class)
    public PostPfnVo webSave(PostPfnVo postPfnVo) {

        if (StringUtils.isBlank(postPfnVo.getId())) {
            PostPfnVo newPostPfnVo = this.insertPostPfn(postPfnVo);
            postPfnVo.setWorkshopArea(newPostPfnVo.getWorkshopArea());
            postPfnVo.setWorkshop(newPostPfnVo.getWorkshop());
            postPfnVo.setId(newPostPfnVo.getId());
//            String id = new ObjectId().toString();
//            postPfnEntity.setId(id);
            postPfnVo.setWorkshopId(newPostPfnVo.getWorkshopId());
            postPfnVo.setAreaId(newPostPfnVo.getAreaId());
            postPfnVo.setPost(postPfnVo.getPost());
            postPfnVo.setCreateTime(newPostPfnVo.getCreateTime());
            if (postPfnVo.getIsFixed().equals("1") || postPfnVo.getIsFixed() == 1) {
//                System.out.println("=======");
                if (postPfnVo.getWorkTrackVos() != null && postPfnVo.getWorkTrackVos().size() > 0) {
                    WorkTrackVo workTrackVo = postPfnVo.getWorkTrackVos().get(0);
                    workTrackVo.setPostId(newPostPfnVo.getWorkTrackVos().get(0).getPostId());
                }
            }
        }
        PostPfnEntity postPfnEntity = new PostPfnEntity();
        postPfnEntity = ObjectConversion.copy(postPfnVo, PostPfnEntity.class);

        workspaceService.updateWorkShop(postPfnVo.getWorkshopId(), postPfnVo.getWorkshop());


        Integer testNum = Integer.valueOf(postPfnVo.getTestNum());
        postPfnEntity.setTestNum(testNum);
        postPfnEntity.setUpdateTime(new Date());
        if (postPfnVo.getWorkTrackVos() != null && postPfnVo.getWorkTrackVos().size() > 0) {
            List<WorkTrackVo> workTrackVoList = postPfnVo.getWorkTrackVos();
            Map<String, WorkTrackDto> workTrackDtoMap = this.updateWorkSpeace(workTrackVoList, postPfnVo);
            postPfnEntity.setWorkTrack(workTrackDtoMap);
            List<String> oldPostIds = postPfnEntity.getPostIds();
            List<String> postIds = new ArrayList<>();
            for (String key : workTrackDtoMap.keySet()) {
                postIds.add(key);
            }
            if (postIds.size() > 0) {
                oldPostIds.removeAll(postIds);
            }
            if (oldPostIds != null && oldPostIds.size() > 0) {
                for (String id : oldPostIds) {
                    WorkspaceEntity workspaceEntity = workspaceService.getOneById(id);
                    List<String> pfnIds = workspaceEntity.getPfnIds();
                    Long count = workspaceService.getFiexdCount(workspaceEntity.getWorkshopId());
                    if (pfnIds != null && pfnIds.size() > 0) {
                        pfnIds.remove(postPfnEntity.getId());
                        if (pfnIds.size() > 0) {
                            workspaceService.updateWorkspace(workspaceEntity);
                        } else if (!(count > 0)) {
                            workspaceService.updateWorkspace(workspaceEntity);
                        } else {
                            WorkspaceEntity workspaceEntity1 = workspaceService.getNeedDelete(workspaceEntity.getWorkshopId());
                            if (workspaceEntity1 != null) {
                                PostPfnEntity postPfnEntity1 = this.getOneById(workspaceEntity.getFixedPfnId());
                                postPfnEntity1.setAreaId(workspaceEntity.getAreaId());
                                mongoTemplate.save(postPfnEntity1);
                            }
                            workspaceService.deleteWorkspace(id);
                        }
                    } else if (!(count > 0)) {
                        continue;
                    } else {
                        WorkspaceEntity workspaceEntity1 = workspaceService.getNeedDelete(workspaceEntity.getWorkshopId());
                        if (workspaceEntity1 != null) {
                            PostPfnEntity postPfnEntity1 = this.getOneById(workspaceEntity1.getFixedPfnId());
                            postPfnEntity1.setAreaId(workspaceEntity.getAreaId());
                            mongoTemplate.save(postPfnEntity1);
                        }
                        workspaceService.deleteWorkspace(id);
                    }
                }
            }
            if (postIds.size() > 0) {
                for (String id : postIds) {
                    WorkspaceEntity workspaceEntity = workspaceService.getOneById(id);
                    Long count = workspaceService.getFiexdCount(workspaceEntity.getWorkshopId());
                    if (!(count > 0)) {
                        continue;
                    } else {
                        WorkspaceEntity workspaceEntity1 = workspaceService.getNeedDelete(workspaceEntity.getWorkshopId());
                        if (workspaceEntity1 != null) {
                            PostPfnEntity postPfnEntity1 = this.getOneById(workspaceEntity.getFixedPfnId());
                            postPfnEntity1.setAreaId(workspaceEntity.getAreaId());
                            mongoTemplate.save(postPfnEntity1);
                            workspaceService.deleteWorkspace(workspaceEntity1.getId());
                        }
                    }
                }
            }
            postPfnEntity.setPostIds(postIds);
        }

        List<CodeSortDto> newCodeSort = new ArrayList<>();
        List<CodeSortDto> codeSort = postPfnVo.getCodeSort();
        List<CodeSortDto> removeCodeSortDto = new ArrayList<>();
        if (testNum == 1) {
//            if (codeSort.size()>0){
            if (codeSort.size() == 1) {
                newCodeSort.add(codeSort.get(0));
//                    System.out.println(11111);
            } else {
//                    System.out.println(2222);
                CodeSortDto codeSortDto = new CodeSortDto();
                codeSortDto.setPointId(new ObjectId().toString());
                codeSortDto.setPoint("1");
                codeSortDto.setCode(0);
                codeSortDto.setMaxSampleCode(0);
                codeSortDto.setSort(0);
                newCodeSort.add(codeSortDto);
            }
//            }
        } else if (testNum > 1) {
            if (codeSort.size() >= testNum) {
                for (int j = 1; j <= codeSort.size() - postPfnEntity.getTestNum(); j++) {
//                    codeSort.remove(codeSort.size()-j);
                    removeCodeSortDto.add(codeSort.get(codeSort.size() - j));
                }
                codeSort.removeAll(removeCodeSortDto);
                newCodeSort.addAll(codeSort);
            } else if (codeSort.size() < testNum) {
                if (codeSort.size() == 0) {
                    for (int j = 1; j <= testNum; j++) {
//                        CodeSortDto lastCodeSort = codeSort.get(codeSort.size()-1);
//                        Integer post = 0;
                        CodeSortDto codeSortDto = new CodeSortDto();
                        codeSortDto.setPointId(new ObjectId().toString());
                        codeSortDto.setPoint(String.valueOf(j));
                        codeSortDto.setCode(0);
                        codeSortDto.setMaxSampleCode(0);
                        codeSortDto.setSort(0);
                        removeCodeSortDto.add(codeSortDto);
                    }
                    codeSort.addAll(removeCodeSortDto);
                    newCodeSort.addAll(codeSort);
                } else {
                    for (int j = 1; j <= testNum - codeSort.size(); j++) {
                        CodeSortDto lastCodeSort = codeSort.get(codeSort.size() - 1);
                        Integer post = Integer.valueOf(lastCodeSort.getPoint());
                        CodeSortDto codeSortDto = new CodeSortDto();
                        codeSortDto.setPointId(new ObjectId().toString());
                        codeSortDto.setPoint(String.valueOf(post + 1));
                        codeSortDto.setCode(0);
                        codeSortDto.setMaxSampleCode(0);
                        codeSortDto.setSort(0);
                        removeCodeSortDto.add(codeSortDto);
                    }
                    codeSort.addAll(removeCodeSortDto);
                    newCodeSort.addAll(codeSort);
                }

            }
        }

        postPfnEntity.setCodeSort(newCodeSort);

        mongoTemplate.save(postPfnEntity);


        ProjectCountEntity projectCountEntity = projectCountService.getOneByProjectId(postPfnEntity.getProjectId());
        if (projectCountEntity != null){
            if (StringUtils.isNotNull(projectCountEntity.getSurvey())){
                projectCountEntity.setSurveyLast(new Date());
                projectCountService.updateById(projectCountEntity);
            }else {
                projectCountEntity.setSurvey(new Date());
                projectCountService.updateById(projectCountEntity);
            }
        }else {
            ProjectCountEntity projectCount = new ProjectCountEntity();
            projectCount.setProjectId(postPfnEntity.getProjectId());
            projectCount.setSurvey(new Date());
            projectCountService.save(projectCount);
        }

        PostPfnVo postPfnVo2 = this.returnPostPfn(postPfnEntity, null);
        return postPfnVo2;
    }


    /**
     * 删除
     *
     * @param postPfnVo
     */
    public void deletePostPfn(PostPfnVo postPfnVo) {
        String pfnId = postPfnVo.getId();
        Query query = new Query(Criteria.where("_id").is(pfnId));

        List<WorkspaceEntity> workspaceEntityList = workspaceService.getListByFixedPostId(pfnId);
        for (WorkspaceEntity workspaceEntity : workspaceEntityList) {
//            System.out.println(workspaceEntity.getPost());
            List<String> pfnIds = workspaceEntity.getPfnIds();
            pfnIds.remove(pfnId);
            if (pfnIds.size() > 0) {
                List<IdentifyHazardsDto> identifyHazardsList = workspaceEntity.getIdentifyHazards();
                List<IdentifyHazardsDto> removeIdentifyHazardsList = new ArrayList<>();
                if (identifyHazardsList.size() > 0) {
                    for (IdentifyHazardsDto identifyHazardsDto : identifyHazardsList) {
                        List<String> oldPfnIds = identifyHazardsDto.getPfnIds();
                        if (oldPfnIds.contains(pfnId)) {
                            oldPfnIds.remove(pfnId);
                            if (oldPfnIds.size() > 0) {
                                identifyHazardsDto.setPfnIds(oldPfnIds);
                            } else {
                                removeIdentifyHazardsList.add(identifyHazardsDto);
                            }
                        }
                    }
                    identifyHazardsList.removeAll(removeIdentifyHazardsList);
                    List<String> onlyNameList = new ArrayList<>();
                    String onlyIdentify = "";
                    for (IdentifyHazardsDto identifyHazardsDto : identifyHazardsList) {
                        String substanceName = "";
                        if (StringUtils.isNotBlank(identifyHazardsDto.getAlias())) {
                            substanceName = identifyHazardsDto.getName() + "(" + identifyHazardsDto.getAlias() + ")";
                            onlyNameList.add(substanceName);
                        } else {
                            substanceName = identifyHazardsDto.getName();
                            onlyNameList.add(substanceName);
                        }
                    }
                    onlyIdentify = onlyNameList.stream().collect(Collectors.joining("、"));
                    workspaceEntity.setOnlyIdentify(onlyIdentify);
                    workspaceEntity.setIdentifyHazards(identifyHazardsList);
                }
                List<IdentifyHazardsDto> fixedHazardsList = workspaceEntity.getFixedHazards();
                List<IdentifyHazardsDto> removeFixedHazardsList = new ArrayList<>();
                if (fixedHazardsList.size() > 0) {
                    for (IdentifyHazardsDto identifyHazardsDto : fixedHazardsList) {
                        List<String> oldPfnIds = identifyHazardsDto.getPfnIds();
                        if (oldPfnIds.contains(pfnId)) {
                            oldPfnIds.remove(pfnId);
                            if (oldPfnIds.size() > 0) {
                                identifyHazardsDto.setPfnIds(oldPfnIds);
                            } else {
                                removeFixedHazardsList.add(identifyHazardsDto);
                            }
                        }
                    }
                    fixedHazardsList.removeAll(removeFixedHazardsList);
                    List<String> fixedNameList = new ArrayList<>();
                    String testHazards = "";
                    for (IdentifyHazardsDto identifyHazardsDto : fixedHazardsList) {
                        String substanceName = "";
                        if (StringUtils.isNotBlank(identifyHazardsDto.getAlias())) {
                            substanceName = identifyHazardsDto.getName() + "(" + identifyHazardsDto.getAlias() + ")";
                            fixedNameList.add(substanceName);
                        } else {
                            substanceName = identifyHazardsDto.getName();
                            fixedNameList.add(substanceName);
                        }
                    }
                    testHazards = fixedNameList.stream().collect(Collectors.joining("、"));
                    workspaceEntity.setTestHazards(testHazards);
                    workspaceEntity.setFixedHazards(fixedHazardsList);
                }
                Map<String, List<String>> pfnPoints = workspaceEntity.getPfnPoints();
                List<String> removePointIds = new ArrayList<>();
                if (StringUtils.checkValNotNull(pfnPoints)) {
                    List<String> pfnPostIds = pfnPoints.get(pfnId);
                    Map<String, PointMapDto> pointMap = workspaceEntity.getPointMap();
                    for (String pfnPointId : pfnPostIds) {
                        PointMapDto pointMapDto = pointMap.get(pfnPointId);
                        List<String> pointMaoPfnIds = pointMapDto.getPfnIds();
                        if (pointMaoPfnIds.contains(pfnId)) {
                            pointMaoPfnIds.remove(pfnId);
                            if (pointMaoPfnIds.size() > 0) {
                                pointMapDto.setPfnIds(pointMaoPfnIds);
                            } else {
                                pointMap.remove(pfnPointId, pointMapDto);
                                removePointIds.add(pfnPointId);
                            }
                        }
                    }
                    workspaceEntity.setPointMap(pointMap);

                    if (removePointIds.size() > 0) {
                        List<String> pointIds = workspaceEntity.getPointIds();
                        pointIds.removeAll(removePointIds);
                        workspaceEntity.setPointIds(pointIds);
                    }
                }
                workspaceEntity.setPfnIds(pfnIds);
                workspaceService.updateWorkspace(workspaceEntity);
            } else {
                Long count = workspaceService.getListByWorkShopId(workspaceEntity.getWorkshopId());
                List<PostPfnEntity> list = this.getListByWorkShopId(workspaceEntity.getWorkshopId());
                if ("1".equals(String.valueOf(count)) && list.size() >= 1) {
//                    System.out.println("====================");
                    workspaceEntity.setSilicaShare("0");
                    workspaceEntity.setPost("");
                    workspaceEntity.setFixedPfnId(list.get(0).getId());
                    workspaceEntity.setOnlyIdentify("");
                    workspaceEntity.setIdentifyHazards(new ArrayList<>());
                    workspaceEntity.setTestHazards("");
                    workspaceEntity.setFixedHazards(new ArrayList<>());
                    workspaceEntity.setPointNum(0);
                    workspaceEntity.setPfnIds(new ArrayList<>());
                    workspaceEntity.setPointMap(new HashMap<>());
                    workspaceEntity.setPointIds(new ArrayList<>());
                    workspaceEntity.setPfnPoints(new HashMap<>());
                    workspaceEntity.setEpe(new ArrayList<>());
//                    workspaceEntity.setEpeRun("");
                    workspaceEntity.setUpdateTime(new Date());
                    workspaceService.updateWorkspace(workspaceEntity);
                } else {
                    workspaceService.deleteWorkspace(workspaceEntity.getId());
                }

            }
        }
        mongoTemplate.remove(query, PostPfnEntity.class);
    }


    /**
     * 批量删除
     *
     * @param postPfnVos
     */
    public void deleteBatchPostPfn(List<PostPfnVo> postPfnVos) {
        for (PostPfnVo postPfnVo : postPfnVos) {
            this.deletePostPfn(postPfnVo);
        }
    }


    /**
     * 根据ID 获取一条
     *
     * @param id
     * @return
     */
    public PostPfnEntity getOneById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        PostPfnEntity postPfnEntity = mongoTemplate.findOne(query, PostPfnEntity.class);
        return postPfnEntity;
    }


    public List<PostPfnEntity> getListByWorkShopId(String workshopId) {
        ObjectId objectId = new ObjectId(workshopId);
        Query query = new Query(Criteria.where("workshop_id").is(objectId));
        List<PostPfnEntity> list = mongoTemplate.find(query, PostPfnEntity.class);
        return list;
    }


    /**
     * 获取工种作业总人数
     *
     * @param projectId
     * @return
     */
    public Map<String, Double> getPeopleNumByProjectId(Long projectId) {
        Query query = new Query(Criteria.where("project_id").is(projectId));
        List<PostPfnEntity> postPfnEntities = mongoTemplate.find(query, PostPfnEntity.class);
        Map<String, Double> map = new HashMap<>();
        Double num = new Double("0");
        Double day = new Double("0");

        if (postPfnEntities.size() > 0) {
            List<String> dayList = postPfnEntities.stream().map(PostPfnEntity::getWorkingDays).distinct().collect(Collectors.toList());
            List<Double> days = dayList.stream().filter(i->StringUtils.isNotBlank(i)).map(Double::valueOf).collect(Collectors.toList());
            day = days.stream().max(Comparator.comparing(x -> x)).orElse(Double.valueOf("0"));
            num = postPfnEntities.stream().mapToDouble(PostPfnEntity::getPeopleNum).sum();
        }
        map.put("num", num);
        map.put("day", day);
        return map;
    }


    public List<String> getPfnList(Long projectId, Long protectionId) {
        List<String> pfnIdList = new ArrayList<>();
        List<WorkspaceVo> workspaceEntityList = workspaceService.findAllList(projectId);
        List<String> idStringList = workspaceEntityList.stream().map(WorkspaceVo::getId).distinct().collect(Collectors.toList());
        List<PostPfnEntity> postPfnEntityList = new ArrayList<>();
        for (String id : idStringList) {
            Query query = new Query(Criteria.where("project_id").is(projectId));
            query.addCriteria(Criteria.where("work_track" + id + "ppe.$[].protection_id").is(protectionId));
            List<PostPfnEntity> pfnEntityList = mongoTemplate.find(query, PostPfnEntity.class);
            if (pfnEntityList.size() > 0) {
                postPfnEntityList.addAll(pfnEntityList);
            }
        }
        if (postPfnEntityList.size() > 0) {
            pfnIdList = postPfnEntityList.stream().map(PostPfnEntity::getId).distinct().collect(Collectors.toList());
        }
        return pfnIdList;
    }


    public List<ProjectPutVo> getallprojectId() {
        List<PostPfnEntity> list = mongoTemplate.findAll(PostPfnEntity.class);
//        list.forEach(System.out::println);
        List<Long> projectIdList = list.stream().map(PostPfnEntity::getProjectId).distinct().collect(Collectors.toList());
//        System.out.println(projectIdList);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("p.id", projectIdList);
        List<ProjectPutVo> putList = projectMapper.getalllist(queryWrapper);
        return putList;
    }

    /**
     * 获取xin车间信息
     *
     * @return
     */
    public PostPfnVo selectPostPfnVo(String id) {
        // todo mongoTemplate.find()
        Query query = new Query(Criteria.where("_id").is(id));
        PostPfnEntity pfnEntity = mongoTemplate.findOne(query, PostPfnEntity.class);
        // 将Mongodb中的PostPfnEntity对象 转化为PostPfnVo对象
        PostPfnVo postPfnVo = returnPostPfn(pfnEntity, null);
//        System.out.println("+++++++++++++++++++++" + postPfnVo);
        return postPfnVo;
    }


    /**
     * 送样后 调查信息修改内容调整
     */
    public Integer sample(PostPfnVo postPfnVo) {
        String id = postPfnVo.getId();
        // 原数据信息
        PostPfnVo oldPostPfnVO = selectPostPfnVo(id);
        Integer a = 0;
        // 项目id
        Long projectId = postPfnVo.getProjectId();
        List<AlDeliverReceived> list = alDeliverReceivedService.alDeliverReceived(String.valueOf(projectId));
//        System.out.println("id = " + id);
        // 新物质信息
        List<WorkTrackVo> workTrackVos = postPfnVo.getWorkTrackVos();
        // 原物质信息
        List<WorkTrackVo> workTrackVos1 = oldPostPfnVO.getWorkTrackVos();
        // 新个体点位信息
        List<PointMapDto> pointMapDtos = new ArrayList<>();
        List<List<PointMapDto>> pointPosition = workTrackVos.stream().map(WorkTrackVo::getPointMapList).collect(Collectors.toList());
        pointPosition.forEach(pointPosition1 -> {
            pointMapDtos.addAll(pointPosition1);
        });
        // 原个体点位信息
        List<PointMapDto> pointMapDtos2 = new ArrayList<>();
        List<List<PointMapDto>> pointPosition1 = workTrackVos1.stream().map(WorkTrackVo::getPointMapList).collect(Collectors.toList());
        pointPosition1.forEach(pointPosition2 -> {
            pointMapDtos2.addAll(pointPosition2);
        });
        // 新个体采样信息
        Map<String, TouchHazardsDto> soloHazards = postPfnVo.getSoloHazards();
        // Java8 Stream
        List<TouchHazardsDto> soloHazardList = soloHazards.values().stream().collect(Collectors.toList());
        List<Integer> individualTesting = soloHazardList.stream().map(TouchHazardsDto::getTestType).collect(Collectors.toList());
        // 原个体采样信息
        Map<String, TouchHazardsDto> soloHazards1 = oldPostPfnVO.getSoloHazards();
        List<TouchHazardsDto> soloHazardList1 = soloHazards1.values().stream().collect(Collectors.toList());
        List<Integer> individualTesting1 = soloHazardList1.stream().map(TouchHazardsDto::getTestType).collect(Collectors.toList());
        // 新样品数信息
        List<Integer> sampleNumList = new ArrayList<>();
        List<String> sampleName = new ArrayList<>();
        List<String> sampleName1 = new ArrayList<>();
        List<Integer> detectionMethods = new ArrayList<>();
        List<Integer> detectionMethods1 = new ArrayList<>();
        List<List<TouchHazardsDto>> collectTouchHazardsDto = workTrackVos.stream().map(WorkTrackVo::getTouchHazardsList).collect(Collectors.toList());
        collectTouchHazardsDto.stream().forEach(list1 -> {
            List<Integer> integers = list1.stream().map(TouchHazardsDto::getSampleNum).collect(Collectors.toList());
            // 新物质名称
            List<String> name = list1.stream().map(TouchHazardsDto::getName).collect(Collectors.toList());
            sampleName.addAll(name);
            // 原检测方式
            List<Integer> detectionMethod = list1.stream().map(TouchHazardsDto::getTestType).collect(Collectors.toList());
            detectionMethods.addAll(detectionMethod);
            sampleNumList.addAll(integers);
        });
        // 原样品数信息
        List<Integer> sampleNumList2 = new ArrayList<>();
        List<List<TouchHazardsDto>> collectTouchHazardsDto1 = workTrackVos1.stream().map(WorkTrackVo::getTouchHazardsList).collect(Collectors.toList());
        collectTouchHazardsDto1.forEach(list2 -> {
            List<Integer> integers2 = list2.stream().map(TouchHazardsDto::getSampleNum).collect(Collectors.toList());
            // 原物质名称
            List<String> name1 = list2.stream().map(TouchHazardsDto::getName).collect(Collectors.toList());
            // 原检测方式
            List<Integer> detectionMethod1 = list2.stream().map(TouchHazardsDto::getTestType).collect(Collectors.toList());
            detectionMethods1.addAll(detectionMethod1);
            sampleName1.addAll(name1);

            sampleNumList2.addAll(integers2);
        });
        // 原个体数
        String testNum = oldPostPfnVO.getTestNum();
        // 新个体数
        String testNum1 = postPfnVo.getTestNum();
        // 判断送样状态
        if (!postPfnVo.getWorkshop().equals(oldPostPfnVO.getWorkshop()) && list != null && !list.isEmpty()) {
            a = 1;
            return a;
            // 点位信息
        } else if (StringUtils.isNotEmpty(list)
                && !CollectionUtils.isEqualCollection(pointMapDtos, pointMapDtos2)) {
            a = 2;
            return a;
            // 物质信息
        } else if (!testNum.equals(testNum1) && StringUtils.isNotEmpty(list)) {
            // 个体采样
            a = 4;
            return a;
        } else if (!CollectionUtils.isEqualCollection(individualTesting, individualTesting1) && StringUtils.isNotEmpty(list)) {
            // 个体采样-检测方式
            a = 4;
            return a;
        } else if (!CollectionUtils.isEqualCollection(sampleNumList, sampleNumList2) && StringUtils.isNotEmpty(list)) {
            // 样品数
            a = 5;
            return a;
            // 工种信息
        } else if (StringUtils.isNotEmpty(list)
                && !postPfnVo.getPfn().equals(oldPostPfnVO.getPfn())) {
            a = 6;
            return a;
        } else if (!CollectionUtils.isEqualCollection(sampleName, sampleName1) && StringUtils.isNotEmpty(list)) {
            // 物质信息 危害因素
            a = 3;
            return a;
        } else if (!CollectionUtils.isEqualCollection(detectionMethods, detectionMethods1) && StringUtils.isNotEmpty(list)) {
            // 物质信息 检测方式
            a = 3;
            return a;
        }else if (!postPfnVo.getPost().equals(oldPostPfnVO.getPost()) && StringUtils.isNotEmpty(list)) {
            // 物质信息 检测方式
            a = 6;
            return a;
        }
        return a;
    }

    /**
     * 判断送样状态
     *
     * @param postPfnVo
     * @return
     */
    public int sampleSendingStatus(PostPfnVo postPfnVo) {
        Integer a = 0;
        // 项目id
        Long projectId = postPfnVo.getProjectId();
        List<AlDeliverReceived> list = alDeliverReceivedService.alDeliverReceived(String.valueOf(projectId));
        if (list != null && !list.isEmpty()) {
            a = 1;
        }
        return a;
    }


    /**
     * 前端获取手否送样及物理发送
     * @param projectId
     * @return
     */
    public Map<String,Boolean> isDeliverAndPhysicalSend(Long projectId){
        boolean a = projectDateService.isPhysicalSend(projectId);
        boolean b = alDeliverReceivedService.isDeliver(projectId);
        Map<String,Boolean> map = new HashMap<>();
        map.put("isDeliver",b);
        map.put("isPhysicalSend",a);
        return map;
    }

    /**
     * 判断危害因素是否缺少游离二氧化硅
     * @param postPfnVo
     * @return
     */
    public boolean isLackOfSilica(PostPfnVo postPfnVo) {
        //获取该工种检测物质轨迹信息
        List<WorkTrackVo> workTrackVos = postPfnVo.getWorkTrackVos();
        if(workTrackVos == null){
            return true;
        }
        for (WorkTrackVo workTrackVo : workTrackVos) {
            //获取该工种各个岗位的 轨迹物质列表 (识别检测物质)
            List<TouchHazardsDto> touchHazardsList = workTrackVo.getTouchHazardsList();
            if(touchHazardsList == null) {
                continue;
            }
            //该岗位是否有需要 根据游离二氧化硅判定(isSilica == 1) 的危害因素
            List<TouchHazardsDto> collect = touchHazardsList.stream().filter(i -> i.getIsSilica() == 1).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)){
                //有，则该岗位需要检测的危害因素里要含有游离二氧化硅,且样品数量要大于0
                List<TouchHazardsDto> collect1 = touchHazardsList.stream().filter(i -> "游离二氧化硅".equals(i.getName()) && i.getSampleNum() > 0).collect(Collectors.toList());
                if(CollectionUtils.isEmpty(collect1)){
                    return false;
                }
            }
        }
        return true;
    }
}
