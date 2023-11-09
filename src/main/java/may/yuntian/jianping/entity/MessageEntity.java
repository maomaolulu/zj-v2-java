package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 检评-首页-消息通知
 * @author gy
 * @date 2023-08-03 19:19
 */
@Data
@TableName("sys_message")
public class MessageEntity implements Serializable {
    /**
     * 自增主键
     */
    private Long id;

    /**
     * 消息类型（0：检评）
     */
    private Integer businessType;

    /**
     * 发送人员类型（0：安联系统 1：系统用户）
     */
    private Integer senderType;

    /**
     * 发送人员ID
     */
    private Long senderId;

    /**
     * 发送人员名称
     */
    private String senderName;

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
    private Date sendTime;
}
