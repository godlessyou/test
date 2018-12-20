package com.tmkoo.searchapi.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * DateUtil 类主要是提供了对日期操作的一些方法，主要是指日期类型与字符型的转换；<br>
 * 日期的比较、日期的输入格式化等。
 */
public class DateUtil {

	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public DateUtil() {
	}

	public static boolean isDate(String s) {
		return parseDate(s) != null;
	}

	
	
	/**
	 * 获得当前日期天内的毫秒值
	 * 
	 * @param currDate 
	 * @return 毫秒值
	 */
	public static long getCurrDayTimeMillis(Date currDate) {
		long currTime = 0;
		if (currDate == null)
			return currTime;
		Calendar cal = Calendar.getInstance();
		cal.setTime(currDate);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minu = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);
		currTime = (hour * 60 * 60 + minu * 60 + sec) * 1000;
		return currTime;
	}

	/**
	 * 仅显示年月日的时间
	 * 
	 * @param date
	 * @return 日期字符串
	 */
	public static String showSimpleDate(String date) {
		if (date == null || date.trim().equals(""))
			return "";
		if (date.trim().length() > 10)
			return date.trim().substring(0, 10);
		return date.trim();
	}

	/**
	 * 根据字符型的年、月、日参数，转换成相应的日期，<br>
	 * 如果年、月、日 三个参数当中有一个是无效的，则返回空值，<br>
	 * 需要注意的是，这里无效是指输入的字符串无法转换成整型数值。
	 */
	public static Date parseDate(String year, String month, String day) {
		int intYear = 0;
		int intMonth = 0;
		int intDay = 0;
		try {
			intYear = Integer.parseInt(year);
			intMonth = Integer.parseInt(month);
			intDay = Integer.parseInt(day);
		} catch (Exception ex) {
			return null;
		}
		return parseDate(intYear, intMonth, intDay);
	}

	/**
	 * 根据字符型的年、月、日参数，转换成相应的日期，<br>
	 * 此方法对于 int month , int day 参数范围不做限制，<br>
	 * 比如设置month为15，day为34等，系统可以直接将超出的部分累加到下一年或下一月。
	 */
	public static Date parseDate(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day);
		return cal.getTime();
	}

	/**
	 * 与Date parseDate(int year,int month,int day)方法类似，只是多了时、分、秒三个参数
	 */
	public static Date parseDate(int year, int month, int day, int hour,
			int min, int sec) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day, hour, min, sec);
		return cal.getTime();
	}

	/**
	 * 将指定字符串按固定格式转换为日期格式，当前兼容的格式如下：<br>
	 * 1、eg. 1978-12-21 14:21:25<br>
	 * 2、eg. 12/21/1978 14:21:35<br>
	 * 如果当前字符串格式违例，则返回null。
	 */

	public static Date parseDate(String strDate, String format) {
		try {
			return getDate(strDate, format);
		} catch (Exception ex) {
			return null;
		}
	}

	private static Date getDate(String strDate, String format) throws Exception {
		SimpleDateFormat formator = new SimpleDateFormat(format);
		return formator.parse(strDate);
	}

	public static Date parseDate(String strDate) {
		Date now = null;
		try {
			now = getDate(strDate, DATETIME_FORMAT);
		} catch (Exception ex) {
			now = null;
		}
		if (now == null) {
			try {
				now = getDate(strDate, DATE_FORMAT);
			} catch (Exception e) {
				now = null;
			}
		}
		return now;
	}

	/**
	 * 判断两个日期之间差了多少天，不足一天，则按一天计算，即20.01天也算21天
	 */
	public static int dateDiff(Date date1, Date date2) {
		if (date1 == null || date2 == null)
			return 0;
		long baseNum = 3600 * 1000 * 24;
		long absNum = Math.abs(date1.getTime() - date2.getTime());
		long mod = absNum % baseNum;
		int num = (int) (absNum / baseNum);
		if (mod > 0)
			num++;
		return num;
	}

	/**
	 * 判断两个日期是否相等
	 * 
	 * @param date1
	 * @param date2
	 * @return 0:相等 1:date1 > date2 -1:date1 < date2
	 */
	public static int dateCompare(Date date1, Date date2) {
		if (date1 == date2)
			return 0;
		if (date1 == null && date2 == null)
			return 0;
		long time1 = 0;
		long time2 = 0;
		if (date1 != null)
			time1 = date1.getTime();
		if (date2 != null)
			time2 = date2.getTime();
		if (time1 == time2)
			return 0;
		if (time1 > time2)
			return 1;
		return -1;
	}

	/**
	 * 将指定日期增量后得到一个新的日期
	 * 
	 * @param type
	 *            增量类型，Calendar.DAY_OF_MONTH、Calendar.MONTH等
	 * @param num
	 *            增量的数量值
	 */
	public static Date dateAdd(Date date, int type, int num) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(type, num);
		return cal.getTime();
	}

	public static String shortDateForChina(Date date) {
		String r = "";
		SimpleDateFormat formator = new SimpleDateFormat("yyyy年 MM月 dd日");
		try {
			r = formator.format(date);
		} catch (Exception ex) {
			r = formator.format(new Date());
		}
		return r;
	}

	/**
	 * 将日期按无格式方式输出，即：按yyyyMMddHHmmss这样的格式输出，此方法很少用到
	 */
	public static String fullTimeNoFormat(Date date) {
		if (date == null)
			return "";
		String r = "";
		SimpleDateFormat formator = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			r = formator.format(date);
		} catch (Exception ex) {
			r = "";
		}
		return r;
	}

	/**
	 * 将日期按"yyyy-MM-dd HH:mm:ss"格式输出<br>
	 * 如果日期的时间部分全为0，则不显示
	 */
	public static String fullTime(Date date) {
		if (date == null)
			return "";
		String format = DATETIME_FORMAT;
		String s = "";
		SimpleDateFormat formator = new SimpleDateFormat(format);
		try {
			s = formator.format(date);
		} catch (Exception ex) {
			s = "";
		}
		if (s != null && s.length() > 11) {
			String sTime = s.substring(11);
			if (sTime.equals("00:00:00"))
				return s.substring(0, 10);
		}
		return s;
	}

	public static String fullTime() {
		return fullTime(new Date());
	}

	public static String fullTime(long date) {
		return fullTime(new Date(date));
	}

	/**
	 * 将日期按指定格式输出，但仅输出日期部分，不显示时间，其他规则与fullTime(Date date , Locale area)一致
	 */
	public static String shortDate(Date date) {
		String s = fullTime(date);
		if (s == null || s.equals(""))
			return s;
		return s.substring(0, 10);
	}

	/**
	 * 获得当前日期的短日期格式。
	 * @return 日期字符串
	 */
	public static String shortDate() {
		return shortDate(new Date());
	}

	public static String shortDate(long date) {
		return shortDate(new Date(date));
	}

	/**
	 * 显示日期的时间部分
	 */
	public static String shortTime(Date date) {
		if (date == null)
			return "";
		String s = "";
		SimpleDateFormat formator = new SimpleDateFormat("HH:mm:ss");
		try {
			s = formator.format(date);
		} catch (Exception ex) {
			s = "";
		}
		return s;
	}

	public static String shortTime() {
		return shortTime(new Date());
	}

	public static String shortTime(long date) {
		return shortTime(new Date(date));
	}
}

