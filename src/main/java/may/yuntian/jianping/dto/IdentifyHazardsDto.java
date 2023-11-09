package may.yuntian.jianping.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;


/**
 * 检测物质和识别不检测物质共用的dto
 * @author
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdentifyHazardsDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 物质ID */
    @Field(name = "substance_id")
    private Long substanceId;

    /** 物质名称 */
    @Field
    private String name;

    /** 别名 */
    @Field
    private String alias;

    /** 标识(选择物质时区分同物质的参数) */
    @Field
    private String mark;

    /** 是否高毒(1非高毒毒物 2高毒毒物) */
    @Field(name = "highly_toxic")
    private Integer highlyToxic;

    /** 是否通过计量认证(空,A,D,B(18.7)) */
    @Field
    private String authentication;


    /** 物质类型(1.毒物(包括co/co2)  2.粉尘  3.噪声  4.高温  5.紫外辐射  6.手传震动  7工频电场  8.高频电磁场   9:超高频辐射  10:微波辐射  11:风速   12:照度 13:激光辐射 ) */
    @Field(name = "s_type")
    private Integer sType;

    /** 是否可以测个体(1可以 2不可以)  */
    @Field(name = "has_solo")
    private Integer hasSolo;

    /**
     * 所有物质合并之后的名称  例如五苯两酯   乙酸之类(不是五苯两酯的那两个酯类)
     */
    @Field(name = "total_merge_name")
    private String totalMergeName;

    /** 是否根据游离二氧化硅判定(1是 2否) */
    @Field(name = "is_silica")
    private Integer isSilica;

    /** 关联总尘ID */
    @Field(name = "total_dust_id")
    private Long totalDustId;

    /** 关联主物质库的ID */
    @Field(name = "indicator_id")
    private Long indicatorId;

    /** 检测类型(1检测,2验证性检测) */
    @Field(name = "test_type")
    private Integer testType;

    /** 样品数量 */
    @Field(name = "sample_num")
    private Integer sampleNum;

    /** 采样天数 */
    @Field(name = "sample_days")
    private Integer sampleDays;

    /** 关联的岗位/工种ID 如此工种未与任何流动岗关联则关联zj_post_pfn表中的自己 有的话也需关联自己*/
    @Field(name = "pfn_ids")
    private List<String> pfnIds;

    /**
     * 是否有资质 1 有  ； 2 无（默认）
     */
    private Integer isQualification;

    /**
     * 是否有检测能力 1 有  ； 2 无（默认）
     */
    private Integer isDetectionAbility;



}
