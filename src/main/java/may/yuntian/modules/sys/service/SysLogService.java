package may.yuntian.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.modules.sys.entity.SysLogEntity;

import java.util.Map;


/**
 * 系统日志
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-16
 */
public interface SysLogService extends IService<SysLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    PageUtils queryPageByOperation(Map<String, Object> params);
    
    /**
     * 登录统计
     * @param username
     * @return
     */
    Map<String, Object> getLoginStatistics(Map<String, Object> params);
    
    /**
     * 根据用户邮箱获取用户上一次的登录日期
     * @param username
     * @return
     */
    String getLastDateByUsername(String username);
    
//    /**
//     * 获取redis中所有人员登录信息
//     * @return
//     */
//    List<Map<String, Object>> getLogListByRedis();

}
