package may.yuntian.modules.sys.service.impl;

import may.yuntian.common.utils.R;
import may.yuntian.modules.sys.dao.SysUserTokenDao;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.entity.SysUserTokenEntity;
import may.yuntian.modules.sys.oauth2.TokenGenerator;
import may.yuntian.modules.sys.service.SysUserService;
import may.yuntian.modules.sys.service.SysUserTokenService;
import may.yuntian.untils.AlRedisUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;
import java.util.HashMap;


@Service("sysUserTokenService")
public class SysUserTokenServiceImpl extends ServiceImpl<SysUserTokenDao, SysUserTokenEntity> implements SysUserTokenService {
	//12小时后过期
	private final static int EXPIRE = 1800 ;
    @Autowired
    private AlRedisUntil alRedisUntil;
    @Autowired
    private SysUserService sysUserService;
    private final static String prefix = "token_" ;

//	@Override
	public R createToken1(long userId) {
		//生成一个token
		String token = TokenGenerator.generateValue();

		//当前时间
		Date now = new Date();
		//过期时间
		Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

		//判断是否生成过token
		SysUserTokenEntity tokenEntity = this.getById(userId);
		if(tokenEntity == null){
			tokenEntity = new SysUserTokenEntity();
			tokenEntity.setUserId(userId);
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);

			//保存token
			this.save(tokenEntity);
		}else{
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);

			//更新token
			this.updateById(tokenEntity);
		}
		R r = R.ok().put("token", token).put("expire", EXPIRE).put("expireTime", expireTime);

		return r;
	}

    @Override
    public R createToken(long userId) {
        //生成一个token
        String token = TokenGenerator.generateValue();
        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

        //判断是否生成过token
        String oldToken = (String)alRedisUntil.hget("anlian_user", String.valueOf(userId));
        SysUserEntity sysUserEntity = sysUserService.getById(userId);
        sysUserEntity.setPassword(null);
        sysUserEntity.setSalt(null);
        HashMap<String,Object> map = new HashMap<>();
        map.put("user_info",sysUserEntity);
        String userInfo = alRedisUntil.toJson(map);
//        String s = map.toString();
//        System.err.println(s);
//        System.err.println(userInfo);
        if(oldToken == null){
            alRedisUntil.hset("anlian_user",String.valueOf(userId),token);
            alRedisUntil.set(prefix+token,userInfo,EXPIRE);
        }else{
            alRedisUntil.del(prefix+oldToken);
            alRedisUntil.hset("anlian_user",String.valueOf(userId),token);
            alRedisUntil.set(prefix+token,userInfo,EXPIRE);
        }
        R r = R.ok().put("token", token).put("expire", EXPIRE).put("expireTime", expireTime);

        return r;
    }

	@Override
	public void logout(long userId) {
		//生成一个token
		String token = TokenGenerator.generateValue();

        String oldToken = (String)alRedisUntil.hget("anlian_user", String.valueOf(userId));
        alRedisUntil.del(prefix+oldToken);
        alRedisUntil.hset("anlian_user",String.valueOf(userId),token);

	}
}
