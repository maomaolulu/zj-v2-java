package may.yuntian.jianping.dto;

import lombok.Data;
import may.yuntian.jianping.dto.Hazards.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;


/**
 *危害因素检测
 *
 * @author lixin
 * @data 2022-11-14
 */
@Data
public class HazardsTestDto implements Serializable {
	private static final long serialVersionUID = 1L;

    /** 粉尘危害因素检测 */
    @Field
    private Dust dust;

    /** 化学危害因素检测 */
    @Field
    private Chemistry chemistry;

    /** 物理危害因素检测 */
    @Field
    private Physics physics;

    /** 放射性危害因素检测 */
    @Field
    private Radiation radiation;

    /** 生物危害因素检测 */
    @Field
    private Biology biology;

//    /** 其他危害因素检测 */
//    @Field
//    private Other other;

}
