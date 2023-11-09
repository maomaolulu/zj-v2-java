package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("zj_equipment_layout")
public class EquipmentLayoutEntity implements Serializable {
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
     * 序号
     */
    private String code;

    /**
     * 车间
     */
    private String workshop;

    /**
     * 岗位
     */
    private String post;

    /**
     * 设备名称
     */
    private String device;

    /**
     * 型号
     */
    private String model;

    /**
     * 总数量
     */
    private Integer totalNum;

    /**
     * 运行数量
     */
    private Integer runNum;

    /**
     * 在用设备数量
     */
    private Integer usedNum;

    /**
     * 设备状态
     */
    private String status;

    /**
     * 具体位置
     */
    private String position;

    /**
     * 备注
     */
    private String remarks;

    /**
     *数据入库时间
     */
    private Date createtime;
    /**
     *修改时间
     */
    private Date updatetime;

    /**
     * 岗位ID
     */
    private String postId;
}
