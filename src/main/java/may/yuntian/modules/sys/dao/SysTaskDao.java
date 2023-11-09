package may.yuntian.modules.sys.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.modules.sys.entity.SysTaskEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务计划、定时器
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2018-02-16
 */
@Mapper
public interface SysTaskDao extends BaseMapper<SysTaskEntity> {
	
}
