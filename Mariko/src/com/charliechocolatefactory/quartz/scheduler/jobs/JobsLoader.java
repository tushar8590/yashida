/**
 * 
 */
package com.charliechocolatefactory.quartz.scheduler.jobs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Tushar
 *
 */
public class JobsLoader extends ActionSupport {

	
	
	private List<String> jobs;
	 
	private String YourJob;
 
	public String getYourJob() {
		return YourJob;
	}
 
	public void setYourJob(String YourJob) {
		this.YourJob = YourJob;
	}
 
	public JobsLoader(){
		jobs = new ArrayList<String>();
		jobs.add("Generic");
		jobs.add("PCI Feed Updater");
		jobs.add("Spec Loader");
		jobs.add("URL Resolver");
		jobs.add("FK Feed Loader");
		jobs.add("OMG Feed Loader");
		jobs.add("SD Feed Loader");
	}
 
	public String[] getDefaultJob(){
		return new String [] {"Generic", "PCI Feed Updater"};
	}
 
	public List<String> getJobs() {
		return jobs;
	}
 
	public void setJobs(List<String> jobs) {
		this.jobs = jobs;
	}
 
	public String execute() {
		return SUCCESS;
	}
 
	public String display() {
		return NONE;
	}


}
