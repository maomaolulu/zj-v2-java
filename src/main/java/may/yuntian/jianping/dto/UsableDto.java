package may.yuntian.jianping.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 采样设备信息入参
 * @author gy
 * @date 2023-07-13 14:57
 */
@Data
public class UsableDto implements Serializable {

    /** 物质名称 */
    private String sub_name;

    /** 物质类型 */
    private int s_type;

    private int is_fixed;

    private String flow;

    /** 项目id */
    private Integer project_id;

}
