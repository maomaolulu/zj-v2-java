package may.yuntian.modules.sys.service.impl;

import may.yuntian.common.exception.RRException;
import may.yuntian.common.utils.Constant;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;
import may.yuntian.modules.sys.dao.SysRoleDao;
import may.yuntian.modules.sys.dao.SysUserDao;
import may.yuntian.modules.sys.entity.SysDeptEntity;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.SysDeptService;
import may.yuntian.modules.sys.service.SysUserRoleService;
import may.yuntian.modules.sys.service.SysUserService;
import org.apache.commons.lang.RandomStringUtils;
//import org.apache.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统用户
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-16
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {
	@Autowired
	private SysUserRoleService sysUserRoleService;
//	private SysUserRoleDao sysUserRoleDao;
	
	@Autowired
	private SysRoleDao sysRoleDao;
	
	@Autowired
	private SysDeptService sysDeptService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String username = (String) params.get("username");
		String jobNum = (String) params.get("jobNum");
		String isBookbuilding = (String) params.get("isBookbuilding");
		Long createUserId = (Long) params.get("createUserId");
		Integer type = (Integer) params.get("type");
		Integer sex = (Integer) params.get("sex");
		String deptIdStr = (String) params.get("deptId");
		String subjection = (String)params.get("subjection");
		Integer status = (Integer)params.get("status");
		
		Long deptId = null;
		//部门ID列表
        List<Long> subDeptIdList = new ArrayList<Long>();
        
		//用户子部门ID列表
		if(StringUtils.checkValNotNull(deptIdStr)) {
			deptId = Long.parseLong(deptIdStr);//返回long基本数据类型
			subDeptIdList = sysDeptService.getSubDeptIdList(deptId);
			subDeptIdList.add(deptId);//添加本级部门，否则将只是子部门
		}

		IPage<SysUserEntity> page = this.page(new Query<SysUserEntity>().getPage(params),
				new QueryWrapper<SysUserEntity>()
						.like(StringUtils.isNotBlank(username), "username", username)
						.like(StringUtils.isNotBlank(jobNum), "job_num", jobNum)
						.apply(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
						.eq(createUserId != null, "create_user_id", createUserId)
						.eq(type != null, "type", type)
						.eq(sex != null, "sex", sex)
						.lt(status!=null,"status", status)
						.eq(StringUtils.isNotBlank(isBookbuilding), "is_bookbuilding", isBookbuilding)
						.eq(StringUtils.isNotBlank(subjection), "subjection", subjection)
						.gt(createUserId == null,"create_user_id", Constant.SUPER_ADMIN)//不显示超级管理员的账户
						.in(subDeptIdList.size() > 0, "dept_id", subDeptIdList)//能看本部门及子部门的用户
//						.eq(StringUtils.checkValNotNull(deptId), "dept_id", deptId)只能看本部门的用户
				);
		
		for(SysUserEntity sysUserEntity : page.getRecords()){
			//为了安全将密码与加密盐隐藏不返回页面
			sysUserEntity.setPassword(null);
			sysUserEntity.setSalt(null);
			
			SysDeptEntity sysDeptEntity = sysDeptService.getById(sysUserEntity.getDeptId());
			if(sysDeptEntity!=null) {
				sysUserEntity.setDeptName(sysDeptEntity.getName());
			}
			if (sysUserEntity.getBelongUserid()!=null) {
				SysUserEntity sysUser = this.getById(sysUserEntity.getBelongUserid());
				if(sysUser!=null) {
					sysUserEntity.setBelongUsername(sysUser.getUsername());
				}
			}
			
		}

		return new PageUtils(page);
	}
	
	/**
	 * 免密登录所需用户数据
	 * @param userId
	 * @param email
	 * @param username
	 * @return
	 */
	@Override
	public SysUserEntity getSysUserEntity(Long userId,String email,String username) {
		SysUserEntity sysUserEntity = baseMapper.selectOne(new QueryWrapper<SysUserEntity>()
				.eq("user_id", userId)
				.eq("email", email)
				.eq("username", username)
				);
		
		
		return sysUserEntity;
	}
	
	
	
	/**
     * 显示全部用户信息列表
     * @return
     */
    public List<SysUserEntity> listAll() {
    	List<SysUserEntity> list = this.list(
	    			new QueryWrapper<SysUserEntity>()
	    			.select("user_id","username","job_num","dept_id","subjection","type","email")
	    			.gt("user_id",1)
	    			.ne("type", 3)
                );
    	return list;
    }
    
    /**
     * 获取所有用户信息不包含中介
     * @return
     */
    public List<SysUserEntity> ListAllByType() {
		List<SysUserEntity> list = this.list(new QueryWrapper<SysUserEntity>()
				.select("user_id","username","job_num","dept_id","subjection","type","email")
				.gt("user_id", 1)
				.eq("type", 1)
				);
    	
    	return list;
	}
    
    /**
     * 根据部门显示用户信息列表
     * @param deptId
     * @return
     */
    public List<SysUserEntity> listByDept(long deptId) {
    	//部门ID列表
        List<Long> subDeptIdList = new ArrayList<Long>();
        
		//用户子部门ID列表
		if(StringUtils.checkValNotNull(deptId)) {
			subDeptIdList = sysDeptService.getSubDeptIdList(deptId);
			subDeptIdList.add(deptId);//添加本级部门，否则将只是子部门
		}
		
    	List<SysUserEntity> list = this.list(
    			new QueryWrapper<SysUserEntity>()
    			.select("user_id","username","job_num","dept_id","email","subjection","type")
    			.gt("user_id",1)
    			.in(subDeptIdList.size() > 0, "dept_id", subDeptIdList)
    			//.eq("dept_id", deptId)
    			);
    	return list;
    }
    
    /**
     * 根据用户IDS显示用户信息列表
     * @param userIds
     * @return
     */
    public List<SysUserEntity> listByUserIds(List<Long> userIds) {
    	
    	List<SysUserEntity> list = new ArrayList<SysUserEntity>();
    	
    	//用户子部门ID列表
    	if(StringUtils.checkValNotNull(userIds) && userIds.size()>0) {
    		list = this.list(
    			new QueryWrapper<SysUserEntity>()
    			.select("user_id","username","job_num","dept_id","email","subjection","type")
    			.in("user_id", userIds)
    		);
    	}
    	
    	return list;
    }
    
    /**
     * 通过用户ID获取其所关联的中介列表
     * @param userId
     * @return
     */
    public List<String> getMediationByUserid(Long userId) {
		List<SysUserEntity> mediationList = this.list(new QueryWrapper<SysUserEntity>()
				.eq("belong_userid", userId)
				.eq("type", 2)
				);
		List<String> usernameList = mediationList.stream().map(SysUserEntity::getUsername).collect(Collectors.toList());
    	return usernameList;
	}
    

	@Override
	public List<String> queryAllPerms(Long userId) {
		return baseMapper.queryAllPerms(userId);
	}

	@Override
	public List<Long> queryAllMenuId(Long userId) {
		return baseMapper.queryAllMenuId(userId);
	}

	@Override
	public SysUserEntity queryByUserName(String username) {
		return baseMapper.queryByUserName(username);
	}
	
	@Override
	public SysUserEntity queryByEmail(String email) {
		return baseMapper.queryByEmail(email);
	}

	@Transactional
	public boolean save(SysUserEntity user) {
		//System.out.println("服务层保存用户信息："+user.toString());
		user.setCreateTime(new Date());
		// sha256加密
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
		user.setSalt(salt);
		baseMapper.insert(user);
//		this.save(user);

		// 检查角色是否越权
//		checkRole(user);

		// 保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
		
		return true;
	}

	@Override
	@Transactional
	public void update(SysUserEntity user) {
		if (StringUtils.isBlank(user.getPassword())) {
			user.setPassword(null);
		} else {
			// sha256加密
			String salt = RandomStringUtils.randomAlphanumeric(20);
			user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());//修改密码时重新生成加密盐
			user.setSalt(salt);
			user.setChangeNumber(user.getChangeNumber()+1);
//			user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt()).toHex());
		}
		baseMapper.updateById(user);

		// 检查角色是否越权
//		checkRole(user);

		// 保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}

	@Override
	public void deleteBatch(Long[] userId) {
		this.removeByIds(Arrays.asList(userId));
	}

	@Override
	public boolean updatePassword(Long userId, String password, String newPassword) {
		SysUserEntity userEntity = new SysUserEntity();
		SysUserEntity oldUserEntity = this.getById(userId);
		userEntity.setPassword(newPassword);
		userEntity.setChangeNumber(oldUserEntity.getChangeNumber()+1);
		return this.update(userEntity,
				new QueryWrapper<SysUserEntity>().eq("user_id", userId).eq("password", password));
	}
	
	@Override
	public boolean updateLoginPassword(String email, String password, String newPassword) {
		SysUserEntity userEntity = new SysUserEntity();
		userEntity.setPassword(newPassword);
		userEntity.setChangeNumber(1);
		return this.update(userEntity,
				new QueryWrapper<SysUserEntity>().eq("email", email).eq("password", password));
	}

	/**
	 * 修改用户连续登录失败的次数为0 登录成功时调用
	 * 
	 * @param userId
	 * @param defeats
	 * @return
	 */
	@Override
	public boolean updateDefeats(Long userId, Integer defeats) {
		SysUserEntity userEntity = new SysUserEntity();
		userEntity.setDefeats(defeats);

		return this.update(userEntity, new QueryWrapper<SysUserEntity>().eq("user_id", userId));
	}

	/**
	 * 修改用户连续登录失败的次数 登录失败时调用
	 * 
	 * @param userId     用户ID
	 * @param defeats    第几次失败
	 * @param maxDefeats 允许最大的失败次数
	 * @return
	 */
	@Override
	public boolean updateDefeats(Long userId, Integer defeats, Integer maxDefeats) {
		SysUserEntity userEntity = new SysUserEntity();
		userEntity.setDefeats(defeats);

		// 如果连续登录失败的次数达到5次，则禁用该账户
		if (defeats >= maxDefeats)
			userEntity.setStatus(0);

		return this.update(userEntity, new QueryWrapper<SysUserEntity>().eq("user_id", userId));
	}

	/**
	 * 解锁与禁用账户，修改账户的状态
	 * 
	 * @param userId
	 * @param status 状态 0：禁用 1：正常
	 * @return
	 */
	@Override
	public boolean updateStatus(Long userId, Integer status) {
		SysUserEntity userEntity = new SysUserEntity();
		userEntity.setStatus(status);

		if (status == 1)
			userEntity.setDefeats(0);

		return this.update(userEntity, new QueryWrapper<SysUserEntity>().eq("user_id", userId));
	}
	
	
	/**
	 * 是否建立人员档案信息
	 * @param userId
	 * @param isBookbuilding(0默认，1建立，2不建立)
	 * @return
	 */
	@Override
	public boolean updateIsBookbuilding(Long userId,Integer isBookbuilding) {
		SysUserEntity userEntity = new SysUserEntity();
		userEntity.setIsBookbuilding(isBookbuilding);

		return this.update(userEntity, new QueryWrapper<SysUserEntity>().eq("user_id", userId));
	}
	

	/**
	 * 检查角色是否越权
	 */
	private void checkRole(SysUserEntity user) {
		if (user.getRoleIdList() == null || user.getRoleIdList().size() == 0) {
			return;
		}

		// 查询用户创建的角色列表
		List<Long> roleIdList = sysRoleDao.queryRoleIdList(user.getCreateUserId());
		Boolean isCheck = false;//第一个即ID=1的角色也设置为超级角色
		for (Long roleId : roleIdList) {//此业务为特殊需求会造成越权行为
			if(roleId==Constant.SUPER_ADMIN) {
				isCheck = true;
				break;
			}
		}
		// 如果不是超级管理员，则需要判断用户的角色是否自己创建
//		if (user.getCreateUserId() == Constant.SUPER_ADMIN) {
		if (user.getCreateUserId() == Constant.SUPER_ADMIN || isCheck) {//ID为1的角色作为HR等特殊角色，不检查其越权情况
//			if (user.getCreateUserId() == Constant.SUPER_ADMIN || user.getCreateUserId().equals(3L)) {//ID为3的用户作为HR等特殊用户，不检查其越权情况
			return;
		}


		// 判断是否越权
		if (!roleIdList.containsAll(user.getRoleIdList())) {
			throw new RRException("新增用户所选角色，不是本人创建");
		}
	}
}
