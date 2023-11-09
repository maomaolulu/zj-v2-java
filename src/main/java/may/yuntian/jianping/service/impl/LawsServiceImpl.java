package may.yuntian.jianping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.jianping.entity.LawsEntity;
import may.yuntian.jianping.entity.LawsProjectEntity;
import may.yuntian.jianping.mapper.LawsMapper;
import may.yuntian.jianping.service.LawsProjectService;
import may.yuntian.jianping.service.LawsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("lawsService")
public class LawsServiceImpl extends ServiceImpl<LawsMapper, LawsEntity> implements LawsService {

    @Autowired
    private LawsProjectService lawsProjectService;

    /**
     * 条件查询法律法规
     * @param params
     * @return
     */
    @Override
    public List<LawsEntity> list(Map<String, Object> params) {
        String lawName = (String)params.get("lawName");
        String referenceNum = (String)params.get("referenceNum");

        List<LawsEntity> list = this.list(new QueryWrapper<LawsEntity>()
                .eq(StringUtils.isNotBlank(lawName), "law_name", lawName)
                .eq(StringUtils.isNotBlank(referenceNum), "reference_name", referenceNum)
        );
        return list;
    }

    /**
     *根据项目id获取法律法规
     * @param projectId
     * @return
     */
    @Override
    public List<LawsEntity> getListByProjectId(Long projectId) {
        List<LawsEntity> lawList = this.list();	//全部法律依据
        List<LawsProjectEntity> selectLawList = lawsProjectService.list(new QueryWrapper<LawsProjectEntity>().eq("project_id", projectId));	//已经选中的法律依据

        if(selectLawList.size()==0) {	//初始化数据时，默认选中的是默认值
            lawList.forEach(action->{
                Integer selectValue = action.getDefaultSelected();
                action.setSelectValue(selectValue);
            });
        }else {
            lawList.forEach(action->{
                selectLawList.forEach(sl->{
                    if(action.getId() == sl.getLawId()) {
                        action.setSelectValue(1);	//被选中的值
                    }
                });
            });
        }
        return lawList;
    }

    /**
     * 根据项目id添加对应法律法规
     * @param projectId
     * @param ids
     */
    @Override
    public void addLawsProject(Long projectId, Long[] ids) {
        lawsProjectService.remove(new QueryWrapper<LawsProjectEntity>().eq("project_id", projectId));

        ArrayList<LawsProjectEntity> list = new ArrayList<>();
        LawsProjectEntity lawsProjectEntity;
        for (Long l : ids) {
            lawsProjectEntity=new LawsProjectEntity();
            lawsProjectEntity.setProjectId(projectId);
            lawsProjectEntity.setLawId(l);

            list.add(lawsProjectEntity);
        }

        lawsProjectService.saveBatch(list);
    }


}
