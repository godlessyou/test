package com.tmkoo.searchapi.util;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.tmkoo.searchapi.vo.TradeMarkProcess;


public class UseComparator {
	public static void main(String args[]) {
		List<TradeMarkProcess> list = new ArrayList<TradeMarkProcess>(); // 数组序列
		TradeMarkProcess b1 = new TradeMarkProcess();
		TradeMarkProcess b2 = new TradeMarkProcess();
		
		b1.setId(1);
		b1.setStatus("等待驳回通知发文");
		Date date1=DateTool.StringToDate("2017-02-01");
		b1.setStatusDate(date1);
		
		
		b2.setId(1);
		b2.setStatus("申请收文");	
		Date date=new Date();
		b2.setStatusDate(date);
		
		
		list.add(b1);
		list.add(b2);
	
//		 Collections.sort(list); //没有默认比较器，不能排序
		System.out.println("数组序列中的元素:");
		myprint(list);
		Collections.sort(list, new statusDateReverseOrder()); // 根据functionId排序
		
		System.out.println("数组序列中的元素:");
		myprint(list);
		
	}
	
	public static void sort(List list, String orderCol, String orderAsc) {
		if (orderAsc!=null && orderAsc.equalsIgnoreCase("desc")){ //降序
			
			if (orderCol.equals("statusDate")){
				Collections.sort(list, new statusDateReverseOrder()); // 根据fileDate降序排序			
			}
		}else{ // 升序
			
			if (orderCol.equals("statusDate")){
				Collections.sort(list, new statusDateComparator()); // 根据fileDate升序排序			
			}
		}
		
		
	}

	// 自定义方法：分行打印输出list中的元素
	public static void myprint(List<TradeMarkProcess> list) {
		Iterator it = list.iterator(); // 得到迭代器，用于遍历list中的所有元素
		while (it.hasNext()) {// 如果迭代器中有元素，则返回true
			System.out.println("\t" + it.next());// 显示该元素
		}
	}


			

	
	
	// 自定义比较器：按fileDate升序来排序
	static class statusDateComparator implements Comparator {
		public int compare(Object object1, Object object2) {// 实现接口中的方法
			TradeMarkProcess p1 = (TradeMarkProcess) object1; // 强制转换
			TradeMarkProcess p2 = (TradeMarkProcess) object2;
			Date date1= p1.getStatusDate();
			Date date2= p2.getStatusDate();
			return date1.compareTo(date2);
		}
	}
	
	
	// 自定义比较器：按fileDate降序来排序
	static class statusDateReverseOrder implements Comparator {
		public int compare(Object object1, Object object2) {// 实现接口中的方法
			TradeMarkProcess p1 = (TradeMarkProcess) object1; // 强制转换
			TradeMarkProcess p2 = (TradeMarkProcess) object2;
			Date date1= p1.getStatusDate();
			Date date2= p2.getStatusDate();
			if (date1!=null && date2!=null){
				return date2.compareTo(date1);
			}else{
				return 1;
			}
		}
	}
		
	
}
