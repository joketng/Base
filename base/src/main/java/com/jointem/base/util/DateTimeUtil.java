package com.jointem.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.jointem.base.util.StringUtils.isInEasternEightZones;
import static com.jointem.base.util.StringUtils.transformTime;

/**
 * Description:时间日期工具
 * Created by Kevin.Li on 2017/4/14.
 */
public final class DateTimeUtil {
    /**
     * 字符串日期比较，返回int类型{-1：小于；0：等于；1：大于}
     * <p/>
     *
     * @param format   日期格式
     * @param dateStr1 日期字符串1
     * @param dateStr2 日期字符串2
     *                 author: Kevin.Li
     *                 created at 2017/4/14 16:19
     */
    public static int dateCompare(String format, String dateStr1, String dateStr2) throws Exception {
        // 设定时间的模板
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        // 得到指定模范的时间
        Date d1 = sdf.parse(dateStr1);
        Date d2 = sdf.parse(dateStr2);
        // 比较
        long result = d1.getTime() - d2.getTime();
        if (result > 0) {
            return 1;
        } else if (result < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * 字符串日期与当前日期比较，返回int类型{-1：小于；0：等于；1：大于}
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 16:22
     */
    public static int dateCompare2CurrentDate(String format, String dateStr)
            throws Exception {
        String currentDate = SystemTool.getDataTime(format);
        return dateCompare(format, dateStr, currentDate);
    }

    /**
     * 将日期时间转为毫秒
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 16:23
     */
    public static long dateStrConvertMillisecond(String dateStr, String format) {
        // 设定时间的模板
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        // 得到指定模版的时间
        Date d;
        try {
            d = sdf.parse(dateStr);
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * 通过日期时间毫秒数得到特定格式的日期时间
     *
     * @param formatter 格式
     * @param dateTime  long型日期时间格式
     *                  <p/>
     *                  author: Kevin.Li
     *                  created at 2017/4/14 16:27
     */
    public static String getFormatedDateTime(long dateTime, String formatter) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(formatter);
        return sDateFormat.format(new Date(dateTime));
    }

    /**
     * 将string日期转换成long
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static long string2Long(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(time);
            return date.getTime();
        } catch (ParseException e){
            return 0L;
        }
    }

    /**
     * 友好地展示时间
     *
     * @param second 时间整形值
     * @return String 返回处理后的时间
     */
    public static String friendDisplayTime(int second) {
        StringBuffer sb = new StringBuffer();
        int mm = 60;
        int hh = mm * 60;
        int dd = hh * 24;
        int mou = dd * 30;
        int yy = mou * 12;

        long year = second / yy;
        long mouth = (second - year * yy) / mou;
        long day = (second - year * yy - mouth * mou) / dd;
        long hour = (second - year * yy - mouth * mou - day * dd) / hh;
        long min = (second - year * yy - mouth * mou - day * dd - hour * hh) / mm;

        if (year > 0) {
            sb.append(year);
            sb.append("年");
        }
        if (mouth > 0) {
            sb.append(mouth);
            sb.append("月");
        }
        if (day > 0) {
            sb.append(day);
            sb.append("天");
        }
        if (hour > 0) {
            if (StringUtils.isEmpty(sb.toString())) {
                sb.append(hour);
                if (min > 0) {
                    sb.append("时");
                } else {
                    sb.append("小时");
                }
            } else {
                sb.append(hour);
                sb.append("时");
            }
        }
        if (min > 0) {
            if (StringUtils.isEmpty(sb.toString())) {
                sb.append(min);
                sb.append("分钟");
            } else {
                sb.append(min);
                sb.append("分");
            }
        }
        if (min == 0) {
            if (StringUtils.isEmpty(sb.toString())) {
                sb.append("小于一分钟");
            }
        }
        return sb.toString();
    }

    /**
     * 以友好的方式显示时间
     *
     * @param sdate 时间字符
     * @return
     */
    public static String friendDisplayTime(String sdate) {
        Date time = null;

        if (isInEasternEightZones()) {
            time = toDate(sdate);
        } else {
            time = transformTime(toDate(sdate), TimeZone.getTimeZone("GMT+08"),
                    TimeZone.getDefault());
        }

        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        String curDate = dateFormat.get().format(cal.getTime());
        String paramDate = dateFormat.get().format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天 ";
        } else if (days > 2 && days < 31) {
            ftime = days + "天前";
        } else if (days >= 31 && days <= 2 * 31) {
            ftime = "一个月前";
        } else if (days > 2 * 31 && days <= 3 * 31) {
            ftime = "2个月前";
        } else if (days > 3 * 31 && days <= 4 * 31) {
            ftime = "3个月前";
        } else {
            ftime = dateFormat.get().format(time);
        }
        return ftime;
    }

    private final static ThreadLocal<SimpleDateFormat> dateTimeFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 将字符串转位日期类型
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 15:44
     */
    public static Date toDate(String sdate) {
        return toDate(sdate, dateTimeFormat.get());
    }

    /**
     * 按指定格式将字符串转位日期类型
     *
     * @param dateFormat 日期时间格式
     *                   <p/>
     *                   author: Kevin.Li
     *                   created at 2017/4/14 15:43
     */
    public static Date toDate(String sdate, SimpleDateFormat dateFormat) {
        try {
            return dateFormat.parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }
}
