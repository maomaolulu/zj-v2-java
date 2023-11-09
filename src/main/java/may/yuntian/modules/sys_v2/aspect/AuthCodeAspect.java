package may.yuntian.modules.sys_v2.aspect;

import may.yuntian.modules.sys_v2.annotation.AuthCode;
import may.yuntian.modules.sys_v2.entity.vo.AuthCodeVo;
import may.yuntian.modules.sys_v2.service.SysUserV2Service;
import may.yuntian.modules.sys_v2.utils.ShiroUtilsV2;
import may.yuntian.modules.sys_v2.utils.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 职能符号判断切面
 *
 * @author hjy
 * @date 2023/4/7 10:36
 */
@Aspect
@Component
public class AuthCodeAspect {

    private final SysUserV2Service userService;

    public AuthCodeAspect(SysUserV2Service userService) {
        this.userService = userService;
    }

    /**
     * 请求前执行
     *
     * @param joinPoint 切点
     */
    @Around("@annotation(authCode)")
    public Object around(ProceedingJoinPoint joinPoint, AuthCode authCode) {
        return handleAuthCode(joinPoint, authCode);
    }

    private Object handleAuthCode(ProceedingJoinPoint joinPoint, AuthCode authCode) {
        String url = authCode.url();
        String system = authCode.system();
        Long userId = ShiroUtilsV2.getUserId();
        String authCodeTemp = userService.getUserAuthCode(system, url, userId);
        //获取相关参数
        Object[] args = joinPoint.getArgs();
        if (StringUtils.isNotEmpty(authCodeTemp)) {
            for (int i = 0; i < args.length; i++) {
                // 對象为  AuthCodeVo
                if (args[i] instanceof AuthCodeVo) {
                    args[i] = new AuthCodeVo(authCodeTemp);
                    break;
                }
            }
        }
        // 脱敏参数
        Object proceed = null;
        try {
            proceed = joinPoint.proceed(args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return proceed;
    }

}
