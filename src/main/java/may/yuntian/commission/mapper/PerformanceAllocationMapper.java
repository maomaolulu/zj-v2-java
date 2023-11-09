package may.yuntian.commission.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import may.yuntian.commission.entity.PerformanceAllocationEntity;
import may.yuntian.commission.vo.PerformanceAllocationVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 绩效分配表
 * 数据持久层接口
 * 
 * @author LiXin
 * @email ''
 * @date 2022-06-17 09:54:17
 */
@Mapper
public interface PerformanceAllocationMapper extends BaseMapper<PerformanceAllocationEntity> {



    @Select("SELECT pa.id as id,p.id as projectId,p.charge as charge,p.company as company,p.identifier as identifier,p.netvalue as netvalue,p.included_output as includedOutput,pa.types as types,pa.performance_money as performanceMoney, " +
            " pa.performance_date as performanceDate " +
            " from co_performance_allocation pa left join al_project p on pa.project_id=p.id" +
            " ${ew.customSqlSegment}")
    List<PerformanceAllocationVo> getListByQurryWrapper(@Param(Constants.WRAPPER) Wrapper<PerformanceAllocationEntity> userWrapper);




    @Select("SELECT  MAX(p2.netvalue) as netvalue," +
            " MAX(CASE p2.types WHEN \"签发提成\" THEN amounts ELSE 0 END) AS issueCommission, " +
            "    MAX(CASE p2.types WHEN \"归档提成\" THEN amounts ELSE 0 END) AS fillingCommission  " +
            "    FROM (SELECT pa.types,SUM(IFNULL(pa.performance_money,0)) AS amounts, SUM(IFNULL(p1.netvalue,0)) AS netvalue FROM al_project p1, co_performance_allocation pa " +
            "    ${ew.customSqlSegment} AND p1.id = pa.project_id GROUP BY pa.types) p2")
    Map<String, BigDecimal> getMapByQurryWrapper(@Param(Constants.WRAPPER) Wrapper<PerformanceAllocationEntity> userWrapper);

}
