package may.yuntian.jianping.mongodto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhuYiCheng
 * @date 2023/9/7 11:32
 */
@Data
public class WeatherDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long project_id;
    private Integer sample_delivery_status;
    private String temperature_max;
    private String gather_date;
    private String pressure;
    private String humidity_rh;
    private Integer total_record_num;

    private String temperature_min;
    private String weather;
    private String wind_speed;

    private Integer sample_record_num;
    private String createtime = "";
    private String updatetime = "";
}
