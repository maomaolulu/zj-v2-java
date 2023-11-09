package may.yuntian.anlian.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import may.yuntian.anlian.entity.CommissionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


/**
 * 提成记录
 * 数据持久层接口
 * 
 * @author LiXin
 * @date 2020-12-09
 */
@Mapper
public interface CommissionMapper extends BaseMapper<CommissionEntity> {

	
	@Select("SELECT c.project_id AS projectId ,c.type AS type,c.subjection AS subjection,c.cms_amount AS cmsAmount,c.personnel AS personnel,c.commission_date AS commissionDate,c.count_date AS countDate, " + 
			" p.identifier AS identifier,p.company AS company,p.type AS projectType,p.status AS status,p.salesmen AS salesmen,p.charge AS charge,apd.report_accept AS planRptissuDate,apa.total_money AS totalMoney, " +
			" apa.netvalue AS netvalue,apa.receipt_money AS receiptMoney,apa.nosettlement_money AS nosettlementMoney,apa.commission AS commission,apa.evaluation_fee AS evaluationFee,p.company_order AS companyOrder, " +
			" apa.service_charge AS serviceCharge,apa.subproject_fee AS subprojectFee,apa.other_expenses AS otherExpenses,apd.sign_date AS signDate,apd.report_binding AS rptBindingDate,p.entrust_type AS entrustType, " +
			" apd.report_filing AS filingDate,apd.receive_amount AS receiptDate,p.remarks AS remarks FROM t_commission c ,al_project p , al_project_amount apa, al_project_date apd" +
			" ${ew.customSqlSegment} AND c.state>=2 AND c.state<=3 AND c.project_id=p.id AND c.project_id=apa.project_id AND c.project_id=apd.project_id")
	List<Map<String, Object>> ListCommissionAll(@Param(Constants.WRAPPER) Wrapper<CommissionEntity> userWrapper);
	
}
