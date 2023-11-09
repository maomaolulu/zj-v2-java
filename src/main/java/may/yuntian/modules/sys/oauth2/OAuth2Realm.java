package may.yuntian.modules.sys.oauth2;

import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.ShiroService;
import may.yuntian.untils.AlRedisUntil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 认证
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-20
 */
@Component
public class OAuth2Realm extends AuthorizingRealm {
    @Autowired
    private ShiroService shiroService;
    @Autowired
    private AlRedisUntil alRedisUntil;
    private final static String prefix = "token_" ;
    //30分钟后过期
    private final static int EXPIRE = 1800 ;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUserEntity user = (SysUserEntity)principals.getPrimaryPrincipal();
        Long userId = user.getUserId();
//        System.out.println("授权(验证权限时调用) OAuth2Realm.doGetAuthorizationInfo......");
        //用户权限列表
        Set<String> permsSet = shiroService.getUserPermissions(userId);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();


        SysUserEntity sysUserEntity;
        //token失效
        if(alRedisUntil.get(prefix+accessToken) == null || alRedisUntil.getExpire(prefix+accessToken) <= 1){
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }else{
            alRedisUntil.expire(prefix+accessToken,EXPIRE);
            //根据accessToken，查询用户信息
            // 从redis中获取json字符串转成对象
            Map retMap = new HashMap();
            retMap = alRedisUntil.jsonToMap((String)alRedisUntil.get(prefix+accessToken));

            String newString = alRedisUntil.toJson(retMap.get("user_info"));
            sysUserEntity = alRedisUntil.fromJson(newString,SysUserEntity.class);
        }

        //查询用户信息
        SysUserEntity user = shiroService.queryUser(sysUserEntity.getUserId());
        //账号锁定
        if(user.getStatus() == 0){
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, accessToken, getName());
        return info;
    }
}
