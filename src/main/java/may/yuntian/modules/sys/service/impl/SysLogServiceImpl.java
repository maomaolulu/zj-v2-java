package may.yuntian.modules.sys.service.impl;

//import com.baomidou.mybatisplus.mapper.QueryWrapper;
//import com.baomidou.mybatisplus.plugins.Page;
//import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;
import may.yuntian.common.utils.SpringContextUtils;
import may.yuntian.modules.sys.dao.SysLogDao;
import may.yuntian.modules.sys.entity.SysConlogEntity;
import may.yuntian.modules.sys.entity.SysLogEntity;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.SysConlogService;
import may.yuntian.modules.sys.service.SysLogService;
import may.yuntian.modules.sys.service.SysUserService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("sysLogService")
public class SysLogServiceImpl extends ServiceImpl<SysLogDao, SysLogEntity> implements SysLogService {

	
	@Autowired
	private SysConlogService sysConlogService; 
	
//    @Override
//    public PageUtils queryPage1(Map<String, Object> params) {
//        String key = (String)params.get("key");
//
//        IPage<SysLogEntity> page = this.selectPage(
//            new Query<SysLogEntity>(params)
//            .getPage(),
//            new QueryWrapper<SysLogEntity>().like(StringUtils.isNotBlank(key),"username", key)
//        );
//
//        return new PageUtils(page);
//    }
    
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
    	SysUserService sysUserService = SpringContextUtils.getBean("sysUserService",SysUserService.class);
        String username = (String)params.get("username");
        String emailStr = null;
        if(StringUtils.isNotBlank(username)) {
        	SysUserEntity user = sysUserService.queryByUserName(username);
        	if(user!=null) {
        		emailStr = user.getEmail();
        	}
        }
        params.put("email", emailStr);
        String email = (String)params.get("email");
        //创建时间
    	String startDate = (String)params.get("startDate");	
    	String endDate = (String)params.get("endDate");	
    	
        IPage<SysLogEntity> page = this.page(
            new Query<SysLogEntity>().getPage(params),
            new QueryWrapper<SysLogEntity>()
//            .like(StringUtils.isNotBlank(username),"username", username)
            .between(StringUtils.isNotBlank(endDate), "create_date", startDate, endDate)//创建时间
            .and(StringUtils.isNotBlank(username),i->i.eq("username", username).or().eq("username", email))
            .orderByDesc("id")
        );

