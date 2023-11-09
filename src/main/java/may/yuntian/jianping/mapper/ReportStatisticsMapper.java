package may.yuntian.jianping.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import may.yuntian.jianping.vo.IssuanceCountVo;
import may.yuntian.jianping.vo.ProjectStatusVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author gy
 * @date 2023-08-08 9:44
 */
@Mapper
public interface ReportStatisticsMapper {

    /**
     * 查询所有项目
     */
    @Select("SELECT ap.`status`, apd.report_filing FROM al_project ap\n" +
            "LEFT JOIN al_project_date apd ON apd.project_id = ap.id " +
            "LEFT JOIN t_category tc ON tc.`name` = ap.type " +
            "${ew.customSqlSegment}")
    List<ProjectStatusVo> selectProjectStatus(@Param(Constants.WRAPPER) QueryWrapper<Object> wrapper);


    /**
     * 签发概况
     */
    @Select("SELECT ap.charge \n" +
            "FROM al_project ap \n" +
            "LEFT JOIN t_project_procedures tpp ON tpp.project_id = ap.id\n" +
            "LEFT JOIN t_category tc ON tc.`name` = ap.type " +
            "${ew.customSqlSegment}")
    List<String> selectIssuanceOverview(@Param(Constants.WRAPPER) QueryWrapper<Object> wrapper);

    /**
     * 签发量统计
     */
    @Select("SELECT DATE_FORMAT(tpp.createtime, '%Y-%m-01') `month`,DATE_FORMAT(tpp.createtime, '%Y-%m-%d') `day`, tc.name_en\n" +
            "FROM al_project ap \n" +
            "LEFT JOIN t_project_procedures tpp ON tpp.project_id = ap.id\n" +
            "LEFT JOIN t_category tc ON tc.`name` = ap.type " +
            "${ew.customSqlSegment}")
    List<IssuanceCountVo> selectIssuanceCount(@Param(Constants.WRAPPER) QueryWrapper<Object> wrapper);
}
