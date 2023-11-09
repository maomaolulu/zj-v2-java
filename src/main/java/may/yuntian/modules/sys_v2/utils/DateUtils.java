package may.yuntian.modules.sys_v2.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author ruoyi
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
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

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 获取当前月1号
     */
    public static Date getNowMonthFirstDay() {
        String time = dateTimeNow(YYYY_MM) + "-01";
        return parseDate(time);
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
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
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
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor) {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor) {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 获取两个日期之内的工作日（只去掉两个日期之间的周末时间，法定节假日未去掉）
     *
     * @param startDate-起始日期
     * @param endDate-结束日期
     * @return Long型时间差对象
     */
    public static int getWorkdayTimeInDate(Date startDate, Date endDate) {
        long start = startDate.getTime();
        long end = endDate.getTime();
        // 根据参数获取起始时间与结束时间的日历类型对象
        Calendar sdate = Calendar.getInstance();
        Calendar edate = Calendar.getInstance();
        sdate.setTimeInMillis(start);
        edate.setTimeInMillis(end);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            sdate.setTime(sdf.parse(sdf.format(sdate.getTime())));
            edate.setTime(sdf.parse(sdf.format(edate.getTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 如果两个时间在同一周并且都不是周末日期，则直接返回时间差，增加执行效率
        if (sdate.get(Calendar.YEAR) == edate.get(Calendar.YEAR)
                && sdate.get(Calendar.WEEK_OF_YEAR) == edate.get(Calendar.WEEK_OF_YEAR)
                && sdate.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                && sdate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY
                && edate.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                && edate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            long between_days = (end - start) / (1000 * 3600 * 24);
            return Integer.parseInt(String.valueOf(between_days));
        }

        // 防止开始日期在周五，结束日期在周日情况下，计算间隔天数不准确问题，开始或结束日期为周六日时，
        // 开始日期在周六日时，设置为下周一。
        if (sdate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            sdate.add(Calendar.DAY_OF_MONTH, 1);
        }
        if (sdate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            sdate.add(Calendar.DAY_OF_MONTH, 2);
        }
        start = sdate.getTimeInMillis();
        // 结束日期为周六日时，设置为周五
        if (edate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            edate.add(Calendar.DAY_OF_MONTH, -2);
        }
        if (edate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            edate.add(Calendar.DAY_OF_MONTH, -1);
        }
        end = edate.getTimeInMillis();

        // 首先取得起始日期与结束日期的下个周一的日期
        Calendar snextM = getNextMonday(sdate);
        Calendar enextM = getNextMonday(edate);
        // 获取这两个周一之间的实际天数
        int days = 0;
        try {
            days = daysBetween(snextM.getTime(), enextM.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 获取这两个周一之间的工作日数(两个周一之间的天数肯定能被7整除，并且工作日数量占其中的5/7)
        int workdays = days / 7 * 5;
        // 获取开始时间的偏移量
        long scharge = 0;
        sdate.setTimeInMillis(start);
        edate.setTimeInMillis(end);
        scharge = getCharge(sdate, scharge);
        // 获取结束时间的偏移量
        long echarge = 0;
        echarge = getCharge(edate, echarge);
        // 计算最终结果，具体为：workdays加上开始时间的时间偏移量，减去结束时间的时间偏移量
        long between_days = workdays + (scharge - echarge) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 计算日期偏移量
     *
     * @param sdate   指定日期
     * @param scharge 偏移量
     * @return
     */
    private static long getCharge(Calendar sdate, long scharge) {
        if (sdate.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                && sdate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            // 只有在开始、结束时间为非周末的时候才计算偏移量
            scharge += (7 - sdate.get(Calendar.DAY_OF_WEEK)) * 24 * 3600000;
            scharge -= sdate.get(Calendar.HOUR_OF_DAY) * 3600000;
            scharge -= sdate.get(Calendar.MINUTE) * 60000;
            scharge -= sdate.get(Calendar.SECOND) * 1000;
            scharge -= sdate.get(Calendar.MILLISECOND);
        }
        return scharge;
    }

    /**
     * 获取下周一
     *
     * @param calendar 指定日期
     * @return
     */
    private static Calendar getNextMonday(Calendar calendar) {
        int addnum = 9 - calendar.get(Calendar.DAY_OF_WEEK);
        if (addnum == 8) {
            // 周日的情况
            addnum = 1;
        }
        calendar.add(Calendar.DATE, addnum);
        return calendar;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);

        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();

        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }


}
