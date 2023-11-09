package may.yuntian.jianping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.jianping.entity.SampleImgEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采样影像记录
 * 数据持久层接口
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-09-14 22:13:15
 */
@Mapper
public interface SampleImgMapper extends BaseMapper<SampleImgEntity> {
	
}
