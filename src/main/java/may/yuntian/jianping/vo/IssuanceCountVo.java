package may.yuntian.jianping.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author gy
 * @date 2023-08-08 14:37
 */
@Data
public class IssuanceCountVo {
    /** 签发所在月 */
    private String month;
    /** 签发所在日 */
    private String day;
    /** 项目类型英文名 */
    private String nameEn;
}
