package may.yuntian.jianping.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 轨迹信息DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkTrackDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 岗位ID 流动岗关联定岗ID 定岗关联zj_workspace表中自己的ID */
    @Field(name = "post_id")
    private String postId;

    /** 关联定岗的接触危害map识别不检测 */
    @Field(name = "identify_hazards")
    private Map<String,TouchHazardsDto> identifyHazardsMap;

    /** 关联定岗的接触危害map识别检测 */
    @Field(name = "touch_hazards")
    private Map<String,TouchHazardsDto> touchHazardsMap;

    /** 个人防护数组 */
    @Field
    private List<ProtectionDto> ppe;

//    /** 个人防护佩戴情况 */
//    @Field(name = "ppe_wear")
//    private String ppeWear;

    /** 危害因素主要来源(报告4.8章节) */
    @Field(name = "hazard_source")
    private String hazardSource;

    /** 工作内容(劳动者作业情况调查中工作内容过程和工作方式作业地点) */
    @Field(name = "work_situation")
    private String  workSituation;

}
