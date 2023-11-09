package may.yuntian.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.modules.sys.entity.SysTaskEntity;

import java.util.List;
import java.util.Map;

import org.quartz.SchedulerException;


/**
 * 任务计划、定时器
 * 
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2018-02-16
 */
public interface SysTaskService extends IService<SysTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 修改任务计划的运行状态
     * @param jobId
     * @param cmd
     * @throws SchedulerException
     */
	void changeStatus(Long jobId, String cmd) throws SchedulerException;

	void updateCron(Long jobId) throws SchedulerException;
	
	/**
	 * 初始化任务调度
	 * @throws SchedulerException
	 */
	void initSchedule() throws SchedulerException;

	int deleteBatchIds(List<Long> ids);

	int delete(Long id);

}
