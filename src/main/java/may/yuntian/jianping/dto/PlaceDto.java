package may.yuntian.jianping.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * 采样记录位置信息DTO
 * @author
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 车间ID */
    @Field(name = "workshop_id")
    private String workshopId;

    /** 车间名称 */
    @Field
    private String workshop;

    /** 区域ID */
    @Field(name = "area_id")
    private String areaId;

    /** 区域名称 */
    @Field
    private String area;

    /** 车间区域拼接字段 */
    @Field(name = "workshop_area")
    private String workshopArea;

    /** 岗位ID */
    @Field(name = "post_id")
    private String postId;

    /** 岗位名称 */
    @Field
    private String post;



}
