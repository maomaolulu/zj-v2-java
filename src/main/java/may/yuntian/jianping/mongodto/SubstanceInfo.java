package may.yuntian.jianping.mongodto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhuYiCheng
 * @date 2023/9/6 14:41
 */
@Data
public class SubstanceInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 物质ID
     */
    private Long substance_id;
    /**
     * 关联总尘ID
     */
    private Long total_dust_id;
    /**
     * 物质名称
     */
    private String name;
    /**
     * 英文名
     */
    private String name_en;
    /**
     * 化学文摘号CAS No.
     */
    private String cas_no;
    /**
     * 最高容许浓度(mg/ m³)
     */
    private Float mac;
    /**
     * 时间加权平均容许浓度(mg/ m³)
     */
    private Float pc_twa;
    /**
     * 短时间接触容许浓度(mg/ m³)
     */
    private Float pc_stel;
    /**
     * 临界不良反应
     */
    private String reaction;
    /**
     * 是否折算(2否,1是)
     */
    private Integer deduction;
    /**
     * 备注
     */
    private String remaks;
    /**
     * 物质类型(1.毒物(包括co/co2)  2.粉尘  3.噪声  4.高温  5.紫外辐射  6.手传震动  7工频电场  8.高频电磁场   9:超高频辐射  10:微波辐射  11:风速   12:照度 13:激光辐射 )
     */
    private Integer s_type;
    /**
     * 计算方式(1.mac2.pc/twa3.pc/twa+pc/stel)
     */
    private Integer compute_mode;
    /**
     * 标识(选择物质时区分同物质的参数)
     */
    private String mark;
    /**
     * 是否根据游离二氧化硅判定(1是 2否)
     */
    private Integer is_silica;
    /**
     * 备注说明(皮/敏/G1)
     */
    private String remarks_note;
    /**
     * 采样物质所属部门(1.职卫2.环境公卫3.辐射)
     */
    private Integer s_dept;
    /**
     * 是否高毒(1非高毒毒物 2高毒毒物)
     */
    private Integer highly_toxic;
    /**
     * 合并之后的名称  例如五苯两酯里 五苯合并之后的名称都是苯系物  乙酸乙酯和乙酸丁酯合并之后的名称都是乙酸酯类   这样最后把所有合并名称去重就得到了合并之后的名字
     */
    private String merge_name;
    /**
     * 所有物质合并之后的名称  例如五苯两酯   乙酸之类(不是五苯两酯的那两个酯类)
     */
    private String total_merge_name;
    /**
     * 在合并采样的物质中的排序
     */
    private Integer merge_sort;
    /**
     * 关联主物质库的ID
     */
    private Long indicator_id;
    /**
     * 采样依据表ID
     */
    private Long sample_id;
    /**
     * 采样方式(0:无,1定点,2个人)
     */
    private Integer sample_mode;
    /**
     * 此采样数据记录在哪张表中
     */
    private String sample_tablename;
    /**
     * 采样及检测依据
     */
    private String basis;
    /**
     * 检测依据的名称
     */
    private String basis_name;
//    /**
//     * 检测依据启用日期
//     */
//    private Date enableDate;
//
//    /**
//     * 检测依据失效日期
//     */
//    private Date invalidDate;
    /**
     * 采样设备
     */
    private String equipment;
    /**
     * 采样流量(L/min)
     */
    private String flow;
    /**
     * 采样时间(min)
     */
    private String test_time;
    /**
     * 采样时长说明
     */
    private String test_time_note;
    /**
     * 能否个体采样（0：否，1：是）
     */
    private Integer ind_sample;
    /**
     * 个体采样设备
     */
    private String ind_equipment;
    /**
     * 个体采样流量(L/min)
     */
    private String ind_flow;
    /**
     * 个体采样时间(min)
     */
    private String ind_test_time;
    /**
     * 个体采样时长说明
     */
    private String ind_test_time_note;
    /**
     * 收集器
     */
    private String collector;
    /**
     * 保存/运输方式
     */
    private String preserve_traffic;
    /**
     * 样品保存要求
     */
    private String preserve_require;
    /**
     * 样品保存期限(天)
     */
    private Integer shelf_life;
//    /**
//     * 空白样要求
//     */
//    @Field(name = "blank_sample")
//    private String blankSample;
//
//    /**
//     * 空白样数量
//     */
//    @Field(name = "blank_num")
//    private Integer blankNum;
    /**
     * 是否通过计量认证(空,A,D,B(18.7))
     */
    private String authentication;
    /**
     * 通过认证的检测方法序号
     */
    private String basis_num;
//    /**
//     * 备注
//     */
//    @Field
//    private String remarks;
    /**
     * 是否默认
     */
    private Integer is_default;
//    /** 样品数量 */
//    @Field(name = "sample_number")
//    private Integer sampleNumber;
    /**
     * lab_main_data表id
     */
    private Long main_data_id;
    /**
     * 物质
     */
    private String substance;
    /**
     * 检测方法
     */
    private String limited_range;
    /**
     * 检测依据
     */
    private String detect_num;
    /**
     * 检测依据名称
     */
    private String detect_name;
    /**
     * 方法默认
     */
    private Integer is_default_st;
}
