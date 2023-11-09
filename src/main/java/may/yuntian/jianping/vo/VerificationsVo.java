package may.yuntian.jianping.vo;


import lombok.Data;

@Data
public class VerificationsVo {
    /**
     * 岗位
     */
    private String place;
    /**
     *物质
     */
    private String testItem;
    /**
     *是否检测 0:未检出 1：已检出
     */
    private Integer minLimit;

}
