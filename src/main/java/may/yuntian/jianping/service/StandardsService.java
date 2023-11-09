package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.jianping.entity.StandardsEntity;

import java.util.List;
import java.util.Map;

public interface StandardsService extends IService<StandardsEntity> {

    List<StandardsEntity> getList(Long projectId);

    Map<String,Object> getStandardsAll(Long projectId);

    void updateStandardsProject(Long projectId,Long[] ids);

}
