package cn.usth.spider.repository;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueRepository implements Repository {

	private Queue<String> lowQueue = new ConcurrentLinkedQueue<String>();
	private Queue<String> heightQueue = new ConcurrentLinkedQueue<String>();
	
	public String poll() {
		String url = this.heightQueue.poll();
		if (url == null) {
			return this.lowQueue.poll();
		}
		return url;
	}

	public void add(String url) {
		this.lowQueue.add(url);
	}

	public void addHeight(String nextUrl) {
		this.heightQueue.add(nextUrl);
	}

}
