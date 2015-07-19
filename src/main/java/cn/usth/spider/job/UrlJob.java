package cn.usth.spider.job;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cn.usth.spider.utils.RedisUtil;

public class UrlJob implements Job{
	RedisUtil redisUtils = new RedisUtil();

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("beginning execute");
		List<String> urls = redisUtils.lrange(RedisUtil.start_url, 0, -1);
		for (String url : urls) {
			redisUtils.add(RedisUtil.heightkey, url);
		}
	}

}
