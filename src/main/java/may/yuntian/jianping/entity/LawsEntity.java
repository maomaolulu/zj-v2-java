package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName("eval_laws")
@Data
public class LawsEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 法律名称
     */
    private String lawName;

    /**
     * 法律文号
     */
    private String referenceNum;

    /**
     * 默认选中状态  默认为1
     */
    private Integer defaultSelected;

    /**
     * 当前法律效力的状态
     */
    private String lawStatus;

    /**
     * 颁布时间
     */
    private String lssuedDate;

    /**
     * 失效日期
     */
    private String expiryDate;

    /**
     * 修订关系
     */
    private String revisedRelationship;

    /**
     * 所属行业
     */
    private String industryInvolved;

    /**
     * 实施日期
     */
    private String materialDate;

    /**
     * 法律效力
     */
    private String forceOfLaw;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 已经选择的值(0:无,1选中)
     */
    @TableField(exist=false)
    private Integer selectValue;
}
