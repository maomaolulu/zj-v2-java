package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.jianping.entity.ProjectCountEntity;

import java.util.Map;

/**
 * 
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2022-07-25 14:15:40
 */
public interface ProjectCountService extends IService<ProjectCountEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据项目ID获取项目信息录入节点信息
     * @param projectId
     * @return
     */
    ProjectCountEntity getOneByProjectId(Long projectId);

}

