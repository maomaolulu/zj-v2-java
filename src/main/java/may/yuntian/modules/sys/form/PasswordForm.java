package may.yuntian.modules.sys.form;

/**
 * 密码表单
 *
 * @author mustang.ma@qq.com
 * @date 2018-01-25
 */
public class PasswordForm {
	/**
	 * 用户名邮箱
	 */
	private String username;
    /**
     * 原密码
     */
    private String password;
    /**
     * 新密码
     */
    private String newPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
