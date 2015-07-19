package cn.usth.spider.download;

import cn.usth.spider.Page;
import cn.usth.spider.utils.PageUtil;

public class HttpClientDownload implements Downloadable {

	public Page download(String url) {
		Page page = new Page();
		page.setUrl(url);
		page.setRawHtml(PageUtil.getContent(url));
		return page;
	}

}
