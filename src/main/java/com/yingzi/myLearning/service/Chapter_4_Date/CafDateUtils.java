package com.yingzi.myLearning.service.Chapter_4_Date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @Author: hk
 * @Description:
 * @Date: 2019/5/31 15:42
 */
public class CafDateUtils extends org.apache.commons.lang3.time.DateUtils{
    /**
     * 比较两个时间是否是同年同日
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(Date date1, Date date2) {
        Calendar calendarOne = new GregorianCalendar();
        Calendar calendartWO = new GregorianCalendar();
        calendarOne.setTime(date1);
        calendartWO.setTime(date2);
         if(calendarOne.get(Calendar.YEAR)==calendartWO.get(Calendar.YEAR)&&
                 calendarOne.get(Calendar.MONTH)==calendartWO.get(Calendar.MONTH)){
             return true;
         }
         return  false;
    }

//    public static void main(String[] args) throws ParseException {

//          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//          Calendar calendar = Calendar.getInstance();
//          int year = calendar.get(Calendar.YEAR);
//          calendar.clear();
//          calendar.set(Calendar.YEAR, year - 1);
//          calendar.roll(Calendar.DAY_OF_YEAR, -1);
//          calender为去年的最后一天

//          String s = sdf.format(calendar.getTime());
//          上个月的最后一天
//          Calendar now = Calendar.getInstance();
//          now.set(Calendar.DAY_OF_MONTH, 1);
//          now.add(Calendar.DATE, -1);
//          String s = sdf.format(now.getTime());
//

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Calendar now = Calendar.getInstance();
//        Date d = new Date();
//        String dateNowStr = sdf.format(d);
//        System.out.println("new出来的格式化后的当前日期：" + dateNowStr);
//        d = now.getTime();
//        System.out.println("上个月: " + (now.get(Calendar.MONTH)));
//        System.out.println("日历格式化后的当前日期：" + sdf.format(d));
//
//        now.set(Calendar.MONTH, now.get(Calendar.MONTH) - 1);
//        System.out.println("month -1后的上个月: " + (now.get(Calendar.MONTH)));
//        d = now.getTime();
//        String dateLastStr = sdf.format(d);
//        System.out.println("日历格式化后上个月的日期：" + dateLastStr);
//
//    }

}
