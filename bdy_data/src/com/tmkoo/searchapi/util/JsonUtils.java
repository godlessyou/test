package com.tmkoo.searchapi.util;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tmkoo.searchapi.common.Global;
import com.tmkoo.searchapi.json.help.JSONArray;
import com.tmkoo.searchapi.json.help.JSONObject;
import com.tmkoo.searchapi.vo.JsonApiProfile;
import com.tmkoo.searchapi.vo.JsonApiProfileAll;
import com.tmkoo.searchapi.vo.JsonApiTodayUseDetail;
import com.tmkoo.searchapi.vo.JsonSbGongGao;
import com.tmkoo.searchapi.vo.JsonSblc;
import com.tmkoo.searchapi.vo.JsonSearchResult;
import com.tmkoo.searchapi.vo.JsonSysp;
import com.tmkoo.searchapi.vo.JsonTmGongGaoInfo;
import com.tmkoo.searchapi.vo.JsonTmInfo;
import com.tmkoo.searchapi.vo.JsonTmInfoList;

public class JsonUtils {
	

	 /**
	  * 
	  * @param searchType  1\2\3\4
	  * @param searchKey  关键词，中文未转码
	  * @return
	  * @throws Exception
	  */
	public static JsonSearchResult doSearch(String searchType, String searchKey, String pageSize, String intCls) throws Exception{
		JsonSearchResult rtn = new JsonSearchResult();
		
		String keyword=URLEncoder.encode(searchKey,"UTF-8");

		
		String url="http://api.tmkoo.com/search.php?apiKey="+Global.webProperties.API_KEY
				+"&apiPassword="+Global.webProperties.API_PASSWORD
				+"&keyword="+keyword
				+"&pageSize="+ pageSize
				+"&searchType="+searchType 
		        +"&intCls=" + intCls;
		
//		String url="http://api.tmkoo.com/sqr-tm-list.php?apiKey="+Global.webProperties.API_KEY
//				+"&apiPassword="+Global.webProperties.API_PASSWORD
//				+"&applicantCn="+keyword;
		
		String jsonString = GraspUtil.getText(url); 
		
	    JSONObject json= new JSONObject(jsonString); 
	    String ret=json.get("ret").toString();  
	    String msg=json.get("msg").toString(); 
	    String remainCount=json.get("remainCount").toString(); 
	    
	    
	    
	    JSONArray jsonArray=json.getJSONArray("results");  
	    List<JsonTmInfoList> list = new ArrayList<JsonTmInfoList>();
	    for(int i=0;i<jsonArray.length();i++){  
	        JSONObject tm=(JSONObject) jsonArray.get(i);  
	        JsonTmInfoList vo = new JsonTmInfoList();
	        vo.setRegNo(tm.get("regNo").toString());
	        vo.setIntCls(tm.get("intCls").toString());
	        vo.setTmImg(tm.get("tmImg").toString());
	        vo.setTmName(tm.get("tmName").toString());
	        vo.setApplicantCn(tm.get("applicantCn").toString());
	        vo.setAppDate(tm.get("appDate").toString());
	        vo.setCurrentStatus(tm.get("currentStatus").toString());
	        vo.setAnnouncementIssue(tm.get("announcementIssue").toString());
	        vo.setAnnouncementDate(tm.get("announcementDate").toString());
	        vo.setRegIssue(tm.get("regIssue").toString());
	        vo.setRegDate(tm.get("regDate").toString());
	        vo.setAgent(tm.get("agent").toString());
	        list.add(vo);
	    }   
	    
		rtn.setAllRecords(""+list.size());
		rtn.setMsg(msg);
		rtn.setRemainCount(remainCount);
		rtn.setResults(list);
		rtn.setRet(ret);
	    return rtn;  
	}

