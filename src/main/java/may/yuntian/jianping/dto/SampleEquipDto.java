package may.yuntian.jianping.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * 采样仪器信息DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SampleEquipDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 仪器名称/型号 */
    @Field(name = "name_model_id")
    private String nameModelId;

    /** 仪器编号 */
    @Field(name = "instrument_code")
    private String instrumentCode;

    /** 仪器名称/型号  多仪器选择备用 */
    @Field(name = "name_model_id1")
    private String nameModelId1;

    /** 仪器编号  多仪器选择备用 */
    @Field(name = "instrument_code1")
    private String instrumentCode1;

    /** 校准仪器名称/编号--声校准器型号/编号 */
    @Field(name = "calibration_info")
    private String calibrationInfo;

    /** 校准仪器名称 */
    @Field(name = "calibration_name")
    private String calibrationName;

    /** 校准仪器编号 */
    @Field(name = "calibration_code")
    private String calibrationCode;

    /** 声校准器校准值/校准值/修正值 */
    @Field(name = "calib_value")
    private String calibValue;

    /** 校正因素 */
    @Field(name = "correct_factor")
    private String correctFactor;




}
