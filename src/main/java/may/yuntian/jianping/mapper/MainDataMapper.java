package may.yuntian.jianping.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.jianping.entity.MainDataEntity;

/**
 * 实验室-主数据
 * 数据持久层接口
 * 
 * @author LiXin
 * @email ''
 * @date 2022-09-13 16:54:23
 */
@Mapper
public interface MainDataMapper extends BaseMapper<MainDataEntity> {
	
}
