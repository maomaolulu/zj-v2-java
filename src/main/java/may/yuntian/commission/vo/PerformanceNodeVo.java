package may.yuntian.commission.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PerformanceNodeVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 环境任务ID
     */
    private Long planId;

    /**
     * 是否是有效时间（1全部，2送样时间，3物理时间）
     */
    private Integer isTime;
    /**
     * 收样日期
     */
    private Date receivedDate;
    /**
     * 物理因素接收日期
     */
    private Date physicalAcceptDate;
    /**
     * 原始记录接收日期
     */
    private Date gatherAcceptDate;
    /**
     * 报告签发日期
     */
    private Date reportIssue;
    /**
     * 报告归档日期
     */
    private Date reportFiling;




}
