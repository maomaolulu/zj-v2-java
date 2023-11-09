package may.yuntian.jianping.mongodto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhuYiCheng
 * @date 2023/9/6 15:43
 */
@Data
public class EquipLis implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 设备名称
     */
    private String name = "";
    /**
     * 调查时总数量 (数字转字符串  因为来源是mysql 默认是null)
     */
    private String total_num;
    /**
     * 调查时运行数量
     */
    private String run_num;
}
