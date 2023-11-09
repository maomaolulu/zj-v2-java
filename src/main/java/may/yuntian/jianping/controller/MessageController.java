package may.yuntian.jianping.controller;

import cn.hutool.core.util.PageUtil;
import io.swagger.annotations.ApiOperation;
import may.yuntian.jianping.dto.MessageDto;
import may.yuntian.jianping.dto.MessageReceiveDto;
import may.yuntian.jianping.entity.MaterialEntity;
import may.yuntian.jianping.entity.MessageEntity;
import may.yuntian.jianping.service.MessageService;
import may.yuntian.jianping.vo.MessageVo;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.sys.utils.PageUtil2;
import may.yuntian.sys.utils.Result;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author gy
 * @date 2023-08-04 10:57
 */
@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    /**
     * 查询消息分页列表
     */
    @GetMapping("/listWithPage")
    @ApiOperation("查询消息分页列表")
    public Result listWithPage(MessageDto dto){
        PageUtil2.startPage();
        List<MessageVo> list = messageService.listWithPage(dto);
        return Result.resultData(list);
    }

    /**
     * 修改接收消息状态(已读/未读)
     */
    @PutMapping("/updateStatus")
    @ApiOperation("修改接收消息状态(已读/未读)")
    public Result listWithPage(@RequestBody MessageReceiveDto dto){
        return messageService.updateStatus(dto.getId()) ? Result.ok("修改消息接收状态成功") : Result.ok("修改消息接收状态失败");
    }



    /**
     * 发送消息测试
     */
    @GetMapping("/messageTest")
    @ApiOperation("发送消息测试")
    public void messageTest(){
        MessageEntity entity = new MessageEntity();
        entity.setTitle("新项目提醒");
        entity.setContent("您有新的项目（【test】），请及时处理");
        entity.setBusinessType(0);
        entity.setSenderType(0);
        SysUserEntity nowUser = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
        entity.setSenderId(nowUser.getUserId());
        entity.setSenderName(nowUser.getUsername());
        // 测试项目负责人id999
        messageService.newMessage(entity, Arrays.asList(999L,1000L));
    }

    /**
     * 获取进行中的项目列表
     */
    @GetMapping("/doingProjects")
    @ApiOperation("获取进行中的项目列表")
    public Result doingProjects(Long receiverId){
        PageUtil2.startPage();
        return Result.resultData(messageService.doingProjects(receiverId));
    }

    /**
     * 项目状态统计
     */
    @GetMapping("/projectStatus")
    @ApiOperation("项目状态统计")
    public Result projectStatus(Long receiverId){
        return Result.ok("查询成功",messageService.projectStatus(receiverId));
    }
}
