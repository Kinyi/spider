package cn.usth.spider.repository;

import java.util.Queue;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import cn.usth.spider.utils.DomainUtil;

public class RandomRepository implements Repository {
	TreeMap<String, Queue<String>> map = new TreeMap<String, Queue<String>>();
	Random random = new Random();

	public String poll() {
		String[] array = map.keySet().toArray(new String[0]);
		int index = random.nextInt(array.length);
		String url = map.get(array[index]).poll();
		return url;
	}

	public void add(String url) {
		String topDomain = DomainUtil.getTopDomain(url);
		Queue<String> queue = map.get(topDomain);
		if (queue == null) {
			queue = new ConcurrentLinkedQueue<String>();
			map.put(topDomain, queue);
		}
		queue.add(url);
	}

	public void addHeight(String nextUrl) {
		String topDomain = DomainUtil.getTopDomain(nextUrl);
		Queue<String> queue = map.get(topDomain);
		if (queue == null) {
			queue = new ConcurrentLinkedQueue<String>();
			map.put(topDomain, queue);
		}
		queue.add(nextUrl);
	}

}
