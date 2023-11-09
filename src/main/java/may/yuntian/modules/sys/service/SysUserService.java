package may.yuntian.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;


/**
 * 系统用户
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-16
 */
public interface SysUserService extends IService<SysUserEntity> {

	PageUtils queryPage(Map<String, Object> params);
	

	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(Long userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);

	/**
	 * 根据用户名，查询系统用户
	 */
	SysUserEntity queryByUserName(String username);
	/**
	 * 根据用户邮箱，查询系统用户
	 */
	SysUserEntity queryByEmail(String email);
	
    /**
     * 通过用户ID获取其所关联的中介列表
     * @param userId
     * @return
     */
    List<String> getMediationByUserid(Long userId);
	

	/**
	 * 保存用户
	 * @return 
	 */
	boolean save(SysUserEntity user);
	
	/**
	 * 修改用户
	 */
	void update(SysUserEntity user);
	
	/**
	 * 删除用户
	 */
	void deleteBatch(Long[] userIds);

	/**
	 * 修改密码
	 * @param userId       用户ID
	 * @param password     原密码
	 * @param newPassword  新密码
	 */
	boolean updatePassword(Long userId, String password, String newPassword);
	
	/**
	 * 登录时修改登录密码
	 * @param username 用户邮箱
	 * @param password  原密码
	 * @param newPassword  新密码
	 */
	boolean updateLoginPassword(String username, String password, String newPassword);
	
	/**
	 * 修改用户连续登录失败的次数为0 登录成功时调用
	 * @param userId	用户ID
	 * @param defeats 	第0次失败
	 * @return
	 */
	boolean updateDefeats(Long userId, Integer defeats);
	
	/**
	 * 修改用户连续登录失败的次数
	 * @param userId	用户ID
	 * @param defeats 	第几次失败
	 * @param maxDefeats 允许最大的失败次数
	 * @return
	 */
	boolean updateDefeats(Long userId, Integer defeats, Integer maxDefeats);
	
	/**
	 * 解锁与禁用账户，修改账户的状态
	 * @param userId
	 * @param status 状态  0：禁用   1：正常
	 * @return
	 */
	boolean updateStatus(Long userId, Integer status);
	
	
	/**
	 * 是否建立人员档案信息
	 * @param userId
	 * @param isBookbuilding(0默认，1建立，2不建立)
	 * @return
	 */
	boolean updateIsBookbuilding(Long userId, Integer isBookbuilding);
	
	/**
     * 显示全部用户信息列表
     * @return
     */
    List<SysUserEntity> listAll();
    
    /**
     * 获取所有用户信息不包含中介
     * @return
     */
    List<SysUserEntity> ListAllByType();
    
    /**
     * 根据部门显示用户信息列表
     * @param deptId
     * @return
     */
    List<SysUserEntity> listByDept(long deptId);
    
    /**
     * 根据用户IDS显示用户信息列表
     * @param userIds
     * @return
     */
    List<SysUserEntity> listByUserIds(List<Long> userIds);
    
    
	/**
	 * 免密登录所需用户数据
	 * @param userId
	 * @param email
	 * @param username
	 * @return
	 */
	SysUserEntity getSysUserEntity(Long userId, String email, String username);
	
}
