package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("zj_realistic_record")
@Data
public class RealisticRecordEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键ID
     */
    @TableId
    private Long id;
    /**
     * 写实记录ID
     */
    private Long realisticId;
    /**
     * 工作时间
     */
    private String workingTime;
    /**
     * 工作地点
     */
    private String place;
    /**
     * 工作内容
     */
    private String jobContent;
    /**
     * 耗费工时
     */
    private String workingHours;
    /**
     * 接触职业病危害因素
     */
    private String hazardFactors;
    /**
     * 备注
     */
    private String remakes;
    /**
     * 数据入库时间
     */
    private Date createtime;
    /**
     * 修改时间
     */
    private Date updatetime;
}
