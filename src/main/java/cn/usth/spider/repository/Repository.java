package cn.usth.spider.repository;

public interface Repository {

	String poll();

	void add(String url);

	void addHeight(String nextUrl);

}
