package may.yuntian.external.datainterface.entity;

import lombok.Data;
import may.yuntian.external.datainterface.pojo.dto.InBatchGatherDTO;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.util.List;

/**
 * 评价结果项
 *
 * @author cwt
 * @Create 2023-4-18 10:35:02
 */
@Document("eval_result")
@Data
public class EvalResultEntity implements Serializable {

    private final long serialVersionUID = 42L;

    /**
     * _id 唯一识别ID
     */
    @MongoId(FieldType.OBJECT_ID)
    private String id;

    /**
     * 项目ID
     */
    @Field(name = "project_id")
    private Long projectId;

    /**
     * 日接触时间
     */
    @Field(name = "touch_time")
    private String touchTime;

    /**
     * 周工作天数
     */
    @Field(name = "touch_days")
    private String touchDays;

    /**
     * 样品批次详情
     */
    @Field(name = "batch_gather_lis")
    private List<InBatchGatherDTO> batchGatherLis;

    /**
     * total_dust_id(int)   ==0 总尘   大于0 呼尘
     */
    @Field(name = "total_dust_id")
    private Integer totalDustId;

    /**
     * limit_v  (高温：接触限值; )
     */
    @Field(name = "limit_v")
    private String limitV;

    /**
     * result_type
     */
    @Field(name = "result_type")
    private Integer resultType;

    /**
     * mac
     */
    @Field(name = "mac")
    private String mac;

    /**
     * pcTwa
     */
    @Field(name = "pc_twa")
    private String pcTwa;

    /**
     * pcStel
     */
    @Field(name = "pc_stel")
    private String pcStel;

    /**
     * pe
     */
    @Field(name = "pe")
    private String pe;

    /**
     * 物质类型(1.毒物(包括co/co2)  2.粉尘  3.噪声  4.高温  5.紫外辐射  7工频电场  6.手传震动 8.高频电磁场   9:超高频辐射  10:微波辐射  11:风速   12:照度 13:激光辐射 )
     */
    @Field(name = "s_type")
    private Integer sType;


    /**
     * 关联id
     */
    @Field(name = "record_id")
    private String recordId;

    /**
     * 体力劳动强度
     */
    @Field(name = "labor_intensity")
    private String laborIntensity;

    /**
     * 接触时间率
     */
    @Field(name = "contact_rate")
    private String contactRate;


}
