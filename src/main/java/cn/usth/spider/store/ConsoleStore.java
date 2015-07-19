package cn.usth.spider.store;

import cn.usth.spider.Page;

public class ConsoleStore implements Storeable{

	public void store(Page page) {
		System.out.println(page.getUrl()+"__"+page.getValues().get("price"));
	}

}
