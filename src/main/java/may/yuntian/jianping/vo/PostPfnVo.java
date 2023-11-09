package may.yuntian.jianping.vo;

import lombok.Data;
import may.yuntian.jianping.dto.CodeSortDto;
import may.yuntian.jianping.dto.PointMapDto;
import may.yuntian.jianping.dto.TouchHazardsDto;
import may.yuntian.jianping.dto.WorkTrackDto;
import may.yuntian.jianping.mongoentity.WorkspaceEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class PostPfnVo implements Serializable {
    private static final long serialVersionUID = 1L;
/**             pfn表中信息                        */

    /**
     * _id 唯一识别ID
     */
    private String id;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 岗位/工种名称
     */
    private String pfn;


    /**
     * 车间ID
     */
    private String workshopId;

    /**
     * 区域ID
     */
    private String areaId;

    /**
     * 关联车间的_id 定岗绑定他本身
     */
    private List<String> postIds;

    /**
     * 岗位类型(1定岗 2流动岗 3流动岗交叉岗位)
     */
    private Integer isFixed;

    /**
     * 岗位总人数
     */
    private Integer peopleNum;

    /**
     * 岗位每班人数
     */
    private Integer shiftNum;

    /**
     * 是否外包 1是 2否
     */
    private Integer epiboly;

    /**
     * 工作方式
     */
    private String workPattern;

    /**
     * 工作班制
     */
    private String workSystem;

    /**
     * 日工作时长
     */
    private String workingTime;

    /**
     * 周工作天数
     */
    private String workingDays;

    /**
     * 工作时间
     */
    private String workingHour;

    /**
     * 工作日写实
     */
    private String realistic;

    /**
     * 体力劳动强度 I II III IV
     */
    private String laborIntensity;

    /**
     * 检测物质轨迹信息
     */
//    private Map<String,WorkTrackDto> workTrack;
    private List<WorkTrackVo> workTrackVos;

    /**
     * 个体检测数量
     */
    private String testNum;

    /**
     * 个体点位信息
     */
    private List<CodeSortDto> codeSort;

    /**
     * 个体检测物质ID数组
     */
    private List<Long> soloSubstanceIds;

    /**
     * 个体接触危害物质数组
     */
    private Map<String, TouchHazardsDto> soloHazards;


    /**
     * 数据入库时间
     */
    private Date createTime;

    /**
     * 数据更新时间
     */
    private Date updateTime;

    /**             zj_workspace表中信息                     */


    /**
     * 车间名称
     */
    private String workshop;


    /**
     * 区域名称
     */
    private String area;

    /**
     * 车间区域拼接字段
     */
    private String workshopArea;

    /**
     * 岗位
     */
    private String post;

    /**
     * 检测点位数量
     */
    private Integer pointNum;

    private List<WorkspaceEntity> workspaceEntityList;


}
