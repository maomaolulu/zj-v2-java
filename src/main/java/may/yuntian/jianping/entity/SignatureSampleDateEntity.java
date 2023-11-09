package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 采样人采样日期签名信息
 * @author gy
 * @date 2023-07-19 15:40
 */
@Data
@TableName("signature_sample_date")
public class SignatureSampleDateEntity {
    /**
     * id
     */
    private int id;
    /**
     * 项目id
     */
    private Integer projectId;
    /**
     * 采样记录：采样日期\r\n采样计划：报告调查日期
     */
    private String gatherDate;
    /**
     * 采样记录物质类型（1.空气有害物  2.噪声定点  3.噪声个体  4.高温  5.紫外辐射  16.co  .co2  8.高频电磁场 9.工频电场  10.手传振动  11.照度  14.激光辐射  15.超高频辐射 17.排风罩风速 18.微小气候）\r\n采样计划 20\r\n
     */
    private Integer type_;
    /**
     * 采样记录：采样人 测试人，采样计划：方案编制人
     */
    private String sampler;
    /**
     * 采样人id
     */
    private String samplerId;
    /**
     * 采样记录：复核人 采样计划：审核人
     */
    private String checker;
    /**
     * 复核人id
     */
    private String checkerId;
    /**
     * 采样记录：陪同人id(即t_signature表id)\r\n采样计划：受检单位主要负责人id(即t_signature表id)
     */
    private String attendant;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
