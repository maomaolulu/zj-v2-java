package may.yuntian.modules.sys.dao;

import may.yuntian.modules.sys.entity.SysEmailVerifyEntity;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 邮件验证码
 * @date 2021-07-09
 */
@Mapper
public interface SysEmailVerifyDao extends BaseMapper<SysEmailVerifyEntity> {

}
