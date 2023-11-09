package may.yuntian.jianping.mongoentity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import may.yuntian.jianping.dto.BatchGatherDto;
import may.yuntian.jianping.dto.PlaceDto;
import may.yuntian.jianping.dto.SubstanceDto;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 采样记录
 *
 * @author
 */
@Data
@Document("zj_plan_record")
@AllArgsConstructor
@NoArgsConstructor
public class PlanRecordEntity implements Serializable {
    private static final long serialVersionUID = 1L;

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
     * 检测方式(1定岗，2流动)
     */
    @Field(name = "is_fixed")
    private Integer isFixed;

    /**
     * 项目编号
     */
    @Field
    private String identifier;

    /**
     * 工种Ids
     */
    @Field(name = "relation_pfn_ids")
    private List<String> pfnIds;

    /**
     * 检测地点
     */
    @Field(name = "pfn_id")
    private String pfnId;

    /**
     * 检测地点信息
     */
    @Field
    private PlaceDto place;

    /**
     * 检测地点
     */
    @Field(name = "test_place")
    private String testPlace;

    /**
     * 点位ID
     */
    @Field(name = "point_id")
    private String pointId;

    /**
     * 点位编号
     */
    @Field(name = "point_code")
    private String pointCode;

    /**
     * 物质信息
     */
    @Field
    private SubstanceDto substance;

    /**
     * 样品号数组
     */
    @Field(name = "sample_code_lis")
    private List<String> sampleCodeLis;

    /**
     * 样品批次详情
     */
    @Field(name = "batch_gather_lis")
    private List<BatchGatherDto> batchGatherLis;

    /**
     * 数据更新时间
     */
    @Field(name = "update_time")
    private Date updateTime;


}
