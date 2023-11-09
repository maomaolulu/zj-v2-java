package may.yuntian.jianping.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import may.yuntian.jianping.entity.MainDataEntity;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 结果表样品批次DTO
 * @author
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchSampleDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 批次数量 */
    @Field(name = "batch_num")
    private Integer batchNum;

    /**
     * 采样日期
     */
    @Field(name = "gather_date")
    private String gatherDate;

    /**  */
    @Field
    private String result;

    /** 样品详情 */
    @Field(name = "gather_map")
    private Map<String,ResultGatherMapDto> gatherMap;

    /**  */
    @Field(name = "conclusion_key")
    private String conclusionKey;

    /**  */
    @Field
    private Map<String,ChemDto> conclusion;



}
