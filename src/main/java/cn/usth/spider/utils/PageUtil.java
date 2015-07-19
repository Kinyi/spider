package cn.usth.spider.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageUtil {
	private static Logger logger = LoggerFactory.getLogger(PageUtil.class);

	public static String getContent(String url){
		long start = System.currentTimeMillis();
		String result = null;
		HttpClientBuilder builder = HttpClients.custom();
		CloseableHttpClient client = builder.build();
		HttpGet request = new HttpGet(url);
		try {
			CloseableHttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			logger.info("下载页面成功,url为{},耗时{}毫秒",url,System.currentTimeMillis()-start);
		} catch (Exception e) {
			logger.info("下载页面失败,url为{}",url);
			e.printStackTrace();
		}
		return result;
	}
}
