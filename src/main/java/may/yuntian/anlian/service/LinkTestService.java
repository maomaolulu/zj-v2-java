package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.anlian.entity.LinkTestEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2023-05-08 09:09:26
 */
public interface LinkTestService extends IService<LinkTestEntity> {

    List<LinkTestEntity> queryPage(Map<String, Object> params);
}

