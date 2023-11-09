package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@TableName("zj_realistic_work")
@Data
public class RealisticWorkEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键ID
     */
    @TableId
    private Long id;
    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 作业情况id
     */
    private Long operationId;
    /**
     * 车间/工作场所
     */
    private String workshop;
    /**
     * 岗位
     */
    private String post;
    /**
     * 岗位总人数
     */
    private Integer totalNum;
    /**
     * 最大班人数
     */
    private Integer maxShiftNum;
    /**
     * 工作制度
     */
    private String shiftSystem;
    /**
     * 写实人数
     */
    private Integer realisticNum;
    /**
     * 姓名
     */
    private String name;
    /**
     * 工龄
     */
    private Integer workingYears;
    /**
     * 工作场所及工作内容描述
     */
    private String jobDescription;
    /**
     * 数据入库时间
     */
    private Date createtime;
    /**
     * 修改时间
     */
    private Date updatetime;


    /**
     * 多个工作写实
     */
    @TableField(exist=false)
    private List<RealisticRecordEntity> allRealisticRecord;

}
