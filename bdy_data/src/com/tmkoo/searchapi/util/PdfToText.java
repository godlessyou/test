package com.tmkoo.searchapi.util;

import java.io.*;

/**
 * <p>
 * Title: pdf extraction
 * </p>
 * <p>
 * Description: email:chris@matrix.org.cn
 * </p>
 * <p>
 * Copyright: Matrix Copyright (c) 2003
 * </p>
 * <p>
 * Company: Matrix.org.cn
 * </p>
 * 
 * @author chris
 * @version 1.0,who use this example pls remain the declare
 */

public class PdfToText {
	
	
	public  void readPdf(String toolsPath, String filePath) throws Exception {
				
		String[] cmd = new String[] { toolsPath, "-enc", "UTF-8", "-q",
				filePath, "-" };
		Process p = Runtime.getRuntime().exec(cmd);
		BufferedInputStream bis = new BufferedInputStream(p.getInputStream());
		InputStreamReader reader = new InputStreamReader(bis, "UTF-8");
		StringWriter out = new StringWriter();
		char[] buf = new char[10000];
		int len;
		while ((len = reader.read(buf)) >= 0) {
			// out.write(buf, 0, len);
			System.out.println("the length is" + len);
		}
		reader.close();
		String ts = new String(buf);
		System.out.println("the str is" + ts);
	}
	
	public static void main(String args[]) throws Exception {
		PdfToText pdfReader=new PdfToText();
		
		String toolsPath="C:\\work\\trademarkmonitor\\xpdf\\pdftotext.exe";
		
		String filePath="C:\\work\\gonggao\\1558-1\\1558_商标初步审定公告_1_200.pdf";
		
		pdfReader.readPdf(toolsPath,filePath);
	}
}