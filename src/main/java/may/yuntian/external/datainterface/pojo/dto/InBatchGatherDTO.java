package may.yuntian.external.datainterface.pojo.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * 批量收集
 *
 * @author cwt
 * @Create 2023-4-18 13:54:38
 */
@Data
public class InBatchGatherDTO implements Serializable {

    private static final long serialVersionUID = 42L;

    /**
     * 检测日期
     */
    @Field(name = "sample_date")
    private String sampleDate;

    /**
     * c_m
     */
    @Field(name = "c_m")
    private String cM;

    /**
     * c_twa
     */
    @Field(name = "c_twa")
    private String cTwa;

    /**
     * c_ste
     */
    @Field(name = "c_ste")
    private String cSte;

    /**
     * c_pe
     */
    @Field(name = "c_pe")
    private String cPe;

    /**
     * 单项结论
     */
    @Field(name = "conclusion")
    private String conclusion;

    /**
     * math_result
     */
    @Field(name = "math_result")
    private String mathResult;
}
