package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.entity.ProjectDateEntity;

public interface ProjectDateService extends IService<ProjectDateEntity> {


    /**
     * 根据项目ID获取项目日期相关信息
     * @param projctId
     * @return
     */
    ProjectDateEntity getOneByProjetId(Long projctId);

    /**
     * 获取是否物理发送
     * @param projectId
     * @return
     */
    public boolean isPhysicalSend(Long projectId);
}
