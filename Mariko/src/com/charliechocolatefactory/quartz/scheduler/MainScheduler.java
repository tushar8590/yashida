/**
 * 
 */
package com.charliechocolatefactory.quartz.scheduler;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.charliechocolatefactory.quartz.scheduler.jobs.GenericJob;
import com.charliechocolatefactory.quartz.scheduler.jobs.JobCart;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;




/**
 * @author JN1831
 *
 */
public class MainScheduler {
	
	static Scheduler scheduler;
	private JobCart jobCart;
	
	
	
	
	
	public void startScheduler(){
		
		// create a list of jobs to be passed to job cart , this list will be taken as a 
		/*JobCart jc = new JobCart();
		jc.setJobList(new ArrayList<String>(Arrays.asList("Generic")));*/
		
		List<Class<? extends Job>> JobClasses = jobCart.getProcessedJobList();
		
		for(Class<? extends Job> jobClass:JobClasses){
		
			JobDetail job = JobBuilder.newJob(jobClass)
				.withIdentity(jobClass.toString(), "group1").build();
			
			
			System.out.println(jobClass.toString()+"Trigger");
			Trigger cronTrigger = TriggerBuilder
					.newTrigger()
					.withIdentity(jobClass.toString()+"Trigger",Scheduler.DEFAULT_GROUP)
					.withSchedule(
						CronScheduleBuilder.cronSchedule("0 27 16 * * ? *"))
					.build();
	 
			// Trigger the job to run on the next round minute
	/*		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("dummyTriggerName", "group1")
				.withSchedule(
					SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInSeconds(5).repeatForever())
				.build();*/
			
			 
			// schedule it
			
			try {
				scheduler = new StdSchedulerFactory().getScheduler();
				
				scheduler.scheduleJob(job, cronTrigger);
			} catch (SchedulerException e) {
				
				e.printStackTrace();
			}
		}	
		
		try {
			scheduler.start();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public  String getSchedulerStatus() throws SchedulerException {
		if (scheduler != null && scheduler.isStarted())
			return "01";
		else if (scheduler == null || scheduler.isShutdown())
			return "02";
		else
			return "03";
	}
	
	
	public  List getRunningJobsList() throws SchedulerException {
		List jobs = null;
		if (scheduler == null || scheduler.isShutdown()) {
			return new ArrayList();
		}
		jobs = getRunningJobNames();
		return jobs;
	}
	
	private  List getRunningJobNames() throws SchedulerException {
		List desc = new ArrayList<String>();
		 for (String groupName : scheduler.getJobGroupNames()) {
			 
		     for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
		 
			  String jobName = jobKey.getName();
			  String jobGroup = jobKey.getGroup();
		 
			  //get job's trigger
			  List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
			  Date nextFireTime = triggers.get(0).getNextFireTime(); 
		 
			  desc.add("[jobName] : " + jobName + " [groupName] : "
					+ jobGroup + " - " + nextFireTime);
		 
			  }
		 
		    }
		return desc;
	}

	public JobCart getJobCart() {
		return jobCart;
	}

	public void setJobCart(JobCart jobCart) {
		this.jobCart = jobCart;
	}

	public static Scheduler getScheduler(){
		return scheduler;
	}
	
}
