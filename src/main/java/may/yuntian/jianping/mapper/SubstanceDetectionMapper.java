package may.yuntian.jianping.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.jianping.entity.SubstanceDetectionEntity;

/**
 * 物质检测信息表（职卫）
 * 数据持久层接口
 * 
 * @author LiXin
 * @email ''
 * @date 2023-06-28 15:02:08
 */
@Mapper
public interface SubstanceDetectionMapper extends BaseMapper<SubstanceDetectionEntity> {
	
}
