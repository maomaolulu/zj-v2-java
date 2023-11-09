package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息通知接收信息实体类
 * @author gy
 * @date 2023-08-04 10:10
 */
@Data
@TableName("sys_message_receive")
public class MessageReceiveEntity implements Serializable {
    /**
     * 自增主键
     */
    private Long id;

    /**
     * 消息ID
     */
    private Long messageId;

    /**
     * 接收人员ID
     */
    private Long receiverId;

    /**
     * 阅读状态（0：未读  1：已读）
     */
    private Integer readingStatus;

    /**
     * 消息已读时间
     */
    private Date readTime;

}
