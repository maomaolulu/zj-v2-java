package may.yuntian.modules.sys.dao;

import may.yuntian.modules.sys.entity.SysDeptEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 部门管理
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-16
 */
@Mapper
public interface SysDeptDao extends BaseMapper<SysDeptEntity> {

    List<SysDeptEntity> queryList(Map<String, Object> params);

    /**
     * 查询子部门ID列表
     * @param parentId  上级部门ID
     */
    List<Long> queryDetpIdList(Long parentId);
    
    /**
     * 查询上级及顶级id
     * @param deptId
     * @return
     */
    Long queryDetpId(Long deptId);

}
