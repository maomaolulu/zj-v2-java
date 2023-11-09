package may.yuntian.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.R;
import may.yuntian.modules.sys.entity.SysUserTokenEntity;

/**
 * 用户Token
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-16
 */
public interface SysUserTokenService extends IService<SysUserTokenEntity> {

	/**
	 * 生成token
	 * @param userId  用户ID
	 */
	R createToken(long userId);

	/**
	 * 退出，修改token值
	 * @param userId  用户ID
	 */
	void logout(long userId);

}
