package may.yuntian.jianping.dto.category;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;


/**
 *职业病危害种类-粉尘
 *
 * @author lixin
 * @data 2022-11-11
 */
@Data
public class DustCategoryDto implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 名称:粉尘 */
	@Field
    private String name;

	/** 是否存在 */
	@Field(name = "is_have")
    private String isHave;

    /** 接触总人数 */
    @Field(name = "total_num")
    private Integer totalNum;

    /** 接触矽尘人数 */
    @Field(name = "silica_num")
    private Integer silicaNum;

    /** 接触煤尘人数 */
    @Field(name = "coal_num")
    private Integer coalNum;

    /** 接触石棉粉尘人数 */
    @Field(name = "asbestos_num")
    private Integer asbestosNum;

}
