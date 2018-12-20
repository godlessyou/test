package com.tmkoo.searchapi.util;

//import java.awt.AlphaComposite;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.Image;
import java.awt.Rectangle;
//import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.log4j.Logger;

//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGEncodeParam;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;
//import com.tmkoo.searchapi.common.Constants;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;




/**
 * 
 * 
 */
public class ImageUtils {
	
	//缩小后图片的长或者宽的最小像素	
	private static float basesize=100;
	
	private static Logger logger = Logger.getLogger(ImageUtils.class);

	
	/**
	 * 将网络图片进行Base64位编码
	 * 
	 * @param in
	 *			图片的二进制流
	 * @return
	 */
	public static byte[] getImageData(InputStream in) throws Exception {
		byte[] data = null;

		// 读取图片字节数组
		try {
	
			data = new byte[in.available()];
			in.read(data);			
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if (in!=null){
				in.close();
			}
		}
	
		return data;
	}
	
	/**
	 * 将网络图片进行Base64位编码
	 * 
	 * @param in
	 *			图片的二进制流
	 * @return
	 */
	public static byte[] getImagebytes(String base64) throws Exception {
		byte[] decoderBytes = null;		
		BASE64Decoder decoder = new BASE64Decoder();	
		try {		
			decoderBytes = decoder.decodeBuffer(base64);		
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return decoderBytes;
	}
	
	public static String encodeImage(byte[] data) throws Exception {
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}
	
	
	/**
	 * 将网络图片进行Base64位编码
	 * 
	 * @param imgUrl
	 *			图片的url路径，如http://.....xx.jpg
	 * @return
	 */
	public static String encodeImgageToBase64(URL imageUrl) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		ByteArrayOutputStream outputStream = null;
		try {
			BufferedImage bufferedImage = ImageIO.read(imageUrl);
			outputStream = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "jpg", outputStream);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(outputStream.toByteArray());// 返回Base64编码过的字节数组字符串
	}

	/**
	 * 将本地图片进行Base64位编码
	 * 
	 * @param imgUrl
	 *			图片的url路径，如http://.....xx.jpg
	 * @return
	 */
	public static String encodeImgageToBase64(String filePath) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		
		// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理  
		byte[] data = null;
		
		InputStream in= null;
		try{
			in=new FileInputStream(filePath);
			data = new byte[in.available()];
			in.read(data); 
			in.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		} finally{
			if (in!=null)
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		// 对字节数组Base64编码  
		BASE64Encoder encoder = new BASE64Encoder();
		// 返回Base64编码过的字节数组字符串  
		return encoder.encode(data);
	}

