package may.yuntian.jianping.dto;

import lombok.Data;
import may.yuntian.jianping.dto.category.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Map;


/**
 * 申报索引职业病危害种类
 *
 * @author lixin
 * @data 2022-11-11
 */
@Data
public class HazardCategoryDto implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 粉尘危害因素种类 */
	@Field("dust_category")
    private DustCategoryDto dustCategory;

    /** 化学危害因素种类 */
    @Field("chemistry_category")
    private ChemistryCategoryDto chemistryCategory;

    /** 物理危害因素种类 */
    @Field(name = "physics_category")
    private PhysicsCategoryDto physicsCategory;

    /** 放射性危害因素种类 */
    @Field(name = "radiation_category")
    private RadiationCategoryDto radiationCategory;

    /** 生物危害因素种类 */
    @Field(name = "biology_category")
    private BiologyCategoryDto biologyCategory;

    /** 其他危害因素种类 */
    @Field(name = "other_category")
    private OtherCategoryDto otherCategory;

}
