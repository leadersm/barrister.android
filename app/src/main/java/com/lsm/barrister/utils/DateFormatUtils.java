package com.lsm.barrister.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Title: DateFormatUtils.java Description: 日期格式化工具类
 *
 * @author lsm
 * @date 2015-3-31
 */
@SuppressLint("SimpleDateFormat")
public class DateFormatUtils {

    private static final String TAG = DateFormatUtils.class.getSimpleName();

    private static final String format = "yyyy-MM-dd HH:mm:ss";
    private static final DateFormat formatter = new SimpleDateFormat(format);
//	private static final PrettyTime prettyFormatter = new PrettyTime(new Locale("zh"));

    public static Date parse(String str) {
        Date date = null;
        try {
            date = formatter.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date parse(String str, String format) {
        Date date = null;
        try {
            date = new SimpleDateFormat(format).parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String format(Date date) {
        return formatter.format(date);
    }

    public static String format(Date date, String format) {
        DateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static String formatLiveTime(Date date, String format) {
        DateFormat formatter = new SimpleDateFormat(format, Locale.US);
        return formatter.format(date);
    }


//	public static String formatPretty(Date date) {
//		return prettyFormatter.format(date);
//	}

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 以友好的方式显示时间
     *
     * @param sdate
     * @return
     */
    public static String formatPretty(Date time) {

        String ftime = "";
        Calendar cal = Calendar.getInstance();

        //判断是否是同一天
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
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
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            else
                ftime = hour + "小时前";
        }/*else if(days == 1){
            ftime = "昨天";
        }else if(days == 2){
            ftime = "前天";
        }else if(days > 2 && days <= 7){ 
            ftime = days+"天前";          
        }*/ else if (days >= 1) {//7
            ftime = dateFormater2.get().format(time);
        }
        return ftime;
    }
}
