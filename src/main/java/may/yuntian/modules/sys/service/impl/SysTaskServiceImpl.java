package may.yuntian.modules.sys.service.impl;


import may.yuntian.common.quartz.QuartzManager;
import may.yuntian.common.quartz.ScheduleJob;
import may.yuntian.common.utils.Constant;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;
import may.yuntian.modules.sys.dao.SysTaskDao;
import may.yuntian.modules.sys.entity.SysTaskEntity;
import may.yuntian.modules.sys.service.SysTaskService;
import may.yuntian.sys.utils.ScheduleJobUtils;

import org.apache.commons.lang.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务计划、定时器
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2018-02-16
 */
@Service("sysTaskService")
public class SysTaskServiceImpl extends ServiceImpl<SysTaskDao, SysTaskEntity> implements SysTaskService {
	
	@Autowired
	QuartzManager quartzManager;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String)params.get("key");

        IPage<SysTaskEntity> page = this.page(
            new Query<SysTaskEntity>().getPage(params),
            new QueryWrapper<SysTaskEntity>().like(StringUtils.isNotBlank(key),"jobName", key)
        );

        return new PageUtils(page);
    }
    

	@Override
	public int delete(Long id) {
		try {
			SysTaskEntity scheduleJob = getById(id);
			quartzManager.deleteJob(ScheduleJobUtils.entityToData(scheduleJob));
			return baseMapper.deleteById(id);
		} catch (SchedulerException e) {
			e.printStackTrace();
			return 0;
		}

	}

	@Override
	public int deleteBatchIds(List<Long> ids) {
		for (Long id : ids) {
			try {
				SysTaskEntity scheduleJob = getById(id);
				quartzManager.deleteJob(ScheduleJobUtils.entityToData(scheduleJob));
			} catch (SchedulerException e) {
				e.printStackTrace();
				return 0;
			}
		}
		
		return 0;//待修改
//		return baseMapper.removeByIds(ids);
	}

	@Override
	public void initSchedule() throws SchedulerException {
		// 这里获取任务信息数据
		List<SysTaskEntity> jobList = baseMapper.selectByMap(new HashMap<String, Object>(16));
		for (SysTaskEntity scheduleJob : jobList) {
			if ("1".equals(scheduleJob.getJobStatus())) {
				ScheduleJob job = ScheduleJobUtils.entityToData(scheduleJob);
				quartzManager.addJob(job);
			}

		}
	}

	@Override
	public void changeStatus(Long jobId, String cmd) throws SchedulerException {
		SysTaskEntity scheduleJob = getById(jobId);
		if (scheduleJob == null) {
			return;
		}
		if (Constant.ScheduleStatus.PAUSE.equals(cmd)) {
			quartzManager.deleteJob(ScheduleJobUtils.entityToData(scheduleJob));
			scheduleJob.setJobStatus(ScheduleJob.STATUS_NOT_RUNNING);
		} else {
			if (!Constant.ScheduleStatus.NORMAL.equals(cmd)) {
			} else {
                scheduleJob.setJobStatus(ScheduleJob.STATUS_RUNNING);
                quartzManager.addJob(ScheduleJobUtils.entityToData(scheduleJob));
            }
		}
		updateById(scheduleJob);
	}

	@Override
	public void updateCron(Long jobId) throws SchedulerException {
		SysTaskEntity scheduleJob = getById(jobId);
		if (scheduleJob == null) {
			return;
		}
		if (ScheduleJob.STATUS_RUNNING.equals(scheduleJob.getJobStatus())) {
			quartzManager.updateJobCron(ScheduleJobUtils.entityToData(scheduleJob));
		}
		updateById(scheduleJob);
	}
}
