package may.yuntian.external.datainterface.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 省平台报送-结果vo
 * @author: liyongqiang
 * @create: 2023-06-20 09:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ResultVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    /** projectId **/
    private Long projectId;
    /** （省）检测项目编码 **/
    private String checkItemCode;
    /** （省）检测项目名称 **/
    private String itemName;
    /** 车间 **/
    private String workArea;
    /** 岗位 **/
    private String detectionArea;
    /** 日接触时间 **/
    private String dailyContactTime;
    /** 周工作天数(0 ~ 7天) **/
    private String weekWorkDay;
    /** 检测点位 **/
    private String pointName;
    /** 检测日期 **/
    private String detectionDate;
    /** 结果项编码（多个以英文逗号分隔）**/
    private String code;
    /** 结果（多个以英文逗号分隔） **/
    private String result;
    /** 检测结果id （备注：根据resultId + detectionDate两字段，可进行数据合并！）**/
    private String resultId;
    /** 计量单位（多个以英文逗号分隔） **/
    private String unit;
    /** 单项结论（1：符合；0：不符合） **/
    private String conclusion;
    /** 危害因素类型：1.化学（包含co/co2） 2.粉尘  3.噪声  4.高温  5.紫外辐射  6.手传振动  7工频电场  8.高频电磁场   9:超高频辐射  10:微波辐射  11:风速   12:照度 13:激光辐射 **/
    private Integer factorType;
    /** 备注 **/
    private String remark;
    /** al_substance表id） **/
    private Long subId;
    /** al_substance表name **/
    private String subName;
}
