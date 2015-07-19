package cn.usth.spider.watcher;

import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

public class SpiderWatcher implements Watcher{
	CuratorFramework client;
	List<String> childrenPath;
	
	public SpiderWatcher(){
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
		client = CuratorFrameworkFactory.newClient("192.168.80.100:2181", retryPolicy);
		client.start();
		
		try {
			childrenPath = client.getChildren().usingWatcher(this).forPath("/spider");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void process(WatchedEvent event) {
		try {
			List<String> newChildrenPath = client.getChildren().usingWatcher(this).forPath("/spider");
			for (String node : newChildrenPath) {
				if (!childrenPath.contains(node)) {
					System.out.println("新增节点IP:"+node);
				}
			}
			for (String node : childrenPath) {
				if (!newChildrenPath.contains(node)) {
					System.out.println("移除节点IP:"+node);
					//TODO--给管理员发送邮件或者短信
				}
			}
			this.childrenPath = newChildrenPath;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		SpiderWatcher spiderWatcher = new SpiderWatcher();
		spiderWatcher.run();
	}

	private void run() {
		while (true) {
			;
		}
	}
}
