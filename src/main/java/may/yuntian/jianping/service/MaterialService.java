package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.jianping.entity.MaterialEntity;

import java.util.List;
import java.util.Map;

public interface MaterialService extends IService<MaterialEntity> {
    /**
     * 分页查询原辅料
     */
    List<MaterialEntity> listAll(Long projectId);
}
