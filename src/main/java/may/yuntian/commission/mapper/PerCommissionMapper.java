package may.yuntian.commission.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import may.yuntian.commission.entity.PerCommissionEntity;
import may.yuntian.commission.vo.PojectCommissionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @program: ANLIAN-JAVA
 * @description:
 * @author: liyongqiang
 * @create: 2022-05-29 18:41
 */
@Mapper
@SuppressWarnings("all")
public interface PerCommissionMapper extends BaseMapper<PerCommissionEntity> {


    @Select("SELECT c.id,c.project_id as projectId,c.dept_name as deptName,c.state,c.subjection,c.personnel,c.type,c.cms_amount as cmsAmount,c.commission_date as commissionDate,c.count_date as countDate,p.company,p.type as projectType,p.identifier,p.status,p.salesmen,p.company_order as companyOrder,p.charge,pa.total_money as totalMoney, " +
            " pa.netvalue,pa.receipt_money as receiptMoney,(pa.total_money-pa.receipt_money) as totalMoneyOutstanding,pa.commission,pa.evaluation_fee as evaluationFee,pa.service_charge as serviceCharge,pa.subproject_fee as subprojectFee,p.entrust_type as entrustType,p.remarks, " +
            " pa.other_expenses as otherExpenses,pd.receive_amount as receiveAmount,pd.sign_date as signDate,pd.report_binding as reportBinding,pd.report_filing as reportFiling " +
            " FROM co_commission c  " +
            " left join al_project p on c.project_id = p.id " +
            " left join al_project_amount pa on pa.project_id = p.id " +
            " left join al_project_date pd on pd.project_id = p.id ${ew.customSqlSegment}")
    List<PojectCommissionVo> listProjectCommission(@Param(Constants.WRAPPER) Wrapper<PerCommissionEntity> userWrapper);


    @Select("SELECT c.project_id as projectId,c.dept_name as deptName,c.subjection,group_concat(c.personnel) as personnel,c.type,SUM(c.cms_amount) as cmsAmount,c.commission_date as commissionDate,c.count_date as countDate,p.company,p.type as projectType,p.identifier,p.status,p.salesmen,p.company_order as companyOrder,p.charge,pa.total_money as totalMoney, " +
            " pa.netvalue,pa.receipt_money as receiptMoney,(pa.total_money-pa.receipt_money) as totalMoneyOutstanding,pa.commission,pa.evaluation_fee as evaluationFee,pa.service_charge as serviceCharge,pa.subproject_fee as subprojectFee,p.entrust_type as entrustType,p.remarks, " +
            " pa.other_expenses as otherExpenses,pd.receive_amount as receiveAmount,pd.sign_date as signDate,pd.report_binding as reportBinding,pd.report_filing as reportFiling " +
            " FROM co_commission c  " +
            " left join al_project p on c.project_id = p.id " +
            " left join al_project_amount pa on pa.project_id = p.id " +
            " left join al_project_date pd on pd.project_id = p.id ${ew.customSqlSegment} GROUP BY " +
            " c.project_id,c.subjection,c.type,c.dept_name,c.commission_date,c.count_date,p.company,p.type,p.identifier,p.status,p.salesmen,p.company_order,p.charge,pa.total_money , " +
            " pa.netvalue,pa.receipt_money,pa.commission,pa.evaluation_fee,pa.service_charge,pa.subproject_fee,p.entrust_type,p.remarks, " +
            " pa.other_expenses,pd.receive_amount,pd.sign_date,pd.report_binding,pd.report_filing" )
    List<PojectCommissionVo> listTypeCommission(@Param(Constants.WRAPPER) Wrapper<PerCommissionEntity> userWrapper);



}
