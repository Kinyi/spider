package cn.usth.spider;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4jTest2 {

	@Test
	public void test() throws Exception {
		Logger logger = LoggerFactory.getLogger(getClass());
		while (true) {
			String url = "http://item.jd.com/1217499.html";
			long start = System.currentTimeMillis();
			Thread.sleep(1000);
			long time = System.currentTimeMillis();
			logger.info("页面下载成功.url:[{}].耗时[{}]毫秒.当前时间戳:[{}]",url,time-start,time);
		}
	}
}
