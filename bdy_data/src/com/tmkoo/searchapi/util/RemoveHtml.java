package com.tmkoo.searchapi.util;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.commons.beanutils.PropertyUtils;
/**
 * 替换实体中字符串属性的HTML代码的工具类。可以替换三个字符( < > ")
 * @author cntmdb
 * @since 2010-8-2
 *
 */
public class RemoveHtml {
	
	public static String clear(String oraStr){
		if(oraStr.toString().indexOf("<")>=0 || oraStr.toString().indexOf(">")>=0  || oraStr.toString().indexOf("\"")>=0 ){
			oraStr = oraStr.toString().replaceAll("<", "&lt;");
			oraStr = oraStr.toString().replaceAll(">", "&gt;");
			oraStr = oraStr.toString().replaceAll("\"", "&quot;");
		}
		return oraStr;
	}
	
	/**
	 * 对对象中字符型属性进行移除HTML标记。
	 * @param object 需要清理的对象
	 * @param unRemoveAttributes  有些属性不需要移除，把属性名放入里面
	 */
	public static void execute(Object object,Object ... unRemoveAttributes){
		System.out.println(object.getClass());
		Class clazz =null;
		try {
			if (object.getClass().getName().indexOf("_$$_javassist") > 0) {
				clazz = Class.forName(object.getClass().getName().substring(0,object.getClass().getName().indexOf("_$$_javassist")));
			} else {
				clazz = Class.forName(object.getClass().getName());
			}
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} 
		String unRemoveAttributeStr = "";
		if(unRemoveAttributes!=null  ){
			for(Object att:unRemoveAttributes){
				unRemoveAttributeStr +=","+att.toString();
			}
			unRemoveAttributeStr +=",";
		}
		Field [] fields = clazz.getDeclaredFields();
		for(Field f:fields){
			try {
				if(unRemoveAttributes!=null  ){
					 if( unRemoveAttributeStr.indexOf(","+ f.getName()+",")>=0){
						 continue;
					 }
				} 
				if(f.getType().getName().equals("java.lang.String")){ 
					Object property  =PropertyUtils.getProperty(object, f.getName());//对象的属性值
					if(property == null || property.toString().equals("")) continue;
					if(property.toString().indexOf("<")>=0 || property.toString().indexOf(">")>=0 ){
						property = property.toString().replaceAll("<", "&lt;");
						property = property.toString().replaceAll(">", "&gt;");
						property = property.toString().replaceAll("\"", "&quot;");
						Method setMethod;
						try {
							String fieldName = f.getName().substring(0,1).toUpperCase()+f.getName().substring(1);
							setMethod = object.getClass().getMethod("set"+fieldName,String.class);
							if(setMethod!=null)
								setMethod.invoke(object,property.toString());  
						} catch (SecurityException e) {
							//不可能到这里
						}
					}
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} 
		}
	}
	
	
	public static void main(String []argus){
		String xxx="<script>alert('')</script>";
		System.out.println(clear(xxx));
		}
	 
}
