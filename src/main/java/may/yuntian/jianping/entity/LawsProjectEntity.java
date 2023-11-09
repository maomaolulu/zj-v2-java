package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName("zj_law_project")
@Data
public class LawsProjectEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 法律法规ID
     */
    private Long lawId;
}
