package may.yuntian.jianping.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Map;

/**
 * 轨迹信息DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeSortDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 个体点位数 点位数有几个 就自然排序到几 */
    @Field(name = "point_id")
    private String pointId;

    /** 点位名称 */
    @Field
    private String point;

    /** 样品编号对应的数字 */
    @Field
    private Integer code;

    /** 布局布点中点位排序字段 */
    @Field
    private Integer sort;

    /** 编号得最大值 */
    @Field(name = "max_sample_code")
    private Integer maxSampleCode;



}
