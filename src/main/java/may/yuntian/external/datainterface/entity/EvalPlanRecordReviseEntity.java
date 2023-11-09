package may.yuntian.external.datainterface.entity;

import lombok.Data;
import may.yuntian.external.datainterface.pojo.dto.InBatchGatherDTO;
import may.yuntian.jianping.dto.ResultSubstanceDto;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.util.List;

/**
 * 采样记录-最终版
 *
 * @author cwt
 * @Create 2023-4-18 10:35:02
 */
@Document("eval_plan_record_revise")
@Data
public class EvalPlanRecordReviseEntity implements Serializable {

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
     * 物质信息
     */
    @Field
    private ResultSubstanceDto substance;

    /**
     * 样品批次详情
     */
    @Field(name = "batch_gather_lis")
    private List<InBatchGatherDTO> batchGatherLis;

    /**
     * 车间名称
     */
    @Field(name = "work_area")
    private String workArea;

    /**
     * 检测点位
     */
    @Field(name = "detect_point")
    private String pointName;

    /**
     * 流动采样还是个体采样
     */
    @Field(name = "is_fixed")
    private Integer isFixed;

    /**
     * 岗位
     */
    @Field(name = "profession")
    private String profession;

    /**
     * 关联id
     */
    @Field(name = "aggregate_id")
    private String aggregateId;
}
