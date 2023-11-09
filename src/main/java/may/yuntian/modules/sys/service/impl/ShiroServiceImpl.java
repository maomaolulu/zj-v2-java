package may.yuntian.modules.sys.service.impl;

import may.yuntian.common.utils.Constant;
import may.yuntian.modules.sys.dao.SysMenuDao;
import may.yuntian.modules.sys.dao.SysUserDao;
import may.yuntian.modules.sys.dao.SysUserTokenDao;
import may.yuntian.modules.sys.entity.SysMenuEntity;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.entity.SysUserTokenEntity;
import may.yuntian.modules.sys.service.ShiroService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShiroServiceImpl implements ShiroService {
    @Autowired
    private SysMenuDao sysMenuDao;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysUserTokenDao sysUserTokenDao;

    @Override
    public Set<String> getUserPermissions(long userId) {
        List<String> permsList;

        //系统管理员，拥有最高权限
        if(userId == Constant.SUPER_ADMIN){
            List<SysMenuEntity> menuList = sysMenuDao.selectList(null);
            permsList = new ArrayList<>(menuList.size());
            for(SysMenuEntity menu : menuList){
                permsList.add(menu.getPerms());
            }
        }else{
            permsList = sysUserDao.queryAllPerms(userId);
        }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for(String perms : permsList){
            if(StringUtils.isBlank(perms)){
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }
    
    /**
     * 获取首页权限
     * @return
     */
    public Set<String> getHomePremsList() {
		SysMenuEntity menu = sysMenuDao.selectOne(new QueryWrapper<SysMenuEntity>()
				.eq("url", "dashboard")
				.eq("parent_id", 0)
				);
		List<SysMenuEntity> menuList = sysMenuDao.queryListParentId(menu.getMenuId());
		List<String> premsList = menuList.stream().map(SysMenuEntity::getPerms).collect(Collectors.toList());
        Set<String> permsSet = new HashSet<>();
        for(String perms : premsList){
            if(StringUtils.isBlank(perms)){
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
		return permsSet;
	}
    
    /**
     * 获取时间节点权限
     */
    public Set<String> getTimeNodeList() {
		SysMenuEntity menuEntity = sysMenuDao.selectOne(new QueryWrapper<SysMenuEntity>()
				.eq("url", "nodeWrite")
				.eq("name", "节点填写")
				);
		List<SysMenuEntity> menuList = sysMenuDao.queryListParentId(menuEntity.getMenuId());
		List<String> premsList = menuList.stream().map(SysMenuEntity::getPerms).collect(Collectors.toList());
        Set<String> permsSet = new HashSet<>();
        for(String perms : premsList){
            if(StringUtils.isBlank(perms)){
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
    	return permsSet;
	}
    

    @Override
    public SysUserTokenEntity queryByToken(String token) {
        return sysUserTokenDao.queryByToken(token);
    }

    @Override
    public SysUserEntity queryUser(Long userId) {
        return sysUserDao.selectById(userId);
    }
}
