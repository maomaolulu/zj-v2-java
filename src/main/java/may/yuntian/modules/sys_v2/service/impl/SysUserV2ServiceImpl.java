package may.yuntian.modules.sys_v2.service.impl;

import may.yuntian.modules.sys_v2.entity.SysUser;
import may.yuntian.modules.sys_v2.mapper.SysRoleMapper;
import may.yuntian.modules.sys_v2.mapper.SysUserMapper;
import may.yuntian.modules.sys_v2.mapper.SysUserRoleMapper;
import may.yuntian.modules.sys_v2.service.SysUserV2Service;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统用户业务处理层
 *
 * @author hjy
 * @date 2023/3/31 16:21
 */
@Service
public class SysUserV2ServiceImpl implements SysUserV2Service {

    private final SysUserMapper userMapper;

    private final SysRoleMapper roleMapper;

    private final SysUserRoleMapper userRoleMapper;

    public SysUserV2ServiceImpl(SysUserMapper userMapper, SysRoleMapper roleMapper, SysUserRoleMapper userRoleMapper) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.userRoleMapper = userRoleMapper;
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public List<SysUser> selectAllocatedList(SysUser user) {
        return userMapper.selectAllocatedList(user);
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public List<SysUser> selectUnallocatedList(SysUser user) {
        return userMapper.selectUnallocatedList(user);
    }

    /**
     * 获取职能字符
     *
     * @param system 系统字符
     * @param url    页面路径
     * @param userId 登陆人id
     * @return 职能字符
     */
    @Override
    public String getUserAuthCode(String system, String url, Long userId) {
        return userRoleMapper.getUserAuthCode(system, url, userId);
    }
}
