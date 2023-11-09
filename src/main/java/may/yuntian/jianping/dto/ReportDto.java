package may.yuntian.jianping.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gy
 * @date 2023-08-08 9:36
 */
@Data
public class ReportDto implements Serializable {
    /** 项目隶属 */
    private String companyOrder;
    /** 时间类型(当天/本周/本月/年度) */
    private String timeLevel;
    /** 查询开始时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;
    /** 查询结束时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
}
