package cn.usth.spider;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class HttpClientTest {
	String url = "http://www.crxy.cn";
	String ip = "202.107.233.85";
	int port = 8080;
	String username = "";
	String password = "";

	/**
	 * 代理ip：202.107.233.85
	 * 端口：8080
	 * 使用httpclient4实现代理
	 * @throws Exception
	 */
	@Test
	public void test1() throws Exception {
		HttpClientBuilder builder = HttpClients.custom();
		HttpHost proxy = new HttpHost(ip, port);
		CloseableHttpClient client = builder.setProxy(proxy).build();
		HttpUriRequest request = new HttpGet(url);
		CloseableHttpResponse response = client.execute(request);
		HttpEntity entity = response.getEntity();
		String result = EntityUtils.toString(entity);
		System.out.println(result);
	}
	
	/**
	 * 使用httpclient3实现代理
	 * @throws Exception
	 */
	@Test
	public void test2() throws Exception {
		HttpClient httpClient = new HttpClient();
		httpClient.getHostConfiguration().setProxy(ip, port);
		
		HttpMethod method = new GetMethod(url);
		httpClient.executeMethod(method);
		String result = new String(method.getResponseBody());
		System.out.println(result);
	}
	
	/**
	 * 使用httpclient4实现代理(带密码的代理)
	 * @throws Exception
	 */
	@Test
	public void test3() throws Exception {
		HttpClientBuilder builder = HttpClients.custom();
		
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		AuthScope authscope = new AuthScope(ip, port);
		Credentials credentials = new UsernamePasswordCredentials(username, password);
		credentialsProvider.setCredentials(authscope, credentials);
		
		CloseableHttpClient client = builder.setDefaultCredentialsProvider(credentialsProvider).build();
		HttpUriRequest request = new HttpGet(url);
		CloseableHttpResponse response = client.execute(request);
		HttpEntity entity = response.getEntity();
		String result = EntityUtils.toString(entity);
		System.out.println(result);
	}
	
	/**
	 * 使用httpclient3实现代理(带密码的代理)
	 * @throws Exception
	 */
	@Test
	public void test4() throws Exception {
		HttpClient httpClient = new HttpClient();
		
		org.apache.commons.httpclient.auth.AuthScope authscope = new org.apache.commons.httpclient.auth.AuthScope(ip, port);
		org.apache.commons.httpclient.Credentials credentials = new org.apache.commons.httpclient.UsernamePasswordCredentials(username, password);
		httpClient.getState().setProxyCredentials(authscope, credentials);
		
		HttpMethod method = new GetMethod(url);
		httpClient.executeMethod(method);
		String result = new String(method.getResponseBody());
		System.out.println(result);
	}
	
	/**
	 * 模拟登陆官网(www.crxy.cn)
	 * @throws Exception
	 */
	@Test
	public void testLogin() throws Exception {
		HttpClientBuilder builder = HttpClients.custom();
		
		CloseableHttpClient client = builder.build();
		HttpPost post = new HttpPost("http://www.crxy.cn/userlogining");
		
		List<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
		param.add(new BasicNameValuePair("username", "18681163341"));
		param.add(new BasicNameValuePair("password", "13266491017mac"));
		param.add(new BasicNameValuePair("loginType", "0"));
		param.add(new BasicNameValuePair("returnUrl", ""));
		HttpEntity entity = new UrlEncodedFormEntity(param);
		
		post.setEntity(entity);
		
		CloseableHttpResponse response = client.execute(post);
		int statusCode = response.getStatusLine().getStatusCode();

		if (statusCode == 302) {
			Header[] headers = response.getHeaders("location");
			String rediretoryUrl = null;
			if (headers.length == 1) {
				Header header = headers[0];
				rediretoryUrl = header.getValue();
			}
			post.setURI(new URI(rediretoryUrl));
			response = client.execute(post);
			HttpEntity entity2 = response.getEntity();
			String result = EntityUtils.toString(entity2);
			System.out.println(result);
		}
	}
	
	/**
	 * 使用Java代码执行js函数
	 * @throws Exception
	 */
	@Test
	public void testOnlineJs() throws Exception {
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		ScriptEngine engine = scriptEngineManager.getEngineByExtension("js");
		URL url2 = new URL("http://www.crxy.cn/js/util.js");
		InputStream stream = url2.openStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
		engine.eval(bufferedReader);
		
		Invocable invocable = (Invocable)engine;
		Object result = invocable.invokeFunction("sleep", "2");
		System.out.println(result);
	}
}
