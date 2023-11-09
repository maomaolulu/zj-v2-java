package may.yuntian.jianping.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.io.Serializable;

/**
 * @author gy
 * @date 2023-09-04 16:52
 */
@Data
public class PostPfnDto implements Serializable {
    /**
     * 物质id
     */
    private int substance_id;

    private String name = "";

    private String alias = "";
    /**
     * 标识(选择物质时区分同物质的参数)
     */
    private String mark = "";
    /**
     * 是否高毒  1: 否    2: 是    默认为1 否
     */
    private int highly_toxic = 2;
    /**
     * 是否根据游离二氧化硅判定  1: 是    2: 否    默认为2 否
     */
    private int is_silica = 2;

    private int total_dust_id;

    private int indicator_id;
    /**
     * 检测类型  1: 检测    2: 验证性检测    默认为1 检测
     */
    private int test_type = 1;
    /**
     * 日接触时长 单位h
     */
    private String touch_time = "";
    /**
     * 周接触天数
     */
    private String touch_days = "";
    /**
     * 接触时段(当前接触时段不和采样时段关联，两个互不影响)
     */
    private String time_frame = "";
}
