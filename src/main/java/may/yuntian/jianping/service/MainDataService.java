package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.jianping.entity.MainDataEntity;

import java.util.Map;

/**
 * 实验室-主数据
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2022-09-13 16:54:23
 */
public interface MainDataService extends IService<MainDataEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
}

