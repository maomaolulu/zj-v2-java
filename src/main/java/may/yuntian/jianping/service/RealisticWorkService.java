package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.jianping.entity.RealisticWorkEntity;

import java.util.List;
import java.util.Map;

public interface RealisticWorkService extends IService<RealisticWorkEntity> {

    List<RealisticWorkEntity> getList(Long projectId);
}
