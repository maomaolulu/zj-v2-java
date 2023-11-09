package may.yuntian.external.datainterface.script.dao;

import may.yuntian.external.datainterface.script.entity.CeProDetectionSubstanceEntity;
import may.yuntian.jianping.entity.SubstanceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 脚本操作 持久层
 *
 * @author cwt
 * @Create 2023-4-13 16:10:11
 */
@Mapper
public interface ScriptMapper {


    /**
     * 获取物质的id和name
     *
     * @return
     */
    @Select("SELECT id, `name`\n" +
            "FROM al_substance")
    List<SubstanceEntity> getIdAndName();


    /**
     * 给省平台物质表字段赋值
     *
     * @param name
     * @param id
     */
    @Update("UPDATE\n" +
            "pro_detection_substance \n" +
            "SET\n" +
            "sub_id=#{id}," +
            "sub_name=#{name}" +
            "WHERE item_name=#{name}")
    void updateSubIdAndSubName(@Param("name") String name, @Param("id") Long id);


    /**
     * 获取省平台物质表的 id和关联al_物质表的id
     *
     * @return
     */
    @Select("SELECT \n" +
            "id,sub_id \n" +
            "FROM\n" +
            "pro_detection_substance")
    List<CeProDetectionSubstanceEntity> getIdAndSubId();

    /**
     * 给对接省平台物质表字段赋值
     *
     * @param subId
     * @param id
     */
    @Update("UPDATE\n" +
            "al_substance_copy_to_pro\n" +
            "\n" +
            "SET\n" +
            "pro_id=#{id}" +
            "\n" +
            "WHERE id=#{subId}")
    void updatePorIdBySubId(@Param("subId") Long subId, @Param("id") Long id);
}
