package com.tmkoo.searchapi.util;
 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class Pinyin { 
	
	
	public static List<String> execute(String key){ 
		char [] cs = key.toCharArray();  
		List< String>  rtn= new ArrayList<String>();
		//每个单字，都去获得读音（可能是多个）。
		List<String[]> pinyins= new ArrayList<String[]>();
		for(int i=0;i<cs.length;i++){
			pinyins.add(execute(cs[i]));
		} 
		//计算总记录数
		int allCount=1;
		for(int i=0;i<pinyins.size();i++ ){
			allCount = allCount * pinyins.get(i).length;
		}
		
		//初始化最后的拼音
		List<String > pinyinAll = new ArrayList<String>();
		for(int i=0;i<allCount;i++ ){
			pinyinAll.add(""); 
		}
		
		//构造最后一位
		String [] pinyin = pinyins.get(pinyins.size()-1) ;
		int index=0;
		for(int j=0;j<allCount/pinyin.length;j++){
			for(int k=0;k<pinyin.length;k++){
				pinyinAll.set(index,pinyin[k]);
				index++;
			}
		}
		//构造前面的
		int lastCount = pinyins.get( pinyins.size()-1).length;
		
		for(int i= pinyins.size()-2 ; i>=0 ; i--){
			index = 0;
			pinyin = pinyins.get(i) ;
			for(int t=0;t<allCount/lastCount/pinyin.length;t++){
				 for(int j=0;j<pinyin.length;j++){
					 String x = pinyin[j];
					 for(int k=0;k<lastCount;k++){ 
						 String tmp = x +" "+ pinyinAll.get(index);
						 pinyinAll.set(index,tmp); 
						 index++;
					 }
				 }
			}
			lastCount = lastCount * pinyin.length;
		}
		return pinyinAll;
	}
	
	/**
	 * 根据中文汉字，获得拼音，若多个只取第一个？？？
	 * @param cnChar
	 * @return
	 */
	
	private static String[] execute(char cnChar){
		try{
			HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
			format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 是否有音调
			format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
			String[] pinyinArray = null;
			try
			{
				pinyinArray = PinyinHelper.toHanyuPinyinStringArray(cnChar, format);
			}
			catch (BadHanyuPinyinOutputFormatCombination e)
			{
				e.printStackTrace();
			} 
			HashMap<String,String> map = new HashMap<String,String>();
			for(int i=0;i<pinyinArray.length;i++){
				if(map.get(pinyinArray[i]) == null){
					map.put(pinyinArray[i], pinyinArray[i]);
				}
			}
			List<String> rtn = new ArrayList<String>();
			for (Iterator iter = map.keySet().iterator(); iter.hasNext();) {
				String pinyin = (String) iter.next(); 
				rtn.add(pinyin);
			}
			String [] rtnA= new String[rtn.size()];
			for(int i=0;i<rtn.size();i++){
				rtnA[i] = rtn.get(i);
			}
			return rtnA; 
		}
		catch(Exception ex){
			System.out.println("----==拼音"+cnChar+"异常："+ex.getMessage()); 
			String [] rtnA= new String[1];
			rtnA[0]="null";
			return rtnA;
		}
	}

	
	public static void main(String []argus){
//		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
//		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 是否有音调
//		format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
//		String[] pinyinArray = null;
//		try
//		{
//			pinyinArray = PinyinHelper.toHanyuPinyinStringArray('重', format);
//		}
//		catch (BadHanyuPinyinOutputFormatCombination e)
//		{
//			e.printStackTrace();
//		}
//		if(pinyinArray != null && pinyinArray.length >0){
//			for(int i=0;i<pinyinArray.length;i++){
//			 System.out.println(pinyinArray[i]);
//			}
//		} 
		
		List<String > ss = Pinyin.execute("重庆人民");
		for(String s:ss){
			System.out.println(s);
		}
	}
}
