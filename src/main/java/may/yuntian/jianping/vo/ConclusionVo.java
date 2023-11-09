package may.yuntian.jianping.vo;

import lombok.Data;
import may.yuntian.jianping.entity.ConclusionPyEntity;

import java.util.List;

/**
 * 结论 包装类
 *
 * @author hjy
 * @description 接收实体
 * @date 2023/7/12 16:14
 */
@Data
@SuppressWarnings("all")
public class ConclusionVo {
    /**
     * 结论列表
     */
    private List<ConclusionPyEntity> conclusion_lis;
    /**
     * 结论id
     */
    private List<Long> conclusion_ids;
    /**
     * 项目id
     */
    private Long project_id;
}
