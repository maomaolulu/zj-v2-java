package may.yuntian.external.datainterface.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;

/**
 * 采样记录
 *
 * @author cwt
 * @Create 2023-4-18 10:35:02
 */
@Document("eval_plan_record")
@Data
public class EvalPlanRecordEntity implements Serializable {

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


}
