package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName("zj_standards_project")
@Data
public class StandardsProjectEntity implements Serializable {
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
     *法律法规依据表ID
     */
    private Long standardId;
}
