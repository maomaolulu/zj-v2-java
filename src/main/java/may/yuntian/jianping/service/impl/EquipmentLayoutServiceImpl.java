package may.yuntian.jianping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.jianping.entity.EquipmentLayoutEntity;
import may.yuntian.jianping.mapper.EquipmentLayoutMapper;
import may.yuntian.jianping.mongoservice.WorkspaceService;
import may.yuntian.jianping.service.EquipmentLayoutService;
import may.yuntian.jianping.vo.WorkspaceVo;
import may.yuntian.sys.utils.PageUtil2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("equipmentLayoutServiceImpl")
public class EquipmentLayoutServiceImpl extends ServiceImpl<EquipmentLayoutMapper, EquipmentLayoutEntity> implements EquipmentLayoutService {

    @Autowired
    private WorkspaceService workspaceService;

    /**
     * 根据项目id获取到设备布局列表
     * @param projectId
     * @return
     */
    @Override
    public List<EquipmentLayoutEntity> getList(Long projectId) {

        QueryWrapper<EquipmentLayoutEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_id", projectId);
        PageUtil2.startPage();
        List<EquipmentLayoutEntity> list = baseMapper.selectList(queryWrapper);

        return list;
    }

    public void init(EquipmentLayoutEntity layout){
        Long projectIdStr = layout.getProjectId();
        boolean ret = this.notExistLayoutByProject(projectIdStr);
        if (!ret){
            baseMapper.delete(new QueryWrapper<EquipmentLayoutEntity>().eq("project_id", projectIdStr));
        }
        List<WorkspaceVo> workList = workspaceService.findAllList(projectIdStr);
        if (workList.size()>0){
            List<EquipmentLayoutEntity> layoutEntityList = new ArrayList<>();
            for (WorkspaceVo workspaceVo : workList){
                EquipmentLayoutEntity layoutEntity = new EquipmentLayoutEntity();
                layoutEntity.setProjectId(projectIdStr);
                layoutEntity.setWorkshop(workspaceVo.getWorkshop());
                layoutEntity.setPost(workspaceVo.getPost());
                layoutEntity.setPostId(workspaceVo.getId());
                layoutEntityList.add(layoutEntity);
            }
            this.saveBatch(layoutEntityList);
        }
    }


    /**
     * 根据项目ID查询是否已经存在于设备布局信息
     * @param projectId 项目ID
     * @return boolean
     */
    private Boolean notExistLayoutByProject(Long projectId) {
        Integer count = baseMapper.selectCount(new QueryWrapper<EquipmentLayoutEntity>().eq("project_id", projectId));
        if(count>0)
            return false;
        else
            return true;
    }

}
