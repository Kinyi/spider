package cn.usth.spider.repository;

import cn.usth.spider.utils.RedisUtil;

public class RedisRepository implements Repository {

	RedisUtil redisUtils = new RedisUtil();
	private String lowkey = "spider.todo.low";
	private String heightkey = "spider.todo.height";
	
	public String poll() {
		String url = redisUtils.poll(heightkey);
		if (url == null) {
			return redisUtils.poll(lowkey);
		}
		return url;
	}

	public void add(String url) {
		redisUtils.add(lowkey, url);
	}

	public void addHeight(String nextUrl) {
		redisUtils.add(heightkey, nextUrl);
	}

}
