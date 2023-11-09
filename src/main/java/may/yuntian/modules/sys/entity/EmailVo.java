package may.yuntian.modules.sys.entity;

import java.io.Serializable;
import java.util.Arrays;


/**
 * 发送邮箱所需字段
 * @author LiXin
 *
 */
public class EmailVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主题
	 */
	private String subject;
	  
	/**
	 * 主题内容
	 */
	private String content;

	/**
	 * 接收人邮箱列表
	 */
	private String[] emails;
	
	
	private String uuid;
	
	private String password;
	
	  
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String[] getEmails() {
		return emails;
	}

	public void setEmails(String[] emails) {
		this.emails = emails;
	}

	@Override
	public String toString() {
		return "EmailVo [subject=" + subject + ", content=" + content + ", emails=" + Arrays.toString(emails) + "]";
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
}
