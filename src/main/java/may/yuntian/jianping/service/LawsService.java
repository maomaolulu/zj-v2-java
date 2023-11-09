package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.jianping.entity.LawsEntity;

import java.util.List;
import java.util.Map;

public interface LawsService extends IService<LawsEntity> {
    List<LawsEntity> list(Map<String,Object> params);

    List<LawsEntity> getListByProjectId(Long projectId);

    void addLawsProject(Long projectId,Long[] ids);
}
