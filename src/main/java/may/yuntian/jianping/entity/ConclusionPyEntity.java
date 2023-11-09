package may.yuntian.jianping.entity;

import lombok.Data;

import java.util.Date;

/**
 * 结论
 *
 * @author hjy
 * @description python代码迁移，保持原python风格
 * @date 2023/7/12 14:43
 */
@Data
@SuppressWarnings("all")
public class ConclusionPyEntity {
    /**
     * 自增主键ID
     */
    private Long id;
    /**
     * 项目ID
     */
    private Long project_id;
    /**
     * 检测结果id(mongo表)
     */
    private String result_id;
    /**
     * 采样记录id(mongo表)
     */
    private String record_id;
    /**
     * 检测点ID(mongo表)
     */
    private String point_id;
    /**
     * 岗位ID(mongo表)
     */
    private String post_id;
    /**
     * 采样地点
     */
    private String test_place;
    /**
     * 车间
     */
    private String workshop;
    /**
     * 岗位
     */
    private String post;
    /**
     * 工种ID
     */
    private String pfn_id;
    /**
     * 工种
     */
    private String pfn;
    /**
     * 检测点编号数值
     */
    private Integer point_code_num;
    /**
     * 检测点编号
     */
    private String point_code;
    /**
     * 检测物质ID
     */
    private Long substance_id;
    /**
     * 检测物质
     */
    private String substance;
    /**
     * 粉尘对应总尘的ID
     */
    private Integer total_dust_id;
    /**
     * 1:毒物，2.粉尘, 3.噪声, 4.高温(同al_substance表)
     */
    private Integer s_type;
    /**
     * 检测物质全称
     */
    private String test_item;
    /**
     * 作业人数
     */
    private Integer worker_num;
    /**
     * 检测结果 (结果值)
     */
    private String test_result;
    /**
     * 限值
     */
    private String limit_v;
    /**
     * 检测结果 (符合/不符合)
     */
    private String result;
    /**
     * 补救措施
     */
    private String measures;
    /**
     * 评价结论
     */
    private String conclusion;
    /**
     * 是否存在高毒物品（2不存在，1存在）
     */
    private Integer highly_toxic;
    /**
     * 数据入库时间
     */
    private Date createtime;
    /**
     * 修改时间
     */
    private Date updatetime;
}
