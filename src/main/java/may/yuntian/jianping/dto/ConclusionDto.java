package may.yuntian.jianping.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * 物理结果DTO
 * @author
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConclusionDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @Field
    private Integer accuracy;

    @Field(name = "limit_v")
    private String limitV;

    @Field(name = "is_noise")
    private Integer isNoise;

    @Field(name = "contact_rate")
    private Integer contactRate;

    @Field(name = "labor_intensity")
    private String laborIntensity;

    @Field(name = "max_v")
    private String maxV;

    @Field(name = "min_v")
    private String minV;

    @Field(name = "test_range")
    private String testRange;

    @Field
    private String conclusion;

}
