package may.yuntian.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.modules.sys.dao.SysEmailVerifyDao;
import may.yuntian.modules.sys.entity.SysEmailVerifyEntity;
import may.yuntian.modules.sys.service.SysEmailVerifyService;

import org.springframework.stereotype.Service;


/**
 * 邮件验证码
 * @date 2021-07-09
 */
@Service("sysEmailVerifyService")
public class SysEmailVerifyServiceImpl extends ServiceImpl<SysEmailVerifyDao, SysEmailVerifyEntity> implements SysEmailVerifyService {

}
