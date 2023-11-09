package may.yuntian.external.datainterface.dao;

import may.yuntian.external.datainterface.pojo.bo.ProParticipantTableBO;
import may.yuntian.external.datainterface.pojo.vo.ProBasicInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 检评 数据访问层
 *
 * @author cwt
 * @Create 2023-4-12 17:19:52
 */
@Mapper
public interface EvaluationMapper {

    /**
     * 检评-基本信息
     *
     * @param projectId 项目id
     * @return 结果
     */
    @Select("SELECT DISTINCT(SELECT pu.user_id FROM al_project_user pu WHERE pu.types = 140 AND pu.project_id =#{projectId} ORDER BY\n" +
            "pu.id LIMIT 1 ) AS projectDirectorId,cs.registered_address AS regAddress,p.id AS projectId,p.identifier AS CODE,\n" +
            "p.type AS checkType,(SELECT pu.username FROM al_project_user pu WHERE pu.types = 140 AND pu.project_id = #{projectId}\n" +
            "ORDER BY pu.id LIMIT 1 ) AS projectDirectorName,pd.report_cover_date AS reportDate,p.charge AS preparer,pd.survey_date AS beginSurveyDate,pd.survey_date AS endSurveyDate,pd.start_date AS beginSamplingDate,pd.end_date AS endSamplingDate,cs.entrust_company AS entrustOrgName,pd.entrust_date AS entrustDate,p.company AS empName,cs.unified_code AS empCreditCode,cs.industry_category AS industryCategory,cs.industry_category_code AS industryCategoryCode,cs.labor_quota AS employeesTotalNum,cs.hazard_num AS contactHazardNum,p.contact AS contactPerson,p.telephone AS contactPhone FROM al_project p LEFT JOIN al_project_user pu ON p.id = pu.project_id\n" +
            "LEFT JOIN al_project_date pd ON pu.project_id = pd.project_id LEFT JOIN t_company_survey cs ON pd.project_id = cs.project_id WHERE p.id =#{projectId}")
    ProBasicInfoVO getEvGeneralInfo(@Param("projectId") Long projectId);

    /**
     * 检评-参与人员
     *
     * @param projectId 项目id
     * @return 结果
     */
    @Select("SELECT pu.project_id as projectId ,pu.username as name,pu.user_id as userId ,pu.types \n" +
            "FROM \n" +
            "al_project_user pu\n" +
            "WHERE pu.project_id =#{projectId}")
    List<ProParticipantTableBO> getParticipantInfo(@Param("projectId") Long projectId);

}
