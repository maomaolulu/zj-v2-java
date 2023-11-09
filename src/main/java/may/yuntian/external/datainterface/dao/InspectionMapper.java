package may.yuntian.external.datainterface.dao;

import may.yuntian.external.datainterface.pojo.bo.ProParticipantTableBO;
import may.yuntian.external.datainterface.pojo.vo.ProBasicInfoVO;
import may.yuntian.jianping.entity.IndustryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 评价 数据访问层
 *
 * @author cwt
 * @Create 2023-4-12 17:20:36
 */
@Mapper
public interface InspectionMapper {


    /**
     * 评价-基本信息
     *
     * @param projectId 项目id
     * @return 结果
     */
    @Select("SELECT DISTINCT p.id as projectId,p.identifier as code,p.type AS checkType,pu.username =(SELECT pu.username from al_project_user pu WHERE pu.types=140 and pu.project_id=1490 ORDER BY pu.id LIMIT 1 ) as projectDirectorName ,pd.report_cover_date as reportDate,p.charge as preparer,pd.survey_date as beginSurveyDate,    pd.survey_date as endSurveyDate ,pd.start_date as beginSamplingDate,pd.end_date as endSamplingDate,cs.entrust_company as entrustOrgName,pd.entrust_date as entrustDate,p.company as empName,cs.credit_code as empCreditCode , cs.industry_category as industryCategory,   cs.labor_quota as employeesTotalNum,  p.contact as contactPerson, p.telephone as contactPhone FROM\n" +
            "al_project p \n" +
            "LEFT JOIN al_project_user pu ON p.id =pu.project_id\n" +
            "LEFT JOIN al_project_date pd ON pu.project_id= pd.project_id\n" +
            "LEFT JOIN eval_company_survey cs ON pd.project_id= cs.project_id \n" +
            "WHERE  p.id=#{projectId}")
    ProBasicInfoVO getInGeneralInfo(@Param("projectId") Long projectId);

    /**
     * 评价-参与人员
     *
     * @param projectId 项目id
     * @return 结果
     */
    @Select("SELECT pu.project_id as projectId ,pu.username as name,pu.user_id as userId ,pu.types \n" +
            "FROM \n" +
            "al_project_user pu\n" +
            "WHERE pu.project_id =#{projectId}")
    List<ProParticipantTableBO> getInParticipantInfo(@Param("projectId") Long projectId);

    /**
     * 行业类别+行业类别编码
     *
     * @param substring
     * @return
     */
    @Select("SELECT \n" +
            "letter,code,`name`\n" +
            "FROM t_industry\n" +
            "WHERE joint =#{substring}")
    IndustryEntity getLetterAndCodeAndNameByJoint(@Param("substring") String substring);

}
