package may.yuntian.modules.sys.service.impl;

import may.yuntian.modules.sys.dao.SysConlogDao;
import may.yuntian.modules.sys.entity.SysConlogEntity;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.SysConlogService;
import may.yuntian.modules.sys.service.SysUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户登录信息统计
 * 
 * @author LiXin
 * @date 2021-04-30
 */

@Service("sysConlogService")
public class SysConlogServiceImpl extends ServiceImpl<SysConlogDao, SysConlogEntity> implements SysConlogService {

	@Autowired
	private SysUserService sysUserService;

	
	/**
	 * 通过用户id获取用户登录统计信息
	 * @param userId
	 * @return
	 */
	public SysConlogEntity getByUserId(Long userId) {
		SysConlogEntity sysConlogEntity = this.getOne(new QueryWrapper<SysConlogEntity>()
				.eq("userid", userId)
				);
		
		return sysConlogEntity;
	}
	
	/**
	 * 获取每周登录次数纪录列表 倒叙
	 */
	public List<SysConlogEntity> getWeekListDesc() {
		String pattern = "yyyy-MM-dd 00:00:00";
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		List<SysConlogEntity> sysConlogWeekList = this.list(new QueryWrapper<SysConlogEntity>()
				.ge("updatetime", simpleDateFormat.format(getWeekStartDate()))
				.orderByDesc("week_login_total")
				.last("limit 10")
				);
		if(sysConlogWeekList!=null&&sysConlogWeekList.size()>0) {
			sysConlogWeekList.forEach(action->{
				SysUserEntity userEntity = sysUserService.getById(action.getUserid());
				action.setUsername(userEntity.getUsername());
			});
		}
		return sysConlogWeekList;
	}
	
	/**
	 * 获取每月登录次数纪录列表 倒叙
	 */
	public List<SysConlogEntity> getMonthListDesc() {
		String pattern = "yyyy-MM-dd 00:00:00";
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		List<SysConlogEntity> sysConlogMonthList = this.list(new QueryWrapper<SysConlogEntity>()
				.ge("updatetime", simpleDateFormat.format(dateTimeMonthStart()))
				.orderByDesc("month_login_total")
				.last("limit 10")
				);
		
		if(sysConlogMonthList!=null&&sysConlogMonthList.size()>0) {
			sysConlogMonthList.forEach(action->{
				SysUserEntity userEntity = sysUserService.getById(action.getUserid());
				action.setUsername(userEntity.getUsername());
			});
		}
		return sysConlogMonthList;
	}
	
	/**
	 * 获取每周、每月登录信息次数人数
	 * @return
	 */
	public Map<String, Object> getCountMap() {
		String pattern = "yyyy-MM-dd 00:00:00";
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		List<SysConlogEntity> sysConlogWeekList = this.list(new QueryWrapper<SysConlogEntity>()
				.ge("updatetime", simpleDateFormat.format(getWeekStartDate()))
				.orderByDesc("week_login_total")
				);
		List<SysConlogEntity> sysConlogMonthList = this.list(new QueryWrapper<SysConlogEntity>()
				.ge("updatetime", simpleDateFormat.format(dateTimeMonthStart()))
				.orderByDesc("month_login_total")
				);
		Integer weekTotalNum = 0;//每周登录总次数
		Integer monthTotalNum = 0;//每月登录总次数
		Integer weekTotal = 0;//每周登录人数
		Integer monthTotal = 0;//每月登录人数
		if(sysConlogWeekList!=null&&sysConlogWeekList.size()>0) {
			weekTotalNum = sysConlogWeekList.stream().collect(Collectors.summingInt(SysConlogEntity::getWeekLoginTotal));
			weekTotal = sysConlogWeekList.size();
		}
		if(sysConlogMonthList!=null&&sysConlogMonthList.size()>0) {
			monthTotalNum = sysConlogMonthList.stream().collect(Collectors.summingInt(SysConlogEntity::getMonthLoginTotal));
			monthTotal = sysConlogMonthList.size();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("weekTotal", weekTotal);//每周登录人数
		map.put("weekTotalNum", weekTotalNum);//每周登录总次数
		map.put("monthTotal", monthTotal);//每月登录人数
		map.put("monthTotalNum", monthTotalNum);//每月登录总次数
		
		return map;
	}
	
	/**
	 * 根据用户ID删除用户登录信息
	 * @param userid
	 */
	public void deleteByUserId(Long userid) {
		List<SysConlogEntity> sysConlogEntityList = this.list(new QueryWrapper<SysConlogEntity>().eq("userid", userid));
		List<Long> userIdList = sysConlogEntityList.stream().map(SysConlogEntity::getId).collect(Collectors.toList());
		baseMapper.deleteBatchIds(userIdList);
	}
    
    /**
     * 获取当前月份第一天
     */
    public static Date dateTimeMonthStart(){
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        Date date = cale.getTime();
        return date;
    }

    /**
     * 获取一周第一天（从星期天开始）
     * @return
     */
    public static Date getWeekStartDate(){
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.WEEK_OF_MONTH, 0);
        cale.setFirstDayOfWeek(Calendar.MONDAY);
	    cale.set(Calendar.DAY_OF_WEEK, cale.getFirstDayOfWeek());
//        cale.set(Calendar.DAY_OF_WEEK, 2);
        Date date = cale.getTime();
        return date;
    }
    
    
}
