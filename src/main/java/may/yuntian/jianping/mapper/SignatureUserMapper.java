package may.yuntian.jianping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.jianping.entity.SignatureUserEntity;
import org.apache.ibatis.annotations.Mapper;


/**
 * 数据持久层接口
 * @author : lixin
 * @date : 2022-3-15
 * @desc : 电子签名对应人员表
 */
@Mapper
public interface SignatureUserMapper extends BaseMapper<SignatureUserEntity> {

}
