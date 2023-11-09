package may.yuntian.jianping.dto.category;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;


/**
 *职业病危害种类-化学
 *
 * @author lixin
 * @data 2022-11-11
 */
@Data
public class ChemistryCategoryDto implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 名称:化学物质 */
	@Field
    private String name;

    /** 是否存在 */
    @Field(name = "is_have")
    private String isHave;

    /** 接触总人数 */
    @Field(name = "total_num")
    private Integer totalNum;

    /** 接触铅人数 */
    @Field(name = "lead_num")
    private Integer leadNum;

    /** 接触苯人数 */
    @Field(name = "benzene_num")
    private Integer benzeneNum;

}
