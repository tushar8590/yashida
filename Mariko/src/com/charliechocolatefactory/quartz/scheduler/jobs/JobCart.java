/**
 * 
 */
package com.charliechocolatefactory.quartz.scheduler.jobs;

import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;

/**
 * @author JN1831
 * this class contains the Job list, trigger time and trigger timing in case of cron trigger
 */
public  class JobCart {

	/**
	 * @param args
	 */
	
  private List<String> jobList;
  private String jobType;
  private String jobTimting;

	private List<String> getJobList() {
		return jobList;
	}

	public void setJobList(List<String> jobList) {
		this.jobList = jobList;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	
	/**
	 * Apply the logic to build the Cron Expression on the basis of Job type
	 * @return
	 */
	public String getJobTimting() {
		return jobTimting;
	}

	public void setJobTimting(String jobTimting) {
		this.jobTimting = jobTimting;
	}
  
	
	/**
	 * This method takes the List of jobs and maps it to respective Java Classes
	 * @return processdList of Jobs
	 */
	public List<Class<? extends Job>> getProcessedJobList(){
		List<Class<? extends Job>> classNames = new ArrayList<Class<? extends Job>>();
		for(String name:jobList){
			classNames.add(JobFactory.getJobClass(name));
		}
		
		return classNames;
	}
   
}
