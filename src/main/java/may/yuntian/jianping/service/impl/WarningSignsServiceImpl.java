package may.yuntian.jianping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;
import may.yuntian.jianping.entity.WarningSignsEntity;
import may.yuntian.jianping.mapper.WarningSignsMapper;
import may.yuntian.jianping.mongoentity.ResultEntity;
import may.yuntian.jianping.mongoservice.ResultService;
import may.yuntian.jianping.service.WarningSignsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 职业危害警示标识设置一览表
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @data 2021-03-17
 */
@Service("warningSignsService")
public class WarningSignsServiceImpl extends ServiceImpl<WarningSignsMapper, WarningSignsEntity> implements WarningSignsService {

    @Autowired
    private ResultService resultService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WarningSignsEntity> page = this.page(
                new Query<WarningSignsEntity>().getPage(params),
                new QueryWrapper<WarningSignsEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 通过项目id查询职业危害警示标识设置一览表
     *
     * @param projectId
     * @return
     */
    @Override
    public List<WarningSignsEntity> seleteByProjectId(Long projectId) {
        List<WarningSignsEntity> warningSignsList = baseMapper.selectList(new QueryWrapper<WarningSignsEntity>().eq("project_id", projectId));
        return warningSignsList;
    }

    //String string2 = list2.stream().collect(Collectors.joining(","));  list转字符串 用，拼接

    /**
     * 通过项目ID初始化生成职业危害警示标识设置一览列表
     *
     * @param
     */
    @Override
    public void initializeWarningSigns(WarningSignsEntity warningSignsEntity) {

        Long projectId = warningSignsEntity.getProjectId();

        List<WarningSignsEntity> warningSignsEntityList = this.seleteByProjectId(projectId);

        if (warningSignsEntityList != null && warningSignsEntityList.size() > 0) {
            for (WarningSignsEntity wEntity : warningSignsEntityList) {
                baseMapper.deleteById(wEntity);
            }
        }

        Map<String, List<ResultEntity>> map = resultService.getResultLsit(projectId);

        List<WarningSignsEntity> warningSignsList = new ArrayList<>();

        for (String key : map.keySet()) {
            List<ResultEntity> resultEntityList = map.get(key);
            List<String> testItemList = new ArrayList<>();
            List<String> cardList = new ArrayList<>();
            for (ResultEntity resultEntity : resultEntityList) {
                testItemList.add(resultEntity.getSubstance().getName());
                if (resultEntity.getSubstance().getName().equals("噪声") && resultEntity.getIsExceed() == 1) {
                    cardList.add(resultEntity.getSubstance().getName());
                }
                if (resultEntity.getSubstance().getSType() == 1 && resultEntity.getSubstance().getHighlyToxic() == 2) {
                    cardList.add(resultEntity.getSubstance().getName());
                }
            }
            String testItem = testItemList.stream().distinct().collect(Collectors.joining("、"));
            String card = cardList.stream().distinct().collect(Collectors.joining("、"));

            WarningSignsEntity warningSigns = new WarningSignsEntity();
            warningSigns.setPointId(key);
            warningSigns.setProjectId(projectId);
            warningSigns.setPlace(resultEntityList.get(0).getTestPlace());
            warningSigns.setHazardFactors(testItem);
            warningSigns.setCard(card);
            warningSignsList.add(warningSigns);
        }
        this.saveBatch(warningSignsList);
    }

}
