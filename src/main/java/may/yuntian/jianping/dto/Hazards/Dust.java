package may.yuntian.jianping.dto.Hazards;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;


/**
 *危害因素检测-粉尘
 *
 * @author lixin
 * @data 2022-11-11
 */
@Data
public class Dust implements Serializable {
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

    /** 矽尘检测点数 */
    @Field(name = "silica_test_num")
    private Integer silicaTestNum;

    /** 矽尘超标点数 */
    @Field(name = "silica_exceed_num")
    private Integer silicaExceedNum;

    /** 煤尘检测点数 */
    @Field(name = "coal_test_num")
    private Integer coalTestNum;

    /** 煤尘超标点数 */
    @Field(name = "coal_exceed_num")
    private Integer coalExceedNum;

    /** 石棉粉尘检测点数 */
    @Field(name = "asbestos_test_num")
    private Integer asbestosTestNum;

    /** 石棉粉尘超标点数 */
    @Field(name = "asbestos_exceed_num")
    private Integer asbestosExceedNum;

}
