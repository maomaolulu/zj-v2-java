package may.yuntian.jianping.mongodto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhuYiCheng
 * @date 2023/9/6 16:57
 */
@Data
public class PfnMap implements Serializable {
    private static final long serialVersionUID = 1L;

    private String pfn_id;
    private String pfn;
    /**
     * // 默认为1 是
     */
    private int is_fixed = 1;
}
