package may.yuntian.commission.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.service.ProjectDateService;
import may.yuntian.anlian.service.ProjectService;
import may.yuntian.commission.entity.PerCommissionEntity;
import may.yuntian.commission.mapper.PerCommissionMapper;
import may.yuntian.commission.service.PerCommissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: ANLIAN-JAVA
 * @description:
 * @author: liyongqiang
 * @create: 2022-05-30 15:38
 */
@Service("perCommissionService")
@SuppressWarnings("all")
public class PerCommissionServiceImpl extends ServiceImpl<PerCommissionMapper, PerCommissionEntity> implements PerCommissionService {



    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectDateService projectDateService;




    /**
     * 根据检评绩效分配ID获取列表信息
     * @param performanceAllocationId
     * @return
     */
    public List<PerCommissionEntity> getListByPerformanceAllocationId(Long performanceAllocationId,Long projectId){
        List<PerCommissionEntity> perCommissionList = baseMapper.selectList(new QueryWrapper<PerCommissionEntity>()
            .eq("project_id",projectId)
            .eq("performance_allocation_id",performanceAllocationId)
            .eq("state",1)
        );
        return perCommissionList;
    }


    /**
     * 根据检评绩效分配ID获取信息
     * @param performanceAllocationId
     * @return
     */
    public PerCommissionEntity getByPerformanceAllocationId(Long performanceAllocationId,Long projectId,String name){
        PerCommissionEntity perCommission = baseMapper.selectOne(new QueryWrapper<PerCommissionEntity>()
                .eq("project_id",projectId)
                .eq("performance_allocation_id",performanceAllocationId)
                .eq("personnel",name)
        );
        return perCommission;
    }


    /**
     * g根据提成人查询
     * @param personnel
     * @return
     */
    public List<Long> getListByPersonnel(String personnel){
        List<PerCommissionEntity> list = baseMapper.selectList(new QueryWrapper<PerCommissionEntity>().eq("personnel",personnel));
        List<Long> projectIds = new ArrayList<>();
        if (list.size()>0){
            projectIds = list.stream().map(PerCommissionEntity::getProjectId).distinct().collect(Collectors.toList());
        }

        return projectIds;
    }


    /**
     * 检评绩效分配页面所需获取列表接口
     * @param performanceAllocationId
     * @param projectId
     * @param type
     * @return
     */
    public List<PerCommissionEntity> getListByIdAndType(Long performanceAllocationId, Long projectId, String type){
        List<PerCommissionEntity> perCommissionList = baseMapper.selectList(new QueryWrapper<PerCommissionEntity>()
                .eq("project_id",projectId)
                .eq("performance_allocation_id",performanceAllocationId)
                .eq("type",type)
        );
        return perCommissionList;
    }


}
