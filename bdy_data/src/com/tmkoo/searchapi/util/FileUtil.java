package com.tmkoo.searchapi.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;



import org.apache.log4j.Logger;

import com.tmkoo.searchapi.common.Constants;



public class FileUtil {

	private static Logger logger = Logger.getLogger(FileUtil.class);

	// 临时excel文件被打开时形成的临时文件名是以~$开头
	private static String tempFileHead = "~$";

	// 将图片保存到本地目录
	public static void saveImageWithData(String imagePath, byte[] b)
			throws Exception {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(imagePath);
			out.write(b);
			out.close();
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 将图片保存到本地目录
	public static void saveImage(String imagePath, InputStream in)
			throws Exception {
		FileOutputStream out = null;
		try {

			out = new FileOutputStream(imagePath);
			byte[] b = new byte[1024];
			int len = 0;
			while ((len = in.read(b)) != -1) {
				out.write(b, 0, len);
				out.flush();
			}

		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Creates a folder to desired location if it not already exists
	 * 
	 * @param dirName
	 *            - full path to the folder
	 * @throws SecurityException
	 *             - in case you don't have permission to create the folder
	 */
	public static void createFolderIfNotExists(String dirName)
			throws SecurityException {
		File theDir = new File(dirName);
		mkDir(theDir);
	}

	public static void mkDir(File file) {
		if (file.getParentFile().exists()) {
			file.mkdir();
		} else {
			mkDir(file.getParentFile());
			file.mkdir();
		}
	}

	public static void deleteFile(String path) {
		File f = new File(path);
		if (f.exists()) {
			if (f.isFile()) {
				f.delete();
			} else {
				deleteDir(f);
			}
		}
	}

	public static boolean deleteDir(File dir) {

		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				File file = new File(dir, children[i]);
				if (file.isFile()) {
					file.delete();
				} else if (file.isDirectory()) {
					deleteDir(file);
				}
			}
		}

		// The directory is now empty so now it can be smoked
		return dir.delete();
	}


	public static void main(String[] args) {
		
		String usedFile = "C:\\tmimage\\bdy\\downloadimage\\伯尔鲁帝\\90888.jpg";
		
		usedFile=usedFile.replaceAll("\\\\", "/");
		
		int pos=usedFile.indexOf(Constants.image_dir)+Constants.image_dir.length();
		
		String imgFilePath=usedFile.substring(pos);
		System.out.println(imgFilePath);
//		System.setProperty("catalina.home", "C:/tomcat/apache-tomcat-7.0.65");
//		checkAndDelete();
		
		String sourceFile="c:/test/11.txt";
		String targetFile="c:/test/2016-02-25/11.txt";
		fileChannelCopy(sourceFile,targetFile);
	}

	public static Properties readProperties(String file) {

		Properties properties = new Properties();
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			if (loader == null) {
				loader = FileUtil.class.getClassLoader();
			}
			properties.load(loader.getResourceAsStream(file));

		} catch (Exception e) {
			StringBuilder sbInfo = new StringBuilder(
					"请将mailvm.properties放在下列目录：\n" + "Webapp/WEB-INF/classes\n"
							+ "Webapp/WEB-INF/lib\n");// 打印一些友好的tips

			logger.debug("没有找到mailvm.properties，将导致无法获得文件主题\n" + sbInfo);
		}

		return properties;

	}

	public ArrayList<String> getFileNames(String filePath) {
		ArrayList<String> fileNames = new ArrayList<String>();
		File file = new File(filePath);
		File[] listfile = file.listFiles();
		for (int i = 0; i < listfile.length; i++) {
			String fileName = listfile[i].getPath().toString();
			fileNames.add(fileName);
		}

		return fileNames;

	}

	public static void getDir(String filePath, List<String> FilePathList,
			int subdir_jibie) {
		File file = new File(filePath);
		File[] listfile = file.listFiles();
		if (listfile == null || listfile.length == 0)
			return;

		for (int i = 0; i < listfile.length; i++) {
			String subDirfilePath = listfile[i].toString();
			if (listfile[i].isDirectory()) {
				if (subdir_jibie > 0) {
					getDir(subDirfilePath, FilePathList, subdir_jibie - 1);
				} else {
					FilePathList.add(subDirfilePath);
				}
			}
		}
	}

	public static void getFile(String filePath, String[] extNames,
			List<String> FilePathList) {
		File file = new File(filePath);
		File[] listfile = file.listFiles();
		if (listfile == null || listfile.length == 0)
			return;

		for (int i = 0; i < listfile.length; i++) {
			String subDirfilePath = listfile[i].toString();

			if (!listfile[i].isDirectory()) {
				if (extNames == null || extNames.equals("")) {
					FilePathList.add(subDirfilePath);
				} else {
					for (String extName : extNames) {
						// excel文件被打开时形成的临时文件名是以~$开头，在查找时需要排除这些文件。
						if (subDirfilePath.startsWith(tempFileHead)) {
							continue;
						}
						// 查找指定扩展名的文件
						if (subDirfilePath.endsWith(extName)) {

							FilePathList.add(subDirfilePath);
							System.out.println("****** = " + subDirfilePath);
							break;
						}
					}
				}
			} else {
				getFile(subDirfilePath, extNames, FilePathList);
			}
		}
	}

	public static ArrayList<String> getSubDir(String filePath) {
		ArrayList<String> subDirs = new ArrayList<String>();
		File file = new File(filePath);
		File[] listfile = file.listFiles();
		for (int i = 0; i < listfile.length; i++) {
			if (listfile[i].isDirectory()) {
				subDirs.add(listfile[i].getAbsolutePath());
			}
		}

		return subDirs;

	}

	/**
	 * 使用文件通道的方式复制文件
	 * 
	 * @param s
	 *            源文件
	 * @param t
	 *            复制到的新文件
	 */
	public static void fileChannelCopy(String sourceFile, String targetFile) {
		
		File s = new File(sourceFile);
		if (s.exists()){
			File t = new File(targetFile);
			FileInputStream fi = null;
			FileOutputStream fo = null;
			FileChannel in = null;
			FileChannel out = null;
			try {
				fi = new FileInputStream(s);
				fo = new FileOutputStream(t);
				in = fi.getChannel();// 得到对应的文件通道
				out = fo.getChannel();// 得到对应的文件通道
				in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					
					in.close();
					fi.close();
					out.close();
					fo.close();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
