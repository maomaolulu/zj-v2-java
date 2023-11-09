package may.yuntian.jianping.mongoentity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import may.yuntian.external.datainterface.pojo.dto.WorkShopDTO;
import may.yuntian.jianping.dto.*;
import may.yuntian.jianping.vo.WorkShopListVo;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 结果计算
 *
 * @author
 */
@Data
@Document("zj_result")
@AllArgsConstructor
@NoArgsConstructor
public class ResultEntity implements Serializable {
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
     * 工种表ID
     */
    @Field(name = "pfn_id")
    private String pfnId;

    /**
     * 流动采样还是个体采样
     */
    @Field(name = "is_fixed")
    private Integer isFixed;

    /**
     * zj_plan_record表ID
     */
    @Field(name = "record_id")
    private String recordId;

    /**
     * 点位ID
     */
    @Field(name = "point_id")
    private String pointId;

    /**
     * 检测点位
     */
    @Field(name = "point")
    private String point;

    /**
     * 检测地点
     */
    @Field(name = "test_place")
    private String testPlace;

    /**
     * 物质信息
     */
    @Field
    private ResultSubstanceDto substance;

    /**
     * 检测类型
     */
    @Field(name = "test_type")
    private Integer testType;

    /**
     * 是否存在
     */
    @Field(name = "is_exist")
    private Integer isExist;

    /**
     * 是否为多物质 1: 是   2: 否
     */
    @Field(name = "multi_substance")
    private Integer multiSubstance;

    /**
     * 多物质名称
     */
    @Field(name = "multi_sub_name")
    private String multiSubName;

    /**
     * 多物质样品标签
     */
    @Field(name = "multi_sub_tag")
    private String multiSubTag;

    /**
     * 多物质的类型  如五苯两酯、烷烃等
     */
    @Field(name = "multi_sub_type")
    private String multiSubType;

    /**
     * 总尘ID  如果是粉尘标识总尘ID
     */
    @Field(name = "total_dust_id")
    private Long totalDustId;

    /**
     * 是否超出
     */
    @Field(name = "is_exceed")
    private Integer isExceed;


    /**
     * 样品批次详情
     */
    @Field(name = "batch_sample_lis")
    private List<BatchSampleDto> batchSampleLis;

    /**
     * 物理结果
     */
    @Field
    private ConclusionDto conclusion;

    /**
     * 数据更新时间
     */
    @Field(name = "update_time")
    private Date updateTime;

    /**
     * 车间信息
     */
    @Field(name = "place")
    private WorkShopDTO workShopDTO;

    /**
     * 日接触时间
     */
    @Field(name = "touch_time")
    private String dailyContactTime;

    /**
     * 周工作天数
     */
    @Field(name = "touch_days")
    private String weekWorkDay;

    /**
     * 工种
     */
    @Field(name = "pfn")
    private String pfn;

    /**
     * 结果类型
     */
    @Field(name = "result_type")
    private Integer resultType;

    /**
     * 噪声限值
     */
    @Field(name = "limit_v")
    private String limitV;

}
