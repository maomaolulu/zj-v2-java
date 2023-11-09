package may.yuntian.jianping.mongodto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author ZhuYiCheng
 * @date 2023/9/6 13:18
 */
@Data
public class Substance implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 方案时的物质
     */
    @NotNull
    private Long id;
    @NotNull
    private String name = "";
    @NotNull
    private String alias = "";
    @NotNull
    private Integer sample_id = 0;
    @NotNull
    private Integer main_data_id = 0;
    /**
     * 方案显示的物质名称
     */
    private String substance_show;
    private SubstanceInfo substance_info;

    /**
     * 检测后的物质
     */
    @NotNull
    private Integer final_id;
    @NotNull
    private String final_name = "";
    @NotNull
    private String final_alias = "";
    @NotNull
    private Integer final_sample_id = 0;
    @NotNull
    private Integer final_main_data_id = 0;
    /**
     * 计算后显示的物质名称
     */
    private String final_substance_show;
    private SubstanceInfo final_substance_info;


}
