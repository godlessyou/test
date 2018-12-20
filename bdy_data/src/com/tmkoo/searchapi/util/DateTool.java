package com.tmkoo.searchapi.util;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import java.util.GregorianCalendar;

public class DateTool {
	
	public static Date getDateAfterYear(Date d, int year) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(d);
		cd.add(Calendar.YEAR, year);// 增加一月
		return cd.getTime();
	}

	public static Date getDateBeforeMonth(Date d, int month) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(d);
		cd.add(Calendar.MONTH, -month);// 减少一月
		return cd.getTime();
	}

	public static Date getDateAfterMonth(Date d, int month) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(d);
		cd.add(Calendar.MONTH, month);// 增加一月
		return cd.getTime();
	}

	public static Date getDateBefore(Date d, int day) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(d);
		cd.set(Calendar.DATE, cd.get(Calendar.DATE) - day);
		return cd.getTime();
	}

	public static Date getDateAfter(Date d, int day) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(d);
		cd.set(Calendar.DATE, cd.get(Calendar.DATE) + day);
		return cd.getTime();
	}

	public static Date StringToDate(String dateString) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static Date StringToDateTime(String dateString) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String getEnglishDate(Date date) {

		DateFormat df = new SimpleDateFormat("MMMMM dd, yyyy", Locale.ENGLISH);
		String time = df.format(date);
		return time;
	}
	
	public static String getDate(Date date) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String time = df.format(date);
		return time;
	}
	
	public static String getDateTime(Date date) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = df.format(date);
		return time;
	}
	

	public static int getYear(String dateString) {		
		Date date=StringToDate(dateString);
		Calendar calender = Calendar.getInstance();  
		calender.setTime(date);  	
	    int year = calender.get(Calendar.YEAR);		  
		return year;
	}
	
	
	public static int getCurrentYear() {	
		Date today = new Date();// 取时间	
		String dateString = DateTool.getDate(today);
		int yearDate = DateTool.getYear(dateString);		
		return yearDate;
	}
	
	public static int getMonth(String dateString) {		
		Date date=StringToDate(dateString);
		Calendar calender = Calendar.getInstance();  
		calender.setTime(date);  	
	    int month = calender.get(Calendar.MONTH);	
	    month++;
		return month;
	}
	
	public static final int daysBetween(Date early, Date late) { 
	     
        java.util.Calendar calst = java.util.Calendar.getInstance();   
        java.util.Calendar caled = java.util.Calendar.getInstance();   
        calst.setTime(early);   
         caled.setTime(late);   
         //设置时间为0时   
         calst.set(java.util.Calendar.HOUR_OF_DAY, 0);   
         calst.set(java.util.Calendar.MINUTE, 0);   
         calst.set(java.util.Calendar.SECOND, 0);   
         caled.set(java.util.Calendar.HOUR_OF_DAY, 0);   
         caled.set(java.util.Calendar.MINUTE, 0);   
         caled.set(java.util.Calendar.SECOND, 0);   
        //得到两个日期相差的天数   
         int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst   
                .getTime().getTime() / 1000)) / 3600 / 24;   
         
        return days;   
   } 
	
	
	public static void test()
    {
         Date earlydate = new Date();   
         Date latedate = new Date();   
         DateFormat df = DateFormat.getDateInstance();   
         try {   
             earlydate = df.parse("2009-09-21");   
             latedate = df.parse("2009-10-16");   
         } catch (ParseException e) {   
               e.printStackTrace();   
           }   
          int days = daysBetween(earlydate,latedate);   
          System.out.println(days);   
    }

	
	/**

	 * 计算两个时间相差多少个年

	 * 

	 * @param early

	 * @param late

	 * @return

	 * @throws ParseException

	 */

	public static int yearsBetween(Date start, Date end) throws ParseException {
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(start);
		
		Calendar endDate = Calendar.getInstance();
		startDate.setTime(start);
		
		return (endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR));
	}

	
	public static void main(String args[]) {
		
		test();

		String dateString = "1999-02-10";
		Date date = StringToDate(dateString);
		System.out.println(date + "\r\n");
		
		String result = getDateTime(date);
		System.out.println(result + "\r\n");
		
//		Date date2 = StringToDateTime(dateString);
//		
//		System.out.println(date2 + "\r\n");
//		String result = getDate(date);
//		System.out.println(result + "\r\n");
//		
//		int year = getYear(dateString);
//		System.out.println(year);
//
//		// 减12个月
//		Date dateBefore = getDateBeforeMonth(date, 12);
//		// 加一天
//		dateBefore = getDateAfter(dateBefore, 1);
//		result = getEnglishDate(dateBefore);
//		System.out.println(result + "\r\n");
//
		
//		String dateString1 = "2009-09-27";
//		Date validStartDate = StringToDate(dateString1);
//		
//		try {
//			// 加10年月
//			int count=0;
//			Date date=validStartDate;											
//			Date now=new Date();	
//			if(now.getTime()>date.getTime()){
//				int result= yearsBetween(date, now);									
//				count=result/10;	
//			}
//			
//			validStartDate=DateTool.getDateAfterYear(validStartDate, 10*count);			
//			Date date2=DateTool.getDateAfterYear(validStartDate, 10);			
//		
//			String dateString=getDate(date2);
//			Date validEndDate=StringToDate(dateString);
//						
//			System.out.println(validStartDate + "\r\n");
//			System.out.println(dateString + "\r\n");
//			System.out.println(validEndDate + "\r\n");
//
//		
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
		
	}

}

