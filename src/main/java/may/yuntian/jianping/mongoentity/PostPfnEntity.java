package may.yuntian.jianping.mongoentity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import may.yuntian.jianping.dto.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author
 */
@Data
@Document("zj_post_pfn")
@AllArgsConstructor
@NoArgsConstructor
public class PostPfnEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** _id 唯一识别ID */
    @MongoId(FieldType.OBJECT_ID)
    private String id;

    /** 项目ID */
    @Field(name = "project_id")
    private Long projectId;

    /** 岗位/工种名称 */
    @Field
    private String pfn;

    /** 车间ID */
    @Field(targetType = FieldType.OBJECT_ID,name = "workshop_id")
    private String workshopId;

    /** 区域ID */
    @Field(targetType = FieldType.OBJECT_ID,name = "area_id")
    private String areaId;

    /** 关联车间的_id 定岗绑定他本身 */
    @Field(name = "post_ids")
    private List<String> postIds;

    /** 岗位类型(1定岗 2流动岗 3流动岗交叉岗位) */
    @Field(name = "is_fixed")
    private Integer isFixed;

    /** 岗位总人数 */
    @Field(name = "people_num")
    private Integer peopleNum;

    /** 岗位每班人数 */
    @Field(name = "shift_num")
    private Integer shiftNum;

    /** 是否外包 1是 2否 */
    @Field
    private Integer epiboly;

    /** 工作方式 */
    @Field(name = "work_pattern")
    private String workPattern;

    /** 工作班制 */
    @Field(name = "work_system")
    private String workSystem;

    /** 日工作时长 */
    @Field(name = "working_time")
    private String workingTime;

    /** 周工作天数 */
    @Field(name = "working_days")
    private String workingDays;

    /** 工作时间 */
    @Field(name = "working_hour")
    private String workingHour;

    /** 工作日写实 */
    @Field(name = "realistic")
    private String realistic;

    /** 体力劳动强度 I II III IV */
    @Field(name = "labor_intensity")
    private String laborIntensity;

    /** 检测物质轨迹信息 */
    @Field(name = "work_track")
    private Map<String,WorkTrackDto> workTrack;

    /** 个体检测数量 */
    @Field(name = "test_num")
    private Integer testNum;

    /** 个体点位信息 */
    @Field(name = "code_sort")
    private List<CodeSortDto> codeSort;

    /** 个体检测物质ID数组 */
    @Field(name = "solo_substance_ids")
    private List<Long> soloSubstanceIds;

    /** 个体接触危害物质数组 */
    @Field(name = "solo_hazards")
    private Map<String,TouchHazardsDto> soloHazards;


    /** 数据入库时间 */
    @Field(name = "create_time")
    private Date createTime;

    /** 数据更新时间 */
    @Field(name = "update_time")
    private Date updateTime;

}