	public static JsonTmInfo info(String regNoTrue, String intClsTrue) throws Exception {
		String jsonString = GraspUtil.getText("http://api.tmkoo.com/info.php?apiKey="+Global.webProperties.API_KEY+"&apiPassword="+Global.webProperties.API_PASSWORD+"&regNo="+regNoTrue+"&intCls="+intClsTrue); 
		JsonTmInfo rtn = new JsonTmInfo();
	    JSONObject json= new JSONObject(jsonString); 
	    rtn.setRet(json.get("ret").toString());
	    rtn.setMsg(json.get("msg").toString());
	    rtn.setRemainCount(json.get("remainCount").toString());
	    rtn.setId(json.get("id").toString());
	    rtn.setTmImg(json.get("tmImg").toString());
	    rtn.setRegNo(json.get("regNo").toString());
	    rtn.setIntCls(json.get("intCls").toString());
	    rtn.setTmName(json.get("tmName").toString());
	    rtn.setAppDate(json.get("appDate").toString());
	    rtn.setApplicantCn(json.get("applicantCn").toString());
	    rtn.setIdCardNo(json.get("idCardNo").toString());
	    rtn.setAddressCn(json.get("addressCn").toString());
	    rtn.setApplicantOther1(json.get("applicantOther1").toString());
	    rtn.setApplicantOther2(json.get("applicantOther2").toString());
	    rtn.setApplicantEn(json.get("applicantEn").toString());
	    rtn.setAddressEn(json.get("addressEn").toString());
	    rtn.setAnnouncementIssue(json.get("announcementIssue").toString());
	    rtn.setAnnouncementDate(json.get("announcementDate").toString());
	    rtn.setRegIssue(json.get("regIssue").toString());
	    rtn.setRegDate(json.get("regDate").toString());
	    rtn.setPrivateDate(json.get("privateDate").toString());
	    rtn.setAgent(json.get("agent").toString());
	    rtn.setCategory(json.get("category").toString());
	    rtn.setHqzdrq(json.get("hqzdrq").toString());
	    rtn.setGjzcrq(json.get("gjzcrq").toString());
	    rtn.setYxqrq(json.get("yxqrq").toString());
	    rtn.setColor(json.get("color").toString());
	    
	    
	    JSONArray jsonArray=json.getJSONArray("goods");  
	    List<JsonSysp> goods = new ArrayList<JsonSysp>();
	    for(int i=0;i<jsonArray.length();i++){  
	        JSONObject goodsTmp=(JSONObject) jsonArray.get(i);  
	        JsonSysp g = new JsonSysp();
	        g.setGoodsCode(goodsTmp.get("goodsCode").toString()); 
	        g.setGoodsName(goodsTmp.get("goodsName").toString());
	        goods.add(g);
	    }   
        rtn.setGoods(goods);
		 
        jsonArray=json.getJSONArray("flow");  
	    List<JsonSblc> flow = new ArrayList<JsonSblc>();
	    for(int i=0;i<jsonArray.length();i++){  
	        JSONObject flowTmp=(JSONObject) jsonArray.get(i);  
	        JsonSblc f = new JsonSblc();
	        f.setFlowDate(flowTmp.get("flowDate").toString());
	        f.setFlowName(flowTmp.get("flowName").toString()); 
	        flow.add(f);
	    }   
	    rtn.setFlow(flow);
	    return rtn;  
	} 
	
	
	public static JsonTmGongGaoInfo gonggao(String regNo) throws Exception {
		String url="http://api.tmkoo.com/tm-gonggao-list.php?apiKey="+Global.webProperties.API_KEY+"&apiPassword="+Global.webProperties.API_PASSWORD+"&regNo="+regNo;
		String jsonString = GraspUtil.getText(url); 
		JsonTmGongGaoInfo rtn = new JsonTmGongGaoInfo();
	    JSONObject json= new JSONObject(jsonString); 
	    
	    String ret=json.get("ret").toString();  
	    String msg=json.get("msg").toString(); 
	    
	    String remainCount=json.get("remainCount").toString(); 
	    
	    JSONArray jsonArray=json.getJSONArray("gonggaos");  
	    List<JsonSbGongGao> gonggaos = new ArrayList<JsonSbGongGao>();
	    for(int i=0;i<jsonArray.length();i++){  
	        JSONObject gonggaoTmp=(JSONObject) jsonArray.get(i);  
	        JsonSbGongGao g = new JsonSbGongGao();
	        String ggDateStr=gonggaoTmp.get("ggDate").toString();
	        if (ggDateStr!=null && !ggDateStr.equals("")){
		        Date ggDate= DateTool.StringToDate(ggDateStr);
		        
		        g.setGgDate(ggDate);
	        }  
	        g.setGgName(gonggaoTmp.get("ggName").toString());
	        g.setGgQihao(gonggaoTmp.get("ggQihao").toString());
	        g.setGgPage(gonggaoTmp.get("ggPage").toString());
	        g.setVcode(gonggaoTmp.get("vcode").toString());
	        gonggaos.add(g);
	    }   
        rtn.setGonggaos(gonggaos);		 
        rtn.setMsg(msg);
        rtn.setRet(ret);
        rtn.setRemainCount(remainCount);
	    return rtn;  
	} 
	
