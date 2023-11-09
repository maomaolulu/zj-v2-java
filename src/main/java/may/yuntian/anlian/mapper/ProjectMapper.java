package may.yuntian.anlian.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 项目表(包含了原任务表的字段)
 * 数据持久层接口
 * 
 * @author LiXin
 * @email ''
 * @date 2022-01-10 14:43:43
 */
@Mapper
public interface ProjectMapper extends BaseMapper<ProjectEntity> {

    @Select("SELECT p.id,p.identifier,p.company,p.status,p.urgent,p.charge,p.remarks,p.company_order,pd.task_release_date,pd.claim_end_date " +
            " FROM al_project p left join al_project_date pd on p.id = pd.project_id " +
            " ${ew.customSqlSegment}")
    List<ProjectTaskVo> getListByQueryWapper(@Param(Constants.WRAPPER) Wrapper<ProjectEntity> userWrapper);


    @Select("SELECT p.id,p.identifier,p.status,p.company,p.company_order,p.office_address,p.status,p.urgent,p.dept_id,p.charge_id,p.charge,p.entrust_type,p.netvalue,p.remarks,pd.task_release_date,pd.survey_date,pd.start_date,pd.end_date,pd.claim_end_date," +
            " pd.plan_survey_date,pd.plan_start_date,pd.plan_end_date,pd.physical_send_date,pd.physical_accept_date,pd.gather_send_date,pd.gather_accept_date,pd.report_cover_date,pd.sign_date,pd.report_issue" +
            " FROM al_project p LEFT JOIN al_project_date pd ON p.id = pd.project_id " +
            " ${ew.customSqlSegment}")
    List<ProjectDateUserVo> getListProjectDateUser(@Param(Constants.WRAPPER) Wrapper<ProjectEntity> userWrapper);

    @Select("SELECT p.identifier,p.company,p.office_address,p.status,p.type,p.charge,p.project_name,p.entrust_type,p.entrust_company," +
            "p.company_order,p.business_source,p.salesmen,p.total_money,p.netvalue,p.remarks,pd.start_date,pd.end_date,pd.task_release_date,pd.deliver_date," +
            "pd.report_issue FROM al_project p left join al_project_date pd on p.id = pd.project_id " +
            " ${ew.customSqlSegment}")
    List<ProjectPutVo> getalllist(@Param(Constants.WRAPPER) Wrapper<ProjectEntity> userWrapper);


    @Select("SELECT p.id,p.identifier,p.company,p.type,p.charge_id,p.charge,p.status,pd.start_date,pd.end_date, " +
            " pd.deliver_date,pd.received_date,pd.physical_send_date,pd.physical_accept_date,pd.gather_send_date, " +
            " pd.gather_accept_date,pd.report_send,pd.report_transfer,pd.report_accept,pd.report_filing " +
            "FROM al_project p left join al_project_date pd on p.id = pd.project_id" +
            " ${ew.customSqlSegment}")
    List<ProjectTimeZoneVo> getTimeZoneList(@Param(Constants.WRAPPER) Wrapper<ProjectEntity> userWrapper);




    @Select("SELECT p.id,p.identifier,p.company,p.charge,p.netvalue,DATE_FORMAT(pd.survey_date,\"%Y-%m-%d\") AS surveyDate,DATE_FORMAT(pd.start_date,\"%Y-%m-%d\") AS startDate,DATE_FORMAT(pd.end_date,\"%Y-%m-%d\") AS endDate," +
            " DATE_FORMAT(pd.plan_survey_date,\"%Y-%m-%d\") AS planSurveyDate,DATE_FORMAT(pd.plan_start_date,\"%Y-%m-%d\") AS planStartDate,DATE_FORMAT(pd.plan_end_date,\"%Y-%m-%d\") AS planEndDate," +
            " DATE_FORMAT(pd.report_cover_date,\"%Y-%m-%d\") AS reportCoverDate,DATE_FORMAT(pd.sign_date,\"%Y-%m-%d\") AS signDate" +
            " FROM al_project p LEFT JOIN al_project_date pd ON p.id = pd.project_id " +
            " ${ew.customSqlSegment}")
    List<ExportSchedulingVo> exportScheduling(@Param(Constants.WRAPPER) Wrapper<ProjectEntity> userWrapper);

}
