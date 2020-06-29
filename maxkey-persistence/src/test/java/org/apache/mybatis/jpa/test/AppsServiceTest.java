package org.apache.mybatis.jpa.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.mybatis.jpa.util.WebContext;
import org.junit.Before;
import org.junit.Test;
import org.maxkey.domain.Accounts;
import org.maxkey.domain.apps.Apps;
import org.maxkey.domain.apps.AppsFormBasedDetails;
import org.maxkey.persistence.service.AccountsService;
import org.maxkey.persistence.service.AppsFormBasedDetailsService;
import org.maxkey.persistence.service.AppsService;
import org.maxkey.util.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppsServiceTest {
	
	private static final Logger _logger = LoggerFactory.getLogger(AppsServiceTest.class);
	
	public static ApplicationContext context;
	
	public static AppsService service;
	
	public AppsService getservice() {
		service=(AppsService)WebContext.getBean("appsService");
		return service;
	}
	

	@Test
	public void get() throws Exception{
		_logger.info("get...");
		Apps a=new Apps();
		a.setPageNumber(2);
		a.setPageSize(10);
		;
		getservice().queryPageResults(a);
		// _logger.info("apps "+);

	}
	
	
	
	@Before
	public void initSpringContext(){
		if(context!=null) return;
		_logger.info("init Spring Context...");
		SimpleDateFormat sdf_ymdhms =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTime=sdf_ymdhms.format(new Date());

		try{
			AppsServiceTest runner=new AppsServiceTest();
			runner.init();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		_logger.info("-- --Init Start at " + startTime+" , End at  "+sdf_ymdhms.format(new Date()));
	}
	
	//Initialization ApplicationContext for Project
	public void init(){
		_logger.info("init ...");
		
		_logger.info("Application dir "+System.getProperty("user.dir"));
		context = new ClassPathXmlApplicationContext(new String[] {"spring/applicationContext.xml"});
		WebContext.applicationContext=context;
		getservice();
		System.out.println("init ...");
		
	}
	
}
