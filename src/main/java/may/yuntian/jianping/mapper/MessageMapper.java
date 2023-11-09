package may.yuntian.jianping.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import may.yuntian.jianping.entity.MessageEntity;
import may.yuntian.jianping.vo.CompanySurveyVo;
import may.yuntian.jianping.vo.DoingProjectVo;
import may.yuntian.jianping.vo.MessageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * @author gy
 * @date 2023-08-03 19:24
 */
@Mapper
public interface MessageMapper extends BaseMapper<MessageEntity> {

    /**
     * 查询某个人接收的消息(分页)
     */
    @Select("SELECT smr.id, sm.title, sm.content, sm.send_time, smr.reading_status FROM `sys_message_receive` smr\n" +
            "LEFT JOIN sys_message sm ON sm.id = smr.message_id ${ew.customSqlSegment}")
    List<MessageVo> listWithPage(@Param(Constants.WRAPPER) QueryWrapper<Object> wrapper);

    /**
     * 查询个人运行中项目列表
     */
    @Select("SELECT ap.id project_id, ap.identifier, ap.company, ap.`status`,apd.task_release_date,apd.report_filing, ap.pub_status  FROM al_project ap \n" +
            "LEFT JOIN (SELECT project_id, user_id FROM `al_project_user` WHERE user_id = 589 GROUP BY project_id) apu ON ap.id = apu.project_id\n" +
            "LEFT JOIN al_project_date apd ON apd.project_id = ap.id\n" +
            "LEFT JOIN t_category tc ON tc.`name` = ap.type " +
            "${ew.customSqlSegment}")
    List<DoingProjectVo> doingProjects(@Param(Constants.WRAPPER) QueryWrapper<Object> wrapper);
}
