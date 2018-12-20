package com.tmkoo.searchapi.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tmkoo.searchapi.vo.AppDataStatus;
import com.tmkoo.searchapi.vo.Applicant;
import com.tmkoo.searchapi.vo.CustomerApplicant;
import com.tmkoo.searchapi.vo.JsonSbGongGao;
import com.tmkoo.searchapi.vo.JsonTmGongGaoInfo;
import com.tmkoo.searchapi.vo.TmDataStatus;
import com.tmkoo.searchapi.vo.TradeMark;
import com.tmkoo.searchapi.vo.TradeMarkCategory;
import com.tmkoo.searchapi.vo.TradeMarkProcess;

@Component
// Spring Service Bean的标识.
//@Transactional
public class TmDbService extends DataService {

	private static Logger logger = Logger.getLogger(TmDbService.class);
	
	public void processDataOfSameRegNumber(List<String> regNumberList)  throws Exception{
		Connection con = null;
		PreparedStatement pstmt = null;
		
		String regNumbers=null;
		
		for(String regNum: regNumberList){
			if (regNumbers==null){
				regNumbers="'"+regNum+"'";
			}else{
				regNumbers=regNumbers+","+"'"+regNum+"'";
			}
		}

		try {
			con = DatabaseUtil.getConForHgj();
			
			//配置执行环境
			String sqlDefault=" SET group_concat_max_len=15000";
			
			pstmt = (PreparedStatement) con.prepareStatement(sqlDefault);			

			pstmt.executeUpdate();
			
			pstmt.close();
			
			
			//将多条记录合并
			String sql = "update trademark a, "
					+ "("
					+ "select regNumber, GROUP_CONCAT(distinct tmType SEPARATOR ',') as tmType, "
					+ "SUBSTRING(REPLACE(concat(';', GROUP_CONCAT(distinct if(ifnull(tmGroup,'')='','null',tmGroup) SEPARATOR ';')),';null',''),2) as tmGroup "
					+ "from trademark "
					+ "group By regNumber having count(regNumber)>1"
					+ ") b "
					+ "set a.tmGroup=b.tmGroup,  a.tmType=b.tmType "
					+ "where a.regNumber=b.regNumber and a.regNumber in ("+ regNumbers+")";

			pstmt = (PreparedStatement) con.prepareStatement(sql);			

			pstmt.executeUpdate();
			
			pstmt.close();
			
						
			
			//删除重复的记录
			sql = "delete from trademark "
					+ "where tmId not in "
					+ "("
					+ "select tmId from (select min(tmId) as tmId from trademark group By regNumber, tmType) c "
					+ ") and regNumber in ("+ regNumbers+")";

			pstmt = (PreparedStatement) con.prepareStatement(sql);			

			pstmt.execute();
			
			pstmt.close();			
			
			
			//删除重复的记录		
//			sql = "delete from trademark_category "
//					+ "where id not in "
//					+ "("
//					+ "select id from (select id from trademark_category group By regNumber, tmType, tmGroup, name) c "
//					+ ") ";
			
			//修改被删除的tmId所对应的的商品/服务记录中的tmId
			//将tmId设置为合并后的商标记录的tmId		
			sql = "update trademark_category a, trademark b set a.tmId=b.tmId where a.regNumber=b.regNumber and b.regNumber in ("+ regNumbers+")";
			pstmt = (PreparedStatement) con.prepareStatement(sql);			

			pstmt.execute();
			
			pstmt.close();	

		} catch (java.lang.ClassNotFoundException e) {
			throw e;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				
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
	}


	public void updateApplicantInfo(String appName,  List<Applicant>applicantList) throws Exception{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		List<Integer>  custIdList=getCustId(appName);
		
		
		String appNames=null;
	   
		ArrayList<String>appNameList=new ArrayList<String>();
		
		for(Applicant applicant: applicantList){
			String applicantName=applicant.getApplicantName();
			if (applicantName==null || applicantName.equals("")){
				continue;
			}
			if(!appNameList.contains(applicantName)){
				appNameList.add(applicantName);
			}
		}
		
		for(String value: appNameList){		
			if (appNames==null){
				appNames="'"+value+"'";
			}else{
				appNames=appNames+","+"'"+value+"'";
			}
		}

		try {
			con = DatabaseUtil.getConForHgj();
			String query = "select mainAppId, applicantName, applicantEnName from  applicant where applicantName in ("+ appNames+")";
			pstmt = (PreparedStatement) con.prepareStatement(query);
		
			rs = pstmt.executeQuery();
			
			List<Applicant> appList=new ArrayList<Applicant>();
			
			if (rs.next()) {
				do {
					
					Applicant applicant = new Applicant();
					int mainAppId = rs.getInt(1);
					String applicantName=rs.getString(2);
					String applicantEnName=rs.getString(3);
					
					applicant.setMainAppId(mainAppId);
					applicant.setApplicantName(applicantName);
					applicant.setApplicantEnName(applicantEnName);
					
					appList.add(applicant);

				} while (rs.next());
			} else {
				logger.info("数据库内没有符合条件的商标的流程数据");

			}
			
			rs.close();
			pstmt.close();
			
			
			
			String appType="法人或其他组织";			
				
			for (Applicant applicant: applicantList){
				String applicantName=applicant.getApplicantName();
				String applicantEnName=applicant.getApplicantEnName();
				String applicantAddress=applicant.getApplicantAddress();
				String applicantEnAddress=applicant.getApplicantEnAddress();
				
				Integer mainAppId = null;
				boolean hasSameOne=false;
				for (Applicant app: appList){
					String name=app.getApplicantName();
					String appEnName=app.getApplicantEnName();				
					if (applicantName!=null && name!=null && applicantName.equalsIgnoreCase(name)){
						
						mainAppId =app.getMainAppId();
						
						if ((applicantEnName==null || applicantEnName.equals("")) && (appEnName==null || appEnName.equals(""))){
							hasSameOne=true;
							break;
						}
						if (applicantEnName!=null && appEnName!=null && applicantEnName.equalsIgnoreCase(appEnName)){
							hasSameOne=true;
							break;
						}
					}					
				
				}
				
				 //如果数据库中没有与当前申请人中文和英文名称完全相同的记录，那么插入当前申请人
				if (!hasSameOne){
					
					String sql = "insert into applicant (applicantName, applicantEnName, applicantAddress, applicantEnAddress, hasTm, fromTm, appType) "
							+ "values (?, ?, ?, ?, ?, ?, ?)";
					if (mainAppId!=null){
						sql = "insert into applicant (applicantName, applicantEnName, applicantAddress, applicantEnAddress, hasTm, fromTm, appType, mainAppId) "
								+ "values (?, ?, ?, ?, ?, ?, ?, ?)";
					}
					
					pstmt = (PreparedStatement) con.prepareStatement(sql);
					
					pstmt.setString(1, applicantName);
					pstmt.setString(2, applicantEnName);
					pstmt.setString(3, applicantAddress);
					pstmt.setString(4, applicantEnAddress);
					
					pstmt.setInt(5, 1);
					pstmt.setInt(6, 1);
					pstmt.setString(7, appType);	
					
					if (mainAppId!=null){
						pstmt.setInt(8, mainAppId);		
					}
					
					pstmt.execute();
					pstmt.close();
					
					if (mainAppId==null){						
						String querySql = "select max(id) from applicant ";
						pstmt = (PreparedStatement) con.prepareStatement(querySql);						
						rs = pstmt.executeQuery();
						if (rs.next()) {
							do {								
								mainAppId = rs.getInt(1);
								break;
							} while (rs.next());
						} 						
						rs.close();
						pstmt.close();
						
						if (mainAppId!=null){
							sql = "update applicant set mainAppId=? where id=? ";						
							pstmt = (PreparedStatement) con.prepareStatement(sql);
							pstmt.setInt(1, mainAppId);		
							pstmt.setInt(2, mainAppId);	
							pstmt.executeUpdate();
							pstmt.close();
						}
						
					}
				}
				
				 //如果数据库中没有当前申请人与客户的对应关系，那么插入对应关系				
				if (mainAppId!=null){
				
					List<CustomerApplicant> customerApplicantList=new ArrayList<CustomerApplicant>();
					String querySql = "select custId from customer_applicant where appId=?";
					pstmt = (PreparedStatement) con.prepareStatement(querySql);	
					pstmt.setInt(1, mainAppId);
					rs = pstmt.executeQuery();
					if (rs.next()) {
						do {								
							Integer cId = rs.getInt(1);							
							CustomerApplicant customerApplicant=new CustomerApplicant();
							customerApplicant.setCustId(cId);
							customerApplicant.setAppId(mainAppId);							
							customerApplicantList.add(customerApplicant);
							break;
						} while (rs.next());
					} 						
					rs.close();
					pstmt.close();
					
					
					List<Integer> list=new ArrayList<Integer>();					
					for (Integer custId:  custIdList){
						boolean addCustomerApplicant=true;
						for(CustomerApplicant ca:customerApplicantList){
							Integer cId=ca.getCustId();
							if (cId.intValue()==custId.intValue()){
								addCustomerApplicant=false;
								break;
							}
						}
						if (addCustomerApplicant){
							list.add(custId);
						}						
					}
					
					if(list!=null && list.size()>0){
						for  (Integer custId:  list){
							String insertSql = "insert into customer_applicant (custId, appId) values(?,?) ";						
							pstmt = (PreparedStatement) con.prepareStatement(insertSql);
							pstmt.setInt(1, custId);		
							pstmt.setInt(2, mainAppId);						
							pstmt.execute();
							pstmt.close();
						}
					}	
				}				
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
		
		
	}

	
	
	// 细粒度查找数据库中是否有某个商标的商品/服务信息，要求regNumber与tmgroup也必须相同
	public List<Integer> getCustId(String appName) throws Exception {
	
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Integer>  custIdList = new ArrayList<Integer>();
		try {
			con = DatabaseUtil.getConForHgj();

			String query = "select custId from  customer_applicant a, applicant b where a.appId=b.mainAppId and b.applicantName=? ";

			pstmt = (PreparedStatement) con.prepareStatement(query);
			pstmt.setString(1, appName);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				do {								
					Integer custId= rs.getInt(1);
					boolean hasSameOne=false;
					for(Integer cId:custIdList){
						if (cId.intValue()==custId.intValue()){
							hasSameOne=true;
							break;
						}
					}
					if (!hasSameOne){
						custIdList.add(custId);
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

		return custIdList;
	}
	
	
	
	// 判断商标图片是否为空
	public boolean tmImageIsNull(String regNumber) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DatabaseUtil.getConForHgj();

			String query = "select tmId from  trademark where regNumber=? and tmImage is null";

			pstmt = (PreparedStatement) con.prepareStatement(query);

			pstmt.setString(1, regNumber);

			rs = pstmt.executeQuery();

			if (rs == null || !rs.next()) {
				return false;
			} else {
				return true;
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

	}
	
	
	// 判断商标图片的存储路径是否为空
	public boolean imgFilePathIsNull(String regNumber) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DatabaseUtil.getConForHgj();

			String query = "select tmId from  trademark where regNumber=? and imgFilePath is null";

			pstmt = (PreparedStatement) con.prepareStatement(query);

			pstmt.setString(1, regNumber);

			rs = pstmt.executeQuery();

			if (rs == null || !rs.next()) {
				return false;
			} else {
				return true;
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

	}
	
	

	// 获取商标注册号和商标的Id
	public List<Map<String, Integer>> getTradeMarkInfo(String appName)
			throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Map<String, Integer>> regNumberList = new ArrayList<Map<String, Integer>>();
		try {
			con = DatabaseUtil.getConForHgj();

			String query = "select regNumber, tmId from  trademark where applicantName=?";

			pstmt = (PreparedStatement) con.prepareStatement(query);

			pstmt.setString(1, appName);

			rs = pstmt.executeQuery();

			if (rs == null) {
				logger.info("数据库内没有"+ appName+"的商标");
			}
			if (rs.next()) {
				do {

					Map<String, Integer> map = new HashMap<String, Integer>();
					// 获取regNumber
					String regNumber = rs.getString(1);
					Integer id = rs.getInt(2);
					if (regNumber != null && !regNumber.equals("")) {
						map.put(regNumber, id);
						regNumberList.add(map);
					}

				} while (rs.next());
			} else {
				logger.info("数据库内没有"+ appName+"的商标");

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

		return regNumberList;
	}

	// 判断潜在客户是否已经更新过一次商标数据
	public boolean isUpdated(String appNameList) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean isUpdate = false;

		try {
			int count=0;
			con = DatabaseUtil.getConForHgj();

			String query = "select  count(*) as count from  trademark  "
					+ "where  applicantName in ("+ appNameList+")";

			pstmt = (PreparedStatement) con.prepareStatement(query);
			
			rs = pstmt.executeQuery();
			
			if (rs!=null && rs.next()) {			
				count=rs.getInt(1);
			}
			
			rs.close();			
			pstmt.close();
			
			//如果数据库已经有该申请人的数据，那么看这些数据是否都被更新过
			if (count>0){
				
				int count2=0;
				
				query = "select  count(*) as count from  trademark  "
						+ "where  modifyDate is null and applicantName in ("+ appNameList+")";

				pstmt = (PreparedStatement) con.prepareStatement(query);
				
				rs = pstmt.executeQuery();

				if (rs!=null && rs.next()) {			
					count2=rs.getInt(1);
				}
				
				//如果更新日期为空的数量为0，那么说明，所以商标都被更新过
				if (count2==0){
					isUpdate = true;
				} 
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

		return isUpdate;
	}
	
	
	
	// 判断潜在客户是否已经更新过一次商标数据
	public List<String> selectApplicant(String searchCondition) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	
		List<String> applicantNameList= new ArrayList<String>();
		try {
			con = DatabaseUtil.getConForHgj();

			String query = "select applicantName "
					+ "from applicant where mainAppId in "
					+ "("
					+ "select id from applicant where applicantName in ("+ searchCondition +") "
					+ ")";

			pstmt = (PreparedStatement) con.prepareStatement(query);
			
			rs = pstmt.executeQuery();

			if (rs == null || !rs.next()) {
				logger.info("没有符合条件的申请人数据");			
			} else {
				do {					
					String appName = rs.getString(1);					
					if(appName!=null && !appName.equals("")){
						if (!applicantNameList.contains(appName)){
							applicantNameList.add(appName);
						}
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

		return applicantNameList;
	}
	
	

	// 获取商标注册号和商标的Id
	public List<String> getRegNumber(String appName) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<String> regNumberList = new ArrayList<String>();
		try {
			con = DatabaseUtil.getConForHgj();

			String query = "select distinct(regNumber) from  trademark where applicantName=?";

			pstmt = (PreparedStatement) con.prepareStatement(query);

			pstmt.setString(1, appName);

			rs = pstmt.executeQuery();

			if (rs == null) {
				logger.info("trademark表内没有" + appName + "的商标数据");
			}
			if (rs.next()) {
				do {

					// 获取regNumber
					String regNumber = rs.getString(1);
					if (regNumber != null && !regNumber.equals("")) {
						regNumberList.add(regNumber);
					}

				} while (rs.next());
			} else {
				logger.info("trademark表内没有" + appName + "的商标数据");

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

		return regNumberList;
	}

	// 获取某个申请人的商标数据
	public List<TradeMark> getTradeMarkList(String appName) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<TradeMark> list = new ArrayList<TradeMark>();
		try {
			con = DatabaseUtil.getConForHgj();

			String query = "select tmId, regNumber, tmType, approvalNumber, regnoticeNumber, validStartDate, validEndDate, applicantName, applicantAddress, agent, appDate, tmCategory, imgFileUrl, status, tmGroup from  trademark where 1=1";

			if (appName != null && !appName.equals("")) {
				query = query + " and applicantName=?";
			}

			pstmt = (PreparedStatement) con.prepareStatement(query);

			if (appName != null && !appName.equals("")) {
				pstmt.setString(1, appName);
			}

			rs = pstmt.executeQuery();

			if (rs == null) {
				logger.info("trademark表内没有" + appName + "的商标数据");
			}
			if (rs.next()) {
				do {

					TradeMark tm = new TradeMark();
					// 获取regNumber
					int tmId = rs.getInt(1);
					String regnumber = rs.getString(2);
					String tmtype = rs.getString(3);
					String approvalnumber = rs.getString(4);
					String regnoticenumber = rs.getString(5);
					Date validstartdate = rs.getDate(6);
					Date validenddate = rs.getDate(7);
					String applicantname = rs.getString(8);
					String applicantaddress = rs.getString(9);
					String agent = rs.getString(10);
					Date appdate = rs.getDate(11);
					String tmcategory = rs.getString(12);
					String imgFileUrl = rs.getString(13);
					String status = rs.getString(14);
					String tmGroup = rs.getString(15);

					tm.setTmId(tmId);
					tm.setRegNumber(regnumber);
					tm.setTmType(tmtype);
					tm.setApprovalNumber(approvalnumber);
					tm.setRegnoticeNumber(regnoticenumber);
					tm.setValidStartDate(validstartdate);
					tm.setValidEndDate(validenddate);
					tm.setApplicantName(applicantname);
					tm.setApplicantAddress(applicantaddress);
					tm.setAgent(agent);
					tm.setAppDate(appdate);
					tm.setTmCategory(tmcategory);
					tm.setImgFileUrl(imgFileUrl);
					tm.setStatus(status);
					tm.setTmGroup(tmGroup);
					list.add(tm);

				} while (rs.next());
			} else {
				// logger.info("trademark表内没有"+appName+"的数据");

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

	// 获取商标注册号和商标的国际分类号
	public String getCorrectAppName(String appName) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DatabaseUtil.getConForHgj();

			String query = "select appName from  applicant_map where  errorName=?";

			pstmt = (PreparedStatement) con.prepareStatement(query);

			pstmt.setString(1, appName);

			rs = pstmt.executeQuery();

			if (rs == null || !rs.next()) {
				return null;
			}
			if (rs.next()) {
				String correctName = rs.getString(1);
				return correctName;
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

		return null;

	}

	// 获取某个客户的所有申请人的商标数据
	public List<TradeMark> getCustomerTmList() throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<TradeMark> list = new ArrayList<TradeMark>();
		try {
			con = DatabaseUtil.getConForHgj();

			String query = "select tmId, regNumber, tmType, approvalNumber, regnoticeNumber, validStartDate, validEndDate, applicantName, applicantAddress, agent, appDate, tmCategory, imgFileUrl, status from  trademark";

			pstmt = (PreparedStatement) con.prepareStatement(query);

			rs = pstmt.executeQuery();

			if (rs == null) {
				logger.info("数据库内没有客户数据");
			}
			if (rs.next()) {
				do {

					TradeMark tm = new TradeMark();
					// 获取regNumber
					int id = rs.getInt(1);
					String regnumber = rs.getString(2);
					String tmtype = rs.getString(3);
					String approvalnumber = rs.getString(4);
					String regnoticenumber = rs.getString(5);
					Date validstartdate = rs.getDate(6);
					Date validenddate = rs.getDate(7);
					String applicantname = rs.getString(8);
					String applicantaddress = rs.getString(9);
					String agent = rs.getString(10);
					Date appdate = rs.getDate(11);
					String tmcategory = rs.getString(12);
					String imgFileUrl = rs.getString(13);
					String status = rs.getString(14);

					tm.setTmId(id);

					tm.setRegNumber(regnumber);
					tm.setTmType(tmtype);
					tm.setApprovalNumber(approvalnumber);
					tm.setRegnoticeNumber(regnoticenumber);
					tm.setValidStartDate(validstartdate);
					tm.setValidEndDate(validenddate);
					tm.setApplicantName(applicantname);
					tm.setApplicantAddress(applicantaddress);
					tm.setAgent(agent);
					tm.setAppDate(appdate);
					tm.setTmCategory(tmcategory);
					tm.setImgFileUrl(imgFileUrl);
					tm.setStatus(status);
					list.add(tm);

				} while (rs.next());
			} else {
				logger.info("数据库内没有客户数据");

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

	// 获取商标的流程数据
	public List<TradeMarkProcess> getTradeMarkProcessList(Integer tmid)
			throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<TradeMarkProcess> list = new ArrayList<TradeMarkProcess>();
		try {
			con = DatabaseUtil.getConForHgj();

			String query = "select status, statusDate  from  trademark_process where tmId=?";

			pstmt = (PreparedStatement) con.prepareStatement(query);
			pstmt.setInt(1, tmid);

			rs = pstmt.executeQuery();

			if (rs == null) {
				logger.info("数据库内没有符合条件的商标的流程数据");
			}
			if (rs.next()) {
				do {

					TradeMarkProcess tmp = new TradeMarkProcess();
					String status = rs.getString(1);
					Date statusdate = rs.getDate(2);
					tmp.setTmId(tmid);
					tmp.setStatus(status);
					tmp.setStatusDate(statusdate);

					list.add(tmp);

				} while (rs.next());
			} else {
				logger.info("数据库内没有符合条件的商标的流程数据");

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
	
	
	
	// 获取商标的流程数据
	public List<TradeMarkCategory> getTmCategoryList(List<String> regNumberList)
			throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<TradeMarkCategory> list = new ArrayList<TradeMarkCategory>();
		String[] b = regNumberList.toArray(new String[regNumberList.size()]);
		String regNumbers=null;
		
		for(String regNum: regNumberList){
			if (regNumbers==null){
				regNumbers="'"+regNum+"'";
			}else{
				regNumbers=regNumbers+","+"'"+regNum+"'";
			}
		}
		
		try {
			con = DatabaseUtil.getConForHgj();

			String query = "select regNumber, tmGroup, name from  trademark_category where regNumber in ("+ regNumbers+")";

			pstmt = (PreparedStatement) con.prepareStatement(query);
		
			rs = pstmt.executeQuery();

			if (rs == null) {
				logger.info("数据库内没有符合条件的商标的流程数据");
			}
			if (rs.next()) {
				do {
					
					TradeMarkCategory tmc = new TradeMarkCategory();
					String regNumber = rs.getString(1);
					String tmgroup = rs.getString(2);
					String name = rs.getString(3);
					
					tmc.setRegNumber(regNumber);
					tmc.setTmGroup(tmgroup);
					tmc.setName(name);

					list.add(tmc);


				} while (rs.next());
			} else {
				logger.info("数据库内没有符合条件的商标的流程数据");

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
	
	
	
	// 获取商标的流程数据
		public List<TradeMarkProcess> getTmProcessList(List<String> regNumberList)
				throws Exception {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			List<TradeMarkProcess> list = new ArrayList<TradeMarkProcess>();
			String[] b = regNumberList.toArray(new String[regNumberList.size()]);
			String regNumbers=null;
			
			for(String regNum: regNumberList){
				if (regNumbers==null){
					regNumbers="'"+regNum+"'";
				}else{
					regNumbers=regNumbers+","+"'"+regNum+"'";
				}
			}
			
			try {
				con = DatabaseUtil.getConForHgj();
				

				String query = "select regNumber, status, statusDate  from  trademark_process where regNumber in ("+ regNumbers+")";

				pstmt = (PreparedStatement) con.prepareStatement(query);
				
				rs = pstmt.executeQuery();

				if (rs == null) {
					logger.info("数据库内没有符合条件的商标的流程数据");
				}
				if (rs.next()) {
					do {

						TradeMarkProcess tmp = new TradeMarkProcess();
						String regNumber = rs.getString(1);
						String status = rs.getString(2);
						Date statusdate = rs.getDate(3);
						
						tmp.setRegNumber(regNumber);
						tmp.setStatus(status);
						tmp.setStatusDate(statusdate);

						list.add(tmp);

					} while (rs.next());
				} else {
					logger.info("数据库内没有符合条件的商标的流程数据");

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
	
	

	// 获取数据库中是否有某个商标的公告数据
	public List<JsonSbGongGao> getTradeMarkGongGao(String regNumber)
			throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<JsonSbGongGao> gonggaos = new ArrayList<JsonSbGongGao>();
		try {
			con = DatabaseUtil.getConForHgj();

			String query = "select ggName, ggDate from  trademarkgonggao where  regNumber=? order by ggDate DESC";

			pstmt = (PreparedStatement) con.prepareStatement(query);

			pstmt.setString(1, regNumber);

			rs = pstmt.executeQuery();

			if (rs == null) {
				return null;
			}
			if (rs.next()) {
				do {

					String ggName = rs.getString(1);
					Date ggDate = rs.getDate(2);

					JsonSbGongGao jsonSbGongGao = new JsonSbGongGao();
					jsonSbGongGao.setGgName(ggName);
					jsonSbGongGao.setGgDate(ggDate);
					gonggaos.add(jsonSbGongGao);

				} while (rs.next());
			} else {
				return null;

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

		return gonggaos;
	}

	// 删除某个商标的商品和服务数据
	public void deleteTradeMarkCategory(String regNumber, Integer tmType) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DatabaseUtil.getConForHgj();

			String query = "delete from trademark_category where regNumber=? and tmType=?";
			pstmt = (PreparedStatement) con.prepareStatement(query);
			pstmt.setString(1, regNumber);
			pstmt.setInt(2, tmType);

			pstmt.executeUpdate();
		

		} catch (java.lang.ClassNotFoundException e) {
			throw e;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
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
	}

	// 删除某个商标的流程数据
	public void deleteTradeMarkProcess(String regNumber) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DatabaseUtil.getConForHgj();

			String query = "delete from trademark_process where regNumber=?";
			pstmt = (PreparedStatement) con.prepareStatement(query);
			pstmt.setString(1, regNumber);

			pstmt.executeUpdate();
		

		} catch (java.lang.ClassNotFoundException e) {
			throw e;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
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
	}

	// 粗粒度查找数据库中是否有某个商标的商品/服务信息
	public List<TradeMarkCategory> getTradeMarkCategory(String  regNumber, Integer tmType)
			throws Exception {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		List<TradeMarkCategory> list = new ArrayList<TradeMarkCategory>();

		try {
			con = DatabaseUtil.getConForHgj();

			String query = "select tmGroup, name from  trademark_category where regNumber=? and tmType=?";

			pstmt = (PreparedStatement) con.prepareStatement(query);
			pstmt.setString(1, regNumber);
			pstmt.setInt(2, tmType);
			rs = pstmt.executeQuery();

			if (rs == null) {
				// logger.info("数据库内没有符合条件的商品/服务信息");
			}
			if (rs.next()) {
				do {
					TradeMarkCategory tmc = new TradeMarkCategory();
					String tmgroup = rs.getString(1);
					String name = rs.getString(2);
					
					tmc.setTmGroup(tmgroup);
					tmc.setName(name);

					list.add(tmc);

				} while (rs.next());
			} else {
				// logger.info("数据库内没有符合条件的商品/服务信息");

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

	// 细粒度查找数据库中是否有某个商标的商品/服务信息，要求regNumber与tmgroup也必须相同
	public boolean getCategoryWithTmGroup(String regNumber, String tmGroup,
			String name) throws Exception {
		boolean result = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DatabaseUtil.getConForHgj();

			String query = "select id from  trademark_category where regNumber=? and tmGroup=? and name=? ";

			pstmt = (PreparedStatement) con.prepareStatement(query);
			pstmt.setString(1, regNumber);
			pstmt.setString(2, tmGroup);
			pstmt.setString(3, name);

			rs = pstmt.executeQuery();

			if (rs == null) {
				result = false;
			}
			if (rs.next()) {
				result = true;
			} else {
				result = false;

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

		return result;
	}

	// 获取客户商标数据更新的时间
	public Map<Integer, Date> getTradeMarkUpdateRecord(String appName,
			String opt) throws Exception {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		Map<Integer, Date> updateRecord = new HashMap<Integer, Date>();
		try {
			con = DatabaseUtil.getConForHgj();

			String query = "select id, optDate from  trademarkupdate where appName=? and opt=? order by optDate DESC";

			pstmt = (PreparedStatement) con.prepareStatement(query);
			pstmt.setString(1, appName);
			pstmt.setString(2, opt);

			rs = pstmt.executeQuery();

			if (rs == null) {
				return null;
			}
			if (rs.next()) {
				do {
					int id = rs.getInt(1);
					Date date = rs.getDate(2);
					updateRecord.put(id, date);
					break;

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

		return updateRecord;
	}

	// 获取客户商标数据更新的时间
	public String getTradeMarkUpdateStatus(String appName, String opt)
			throws Exception {
		String status = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DatabaseUtil.getConForHgj();

			String query = "select status from  trademarkupdate where appName=? and opt=? order by optDate DESC";

			pstmt = (PreparedStatement) con.prepareStatement(query);

			pstmt.setString(1, appName);
			pstmt.setString(2, opt);

			rs = pstmt.executeQuery();

			if (rs == null) {
				return null;
			}
			if (rs.next()) {
				do {
					status = rs.getString(1);
					break;

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

		return status;
	}
	
	
	
	

	// 获取某个商标数据更新的时间
	public Date getTmDetailUpdateDate(Integer tmId) throws Exception {
		Date date = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DatabaseUtil.getConForHgj();

			String query = "select modifyDate from  trademark where tmId=?";

			pstmt = (PreparedStatement) con.prepareStatement(query);
			pstmt.setInt(1, tmId);

			rs = pstmt.executeQuery();

			if (rs == null) {
				return null;
			}
			if (rs.next()) {
				do {
					date = rs.getDate(1);
					break;

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

		return date;
	}

	// 获取某个客户的商标数据更新的时间
	public Date geTmDataUpdateDate(Integer custId) throws Exception {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Date date = null;
		try {
			con = DatabaseUtil.getConForHgj();

			String query = "select optDate from  trademarkupdate where opt=tm order by optDate DESC";

			pstmt = (PreparedStatement) con.prepareStatement(query);
			pstmt.setInt(1, custId);

			rs = pstmt.executeQuery();

			if (rs == null) {
				return null;
			}
			if (rs.next()) {
				do {
					date = rs.getDate(1);
					break;

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

		return date;
	}

	// 获取某个客户的商标数据更新的数量
	public List<AppDataStatus> getApplicantTmStatus(String appName)
			throws Exception {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<AppDataStatus> appDataStatusList = new ArrayList<AppDataStatus>();

		try {
			con = DatabaseUtil.getConForHgj();
			// String query =
			// "select applicantname, count(id) as count from  trademark where custId=? and modifyDate=? group by applicantname ";

			String query = "select a.totalCount, b.updateCount, b.modifyDate "
					+ "from "
					+ "(select applicantName, count(tmId) as totalCount from trademark where 1=1 and applicantName=?  ) a  "
					+ "left join (select applicantName, count(tmId) as updateCount, max(modifyDate) as modifyDate from trademark where 1=1 and applicantName=? and modifyDate is not null )  b "
					+ "on a.applicantName=b.applicantName";

			pstmt = (PreparedStatement) con.prepareStatement(query);
			pstmt.setString(1, appName);
			pstmt.setString(2, appName);
			// String date = DateTool.getDate(modifyDate);
			// pstmt.setString(2, "" + date);

			rs = pstmt.executeQuery();

			if (rs == null) {
				return null;
			}
			if (rs.next()) {
				do {

					int totalCount = rs.getInt(1);
					int updateCount = rs.getInt(2);
					Date modifyDate = rs.getDate(3);

					if (totalCount > 0) {
						AppDataStatus appDataStatus = new AppDataStatus();
						appDataStatus.setAppName(appName);
						appDataStatus.setTotalCount(totalCount);
						appDataStatus.setUpdateCount(updateCount);
						appDataStatus.setModifyDate(modifyDate);

						// if (modifyDate==null){
						// Date date=new Date();
						// appDataStatus.setModifyDate(date);
						// System.out.println("modifyDate is null, set it with: "+
						// date );
						// }

						appDataStatusList.add(appDataStatus);
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

		return appDataStatusList;
	}

	// 获取某个客户商标总数
	public TmDataStatus getCustomerTmStatus(String appName) throws Exception {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		TmDataStatus tmDataStatus = new TmDataStatus();

		try {
			con = DatabaseUtil.getConForHgj();

			String query = "select  a.totalTmCount, b.updateTmCount "
					+ "from "
					+ "(select count(tmId) as totalTmCount from trademark where 1=1 and applicantName=?) a  "
					+ "left join (select applicantName, count(tmId) as updateTmCount from trademark where 1=1 and applicantName=? and modifyDate is not null)  b "
					+ "on a.applicantName=b.applicantName";

			pstmt = (PreparedStatement) con.prepareStatement(query);
			pstmt.setString(1, appName);
			pstmt.setString(2, appName);
			rs = pstmt.executeQuery();

			if (rs == null) {
				return tmDataStatus;
			}
			if (rs.next()) {
				do {
					int totalTmCount = rs.getInt(1);
					int updateTmCount = rs.getInt(2);

					tmDataStatus.setTotalTmCount(totalTmCount);
					tmDataStatus.setUpdateTmCount(updateTmCount);
					break;

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

		return tmDataStatus;
	}

	// 将商标公告数据插入tradmarkgonggao表
	public void insertTradeMarkGongGaoTable(String regNumber, List<JsonSbGongGao> ggList)
			throws Exception {

		Connection con = null;
		PreparedStatement pstmt = null;

		// Integer custId = jsonTmGongGaoInfo.getCustId();
//		String regNumber = jsonTmGongGaoInfo.getRegNumber();
//
//		List<JsonSbGongGao> gonggaos = jsonTmGongGaoInfo.getGonggaos();
		try {
			con = DatabaseUtil.getConForHgj();

			for (JsonSbGongGao gg : ggList) {

				String ggName = gg.getGgName();
				String ggQihao = gg.getGgQihao();
				String ggPage = gg.getGgPage();
				Date ggDate = gg.getGgDate();

				// 先删除相同的公告期的公告
//				String delSql = "delete from trademarkgonggao where 1=1 and  regNumber=? and ggQihao=?";
//				pstmt = (PreparedStatement) con.prepareStatement(delSql);
//
//				pstmt.setString(1, regNumber);
//				pstmt.setString(2, ggQihao);
//				pstmt.execute();
//				pstmt.close();

				// 插入当前公告
				String query = "insert into trademarkgonggao (regNumber, ggName, ggDate, ggQihao, ggPage) values (?, ?, ?, ?, ?)";
				pstmt = (PreparedStatement) con.prepareStatement(query);
				// pstmt.setInt(1, custId);
				pstmt.setString(1, regNumber);
				pstmt.setString(2, ggName);
				if (ggDate == null) {
					pstmt.setString(3, null);
				} else {
					String date = DateTool.getDateTime(ggDate);
					pstmt.setString(3, "" + date);
				}
				pstmt.setString(4, ggQihao);
				pstmt.setString(5, ggPage);
				pstmt.execute();
				pstmt.close();
			}

		} catch (java.lang.ClassNotFoundException e) {
			throw e;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
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

	}

	// 将商标流程数据插入trademarkprocess表
	public void insertTradeMarkProcessTable(List<TradeMarkProcess> tmPs)
			throws Exception {

		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DatabaseUtil.getConForHgj();

			for (TradeMarkProcess tmp : tmPs) {

				int tmid = tmp.getTmId();
				String regNumber=tmp.getRegNumber();
				String status = tmp.getStatus();
				Date statusdate = tmp.getStatusDate();

				String query = "insert into trademark_process (" + "tmId, regNumber, "
						+ "status, " + "statusDate )" + "values (?, ?, ?, ?)";
				pstmt = (PreparedStatement) con.prepareStatement(query);
				pstmt.setInt(1, tmid);
				pstmt.setString(2, regNumber);
				pstmt.setString(3, status);
				if (statusdate == null) {
					pstmt.setString(4, null);
				} else {
					String date = DateTool.getDateTime(statusdate);
					pstmt.setString(4, "" + date);
				}
				pstmt.execute();
				pstmt.close();
			}

		} catch (java.lang.ClassNotFoundException e) {
			throw e;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
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

	}

	// 将官网商标数据插入trademark表
	public void insertTradeMarkTable(TradeMark tm) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DatabaseUtil.getConForHgj();

			String regnumber = tm.getRegNumber();
			String tmtype = tm.getTmType();
			String tmname = tm.getTmName();
			String applicantname = tm.getApplicantName();
			String status = tm.getStatus();
			Date appdate = tm.getAppDate();
			String approvalnumber = tm.getApprovalNumber();
			Date approvaldate = tm.getApprovalDate();
			String regnoticenumber = tm.getRegnoticeNumber();
			Date regnoticedate = tm.getRegNoticeDate();

			String agent = tm.getAgent();

			String imgFileUrl = tm.getImgFileUrl();

			// String country = tm.getCountry();
			// String tujing = tm.getTujing();

			String query = "insert into trademark" + "(" + "regNumber, "
					+ "tmType, " + "tmName, " + "applicantName, " + "status, "
					+ "appDate, " + "approvalNumber, " + "approvalDate, "
					+ "regnoticeNumber, " + "regNoticeDate, " + "agent, "
					+ "imgFileUrl ) "
					+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = (PreparedStatement) con.prepareStatement(query);

			pstmt.setString(1, regnumber);
			pstmt.setString(2, tmtype);
			pstmt.setString(3, tmname);
			pstmt.setString(4, applicantname);
			pstmt.setString(5, status);

			if (appdate == null) {
				pstmt.setString(6, null);
			} else {
				String date = DateTool.getDateTime(appdate);
				pstmt.setString(6, date);
			}

			pstmt.setString(7, approvalnumber);

			if (approvaldate == null) {
				pstmt.setString(8, null);
			} else {
				String date = DateTool.getDateTime(approvaldate);
				pstmt.setString(8, date);
			}

			pstmt.setString(9, regnoticenumber);

			if (regnoticedate == null) {
				pstmt.setString(10, null);
			} else {
				String date = DateTool.getDateTime(regnoticedate);
				pstmt.setString(10, "" + date);
			}

			pstmt.setString(11, agent);
			pstmt.setString(12, imgFileUrl);

			pstmt.execute();
			

		} catch (java.lang.ClassNotFoundException e) {
			throw e;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
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
	}

	// 将商品和服务数据插入TradeMarkCategory表
	public void insertTradeMarkCategoryTable(
			List<TradeMarkCategory> tradeMarkCategoryList) throws Exception {

		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DatabaseUtil.getConForHgj();

			for (TradeMarkCategory tmc : tradeMarkCategoryList) {
			    Integer tmId=tmc.getTmId();

				String regNumber = tmc.getRegNumber();

				String name = tmc.getName();
				int No = tmc.getNo();
				String tmGroup = tmc.getTmGroup();
				Integer tmType = tmc.getTmType();

				String query = "insert into trademark_category (name, no, tmGroup, tmType, regNumber, tmId) "
						+ "values (?, ?, ?, ?, ?, ?)";
				pstmt = (PreparedStatement) con.prepareStatement(query);
				if (name != null) {
					int len = name.length();
					if (len > 400) {
						logger.info(" Data too long for column name:" + len);
						name = name.substring(0, 400);
					}
				}
				pstmt.setString(1, name);
				pstmt.setInt(2, No);
				pstmt.setString(3, tmGroup);
				pstmt.setInt(4, tmType);
				pstmt.setString(5, regNumber);
				pstmt.setInt(6, tmId);
				
				pstmt.execute();
				pstmt.close();
			}

		} catch (java.lang.ClassNotFoundException e) {
			throw e;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
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

	}

	// 更新trademark表中的官网商标图片
	public void updateTradeMarkImage(Integer id, String tmimage, String imgFilePath)
			throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DatabaseUtil.getConForHgj();

			String query = "update  trademark set tmImage=?, imgFilePath=?, modifyDate=? where tmId=? ";

			pstmt = (PreparedStatement) con.prepareStatement(query);
			pstmt.setString(1, tmimage);
			pstmt.setString(2, imgFilePath);
			// 设置修改时间
			Date now = new Date();
			String modifyDate = DateTool.getDate(now);
			pstmt.setString(3, "" + modifyDate);
			pstmt.setInt(4, id);

			pstmt.executeUpdate();
			

		} catch (java.lang.ClassNotFoundException e) {
			throw e;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
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
	}

	// 更新trademark表中的官网商标状态
	public void updateTradeMarkStatus(Integer id, String status,
			String imgFileUrl) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DatabaseUtil.getConForHgj();

			String query = "update  trademark set status=?, imgFileUrl=? where tmId=?";

			pstmt = (PreparedStatement) con.prepareStatement(query);
			pstmt.setString(1, status);
			pstmt.setString(2, imgFileUrl);
			pstmt.setInt(3, id);
			pstmt.executeUpdate();

		} catch (java.lang.ClassNotFoundException e) {
			throw e;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
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
	}
	
	

	// 更新trademark表中的官网商标图片
	public void updateTMModifyData(List<String> regNumberList) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		String regNumbers=null;
		
		for(String regNum: regNumberList){
			if (regNumbers==null){
				regNumbers="'"+regNum+"'";
			}else{
				regNumbers=regNumbers+","+"'"+regNum+"'";
			}
		}
		
		try {
			con = DatabaseUtil.getConForHgj();

			String query = "update  trademark set  modifyDate=? where  regNumber in ("+ regNumbers+")";

			pstmt = (PreparedStatement) con.prepareStatement(query);

			// 设置修改时间
			Date now = new Date();
			String modifyDate = DateTool.getDate(now);
			pstmt.setString(1, "" + modifyDate);
			

			pstmt.executeUpdate();

		} catch (java.lang.ClassNotFoundException e) {
			throw e;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
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
	}
	
	
	// 更新trademark表中的官网商标图片
		public void updateTradeMarkModifyData(Integer id) throws Exception {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				con = DatabaseUtil.getConForHgj();

				String query = "update  trademark set  modifyDate=? where tmId=?";

				pstmt = (PreparedStatement) con.prepareStatement(query);

				// 设置修改时间
				Date now = new Date();
				String modifyDate = DateTool.getDate(now);
				pstmt.setString(1, "" + modifyDate);
				pstmt.setInt(2, id);

				pstmt.executeUpdate();

			} catch (java.lang.ClassNotFoundException e) {
				throw e;
			} catch (SQLException ex) {
				throw ex;
			} catch (Exception e) {
				throw e;
			} finally {
				try {
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
		}
		
		

	// 更新trademark表中的官网商标数据
	public void updateTradeMarkTable(TradeMark tm, boolean updateImage)
			throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DatabaseUtil.getConForHgj();

			String query = "update trademark "
					+ "set applicantName=?, applicantAddress=?, applicantEnName=?, applicantEnAddress=?, gtApplicantName=?, "
					+ "status=?, appDate=?, approvalNumber=?, approvalDate=?, "
					+ "regnoticeNumber=?, regNoticeDate=?, "
					+ "validStartDate=?, validEndDate=?, tmCategory=?, tmGroup=?, agent=?, imgFileUrl=?, tmStatus=?, priorDate=?, hqzdDate=?, gjRegDate=? "
					+ "where tmId=? ";

			if (updateImage) {
				query = "update trademark "
						+ "set applicantName=?, applicantAddress=?, applicantEnName=?, applicantEnAddress=?, gtApplicantName=?, "
						+ "status=?, appDate=?, approvalNumber=?, approvalDate=?, "
						+ "regnoticeNumber=?, regNoticeDate=?, "
						+ "validStartDate=?, validEndDate=?, tmCategory=?, tmGroup=?, agent=?, imgFileUrl=?, tmStatus=?, priorDate=?, hqzdDate=?, gjRegDate=?, imgFilePath=?, tmimage=? "
						+ "where tmId=? ";
			}

			pstmt = (PreparedStatement) con.prepareStatement(query);

			int id = tm.getTmId();

			String applicantname = tm.getApplicantName();
			String applicantaddress = tm.getApplicantAddress();
			String applicantenname = tm.getApplicantEnName();
			String applicantenaddress = tm.getApplicantEnAddress();
			String gtapplicantname = tm.getGtApplicantName();

			String status = tm.getStatus();
			Date appdate = tm.getAppDate();
			String approvalnumber = tm.getApprovalNumber();
			Date approvaldate = tm.getApprovalDate();
			String regnoticenumber = tm.getRegnoticeNumber();
			Date regnoticedate = tm.getRegNoticeDate();
			Date validstartdate = tm.getValidStartDate();
			Date validenddate = tm.getValidEndDate();
			String tmcategory = tm.getTmCategory();
			String tmGroup = tm.getTmGroup();
			String agent = tm.getAgent();
			String tmimage = tm.getTmImage();
			String imgFileUrl = tm.getImgFileUrl();
			String imgFilePath= tm.getImgFilePath();
			String tmStatus = tm.getTmStatus();
			
			String yxqrq=tm.getYxqrq();
			Date gjzcrq=tm.getGjzcrq();
			Date hqzdrq=tm.getHqzdrq();

			pstmt.setString(1, applicantname);
			pstmt.setString(2, applicantaddress);
			pstmt.setString(3, applicantenname);
			pstmt.setString(4, applicantenaddress);
			pstmt.setString(5, gtapplicantname);
			pstmt.setString(6, status);

			if (appdate == null) {
				pstmt.setString(7, null);
			} else {
				String date = DateTool.getDateTime(appdate);
				pstmt.setString(7, date);
			}

			pstmt.setString(8, approvalnumber);

			if (approvaldate == null) {
				pstmt.setString(9, null);
			} else {
				String date = DateTool.getDateTime(approvaldate);
				pstmt.setString(9, date);
			}

			pstmt.setString(10, regnoticenumber);

			if (regnoticedate == null) {
				pstmt.setString(11, null);
			} else {
				String date = DateTool.getDateTime(regnoticedate);
				pstmt.setString(11, "" + date);
			}

			if (validstartdate == null) {
				pstmt.setString(12, null);
			} else {
				String date = DateTool.getDateTime(validstartdate);
				pstmt.setString(12, "" + date);
			}

			if (validenddate == null) {
				pstmt.setString(13, null);
			} else {
				String date = DateTool.getDateTime(validenddate);
				pstmt.setString(13, "" + date);
			}
			if (tmcategory != null) {
				int len = tmcategory.length();
				if (len > 2500) {
					logger.info("Data too long for  tmcategory: " + len);
					tmcategory = tmcategory.substring(0, 2500);
				}
			}
			pstmt.setString(14, tmcategory);
			pstmt.setString(15, tmGroup);
			pstmt.setString(16, agent);

			pstmt.setString(17, imgFileUrl);
			pstmt.setString(18, tmStatus);
			
			pstmt.setString(19, yxqrq);
			
			if (hqzdrq == null) {
				pstmt.setString(20, null);
			} else {
				String date = DateTool.getDateTime(hqzdrq);
				pstmt.setString(20, date);
			}			
			
			if (gjzcrq == null) {
				pstmt.setString(21, null);
			} else {
				String date = DateTool.getDateTime(gjzcrq);
				pstmt.setString(21, date);
			}
			
			

			// 设置修改时间
//			Date now = new Date();
//			String modifyDate = DateTool.getDate(now);
//			pstmt.setString(18, "" + modifyDate);
						
			if (!updateImage) {
				pstmt.setInt(22, id);
			} else {
				pstmt.setString(22, imgFilePath);
				pstmt.setString(23, tmimage);
				pstmt.setInt(24, id);
			}

			pstmt.executeUpdate();

		} catch (java.lang.ClassNotFoundException e) {
			throw e;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
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
	}

	// 更新trademark表中的商标图片数据
	public void updateTmImage(Integer id, String tmImage) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DatabaseUtil.getConForHgj();

			String query = "update trademark set tmImage=? where tmId=?";

			pstmt = (PreparedStatement) con.prepareStatement(query);

			pstmt.setString(1, tmImage);
			pstmt.setInt(2, id);

			pstmt.executeUpdate();
		

		} catch (java.lang.ClassNotFoundException e) {
			throw e;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
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
	}

	// 将对客户的数据更新操作记录插入表中
	public void insertTradeMarkUpdateTable(String appName, String opt)
			throws Exception {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DatabaseUtil.getConForHgj();

			// 先删除记录
			String sql = "delete from trademarkupdate where appName=? and opt=?";
			pstmt = (PreparedStatement) con.prepareStatement(sql);
			pstmt.setString(1, appName);
			pstmt.setString(2, opt);
			pstmt.execute();
			pstmt.close();

			// 再插入记录
			String query = "insert into trademarkupdate (appName, opt, optDate) values (?, ?, ?)";
			pstmt = (PreparedStatement) con.prepareStatement(query);
			pstmt.setString(1, appName);
			pstmt.setString(2, opt);
			Date optDate = new Date();
			String date = DateTool.getDateTime(optDate);
			pstmt.setString(3, "" + date);
			pstmt.execute();

		} catch (java.lang.ClassNotFoundException e) {
			throw e;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
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

	}
	
	

	// 更新trademarkupdate表中的状态数据
	public void updateTmUpdateRecord(String appName, String status, String opt)
			throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		boolean insertData=false;
		try {
			
			Date optDate = new Date();
			String date = DateTool.getDateTime(optDate);
			
			
			con = DatabaseUtil.getConForHgj();
			
			String sql = "select id, optDate from  trademarkupdate where appName=? and opt=? order by optDate DESC";

			pstmt = (PreparedStatement) con.prepareStatement(sql);
			pstmt.setString(1, appName);
			pstmt.setString(2, opt);

			rs = pstmt.executeQuery();

			if (rs == null || !rs.next()) {
				insertData=true;
			}
			rs.close();
			pstmt.close();
			
			if (insertData){
				sql = "insert into trademarkupdate (appName, opt, optDate, status) values (?, ?, ?, ?)";
				pstmt = (PreparedStatement) con.prepareStatement(sql);				
				pstmt.setString(1, appName);
				pstmt.setString(2, opt);				
				pstmt.setString(3, "" + date);
				pstmt.setString(4, status);		
				pstmt.execute();	
			}else{				
				sql = "update  trademarkupdate set status=?, optDate=? where appName=? and opt=? ";
				pstmt = (PreparedStatement) con.prepareStatement(sql);
				pstmt.setString(1, status);					
				pstmt.setString(2, "" + date);
				pstmt.setString(3, appName);	
				pstmt.setString(4, opt);
				pstmt.executeUpdate();	
			}
			

	
		} catch (java.lang.ClassNotFoundException e) {
			throw e;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
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
	}

	// 将新的申请人信息插入到数据库。
	public static void insertApplicantTable(List<String> appNames)
			throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DatabaseUtil.getConForHgj();

			Date date = new Date();
			String modifyTime = DateTool.getDateTime(date);

			Integer appType = 1;

			for (String appName : appNames) {

				String query = "insert into applicant (appName) "
						+ "values (?)";

				pstmt = (PreparedStatement) con.prepareStatement(query);
				pstmt.setString(1, appName);

				pstmt.execute();
				pstmt.close();
				logger.info("insert appName:" + appName);

			}

		} catch (SQLException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
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
	}

	public static void main(String[] args) {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
