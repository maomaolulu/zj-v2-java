package may.yuntian.jianping.vo;

import lombok.Data;

@Data
public class MeasureLayoutVo {

    /** 项目ID */
    private Long projectId;

    /** 点位ID */
    private String pointId;

    /** 点位编号 */
    private Integer sort;

    /** 检测地点 车间/岗位/点位 */
    private String place;

    /** 检测物质 拼接字段 */
    private String substances;

}
