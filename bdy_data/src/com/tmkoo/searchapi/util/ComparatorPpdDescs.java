package com.tmkoo.searchapi.util;
 

import java.util.Comparator;

import com.tmkoo.searchapi.vo.JsonTmInfoList;

public class ComparatorPpdDescs  implements Comparator<JsonTmInfoList> {

	private String searchKey;//搜索关键词
	public ComparatorPpdDescs(String searchKey){
		this.searchKey = searchKey;
	}
	
	
	/**
     * 如果o1小于o2,返回一个负数;如果o1大于o2，返回一个正数;如果他们相等，则返回0;
     */
	@Override
	public int compare(JsonTmInfoList d1, JsonTmInfoList d2) {
		int sim1 = MyLevenshtein.similar(d1.getTmName(),searchKey);
		int sim2 = MyLevenshtein.similar(d2.getTmName(),searchKey);
		 if(sim1<sim2){
			 return  1;
		 }
		 else  if(sim1 == sim2){
			 return 0;
		 } 
		 else {
			 return  -1;
		}
	}
}
