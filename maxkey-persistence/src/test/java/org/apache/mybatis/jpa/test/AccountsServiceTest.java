package org.apache.mybatis.jpa.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.mybatis.jpa.util.WebContext;
import org.junit.Before;
import org.junit.Test;
import org.maxkey.domain.Accounts;
import org.maxkey.domain.apps.AppsFormBasedDetails;
import org.maxkey.persistence.service.AccountsService;
import org.maxkey.persistence.service.AppsFormBasedDetailsService;
import org.maxkey.util.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AccountsServiceTest {
	
	private static final Logger _logger = LoggerFactory.getLogger(AccountsServiceTest.class);
	
	public static ApplicationContext context;
	
	public static AccountsService service;
	
	public AccountsService getservice() {
		service=(AccountsService)WebContext.getBean("accountsService");
		return service;
	}
	

	@Test
	public void get() throws Exception{
		_logger.info("get...");
		Accounts accounts=service.get("26b1c864-ae81-4b1f-9355-74c4c699cb6b");
		
		 _logger.info("accounts "+accounts);

	}
	
	@Test
	public void load() throws Exception{
		_logger.info("get...");
		Accounts queryAccounts=new Accounts("7BF5315CA1004CDB8E614B0361C4D46B","fe86db85-5475-4494-b5aa-dbd3b886ff64");
		Accounts accounts=service.load(queryAccounts);
		
		 _logger.info("accounts "+accounts);

	}
	
	
	@Test
	public void findAll() throws Exception{
		_logger.info("findAll...");
		_logger.info("findAll "+service.findAll());
	}
	
	@Before
	public void initSpringContext(){
		if(context!=null) return;
		_logger.info("init Spring Context...");
		SimpleDateFormat sdf_ymdhms =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTime=sdf_ymdhms.format(new Date());

		try{
			AccountsServiceTest runner=new AccountsServiceTest();
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
