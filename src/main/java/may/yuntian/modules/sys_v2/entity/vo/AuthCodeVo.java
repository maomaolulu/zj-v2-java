package may.yuntian.modules.sys_v2.entity.vo;

/**
 * 职能权限判断载体
 *
 * @author hjy
 * @date 2023/4/7 14:19
 */
public class AuthCodeVo {
    /**
     * 权限职能码
     */
    private String authCode;

    public AuthCodeVo() {

    }

    public AuthCodeVo(String authCode) {
        this.authCode = authCode;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
}
