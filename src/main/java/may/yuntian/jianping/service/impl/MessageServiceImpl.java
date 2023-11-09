package may.yuntian.jianping.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.jianping.dto.MessageDto;
import may.yuntian.jianping.entity.MessageEntity;
import may.yuntian.jianping.entity.MessageReceiveEntity;
import may.yuntian.jianping.mapper.MessageMapper;
import may.yuntian.jianping.service.MessageReceiveService;
import may.yuntian.jianping.service.MessageService;
import may.yuntian.jianping.vo.DoingProjectVo;
import may.yuntian.jianping.vo.MessageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author gy
 * @date 2023-08-03 19:26
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, MessageEntity> implements MessageService {

    @Autowired
    private MessageReceiveService messageReceiveService;
    private final List<String> fitProjectType = Arrays.asList("ZJ", "ZW");

    @Override
    public List<MessageVo> listWithPage(MessageDto dto){
        Date deadLine = DateUtil.offsetDay(new Date(), -90);
        QueryWrapper<Object> wrapper = new QueryWrapper<>();
        wrapper.eq("sm.business_type", dto.getBusinessType());
        wrapper.eq("smr.receiver_id", dto.getReceiverId());
        wrapper.ge("sm.send_time", deadLine);
        wrapper.orderByDesc("sm.send_time");
        return baseMapper.listWithPage(wrapper);
    }

    @Override
    public Boolean updateStatus(Long id) {
        MessageReceiveEntity entity = new MessageReceiveEntity();
        entity.setId(id);
        entity.setReadingStatus(1);
        return messageReceiveService.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void newMessage(MessageEntity messageEntity, List<Long> receiveIds) {
        this.save(messageEntity);
        List<MessageReceiveEntity> saveList = new ArrayList<>();
        for (Long receiveId : receiveIds){
            MessageReceiveEntity one =  new MessageReceiveEntity();
            one.setMessageId(messageEntity.getId());
            one.setReceiverId(receiveId);
            one.setReadingStatus(0);
            saveList.add(one);
        }
        messageReceiveService.saveBatch(saveList);
    }

    @Override
    public List<DoingProjectVo> doingProjects(Long receiverId){
        QueryWrapper<Object> query = new QueryWrapper<>();
        query.and(wrapper -> wrapper.eq("apu.user_id", receiverId).or().eq("ap.charge_id", receiverId));
        query.lt("ap.`status`",40);
        query.in("tc.name_en", fitProjectType);
        return baseMapper.doingProjects(query);
    }

    @Override
    public Map<String, Object> projectStatus(Long receiverId) {
        Map<String, Object> returnMap = new HashMap<>(7);
        long year = DateUtil.year(new Date());
        Date sTime = DateUtil.parse(year +"-01-01");
        Date eTime = DateUtil.parse((year+1) +"-01-01");
        QueryWrapper<Object> query = new QueryWrapper<>();
        query.and(wrapper -> wrapper.eq("apu.user_id", receiverId).or().eq("ap.charge_id", receiverId));
        query.in("tc.name_en", fitProjectType);
        query.and(wrapper -> wrapper.isNull("apd.report_filing").or(wrapper1 -> wrapper1.ge("apd.report_filing",sTime).lt("apd.report_filing",eTime)));
        List<DoingProjectVo> list = baseMapper.doingProjects(query);
        // 完成项目(年度)
        returnMap.put("finishedProject",list.stream().filter(fp -> fp.getReportFiling() != null).count());
        // 现场调查
        returnMap.put("fieldInvestigation",list.stream().filter(fp -> fp.getStatus() == 4).count());
        // 采样
        returnMap.put("sampling",list.stream().filter(fp -> fp.getStatus() == 5).count());
        // 检测
        returnMap.put("detection",list.stream().filter(fp -> fp.getStatus() == 10).count());
        // 待签发
        returnMap.put("toBeIssued",list.stream().filter(fp -> fp.getStatus() >= 20 && fp.getStatus() <= 30).count());
        // 待归档
        returnMap.put("toBeArchived",list.stream().filter(fp -> fp.getStatus() >= 40 && fp.getStatus() <= 50 && fp.getReportFiling() == null).count());
        // 待公示
        returnMap.put("toBeAnnounced",list.stream().filter(fp -> fp.getPubStatus() == 6).count());
        return returnMap;
    }
}
