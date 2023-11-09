package may.yuntian.jianping.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gy
 * @date 2023-08-15 9:59
 */
@Data
public class PlanRecordDto implements Serializable {
    /**
     * 项目id
     */
    private Long project_id;

    /**
     * 样品id
     */
    private String id;
}
