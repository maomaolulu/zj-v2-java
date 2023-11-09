package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhuYiCheng
 * @date 2023/7/11 14:43
 */
@Data
@TableName("al_weather")
public class WeatherEntity implements Serializable {
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
     * 天气情况
     */
    private String weather;
    /**
     * 最低温度(℃)
     */
    private String temperatureMin;
    /**
     * 最高温度(℃)
     */
    private String temperatureMax;
    /**
     * 湿度%RH
     */
    private String humidityRh;
    /**
     * 风速m/s
     */
    private String windSpeed;
    /**
     * 气压kPa
     */
    private String pressure;
    /**
     * 采集日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date gatherDate;
    /**
     * 数据入库时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createtime;
    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date updatetime;
    /**
     *  状态 送样单进度：1：待收样(默认);2：检测中;3：待结果审核;  4：待编制;   5-项目归档
     */
//    @TableField(exist = false)
//    private Integer sample_delivery_status;
//    @TableField(exist = false)
//    private Integer sample_record_num;
//    @TableField(exist = false)
//    private Integer total_record_num;
}
