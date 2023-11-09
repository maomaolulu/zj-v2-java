package may.yuntian.jianping.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * 样品信息DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GatherMapDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 样品编号 */
    @Field(name = "sample_code")
    private String sampleCode;

    /** 采样时段 */
    @Field(name = "time_frame")
    private String timeFrame;

    /** 接触时长 */
    @Field(name = "contact_duration")
    private String contactDuration;

    /** 佩戴人 */
    @Field
    private String people;

    /** 测试时长(采样持续时长)  */
    @Field(name = "test_duration")
    private Integer testDuration;

    /** 采样前流量 */
    @Field(name = "before_flow")
    private String beforeFlow;

    /** 采样后流量 */
    @Field(name = "after_flow")
    private String afterFlow;

    /** 测试开始时间 */
    @Field(name = "begin_time")
    private String beginTime;

    /** 测试结束时间 */
    @Field(name = "end_time")
    private String endTime;

    /** 采样体积 */
    @Field(name = "sample_volume")
    private String sampleVolume;

    /** 采样仪器详情 */
    @Field(name = "sample_equip")
    private SampleEquipDto sampleEquip;

    /** 采样结果详情 */
    @Field
    private ResultDto result;

    /** 条形码编号 */
    @Field(name = "bar_code")
    private String barCode;

    /** 打印次数 */
    @Field(name = "print_num")
    private Integer printNum;


}
