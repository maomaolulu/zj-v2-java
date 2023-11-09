package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author
 */
@TableName("t_industry")
@Data
public class IndustryEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId
    private Long id;
    /**
     * 父级id
     */
    private Long pid;
    /**
     * 关系id
     */
    private Long mapId;
    /**
     * 字母
     */
    private String letter;
    /**
     * 编号
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 说明
     */
    private String remark;
    /**
     * 危害等级
     */
    private String hazardRating;
    /**
     * 字母 编号 名称拼接
     */
    private String joint;
    /**
     * 数据入库时间
     */
    private Date createtime;
    /**
     * 修改时间
     */
    private Date updatetime;


}
