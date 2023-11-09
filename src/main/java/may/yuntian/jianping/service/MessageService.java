package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.jianping.dto.MessageDto;
import may.yuntian.jianping.entity.MessageEntity;
import may.yuntian.jianping.vo.DoingProjectVo;
import may.yuntian.jianping.vo.MessageVo;
import may.yuntian.sys.utils.Result;

import java.util.List;
import java.util.Map;

/**
 * @author gy
 * @date 2023-08-03 19:25
 */
public interface MessageService extends IService<MessageEntity> {
    /**
     * 用户接收消息分页查询
     */
    List<MessageVo> listWithPage(MessageDto dto);

    /**
     * 修改接收消息状态(已读/未读)
     */
    Boolean updateStatus(Long id);

    /**
     * 新建一条的发送和多条消息接收
     */
    void newMessage(MessageEntity messageEntity, List<Long> receiveIds);

    /**
     * 查询进行中项目分页列表
     */
    List<DoingProjectVo> doingProjects(Long receiverId);

    /**
     * 查询参与的项目状态统计
     */
    Map<String, Object> projectStatus(Long receiverId);
}
