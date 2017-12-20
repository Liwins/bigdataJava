package cn.riversky.logAnalyze.storm.utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/14.
 */
public class DateUtils {
    /**
     * 根据指定格式返回当前实现
     *
     * @param formatter
     * @return
     */
    public static String getDateTime(String formatter) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatter);
        return sdf.format(new Date());
    }

    /**
     * 根据日历时间获取当时的时间
     * @param calendar
     * @return
     */
    public static String getDateTime(Calendar calendar){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return  sdf.format(calendar.getTime());
    }
    public static String before15Minute(Calendar calendar){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        calendar.add(Calendar.MINUTE,-15);
        return sdf.format(calendar.getTime());
    }
    public static String before30Minute(Calendar calendar){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        calendar.add(Calendar.MINUTE,-30);
        return sdf.format(calendar.getTime());
    }
    public static String beforeOneHour(Calendar calendar) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        calendar.add(Calendar.HOUR, -1);
        return formatter.format(calendar.getTime());
    }

    public static String beforeOneDay(Calendar calendar) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return formatter.format(calendar.getTime());
    }

    /**
     * 获取当前时间 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getDateTime() {
        return DateUtils.getDateTime("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getDate() {
        return DateUtils.getDateTime("yyyy-MM-dd");
    }

    public static String getDate(String formatter) {
        return DateUtils.getDateTime(formatter);
    }

    /**
     * 根据String时间类型获取日期
     *
     * @param dateTime
     * @return
     */
    public static String removeTime(String dateTime) {
        return dateTime.substring(0, dateTime.indexOf(" "));
    }

    /**
     * 获取指定时间之前minute的时间  例如:minute = 30, 2014-07-15 12:00:00 ->  2014-07-15 11:30:00
     * 通过Calendar工具类进行构建
     *
     * @param time
     * @param minute
     * @return
     */
    public static String getBeforeMinute(String time, int minute) {
        String result = time;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date myDate = sdf.parse(time);
            Calendar c = Calendar.getInstance();
            c.setTime(myDate);
            c.add(Calendar.MINUTE, -minute);
            myDate = c.getTime();
            result = sdf.format(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将标准时间转化成yyyyMMdd
     *
     * @param date
     * @return
     */
    public static String splitDate(String date) {
        return date.substring(0, date.indexOf(" ")).replace("-", "");
    }

    /**
     * 获取昨天
     * @param formatter
     * @return
     * @throws ParseException
     */
    public static String getYesterday(String formatter) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar= Calendar.getInstance();
        calendar.setTime(sdf.parse(formatter));
        calendar.add(Calendar.DATE,-1);
        return sdf.format(calendar.getTime());
    }
    /**
     * 替换{}中的变量
     *
     * @param data
     * @param key
     * @param newData
     * @return
     */
    public static String replaceParentheses(String data, String key, String newData) {
        return data.replaceAll("\\{" + key + "\\}", newData);
    }

    public static String replaceParentheses(String data, String key) {
        return data.replaceAll("\\{" + key + "\\}", "");
    }

    /**
     * 格式化double,不使用科学计数法
     * @param doubleValue
     * @param fractionDigits
     * @return
     */
    public static String formatDouble(String doubleValue,int fractionDigits){
        NumberFormat nf=NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(fractionDigits);
        return nf.format(Double.parseDouble(doubleValue));
    }
    public static String formatDouble(double doubleValue, int fractionDigits) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(fractionDigits);
        return nf.format(doubleValue);
    }
    public static String formatDouble(String doubleValue) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(2);
        return nf.format(Double.parseDouble(doubleValue));
    }
    public static String formatDouble(double doubleValue) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(2);
        return nf.format(doubleValue);
    }

    /**
     * 将Object转成int,直接舍去
     * @param str
     * @return
     */
    public static String getInt(Object str){
        return Integer.toString(Integer.parseInt(str.toString().replaceAll("\\.\\d+","")));
    }
}
