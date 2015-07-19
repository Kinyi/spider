package cn.usth.spider.job;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

public class UrlManager {

	public static void main(String[] args) {
		
		Scheduler scheduler;
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
			String name = UrlJob.class.getName();
			JobDetail jobDetail = new JobDetail(name, Scheduler.DEFAULT_GROUP, UrlJob.class);
			CronTrigger cronTrigger = new CronTrigger(name, Scheduler.DEFAULT_GROUP, "0 00 00 * * ?");
			scheduler.scheduleJob(jobDetail, cronTrigger);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
