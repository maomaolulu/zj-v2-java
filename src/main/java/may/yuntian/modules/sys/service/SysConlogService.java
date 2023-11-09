package may.yuntian.modules.sys.service;


import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.modules.sys.entity.SysConlogEntity;


/**
 * 用户登录信息统计
 * 
 * @author LiXin
 * @date 2021-04-30
 */
public interface SysConlogService extends IService<SysConlogEntity> {

	
	/**
	 * 通过用户id获取用户登录统计信息
	 * @param userId
	 * @return
	 */
	SysConlogEntity getByUserId(Long userId);
	
	/**
	 * 获取每周登录次数纪录列表 倒叙
	 */
	List<SysConlogEntity> getWeekListDesc();
	
	/**
	 * 获取每月登录次数纪录列表 倒叙
	 */
	List<SysConlogEntity> getMonthListDesc();
	
	/**
	 * 获取每周、每月登录信息次数人数
	 * @return
	 */
	Map<String, Object> getCountMap();
	
	/**
	 * 根据用户ID删除用户登录信息
	 * @param userid
	 */
	void deleteByUserId(Long userid);

}
