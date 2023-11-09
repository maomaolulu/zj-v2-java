package may.yuntian.modules.sys.service.impl;

import may.yuntian.common.exception.RRException;
import may.yuntian.common.utils.Constant;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;
import may.yuntian.modules.sys.dao.SysRoleDao;
import may.yuntian.modules.sys.dao.SysUserDao;
import may.yuntian.modules.sys.entity.SysDeptEntity;
import may.yuntian.modules.sys.entity.SysRoleEntity;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.SysDeptService;
import may.yuntian.modules.sys.service.SysRoleDeptService;
import may.yuntian.modules.sys.service.SysRoleMenuService;
import may.yuntian.modules.sys.service.SysRoleService;
import may.yuntian.modules.sys.service.SysUserRoleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 角色
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-16
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysUserDao sysUserDao;
	
    @Autowired
    private SysUserRoleService sysUserRoleService;
    
    @Autowired
	private SysRoleDeptService sysRoleDeptService;
	
	@Autowired
	private SysDeptService sysDeptService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String roleName = (String)params.get("roleName");
		Long createUserId = (Long)params.get("createUserId");
		String subordinate = (String)params.get("subordinate");

		IPage<SysRoleEntity> page = this.page(
			new Query<SysRoleEntity>().getPage(params),
			new QueryWrapper<SysRoleEntity>()
				.like(StringUtils.isNotBlank(roleName),"role_name", roleName)
				.apply(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
				.eq(createUserId != null,"create_user_id", createUserId)
				.eq(StringUtils.isNotBlank(subordinate),"subordinate", subordinate)
		);
		
		for(SysRoleEntity sysRoleEntity : page.getRecords()){
			SysDeptEntity sysDeptEntity = sysDeptService.getById(sysRoleEntity.getDeptId());
			if(sysDeptEntity != null){
				sysRoleEntity.setDeptName(sysDeptEntity.getName());
			}
			
			//此角色中包含的系统用户信息
			List<Long> userIds = sysUserRoleService.queryUserIdList(sysRoleEntity.getRoleId());//根据角色ID，获取用户ID列表
			List<SysUserEntity> sysUserList = listByUserIds(userIds);
			sysRoleEntity.setSysUserList(sysUserList);
		}

		return new PageUtils(page);
	}
	
	/**
	 * 获取管理员信息
	 * @return
	 */
	public List<SysRoleEntity> getList() {
		List<SysRoleEntity> roleList = this.list(
				new QueryWrapper<SysRoleEntity>()
				.eq("role_name", "管理员")
				);
		for(SysRoleEntity sysRoleEntity:roleList) {
			SysDeptEntity sysDeptEntity = sysDeptService.getById(sysRoleEntity.getDeptId());
			if(sysDeptEntity != null){
				sysRoleEntity.setDeptName(sysDeptEntity.getName());
			}
			
			//此角色中包含的系统用户信息
			List<Long> userIds = sysUserRoleService.queryUserIdList(sysRoleEntity.getRoleId());//根据角色ID，获取用户ID列表
			List<SysUserEntity> sysUserList = listByUserIds(userIds);
			sysRoleEntity.setSysUserList(sysUserList);
		}
		return roleList;
	}
	
	/**
     * 根据用户IDS显示用户信息列表
     * @param userIds
     * @return
     */
    private List<SysUserEntity> listByUserIds(List<Long> userIds) {
    	
    	List<SysUserEntity> list = new ArrayList<SysUserEntity>();
    	
    	//用户子部门ID列表
    	if(userIds != null && userIds.size()>0) {
    		list = sysUserDao.selectList(
    			new QueryWrapper<SysUserEntity>()
    			.select("user_id","username","job_num","dept_id","email")
    			.in("user_id", userIds)
    		);
    	}
    	
    	return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(SysRoleEntity role) {
        role.setCreateTime(new Date());
        //System.out.println("保存角色信息："+role.toString());
        baseMapper.insert(role);
//        this.save(role);

        //检查权限是否越权
//        checkPrems(role);

        //保存角色与菜单关系
        sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
        
        //保存角色与部门关系
        if(role.getDeptIdList() != null) {
        	sysRoleDeptService.saveOrUpdate(role.getRoleId(), role.getDeptIdList(),role.getProjectTypeList(),role.getOrderList(),role.getSourceList());
        }
        
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysRoleEntity role) {
        this.updateById(role);

        //检查权限是否越权
//        checkPrems(role);

        //更新角色与菜单关系
        sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
      
        //保存角色与部门关系
        if(role.getDeptIdList() != null) {
        	sysRoleDeptService.saveOrUpdate(role.getRoleId(), role.getDeptIdList(),role.getProjectTypeList(),role.getOrderList(),role.getSourceList());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] roleIds) {
        //删除角色
        this.removeByIds(Arrays.asList(roleIds));

        //删除角色与菜单关联
        sysRoleMenuService.deleteBatch(roleIds);
        
        //删除角色与部门关联
      	sysRoleDeptService.deleteBatch(roleIds);
      		
        //删除角色与用户关联
        sysUserRoleService.deleteBatch(roleIds);
    }


    @Override
	public List<Long> queryRoleIdList(Long createUserId) {
		return baseMapper.queryRoleIdList(createUserId);
	}

	/**
	 * 检查权限是否越权
	 */
	private void checkPrems(SysRoleEntity role){
		
		// 查询用户创建的角色列表
		List<Long> roleIdList = queryRoleIdList(role.getCreateUserId());
		Boolean isCheck = false;//第一个即ID=1的角色也设置为超级角色
		for (Long roleId : roleIdList) {//此业务为特殊需求会造成越权行为
			if(roleId==Constant.SUPER_ADMIN) {
				isCheck = true;
				break;
			}
		}
				
		//如果不是超级管理员，则需要判断角色的权限是否超过自己的权限
		if(role.getCreateUserId() == Constant.SUPER_ADMIN || isCheck){
//			if(role.getCreateUserId() == Constant.SUPER_ADMIN){
			return ;
		}
		
		//查询用户所拥有的菜单列表
		List<Long> menuIdList = sysUserDao.queryAllMenuId(role.getCreateUserId());
//		List<Long> menuIdList = sysUserService.queryAllMenuId(role.getCreateUserId());
		
		//判断是否越权
		if(!menuIdList.containsAll(role.getMenuIdList())){
			throw new RRException("新增角色的权限，已超出你的权限范围");
		}
	}
}
