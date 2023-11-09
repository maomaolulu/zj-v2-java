package may.yuntian.jianping.mongodto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZhuYiCheng
 * @date 2023/9/6 15:12
 */
@Data
public class BatchGatherLis implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 采样轮次
     */
    private Integer batch_num;
    /**
     * 采样日期  格式
     */
    private String gather_date;
    /**
     * 采样状态:  1: 进行中(包括未采)   2: 已完成
     */
    private Integer status = 1;
    /**
     * 是否有空白样  1: 是  2: 否
     */
    @NotNull
    private Integer has_kb_code = 2;
    /**
     * 计划样品编号列表
     */
    private List<String> sample_code_lis = new ArrayList<>();
    /**
     * 计划空白样编号列表
     */
    private List<String> sample_kb_code_lis = new ArrayList<>();
    /**
     *
     */
    private Scene scene;
    /**
     * 作业人数 (每天的作业人数不一定相等 第一天填写后 第二天第三天同第一天 第二天填写/修改时第一天和第三天不用跟着修改)
     */
    private Integer operators_num = 0;
    /**
     * 运行设备数量   修改前
     */
    private Integer working_equip = 0;
    /**
     * 工程防护运行情况   修改前
     */
    private String epe_working;
    /**
     * 个人防护运行情况   修改前
     */
    private String ppe_working;
    /**
     * 运行设备数量   修改后 在送样单提交后，现场调查的部分字段将不能修改，这些字段会在方案生成时代入方案和记录信息； 作业内容/设备/人员/工程防护个人防护 这些信息现场调查时填写配置情况同步到方案/记录信息里  采用记录填写运行状况
     */
    private List<Map<String, String>> equip_working_lis = new ArrayList<>();
    /**
     * 工程防护运行情况   修改后
     */
    private List<Map<String, String>> epe_working_lis = new ArrayList<>();
    /**
     * 个人防护运行情况   修改后
     */
    private List<Map<String, String>> ppe_working_lis = new ArrayList<>();
    /**
     * 正常样品采样记录
     */
    private Map<String, GatherLis> gather_map = new HashMap<>();
    /**
     * 空白样采样记录
     */
    private Map<String, GatherLis> gather_kb_map = new HashMap<>();
    /**
     * 已打印的条形码
     */
    private List<String> print_bar_code_lis = new ArrayList<>();


}
