package may.yuntian.jianping.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

@Data
@Document("zj_workspace")
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** _id 唯一识别ID */
    private String id;

    /** 项目ID */
    private Long projectId;

    /** 车间ID */
    private String workshopId;

    /** 车间名称 */
    private String workshop;

    /** 区域ID */
    private String areaId;

    /** 区域名称 */
    private String area;

    /** 车间区域拼接字段 */
    private String workshopArea;

    /** 游离二氧化硅共用(0未被共用,-1已被共用,有值共用岗位的ID) */
    private String silicaShare;

    /** 岗位 */
    private String post;

    /** 关联定点工种的ID 可能没有 */
    private String fixedPfnId;

    /** 识别不检测物质合集(identifyHazardsList数组中的物质名称拼接) */
    private String onlyIdentify;

    /** 识别不检测物质数组 */
    private List<IdentifyHazardsDto> identifyHazards;

    /** 识别不检测物质合集(fixedHazardsList数组中的物质名称拼接) */
    private String testHazards;

    /** 定点岗位检测物质数组 */
    private List<IdentifyHazardsDto> fixedHazards;

    /** 检测点位数量 */
    private Integer pointNum;

    /** 关联的岗位/工种ID 如此工种未与任何流动岗关联则关联zj_post_pfn表中的自己 有的话也需关联自己*/
    private List<String> pfnIds;

    /** 点位信息List */
    private List<PointMapDto> pointMapList;

    /** 点位ID数组 */
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
    private Map<String,List<String>> pfnPoints;

    /** 工程防护数组 */
    private List<String> epe;


}
