package cn.usth.spider.utils;

import java.util.concurrent.TimeUnit;

public class SleepUtil {

	public static void sleep(long time){
		try {
			TimeUnit.SECONDS.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
