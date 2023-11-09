package may.yuntian.jianping.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author gy
 * @date 2023-08-07 17:05
 */
@Data
public class DoingProjectVo {
    /** 项目id */
    private Long projectId;
    /** 项目编号 */
    private String identifier;
    /** 收检公司 */
    private String company;
    /** 项目状态 */
    private Integer status;
    /** 下发时间 */
    private String taskReleaseDate;
    /** 报告归档日期 */
    private Date reportFiling;
    /** 项目公示状态 */
    private Integer pubStatus;
}
