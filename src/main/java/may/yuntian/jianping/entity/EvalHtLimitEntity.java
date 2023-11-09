package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 高温接触限制
 * @author gy
 * @date 2023-07-31 13:17
 */
@Data
@TableName("eval_ht_limit")
public class EvalHtLimitEntity implements Serializable {
    /**
     * 自增主键ID
     */
    @TableId
    private Long id;

    /**
     * 日接触时间率
     */
    private Integer contactRate;
    /**
     * 体力劳动强度等级
     */
    private String laborIntensity;
    /**
     * 对应限制
     */
    private Integer limitValue;
    /**
     * 数据入库时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
}
