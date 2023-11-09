package may.yuntian.jianping.dto.Hazards;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;


/**
 *职业病危害种类-化学
 *
 * @author lixin
 * @data 2022-11-11
 */
@Data
public class Chemistry implements Serializable {
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

    /** 铅检测点数 */
    @Field(name = "lead_test_num")
    private Integer leadTestNum;

    /** 铅超标点数 */
    @Field(name = "lead_exceed_num")
    private Integer leadExceedNum;

    /** 苯检测点数 */
    @Field(name = "benzene_test_num")
    private Integer benzeneTestNum;

    /** 铅超标点数 */
    @Field(name = "benzene_exceed_num")
    private Integer benzeneExceedNum;

}
