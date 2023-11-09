package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.jianping.entity.ConclusionEntity;

import java.util.List;
import java.util.Map;

/**
 * 结论
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2022-11-15 08:54:28
 */
public interface ConclusionService extends IService<ConclusionEntity> {

    List<ConclusionEntity> getListByProjectId(Long projectId);
    
}

