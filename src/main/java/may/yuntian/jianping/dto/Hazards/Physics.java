package may.yuntian.jianping.dto.Hazards;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;


/**
 *职业病危害种类-噪声
 *
 * @author lixin
 * @data 2022-11-11
 */
@Data
public class Physics implements Serializable {
	private static final long serialVersionUID = 1L;

    /** 是否检测 */
    @Field
    private String detection;

    /** 检测点位数 */
    @Field(name = "test_num")
    private Integer testNum;

    /** 超标点数 */
    @Field(name = "exceed_num")
    private Integer exceedNum;

    /** 噪声检测点数 */
    @Field(name = "noise_test_num")
    private Integer noiseTestNum;

    /** 噪声超标点数 */
    @Field(name = "noise_exceed_num")
    private Integer noiseExceedNum;

}
