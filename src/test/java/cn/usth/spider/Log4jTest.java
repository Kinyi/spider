package cn.usth.spider;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4jTest {

	@Test
	public void test() throws Exception {
		while (true) {
			Logger logger = LoggerFactory.getLogger(Log4jTest.class);
			logger.error("日志时间" + System.currentTimeMillis());
			TimeUnit.SECONDS.sleep(1);
		}
	}
}
