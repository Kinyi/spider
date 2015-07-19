package cn.usth.spider;
/**
 *  1.检查hbase是否开启--start-hbase.sh
	2.开启redis服务
		redis-server /etc/redis.conf
		redis-cli
		keys *
		llen slor_index
	3.爬取数据--Spider.java
	4.开启solr服务--solr-4.10.2\example/java -jar start.jar
	5.建立索引--SolrIndex.java
	6.开启jetty
	7.在浏览器中查看结果--localhost:8080/goods
 */
import java.net.InetAddress;
import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.usth.spider.download.Downloadable;
import cn.usth.spider.download.HttpClientDownload;
import cn.usth.spider.process.JdProcess;
import cn.usth.spider.process.Processable;
import cn.usth.spider.repository.QueueRepository;
import cn.usth.spider.repository.RedisRepository;
import cn.usth.spider.repository.Repository;
import cn.usth.spider.store.ConsoleStore;
import cn.usth.spider.store.HbaseStore;
import cn.usth.spider.store.Storeable;
import cn.usth.spider.threadpool.FixThreadPool;
import cn.usth.spider.threadpool.ThreadPool;
import cn.usth.spider.utils.Config;
import cn.usth.spider.utils.SleepUtil;

public class Spider {
	private Downloadable downloadable = new HttpClientDownload();
	private Processable processable;
	private Storeable storeable = new ConsoleStore();
	private Repository repository = new QueueRepository();
	private ThreadPool threadPool = new FixThreadPool();

	Logger logger = LoggerFactory.getLogger(getClass());
	
	public Spider(){
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
		CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.80.100:2181", retryPolicy);
		client.start();
		
		try {
			String ip = InetAddress.getLocalHost().getHostAddress();
			client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).withACL(Ids.OPEN_ACL_UNSAFE).forPath("/spider/"+ip,ip.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ThreadPool getThreadPool() {
		return threadPool;
	}

	public void setThreadPool(ThreadPool threadPool) {
		this.threadPool = threadPool;
	}

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	public Storeable getStoreable() {
		return storeable;
	}

	public void setStoreable(Storeable storeable) {
		this.storeable = storeable;
	}

	public Downloadable getDownloadable() {
		return downloadable;
	}

	public void setDownloadable(Downloadable downloadable) {
		this.downloadable = downloadable;
	}

	public Processable getProcessable() {
		return processable;
	}

	public void setProcessable(Processable processable) {
		this.processable = processable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.usth.spider.Downloadable#download(java.lang.String)
	 */
	public Page download(String url) {
		return this.downloadable.download(url);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.usth.spider.processable#process(cn.usth.spider.Page)
	 */
	public void process(Page page) {
		this.processable.process(page);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.usth.spider.Storeable#store(cn.usth.spider.Page)
	 */
	public void store(Page page) {
		this.storeable.store(page);
	}

	public void start() {
		check();
		logger.info("开始爬取数据");
		while (!Thread.currentThread().isInterrupted()) {
			final String url = this.repository.poll();
			if (url == null) {
				SleepUtil.sleep(Config.Millis_2);
			} else {
				this.threadPool.execute(new Runnable() {
					public void run() {
						Page page = Spider.this.download(url);
						Spider.this.process(page);
						List<String> targetUrls = page.getTargetUrls();
						for (String nextUrl : targetUrls) {
							if (nextUrl.startsWith("http://list.jd.com/")) {
								Spider.this.repository.addHeight(nextUrl);
							} else {
								Spider.this.repository.add(nextUrl);
							}
						}
						if (!url.startsWith("http://list.jd.com/")) {
							Spider.this.store(page);
						}
					}
				});
				SleepUtil.sleep(Config.Millis_1);
			}
		}
	}

	private void check() {
		if (processable == null) {
			String message = "请设置processable！";
			logger.error(message);
			throw new RuntimeException(message);
		}
		logger.info("========================================================");
		logger.info("downloadable:{}", downloadable.getClass().getName());
		logger.info("processable:{}", processable.getClass().getName());
		logger.info("storeable:{}", storeable.getClass().getName());
		logger.info("repository:{}", repository.getClass().getName());
		logger.info("threadPool:{}", threadPool.getClass().getName());
		logger.info("========================================================");
	}

	public void setSeedUrl(String url) {
		this.repository.add(url);
	}

	public static void main(String[] args) {
		String url = "http://list.jd.com/list.html?cat=9987,653,655";
		Config.setConf();
		Spider spider = new Spider();
		spider.setProcessable(new JdProcess());
//		spider.setRepository(new RandomRepository());
		spider.setRepository(new RedisRepository());
		spider.setStoreable(new HbaseStore());
		spider.setSeedUrl(url);
		spider.start();
	}

}
