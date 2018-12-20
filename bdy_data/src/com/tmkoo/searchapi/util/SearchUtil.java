package com.tmkoo.searchapi.util;


import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tmkoo.searchapi.util.JsonUtils;
import com.tmkoo.searchapi.vo.JsonSearchResult;
import com.tmkoo.searchapi.vo.JsonTmGongGaoInfo;
import com.tmkoo.searchapi.vo.JsonTmInfo;
import com.tmkoo.searchapi.vo.JsonTmInfoList;


/**
 * 搜索
 * 
 * @author tmkoo
 */

@Component
//Spring Service Bean的标识.
//@Transactional
public class SearchUtil {
	
	
	private int remainSearchCount=2000;
	
	private int remainInfoCount=40000;
	
	private int remainGgCount=20000;
	
	
	public void init(){
		remainSearchCount=2000;
		remainInfoCount=40000;
		remainGgCount=20000;
	}

	/**
	 * 查询商标，返回符合条件的商标列表
	 * 
	 * @param searchType
	 * @param searchKey
	 * @return
	 */	
	public List<JsonTmInfoList> search(String searchType, String searchKey, String pageSize, String intCls) throws Exception {
		List<JsonTmInfoList> tmList = null;	
//		try {
			JsonSearchResult jsonSearchResult = JsonUtils.doSearch(searchType,
					searchKey, pageSize, intCls);
			
			String remainCt=jsonSearchResult.getRemainCount();
			
			if (remainCt!=null && !remainCt.equals("")){
				setRemainSearchCount(Integer.parseInt(remainCt));
			}
			if (jsonSearchResult.getRet().equals("1")) {
				// 出现错误了
				if (jsonSearchResult.getMsg().indexOf("您今天的search接口调用，超过了最大") >= 0) {
					// 今天的查询次数超过了
					throw new Exception("符合条件的商标列表接口的查询次数配额不足，请您联系网站管理员或者稍后再查询！");
				} else {
					throw new Exception(jsonSearchResult.getMsg());
				}
			}
			tmList  = jsonSearchResult.getResults();			
			
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
		return tmList;
	}
	
	

	/**
	 * 查询商标，返回符合条件的商标列表
	 * 
	 * @param searchType
	 * @param searchKey
	 * @return
	 */	
	public List<JsonTmInfoList> search(String searchType, String searchKey) throws Exception {
		List<JsonTmInfoList> tmList = search(searchType, searchKey,  "0", "0");
		return tmList;
	}
	
/*
	// 获取一组商标的详细信息
	public List<JsonTmInfo> getTmDetail(List<JsonTmInfoList> list) {
	
		List<JsonTmInfo> tmList=new ArrayList<JsonTmInfo>();
		for (JsonTmInfoList JsonTmInfo : list) {
			String regNo = JsonTmInfo.getRegNo();
			String intCls = JsonTmInfo.getIntCls();
			String currentStatus = JsonTmInfo.getCurrentStatus();
			// 参数校验
			if (regNo == null || regNo.trim().equals("") || intCls == null
					|| intCls.trim().equals("")) {
				continue;
			}					
			try {
				JsonTmInfo tmInfo = JsonUtils.info(regNo, intCls);
				if (tmInfo.getRet().equals("1")) {
					// 出现错误了
					System.out.println(tmInfo.getMsg());
					continue;
				}
				tmInfo.setCurrentStatus(currentStatus);
				tmList.add(tmInfo);
				
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
		return tmList;
	}
	
	*/
	
	
    // 获取某个商标的详细信息
	public JsonTmInfo getTmDetailInfo(String regNo, String intCls) throws Exception {
		JsonTmInfo tmInfo = null;

					
//		try {
			tmInfo = JsonUtils.info(regNo, intCls);
			
			String remainCt=tmInfo.getRemainCount();
			
			if (remainCt!=null && !remainCt.equals("")){
				setRemainInfoCount(Integer.parseInt(remainCt));
			}
			
			if (tmInfo.getRet().equals("1")) {
				// 出现错误了
				
				if (tmInfo.getMsg().indexOf("您今天的info接口调用，超过了最大") >= 0) {
					// 今天的查询次数超过了
					throw new Exception("商标详情接口的查询次数配额不足，请您联系网站管理员或者稍后再查询！");
				} else {
					throw new Exception(tmInfo.getMsg());
				}
//				throw new Exception(tmInfo.getMsg());
				
			}
					
			
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}

		return tmInfo;
	}
	
	
	
	//获取商标公告信息
	public JsonTmGongGaoInfo getTmGongGaoInfo(String regNo) throws Exception{
		JsonTmGongGaoInfo gonggaoInfo = null;
					
//		try {
			gonggaoInfo = JsonUtils.gonggao(regNo);	
			
			String remainCt=gonggaoInfo.getRemainCount();
			
			if (remainCt!=null && !remainCt.equals("")){
				setRemainGgCount(Integer.parseInt(remainCt));
			}
			
			if (gonggaoInfo.getRet().equals("1")) {
				// 出现错误了
				if (gonggaoInfo.getMsg().indexOf("您今天的tm-gonggao-list接口调用，超过了最大") >= 0) {
					// 今天的查询次数超过了
					throw new Exception("商标公告接口的查询次数配额不足，请您联系网站管理员或者稍后再查询！");
				} else {
					throw new Exception(gonggaoInfo.getMsg());
				}
			}
			
			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		return gonggaoInfo;
	}
	
	

	public int getRemainSearchCount() {
		return remainSearchCount;
	}


	public void setRemainSearchCount(int remainSearchCount) {
		this.remainSearchCount = remainSearchCount;
	}


	public int getRemainInfoCount() {
		return remainInfoCount;
	}


	public void setRemainInfoCount(int remainInfoCount) {
		this.remainInfoCount = remainInfoCount;
	}


	public int getRemainGgCount() {
		return remainGgCount;
	}


	public void setRemainGgCount(int remainGgCount) {
		this.remainGgCount = remainGgCount;
	}

	
}
