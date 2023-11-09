package may.yuntian.jianping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.jianping.entity.StandardsEntity;
import may.yuntian.jianping.entity.StandardsProjectEntity;
import may.yuntian.jianping.mapper.StandardsMapper;
import may.yuntian.jianping.service.StandardsProjectService;
import may.yuntian.jianping.service.StandardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("standardsService")
public class StandardsServiceImpl extends ServiceImpl<StandardsMapper, StandardsEntity> implements StandardsService {
    @Autowired
    private StandardsProjectService standardsProjectService;

    @Override
    public List<StandardsEntity> getList(Long projectId) {
        //获取全部法律法规
        List<StandardsEntity> list = this.list();
        List<StandardsProjectEntity> entityList = standardsProjectService.list(new QueryWrapper<StandardsProjectEntity>().eq("project_id", projectId ));


        //未选中任何法律法规
        if (entityList.size()==0){
            list.forEach(v->{
                Integer isCheck = v.getIsCheck();
                v.setIsCheck(isCheck);
            }) ;
        }else {
            list.forEach(v->{
                entityList.forEach(v2->{
                    if (v.getId()==v2.getStandardId()){
                        v.setIsCheck(1);
                    }
                });
            });
        }
        return list;
    }

    @Override
    public Map<String, Object> getStandardsAll(Long projectId) {
        // 已选中的 常用法律法规
        List<StandardsEntity> testBasisList = this.list(new QueryWrapper<StandardsEntity>().in("industry_name", "基本"));	//无类型法律依据
        List<StandardsProjectEntity> selectLawList = standardsProjectService.list(new QueryWrapper<StandardsProjectEntity>().eq("project_id", projectId));	//已经选中的法律依据

        if(selectLawList.size()==0) {	//初始化数据时，默认选中的是默认值
            testBasisList.forEach(action->{
                Integer selectValue = action.getIsCheck();
                action.setSelectValue(selectValue);
            });
        }else {
            testBasisList.forEach(action->{
                selectLawList.forEach(sl->{
                    if(action.getId() == sl.getStandardId()) {
                        action.setSelectValue(1);	//被选中的值
                    }
                });
            });
        }
        //已选中的其他和有类型的法律法规
        List<Long> collect = testBasisList.stream().distinct().map(StandardsEntity::getId).collect(Collectors.toList());
        List<StandardsProjectEntity> testBasisEntities = standardsProjectService.list(new QueryWrapper<StandardsProjectEntity>().eq("project_id", projectId).notIn("standard_id",collect));	//已经选中的法律依据
        List<StandardsEntity> selectLawList2 = new ArrayList<>();
        if(testBasisEntities!=null&&testBasisEntities.size()>0){
            List<Long> collect1 = testBasisEntities.stream().distinct().map(StandardsProjectEntity::getStandardId).collect(Collectors.toList());
            selectLawList2 = this.list(new QueryWrapper<StandardsEntity>().in("id",collect1));
        }




        //有类型除其他的法律法规
        List<StandardsEntity> testBasisList2 = this.list(new QueryWrapper<StandardsEntity>().notIn("industry_name","基本"));
        Map<String,List<StandardsEntity>> map = testBasisList2.stream().collect(Collectors.groupingBy(StandardsEntity::getIndustryName));
        map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByKey()));

        //其他 法律法规

        List<StandardsEntity> testBasisList3 = this.list(new QueryWrapper<StandardsEntity>().isNotNull("industry_name").eq("industry_name","其它"));

        Map<String, Object> map1 = new HashMap<>();

        map1.put("list1",testBasisList);//基本
        map1.put("list2",testBasisList3);//其他的
        map1.put("list3",selectLawList2);//已选择的
        map1.put("map",map);//有类型除其他的法律法规

        return map1;
    }

    @Override
    public void updateStandardsProject(Long projectId, Long[] ids) {
        //首先删除旧的数据
        standardsProjectService.remove(new QueryWrapper<StandardsProjectEntity>().eq("project_id", projectId));

        //赋值新的存储对象
        List<StandardsProjectEntity> selectLawList = new ArrayList<>();
        StandardsProjectEntity lawc ;
        for(Long testBasisId : ids) {
            lawc = new StandardsProjectEntity();
            lawc.setProjectId(projectId);
            lawc.setStandardId(testBasisId);
            selectLawList.add(lawc);
        }

        standardsProjectService.saveBatch(selectLawList);
    }

//    @Override
//    public List<StandardsEntity> listAll(Map<String, Object> params) {
//        String name = (String) params.get("industryName");
//        String standardName = (String) params.get("standardName");
//
//        List<StandardsEntity> list = this.list(new QueryWrapper<StandardsEntity>()
//                .eq(StringUtils.isNotBlank(name), "industry_name", name)
//                .eq(StringUtils.isNotBlank(standardName), "standard_name", standardName)
//        );
//
//        return list;
//    }
}
