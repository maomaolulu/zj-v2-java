package may.yuntian.jianping.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import may.yuntian.jianping.entity.SubstanceEntity;
import may.yuntian.jianping.vo.SubstanceMapVo;
import may.yuntian.jianping.vo.SubstanceNewVo;
import may.yuntian.jianping.vo.SubstanceVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 检测物质数据
 * 数据持久层接口
 *
 * @author LiXin
 * @email ''
 * @date 2022-03-07 09:39:33
 */
@Mapper
public interface SubstanceMapper extends BaseMapper<SubstanceEntity> {

    @Select("SELECT s.*,ss.authentication FROM al_substance s left join al_substance_sample ss on s.id = ss.substance_id WHERE ss.sample_mode = 1 and s.id not in(647, 648, 651, 652, 653, 628, 637, 640, 633)")
    List<SubstanceVo> getListAll();


    @Select("SELECT s.*,sa.authentication FROM al_substance s left join al_substance_sample sa on s.id = sa.substance_id ${ew.customSqlSegment}")
    List<SubstanceVo> getListByShortcutKey(@Param(Constants.WRAPPER) Wrapper<SubstanceEntity> Wrapper);

    /**
     * 分流xin查询物质信息
     *
     * @param companyName
     * @return
     */
    @Select("SELECT md.is_qualification,md.is_detection_ability, s.id,s.total_dust_id,s.name,s.name_en,s.cas_no,s.mac,s.pc_twa,s.pc_stel,s.reaction,s.deduction,s.remaks,s.s_type,s.compute_mode,s.s_dept,s.highly_toxic,s.createtime,s.updatetime,s.remarks_note,s.mark,s.is_silica,s.sjsb_code,s.indicator_id,s.merge_name,s.total_merge_name,s.merge_sort,md.is_qualification as authentication,sd.ind_sample as has_solo\n" +
            " FROM al_substance s LEFT JOIN al_substance_detection sd on s.id=sd.substance_id\n" +
            " LEFT JOIN lab_main_data md on sd.main_data_id=md.id WHERE sd.lab_source=#{companyName} AND s.id not in(648,651,652,653,628,637,640,634,635,636,326,605,606,607,608,609,610,611,612,694) AND sd.main_data_id > 0")
    List<SubstanceNewVo> getListNewAll(@Param("companyName") String companyName);

    /**
     * 获取采样计划需要的物质信息字段
     * @param wrapper wrapper
     * @return result
     */
    @Select("SELECT\n" +
            "\ts.id substance_id,\n" +
            "\ts.total_dust_id,\n" +
            "\ts.`name`,\n" +
            "\ts.name_en,\n" +
            "\ts.cas_no,\n" +
            "\ts.mac,\n" +
            "\ts.pc_twa,\n" +
            "\ts.pc_stel,\n" +
            "\ts.reaction,\n" +
            "\ts.deduction,\n" +
            "\ts.remaks,\n" +
            "\ts.s_type,\n" +
            "\ts.compute_mode,\n" +
            "\ts.mark,\n" +
            "\ts.is_silica,\n" +
            "\ts.remarks_note,\n" +
            "\ts.s_dept,\n" +
            "\ts.highly_toxic,\n" +
            "\ts.merge_name,\n" +
            "\ts.total_merge_name,\n" +
            "\ts.merge_sort,\n" +
            "\ts.indicator_id,\n" +
            "\tsd.id sample_id,\n" +
            "\tsd.sample_tablename,\n" +
            "\tsd.standard_serial_num basis,\n" +
            "\tsd.standard_name basis_name,\n" +
            "\tsd.equipment,\n" +
            "\tsd.flow,\n" +
            "\tsd.test_time,\n" +
            "\tsd.test_time_note,\n" +
            "\tsd.ind_sample,\n" +
            "\tsd.ind_equipment,\n" +
            "\tsd.ind_flow,\n" +
            "\tsd.ind_test_time,\n" +
            "\tsd.ind_test_time_note,\n" +
            "\tsd.collector,\n" +
            "\tsd.preserve_traffic,\n" +
            "\tsd.preserve_require,\n" +
            "\tsd.shelf_life,\n" +
            "\tsd.qualification authentication,\n" +
            "\tIFNULL(sd.detection_method_num, \"\") basis_num,\n" +
            "\tsd.mark_num is_default,\n" +
            "\tsd.main_data_id\n" +
            "FROM\n" +
            "\tal_substance s\n" +
            "\tLEFT JOIN al_substance_detection sd ON s.id = sd.substance_id ${ew.customSqlSegment}")
    List<JSONObject> getSamplePlanSubstanceMap (@Param(Constants.WRAPPER) Wrapper<Object> wrapper);
}
