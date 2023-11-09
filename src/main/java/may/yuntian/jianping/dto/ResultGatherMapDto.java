package may.yuntian.jianping.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * 样品信息DTO
 * @author
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultGatherMapDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 样品编号 */
    @Field(name = "sample_code")
    private String sampleCode;

    /** 接害时长 */
    @Field(name = "contact_duration")
    private String contactDuration;



}