	/**
	 * 将Base64位编码的图片进行解码，并保存到指定目录
	 * 
	 * @param base64
	 *			base64编码的图片信息
	 * @return
	 */
	public static void decodeBase64ToImage(String base64, String filePath) {
		BASE64Decoder decoder = new BASE64Decoder();
		FileOutputStream write = null;
		try {
			write = new FileOutputStream(new File(filePath));
			byte[] decoderBytes = decoder.decodeBuffer(base64);
			write.write(decoderBytes);
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if (write!=null)
				try {
					write.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	
	/**
	 * 读取一张图片的RGB值，得到去掉部分白边所要截取的范围
	 * 
	 * @throws Exception
	 */
	public static void getPartImagePixel(BufferedImage bi, ArrayList<String> imageSize,
			int start1, int end1, int start2, int end2, boolean getHeight) {
		int[] rgb = new int[3];
		int startPosition = 0;
		int endPosition = 0;
		boolean findFirstBlack = false;
		boolean findWhight = false;
		boolean findBlack = false;
		int i = 0;
		int j = 0;
		try{
			for (i = start1; i < end1; i++) {
				if (findBlack) {
//					if (findWhight) {
						// 设置endHeight为截图的终止位置的高度/宽度
						if (i > 1) {
							endPosition = i - 1;
						} else {
							endPosition = i;
						}
						// break;
						findBlack = false;
//					}
					findWhight = true;
				}
				for (j = start2; j < end2; j++) {
					int w = i;
					int h = j;
					if (getHeight){
						w = j;
						h = i;
					}
					int pixel = bi.getRGB(w, h); // 下面三行代码将一个数字转换为RGB数字
					rgb[0] = (pixel & 0xff0000) >> 16;
					rgb[1] = (pixel & 0xff00) >> 8;
					rgb[2] = (pixel & 0xff);
					if (pixel != -1) {
						// 第一次找到黑色，设置startHeight为截图的起始位置的高度/宽度
						if (!findFirstBlack) {
							findFirstBlack = true;
							if (i > 1) {
								startPosition = i - 1;
							} else {
								startPosition = i;
							}
						}
						if (!findBlack) {
							findBlack = true;
						}
						findWhight = false;
						// System.out.println(pixel+"");
						// System.out.println("i=" + i + ",j=" + j + ":(" + rgb[0] +
						// ","
						// + rgb[1] + "," + rgb[2] + ")");
					}
				}
			}	
		}catch(Exception e){
			System.out.println("i="+i + " j="+j);
			e.printStackTrace();
		}
		imageSize.add(startPosition + "");
		imageSize.add(endPosition + "");

	}
	
	
	/**
	 * 读取一张图片的RGB值，得到去掉图片的上下左右白边所要截取的范围
	 * 
	 * @throws Exception
	 */
	public static String getImagePixel(String imagePath, ArrayList<String> imageSize,
			int limit) {
		int[] rgb = new int[3];
		int width = 0;
		int height = 0;
		int minx = 0;
		int miny = 0;

		File imgFile = new File(imagePath);
		ImageInputStream iis = null;
		String formatName = null;
		BufferedImage bi = null;
		try {
			iis = ImageIO.createImageInputStream(imgFile);
			Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
			if (readers.hasNext()) {
				// get the first reader
				ImageReader reader = readers.next();
				formatName = reader.getFormatName();
				logger.debug("Format of image: " + imagePath + " is "
						+ formatName);
				reader.setInput(iis, true);

				width = reader.getWidth(0);
				height = reader.getHeight(0);

				bi = reader.read(0);
				minx = bi.getMinX();
				miny = bi.getMinY();
			} else {
				logger.debug("can not recognize the format of image : "
						+ imagePath);
				// throw new RuntimeException("No readers found!");
			}

		} catch (Exception e) {
			logger.debug("Read image file: " + imagePath + " exception: "
					+ e.getMessage());
			// e.printStackTrace();
		} finally {
			if (iis != null) {
				try {
					iis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		// 对于超过一定高度的图片, 处理空白
		if (height > limit) {
			
			// 因为有些图片的边是黑色的,为了避免黑边的影响,
			// 接下来循环的起始点的高度和宽度增加2个像素.结束点的高度和宽度减少2个像素
			int pic_pixel = 2;
			int startH = miny;
			int endH = height;
			int startW = minx;
			int endW = width;
			if (height > pic_pixel) {
				endH = height - pic_pixel;
			}
			if (miny + pic_pixel < endH) {
				startH = miny + pic_pixel;
			}
			if (width > pic_pixel) {
				endW = width - pic_pixel;
			}
			if (minx + 5 < endW) {
				startW = minx + 5;
			}
			//获得截取的起始高度和结束高度
			getPartImagePixel(bi, imageSize, startH, endH, startW, endW, true);
			
			//获得截取的起始宽度和结束宽度
			getPartImagePixel(bi, imageSize, startW, endW, startH, endH, false);
					}
		return formatName;
	}
	
	
	
	/**
	 * 对图片进行裁剪	  
	 * 
	 */
	public static void cutImage(String imagePath, String outputImage, String type,
			int x, int y, int width, int height) throws IOException {

		File imgFile = new File(imagePath);
		ImageInputStream imageStream = null;
		OutputStream out = null;
		try {
			out = new FileOutputStream(outputImage);
			String imageType = (null == type || "".equals(type)) ? "jpg" : type;
			Iterator<ImageReader> readers = ImageIO
					.getImageReadersByFormatName(imageType);
			ImageReader reader = readers.next();
			imageStream = ImageIO.createImageInputStream(imgFile);
			reader.setInput(imageStream, true);
			ImageReadParam param = reader.getDefaultReadParam();
			Rectangle rect = new Rectangle(x, y, width, height);
			param.setSourceRegion(rect);
			BufferedImage bi = reader.read(0, param);
			ImageIO.write(bi, imageType, out);
		} finally {
			imageStream.close();
			out.close();
		}

	}

	
	public static boolean cutImage(String imagePath, String outputImage, int limit) {
		ArrayList<String> imageSize = new ArrayList<String>();
		try {
			// 对于超过一定高度的图片,去掉图片上下的空白
			String imageType = getImagePixel(imagePath, imageSize, limit);
			int startHeight = 0;			
			int endHeight = 0;
			int startWidth = 0;
			int endWidth = 0;
			if (imageSize.size() == 4) {
				String value = imageSize.get(0);
				startHeight = Integer.parseInt(value);
				value = imageSize.get(1);
				endHeight = Integer.parseInt(value);
				value = imageSize.get(2);
				startWidth = Integer.parseInt(value);
				value = imageSize.get(3);
				endWidth = Integer.parseInt(value);
			}			
			int rectangleHeight = endHeight;
			if (endHeight > startHeight) {
				rectangleHeight = endHeight - startHeight;
			}
			int rectangleWidth = endWidth;
			if (endWidth > startWidth) {
				rectangleWidth = endWidth - startWidth;
			}
			if (rectangleHeight > 0 && imageType != null) {
				cutImage(imagePath, outputImage, imageType, startWidth, startHeight,
						rectangleWidth, rectangleHeight);				
			} else {
				return false;
			}

		} catch (Exception e) {
			return false;
			// throw e;
		}
		return true;

	}
	
	
	
	
	// 对图片进行压缩处理，保持图片不失真
	public static boolean compressImage(String sourceFile, String tempFile, String destFile) {
		
		String imagePath=sourceFile;	
		
		/*
		//高于300的图片，进行去掉空白的操作		
		int limit=300;
		boolean result = cutImage(sourceFile, tempFile, limit);
		if (result){	
			imagePath=tempFile;
		}		
		*/
		
		// 对图片进行缩小处理
//		boolean result=thumbnailImage(imagePath, destFile);
		
		BufferedImage bufferedImage=zoomImage(imagePath);
	         
		boolean result=saveImage(bufferedImage, destFile);  
		
		return result;
	}
	
	
	/**
	 * <p>
	 * Title: thumbnailImage
	 * </p>
	 * <p>
	 * Description: 根据图片路径生成缩略图
	 * </p>
	 * 
	 * @param imagePath
	 *            原图片路径
	 * @param destFile	
	 */
	public static boolean thumbnailImage(String imagePath, String destFile) {
		
		int width = 0;
		int height = 0;		
		float basesize = 100;

		File imgFile = new File(imagePath);
		ImageInputStream iis = null;
//			String formatName = null;
		BufferedImage im = null;
		BufferedImage imResult = null;  
		try {
			iis = ImageIO.createImageInputStream(imgFile);
			Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
			if (readers.hasNext()) {
				// get the first reader
				ImageReader reader = readers.next();
//					formatName = reader.getFormatName();
//					logger.info("Format of image: " + imagePath + " is "
//							+ formatName);
				reader.setInput(iis, true);

				width = reader.getWidth(0);
				height = reader.getHeight(0);
				
				float fWidth=(float)width;
				float fHeight=(float)height;
				
				int toWidth=width;
				int toHeight=height;
				
				// 根据原图与要求缩小的尺寸，找到最合适的缩略图比例
				float size1 = fHeight/basesize;		
				if (size1>1){
					float size2 = fWidth/basesize;	
					if (size2>1){	
						float count = size1 < size2 ? size1:size2;
						float tempWidth = width/count;
						float tempheight = height/count;
						/* 调整后的图片的宽度和高度 */  
						width = (int)tempWidth;						
						height = (int)tempheight;
					}
				}				

				im = reader.read(0);	
				
				/* 新生成结果图片 */  
				imResult = new BufferedImage(toWidth, toHeight,  
	                    BufferedImage.TYPE_INT_RGB);  
	  
				imResult.getGraphics().drawImage(  
	                    im.getScaledInstance(toWidth, toHeight,  
	                            java.awt.Image.SCALE_SMOOTH), 0, 0, null);  
				
				boolean result=saveImage(im,destFile);
				
			
				return result;
			} else {
				logger.info("can not recognize the format of image : "
						+ imagePath);
				// throw new RuntimeException("No readers found!");
				return false;
			}

		} catch (Exception e) {
			logger.debug("Read image file: " + imagePath + " exception: "
					+ e.getMessage());
			// e.printStackTrace();
			return false;
		} finally {
			if (iis != null) {
				try {
					iis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	
	  /** 
     * @param im 
     *            原始图像 
     * @param basesize 
     *            用来计算压缩倍数,比如0.5就是缩小一半,0.98等等double类型 
     * @return 返回处理后的图像 
     */  
	public static BufferedImage zoomImage(String src) {  
          
        BufferedImage result = null;  
  
        try {  
            File srcfile = new File(src);  
            if (!srcfile.exists()) {  
                System.out.println("文件不存在");  
                  
            }  
            BufferedImage im = ImageIO.read(srcfile);  
  
            /* 原始图像的宽度和高度 */  
            int width = im.getWidth();  
            int height = im.getHeight();  
            
            float fWidth=(float)width;
			float fHeight=(float)height;
			
			
			//压缩计算  
            float resizeTimes = 1f;  /*这个参数是要转化成的倍数,如果是1就是转化成1倍*/  
              
			
			// 根据原图与要求缩小的尺寸，找到最合适的缩略图比例
			float resizeTimes1 = basesize/fHeight;		
			if (resizeTimes1<1){
				float resizeTimes2 = basesize/fWidth;	
				if (resizeTimes2<1){	
					resizeTimes = resizeTimes1 > resizeTimes2 ? resizeTimes1:resizeTimes2;
				}
			}		
              
          
            /* 调整后的图片的宽度和高度 */  
            int toWidth = (int) (width * resizeTimes);  
            int toHeight = (int) (height * resizeTimes);  
  
            /* 新生成结果图片 */  
            result = new BufferedImage(toWidth, toHeight,  
                    BufferedImage.TYPE_INT_RGB);  
  
            result.getGraphics().drawImage(  
                    im.getScaledInstance(toWidth, toHeight,  
                            java.awt.Image.SCALE_SMOOTH), 0, 0, null);  
              
  
        } catch (Exception e) {  
            System.out.println("创建缩略图发生异常" + e.getMessage());  
        }  
          
        return result;  
  
    }  
	
	
	  /** 
     * @param im 
     *            原始图像 
     * @param fileFullPath 
     *            生成的图片全路径 
     * @return 返回处理成功与否 
     */  
//    public static boolean writeHighQuality(BufferedImage im, String fileFullPath) {  
//         try {  
//             /*输出到文件流*/  
//             FileOutputStream newimage = new FileOutputStream(fileFullPath);  
//             JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(newimage);  
//             JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(im);  
//             /* 压缩质量 */  
//             jep.setQuality(0.9f, true);  
//             encoder.encode(im, jep);  
//             /*近JPEG编码*/  
//             newimage.close();  
//             return true;  
//         } catch (Exception e) {  
//             return false;  
//         }  
//     } 
	
	 
	 static boolean saveImage(BufferedImage dstImage, String dstName) { 
		 
		 try {  
			    
			 String formatName = dstName.substring(dstName.lastIndexOf(".") + 1);  
			 //FileOutputStream out = new FileOutputStream(dstName);  
			 //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);  
			 //encoder.encode(dstImage);  
			 ImageIO.write(dstImage, /*"GIF"*/ formatName /* format desired */ , new File(dstName) /* target */ );  
            return true;  
        } catch (Exception e) {  
            return false;  
        }  
		  
	}  
	
	
	/**
	 * 将图片写入到磁盘
	 * @param img 图片数据流
	 * @param fileName 文件保存时的名称
	 */
	public static void writeImageToDisk(byte[] img, String filePath){
		try {
			if (img==null || img.length==0){
				logger.info("没有获取到图片内容");
				return ;
			}
			File file = new File(filePath);
			FileOutputStream fops = new FileOutputStream(file);
			fops.write(img);
			fops.flush();
			fops.close();
//			System.out.println("图片已经写入到C盘");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 根据地址获得数据的字节流
	 * @param strUrl 网络连接地址
	 * @return
	 */
	public static byte[] getImageFromNetByUrl(String strUrl) throws Exception{
		
		int tryTime=3;	
		byte[] btImg = null;
		while(tryTime>0){
			InputStream inStream = null;
			ByteArrayOutputStream outStream = null;
			try {
				URL requestURL=new URL(strUrl);
				HttpURLConnection conn = (HttpURLConnection)requestURL.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(20 * 1000);
				
				int code=conn.getResponseCode();
				if(code==404){
					return null;
				}
				
				inStream = conn.getInputStream();//通过输入流获取图片数据
				outStream = new ByteArrayOutputStream();
				btImg = readInputStream(inStream, outStream);//得到图片的二进制数据
				
			} catch (Exception e) {			
				String message=e.getMessage();
				if (message.indexOf("Connection timed out")>-1){
					logger.info(message);
				}else{
					throw e;
				}	
			} finally{
				
				tryTime--;
				
				if(inStream!=null){
					try {
						inStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(outStream!=null){
					try {
						outStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			if(btImg!=null){
				break;
			}
			
		}
		
		return btImg;
	}
	
	/**
	 * 从输入流中获取数据
	 * @param inStream 输入流
	 * @return
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream inStream, ByteArrayOutputStream outStream) throws Exception{
	
		byte[] buffer = new byte[1024];
		int len = 0;
		while( (len=inStream.read(buffer)) != -1 ){
			outStream.write(buffer, 0, len);
		}	
		return outStream.toByteArray();
	}
	
	

	/**
	 * 根据地址获得数据的字节流
	 * @param strUrl 网络连接地址
	 * @return
	 */
	public static void getImageFromUrl(String strUrl, String filePath) throws Exception{
		
		long startGetImag = System.currentTimeMillis();	
			
		
		byte[] img;
		
		logger.info("开始获取图片：" + strUrl);
		img = getImageFromNetByUrl(strUrl);
		
		long endGetImag = System.currentTimeMillis();	
		
		long time=(endGetImag-startGetImag)/1000;
		
		logger.info("完成获取图片, 耗时: "+ time +" 秒");
	
		writeImageToDisk(img, filePath);

	}
	
	public static void main(String[] args) {
		// 测试从图片文件转换为Base64编码
//		String sourceFile="C:/temp/test/23168059.jpg";
//		String tempFile="C:/temp/test/23168059_2.jpg";
//		String destFile="C:/temp/test/test4.jpg";
//	
//		boolean result = compressImage(sourceFile, tempFile, destFile);
	
		
//		filePath="C:/temp/huiguanjia/downloadimage/zanwutu.jpg";
//		String base64String= encodeImgageToBase64(filePath);
//		System.out.println(base64String);
		
//		String base64String="/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAMCAgMCAgMDAwMEAwMEBQgFBQQEBQoHBwYIDAoMDAsKCwsNDhIQDQ4RDgsLEBYQERMUFRUVDA8XGBYUGBIUFRT/wAALCABkAGQBAREA/8QAHAABAAIDAQEBAAAAAAAAAAAAAAYHAQUIBAIJ/8QALxAAAQQCAQQBBAAEBwAAAAAAAQACAwQFBhEHEhMhMQgUIkEVI1FhFigyM3aBtP/aAAgBAQAAPwD9U0RERERERERERERERERERERERERERERERERERFhOUWURYWURFWv1E7lb0vpHnpsTbhp7DkIv4XiJZ7DYGsu2P5cTy8+mhhcZHH9Njcf0qy2Tr3k9I6ObFaGQxGcmxFOTHU8ni8jZyc89tlWMRed0VUMbNJNIw8Ehp8jeDySBO+i+e2azDisDc81qnhMRBVyWRy1W+y5cu9jB5WSzwxslBLZO/jkhxb8fBpHqv9Qmz6v1mxOCflbeNMtjJW8XUnqOgD6xoOjj+7BLWPYy2Huj5IL2tH7Bct/o/Xy3hc/uGy7PZ2zJYbGjFarWpDFtb35MRx+dz44yWMsS2bUcPDXeMdg99o7h0JfyOyZTFYm5gqVSk+xGJbNXOCRk0HLQQwiPuHcDyHeyPXolQTH9Q98v9VM3pIoa6yfF4ijl3XPPYLZG2ZrMQYG9vot+1J55PPePQ4U61ezsGG161Y3jIYZ1qOxI8WMXFJBXZAXfy2uEr3HuaDwXc8O454bzwqd6ffUBW/wZtr81cyuxy4/K7DLNJjYmF9LGV79iOMl4LP8ATHGA3gl54B9+it/0g3zcYNN16rnMJnNrntWe1meZVrVh9hI5zq9iw0zDl4iMfkEbeS4khvyrrRFWv1B4CfMdLdjsVLENW1TxV94klqiclrqsrXNbyR2kg8d3s8cj9lc24a06/wBM9L2HNZ9+f6f4JuF+6dAXxV22RBA2CLit3eSCCd7JJHkOf5e1v4sicFanSDE7DnetO65LKSUrdDWs1YxVUz27M1mrHLj6E3jhLiGFhe97nGQOdyWgEBvuoeveUtdKtrfd2DF6x9m2nc8EebFiWPZcldZ9v2S2HjtEcFZp8ndwxjJSAA2P1IdpZisZ1z6a9M6E9CDHZafF7JOa1proGSY9k72xAE9znTvbWfHz7eIJne+0rsMegFTmun/Nxvn/AAzA/wDsyisDauo2t6ZrmZzuWysUOLwzgzISwNdYdXce3hrmRhz+782fjxz+Q9e1y7Pn9hH087prNbV8njMnvWZztDAvyzBWmsOyV2Z0L21yfK0MrzPneZWx9jYnchWNb1atr31FdIdexE9wVsVrmZuTxvtyvDo420akJc0v4P8Auu45HyOR7C6BRF5cpjKuaxtvH3oGWaVuJ8E8Eg5bJG5pa5p/sQSP+14cVqOGwWr1tcx2MrUcFVrNpwY+vGI4YoWt7QxrR8AD+i1PTnpdgultDLVME24G5TIy5S1Neuy25pJnhreTJK5ziGsjYxoJPDWAfpfUPTLBwzXrfhfPlrlc1JcpbcLNnxHnlgdKHBrDyfwADf7LSX/p56fZXQbunW9brT4S65ks7XFwmfKwARzeYESNlZ2t7HhwLO0BpAACkFrpnq99lIXcJUyLqdZtOCW8z7iRsTfhve/lx/rySST8ryjo7orbD5xp+DE72hjpRQi73NBJDS7jkgEngfrk/wBVs9Z0PW9Lfffr+AxmEffkEtx+OqRwOsyAcB8haB3uA9cu5PCV9LxsW0TbDKx9zLuYYYrFl/ea0R47o4R8RtcWgu4HLiB3E8DiP6X0O1HQN0zW04elYiy+ViZXkfPdmnjrwtcX+KvG9xbCwvJeWsABd74+FPkRERERERERERERERERERERERERERERERERERERERERERF//9k=";
		
//		String base64String="/9j/4AAQSkZJRgABAQEAkQCRAAD/4QD4RXhpZgAATU0AKgAAAAgABgEaAAUAAAABAAAAVgEbAAUA\nAAABAAAAXgEoAAMAAAABAAMAAAExAAIAAAAQAAAAZgEyAAIAAAAUAAAAdodpAAQAAAABAAAAigAA\nALYAAN7+AAAD6AAA3v4AAAPocGFpbnQubmV0IDQuMC4yADIwMTQ6MTI6MDUgMTU6MTQ6MTMAAAOg\nAQADAAAAAf//AACgAgAEAAAAAQAAAcmgAwAEAAAAAQAAAckAAAAAAAAAAwEaAAUAAAABAAAA4AEb\nAAUAAAABAAAA6AEoAAMAAAABAAIAAAAAAAAAAABIAAAAAQAAAEgAAAAB/9sAQwABAQEBAQEBAQEB\nAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEB/9sA\nQwEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEB\nAQEBAQEBAQEB/8AAEQgA4QEsAwEiAAIRAQMRAf/EAB8AAAEFAQEBAQEBAAAAAAAAAAABAgMEBQYH\nCAkKC//EALUQAAIBAwMCBAMFBQQEAAABfQECAwAEEQUSITFBBhNRYQcicRQygZGhCCNCscEVUtHw\nJDNicoIJChYXGBkaJSYnKCkqNDU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6\ng4SFhoeIiYqSk5SVlpeYmZqio6Slpqeoqaqys7S1tre4ubrCw8TFxsfIycrS09TV1tfY2drh4uPk\n5ebn6Onq8fLz9PX29/j5+v/EAB8BAAMBAQEBAQEBAQEAAAAAAAABAgMEBQYHCAkKC//EALURAAIB\nAgQEAwQHBQQEAAECdwABAgMRBAUhMQYSQVEHYXETIjKBCBRCkaGxwQkjM1LwFWJy0QoWJDThJfEX\nGBkaJicoKSo1Njc4OTpDREVGR0hJSlNUVVZXWFlaY2RlZmdoaWpzdHV2d3h5eoKDhIWGh4iJipKT\nlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uLj5OXm5+jp6vLz9PX2\n9/j5+v/aAAwDAQACEQMRAD8A/v4ooooAKKKKACiiigAooooAKKKKACiiigAooooAKKQsB1qN5Qil\n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\nUUAFFFFABRRRQAUUUUAFFFFABRRRQB//2Q==";
	  //String base64String="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";
//		String base64String="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";
		// 测试从Base64编码转换为图片文件  
//		decodeBase64ToImage(base64String,"C:/work/test111.jpg");
		
		
	}
}
