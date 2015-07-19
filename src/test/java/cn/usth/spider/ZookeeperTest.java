package cn.usth.spider;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

public class ZookeeperTest {

	@Test
	public void test1() throws Exception {
		Watcher watcher = new Watcher() {
			
			public void process(WatchedEvent event) {
				// TODO Auto-generated method stub
				
			}
		};
		ZooKeeper zooKeeper = new ZooKeeper("192.168.80.100:2181", 5000, watcher);
		String path = zooKeeper.create("/spider", "0".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println(path);
		while (true) {
			
		}
	}
	
	@Test
	public void test2() throws Exception {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
		CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.80.100:2181", retryPolicy);
		client.start();
		
		String path = client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).withACL(Ids.OPEN_ACL_UNSAFE).forPath("/spider","1".getBytes());
		System.out.println(path);
		
		while (true) {
			;
		}
	}
}
