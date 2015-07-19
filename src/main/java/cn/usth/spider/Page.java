package cn.usth.spider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Page {

	private String url;
	private String goodsId;
	private String rawHtml;
	private List<String> targetUrls = new ArrayList<String>();
	private Map<String, String> values = new HashMap<String, String>();

	public List<String> getTargetUrls() {
		return targetUrls;
	}

	public void setTargetUrls(List<String> targetUrls) {
		this.targetUrls = targetUrls;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getRawHtml() {
		return rawHtml;
	}

	public void setRawHtml(String rawHtml) {
		this.rawHtml = rawHtml;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getValues() {
		return values;
	}

	public void setValues(Map<String, String> values) {
		this.values = values;
	}
	
	public void addField(String key,String value){
		this.values.put(key, value);
	}
	
	public void addTargetUrl(String url){
		this.targetUrls.add(url);
	}
}
