package cn.usth.spider.utils;


import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {
	public static String start_url = "http://list.jd.com/list.html?cat=9987,653,655";
	
	public static String heightkey = "spider.todo.height";
	public static String lowkey = "spider.todo.low";
	
	
	JedisPool jedisPool = null;
	public RedisUtil(){
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(10);
		poolConfig.setMaxTotal(100);
		poolConfig.setMaxWaitMillis(10000);
		poolConfig.setTestOnBorrow(true);
		jedisPool = new JedisPool(poolConfig, "192.168.80.100", 6379);
	}
	
	public List<String> lrange(String key,int start,int end){
		Jedis resource = jedisPool.getResource();
		
		List<String> list = resource.lrange(key, start, end);
		jedisPool.returnResource(resource);
		return list;
		
	}
	
	public void add(String lowKey, String url) {
		Jedis resource = jedisPool.getResource();
		resource.lpush(lowKey, url);
		jedisPool.returnResource(resource);
	}
	public String poll(String key) {
		Jedis resource = jedisPool.getResource();
		String result = resource.lpop(key);
		jedisPool.returnResource(resource);
		return result;
	}
	
}
