package may.yuntian.jianping.dto;

import lombok.Data;

/**
 * @author gy
 * @date 2023-08-04 11:18
 */
@Data
public class MessageDto {

    /**
     * 接收人ID
     */
    private Long receiverId;

    /**
     * 业务类型(0:检评)
     */
    private Integer businessType;
}
