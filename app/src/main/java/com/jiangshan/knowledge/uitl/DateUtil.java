/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.jiangshan.knowledge.uitl;

import android.annotation.SuppressLint;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
    public static boolean isDateConflicting(Calendar start1, Calendar end1,
                                            Calendar start2, Calendar end2, String week) {
        start1.set(14, 0);
        end1.set(14, 0);
        start2.set(14, 0);
        end2.set(14, 0);
        boolean flag = false;
        if ((start1.getTime().compareTo(start2.getTime()) > 0)
                && (start1.getTime().compareTo(end2.getTime()) <= 0)
                && (end1.getTime().compareTo(end2.getTime()) >= 0)) {
            flag = compareCal(start1, end2, week);
        } else if ((end1.getTime().compareTo(start2.getTime()) >= 0)
                && (end1.getTime().compareTo(end2.getTime()) < 0)
                && (start1.getTime().compareTo(start2.getTime()) <= 0)) {
            flag = compareCal(start2, end1, week);
        } else if ((start1.getTime().compareTo(start2.getTime()) > 0)
                && (start1.getTime().compareTo(end2.getTime()) < 0)
                && (end1.getTime().compareTo(start2.getTime()) > 0)
                && (end1.getTime().compareTo(end2.getTime()) < 0)) {
            flag = compareCal(start1, end1, week);
        } else if ((start1.getTime().compareTo(start2.getTime()) <= 0)
                && (end1.getTime().compareTo(end2.getTime()) >= 0)) {
            flag = compareCal(start2, end2, week);
        }

        return flag;
    }

    private static boolean compareCal(Calendar cal1, Calendar cal2, String week) {
        if (cal1.getTime().compareTo(cal2.getTime()) == 0) {
            int day = cal1.get(7);
            if (day == 1)
                day = 7;
            else {
                --day;
            }
            if (week.indexOf(String.valueOf(day)) != -1)
                return true;
        } else {
            Calendar temp = (Calendar) cal1.clone();
            temp.add(5, 6);
            if (temp.getTime().compareTo(cal2.getTime()) <= 0) {
                return true;
            }
            int day1 = cal1.get(7);
            int day2 = cal2.get(7);

            if (day1 == 1)
                day1 = 7;
            else {
                --day1;
            }
            if (day2 == 1)
                day2 = 7;
            else {
                --day2;
            }
            if (day1 < day2) {
                for (int i = day1; i <= day2; ++i)
                    if (week.indexOf(String.valueOf(i)) != -1)
                        return true;
            } else {
                for (int i = day1; i <= 7; ++i) {
                    if (week.indexOf(String.valueOf(i)) != -1) {
                        return true;
                    }
                }
                for (int i = 1; i <= day2; ++i) {
                    if (week.indexOf(String.valueOf(i)) != -1) {
                        return true;
                    }
                }
            }

        }

        return false;
    }

    public static Date getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(5, 1);
        return calendar.getTime();
    }

    public static Timestamp getCurOpTime() {
        return new Timestamp(System.currentTimeMillis());
    }

    
    public static String formatDate(Date date, String pattern, Locale locale) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
        return sdf.format(date);
    }

    public static Date parseDate(String date, String pattern, Locale locale) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Wrong Date Format: " + date);
        }
    }

    public static boolean isDate(String date, String pattern, Locale locale) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
        try {
            String temp = sdf.format(sdf.parse(date));
            return temp.equalsIgnoreCase(date);
        } catch (ParseException e) {
        }
        return false;
    }

    public static Date addDays(Date date, int days) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.add(5, days);
        return cd.getTime();
    }

    public static String addDays(String date_ddMMMyy, int days) {
        try {
            Date date = StringUS2Date(date_ddMMMyy);
            Date newdate = addDays(date, days);
            String string = Date2StringUS(newdate);
            return string;
        } catch (ParseException e) {
        }
        return null;
    }

    public static long subDays(String datestr1, String datestr2)
            throws ParseException {
        Date date1 = StringUS2Date(datestr1);
        Date date2 = StringUS2Date(datestr2);
        long milliseconds = date1.getTime() - date2.getTime();
        long days = milliseconds / 86400000L;
        return days;
    }

    public static long subDays(Date date1, Date date2) {
        long milliseconds = date1.getTime() - date2.getTime();
        long days = milliseconds / 86400000L;
        return days;
    }

    public static String getLastDayofWeek(String date_ddMMMyy,
                                          int firstdayofweek) throws ParseException {
        Date date = StringUS2Date(date_ddMMMyy);
        int weekday = dayofWeek(date);
        int offset = weekday - firstdayofweek;
        if (offset < 0) {
            offset += 7;
        }
        Date lastdate = addDays(date, -1 * offset + 6);
        String datestr = Date2StringUS(lastdate);
        return datestr;
    }

    public static String getFirstDayofWeek(String date_ddMMMyy,
                                           int firstdayofweek) throws ParseException {
        Date date = StringUS2Date(date_ddMMMyy);
        int weekday = dayofWeek(date);
        int offset = weekday - firstdayofweek;
        if (offset < 0) {
            offset += 7;
        }
        Date lastdate = addDays(date, -1 * offset);
        String datestr = Date2StringUS(lastdate);
        return datestr;
    }

    public static int dayofWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int d = c.get(7) - 1;
        d = (d == 0) ? 7 : d;
        return d;
    }

    public static String Date2StringUS(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMMyy", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String s = sdf.format(date);
        return s.toUpperCase();
    }

    public static String Date2String(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String s = sdf.format(date);
        return s.toUpperCase();
    }

    public static Date StringUS2Date(String ddMMMyy) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMMyy", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        Date date = sdf.parse(ddMMMyy);
        return date;
    }

    public static Date String2Date(String yyyyMMdd) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        Date date = sdf.parse(yyyyMMdd);
        return date;
    }

    public static Date now() throws ParseException {
        Calendar now = Calendar.getInstance(TimeZone
                .getTimeZone("Asia/Shanghai"));

        Date d = now.getTime();
        return d;
    }

    public static String getFrequence(String day) {
        if (day.length() == 7)
            return "D";
        if (day.length() < 5)
            return day;
        StringBuffer result = new StringBuffer("X");
        for (int i = 1; i < 8; ++i) {
            if (day.indexOf(String.valueOf(i)) < 0)
                result.append(i);
        }
        return result.toString();
    }

    public static String getFrequence(String day, int add) {
        if (day.length() == 7)
            return "D";
        StringBuffer sbtmp = new StringBuffer();
        if (day.length() < 5) {
            if (add == 0)
                return day;
            int temp = Integer.parseInt(day);
            while (temp > 0) {
                int m = (temp % 10 + add) % 7;
                temp /= 10;
                if (m == 0)
                    sbtmp.append(7);
                else {
                    sbtmp.insert(0, m);
                }
            }
            return sbtmp.toString();
        }
        if (add != 0) {
            int temp = Integer.parseInt(day);
            while (temp > 0) {
                int m = (temp % 10 + add) % 7;
                if (m == 0)
                    m = 7;
                temp /= 10;
                sbtmp.append(m);
            }
        } else {
            sbtmp.append(day);
        }
        StringBuffer result = new StringBuffer("X");
        for (int i = 1; i < 8; ++i) {
            if (sbtmp.indexOf(String.valueOf(i)) < 0)
                result.append(i);
        }
        return result.toString();
    }

    public static boolean isDateStringValid(String date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        if ((date != null) && (!(date.equals("")))) {
            try {
                sdf.format(sdf.parse(date));
                return true;
            } catch (ParseException e) {
                return false;
            }
        }
        return false;
    }

    public static Date getRightDate(Date bDate, Date eDate, String frequece) {
        if ((frequece == null) || (frequece.length() < 1)) {
            return null;
        }

        Date now = new Date();
        try {
            now = String2Date(Date2String(now()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date standDate = (subDays(bDate, now) > 3L) ? bDate : addDays(now, -3);
        int dayInWeek = dayofWeek(standDate);

        char[] arrFreq = frequece.toCharArray();
        int length = arrFreq.length;
        int[] iFreq = new int[length];
        for (int i = 0; i < length; ++i) {
            iFreq[i] = ((Integer.parseInt(String.valueOf(arrFreq[i])) + 7 - dayInWeek) % 7);
        }
        int chday = min(iFreq);
        Date rDate = addDays(standDate, chday);
        if ((eDate != null) && (eDate.before(rDate))) {
            return null;
        }
        return rDate;
    }

    public static int min(int[] data) {
        int length = data.length;
        if (length > 1) {
            int tmin = data[0];
            for (int i = 1; i < length; ++i) {
                tmin = (tmin < data[i]) ? tmin : data[i];
            }
            return tmin;
        }
        if (length < 1) {
            return -1;
        }
        return data[0];
    }

    public static String[] getDateRange(String USAS_String) throws Exception {
        Date currentDate = new Date();
        if ((null != USAS_String) && (!(USAS_String.trim().equals("")))) {
            currentDate = String2Date(USAS_String);
        }
        return getDateRange(currentDate);
    }

    public static String[] getDateRange(Date USAS_Date) {
        Date currentDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        String currentDateStr = formatDate(cal.getTime(), "yyyy-MM-dd",
                Locale.ENGLISH);
        String[] result = {currentDateStr, currentDateStr};
        if (null != USAS_Date) {
            currentDate = USAS_Date;
        }

        cal.setTime(currentDate);
        cal.add(3, -1);
        cal.set(7, 2);
        result[0] = formatDate(cal.getTime(), "yyyy-MM-dd", Locale.ENGLISH);

        cal.setTime(currentDate);
        cal.add(5, 591);
        result[1] = formatDate(cal.getTime(), "yyyy-MM-dd", Locale.ENGLISH);
        return result;
    }

    public static Date SectionString2Date(String ddMMM) {
        if ((ddMMM != null) && (ddMMM.length() == 5)) {
            ddMMM = ddMMM + "72";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMMyy", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        try {
            Date date = sdf.parse(ddMMM);
            return date;
        } catch (Exception e) {
        }
        return null;
    }

    public static String Date2SectionString(Date d) {
        if (null == d) {
            return "---";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String s = sdf.format(d);
        return s.toUpperCase();
    }

    public static String Time2String(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String s = sdf.format(date);
        return s.toUpperCase();
    }

    public static boolean isDateMatches(String str) {
        return (str.matches("\\d{4}/\\d+/\\d+;-;-"));
    }

    public static boolean isDateEffective(String date) {
        try {
            if ((null == date) || ("".equals(date)))
                return false;
            if (date.contains("-")) {
                String[] str = date.split("-");
                date = str[0]
                        .concat("-")
                        .concat((str[1].length() == 2) ? str[1] : "0"
                                .concat(str[1]))
                        .concat("-")
                        .concat((str[2].length() == 2) ? str[2] : "0"
                                .concat(str[2]));
            } else if (date.contains("/")) {
                String[] str = date.split("/");
                date = str[0]
                        .concat("-")
                        .concat((str[1].length() == 2) ? str[1] : "0"
                                .concat(str[1]))
                        .concat("-")
                        .concat((str[2].length() == 2) ? str[2] : "0"
                                .concat(str[2]));
            } else {
                return false;
            }
            int year = Integer.parseInt(date.substring(0, 4));
            int month = Integer.parseInt(date.substring(5, 7)) - 1;
            int day = Integer.parseInt(date.substring(8));
            Calendar calendar = GregorianCalendar.getInstance();

            calendar.setLenient(false);
            calendar.set(1, year);
            calendar.set(2, month);
            calendar.set(5, day);

            calendar.get(1);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public static boolean isCorrectDateSeq(String begin, String end) {
        if ((null == begin) || ("".equals(begin)) || (null == end)
                || ("".equals(end)))
            return false;
        if ((begin.contains("-")) && (end.contains("-"))) {
            String[] strBegin = begin.split("-");
            begin = strBegin[0]
                    .concat("-")
                    .concat((strBegin[1].length() == 2) ? strBegin[1] : "0"
                            .concat(strBegin[1]))
                    .concat("-")
                    .concat((strBegin[2].length() == 2) ? strBegin[2] : "0"
                            .concat(strBegin[2]));

            String[] strEnd = end.split("-");
            end = strEnd[0]
                    .concat("-")
                    .concat((strEnd[1].length() == 2) ? strEnd[1] : "0"
                            .concat(strEnd[1]))
                    .concat("-")
                    .concat((strEnd[2].length() == 2) ? strEnd[2] : "0"
                            .concat(strEnd[2]));
        } else if ((begin.contains("/")) && (end.contains("/"))) {
            String[] strBegin = begin.split("/");
            begin = strBegin[0]
                    .concat("-")
                    .concat((strBegin[1].length() == 2) ? strBegin[1] : "0"
                            .concat(strBegin[1]))
                    .concat("-")
                    .concat((strBegin[2].length() == 2) ? strBegin[2] : "0"
                            .concat(strBegin[2]));

            String[] strEnd = end.split("/");
            end = strEnd[0]
                    .concat("-")
                    .concat((strEnd[1].length() == 2) ? strEnd[1] : "0"
                            .concat(strEnd[1]))
                    .concat("-")
                    .concat((strEnd[2].length() == 2) ? strEnd[2] : "0"
                            .concat(strEnd[2]));
        } else {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date();
        Date endDate = new Date();
        try {
            beginDate = sdf.parse(begin);
            endDate = sdf.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ((endDate.after(beginDate)) || (endDate.equals(beginDate)));
    }

    public static boolean isYearIn20Range(String date) {
        if ((null == date) || ("".equals(date)))
            return false;
        if (date.contains("-")) {
            String[] str = date.split("-");
            date = str[0]
                    .concat("-")
                    .concat((str[1].length() == 2) ? str[1] : "0"
                            .concat(str[1]))
                    .concat("-")
                    .concat((str[2].length() == 2) ? str[2] : "0"
                            .concat(str[2]));
        } else if (date.contains("/")) {
            String[] str = date.split("/");
            date = str[0]
                    .concat("-")
                    .concat((str[1].length() == 2) ? str[1] : "0"
                            .concat(str[1]))
                    .concat("-")
                    .concat((str[2].length() == 2) ? str[2] : "0"
                            .concat(str[2]));
        } else {
            return false;
        }
        int year = Integer.parseInt(date.substring(0, 4));
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setLenient(false);
        int curYear = calendar.get(1);

        return ((year >= curYear - 20) && (year <= curYear + 20));
    }

    public static Date getCurrentDay() {
        return new Date();
    }
    
    /**
     * 获取当前的时间
     * @return
     */
    public static String getTime(){
   	 SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return sdf.format(System.currentTimeMillis());
   }
    
    public static String paseFromStr(Object str){
   	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
     return sdf.format(str);
    }
    
    public static String paseFromStr2(Object str){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    	sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
    	return sdf.format(str);
    }
    
    public static String paseFromStr(String str){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
    	return sdf.format(Long.valueOf(str));
    }
    
    /**
     * 获取系统时间戳
     * @return
     */
	@SuppressLint("SimpleDateFormat")
	public static String getUnixTime() {
		try {
			Timestamp appointTime = Timestamp.valueOf(new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss").format(new Date()));
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date date = df.parse(String.valueOf(appointTime));
			long s = date.getTime();
			return String.valueOf(s).substring(0, 10);
		} catch (Exception e) {
			return "0";
		}
	}
}
