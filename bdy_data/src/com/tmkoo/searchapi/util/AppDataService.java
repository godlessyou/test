package com.tmkoo.searchapi.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.tmkoo.searchapi.vo.Applicant;
import com.tmkoo.searchapi.vo.TradeMarkProcess;

public class AppDataService {

	public static Logger logger = Logger.getLogger(AppDataService.class);

	// 获取申请人名称中包含[的数据，例如：上海范雅家居有限公司[2014.08.14 第1419期转]
	public static List<String> getAppNames(String tableName, String columnName,
			String keyWord, boolean endFlag) throws Exception {

		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;
		List<String> applicants = new ArrayList<String>();

		try {

			con = DatabaseUtil.getConForHgj();
			statement = con.createStatement();

			String selectSql = "SELECT distinct(" + columnName + ") FROM "
					+ tableName + "  where " + columnName + " like '%"
					+ keyWord + "%'";

			rs = statement.executeQuery(selectSql);
			if (rs == null) {
				logger.info("数据库内没有符合条件的数据");
			}
			if (rs.next()) {
				do {
					String appName = rs.getString(1);
					String result=null;
					if (appName != null) {
						if (endFlag){
							boolean hasFlag=appName.endsWith(keyWord);
							if (hasFlag){
								result=appName.substring(0,appName.length()-1);
							}
							
						}else{
							int pos = appName.indexOf(keyWord);
							if (pos > -1) {
								result = appName.substring(0, pos);
							}							
						}
						if (result!=null && !applicants.contains(result)) {
							applicants.add(result);
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

		return applicants;
	}

	// 对申请人名称进行处理，去掉[之后的字符串，例如：上海范雅家居有限公司[2014.08.14 第1419期转] 处理后变为 上海范雅家居有限公司
	public static void updateTable(List<String> appNameList, String tableName,
			String columnName, String keyWord) throws Exception {

		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DatabaseUtil.getConForHgj();

			String query = "update " + tableName + " set " + columnName
					+ "=? where " + columnName + " like '%" + keyWord
					+ "%' and " + columnName + " like ?";

			for (String appName : appNameList) {
				pstmt = (PreparedStatement) con.prepareStatement(query);
				String likeStr = "%" + appName + "%";
				pstmt.setString(1, appName);
				pstmt.setString(2, likeStr);
				pstmt.executeUpdate();
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

	// 获取申请人名称中包含,的数据，例如：云南三叶国际航空服务有限公司,空旅行社有限公司
	// 并将逗号分隔的申请人数据进行处理，处理后形成多个申请人
	public static List<Applicant> getApplicantData(String keyWord)
			throws Exception {

		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;
		List<Applicant> applicants = new ArrayList<Applicant>();

		try {

			con = DatabaseUtil.getConForHgj();
			statement = con.createStatement();

			String selectSql = "SELECT appName, address FROM applicant1  where appName like '%"
					+ keyWord + "%'";

			rs = statement.executeQuery(selectSql);
			if (rs == null) {
				logger.info("数据库内没有符合条件的数据");
			}
			if (rs.next()) {
				do {
					String appName = rs.getString(1);
					String address = rs.getString(2);
					if (appName != null) {
						int pos = appName.indexOf(",");
						if (pos > -1) {
							// String tempName = appName.substring(0, pos);
							// int len=tempName.length();
							// 判断规则：如果申请人名称长度小于4，认为是个人
							// 忽略个人，只保留公司。
							// if (len<4){
							// continue;
							// }
							StringTokenizer idtok = new StringTokenizer(
									appName, ",");
							while (idtok.hasMoreTokens()) {
								String value = idtok.nextToken();
								String name = value.trim();
								boolean sameOne = false;
								for (Applicant app : applicants) {
									String name2 = app.getApplicantName();
									if (name2.equals(name)) {
										sameOne = true;
										break;
									}
								}
								if (!sameOne) {
									Applicant applicant = new Applicant();
									applicant.setApplicantName(appName);
//									applicant.setAddress(address);
									applicants.add(applicant);
								}
							}
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

		return applicants;
	}

	// 插入申请人数据
	public static void insertApplicantData(List<Applicant> applicantList)
			throws Exception {

		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DatabaseUtil.getConForHgj();

			String query = "insert into applicant1 (appName, address) values (?,?)";

			for (Applicant applicant : applicantList) {
				pstmt = (PreparedStatement) con.prepareStatement(query);
				String appName = applicant.getApplicantName();
//				String address = applicant.getAddress();
				pstmt.setString(1, appName);
//				pstmt.setString(2, address);
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

	// 删除申请人名称中包含,的数据，例如：云南三叶国际航空服务有限公司,空旅行社有限公司
	public static void deleteApplicantData(String keyWord) throws Exception {

		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;

		try {

			con = DatabaseUtil.getConForHgj();
			statement = con.createStatement();

			String sql = "delete FROM applicant1  where appName like '%"
					+ keyWord + "%'";

			statement.execute(sql);

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

	}

	public static void processYunNanTableData() {
		try {
			String tableName = "yunnan";
			String columnName = "fAddr";
			String keyWord = "[";
			boolean endFlag=false;
			// 获取申请人名称中包含[的数据，例如：上海范雅家居有限公司[2014.08.14 第1419期转]
			List<String> applicants = getAppNames(tableName, columnName,
					keyWord, endFlag);
			// 对申请人名称进行处理，去掉[之后的字符串，例如：上海范雅家居有限公司[2014.08.14 第1419期转] 处理后变为
			// 上海范雅家居有限公司
			updateTable(applicants, tableName, columnName, keyWord);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void processApplicantTableData() {
		try {
			String tableName = "applicant1";
			String columnName = "appName";
			boolean endFlag=true;
			
			String[] keyWords = new String[2];
			keyWords[0] = "()";
//			keyWords[1] = "（）";
			keyWords[1] = "（";
			
			int len=keyWords.length;

			for (int i = 0; i < len; i++) {
				if (i == 0) {
					continue;
				}

				String keyWord = keyWords[i];
			
				// 获取申请人名称中包含()，以及（）的数据，例如：胡茜()()()() 曹富娟（）
				List<String> applicants = getAppNames(tableName, columnName,
						keyWord, endFlag);
				// 对申请人名称进行处理，去掉()之后的字符串，例如：胡茜()()()()处理后变为 胡茜
				updateTable(applicants, tableName, columnName, keyWord);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void processDouHaoData() {
		try {

			// 获取申请人名称中包含,的数据，例如：云南三叶国际航空服务有限公司,空旅行社有限公司
			// 并将逗号分隔的申请人数据进行处理，处理后形成多个申请人

			String[] keyWords = new String[2];
			keyWords[0] = ",";
			keyWords[1] = "，";
			int len=keyWords.length;
			
			for (int i = 0; i < len; i++) {
				if (i == 0) {
					continue;
				}

				String keyWord = keyWords[i];

				List<Applicant> applicants = getApplicantData(keyWord);

				// 将处理后的数据插入数据库
				insertApplicantData(applicants);

				// 删除申请人名称中包含,的数据
				deleteApplicantData(keyWord);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		try {

			processApplicantTableData();

			// processDouHaoData();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
