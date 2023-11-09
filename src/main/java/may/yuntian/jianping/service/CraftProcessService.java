package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.jianping.entity.CraftProcessEntity;

import java.util.List;

public interface CraftProcessService extends IService<CraftProcessEntity> {
    List<CraftProcessEntity> craftList(Long projectId);
}

