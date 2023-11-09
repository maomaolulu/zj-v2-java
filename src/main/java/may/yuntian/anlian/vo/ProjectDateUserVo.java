package may.yuntian.anlian.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import may.yuntian.anlian.entity.ProjectProceduresEntity;
import may.yuntian.jianping.entity.ProjectUserEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ProjectDateUserVo {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 受检企业名称
     */
    private String company;
    /**
     * 受检企业名称
     */
    private String officeAddress;
    /**
     * 项目编号
     */
    private String identifier;
    /**
     * 委托类型
     */
    private String entrustType;
    /**
     * 加急状态(0正常，1较急、2加急)
     */
    private Integer urgent;
    /**
     * 项目净值(元)
     */
    private BigDecimal netvalue;

    /**
     * 任务下发日期
     */
    private Date taskReleaseDate;
    /**
     * 计划完成日期
     */
    private Date planFinishDate;

    /**
     * 要求报告完成日期
     */
    private Date claimEndDate;

    /**
     * 计划调查日期
     */
    private Date planSurveyDate;
    /**
     * 计划采样开始日期
     */
    private Date planStartDate;
    /**
     * 计划采样结束日期
     */
    private Date planEndDate;

    /**
     * 物理因素发送日期
     */
    private Date physicalSendDate;
    /**
     * 物理因素接收日期
     */
    private Date physicalAcceptDate;
    /**
     * 采样记录发送日期
     */
    private Date gatherSendDate;
    /**
     * 原始记录接收日期
     */
    private Date gatherAcceptDate;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 负责人ID
     */
    private Long chargeId;
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
     * 实际调查日期
     */
    private Date surveyDate;
    /**
     * 实际采样开始日期
     */
    private Date startDate;

    /**
     * 实际采样开始日期
     */
    private Date endDate;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 项目状态
     */
    private Integer status;

    /**
     * 任务人员相关
     */
    private List<ProjectUserEntity> projectUserList;

    /**
     * 状态流程
     */
    private List<ProjectProceduresEntity> projectProceduresEntityList;

    /**
     * 报告封面日期
     */
    private Date reportCoverDate;
    /**
     * 项目签订日期
     */
    private Date signDate;

    /**
     * 实验室报告出具日期
     */
    private Date labReportDate;

    /**
     * 报告编制日期
     */
    private Long reportDay;

    /**
     * 报告签发日期
     */
    private Date reportIssue;

    /**
     * 是否上传委托协议  1 是 2 否
     */
    private Integer isProtocol;
    /**
     * 项目隶属
     */
    private String companyOrder;

}
