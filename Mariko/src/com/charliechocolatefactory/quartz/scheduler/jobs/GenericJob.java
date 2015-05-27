/**
 * 
 */



package com.charliechocolatefactory.quartz.scheduler.jobs;

/**
 * @author JN1831
 *
 */
import java.io.IOException;
import java.util.Properties;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
public class GenericJob implements Job{
	
	
	Properties props1 = new Properties();
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("Hello");
		try {
			props1.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("com/charliechochlatefactory/resources/DBConfig.properties"));
			System.out.println(props1.get("DB_USERNAME"));
			
		} catch (IOException e) {
			e.printStackTrace();
		 }
	}

}
