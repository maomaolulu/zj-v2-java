package may.yuntian.jianping.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 采样记录样品批次DTO
 * @author
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchGatherDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 批次数量 */
    @Field(name = "batch_num")
    private Integer batchNum;

    /** 采样日期 */
    @Field(name = "gather_date")
    private String gatherDate;

    /** 状态 */
    @Field
    private Integer status;

    /** 是否有空白样 */
    @Field(name = "has_kb_code")
    private Integer hasKbCode;

    /** 样品编号数组 */
    @Field(name = "sample_code_lis")
    private List<String> sampleCodeLis;

    /** 空白样编号数组 */
    @Field(name = "sample_kb_code_lis")
    private List<String> sampleKbCodeLis;

    /** 气象条件 */
    @Field
    private SceneDto scene;

    /** 操作数量 */
    @Field(name = "operators_num")
    private Integer operatorsNum;

    /** 运行的设备数量 */
    @Field(name = "working_equip")
    private Integer workingEquip;

    /** 工程防护 */
    @Field(name = "epe_working")
    private String epeWorking;

    /** 个人防护 */
    @Field(name = "ppe_working")
    private String ppeWorking;

    /** 样品详情 */
    @Field(name = "gather_map")
    private Map<String,GatherMapDto> gatherMap;

    /** 空白样详情 */
    @Field(name = "gather_kb_map")
    private Map<String,GatherMapDto> gatherKbMap;

    /** 打印编码数组 */
    @Field(name = "print_bar_code_lis")
    private List<String> printBarCodeLis;


}
