package cn.usth.spider.utils;

import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

public class HtmlUtil {

	public static String getText(TagNode rootnode,String xpath){
		String result = null;
		try {
			Object[] objects = rootnode.evaluateXPath(xpath);
			if (objects.length == 1) {
				TagNode node = (TagNode)objects[0];
				result = node.getText().toString();
			}
		} catch (XPatherException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String getAttributeByName(TagNode rootnode,String xpath,String attr){
		String result = null;
		try {
			Object[] objects = rootnode.evaluateXPath(xpath);
			if (objects.length == 1) {
				TagNode node = (TagNode)objects[0];
				result = node.getAttributeByName(attr);
			}
		} catch (XPatherException e) {
			e.printStackTrace();
		}
		return result;
	}
}
