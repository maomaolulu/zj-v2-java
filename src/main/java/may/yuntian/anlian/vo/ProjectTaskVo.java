package may.yuntian.anlian.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProjectTaskVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键ID
     */
    private Long id;
    /**
     * 项目编号
     */
    private String identifier;
    /**
     * 受检企业名称
     */
    private String company;
    /**
     * 项目状态(1项目录入，2任务分配，5采样，10收样，20检测报告，30报告装订，40质控签发，50报告寄送，60项目归档，70项目结束，98任务挂起，99项目中止)
     */
    private Integer status;
    /**
     * 负责人
     */
    private String charge;
    /**
     * 是否加急 加急状态(0正常，1较急、2加急)
     */
    private Integer urgent;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 采样人
     */
    private String samplePerson;
    /**
     * 要求报告完成日期
     */
    private Date claimEndDate;
    /**
     * 任务下发日期
     */
    private Date taskReleaseDate;

    /**
     * 项目隶属
     */
    private String companyOrder;

}
