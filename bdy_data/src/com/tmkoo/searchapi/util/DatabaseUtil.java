package com.tmkoo.searchapi.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//import java.util.Properties;




import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Component;


@Component
public class DatabaseUtil {
	
	
	
//	private static Properties props = null;
//	
	private static boolean initFlag = false;
	
	private static String dataBaseIp = "localhost";
	
	private static javax.sql.DataSource dataSource = null;
	
	private static ApplicationContext applicationContext = null;
	
//	private static String dataBaseIp="192.168.0.8";
	
	
	
	public static String getDataBaseIp() {
		return dataBaseIp;
	}


	public static void setDataBaseIp(String dataBaseIp) {
		DatabaseUtil.dataBaseIp = dataBaseIp;
	}


	public static void init(String ipAddress) {
		
		if (!initFlag){			
			if (ipAddress!=null && !ipAddress.equals("")){
				dataBaseIp=ipAddress;
			}			
								
			initFlag=true;
		}
		
//		if (!initFlag){
//			// 第一次加载的properties
//			props = FileUtil.readProperties("application.properties");
//			if (props != null) {
//				String value = props.getProperty("dataBaseIp");
//				if (value!=null && !value.equals("")){
//					dataBaseIp=value;
//				}
//			}
//	
//			initFlag = true;
//		}
	}

	
	public static boolean isInitFlag() {
		return initFlag;
	}


	public static void setInitFlag(boolean initFlag) {
		DatabaseUtil.initFlag = initFlag;
	}
	
	
	public static void setDataSource() {
        try{
			String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	        System.out.println("path = " + path);
	        String filepath = path + "/applicationContext.xml";
	
	        applicationContext = new FileSystemXmlApplicationContext(filepath);
			dataSource = (javax.sql.DataSource)applicationContext.getBean("gsDataSource");
        }catch (Exception e){
        	e.printStackTrace();
        }
	}


	public static Connection getConForHgj() throws SQLException,
			java.lang.ClassNotFoundException {
		
		if (!initFlag){	
			setDataSource();
			initFlag=true;
		}
		
		Connection con = null;
		
		if (dataSource!=null){
			con = dataSource.getConnection();
		}else{
			
			String driverName = "com.mysql.jdbc.Driver";
			// String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

			// 第一步：加载MySQL的JDBC的驱动
			Class.forName(driverName);

			// 取得连接的url,能访问MySQL数据库的用户名,密码；test：数据库名		
//			String url = "jdbc:mysql://"+ dataBaseIp +":3306/hgjdb";
			String url = "jdbc:mysql://"+ dataBaseIp +":3306/bdy_db";
			String username = "bdyuser";
			String password = "123456";

			// String url = "jdbc:sqlserver://192.168.1.107:1433;DatabaseName=ipms";
			// String username = "sa";
			// String password = "dwq@2016";

			// 第二步：创建与MySQL数据库的连接类的实例
			con = DriverManager.getConnection(url, username, password);
			
		}
		
		
		return con;
	}
	
	

	public static Connection getConForGsDb() throws SQLException,
			java.lang.ClassNotFoundException {

		String driverName = "com.mysql.jdbc.Driver";
		// String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

		// 第一步：加载MySQL的JDBC的驱动
		Class.forName(driverName);		
		
		// 取得连接的url,能访问MySQL数据库的用户名,密码；test：数据库名		
		String url = "jdbc:mysql://"+ dataBaseIp +":3306/gs_db";
		String username = "hgjuser";
		String password = "123456";

		// String url = "jdbc:sqlserver://192.168.1.107:1433;DatabaseName=ipms";
		// String username = "sa";
		// String password = "dwq@2016";

		// 第二步：创建与MySQL数据库的连接类的实例
		Connection con = DriverManager.getConnection(url, username, password);
		return con;
	}

	public static void main(String[] args) {

		try {

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
