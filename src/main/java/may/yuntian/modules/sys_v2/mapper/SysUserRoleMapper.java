package may.yuntian.modules.sys_v2.mapper;

import may.yuntian.modules.sys_v2.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户角色关系处理
 *
 * @author hjy
 * @date 2023/3/31 16:08
 */
@Mapper
@Repository
public interface SysUserRoleMapper {
    /**
     * 通过用户ID删除用户和角色关联
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteUserRoleByUserId(Long userId);

    /**
     * 批量删除用户和角色关联
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteUserRole(Long[] ids);

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    public int countUserRoleByRoleId(Long roleId);

    /**
     * 批量新增用户角色信息
     *
     * @param userRoleList 用户角色列表
     * @return 结果
     */
    public int batchUserRole(List<SysUserRole> userRoleList);

    /**
     * 删除用户和角色关联信息
     *
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    public int deleteUserRoleInfo(SysUserRole userRole);

    /**
     * 批量取消授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    public int deleteUserRoleInfos(@Param("roleId") Long roleId, @Param("userIds") Long[] userIds);

    /**
     * 获取职能字符
     *
     * @param system 系统字符
     * @param url    页面路径
     * @param userId 登陆人id
     * @return 职能字符
     */
    String getUserAuthCode(@Param("system") String system, @Param("url") String url, @Param("userId") Long userId);

    /**
     * 查询某人是否拥有某权限
     *
     * @param userId  用户id
     * @param roleKey 权限字符
     * @param system  角色归属系统（可不填）
     * @return 是否
     */
    boolean checkUserRole(@Param("userId") Long userId, @Param("roleKey") String roleKey, @Param("system") String system);
}
