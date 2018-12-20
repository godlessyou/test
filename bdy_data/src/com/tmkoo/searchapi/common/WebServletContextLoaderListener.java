package com.tmkoo.searchapi.common;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.tmkoo.searchapi.service.system.WebPropertiesService;
import com.tmkoo.searchapi.util.ExecutorProcessPool;
 
/**
 *  网站的初始化
 * 
 * @author 
 *
 */
public class WebServletContextLoaderListener implements ServletContextListener{
	 
	/**
	 * 初始化 
	 */
	public void contextInitialized(ServletContextEvent servletContextEvent) {  
		ServletContext servletContext = servletContextEvent.getServletContext();  
         
		WebPropertiesService webPropertiesService = (WebPropertiesService)WebApplicationContextUtils.getWebApplicationContext(servletContext).getBean("webPropertiesService");  
		webPropertiesService.fillGlobal();       
		
    }   
     /**
      * 销毁资源 
      */
    public void contextDestroyed(ServletContextEvent servletContextEvent) {  
    	//关闭线程池
    	ExecutorProcessPool pool = ExecutorProcessPool.getInstance();
    	pool.shutdown();
    } 
}
