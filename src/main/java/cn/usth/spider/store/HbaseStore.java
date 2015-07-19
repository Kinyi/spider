package cn.usth.spider.store;

import java.io.IOException;
import java.util.Map;

import cn.usth.spider.Page;
import cn.usth.spider.utils.HbaseUtil;
import cn.usth.spider.utils.RedisUtil;

public class HbaseStore implements Storeable {
	RedisUtil redisUtil = new RedisUtil();

	public void store(Page page) {
		try {
			String goodsId = page.getGoodsId();
			Map<String, String> values = page.getValues();
			HbaseUtil.put(HbaseUtil.TABLE_NAME, goodsId, HbaseUtil.COLUMNFAMILY_1, HbaseUtil.COLUMNFAMILY_1_DATA_URL, page.getUrl());
			HbaseUtil.put(HbaseUtil.TABLE_NAME, goodsId, HbaseUtil.COLUMNFAMILY_1, HbaseUtil.COLUMNFAMILY_1_PIC_URL, values.get("pic"));
			HbaseUtil.put(HbaseUtil.TABLE_NAME, goodsId, HbaseUtil.COLUMNFAMILY_1, HbaseUtil.COLUMNFAMILY_1_PRICE, values.get("price"));
			HbaseUtil.put(HbaseUtil.TABLE_NAME, goodsId, HbaseUtil.COLUMNFAMILY_1, HbaseUtil.COLUMNFAMILY_1_TITLE, values.get("title"));
			
			HbaseUtil.put(HbaseUtil.TABLE_NAME, goodsId, HbaseUtil.COLUMNFAMILY_2, HbaseUtil.COLUMNFAMILY_2_PARAM, values.get("spec"));
			
			//把商品ID加入索引库中
			redisUtil.add("solr_index", goodsId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
