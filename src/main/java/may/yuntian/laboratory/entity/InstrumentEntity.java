package may.yuntian.laboratory.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("eval_lab_detect_instrument")
public class InstrumentEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键ID
     */
    @TableId
    private Long id;

    private String model;

    private String name;

    private String code;

    @TableField(exist = false)
    private String link;


}
