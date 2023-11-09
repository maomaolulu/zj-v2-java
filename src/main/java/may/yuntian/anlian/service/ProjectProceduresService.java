package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.entity.ProjectProceduresEntity;

import java.util.List;

/**
 * 项目流程
 * 业务逻辑层接口
 *
 * @author LiXin
 * @date 2020-12-02
 */
public interface ProjectProceduresService extends IService<ProjectProceduresEntity> {

	List<ProjectProceduresEntity> listProceduresByProjectId(Long projectId);

	void deleteByProjectId(Long id);

	/**
	 * 通过项目ID和状态获取项目流程信息
	 * @param projectId
	 * @param status
	 * @return
	 */
	List<ProjectProceduresEntity> getByProjectIdAndStatus(Long projectId, Integer status);
	
	/**
	 * 获取一条
	 * @param projectId
	 * @param status
	 * @return
	 */
	ProjectProceduresEntity getProceduresEntity(Long projectId, Integer status);
	
}