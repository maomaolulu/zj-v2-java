package may.yuntian.anlian.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//import java.util.Calendar;

//import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

/**
 * 时间工具类
 *
 * @author MaYong
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";
    
    public static final String HH_MM = "HH:mm";

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    

    private static final String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }
    
    /**
     * 获取当前年, 默认格式为yyyy
     * @return String
     */
    public static String getNowYear() {
    	return dateTimeNow(YYYY);
    }
    
    /**
     * 获取下一年, 默认格式为yyyy
     * @return String
     */
    public static String getNextYear() {
    	Integer yyyy = Integer.valueOf(getNowYear())+1;
    	return String.valueOf(yyyy);
    }

    public static String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }
    
    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static String getDateYymmdd() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyMMdd");
    }
    
    /**
     * 获取当前年份的后两位 如2019年返回值为19
     */
    public static String getYY() {
    	String year = new SimpleDateFormat("yy", Locale.CHINESE).format(new Date()); 
    	return year;
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }
    
    /**
     * 时间型字符串转化为日期 格式
     */
    public static Date parseTime(Object str) {
    	if (str == null) {
    		return null;
    	}
    	try {
    		return parseDate(str.toString(), HH_MM);
    	} catch (ParseException e) {
    		return null;
    	}
    }
    
    /**
     * 遍历起止日期中间的所有日期
     * 传入的起止日期，返回一个泛型为String的集合
     * @param start
     * @param end
     * @return
     */
    public static List<Date> getDates(Date start, Date end) {
        List<Date> list = new ArrayList<Date>();
        //List<String> list = new ArrayList<String>();
        long s = start.getTime();
        long e = end.getTime();

        Long oneDay = 1000 * 60 * 60 * 24l;

        while (s <= e) {
            start = new Date(s);
            list.add(start);//返回一个泛型为Date的集合
            //list.add(new SimpleDateFormat("yyyy-MM-dd").format(start));//返回一个泛型为String的集合
            s += oneDay;
        }
        return list;
    }
    
    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }
    
    
    /**
     * 计算两个时间差多少天
     */
    public static Long getDays(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        return day;
    }
    
    /**
	 * 统计日期类型(年Y,月M,周W,日D)格式化转换
	 * @param dateType(年Y,月M,周W,日D)
	 * @param column 表结构列名
	 * @param asColumn 列名的重命名
	 * @return
	 */
	public static String formatYMWD(String dateType,String column,String asColumn) {
		dateType = dateType==null?"":dateType.toUpperCase();
		String format="DATE_FORMAT(%s,'%s') as %s";
		String retFormat=column+" as "+asColumn;;
		switch (dateType) {
		case "Y":
			retFormat = String.format(format,column,"%Y",asColumn);
			break;
		case "M":
			retFormat = String.format(format,column,"%Y.%m",asColumn);
			break;
		case "W":
			retFormat = String.format(format,column,"%XW%V",asColumn);
			break;
		case "D":
			retFormat = String.format(format,column,"%Y-%m-%d",asColumn);
			break;

		default:
			retFormat=column+" as "+asColumn;
			break;
		}
		
		return retFormat;
	}
	
	/**
	 * 获取指定年份的年、12个月、53个周的组合列表
	 * @param yyyy
	 * @return 2020,2020.01 - 2020.12,2020W01 - 2020W53
	 */
	public static List<String> getListYMW(String yyyy) {
		List<String>  ymws = new ArrayList<>();
		List<String> yms = get12YM(yyyy);
    	List<String> yws = get53YW(yyyy);
    	
    	ymws.add(yyyy);
    	ymws.addAll(yms);
    	ymws.addAll(yws);
    	
		return ymws;
	}
	
	/**
	 * 获取指定年份的12个月
	 * @param yyyy
	 * @return 2020.01 - 2020.12
	 */
	public static List<String> get12YM(String yyyy) {
		List<String>  yms = new ArrayList<>();
		
		for(int i=1;i<=12;i++) {
			String m = String.format("%2d", i).replace(" ", "0");
			yms.add(yyyy+"."+m);
		}
		return yms;
	}
	
	/**
	 * 获取指定年份的53个周
	 * @param yyyy
	 * @return 2020W01 - 2020W53
	 */
	public static List<String> get53YW(String yyyy) {
		List<String>  yws = new ArrayList<>();
		
		for(int i=1;i<=53;i++) {
			//String w = String.format("%2d", i).replace(" ", "0");
			String w = String.valueOf(i);
			yws.add(yyyy+"W"+w);
		}
		return yws;
	}
	
	/**
	 * 计算两个时间点相减的毫秒数
	 * @param dtTime1
	 * @param dtTime2
	 * @return
	 */
	public static long calculateMillSeconds(Date dtTime1, Date dtTime2)
	{
		return dtTime1.getTime() - dtTime2.getTime();
	}
	
	/**
	 * 计算两个时间点相减的秒数
	 * @param dtTime1
	 * @param dtTime2
	 * @return
	 */
	public static double calculateSeconds(Date dtTime1, Date dtTime2)
	{
		Long l_lMillSeconds = calculateMillSeconds(dtTime1, dtTime2);
		return l_lMillSeconds.doubleValue() / 1000;
	}

	/**
	 * 计算两个时间点相减的分钟数
	 * @param dtTime1
	 * @param dtTime2
	 * @return
	 */
	public static double calculateMinutes(Date dtTime1, Date dtTime2)
	{
		return calculateSeconds(dtTime1, dtTime2) / 60;
	}

	/**
	 * 计算两个时间点相减的小时数
	 * @param dtTime1
	 * @param dtTime2
	 * @return
	 */
	public static double calculateHours(Date dtTime1, Date dtTime2)
	{
		return calculateMinutes(dtTime1, dtTime2) / 60;
	}
	
	/**
	 * 计算两个时间点相减的小时数
	 * @param stringTime（09:15-09:30）
	 * @return 0.25
	 */
	public static double calculateHoursByString(String stringTime)
	{
		String[] stringTimes = stringTime.split("-");
		Date time1 = parseTime(stringTimes[0]);
		Date time2 = parseTime(stringTimes[1]);
		double diffHour = calculateHours(time2,time1);
		return diffHour;
	}
	/**
	 * 计算两个时间点相减的小时数,多参数以中文顿号隔开
	 * 但多个时段的计算时调用此方法
	 * @param stringTimeDaytonSign（09:00-09:18、10:00-10:18、14:00-14:24）
	 * @return [0.3, 0.3, 0.4]
	 */
	public static double[] calculateHoursByDaytonSign(String stringTimeDaytonSign)
	{
		String[] stringTimes = stringTimeDaytonSign.split("、");
		double[] doubles = new double[stringTimes.length];
		for (int i=0;i<stringTimes.length;i++) {
			doubles[i] = calculateHoursByString(stringTimes[i]);
		}
		return doubles;
	}
	
	/**
	 * 计算多参数以中文顿号隔开,两个时间点相减的小时数后的和
	 * 但多个时段的计算时间和时调用此方法
	 * @param stringTimeDaytonSign（09:00-09:18、10:00-10:18、14:00-14:24）
	 * @return 1.0
	 */
	public static double calculateHoursSumByDaytonSign(String stringTimeDaytonSign)
	{
		double retDouble = 0;
		double[] doubles = calculateHoursByDaytonSign(stringTimeDaytonSign);
		for (int i=0;i<doubles.length;i++) {
			retDouble += doubles[i];
		}
		return retDouble;
	}
	
	public static void main(String[] args) {
		//		时间型字符串转化为日期 格式
		Date time1 = parseTime("09:15");
		Date time2 = parseTime("09:30");
		System.out.println("时间型字符串转化为日期 格式: time1="+time1+" , time2="+time2);
		String time1_str = parseDateToStr(YYYY_MM_DD_HH_MM_SS, time1);
		String time2_str = parseDateToStr(YYYY_MM_DD_HH_MM_SS, time2);
		System.out.println("时间型字符串转化为日期 字符串格式: time1_str="+time1_str+" , time2_str="+time2_str);
		
		//计算两个时间点相减的小时数
		double ch = calculateHours(time2,time1);
		System.out.println("计算两个时间点相减的小时数:"+ch);
		
		String stringTime1="09:15-09:30";
		System.out.println("计算两个时间点("+stringTime1+")相减的小时数:"+calculateHoursByString(stringTime1));
		String stringTime2="10:00-10:45";
		System.out.println("计算两个时间点("+stringTime2+")相减的小时数:"+calculateHoursByString(stringTime2));
		
		String stringTimeDaytonSign = "09:00-09:18、10:00-10:18、14:00-14:24";
		double[] doubles1 = calculateHoursByDaytonSign(stringTimeDaytonSign);
		System.out.println("计算多个时间点("+stringTimeDaytonSign+")相减的小时数:"+Arrays.asList(doubles1));
		System.out.println("计算多个时间点("+stringTimeDaytonSign+")相减的小时数后的和:"+calculateHoursSumByDaytonSign(stringTimeDaytonSign));

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
     * 获取当月最后一天
     */
    public static Date dateTimeMonthEnd(){
    	Calendar ca = Calendar.getInstance();    
    	ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date date = ca.getTime();
        return date;
    }
	
	
    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR,year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }
     
    /**
     * 获取某年最后一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR,year);
        calendar.roll(Calendar.DAY_OF_YEAR,-1);
        Date currYearLast = calendar.getTime();
         
        return currYearLast;
    }


    /**
     * 指定日期加上天数后的日期
     * @param num 为增加的天数
     * @param newDate 创建时间
     * @return
     * @throws ParseException
     */
    public static String plusDay(int num,String newDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date currdate = format.parse(newDate);
//        System.out.println("现在的日期是：" + currdate);
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
        currdate = ca.getTime();
        String enddate = format.format(currdate);
//        System.out.println("增加天数以后的日期：" + enddate);
        return enddate;
    }
	
	
}
