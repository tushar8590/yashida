/**
 * 
 */
package com.charliechocolatefactory.quartz.scheduler.jobs;

/**
 * @author Tushar
 *
 */
import org.quartz.Job;

public class JobFactory {

	
	  public static Class<? extends Job> getJobClass(String jobName){
		  
		  Class<? extends Job> jobClass = null;
		  
		  if(jobName.equalsIgnoreCase("generic")){
			  jobClass = GenericJob.class;
		  }else if(jobName.trim().equalsIgnoreCase("PCI Feed Updater")){
			  jobClass = PriceCheckIndiaFeedUpdater.class;
		  }else if(jobName.trim().equalsIgnoreCase("URL Resolver")){
			  jobClass = URLResolver.class;
		  }else if(jobName.trim().equalsIgnoreCase("Spec Loader")){
			  jobClass = FlipkartSpecLoader.class;
			  
		  }
		  
		  
		  return jobClass;
	  }
}
