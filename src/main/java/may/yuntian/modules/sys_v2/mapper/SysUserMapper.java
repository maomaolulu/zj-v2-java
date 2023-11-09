package may.yuntian.modules.sys_v2.mapper;

import may.yuntian.modules.sys_v2.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统用户数据处理层
 *
 * @author hjy
 * @date 2023/3/31 16:27
 */
@Mapper
@Repository
public interface SysUserMapper {
    /**
     * 根据条件分页查询已配用户角色列表
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
}
