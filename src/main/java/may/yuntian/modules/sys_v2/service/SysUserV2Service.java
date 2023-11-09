package may.yuntian.modules.sys_v2.service;

import may.yuntian.modules.sys_v2.entity.SysUser;

import java.util.List;

/**
 * @author hjy
 * @date 2023/3/31 16:21
 */
public interface SysUserV2Service {
    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public List<SysUser> selectAllocatedList(SysUser user);

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public List<SysUser> selectUnallocatedList(SysUser user);

    /**
     * 获取职能
     *
     * @param system 系统字符
     * @param url    页面路径
     * @param userId 登陆人id
     * @return 职能字符
     */
    String getUserAuthCode(String system, String url, Long userId);
}
