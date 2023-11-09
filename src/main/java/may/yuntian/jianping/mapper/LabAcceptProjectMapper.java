package may.yuntian.jianping.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.jianping.entity.LabAcceptProjectEntity;

/**
 * 实验室-接受的项目(按照原始记录单批次区分)
 * 数据持久层接口
 * 
 * @author LiXin
 * @email ''
 * @date 2023-02-17 15:37:49
 */
@Mapper
public interface LabAcceptProjectMapper extends BaseMapper<LabAcceptProjectEntity> {
	
}
