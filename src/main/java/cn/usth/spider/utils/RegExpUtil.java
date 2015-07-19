package cn.usth.spider.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpUtil {

	public static String getValue(String content,String regexp){
		Pattern compile = Pattern.compile(regexp);
		Matcher matcher = compile.matcher(content);
		String value = null;
		if (matcher.find()) {
			value = matcher.group(1);
		}
		return value;
	}
}
