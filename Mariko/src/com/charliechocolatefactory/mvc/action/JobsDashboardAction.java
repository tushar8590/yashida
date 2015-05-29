package com.charliechocolatefactory.mvc.action;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.quartz.SchedulerException;

import com.charliechocolatefactory.quartz.scheduler.*;
import com.charliechocolatefactory.quartz.scheduler.jobs.JobCart;


public class JobsDashboardAction {

	
	private Date date1;
	private String YourJob;
	
		private String status;
	 
		
		private List<String> runningJobs;
		private JobCart jc;
		private MainScheduler mc;
	 
		// all struts logic here
		public String execute() {
			System.out.println(getYourJob());
			
			System.out.println("Setting Up Scheduler");
			// Instantiate MainScheduler
			mc = new MainScheduler();
			
			// Create JobCart with the with the List of elements selected
			
			jc = new JobCart();
			jc.setJobList(new ArrayList<String>(Arrays.asList(this.getYourJob().split(","))));
			mc.setJobCart(jc);
			
			mc.startScheduler();
			
			
			try {
				
				if(mc.getSchedulerStatus().equals("01"))
					this.setStatus("Scheduler Started");
					this.setRunningJobs(mc.getRunningJobsList());
				
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
			
			return "SUCCESS";
	 
		}



		public String getStatus() {
			return status;
		}



		public void setStatus(String status) {
			this.status = status;
		}




		public Date getDate1() {
			return date1;
		}



		public void setDate1(Date date1) {
			this.date1 = date1;
		}



		public List<String> getRunningJobs() {
			return runningJobs;
		}



		public void setRunningJobs(List<String> runningJobs) {
			this.runningJobs = runningJobs;
		}



		public String getYourJob() {
			return YourJob;
		}



		public void setYourJob(String yourJob) {
			YourJob = yourJob;
		}
	}
	
