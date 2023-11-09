package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.jianping.entity.ProjectUserEntity;

import java.util.List;


/**
 *采样人员人员实体类
 *
 * @author LiXin
 * @data 2022-02-21
 */
public interface ProjectUserService extends IService<ProjectUserEntity> {

    /**
     * 通过项目Id获取采样人员与组长列表
     * @param projectId
     * @return
     */
    List<ProjectUserEntity> getListByProjectId(Long projectId);

    /**
     * 获取组员及组长列表
     * @param projectId
     * @return
     */
    String getListByTypeAndProjectId(Long projectId);

    /**
     * 根据类型和项目ID获取人员列表
     * @param type
     * @param projectId
     * @return
     */
    List<ProjectUserEntity> getListByType(Integer type, Long projectId);

    /**
     * 根据类型获取人员名称
     * @param type
     * @param projectId
     * @return
     */
    String getTypeName(Integer type,Long projectId);

    List<Long> getPlanIdListByUsername(String username);

    /**
     * 获取报告签字人员-第一条
     * @param projectId
     * @return
     */
    ProjectUserEntity getJiShu(Long projectId);

    /**
     * 通过项目Id获取采样人员与组长列表
     * @param
     * @return
     */
    List<ProjectUserEntity> getListByProjectIdList(List<Long> projectIdList);
}
