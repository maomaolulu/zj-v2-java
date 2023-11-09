package may.yuntian.modules.sys.service;

import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.entity.SysUserTokenEntity;

import java.util.Set;

/**
 * shiro相关接口
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-06-06
 */
public interface ShiroService {
    /**
     * 获取用户权限列表
     */
    Set<String> getUserPermissions(long userId);
    
    /**
     * 获取首页权限
     * @return
     */
    Set<String> getHomePremsList();
    
    /**
     * 获取时间节点权限
     */
    Set<String> getTimeNodeList();

    SysUserTokenEntity queryByToken(String token);

    /**
     * 根据用户ID，查询用户
     * @param userId
     */
    SysUserEntity queryUser(Long userId);
}
