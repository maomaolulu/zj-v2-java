package may.yuntian.jianping.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author gy
 * @date 2023-07-19 14:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignatureDto implements Serializable {
    /** 项目id */
    private Integer project_id;
    /** 类型 */
    private Integer type;
}
