package may.yuntian.jianping.mongodto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhuYiCheng
 * @date 2023/9/6 15:34
 */
@Data
public class Scene implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 温度
     */
    private String temp = "";
    /**
     * 湿度
     */
    private String humidity = "";
    /**
     * 气压
     */
    private String pressure = "";
    /**
     * 风速
     */
    private String wind_speed = "";
}
