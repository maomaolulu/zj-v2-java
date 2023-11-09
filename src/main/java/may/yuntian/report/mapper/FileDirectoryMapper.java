package may.yuntian.report.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.report.entity.FileDirectoryEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件目录表
 * 数据持久层接口
 * 
 * @author LiXin
 * @email ''
 * @date 2022-04-14 20:45:49
 */
@Mapper
public interface FileDirectoryMapper extends BaseMapper<FileDirectoryEntity> {
	
}
