package may.yuntian.jianping.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * 气象条件DTO
 * @author
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SceneDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 温度 */
    @Field
    private String temp;

    /** 湿度 */
    @Field
    private String humidity;

    /** 气压 */
    @Field
    private String pressure;

    /** 风速 */
    @Field(name = "wind_speed")
    private String windSpeed;


}
