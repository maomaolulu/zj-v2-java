package may.yuntian.jianping.vo;

import lombok.Data;
import may.yuntian.jianping.dto.PointMapDto;
import may.yuntian.jianping.dto.TouchHazardsDto;

import java.io.Serializable;
import java.util.List;

/**
 * @author
 */
@Data
public class WorkShopListVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 车间ID */
    private String workshopId;

    /** 车间名称 */
    private String workshop;

    /** 岗位List */
    private List<PostPfnVo> postPfnVo;





}
