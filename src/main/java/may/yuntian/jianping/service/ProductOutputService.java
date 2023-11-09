package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.jianping.entity.ProductOutputEntity;

import java.util.List;
import java.util.Map;

/**
 * 主要产品与年产量
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2022-11-10 15:52:36
 */
public interface ProductOutputService extends IService<ProductOutputEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据项目ID获取产品及产量信息
     * @param projectId
     * @return
     */
    List<ProductOutputEntity> getListByProjectId(Long projectId);
    
}

