package may.yuntian.modules.sys.service;

import may.yuntian.modules.sys.entity.SysCaptchaEntity;

import java.awt.image.BufferedImage;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 验证码
 *
 * @author mustang.ma@qq.com
 * @date 2018-02-10
 */
public interface SysCaptchaService extends IService<SysCaptchaEntity> {

    /**
     * 获取图片验证码
     */
    BufferedImage getCaptcha(String uuid);

    /**
     * 验证码效验
     * @param uuid  uuid
     * @param code  验证码
     * @return  true：成功  false：失败
     */
    boolean validate(String uuid, String code);
}
