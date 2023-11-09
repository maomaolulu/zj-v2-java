package may.yuntian.jianping.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;

import may.yuntian.jianping.mapper.LabAcceptProjectMapper;
import may.yuntian.jianping.entity.LabAcceptProjectEntity;
import may.yuntian.jianping.service.LabAcceptProjectService;

/**
 * 实验室-接受的项目(按照原始记录单批次区分)
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2023-02-17 15:37:49
 */
@Service("labAcceptProjectService")
public class LabAcceptProjectServiceImpl extends ServiceImpl<LabAcceptProjectMapper, LabAcceptProjectEntity> implements LabAcceptProjectService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<LabAcceptProjectEntity> page = this.page(
                new Query<LabAcceptProjectEntity>().getPage(params),
                new QueryWrapper<LabAcceptProjectEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 获取实验室-接受的项目(按照原始记录单批次区分)信息
     * @param projectId
     * @return
     */
    public LabAcceptProjectEntity getOneByProjectId(Long projectId){
        LabAcceptProjectEntity labAcceptProjectEntities = baseMapper.selectOne(new QueryWrapper<LabAcceptProjectEntity>()
                .eq("project_id",projectId).last("limit 1")
        );
        return labAcceptProjectEntities;
    }

    /**
     * 获取实验室-接受的项目(按照原始记录单批次区分)信息
     * @param projectId
     * @return
     */
    public List<LabAcceptProjectEntity> getListByProjectId(Long projectId){
        List<LabAcceptProjectEntity>  list = baseMapper.selectList(new QueryWrapper<LabAcceptProjectEntity>()
                .eq("project_id",projectId)
        );
        return list;
    }

}
