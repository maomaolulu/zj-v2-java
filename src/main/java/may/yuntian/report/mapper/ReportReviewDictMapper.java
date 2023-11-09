package may.yuntian.report.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.anlian.vo.ReportSelectDictVo;
import may.yuntian.report.entity.ReportReviewDictEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 报告技术审核项对应关系表
 * 数据持久层接口
 *
 * @author LinXin
 * @date 2022-04-14
 */
@Mapper
public interface ReportReviewDictMapper extends BaseMapper<ReportReviewDictEntity> {
	
	@Select("SELECT sys_dict_code,value,name FROM t_report_review_dict WHERE project_id = #{projectId} AND type=#{type} ")
	List<ReportSelectDictVo> selectSysDictByProjectIdAndType(@Param("projectId") Long projectId, @Param("type") String type);
}
