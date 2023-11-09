package may.yuntian.anlian.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import may.yuntian.anlian.entity.CategoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 类型信息记录
 * 数据持久层接口
 * 
 * @author LiXin
 * @date 2020-12-10
 */
@Mapper
public interface CategoryMapper extends BaseMapper<CategoryEntity> {


	/**
	 * 通过模块查询出没有子集的子集列表
	 * @param module
	 * @return
	 */
	@Select("select id,name,name_en,pid,is_old, (select count(*) from t_category where pid=t.id) as child from t_category as t where module = #{module} HAVING child = 0")
	List<CategoryEntity> getList(@Param("module") String module);
	
}
