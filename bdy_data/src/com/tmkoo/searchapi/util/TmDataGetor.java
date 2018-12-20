package com.tmkoo.searchapi.util;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tmkoo.searchapi.vo.JsonSbGongGao;
import com.tmkoo.searchapi.vo.JsonTmGongGaoInfo;
import com.tmkoo.searchapi.vo.JsonTmInfo;
import com.tmkoo.searchapi.vo.JsonTmInfoList;

@Component
// Spring Service Bean的标识.
// @Transactional
public class TmDataGetor extends TmDbService {

	private static Logger logger = Logger.getLogger(TmDataGetor.class);

	// 从API接口获取某个申请人的商标列表
	protected List<JsonTmInfoList> getTradeMarkByName(String tmName,
			String tmType, SearchUtil searchUtil) throws Exception {

		// logger.info("按照申请人：" + appName + "，从API接口获取商标数据列表");
		long start = System.currentTimeMillis();
		
		//按什么来查，1: 商标名， 2：注册号， 3：申请人 4：商标名/注册号/申请人只要匹配到就算。
		String searchType="1";

		// 页面大小，即一次api调用最大获取多少条记录，取值范围：[0-50]。
		// 0- 不分页，一次性获得全部查询结果(但是，最多1万条记录)；
		// 1- 每次取1条记录，2-每次取2条记录 。。。。50-每次取50条记录
		String pageSize = "0";

		// 0：全部国际分类
		// 非0：限定在指定类别，类别间用分号分割。如：4;12;34 表示在第4、12、34类内查询
		String intCls = tmType;
				

		List<JsonTmInfoList> JsonTmInfoList = searchUtil.search(searchType, tmName,
				pageSize, intCls);

		long end = System.currentTimeMillis();

		long duration = (end - start) / 1000;

		int size = 0;
		if (JsonTmInfoList != null) {
			size = JsonTmInfoList.size();
		}

		logger.info("从API接口获取" + size + "条商标数据，耗时：" + duration + " 秒");

		return JsonTmInfoList;
	}
	
	
	

	// 从API接口获取某个申请人的商标列表
	protected List<JsonTmInfoList> getTradeMarkListByRegNumber(
			String regNumber, SearchUtil searchUtil) throws Exception{

		List<JsonTmInfoList> list = searchUtil.search("2", regNumber);			

		return list;
	}
	
	

	// 从API接口获取某个申请人的商标列表
	protected List<JsonTmInfoList> getTradeMarkList(String appName,
			SearchUtil searchUtil) throws Exception {
		
		List<JsonTmInfoList> jsonTmInfoList =null;
		int tryTime = 3;
		while (tryTime > 0) {			
			try {
				
				long start = System.currentTimeMillis();

				jsonTmInfoList = searchUtil.search("3", appName);

				long end = System.currentTimeMillis();

				long duration = (end - start) / 1000;

				int size = 0;
				if (jsonTmInfoList != null) {
					size = jsonTmInfoList.size();
					logger.info("从API接口获取" + size + "条商标数据，耗时：" + duration + " 秒");
					break;
				}else {
					tryTime--;
				}
				
			} catch (Exception e) {
				String message=e.getMessage();
				if (message.indexOf("Connection timed out")>-1){
					logger.info(message);
				}else{
					throw e;
				}	
			} finally{				
				tryTime--;
			}			
		}
		
		if (jsonTmInfoList == null || jsonTmInfoList.size()==0) {
			logger.info("从API接口获取 0 条商标数据");
		}
		
		return jsonTmInfoList;
		
	}

	// 从API接口获取商标详细数据
	protected JsonTmInfo getTmDetail(String regNumber, String tmType,
			SearchUtil searchUtil) throws Exception {

		JsonTmInfo jsonTmInfo = null;
		int tryTime = 2;
		while (tryTime > 0) {
			
			try {
				// logger.info("按照注册号：" + regNumber + "，从API接口获取商标详细数据");

				long start2 = System.currentTimeMillis();

				jsonTmInfo = searchUtil.getTmDetailInfo(regNumber, tmType);

				long end2 = System.currentTimeMillis();

				long duration2 = (end2 - start2) / 1000;

				logger.info("按照注册号：" + regNumber + "，获取商标详细数据耗时：" + duration2
						+ " 秒");

				if (jsonTmInfo != null) {
					break;
				} else {
					tryTime--;
				}
				
			} catch (Exception e) {
				String message=e.getMessage();
				if (message.indexOf("Connection timed out")>-1){
					logger.info(message);
				}else{
					throw e;
				}	
			} finally{
				
				tryTime--;
				
				
			}
			

			
		}

		return jsonTmInfo;
	}

	// 从API接口获取商标公告数据
	protected JsonTmGongGaoInfo getGongGao(String regNumber,
			SearchUtil searchUtil, String appName) throws Exception {

		JsonTmGongGaoInfo jsonTmGongGaoInfo = null;
		int tryTime = 2;
		while (tryTime > 0) {
			
			try {
				// logger.info("按照注册号：" + regNumber + "，从API接口获取商标公告数据");

				long start = System.currentTimeMillis();

				jsonTmGongGaoInfo = searchUtil.getTmGongGaoInfo(regNumber);

				long end = System.currentTimeMillis();

				long duration = (end - start) / 1000;

				logger.info("按照注册号：" + regNumber + "，获取" + appName + "的商标公告耗时："
						+ duration + " 秒");

				if (jsonTmGongGaoInfo != null) {
					List<JsonSbGongGao> list = jsonTmGongGaoInfo.getGonggaos();
					if (list != null && list.size() > 0) {
						// jsonTmGongGaoInfo.setCustId(custId);
						jsonTmGongGaoInfo.setRegNumber(regNumber);
					}
					break;
				} else {
					tryTime--;
				}
				
			} catch (Exception e) {
				String message=e.getMessage();
				if (message.indexOf("Connection timed out")>-1){
					logger.info(message);
				}else{
					throw e;
				}	
			} finally{
				
				tryTime--;				
			}
			

			
		}

		return jsonTmGongGaoInfo;
	}

}
