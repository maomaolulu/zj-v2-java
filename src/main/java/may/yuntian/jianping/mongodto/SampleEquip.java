package may.yuntian.jianping.mongodto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhuYiCheng
 * @date 2023/9/6 16:21
 */
@Data
public class SampleEquip implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 仪器名称/型号
     */
    private String name_model_id = "";
    /**
     * 仪器编号
     */
    private String instrument_code = "";
    /**
     * 仪器名称/型号(多仪器选择备用1)
     */
    private String name_model_id1 = "";
    /**
     * 仪器编号(多仪器选择备用1)
     */
    private String instrument_code1 = "";
    /**
     * 校准仪器名称 / 编号 - -声校准器型号 / 编号
     */
    private String calibration_info = "";
    /**
     * 校准仪器名称
     */
    private String calibration_name = "";
    /**
     * 校准仪器编号
     */
    private String calibration_code = "";
    /**
     * 声校准器校准值/校准值/修正值
     */
    private String calib_value = "";
    /**
     * 修正系数
     */
    private String correct_factor;

}
