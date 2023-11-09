package may.yuntian.external.datainterface.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 结果项
 *
 * @author cwt
 * @Create 2023-4-12 16:57:23
 */
@Data
public class ProResultItemVO implements Serializable {

    private static final long serialVersionUID = 42L;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 检测项目编码  pro_detection_substance.check_item_code
     */
    private String checkItemCode;

    /**
     * 检测项目名称  pro_detection_substance.item_name
     */
    private String itemName;

    /**
     * 物质名称  al_substance表
     */
    private String subName;

    /**
     * 物质id
     */
    private Long subId;

    /**
     * 车间
     */
    private String workArea;

    /**
     * 岗位
     */
    private String detectionArea;

    /**
     * 日接触时间  N..4,2：总长度最多为4位数字字符，小数点后保留2位数字
     */
    private String dailyContactTime;

    /**
     * 周工作天数  0~7
     */
    private String weekWorkDay;

    /**
     * 检测点位
     */
    private String pointName;

    /**
     * 检测日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date detectionDate;

    /**
     * 结果项编码  参考-数据字典2
     */
    private String code;

    /**
     * 结果
     */
    private String result;

    /**
     * 结果2
     */
    private String result2;

    /**
     * 计量单位编码  参考-数据字典3
     */
    private String unit;

    /**
     * mac限值或pc_twa限值
     */
    private String limit;

    /**
     * pc-stel限值或pe限值
     */
    private String limit2;

    /**
     * zj_result mongo的_id
     */
    private String resultId;
    /**
     * 单项结论  1：符合；0：不符合
     */
    private String conclusion;

    /**
     * 危害因素类型  1化学；2粉尘；3物理
     */
    private Integer factorType;

    /**
     * 特殊标识  化学细分三类：1MAC；2PC-TWA + PC-STEL；3PC-TWA + 峰接触浓度。	  粉尘：1总尘（C-TWA总尘 + C-PE总尘）；2呼尘（C-TWA呼尘 + C-PE呼尘）
     */
    private Integer specialIdentityFields;

    /**
     * 关联id eval_result <==> eval_plan_record_revise
     */
    private String correlation;

    /**
     * 字段标识 1:表示从eval_result查  2:表示从eval_plan_record_revise查
     */
    private Integer fieldTag;

    /**
     * 字段标识表示：从上游获取的结果数据行记录  默认值0
     */
    private Integer manual;

    /**
     * 噪声标识
     */
    private String mark;

}

