package may.yuntian.jianping.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gy
 * @date 2023-08-04 11:05
 */
@Data
public class MessageVo implements Serializable {
    /**
     * 接收信息ID
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sendTime;

    /**
     * 是否已读
     */
    private Integer readingStatus;
}
