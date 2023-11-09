package may.yuntian.report.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.report.entity.ReportProofreadEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 报告技术校核记录表
 * 数据持久层接口
 *
 * @author LinXin
 * @date 2022-04-14
 */
@Mapper
public interface ReportProofreadMapper extends BaseMapper<ReportProofreadEntity> {
	
}
