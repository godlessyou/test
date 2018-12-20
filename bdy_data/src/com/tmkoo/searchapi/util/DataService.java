package com.tmkoo.searchapi.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tmkoo.searchapi.vo.Applicant;

public class DataService {

	public static Logger logger = Logger.getLogger(DataService.class);

	public static void deleteDateFromTable(String tableName) throws Exception {
		Connection con = null;
		Statement statement = null;

		try {
			con = DatabaseUtil.getConForHgj();
			statement = con.createStatement();
			String sql = "truncate table " + tableName;
			statement.executeUpdate(sql);

		} catch (java.lang.ClassNotFoundException e) {
			throw e;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static List<Integer> getCustomerId() throws Exception {

		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;
		List<Integer> custIds = new ArrayList<Integer>();
		try {

			//
			con = DatabaseUtil.getConForHgj();
			statement = con.createStatement();
			String selectSql = "select custId from customer";
			rs = statement.executeQuery(selectSql);
			if (rs == null) {
				throw new RuntimeException("数据库内没有符合条件的数据");
			}
			if (rs.next()) {
				do {
					// 获取appId
					int id = rs.getInt(1);
					custIds.add(id);
				} while (rs.next());
			} else {
				throw new RuntimeException("数据库内没有符合条件的数据");

			}

		} catch (java.lang.ClassNotFoundException e) {
			throw e;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return custIds;
	}

	public static List<String> getCustNames() throws Exception {

		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();
		try {

			//
			con = DatabaseUtil.getConForHgj();
			statement = con.createStatement();
			String selectSql = "select distinct(custName) from customer";
			rs = statement.executeQuery(selectSql);
			if (rs == null) {
				throw new RuntimeException("数据库内没有符合条件的数据");
			}
			if (rs.next()) {
				do {

					String custName = rs.getString(1);
					list.add(custName);
				} while (rs.next());
			} else {
				throw new RuntimeException("数据库内没有符合条件的数据");

			}

		} catch (java.lang.ClassNotFoundException e) {
			throw e;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	// 获取客户名称
	public static String getCustomerName(Integer custId) throws Exception {

		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;
		String custName = null;
		try {

			//
			con = DatabaseUtil.getConForHgj();
			statement = con.createStatement();
			String selectSql = "select custName from customer where custId="
					+ custId;
			rs = statement.executeQuery(selectSql);
			if (rs == null) {
				throw new RuntimeException("数据库内没有符合条件的数据");
			}
			if (rs.next()) {
				do {
					// 获取appId
					custName = rs.getString(1);
					break;
				} while (rs.next());
			} else {
				throw new RuntimeException("数据库内没有符合条件的数据");

			}

		} catch (java.lang.ClassNotFoundException e) {
			throw e;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return custName;
	}

	public static Map<Integer, Integer> getCustomerInfo() throws Exception {

		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;
		Map<Integer, Integer> custIds = new HashMap<Integer, Integer>();
		try {

			con = DatabaseUtil.getConForHgj();
			statement = con.createStatement();
			String selectSql = "select custId, wpmCustId from customer";
			rs = statement.executeQuery(selectSql);
			if (rs == null) {
				throw new RuntimeException("数据库内没有符合条件的数据");
			}
			if (rs.next()) {
				do {
					// 获取appId
					int hgjCustId = rs.getInt(1);
					int wpmcustId = rs.getInt(2);
					custIds.put(hgjCustId, wpmcustId);
				} while (rs.next());
			} else {
				throw new RuntimeException("数据库内没有符合条件的数据");

			}

		} catch (java.lang.ClassNotFoundException e) {
			throw e;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return custIds;
	}

	public Map<String, Integer> getCustInfo() throws Exception {

		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;
		Map<String, Integer> custIds = new HashMap<String, Integer>();
		try {

			con = DatabaseUtil.getConForHgj();
			statement = con.createStatement();
			String selectSql = "select custId, custName from customer";
			rs = statement.executeQuery(selectSql);
			if (rs == null) {
				throw new RuntimeException("数据库内没有符合条件的数据");
			}
			if (rs.next()) {
				do {
					// 获取appId
					int custId = rs.getInt(1);
					String custName = rs.getString(2);
					custIds.put(custName, custId);
				} while (rs.next());
			} else {
				throw new RuntimeException("数据库内没有符合条件的数据");

			}

		} catch (java.lang.ClassNotFoundException e) {
			throw e;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return custIds;
	}

	public static Map<Integer, String> getApplicantInfo(Integer custId,
			String condition) throws Exception {

		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;
		Map<Integer, String> applicants = new HashMap<Integer, String>();

		try {

			//
			con = DatabaseUtil.getConForHgj();
			statement = con.createStatement();

			String selectSql = "";
			// 当appType为1，意味着申请人是商标权利人，

			if (custId != null) {
				selectSql = "select id, applicantName from applicant where custId="
						+ custId + condition;
			} else {
				selectSql = "select id, applicantName from applicant where 1=1 "
						+ condition;
			}
			rs = statement.executeQuery(selectSql);
			if (rs == null) {
				throw new RuntimeException("数据库内没有符合条件的数据");
			}
			if (rs.next()) {
				do {
					// 获取appId
					int id = rs.getInt(1);
					String appName = rs.getString(2);
					applicants.put(id, appName);
				} while (rs.next());
			} else {
				logger.info("数据库内没有符合条件的数据");

			}

		} catch (java.lang.ClassNotFoundException e) {
			throw e;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return applicants;
	}

	// 获取申请人名称
	public static List<String> getAppNames(int custId) throws Exception {

		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;
		List<String> applicants = new ArrayList<String>();

		try {

			//
			con = DatabaseUtil.getConForHgj();
			statement = con.createStatement();
			// 当appType为1，意味着申请人是商标权利人，
			String selectSql = "select applicantName from applicant where appType=1 and custId="
					+ custId;
			rs = statement.executeQuery(selectSql);
			if (rs == null) {
				logger.info("数据库内没有符合条件的数据");
			}
			if (rs.next()) {
				do {
					String appName = rs.getString(1);

					if (appName != null) {

						if (appName.indexOf("（") > -1) {
							appName = appName.replaceAll("（", "(");
						}
						if (appName.indexOf("）") > -1) {
							appName = appName.replaceAll("）", ")");
						}
					}

					applicants.add(appName);
				} while (rs.next());
			} else {
				logger.info("数据库内没有符合条件的数据");

			}

		} catch (java.lang.ClassNotFoundException e) {
			throw e;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return applicants;
	}
	

	//获取申请人名称
	public static List<String> getAppNameList(String interval) throws Exception {

		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;
		
		List<String> appNameList= new ArrayList<String>();
		List<String> rtnAppNames = new ArrayList<String>();
		
	
		

		try {

			//
			con = DatabaseUtil.getConForHgj();
			statement = con.createStatement();
			// 当appType为1，意味着申请人是商标权利人，
			// 获取所有申请人，新增加的申请人排在前面（按custId从大到小排序）
			String selectSql = "select applicantName, applicantEnName from applicant where  "
					+ "applicantName<>'腾讯科技（深圳）有限公司' order by id DESC ";
			rs = statement.executeQuery(selectSql);
			if (rs == null) {
				logger.info("数据库内没有符合条件的数据");
			}
			if (rs.next()) {
				do {
					
					String appName = rs.getString(1);
//					String appEnName = rs.getString(2);					
					
					if(appName!=null && !appName.equals("")){
						if(!rtnAppNames.contains(appName)){
							rtnAppNames.add(appName);		
						}
					}
//					if(appEnName!=null && !appEnName.equals("")){
//						if(!rtnAppNames.contains(appName)){
//							rtnAppNames.add(appEnName);		
//						}
//					}
					
						
				} while (rs.next());
			} else {
				logger.info("数据库内没有符合条件的数据");

			}
			rs.close();
			statement.close();
			
			
			
			statement = con.createStatement();			
			// 获取更新过商标的申请人
			selectSql = "select appName from trademarkupdate  where opt='tm' and appName<>'腾讯科技（深圳）有限公司' ";
			rs = statement.executeQuery(selectSql);
			if (rs == null) {
				logger.info("数据库内没有符合条件的数据");
			}
			if (rs.next()) {
				do {					
					String appName = rs.getString(1);
					
					// 删除客户Id相同，并且申请人名字相同的
					// 剩下的是未更新过商标的申请人
					if(appName!=null && !appName.equals("")){
						Iterator<String> it = rtnAppNames.iterator();
						while (it.hasNext()) {							
							String name= it.next();	
							if (name.equals(appName) ){
								it.remove();
							}
						}						
					}					
						
				} while (rs.next());
			} else {
				logger.info("数据库内没有符合条件的数据");

			}
			rs.close();
			statement.close();
			
			
			// 获取所有更新日期符合要求的申请人
			statement = con.createStatement();
			selectSql = "select distinct(appName) from trademarkupdate where  opt='tm' "
					+ "and appName<>'腾讯科技（深圳）有限公司' "
					+ "and DATE_ADD(optDate, INTERVAL " + interval + " DAY) < curdate() "
					+ "and appName in (select distinct(applicantName) from applicant) "
					+ "order by optDate ASC";
			rs = statement.executeQuery(selectSql);
			if (rs == null) {
				logger.info("数据库内没有符合条件的数据");
			}
			if (rs.next()) {
				do {
					
					String appName = rs.getString(1);	
					if(appName!=null && !appName.equals("")){	
						if(!appNameList.contains(appName)){
							appNameList.add(appName);	
						}
					}
					
						
				} while (rs.next());
			} else {
				logger.info("数据库内没有符合条件的数据");

			}

		} catch (java.lang.ClassNotFoundException e) {
			throw e;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		//将上述结果组合在一起，就是需要更新商标的申请人，并且未更新过的申请人排在前面
		for(String appName: appNameList){			
			boolean sameOne=false;
			for(String name: rtnAppNames){				
				if (appName.equals(name)){
					sameOne=true;
					break;
				}				
			}			
			if (!sameOne){
				rtnAppNames.add(appName);
			}
						
		}
		return rtnAppNames;
	}
	
	
	/*

	// 获取申请人名称
	public static List<String> getAppNames(String interval) throws Exception {

		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;
		List<String> applicants = new ArrayList<String>();
		
		List<String> applicantList = new ArrayList<String>();

		try {

			con = DatabaseUtil.getConForHgj();
			statement = con.createStatement();
			// 选择所有没有获取/更新过商标的申请人，再加上已经更新过商标的申请人（按照更新时间排序，最先更新的排在前面））
			String selectSql = "select a.appName from ("
					+ "SELECT distinct(appName) FROM applicant1 "
					+ "WHERE LENGTH(appName) >12 "
					+ "and appName not in "
					+ "("
					+ "SELECT appName FROM applicant1 where  "
					+ "(appName like '%楼%' and appName like '%号%')  "
					+ "or (appName like '%巷%' and appName like '%号%')  "
					+ "or (appName like '%第%' and appName like '%号%')	"
					+ "or appName like '%证号%'	"
					+ "or appName like '%护照号%' "
					+ "or appName like '%新竹市%'	"
					+ "or appName like '%??%' "
					+ "or appName like '%（A）%' "
					+ "or appName like '%（B）%' "
					+ "or appName like '%（D）%' "
					+ "or appName like '%（0）%' "
					+ "or appName like '%（1）%' "
					+ "or appName like '%（2）%' "
					+ "or appName like '%（7）%' "
					+ "or appName like '%（8）%' "
					+ "or appName like '%（9）%' "
					+ ") "
					+ "and appName not in"
					+ "("
					+ "select b.appName from (select appName, RIGHT(appName, 1) as endValue from applicant1 "
					+ "WHERE LENGTH(appName) > 6 ) b "
					+ "where b.endValue in ('0','1','2','3','4','5','6','7','8','9','X') "
					+ ") "
					+ "union "
					+ "select c.appName from "
					+ "("
					+ "select appName, RIGHT(appName, 1) as endValue "
					+ "from applicant1 where  LENGTH(appName) =12 "
					+ ") c "
					+ "where c.endValue in ('厂','学','所','店','屋','库','馆','校','吧','社','院','矿','茶','讯','坊') "
					+ ") a "
					+ "where a.appName not in (select appName from trademarkupdate  where opt='tm') "
					+ "union "
					+ "("
					+ "select  appName from trademarkupdate where opt='tm' "
					+ "and DATE_ADD(optDate, INTERVAL "
					+ interval
					+ " DAY) < curdate() "
					+ "and appName in (select distinct(appName) from applicant1) "
					+ "order by optDate ASC" + ")";
			rs = statement.executeQuery(selectSql);
			if (rs == null) {
				logger.info("数据库内没有符合条件的数据");
			}
			if (rs.next()) {
				do {
					String appName = rs.getString(1);

					if (appName != null) {
						if (!applicants.contains(appName))
							applicants.add(appName);
					}

				} while (rs.next());
			} else {
				logger.info("数据库内没有符合条件的数据");

			}
			rs.close();
			statement.close();
			
			
			statement = con.createStatement();
			// 选择所有没有获取/更新过商标的申请人，再加上已经更新过商标的申请人（按照更新时间排序，最先更新的排在前面））
			selectSql = "select  distinct(appName) from trademarkupdate where opt='tm' "
					+ "and DATE_ADD(optDate, INTERVAL "
					+ interval
					+ " DAY) < curdate() "
					+ "and appName in (select distinct(appName) from applicant1) "
					+ "order by optDate ASC";
			rs = statement.executeQuery(selectSql);
			if (rs == null) {
				logger.info("数据库内没有符合条件的数据");
			}
			if (rs.next()) {
				do {
					String appName = rs.getString(1);

					if (appName != null) {
						if (!applicantList.contains(appName))
							applicantList.add(appName);
					}

				} while (rs.next());
			} else {
				logger.info("数据库内没有符合条件的数据");

			}

		} catch (java.lang.ClassNotFoundException e) {
			throw e;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		

		//剔除重复的申请人
		for(String appName: applicantList){			
			boolean sameOne=false;
			for(String name: applicants){			
				if (appName.equals(name)){
					sameOne=true;
					break;
				}				
			}			
			if (!sameOne){
				applicants.add(appName);
			}
			
			
		}
		

		return applicants;
	}
	
	*/

	// 获取客户商标数据更新的时间
	public List<String> getAppNameHasTm(String interval) throws Exception {
		String status = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		List<String> list = new ArrayList<String>();

		try {
			con = DatabaseUtil.getConForHgj();

			String query = "select distinct(appName) from  trademarkupdate ";
			
			if (interval!=null) {
				query=query+ " where opt='tm' and DATE_ADD(optDate, INTERVAL "+ interval + " DAY) < curdate() ";
			}

			pstmt = (PreparedStatement) con.prepareStatement(query);

			rs = pstmt.executeQuery();

			if (rs == null) {
				return null;
			}
			if (rs.next()) {
				do {
					String name = rs.getString(1);
					if (name != null && !list.contains(name)) {
						list.add(name);
					}

				} while (rs.next());
			}

		} catch (java.lang.ClassNotFoundException e) {
			throw e;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	// 获取申请人名称
	public static List<String> getAppNameFromTm(int custId) throws Exception {

		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;
		List<String> applicants = new ArrayList<String>();

		try {

			//
			con = DatabaseUtil.getConForHgj();
			statement = con.createStatement();
			// 当appType为1，意味着申请人是商标权利人，
			String selectSql = "select distinct(applicantname) from trademark where custId="
					+ custId;
			rs = statement.executeQuery(selectSql);
			if (rs == null) {
				logger.info("数据库内没有符合条件的数据");
			}
			if (rs.next()) {
				do {
					String appName = rs.getString(1);

					applicants.add(appName);
				} while (rs.next());
			} else {
				logger.info("数据库内没有符合条件的数据");

			}

		} catch (java.lang.ClassNotFoundException e) {
			throw e;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return applicants;
	}

	// 获取申请人名称
	public static List<String> getTmAppNames(int custId) throws Exception {

		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;
		List<String> applicants = new ArrayList<String>();

		try {

			//
			con = DatabaseUtil.getConForHgj();
			statement = con.createStatement();
			// 当appType为1，意味着申请人是商标权利人，
			String selectSql = "select distinct(applicantname) from trademark where appType=1 and custId="
					+ custId;
			rs = statement.executeQuery(selectSql);
			if (rs == null) {
				logger.info("数据库内没有符合条件的数据");
			}
			if (rs.next()) {
				do {
					String appName = rs.getString(1);

					applicants.add(appName);
				} while (rs.next());
			} else {
				logger.info("数据库内没有符合条件的数据");

			}

		} catch (java.lang.ClassNotFoundException e) {
			throw e;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return applicants;
	}

	public static void updateHgjDb() {
		try {
			// logger.info("start insert applicant table");
			// insertApplicantData();
			// logger.info("end insert applicant table");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static List<Integer> checkParam(String[] args, List<Integer> custIds) {

		if (args == null || args.length == 0) {
			return custIds;
		}

		List<Integer> usedCustIds = new ArrayList<Integer>();
		for (String idstr : args) {
			if (!StringUtils.isNum(idstr)) {
				logger.info("custId参数必须是数字");
				continue;
			}
			int id = Integer.parseInt(idstr);
			boolean find = false;
			// 检查输入的参数中的custId是否符合
			for (Integer hgjCustId : custIds) {
				int custid = hgjCustId.intValue();
				if (id == custid) {
					find = true;
					break;
				}
			}
			// 如果参数不符合，那么直接返回。
			if (!find) {
				logger.info("custId参数必须是32，33，34，39，40，41，42,...中的任何一个或者几个");

				break;
			}
			usedCustIds.add(id);

		}

		return usedCustIds;
	}

	public static void main(String[] args) {
		try {

			updateHgjDb();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
