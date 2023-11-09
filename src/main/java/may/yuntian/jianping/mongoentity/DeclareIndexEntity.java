package may.yuntian.jianping.mongoentity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import may.yuntian.jianping.dto.*;
import may.yuntian.jianping.entity.ProductOutputEntity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.data.mongodb.repository.Tailable;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author
 */
@Data
@Document("zj_declare_index")
@AllArgsConstructor
@NoArgsConstructor
public class DeclareIndexEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** _id 唯一识别ID */
    @MongoId(FieldType.OBJECT_ID)
    private String id;

    /** 项目ID */
    @Field(name = "project_id")
    private Long projectId;

    /** 基本信息 */
    @Field(name = "essential_information")
    private InformationMapDto information;

    /** 职业病危害因素种类 */
    @Field(name = "hazard_category")
    private HazardCategoryDto hazardCategory;

    /** 职业病危害因素检测情况 */
    @Field
    private DetectionDto detection;

    /** 危害因素检测 */
    @Field(name = "hazards_test")
    private HazardsTestDto hazardsTest;

    /** 主要产品及年产量 */
    private List<ProductOutputEntity> productOutputList;

    /** 数据入库时间 */
    @Field(name = "create_time")
    private Date createTime;

}
