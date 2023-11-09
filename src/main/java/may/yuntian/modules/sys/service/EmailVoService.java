package may.yuntian.modules.sys.service;

import may.yuntian.modules.sys.entity.EmailVo;

/**
 * 发送邮箱数据接口
 *
 * @author LiXin
 */
public interface EmailVoService {

	/**
	 * 发送邮件
	 * @param emailVo 邮件信息
	 */
	void sendSimpleMail(EmailVo emailVo);
	
	/**
	 * 用于忘记密码发送邮件
	 * @param emailVo 邮件信息
	 */
	 void sendMailByUpdatePassword(EmailVo emailVo);

}