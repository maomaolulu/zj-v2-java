package may.yuntian.jianping.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * 结果表物质信息DTO
 * @author
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultSubstanceDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 物质ID */
    @Field
    private Long id;

    /** 物质名称 */
    @Field
    private String name;

    /** 物质别名 */
    @Field
    private String alias;

    /** 采样依据表ID */
    @Field(name = "sample_id")
    private Long sampleId;

    /** lab_main_data表id */
    @Field(name = "main_data_id")
    private Long mainDataId;

    /** 展示物质名称 */
    @Field(name = "substance_show")
    private String substanceShow;

    /** 物质类型(1.毒物(包括co/co2)  2.粉尘  3.噪声  4.高温  5.紫外辐射  6.手传震动  7工频电场  8.高频电磁场   9:超高频辐射  10:微波辐射  11:风速   12:照度 13:激光辐射 ) */
    @Field(name = "s_type")
    private Integer sType;

    /**
     * 此采样数据记录在哪张表中
     */
    @Field(name = "sample_tablename")
    private String sampleTablename;

    /** 标识(选择物质时区分同物质的参数) */
    @Field
    private String mark;

    /** 是否根据游离二氧化硅判定(1是 2否) */
    @Field(name = "is_silica")
    private Integer isSilica;

    /** 是否高毒(1非高毒毒物 2高毒毒物) */
    @Field(name = "highly_toxic")
    private Integer highlyToxic;

    /** 关联总尘ID */
    @Field(name = "total_dust_id")
    private Long totalDustId;

    /** 最终物质ID */
    @Field(name = "final_id")
    private String finalId;

    /** 最终物质名称 */
    @Field(name = "final_name")
    private String finalName;

    /** 最终物质别名 */
    @Field(name = "final_alias")
    private String finalAlias;

    /** 最终物质采样依据表ID */
    @Field(name = "final_sample_id")
    private Long finalSampleId;

    /** 最终物质lab_main_data表id */
    @Field(name = "final_main_data_id")
    private Long finalMainDataId;

    /** 最终物质展示名称 */
    @Field(name = "final_substance_show")
    private String finalSubstanceShow;

    /**
     * 物质信息
     */
    @Field(name = "substance_info")
    private SubstanceInfoDto substanceInfoDto;



}
