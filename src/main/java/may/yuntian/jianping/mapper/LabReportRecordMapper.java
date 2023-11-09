package may.yuntian.jianping.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.jianping.entity.LabReportRecordEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

/**
 * 报告编制记录表
 * 数据持久层接口
 * 
 * @author LiXin
 * @email ''
 * @date 2023-02-03 16:54:59
 */
@Mapper
public interface LabReportRecordMapper extends BaseMapper<LabReportRecordEntity> {

    @Select("SELECT report_date FROM lab_report_record WHERE project_id = #{projectId} AND report_status >= 3 ORDER BY report_date DESC LIMIT 1")
    Date getReportDateDate(@Param("projectId") Long projectId);

}
