package may.yuntian.jianping.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import may.yuntian.jianping.entity.CompanySurveyEntity;
import may.yuntian.jianping.vo.CompanySurveyVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CompanySurveyMapper extends BaseMapper<CompanySurveyEntity> {

    /**
     * 3表联查
     *
     * @param wrapper
     * @return
     */
    @Select("select cs.id,cs.project_id,cs.company,cs.contact,cs.office_address,cs.detection_type,apd.report_cover_date report_issue,cs.accompany,apd.survey_date " +
            ",cs.publicity_status,cs.technical_persons,cs.sampling_company,cs.sampling_date,cs.publicity_path path,ap.apply_publicity_status " +
            ",ap.charge,cs.test_date samplingDate , apd.report_accept,ap.director_reject,ap.control_reject,ap.identifier,ap.publicity_last_time, " +
            "cs.laboratory_person,ap.hide_status,ap.hide_remark " +
            "from  al_project ap  " +
            "left join  t_company_survey cs on ap.id= cs.project_id " +
            "left join al_project_date apd on cs.project_id=apd.project_id " +
            "${ew.customSqlSegment}  ")
    List<CompanySurveyVo> publicityList(@Param(Constants.WRAPPER) QueryWrapper wrapper);

}
