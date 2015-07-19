package cn.usth.spider.process;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.usth.spider.Page;
import cn.usth.spider.utils.HtmlUtil;
import cn.usth.spider.utils.JSONUtil;
import cn.usth.spider.utils.PageUtil;
import cn.usth.spider.utils.RegExpUtil;

public class JdProcess implements Processable {

	public void process(Page page) {
		HtmlCleaner htmlCleaner = new HtmlCleaner();
		TagNode rootNode = htmlCleaner.clean(page.getRawHtml());
		String url = page.getUrl();
		String startUrl = "http://list.jd.com";
		if (url.startsWith(startUrl)) {
			String nextUrl = HtmlUtil.getAttributeByName(rootNode, "//*[@id=\"J_topPage\"]/a[2]", "href");
			if (!nextUrl.startsWith("javascript:;")) {
				String fullUrl = startUrl+nextUrl.replace("&amp;", "&");
				page.addTargetUrl(fullUrl);
			}
			try {
				Object[] objects = rootNode.evaluateXPath("//*[@id=\"plist\"]/ul/li/div/div[4]/a");
				for (Object object : objects) {
					TagNode node = (TagNode)object;
					String phoneUrl = node.getAttributeByName("href");
					page.addTargetUrl(phoneUrl);
				}
			} catch (XPatherException e) {
				e.printStackTrace();
			}
			
		}else {
			parseProduct(page, rootNode);
		}
		
	}

	private void parseProduct(Page page, TagNode rootNode) {
		//标题
		String title = HtmlUtil.getText(rootNode, "//*[@id=\"name\"]/h1");
		page.addField("title", title);
		//图片
		String pic = HtmlUtil.getAttributeByName(rootNode, "//*[@id=\"spec-n1\"]/img", "src");
		page.addField("pic", pic);
		//价格
		String goodsId = RegExpUtil.getValue(page.getUrl(), "http://item.jd.com/(\\d+).html");
		String jdGoodsId = "jd_"+goodsId;
		page.setGoodsId(jdGoodsId);
		String url = "http://p.3.cn/prices/get?skuid=J_"+goodsId;
		String content = PageUtil.getContent(url);
		String price = null;
		if (!"skuids input error".equals(content)) {
			price = JSONUtil.getValue(content, "p");
		}
		page.addField("price", price);
		//规格
		try {
			Object[] objects = rootNode.evaluateXPath("//*[@id=\"product-detail-2\"]/table/tbody/tr");
			JSONArray jsonArray = new JSONArray();
			for (Object object : objects) {
				if (!"".equals(((TagNode)object).getText().toString().trim())) {
					JSONObject jsonObject = new JSONObject();
					TagNode trNode = (TagNode) object;
					Object[] thNode = trNode.evaluateXPath("/th");
					if (thNode.length == 1) {
						String th = ((TagNode) thNode[0]).getText().toString();
						jsonObject.put("name", th);
						jsonObject.put("value", "");
					} else {
						Object[] tdNode = trNode.evaluateXPath("/td");
						if (tdNode.length == 2) {
							String name = ((TagNode) tdNode[0]).getText().toString();
							String value = ((TagNode) tdNode[1]).getText().toString();
							jsonObject.put("name", name);
							jsonObject.putOnce("value", value);
						}
					}
					jsonArray.put(jsonObject);
				}
			}
			page.addField("spec", jsonArray.toString());
		} catch (XPatherException e) {
			e.printStackTrace();
		}
	}
}