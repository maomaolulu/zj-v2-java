package may.yuntian.jianping.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * 采样记录物质信息DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubstanceDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 原物质ID */
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

    /** 物质详情 */
    @Field(name = "substance_info")
    private SubstanceInfoDto substanceInfo;

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

    /** 最终物质详情 */
    @Field(name = "final_substance_info")
    private SubstanceInfoDto finalSubstanceInfo;

}
