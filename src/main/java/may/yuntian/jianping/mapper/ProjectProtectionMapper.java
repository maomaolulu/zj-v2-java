package may.yuntian.jianping.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import may.yuntian.jianping.entity.ProjectProtectionEntity;
import may.yuntian.jianping.vo.ProtectionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 项目防护用品
 * 数据持久层接口
 * 
 * @author zhanghao
 * @date 2022-03-10
 */
@Mapper
public interface ProjectProtectionMapper extends BaseMapper<ProjectProtectionEntity> {
    @Select(" SELECT pp.*,ap.type,ap.snr,ap.nrr,ap.name,ap.illustrate FROM al_protection ap " +
            "left join zj_project_protection pp on ap.id=pp.protection_id " +
            " ${ew.customSqlSegment} ")
    List<ProtectionVo> getProtectionList(@Param(Constants.WRAPPER) QueryWrapper wrapper);

}
