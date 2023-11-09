package may.yuntian.jianping.mongodto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhuYiCheng
 * @date 2023/9/6 15:43
 */
@Data
public class EpeWorkingLis implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 设备防护名称
     */
    private String name="";
    /**
     * 运行情况
     */
    private String working;

}
