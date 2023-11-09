package may.yuntian.anlian.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Data
public class ExportSchedulingVo {


    /**
     * id
     */
    private Long id;
    /**
     * 项目编号
     */
    private String identifier;
    /**
     * 受检企业名称
     */
    private String company;
    /**
     * 项目净值(元)
     */
    private BigDecimal netvalue;
    /**
     * 实际调查日期
     */
    private String planSurveyDate;
    /**
     * 实际采样开始日期
     */
    private String planStartDate;
    /**
     * 实际采样结束日期
     */
    private String planEndDate;
    /**
     * 负责人
     */
    private String charge;

    /**
     * 采样人员
     */
    private String username;

    /**
     * 报告调查人员
     */
    private String surveyName;

    /**
     * 报告采样人员
     */
    private String sampleName;

    /**
     * 实验室人员
     */
    private String labPerson;

    /**
     * 报告调查日期
     */
    private String surveyDate;
    /**
     * 报告采样开始日期
     */
    private String startDate;

    /**
     * 报告采样开始日期
     */
    private String endDate;

    /**
     * 项目签订日期
     */
    private String signDate;

    /**
     * 报告封面日期
     */
    private String reportCoverDate;

}
