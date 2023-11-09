package may.yuntian.jianping.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import may.yuntian.jianping.vo.PublicityPageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PublicityMapper {


    /**
     * 公示相关列表页
     * @param wrapper
     * @return
     */
    @Select("SELECT p.id,p.identifier,p.company,p.project_name,p.charge_id,p.pub_status,p.binding_status,p.protocol,pd.report_cover_date,pd.report_issue,DATEDIFF(NOW(),pd.report_cover_date) as issueDay,p.charge,p.publicity_last_time " +
            " FROM al_project p " +
            " left join al_project_date pd on p.id = pd.project_id " +
            " ${ew.customSqlSegment}")
    List<PublicityPageVo> getPublicityList(@Param(Constants.WRAPPER) QueryWrapper wrapper);





}
