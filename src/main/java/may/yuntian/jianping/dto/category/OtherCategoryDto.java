package may.yuntian.jianping.dto.category;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;


/**
 *职业病危害种类-其他
 *
 * @author lixin
 * @data 2022-11-11
 */
@Data
public class OtherCategoryDto implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 名称:其他因素 */
	@Field
    private String name;

    /** 是否存在 */
    @Field(name = "is_have")
    private String isHave;

    /** 接触总人数 */
    @Field(name = "total_num")
    private Integer totalNum;
    

}