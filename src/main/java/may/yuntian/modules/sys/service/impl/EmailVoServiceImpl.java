package may.yuntian.modules.sys.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import may.yuntian.modules.sys.entity.EmailVo;
import may.yuntian.modules.sys.service.EmailVoService;

/**
 * 发送邮箱所需字段
 * @author LiXin
 *
 */
@Service("emailVoService")
public class EmailVoServiceImpl implements EmailVoService {
	

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	 private String from;
	
	/**
	 * 发送邮件
	 * @param emailVo 邮件信息
	 */
	@Override
	 public void sendSimpleMail(EmailVo emailVo) {
	    SimpleMailMessage message = new SimpleMailMessage();
	    message.setFrom(from);
	    message.setSubject(emailVo.getSubject());
	    message.setText(emailVo.getContent());
	    message.setTo(emailVo.getEmails());
	    javaMailSender.send(message);
	}
	
	
	/**
	 * 用于忘记密码发送邮件
	 * @param emailVo 邮件信息
	 */
	@Override
	 public void sendMailByUpdatePassword(EmailVo emailVo) {
	    SimpleMailMessage message = new SimpleMailMessage();
	    message.setFrom(from);
	    message.setSubject(emailVo.getSubject());
	    message.setText(emailVo.getContent());
	    message.setTo(emailVo.getEmails());
	    javaMailSender.send(message);
	}

}
