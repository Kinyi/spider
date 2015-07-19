package cn.usth.spider.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

	public static long Millis_1;
	public static long Millis_2;
	public static int nThread;
	
	public static void setConf(){
		Config config = new Config();
		InputStream stream = config.getClass().getResourceAsStream("/config.properties");
		Properties properties = new Properties();
		try {
			properties.load(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Millis_1 = Long.parseLong(properties.getProperty("spiderInterval"));
		Millis_2 = Long.parseLong(properties.getProperty("firstInterval"));
		nThread = Integer.parseInt(properties.getProperty("nThread"));
	}
	
/*	public static void main(String[] args) {
		setConf();
		System.out.println(Millis_2);
	}*/
}
