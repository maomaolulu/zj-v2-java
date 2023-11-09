package may.yuntian.jianping.vo;

import lombok.Data;
import may.yuntian.jianping.dto.EngineDto;
import may.yuntian.jianping.dto.PointMapDto;
import may.yuntian.jianping.dto.ProtectionDto;
import may.yuntian.jianping.dto.TouchHazardsDto;

import java.io.Serializable;
import java.util.List;

@Data
public class WorkTrackVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 岗位ID 流动岗关联定岗ID 定岗关联zj_workspace表中自己的ID */
    private String postId;

    /** 车间ID */
    private String workshopId;

    /** 车间名称 */
    private String workshop;

    /** 区域名称 */
    private String areaId;

    /** 区域名称 */
    private String area;

    /** 车间区域拼接字段 */
    private String workshopArea;

    /** 岗位 */
    private String post;


    /** 工作内容(劳动者作业情况调查中工作内容过程和工作方式作业地点) */
    private String  workSituation;

    /** 危害因素主要来源(报告4.8章节) */
    private String hazardSource;

    /** 游离二氧化硅共用(0未被共用,-1已被共用,有值共用岗位的ID) */
    private String silicaShare;

    /** 取消游里二氧化硅共用假字段 */
    private String cancelSo2="";

    /**
     * 轨迹物质列表 识别检测物质
     */
    private List<TouchHazardsDto> touchHazardsList;

    /**
     * 轨迹物质列表 识别不检测物质
     */
    private List<TouchHazardsDto> identifyHazardsList;

    /**
     * 点位信息List
     */
    private List<PointMapDto> pointMapList;

    /** 个人防护数组 */
    private List<ProtectionDto> ppe;

    /** 个人防护佩戴情况 */
    private String ppeWear;

    /** 工程防护数组 */
    private List<EngineDto> epe;

    /** 工程防护运行情况 */
    private String epeRun;


}
