package may.yuntian.jianping.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.geometry.euclidean.threed.NotARotationMatrixException;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;


/**
 * 采样记录物质详情dto
 * @author
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubstanceInfoDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 物质ID */
    @Field(name = "substance_id")
    private Long substanceId;

    /** 关联总尘ID */
    @Field(name = "total_dust_id")
    private Long totalDustId;

    /** 物质名称 */
    @Field
    private String name;

    /**
     * 英文名
     */
    @Field(name = "name_en")
    private String nameEn;
    /**
     * 化学文摘号CAS No.
     */
    @Field(name = "cas_no")
    private String casNo;
    /**
     * 最高容许浓度(mg/ m³)
     */
    @Field
    private Float mac;
    /**
     * 时间加权平均容许浓度(mg/ m³)
     */
    @Field(name = "pc_twa")
    private Float pcTwa;
    /**
     * 短时间接触容许浓度(mg/ m³)
     */
    @Field(name = "pc_stel")
    private Float pcStel;
    /**
     * 临界不良反应
     */
    @Field
    private String reaction;
    /**
     * 是否折算(2否,1是)
     */
    @Field
    private Integer deduction;
    /**
     * 备注
     */
    @Field
    private String remaks;

    /** 物质类型(1.毒物(包括co/co2)  2.粉尘  3.噪声  4.高温  5.紫外辐射  6.手传震动  7工频电场  8.高频电磁场   9:超高频辐射  10:微波辐射  11:风速   12:照度 13:激光辐射 ) */
    @Field(name = "s_type")
    private Integer sType;

    /**
     * 计算方式(1.mac2.pc/twa3.pc/twa+pc/stel)
     */
    @Field(name = "compute_mode")
    private Integer computeMode;

    /** 标识(选择物质时区分同物质的参数) */
    @Field
    private String mark;

    /** 是否根据游离二氧化硅判定(1是 2否) */
    @Field(name = "is_silica")
    private Integer isSilica;

    /** 备注说明(皮/敏/G1) */
    @Field(name = "remarks_note")
    private String remarksNote;

    /**
     * 采样物质所属部门(1.职卫2.环境公卫3.辐射)
     */
    @Field(name = "s_dept")
    private Integer sDept;

    /** 是否高毒(1非高毒毒物 2高毒毒物) */
    @Field(name = "highly_toxic")
    private Integer highlyToxic;

    /**
     * 合并之后的名称  例如五苯两酯里 五苯合并之后的名称都是苯系物  乙酸乙酯和乙酸丁酯合并之后的名称都是乙酸酯类   这样最后把所有合并名称去重就得到了合并之后的名字
     */
    @Field(name = "merge_name")
    private String mergeName;

    /**
     * 所有物质合并之后的名称  例如五苯两酯   乙酸之类(不是五苯两酯的那两个酯类)
     */
    @Field(name = "total_merge_name")
    private String totalMergeName;

    /**
     * 在合并采样的物质中的排序
     */
    @Field(name = "merge_sort")
    private Integer mergSort;

    /** 关联主物质库的ID */
    @Field(name = "indicator_id")
    private Long indicatorId;

    /** 采样依据表ID */
    @Field(name = "sample_id")
    private Long sampleId;

    /**
     * 采样方式(0:无,1定点,2个人)
     */
    @Field(name = "sample_mode")
    private Integer sampleMode;

    /**
     * 此采样数据记录在哪张表中
     */
    @Field(name = "sample_tablename")
    private String sampleTablename;

    /**
     * 采样及检测依据
     */
    @Field
    private String basis;

    /**
     * 检测依据的名称
     */
    @Field(name = "basis_name")
    private String basisName;

//    /**
//     * 检测依据启用日期
//     */
//    @Field(name = "enable_date")
//    private Date enableDate;
//
//    /**
//     * 检测依据失效日期
//     */
//    @Field(name = "invalid_date")
//    private Date invalidDate;

    /**
     * 采样设备
     */
    @Field
    private String equipment;

    /**
     * 采样流量(L/min)
     */
    @Field
    private String flow;

    /**
     * 采样时间(min)
     */
    @Field(name = "test_time")
    private String testTime;

    /** 采样时长说明 */
    @Field(name = "test_time_note")
    private String testTimeNote;

    /**
     * 能否个体采样（0：否，1：是）
     */
    @Field(name = "ind_sample")
    private Integer indSample;

    /**
     * 个体采样设备
     */
    @Field(name = "ind_equipment")
    private String indEquipment;

    /**
     * 个体采样流量(L/min)
     */
    @Field(name = "ind_flow")
    private String indFlow;

    /**
     * 个体采样时间(min)
     */
    @Field(name = "ind_test_time")
    private String indTestTime;

    /** 个体采样时长说明 */
    @Field(name = "ind_test_time_note")
    private String indTestTimeNote;

    /**
     * 收集器
     */
    @Field
    private String collector;

    /**
     * 保存/运输方式
     */
    @Field(name = "preserve_traffic")
    private String preserveTraffic;

    /**
     * 样品保存要求
     */
    @Field(name = "preserve_require")
    private String preserveRequire;

    /**
     * 样品保存期限(天)
     */
    @Field(name = "shelf_life")
    private Integer shelfLife;

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
    @Field
    private Integer authentication;

    /**
     * 通过认证的检测方法序号
     */
    @Field(name = "basis_num")
    private String basisNum;

//    /**
//     * 备注
//     */
//    @Field
//    private String remarks;

    /** 是否默认 */
    @Field(name = "is_default")
    private Integer isDefault;

//    /** 样品数量 */
//    @Field(name = "sample_number")
//    private Integer sampleNumber;

    /** lab_main_data表id */
    @Field(name = "main_data_id")
    private Long mainDataId;

    /** 物质 */
    @Field
    private String substance;

    /** 检测方法 */
    @Field(name = "limited_range")
    private String limitedRange;

    /** 检测依据 */
    @Field(name = "detect_num")
    private String detectNum;

    /** 检测依据名称 */
    @Field(name = "detect_name")
    private String detectName;

    /** 方法默认 */
    @Field(name = "is_default_st")
    private Integer isDefaultSt;



}
