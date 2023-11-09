package may.yuntian.jianping.mongodto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhuYiCheng
 * @date 2023/9/6 16:25
 */
@Data
public class Result implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 是否小于检出限  1: 是   2: 否
     */
    private Integer lt_detection;
    /**
     * 第一次测量结果 / WBGT指数(℃)头部 / 检测读数(PPM)1 / 检测读数( %)1 / 电场强度(KV / m)1 / 电场强度(v / m)1 / 眼、面罩内长波(μw / cm²)
     */
    private String result1 = "";
    /**
     * 第二次测量结果 / WBGT指数(℃)腹部 / 检测读数(PPM)2 / 检测读数( %)2 / 电场强度(KV / m)2 / 电场强度(v / m)2 / 眼、面罩内中波(μw / cm²)
     */
    private String result2 = "";
    /**
     * 第三次测量结果 / WBGT指数(℃)踝部 / 检测读数(PPM)3 / 检测读数( %)3 / 电场强度(KV / m)3 / 电场强度(v / m)3 / 眼、面罩内短波(μw / cm²)
     */
    private String result3 = "";
    /**
     * 第二次测量WBGT指数(℃)头/ 风速2 / 眼、面罩内长波对应修正系数
     */
    private String result4 = "";
    /**
     * 第二次测量WBGT指数(℃)胸部/ 风速3 / 眼、面罩内中波对应修正系数
     */
    private String result5 = "";
    /**
     * 第二次测量WBGT指数(℃)腹部/ 气压 / 眼、面罩内短波对应修正系数
     */
    private String result6 = "";
    /**
     * 第三次测量WBGT指数(℃)头部
     */
    private String result7 = "";
    /**
     * 第三次测量WBGT指数(℃)胸部  /  照射时间（s）
     */
    private String result8 = "";
    /**
     * 第三次测量WBGT指数(℃)腹部  /  波长（nm）
     */
    private String result9 = "";
    /**
     * 测量结果平均值 / WBGT指数平均值(℃) / 平均值(PPM) / 平均值( %) / 电场强度(KV / m) 平均值 / 电场强度(v / m) 平均值 / 眼、面罩内有效照度(μw / cm²)
     */
    private String result_avg = "";
    /**
     * LAeq, Te[dB﹙A﹚] / WBGT(℃) 时间加权平均值  噪声的全天均值
     */
    private String result_time_avg = "";
    /**
     * 检测结果(mg / m3) / 结果浓度(mg / m³)  噪声的等效声级
     */
    private String result = "";
    /**
     * co2检测结果
     */
    private String fruit = "";
    /**
     * 检测结果(mg / m3) 字符串 / 结果浓度(mg / m3)字符串
     */
    private String result_str = "";
    /**
     * 磁场强度(A / m)1 / 眼 / 面罩外长波(μw / cm²)
     */
    private String fruit1 = "";
    /**
     * 磁场强度(A / m)2 / 眼 / 面罩外中波(μw / cm²)
     */
    private String fruit2 = "";
    /**
     * 磁场强度(A / m)3 / 眼面罩外短波(μw / cm²)
     */
    private String fruit3 = "";
    /**
     * 第二次修正WBGT指数(℃)头部 / 眼、面罩外长波对应修正系数
     */
    private String fruit4 = "";
    /**
     * 第二次修正WBGT指数(℃)胸部 / 眼、面罩外中波对应修正系数
     */
    private String fruit5 = "";
    /**
     * 第二次修正WBGT指数(℃)腹部 / 眼、面罩外短波对应修正系数
     */
    private String fruit6 = "";
    /**
     * 第三次修正WBGT指数(℃)头部
     */
    private String fruit7 = "";
    /**
     * 第三次修正WBGT指数(℃)胸部
     */
    private String fruit8 = "";
    /**
     * 第三次修正WBGT指数(℃)腹部
     */
    private String fruit9 = "";
    /**
     * 磁场强度(A / m) 平均值 / 眼、面罩外有效照度(μw / cm²)
     */
    private String fruit_avg = "";
    /**
     * 磁场强度(A / m) 平均值 / 眼、面罩外有效照度(μw / cm²) /修正平均值1  工频电场修正值1
     */
    private String fruit_avg1 = "";
    /**
     * 磁场强度(A / m) 平均值 / 眼、面罩外有效照度(μw / cm²) /修正平均值2  工频电场修正值2
     */
    private String fruit_avg2 = "";
    /**
     * 磁场强度(A / m) 平均值 / 眼、面罩外有效照度(μw / cm²) /修正平均值3  工频电场修正值3
     */
    private String fruit_avg3 = "";
    /**
     * 11 风速 排风罩形式（矩形、条缝罩、圆形罩、其他） 8：高频电磁场 设备名称和频率范围中的设备名称  6: 手传振动 被测仪器设备型号及参数  9: 工频电场 工频设备型号和参数
     * 4: 高温  被测仪器设备型号及参数   2 噪声  测量编号   5紫外  设备  超高频辐射设备型号和参数 14: 微小气候 天气情况
     * 测量位置
     * 微波设备型号参数
     */
    private String about1 = "";
    /**
     * 11 风速 排风罩罩口面积（长×宽/半径×半径 m2）  12 照度  面积（m2）  9: 工频电场 工作内容  4: 高温  劳动强度等级  2: 噪声 测量位置
     * 测量面积  工频电场 测点与电磁场源的距离   超高频辐射 测量距离 8：高频电磁场 设备名称和频率范围中的频率范围
     * 微波辐射类型
     * 激光辐射 辐照度对应的限值
     */
    private String about2 = "";
    /**
     * 11 风速 现场情况   9: 工频电场 防护措施   8：高频电磁场 工频电场 防护措施   超高频辐射 波形
     * 激光辐射 照射量对应的限值
     */
    private String about3 = "";
    /**
     * 激光辐射 设备型号、参数    现场情况
     */
    private String about4 = "";
    /**
     * 排风罩形式（矩形、条缝罩、圆形罩、其他)
     */
    private String about5 = "";
    /**
     * 实验室检测仪器
     */
    private String testing_instrument = "";

}
