package cn.usth.spider.utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONUtil {

	public static String getValue(String content,String attr){
		JSONArray jsonArray = new JSONArray(content);
		JSONObject jsonObject = jsonArray.getJSONObject(0);
		return jsonObject.get(attr).toString();
	}
}
