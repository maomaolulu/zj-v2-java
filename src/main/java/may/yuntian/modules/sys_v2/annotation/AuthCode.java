package may.yuntian.modules.sys_v2.annotation;

import java.lang.annotation.*;

/**
 * 自定义职能判断注解
 *
 * @author hjy
 * @date 2023/4/7 10:41
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthCode {
    /**
     * 菜单url
     */
    public String url() default "";

    /**
     * 系统模块
     */
    public String system() default "";

}
