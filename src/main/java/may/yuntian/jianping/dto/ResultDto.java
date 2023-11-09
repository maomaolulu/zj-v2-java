package may.yuntian.jianping.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * 轨迹信息DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 是否小于检出值(1 是, 2 否) */
    @Field(name = "lt_detection")
    private Integer ltDetection;

    /** 第一次测量结果/WBGT指数(℃)头部/检测读数(PPM)1/检测读数(%)1/电场强度(KV/m)1/电场强度(v/m)1/眼、面罩内长波(μw/cm²) */
    @Field
    private String result1 = "";

    /** 第二次测量结果/WBGT指数(℃)腹部/检测读数(PPM)2/检测读数(%)2/电场强度(KV/m)2/电场强度(v/m)2/眼、面罩内中波(μw/cm²) */
    @Field
    private String result2 = "";

    /** 第三次测量结果/WBGT指数(℃)踝部/检测读数(PPM)3/检测读数(%)3/电场强度(KV/m)3/电场强度(v/m)3/眼、面罩内短波(μw/cm²) */
    @Field
    private String result3 = "";

    /** 第二次测量WBGT指数(℃)头/风速2 */
    @Field
    private String result4 = "";

    /** 第二次测量WBGT指数(℃)胸部/风速3 */
    @Field
    private String result5 = "";

    /** 第二次测量WBGT指数(℃)腹部/气压 */
    @Field
    private String result6 = "";

    /** 第三次测量WBGT指数(℃)头部 */
    @Field
    private String result7 = "";

    /** 第三次测量WBGT指数(℃)胸部/照射时间（s） */
    @Field
    private String result8 = "";

    /** 第三次测量WBGT指数(℃)腹部/波长（nm） */
    @Field
    private String result9 = "";

    /** 测量结果平均值/WBGT指数平均值(℃)/平均值(PPM)/平均值(%)/电场强度(KV/m)平均值/电场强度(v/m)平均值/眼、面罩内有效照度(μw/cm²) */
    @Field(name = "result_avg")
    private String resultAvg = "";

    /** LAeq,Te[dB﹙A﹚]/WBGT(℃)时间加权平均值噪声的全天均值 */
    @Field(name = "result_time_avg")
    private String resultTimeAvg = "";

    /** 检测结果(mg/m3)/结果浓度(mg/m³)噪声的等效声级 */
    @Field
    private String result = "";

    /** 检测结果(mg/m3)字符串/结果浓度(mg/m3)字符串 */
    @Field(name = "result_str")
    private String resultStr = "";

    /** co2检测结果 */
    @Field
    private String fruit = "";

    /** 磁场强度(A/m)1/眼/面罩外长波(μw/cm²) */
    @Field
    private String fruit1 = "";

    /** 磁场强度(A/m)2/眼/面罩外中波(μw/cm²) */
    @Field
    private String fruit2 = "";

    /** 磁场强度(A/m)3/眼面罩外短波(μw/cm²) */
    @Field
    private String fruit3 = "";

    /** 第二次修正WBGT指数(℃)头部 */
    @Field
    private String fruit4 = "";

    /** 第二次修正WBGT指数(℃)胸部 */
    @Field
    private String fruit5 = "";

    /** 第二次修正WBGT指数(℃)腹部 */
    @Field
    private String fruit6 = "";

    /** 第三次修正WBGT指数(℃)头部 */
    @Field
    private String fruit7 = "";

    /** 第三次修正WBGT指数(℃)胸部 */
    @Field
    private String fruit8 = "";

    /** 第三次修正WBGT指数(℃)腹部 */
    @Field
    private String fruit9 = "";

    /** 磁场强度(A/m)平均值/眼、面罩外有效照度(μw/cm²) */
    @Field(name = "fruit_avg")
    private String fruitAvg = "";

    /** 磁场强度(A/m)平均值/眼、面罩外有效照度(μw/cm²)/修正平均值1工频电场修正值1 */
    @Field(name = "fruit_avg1")
    private String fruitAvg1 = "";

    /** 磁场强度(A/m)平均值/眼、面罩外有效照度(μw/cm²)/修正平均值2工频电场修正值2 */
    @Field(name = "fruit_avg2")
    private String fruitAvg2 = "";

    /** 磁场强度(A/m)平均值/眼、面罩外有效照度(μw/cm²)/修正平均值3工频电场修正值3 */
    @Field(name = "fruit_avg3")
    private String fruitAvg3 = "";

    /** 检测结果 */
    @Field
    private String about1 = "";

    /** 检测结果 */
    @Field
    private String about2 = "";

    /** 检测结果 */
    @Field
    private String about3 = "";

    /** 检测结果 */
    @Field
    private String about4 = "";

    /** 检测结果 */
    @Field
    private String about5 = "";

    /** 实验室检测仪器 */
    @Field(name = "testing_instrument")
    private String testingInstrument = "";





}
