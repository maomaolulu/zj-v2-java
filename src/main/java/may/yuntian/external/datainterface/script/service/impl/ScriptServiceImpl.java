package may.yuntian.external.datainterface.script.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import may.yuntian.external.datainterface.script.constant.TimeConstants;
import may.yuntian.external.datainterface.script.dao.ScriptMapper;
import may.yuntian.external.datainterface.script.entity.CeProDetectionSubstanceEntity;
import may.yuntian.external.datainterface.script.service.ScriptService;
import may.yuntian.jianping.entity.SubstanceEntity;
import may.yuntian.jianping.mongoentity.PostPfnEntity;
import may.yuntian.jianping.mongoentity.WorkspaceEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 脚本操作 Service实现层
 *
 * @author cwt
 * @Create 2023-4-13 16:11:36
 */
@Service
@Slf4j
public class ScriptServiceImpl implements ScriptService {

    private final Logger logger = LoggerFactory.getLogger(ScriptServiceImpl.class);

    @Autowired
    private ScriptMapper scriptMapper;

    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * pro_detection_substance  表数据处理
     */
    @Override
    public void tableDataProcessing() {
        // 获取物质的id和name
        List<SubstanceEntity> list = scriptMapper.getIdAndName();
        log.info("=======================" + list.size());
        list.forEach(substanceEntity -> {
            Long id = substanceEntity.getId();
            String name = substanceEntity.getName();
            scriptMapper.updateSubIdAndSubName(name, id);
        });
    }

    /**
     * al_substance_copy_to_pro  表数据处理
     */
    @Override
    public void tableDataProcessing1() {
        // 获取省平台物质表的 id和关联al_物质表的id
        List<CeProDetectionSubstanceEntity> list = scriptMapper.getIdAndSubId();
        log.info("=======================" + list.size());
        list.forEach(proDetectionSubstanceEntity -> {
            Long id = proDetectionSubstanceEntity.getId();
            Long subId = proDetectionSubstanceEntity.getSubId();
            scriptMapper.updatePorIdBySubId(subId, id);
        });
    }


    /**
     * @description zj_workspace脏数据处理
     */
    @Override
    public void mongoTest() {
        String time = TimeConstants.TIME;
        DateTime dateTime = DateUtil.parse(time);
        Query query = Query.query(Criteria.where("create_time").gt(dateTime));
        List<WorkspaceEntity> list = mongoTemplate.find(query, WorkspaceEntity.class);
        logger.info("--------------------------------" + list.size());
        list.forEach(workspaceEntity -> {
            if (workspaceEntity.getFixedHazards() != null) {
                workspaceEntity.getFixedHazards().forEach(identifyHazardsDto -> {
                    Integer hasSolo = identifyHazardsDto.getHasSolo();
                    if (hasSolo != null) {
                        switch (hasSolo) {
                            case 1:
                                identifyHazardsDto.setHasSolo(2);
                                break;
                            case 2:
                                identifyHazardsDto.setHasSolo(1);
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
            mongoTemplate.save(workspaceEntity);
        });

    }

    /**
     * zj_post_pfn脏数据处理
     */
    @Override
    public void mongoTest1() {
        String time = TimeConstants.TIME;
        DateTime dateTime = DateUtil.parse(time);
        Query query = Query.query(Criteria.where("create_time").gt(dateTime));
        List<PostPfnEntity> list = mongoTemplate.find(query, PostPfnEntity.class);
        logger.info("--------------------------------" + list.size());
        list.forEach(pfnEntity -> {
            if (pfnEntity.getWorkTrack() != null) {
                pfnEntity.getWorkTrack().forEach((s, workTrackDto) -> {
                    workTrackDto.getIdentifyHazardsMap().forEach((s1, touchHazardsDto) -> {
                        Integer hasSolo = touchHazardsDto.getHasSolo();
                        if (hasSolo != null) {
                            switch (hasSolo) {
                                case 1:
                                    touchHazardsDto.setHasSolo(2);
                                    break;
                                case 2:
                                    touchHazardsDto.setHasSolo(1);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                    workTrackDto.getTouchHazardsMap().forEach((s1, touchHazardsDto) -> {
                        Integer hasSolo = touchHazardsDto.getHasSolo();
                        if (hasSolo != null) {
                            switch (hasSolo) {
                                case 1:
                                    touchHazardsDto.setHasSolo(2);
                                    break;
                                case 2:
                                    touchHazardsDto.setHasSolo(1);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                });
            }
            mongoTemplate.save(pfnEntity);
        });

    }

}
