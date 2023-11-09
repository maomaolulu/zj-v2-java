package may.yuntian.jianping.mongodto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author ZhuYiCheng
 * @date 2023/9/6 11:13
 */
@Data
public class Place implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 车间id
     */
    private String workshop_id;
    /**
     * 车间
     */
    @NotNull
    private String workshop = "";
    /**
     * 区域id
     */
    private String area_id;
    /**
     * 区域
     */
    @NotNull
    private String area = "";
    /**
     * 车间/区域
     */
    private String workshop_area;
    /**
     * 岗位id
     */
    @NotNull
    private String post_id = "";
    /**
     * 岗位(检评里岗位这一层可以不展示)
     */
    private String post;

}
