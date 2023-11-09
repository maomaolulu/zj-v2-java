package may.yuntian.modules.sys.dao;

import may.yuntian.modules.sys.entity.SysCaptchaEntity;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 验证码
 *
 * @author mustang.ma@qq.com
 * @date 2017-10-16
 */
@Mapper
public interface SysCaptchaDao extends BaseMapper<SysCaptchaEntity> {

}
