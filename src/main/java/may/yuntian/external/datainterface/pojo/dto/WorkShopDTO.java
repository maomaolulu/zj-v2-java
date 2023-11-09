package may.yuntian.external.datainterface.pojo.dto;

import lombok.Data;
import may.yuntian.jianping.vo.PostPfnVo;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;

/**
 * 车间信息
 *
 * @author mi
 */
@Data
public class WorkShopDTO implements Serializable {

    private static final long serialVersionUID = 42L;

    /**
     * 车间Id
     */
    @Field(name = "workshop_id")
    private String workshopId;

    /**
     * 车间名称
     */
    @Field(name = "workshop")
    private String workshop;

    /**
     * 岗位
     */
    @Field(name = "post")
    private String post;
}
