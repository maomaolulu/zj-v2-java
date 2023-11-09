package may.yuntian.jianping.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author gy
 * @date 2023-08-08 10:29
 */
@Data
public class ProjectStatusVo {
    /** 项目状态 */
    private Integer status;
    /** 报告日期 */
    private Date reportFiling;
}
