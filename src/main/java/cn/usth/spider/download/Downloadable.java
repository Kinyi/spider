package cn.usth.spider.download;

import cn.usth.spider.Page;

public interface Downloadable {

	public abstract Page download(String url);

}