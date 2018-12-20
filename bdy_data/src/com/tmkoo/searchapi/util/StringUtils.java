package com.tmkoo.searchapi.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class StringUtils {
	
	
	public static String replaceChar(String name) {
		name = name.replaceAll("[/\\\\:*\"?<>|]","");
	    return name;
	}
	
	
	
	// 整型数组排序
	public static void bubbleSort(int[] numbers) {
		int temp; // 记录临时中间值
		int size = numbers.length; // 数组大小
		for (int i = 0; i < size - 1; i++) {
			for (int j = i + 1; j < size; j++) {
				if (numbers[i] > numbers[j]) { // 交换两数的位置
					temp = numbers[i];
					numbers[i] = numbers[j];
					numbers[j] = temp;
				}
			}
		}
	}
	
	// 字符串排序
	public static void listSort(List<?> list) {		
		Collections.sort(list, new Comparator<Object>() {
	      @Override
	      public int compare(Object o1, Object o2) {
	        return new Integer((String) o1).compareTo(new Integer((String) o2));
	      }
	    });		
	}
	
	
	
	
	public static boolean isNum(String str) {
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}
	
	
	public static String getPercent(long y, long z, boolean baifenhao){
		String baifenbi = "";// 接受百分比的值
		double baiy = y * 1.0;
		double baiz = z * 1.0;
		double fen = baiy / baiz;
		// NumberFormat nf = NumberFormat.getPercentInstance();注释掉的也是一种方法
		// nf.setMinimumFractionDigits( 2 ); 保留到小数点后几位
//		DecimalFormat df1 = new DecimalFormat("##.00%");
		DecimalFormat df2 = new DecimalFormat("0.00%");
		
		// ##.00%
		// 百分比格式，后面不足2位的用0补齐
		// baifenbi=nf.format(fen);
//		baifenbi = df1.format(fen);		
		
		baifenbi = df2.format(fen);	
		if(!baifenhao){	
			if (baifenbi!=null){
				int len=baifenbi.length();
				baifenbi = baifenbi.substring(0,len-1);
			}			
		}	
		
		return baifenbi;
	} 
	
    // 字符串合并
	public static String addString(String dest, String source) {

		String data = "";
		if (source != null && !source.equals("")) {
			data = dest + source;
		}else{
			data=dest;
		}
		return data;
	}
	
	
	// 计算字符串的hash值
	public static int getStringHash(String data) {					
	
		int hashValue = HashAlgorithms.FNVHash1(data); 
		
		return hashValue;

	}
	
	
	public static void test(String serviceInfo, String key, String tmType){
		
		String tmTypeStr=tmType;
		int len=tmType.length();
		if (len==1){
			tmTypeStr="0"+tmType;			
		}else if (len==1){
			tmTypeStr=tmType;			
		}
		List<String> serviceInfoList = Arrays.asList(serviceInfo.split(key));
		List<String> arrayList2 = new ArrayList<String>(serviceInfoList);
		
		int count=0;
		Iterator<String> it = arrayList2.iterator();
		while (it.hasNext()) {
			String goodDes=it.next();	
			if (!goodDes.startsWith(tmTypeStr)){
				if (count>0){
					int index=count-1;
					String s=arrayList2.get(index);
					s=s+key+goodDes;
					arrayList2.set(index, s);
					it.remove();
					count--;
				}
			}
			count++;
		}
		
		for(String serviceDescription: arrayList2){		
			System.out.println(serviceDescription);
		}
		
		
	}
	
	
	
	public static void main(String[] args) {
		
		String key=",";
		
//		String serviceInfo="4102_组织文化或教育展览,4101_教育,4101_提供培训,4105_娱乐,4102_组织体育比赛";
//		String tmType="41";
		String serviceInfo="0901_搜索和搜寻照片的计算机软件,0901_观看三维模式照片的计算机软件,0901_创编照片浏览和加注照片文字以及建立照片影集的计算机软件,0901_用于照片和图形位置定位的计算机软件,0901_用于从照片,图像以及三维模型中获取三维数据的计算机软件,0901_用于给照片,图像以及三维模型加注文字的计算机软件";
		String tmType="9";
		test(serviceInfo,key,tmType);
		
//		 ArrayList list = new ArrayList();
//	    list.add("1");
//	    list.add("10");
//	    list.add("22");
//	    list.add("33");
//	    list.add("41");
//	    list.add("45");	    
//	    listSort(list);
	    
//	    System.out.println(list.toString());
		
//		String baifenbi=getPercent(29,59, false);
//		System.out.println(baifenbi);
//		
//		baifenbi=getPercent(29,59, true);
//		System.out.println(baifenbi);
		 
//		String zanwutu="/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCADIAMgDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD3GiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooqhrSNJo9yFk8sgbtxOMAHPb8qAJf7Ss/tP2b7QnnZ27O+atV51ZWkl0ZZvtCwrHgtK5PU+4/GtS20W7vEL2+qRyKDgkO2AaAOwLqpwWAPucU3zY/76/nXHeJ0aKezjZsssCqT6kU2LRLGSFHbWrdGZQSpxx7feoA6sanZNcCAXMZlJ2hQe9Sy3lrbsEnuYYmIyFeQLkfQn2rnNP0axhv4JF1aCZlYERrgEn2w1W9b0KfU71JopY0VY9hDZz1J/rQBqf2nYf8/wBbf9/V/wAaP7TsP+f62/7+r/jXM/8ACI3n/PxB+v8AhWGIGe6ECkFi+wEdCc4oA9C/tOw/5/rb/v6v+NH9p2H/AD/W3/f1f8a5n/hEbz/n4g/X/Cj/AIRG8/5+IP1/woA6qG7trhisNxFKwGSEcMR+Rpsl/ZxOUkuoEcdVaUAj8Ky9E0SfS7qSWWSNwybcLnrn6e1c7ro3a9cDpllH6CgDtP7TsP8An+tv+/q/40f2nYf8/wBbf9/V/wAa5W78OGxgM9xexqgIGQhP6VDb6NBcxl49TtwAcYcbf5mgDsP7TsP+f62/7+r/AI0f2nYf8/1t/wB/V/xrlP8AhHo/+gpaf99f/XqpqOlHT4opBcxzJISAY+elAHexSxzoJInR0PRlbIP40+sjw1/yA4f95v51r0AFFFFABRRRQAUUUUAFZ+twG50qWITRRbivzyvtXr3NaFVNStI72xeCWTy0JBLemKAOJudOa1tmcX9nKuRmOKbcT+HeksLWW4iZkv7e2AbBWSfYT9BWleeHYI7ZmtbsSzA8IXUZ/HNMsPDySxMby5WBwcKokVsj65oAb4lUpNZKWDFbdQWB4P0Pepbrw/bwaKb0Syl/LV9pxjJx7e9Q6jpl5c37FHR4RhUZplwF+meK379FfQWtIpYnlEaoAJAASMdzQBheGLJLm+aZnYGDaygdyfXP0ror/W7XTZ1hnEhdlD/KueP8is3wzZzWU1wJwi71UKBIrE4z6H3rR1HQ7XU7hZpnmVlXZhCB79x70AU5fFViYZBGs3mFTtynf865jTJ4bbUoJ5wxjjO7AGT7d/XFa+taFZabYefHJMZC4QBmGP5VW0HR4tUM5neRUjwAUIGSfqPagDd/4SrTvSf/AL4/+vR/wlWnek//AHx/9euZ1mxi07UGt4WcoFBy555+grbsfDNlc2EE7y3AeRAxAYY59OKANbT9atdSmaOASBlXcdy4GPz965PWv+Rhn/31/kK6rTtEttMmaWF5WZl2kOQR+grlda/5GGf/AH1/kKALuuaffebLNPc/6KZMorM7BfwAOOuKqx2GjGAu+qMXVcsqxkZ+mRzV+/v9aW8uIokJhDEKDECCKyV0TUriQkWbITknI2j8KAK8Fo97d+TaIzZPGew9Sa19dshp2l2Ft5m8qzktjrn2/GobNNb00OlvayLuOWPlBs/jUmtyXUulWL3qlZyz7gV2nFAG54a/5AcP+838616yPDX/ACA4f95v51r0AFFFFABRRRQAUUUUAFZuv/8AIDuvoP5itKs3X/8AkB3X0H8xQBxdhFZTO/225aBQAVITdk/gKv8A2LQv+gpL/wB+j/hVCwntbd3a6tftCkAKN+3Bq9/aWkf9Af8A8jGgBfsWhf8AQUl/79H/AApktpoqxOU1ORnCkqpiPJ/Knf2lpH/QH/8AIxpH1DSWjYJpOHKkKTKeKAE8Nf8AIch/3W/ka7quG8MIza1GQpIVWJI6AY7/AJ1sa3rt1pt8IIUhZCgbLqc/zoAreMJ/mtrcHoC5H6D+RrQ8LweVo4kI5lcv/T+lcpeXdzq16rugMjAIqRg/pmu/s4BbWUEHeNAv40Acd4o/5DTf9c1rq9I/5BFp/wBclrlfFKMur7ipAaNSD2NJbeJr21tooEjgKxqFBKnOPfmgDt64PWv+Rhn/AN9f5Cug0LWbnVJpUmSJQiggoDnP4mud107deuG64ZTj8BQBP4nWNdWJVGRyoLEjhvcEf54qMR3BUEa3CBjgec4x+lSX2uwaiVNzpysVyFIlIOPqBVT7Xp//AEDP/JhqALHlXH/Qcg/7/P8A4VJqqsujWAe4Wc75MyKxbP5iqf2vT/8AoGf+TDU28v1uLaC3itxDFEWIG/dkn6/SgDrfDX/IDh/3m/nWvWV4cjZNEgDqQSWIB4OCa1aACiiigAooooAKKKKACkwGGCMg9jS0UAM8qP8AuL+VHlR/3F/Kn0UAM8qP+4v5UeVH/cX8qfRQAgVV6KBn0FLRRQAUUUUAIVVhgqCPcU3yo/7i/lT6KAECKvRQM+gpjQQuxLRISepKjJ/SpKKAIvs0H/PCP/vgUfZoP+eEf/fAqWigCL7NB/zwj/74FKLaAHIhjBHcIKkooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAPSiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKAP/9k=";
//			
//		int wutuHashValue=getStringHash(zanwutu);
//		
//		System.out.println(wutuHashValue);
		
//		String name="靳羽西 护照号码:204979336";
//		String result=replaceChar(name);
//		
//		System.out.println(result);
		 
		 
	}


}
