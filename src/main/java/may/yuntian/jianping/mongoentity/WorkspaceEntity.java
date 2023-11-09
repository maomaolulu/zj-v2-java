package may.yuntian.jianping.mongoentity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import may.yuntian.jianping.dto.EngineDto;
import may.yuntian.jianping.dto.IdentifyHazardsDto;
import may.yuntian.jianping.dto.PointMapDto;
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
@Document("zj_workspace")
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** _id 唯一识别ID */
    @MongoId(FieldType.OBJECT_ID)
    private String id;

    /** 项目ID */
    @Field(name = "project_id")
    private Long projectId;

    /** 车间ID */
    @Field(targetType = FieldType.OBJECT_ID,name = "workshop_id")
    private String workshopId;

    /** 车间名称 */
    @Field
    private String workshop;

    /** 区域ID */
    @Field(targetType = FieldType.OBJECT_ID,name = "area_id")
    private String areaId;

    /** 区域名称 */
    @Field
    private String area;

    /** 车间区域拼接字段 */
    @Field(name = "workshop_area")
    private String workshopArea;

    /** 游离二氧化硅共用(0未被共用,-1已被共用,有值共用岗位的ID) */
    @Field(name = "silica_share")
    private String silicaShare;

    /** 岗位 */
    @Field
    private String post;




    /** 关联定点工种的ID 可能没有 */
    @Field(name = "fixed_pfn_id")
    private String fixedPfnId;

    /** 识别不检测物质合集(identifyHazardsList数组中的物质名称拼接) */
    @Field(name = "only_identify")
    private String onlyIdentify;

    /** 识别不检测物质数组 */
    @Field(name = "identify_hazards")
    private List<IdentifyHazardsDto> identifyHazards;

    /** 识别不检测物质合集(fixedHazardsList数组中的物质名称拼接) */
    @Field(name = "test_hazards")
    private String testHazards;



    /** 定点岗位检测物质数组 */
    @Field(name = "fixed_hazards")
    private List<IdentifyHazardsDto> fixedHazards;

    /** 检测点位数量 */
    @Field(name = "point_num")
    private Integer pointNum;

    /** 关联的岗位/工种ID 如此工种未与任何流动岗关联则关联zj_post_pfn表中的自己 有的话也需关联自己*/
    @Field(name = "pfn_ids")
    private List<String> pfnIds;

    /** 检测点位详情 根据点位数量添加 检测点位数量为0时此Map对象为空*/
    /**
     *     "point_map" : { 字段名
     *         "62d4ffb9aed0037d07dd49dd" : { 生成的唯一识别的objectId字符串 key值
     *             "point_id" : "62d4ffb9aed0037d07dd49dd", 点位ID 生成的唯一识别的objectId字符串 与key值相同
     *             "point" : "1",  点位名称  检评为数字  评价是名称
     *             "pfn_ids" : [],  关联的岗位/工种ID 如此工种未与任何流动岗关联则关联zj_post_pfn表中的自己 有的话也需关联自己
     *             "img_id" : 0,  布局布点图片ID
     *             "code" : 0,   样品编号对应的数字
     *             "sort" : 0    布局布点中点位排序字段
     *         }
     *     }
     *
     */
    @Field(name = "point_map")
    private Map<String,PointMapDto> pointMap;

    /** 点位ID数组 */
    @Field(name = "point_ids")
    private List<String> pointIds;

    /** 工种对应点位的Map集合 */
    /**
     * "pfn_points": { // 字段名称  可能有多个 也可能为空
     *         "工种ID1": [  对应工种表zj_post_pfn 的ID
     *             "点位ID1", 本身点位的ID 可能有多个 也可能为空
     *             "点位ID2"
     *         ],
     *         "工种ID2": [
     *             "点位ID3",
     *             "点位ID4"
     *         ]
     *     },
     */
    @Field(name = "pfn_points")
    private Map<String,List<String>> pfnPoints;

    /** 工程防护数组 */
    @Field
    private List<EngineDto> epe;

//    /** 工程防护运行情况 */
//    @Field(name = "epe_run")
//    private String epeRun;

    /** 数据入库时间 */
    @Field(name = "create_time")
    private Date createTime;

    /** 数据更新时间 */
    @Field(name = "update_time")
    private Date updateTime;

}
