package com.wnc.itbook;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import translate.bean.WordExchange;
import translate.site.baidu.BaiduWordTranslate;

public class TestExpand {
	public static void main(String[] args) throws Exception {
		String word = "fragments";
		BaiduWordTranslate baiduWordTranslate = new BaiduWordTranslate(word + "");
		String apiLink = baiduWordTranslate.getApiLink();
		System.out.println(apiLink);
		JSONObject jsonObject0 = baiduWordTranslate.getJsonObject().getJSONObject("dict_result")
				.getJSONObject("simple_means").getJSONArray("symbols").getJSONObject(0);
		String string = jsonObject0.getString("ph_am");
		System.out.println("[" + string + "]");
		JSONArray jsonArray = jsonObject0.getJSONArray("parts");
		System.out.println(jsonArray);
		String means = "";
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			if (i != 0) {
				means += "\n" + jsonObject.getString("part") + jsonObject.getString("means");
			} else {
				means += jsonObject.getString("part") + jsonObject.getString("means");
			}
		}
		System.out.println(means.replaceAll("[\\[\\]\"]", ""));

		String orginal = baiduWordTranslate.getJsonObject().getJSONObject("dict_result").getJSONObject("collins")
				.getString("word_name");
		if (!word.equalsIgnoreCase(orginal)) {
			System.out.println("原型:" + orginal);
		}
		WordExchange wordExchange = baiduWordTranslate.getWordExchange();
		System.out.println(wordExchange);
	}
}
