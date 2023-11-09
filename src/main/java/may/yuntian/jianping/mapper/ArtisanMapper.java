package may.yuntian.jianping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.jianping.entity.ArtisanEntity;
import org.apache.ibatis.annotations.Mapper;


/**
 * 技术人员信息
 * 数据持久层接口
 * 
 * @author LiXin
 * @date 2020-11-17
 */
@Mapper
public interface ArtisanMapper extends BaseMapper<ArtisanEntity> {

}
