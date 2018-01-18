package com.cnpc.framework.base.pojo;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cnpc.framework.utils.DateUtil;

public class QuartzJobFactory implements Job{

	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap dataMap=context.getMergedJobDataMap();
		
	}
	

}
