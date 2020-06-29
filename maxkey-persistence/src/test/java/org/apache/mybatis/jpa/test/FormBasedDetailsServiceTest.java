package org.apache.mybatis.jpa.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.mybatis.jpa.util.WebContext;
import org.junit.Before;
import org.junit.Test;
import org.maxkey.domain.apps.AppsFormBasedDetails;
import org.maxkey.persistence.service.AppsFormBasedDetailsService;
import org.maxkey.util.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class FormBasedDetailsServiceTest {
	
	private static final Logger _logger = LoggerFactory.getLogger(FormBasedDetailsServiceTest.class);
	
	public static ApplicationContext context;
	
	public static AppsFormBasedDetailsService service;
	
	public AppsFormBasedDetailsService getservice() {
		service=(AppsFormBasedDetailsService)WebContext.getBean("appsFormBasedDetailsService");
		return service;
	}
	
	@Test
	public void insert() throws Exception{
		_logger.info("insert...");
		
		AppsFormBasedDetails formBasedDetails=new AppsFormBasedDetails();
		
		service.insert(formBasedDetails);
		
		Thread.sleep(1000);
		service.remove(formBasedDetails.getId());
		
	}
	
	@Test
	public void get() throws Exception{
		_logger.info("get...");
		AppsFormBasedDetails formBasedDetails=service.get("850379a1-7923-4f6b-90be-d363b2dfd2ca");
		
		 _logger.info("formBasedDetails "+formBasedDetails);

	}
	
	
	@Test
	public void remove() throws Exception{
		
		_logger.info("remove...");
		AppsFormBasedDetails formBasedDetails=new AppsFormBasedDetails();
		formBasedDetails.setId("921d3377-937a-4578-b1e2-92fb23b5e512");
		service.remove(formBasedDetails.getId());
		
	}
	
	@Test
	public void batchDelete() throws Exception{
		_logger.info("batchDelete...");	
		List<String> idList=new ArrayList<String>();
		idList.add("8584804d-b5ac-45d2-9f91-4dd8e7a090a7");
		idList.add("ab7422e9-a91a-4840-9e59-9d911257c918");
		idList.add("12b6ceb8-573b-4f01-ad85-cfb24cfa007c");
		idList.add("dafd5ba4-d2e3-4656-bd42-178841e610fe");
		service.batchDelete(idList);
	}

	@Test
	public void queryPageResults() throws Exception{
		
		_logger.info("queryPageResults...");
		AppsFormBasedDetails formBasedDetails=new AppsFormBasedDetails();
		 //student.setId("af04d610-6092-481e-9558-30bd63ef783c");
		// student.setStdGender("M");
		 //student.setStdMajor(政治");
		 //student.setPageResults(10);
		 //student.setPage(2);
		 //_logger.info("queryPageResults "+service.queryPageResults(formBasedDetails));
	}
	
	@Test
	public void queryPageResultsByMapperId() throws Exception{

		_logger.info("queryPageResults by mapperId...");
		 AppsFormBasedDetails formBasedDetails=new AppsFormBasedDetails();
		// student.setStdGender("M");
		 //student.setStdMajor(政治");
		// student.setPageResults(10);
		// student.setPage(2);
		 
		 //_logger.info("queryPageResults by mapperId "+service.queryPageResults("queryPageResults1",formBasedDetails));
		 
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
			FormBasedDetailsServiceTest runner=new FormBasedDetailsServiceTest();
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
