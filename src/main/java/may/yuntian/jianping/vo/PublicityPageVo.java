package may.yuntian.jianping.vo;

import lombok.Data;

import java.util.Date;

@Data
public class PublicityPageVo {
    private static final long serialVersionUID = 1L;

    private Long id;//项目ID

    private String type;//项目类型

    private String identifier;//项目编号

    private String company;//受检单位

    private String projectName;//项目名称

    private Long chargeId; //负责人ID

    private String detectionType;//检测性质

    private Integer pubStatus;//公示状态 （1.未提交，2.待审核，3.主管驳回，4.主管通过/待审核（质控显示‘待审核’），5.质控驳回，6.待公示，7.已公示,8.公示失败）

    private Integer bindingStatus;//胶装状态（0.不显示，1.待胶装，2.已胶装）

    private Date reportCoverDate;//报告封面日期（显示为报告签发日期）

    private Date reportIssue;//实际签发日期

    private Integer issueDay;//当前日期与报告封面日期（显示为报告签发日期）的差值

    private String charge;//负责人

    private Date publicityLastTime;//最近一次操作日期

    private String directorReject;//主管驳回原因

    private String controlReject;//质控驳回原因

    private Date publicityDate;

    private Integer protocol;//有无委托协议（0.无，1.有）

}
