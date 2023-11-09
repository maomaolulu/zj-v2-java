package may.yuntian.jianping.mongodto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/9/6 10:08
 */
@Data
@Document("zj_plan_record")
public class ZjPlanRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * _id
     */
    @MongoId(FieldType.OBJECT_ID)
    private String _id;
    /**
     * m_id  采样计划重新生成时方便数据更新用(重新生成采样计划时先生成新计划再删除原计划  这是m_id可以保留与后续数据的关联性)
     */
    @NotNull
    @Field(targetType = FieldType.OBJECT_ID)
    private String m_id;
    /**
     * 项目id
     */
    @NotNull
    private Long project_id = 0L;
    /**
     * 项目编号
     */
    private String identifier = "";
    /**
     * 采样方式 1: 定点   2: 个体
     */
    @NotNull
    private Integer is_fixed;
    /**
     * 补采次数(第几次补采, 首次采样为正常采样值为0 出现超标重采为复采  由于未开工导致的采样不算补采 或者需要补充岗位再采样的也不算复采)
     */
    private Integer retest_num = 0;
    /**
     * 关联的所有工种id信息
     */
    private List<String> relation_pfn_ids = new ArrayList<>();
    /**
     * 关联的所有工种id信息   -------当前暂不使用---------------
     */
    // relation_pfn_map = DictField(EmbeddedDocumentField(PfnMapObj), default={})
    /**
     *
     */
    private String pfn_id;
    /**
     * 工种 (检评里展示为岗位/工种 实际对应的是工种，只是这个工种名字叫xxx岗)
     */
    private String pfn;
    /**
     * 检测地点信息
     */
    private Place place;
    /**
     * 点位id
     */
    @NotNull
    private String point_id;
    /**
     * 点位
     */
    @NotNull
    private String point = "";
    /**
     * 点位编号
     */
    @NotNull
    private String point_code = "";
    /**
     * 点位编号的数字
     */
    private Integer point_code_num = 0;
    /**
     * 检测地点  (定点值为 workshop_area + post + point   个体为 pfn + point)
     */
    @NotNull
    private String test_place = "";
    /**
     * 检测物质信息
     */
    private Substance substance;
    /**
     * 检测类型 1:检测   2:验证性检测
     */
    private Integer test_type = 1;
    /**
     * 是否为多物质 1: 是   2: 否
     */
    private Integer multi_substance = 2;
    /**
     * 多物质名称
     */
    private String multi_sub_name;
    /**
     * 多物质样品标签
     */
    private String multi_sub_tag;
    /**
     * 多物质的类型  如五苯两酯、烷烃等
     */
    private String multi_sub_type;
    /**
     * 品编号列表
     */
    private List<String> sample_code_lis = new ArrayList<>();
    /**
     * 空白样编号列表
     */
    private List<String> sample_kb_code_lis = new ArrayList<>();
    /**
     * 已打印的条形码
     */
    private List<String> print_bar_code_lis = new ArrayList<>();
    /**
     * 采样时段的总时长
     */
    private String total_time_frame;
    /**
     * 一共采几天样品
     */
    private Integer sample_days;
    /**
     * 每天采几个样
     */
    private Integer sample_num;
    /**
     * 批次采样信息列表  批次减一为信息的位置数
     */
    private List<BatchGatherLis> batch_gather_lis = new ArrayList<>();
    /**
     * 作业情况
     */
    private Operation operation;
    /**
     * 采样计划说明
     */
    private String sample_note = "";
    /**
     * 是否暂停  1: 是   2:否(默认)
     */
    private Integer is_suspend = 2;
    /**
     * 是否存在 1: 是  2:否
     * 例如 1当其它粉尘总尘和呼尘按游离二氧化硅值判断为其它粉尘时 只有总尘没有呼尘 此时呼尘记录就不存在了；
     * 2 验证性检测物质检测结果都小于检测值时此记录也将不存在
     */
    private Integer is_existed = 1;
    /**
     * 创建时间
     */
    private Date create_time;
    /**
     * 更新时间
     */
    private Date update_time;
//    private String _class;

    @TableField(exist = false)
    private Integer show_type = 0;

}
