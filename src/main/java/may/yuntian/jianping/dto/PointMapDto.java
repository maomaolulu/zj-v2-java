package may.yuntian.jianping.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;

/**
 * 点位信息DTO
 */
@Data
public class PointMapDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 点位唯一识别ID  (new ObjectId().toString()) */
    @Field(name = "point_id")
    private String pointId;

    /** 点位名称  检评为数字  评价是名称 */
    @Field
    private String point;

    /** 关联的岗位/工种ID 如此工种未与任何流动岗关联则关联zj_post_pfn表中的自己 有的话也需关联自己 */
    @Field(name = "pfn_ids")
    private List<String> pfnIds;

    /** 布局布点图片ID */
    @Field(name = "img_id")
    private Integer imgId;

    /** 样品编号对应的数字 */
    @Field
    private Integer code;
    /** 布局布点中点位排序字段 */
    @Field
    private Integer sort;
    /** 编号得最大值 */
    @Field(name = "max_sample_code")
    private Integer maxSampleCode;

    public String getPointId() {
        return pointId;
    }

    public void setPointId(String pointId) {
        this.pointId = pointId;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public List<String> getPfnIds() {
        return pfnIds;
    }

    public void setPfnIds(List<String> pfnIds) {
        this.pfnIds = pfnIds;
    }

    public Integer getImgId() {
        return imgId;
    }

    public void setImgId(Integer imgId) {
        this.imgId = imgId;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getMaxSampleCode() {
        return maxSampleCode;
    }

    public void setMaxSampleCode(Integer maxSampleCode) {
        this.maxSampleCode = maxSampleCode;
    }
}
