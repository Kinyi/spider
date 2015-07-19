package cn.usth.spider.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.usth.spider.utils.Config;

public class FixThreadPool implements ThreadPool {
	ExecutorService fixThreadPool = Executors.newFixedThreadPool(Config.nThread);

	public void execute(Runnable runnable) {
		fixThreadPool.execute(runnable);
	}

}
