package may.yuntian.anlian.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CommissionTimeNodeVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 采样日期
     */
    private Date gatherDate;

    /**
     * 送样日期
     */
    private Date deliverDate;
    /**
     * 物理因素发送日期
     */
    private Date physicalSendDate;
    /**
     * 采样记录发送日期
     */
    private Date gatherSendDate;
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

}
