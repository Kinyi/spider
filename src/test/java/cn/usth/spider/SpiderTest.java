package cn.usth.spider;

import org.junit.Test;

import cn.usth.spider.download.HttpClientDownload;
import cn.usth.spider.process.JdProcess;
import cn.usth.spider.store.ConsoleStore;

public class SpiderTest {

	@Test
	public void testSpider() throws Exception {
		Spider spider = new Spider();
		//下载
		String url = "http://list.jd.com/list.html?cat=9987,653,655";
		spider.setDownloadable(new HttpClientDownload());
		spider.setProcessable(new JdProcess());
		spider.setStoreable(new ConsoleStore());
		Page page = spider.download(url);
//		System.out.println(page.getRawHtml());
		//解析
		spider.process(page);
//		System.out.println(page.getValues());
		//存储
		spider.store(page);
		spider.start();
	}
}