	public static JsonApiProfile profile(String apiKey, String apiPassword) throws Exception {
		String jsonString = GraspUtil.getText("http://api.tmkoo.com/profile.php?apiKey="+apiKey+"&apiPassword="+apiPassword); 
		JsonApiProfile rtn = new JsonApiProfile();
	    JSONObject json= new JSONObject(jsonString); 
	    rtn.setRet(json.get("ret").toString());
	    rtn.setMsg(json.get("msg").toString());
	    rtn.setRestCount(json.get("restCount").toString());
	    rtn.setPriceCategory(json.get("priceCategory").toString());
	    rtn.setValidDate(json.get("validDate").toString());  
	    return rtn;  
	} 
	
	public static JsonApiProfileAll profileAll(String apiKey, String apiPassword) throws Exception {
		String jsonString = GraspUtil.getText("http://api.tmkoo.com/profile.php?withdetail=true&apiKey="+apiKey+"&apiPassword="+apiPassword); 
		JsonApiProfileAll rtn = new JsonApiProfileAll();
	    JSONObject json= new JSONObject(jsonString); 
	    rtn.setRet(json.get("ret").toString());
	    rtn.setMsg(json.get("msg").toString());
	    rtn.setRestCount(json.get("restCount").toString());
	    rtn.setPriceCategory(json.get("priceCategory").toString());
	    rtn.setValidDate(json.get("validDate").toString());  
	    
	    List<JsonApiTodayUseDetail> todayUserDetails = new ArrayList<JsonApiTodayUseDetail>();
	    
	    JSONArray jsonArray=json.getJSONArray("todayUserDetails");   
	    for(int i=0;i<jsonArray.length();i++){
	    	JSONObject todayUserDetail=(JSONObject) jsonArray.get(i);  
	    	JsonApiTodayUseDetail f = new JsonApiTodayUseDetail();
	        f.setApiName(todayUserDetail.get("apiName").toString());
	        f.setUsedCount(todayUserDetail.get("usedCount").toString());
	        f.setAllCount(todayUserDetail.get("allCount").toString()); 
	        todayUserDetails.add(f); 
	    }   
        rtn.setTodayUserDetails(todayUserDetails);
	    
	    return rtn;  
	}

	public static JsonSearchResult doSqrTmList(String applicantName, String idCardNo) throws Exception{
		JsonSearchResult rtn = new JsonSearchResult();
		String jsonString = GraspUtil.getText("http://api.tmkoo.com/sqr-tm-list.php?apiKey="+Global.webProperties.API_KEY
				+"&apiPassword="+Global.webProperties.API_PASSWORD
				+"&applicantCn="+URLEncoder.encode(applicantName,"UTF-8")
				+"&idCardNo="+idCardNo); 
		
	    JSONObject json= new JSONObject(jsonString); 
	    String ret=json.get("ret").toString();  
	    String msg=json.get("msg").toString(); 
	    String remainCount=json.get("remainCount").toString();  
	    
	    JSONArray jsonArray=json.getJSONArray("results");  
	    List<JsonTmInfoList> list = new ArrayList<JsonTmInfoList>();
	    for(int i=0;i<jsonArray.length();i++){  
	        JSONObject tm=(JSONObject) jsonArray.get(i);  
	        JsonTmInfoList vo = new JsonTmInfoList();
	        vo.setRegNo(tm.get("regNo").toString());
	        vo.setIntCls(tm.get("intCls").toString());
	        vo.setTmImg(tm.get("tmImg").toString());
	        vo.setTmName(tm.get("tmName").toString());
	        vo.setApplicantCn(tm.get("applicantCn").toString());
	        vo.setAppDate(tm.get("appDate").toString());
	        vo.setCurrentStatus(tm.get("currentStatus").toString());
	        vo.setAnnouncementIssue(tm.get("announcementIssue").toString());
	        vo.setAnnouncementDate(tm.get("announcementDate").toString());
	        vo.setRegIssue(tm.get("regIssue").toString());
	        vo.setRegDate(tm.get("regDate").toString());
	        vo.setAgent(tm.get("agent").toString());
	        list.add(vo);
	    }   
	    
		rtn.setAllRecords(""+list.size());
		rtn.setMsg(msg);
		rtn.setRemainCount(remainCount);
		rtn.setResults(list);
		rtn.setRet(ret);
	    return rtn;  
	} 
}
