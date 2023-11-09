package may.yuntian.jianping.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.io.Serializable;

/**
 * @author gy
 * @date 2023-08-15 11:55
 */
@Data
public class MongoUpdateDto implements Serializable {
    /**
     * 更新条件
     */
    private Query flt;

    /**
     * 更新内容
     */
    private Update update;

    /**
     * 回退内容
     */
    private Update reduction;

}
