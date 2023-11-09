package may.yuntian.modules.sys.dao;


import may.yuntian.modules.sys.entity.SysLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统日志
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-16
 */
@Mapper
public interface SysLogDao extends BaseMapper<SysLogEntity> {
	
}
