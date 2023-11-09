package may.yuntian.jianping.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * 轨迹信息DTO
 * @author
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChemDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**  */
    @Field(name = "c_m")
    private String cM;

    /**  */
    @Field(name = "c_twa")
    private String cTwa;

    /**  */
    @Field(name = "c_ste")
    private String cSte;

    /**  */
    @Field(name = "c_pe")
    private String cPe;

    /**  */
    @Field
    private String mac;

    /**  */
    @Field(name = "pc_twa")
    private String pcTwa;

    /**  */
    @Field(name = "pc_twa_f")
    private String pcTwaF;

    /**  */
    @Field(name = "pc_stel")
    private String pcStel;

    /**  */
    @Field
    private String rf;

    /**  */
    @Field
    private String pe;

    /**  */
    @Field(name = "max_v")
    private String maxV;

    /**  */
    @Field(name = "min_v")
    private String minV;

    /**  */
    @Field(name = "test_range")
    private String testRange;

    /**  */
    @Field
    private String conclusion;





}
