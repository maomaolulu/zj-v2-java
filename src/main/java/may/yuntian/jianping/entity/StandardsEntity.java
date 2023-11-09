package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;

@TableName("eval_standards")
@Data
public class StandardsEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键Id
     */
    @TableId
    private Long id;

    /**
     * 行业名称
     */
    private String industryName;

    /**
     * 标准名称
     */
    private String standardName;

    /**
     * 文号
     */
    private String referenNum;

    /**
     * 状态
     */
    private String status;

    /**
     * 颁布日期
     */
    private String lssuedDate;

    /**
     * 实施日期
     */
    private String materialDate;

    /**
     * 失效日期
     */
    private String expiryDate;

    /**
     * 修订关系
     */
    private String revisedRelationship;

    /**
     * 法律效率
     */
    private String forceOfLaw;

//    /**
//     * 所属行业
//     */
//    private String industryInvolved;

    /**
     * 是否勾选
     */
    private Integer isCheck;
    /**
     * 已经选择的值(0:无,1选中)
     */
    @TableField(exist=false)
    private Integer selectValue;
}
