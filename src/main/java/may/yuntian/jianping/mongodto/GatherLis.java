package may.yuntian.jianping.mongodto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhuYiCheng
 * @date 2023/9/6 15:43
 */
@Data
public class GatherLis implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @NotNull
    private String sample_code;
    /**
     * 采样时段
     */
    private String time_frame = "";
    /**
     * 代表接触时长  噪声个体采样记录持续时间
     */
    private String contact_duration = "0";
    /**
     * 佩戴人姓名(个体采样)
     */
    private String people = "";
    /**
     * 采样时间(时长) / 测量时间(时长)  单位min
     */
    private Integer test_duration;
    /**
     * 采样前流量(L / min)
     */
    private String before_flow;
    /**
     * 采样后流量(L / min)
     */
    private String after_flow;
    /**
     * 采样开始时间
     */
    private String begin_time;
    /**
     * 采样结束时间
     */
    private String end_time;
    /**
     * 采样体积(L)
     */
    private String sample_volume;
    /**
     * 采样设备
     */
    private SampleEquip sample_equip;
    /**
     * 采样记录结果  这个字段不做映射  根据需要填写
     */
    private Result result;
    /**
     * 条形码
     */
    private String bar_code = "";
    /**
     * 打印次数
     */
    private Integer print_num = 0;
    /**
     * 条形码最近一次打印时间
     */
    private Date print_time;


}
