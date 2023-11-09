package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.vo.ExportSchedulingVo;
import may.yuntian.anlian.vo.ProjectDateUserVo;
import may.yuntian.anlian.vo.ProjectTaskVo;
import may.yuntian.anlian.vo.ProjectTimeZoneVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 项目表(包含了原任务表的字段)
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2022-01-10 14:43:43
 */
public interface ProjectService extends IService<ProjectEntity> {


    /**
     * ipad端 获取本人负责或参与得项目列表
     * @param params
     * @return
     */
    List<ProjectTaskVo> getMyTaskList(Map<String,Object> params);


    /**
     * web端 项目排单列表页
     * @param params
     * @return
     */
    List<ProjectDateUserVo> getListProjectDateUser(Map<String,Object> params);

    /**
     * 排单列表导出信息处理
     * @param params
     * @return
     */
    public List<ExportSchedulingVo> exportScheduling(Map<String,Object> params);

    /**
     * 导出排单信息
     * @param all
     * @param response
     * @throws IOException
     */
    public void download(List<ExportSchedulingVo> all, HttpServletResponse response) throws IOException;


    /**
     * web端我的任务列表
     * @param params
     * @return
     */
    List<ProjectDateUserVo> getMytaskList(Map<String, Object> params);


    /**
     * 项目排单接口
     * @param projectDateUserVo
     */
    void saveOrUpdateProjectUser(ProjectDateUserVo projectDateUserVo);

    /**
     * 项目信息 单条
     * @param projectId
     * @return
     */
    ProjectDateUserVo getOneByProjectId(Long projectId);

    /**
     * web端 实验室数据录入列表
     * @param params
     * @return
     */
    List<ProjectDateUserVo> getListLab(Map<String, Object> params);

    /**
     * web任务列表部门权限列表
     * @param params
     * @return
     */
    List<ProjectDateUserVo> planDeptLit(Map<String, Object> params);

    /**
     * web任务列表无权限控制
     * @param params
     * @return
     */
    List<ProjectDateUserVo> planLit(Map<String, Object> params);

    /**
     * 节点填写列表页
     * @param params
     * @return
     */
    List<ProjectTimeZoneVo> getTimeList(Map<String,Object> params);

    /**
     * 时间节点获取接口
     * @param id
     */
    ProjectEntity getTimeZoneById(Long id);

    /**
     * 修改时间节点
     */
    void updateTimeZone(HttpServletRequest httpRequest, ProjectEntity project);

}