        return new PageUtils(page);
    }
    
    
    @Override
    public PageUtils queryPageByOperation(Map<String, Object> params) {
        String username = (String)params.get("username");//用户登录邮箱
        //创建时间
    	String startDate = (String)params.get("startDate");	
    	String endDate = (String)params.get("endDate");		
    	SysUserService sysUserService = SpringContextUtils.getBean("sysUserService",SysUserService.class);
        IPage<SysLogEntity> page = this.page(
            new Query<SysLogEntity>().getPage(params),
            new QueryWrapper<SysLogEntity>()
            .like(StringUtils.isNotBlank(username),"username", username)
            .eq("operation", "登录成功")
            .between(StringUtils.isNotBlank(endDate), "create_date", startDate, endDate)//创建时间
            .orderByDesc("id")
        );
        page.getRecords().forEach(action->{
        	SysUserEntity user = sysUserService.queryByEmail(action.getUsername());
        	action.setName(user.getUsername());
        	action.setSubjection(user.getSubjection());
        	
        });

        return new PageUtils(page);
    }
    
    /**
     * 登录统计
     * @param username
     * @return
     */
    public Map<String, Object> getLoginStatistics(Map<String, Object> params) {
    	String username = (String)params.get("username");
    	SysUserService sysUserService = SpringContextUtils.getBean("sysUserService",SysUserService.class);
    	SysUserEntity user = sysUserService.queryByEmail(username);
		List<SysLogEntity> sysLogList = baseMapper.selectList(new QueryWrapper<SysLogEntity>()
				.eq("username", username)
				.eq("operation", "登录成功")
				.orderByDesc("create_date")
				);
		String pattern = "yyyy-MM-dd 00:00:00";
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	    //根据每月第一天获取大于这天的数据
		List<SysLogEntity> sysLogEntityList = baseMapper.selectList(new QueryWrapper<SysLogEntity>()
				.eq("username", username)
				.eq("operation", "登录成功")
				.ge("create_date", simpleDateFormat.format(dateTimeMonthStart()))
				);
		//根据每周第一天获取大于当天的数据
		List<SysLogEntity> sysLogEntityList2 = baseMapper.selectList(new QueryWrapper<SysLogEntity>()
				.eq("username", username)
				.eq("operation", "登录成功")
				.ge("create_date", simpleDateFormat.format(getWeekStartDate()))
				);
		int monthLoginTotal = sysLogEntityList.size();//当月登录次数
		int weekLoginTotal = sysLogEntityList2.size();//本周登录次数
		String str = "yyyy-MM-dd HH:mm";
	    SimpleDateFormat strFormat = new SimpleDateFormat(str);
		String currentLoginTime = strFormat.format(sysLogList.get(0).getCreateDate());//当前登录时间
		String lastLoginTime = null;//上次登录时间
		if(sysLogList.size()>=2) {
			lastLoginTime = strFormat.format(sysLogList.get(1).getCreateDate());//上次登录时间
		}
		String ip = sysLogList.get(0).getIp();//当前登录ip地址
		int AllLoginTotal = sysLogList.size();//全部
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("currentLoginTime", currentLoginTime);
		map.put("lastLoginTime", lastLoginTime);
		map.put("weekLoginTotal", weekLoginTotal);
		map.put("monthLoginTotal", monthLoginTotal);
		map.put("AllLoginTotal", AllLoginTotal);
		map.put("ip", ip);
		map.put("email", username);
		map.put("username", user.getUsername());
		
		SysConlogEntity sysConlogEntity = sysConlogService.getByUserId(user.getUserId());
		sysConlogEntity.setWeekLoginTotal(weekLoginTotal);
		sysConlogEntity.setMonthLoginTotal(monthLoginTotal);
		sysConlogEntity.setUpdatetime(new Date());
		sysConlogService.updateById(sysConlogEntity);
		
//		//将个人登录信息存入redis
//		Jedis jedis = new Jedis("localhost");
//		String objectToJson = JSON.toJSONString(map);//将map对象转为JSON对象
//		jedis.hset("log",username, objectToJson);//将用户名和登录信息以键值对的形式存入redis
		
		return map;
	}
    
    /**
     *  根据用户邮箱获取用户上一次的登录日期
     * @param username
     * @return
     */
    public String getLastDateByUsername(String username) {
    	List<SysLogEntity> sysLogList = baseMapper.selectList(new QueryWrapper<SysLogEntity>()
				.eq("username", username)
				.eq("operation", "登录成功")
				.orderByDesc("create_date")
				.last("limit 3")
				);
    	String str = "yyyy-MM-dd HH:mm:dd";
	    SimpleDateFormat strFormat = new SimpleDateFormat(str);
	    String lastLoginTime = "";
    	if(sysLogList.size()>=2) {
			lastLoginTime = strFormat.format(sysLogList.get(1).getCreateDate());//上次登录时间
		}
    	
    	return lastLoginTime;
	}
    
//    /**
//     * 获取redis中所有人员登录信息
//     * @return
//     */
//    public List<Map<String, Object>> getLogListByRedis() {
//    	Jedis jedis = new Jedis("localhost");
//        Map<String, String> allMaps = jedis.hgetAll("log");
//        List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
//        for(String value:allMaps.values()) {
//        	System.out.println(value);
//        	Map<String, Object> valueMap = JSON.parseObject(value);//将JSON对象转换成map对象
//        	mapList.add(valueMap);
//        }
//    	
//    	
//    	return mapList;
//	}
    
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
     * Calendar.MONDAY
     * @return
     */
    public static Date getWeekStartDate(){
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.WEEK_OF_MONTH, 0);
//        cale.set(Calendar.DAY_OF_WEEK, 1);
        cale.setFirstDayOfWeek(Calendar.MONDAY);
	    cale.set(Calendar.DAY_OF_WEEK, cale.getFirstDayOfWeek());
        Date date = cale.getTime();
        return date;
    }
    
    
}
