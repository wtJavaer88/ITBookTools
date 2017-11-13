package com.wnc.itbook;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wnc.itbook.dao.DictionaryDao;
import com.wnc.tools.FileOp;

import db.DbExecMgr;
import translate.bean.WordExchange;
import translate.site.baidu.BaiduWordTranslate;

public class SeekITWords {
	public static void main(String[] args) {
		DbExecMgr.refreshCon("jdbc:sqlite:D:/database/itdict.db");
		int i = 0;
		List<String> readFrom = FileOp.readFrom("D:/用户目录/workspace-sts/ITBookTools/aggrev.txt");
		for (String string : readFrom) {
			if (i++ < 9500) {
				continue;
			}
			if (!DbExecMgr.isExistData("SELECT * FROM DICTIONARY WHERE TOPIC_WORD='" + string.split(" ")[0] + "'"))
				seek(string);
		}
	}

	private static void seek(String string) {
		String word = string.split(" ")[0];
		String weight = string.split(" ")[1];
		BaiduWordTranslate baiduWordTranslate = new BaiduWordTranslate(word + "");

		doDict(word, weight, baiduWordTranslate);
		doWE(word, baiduWordTranslate);
	}

	private static void doDict(String word, String weight, BaiduWordTranslate baiduWordTranslate) {
		try {
			System.out.println(word);
			// String apiLink = baiduWordTranslate.getApiLink();
			// System.out.println(apiLink);
			JSONObject jsonObject0 = baiduWordTranslate.getJsonObject().getJSONObject("dict_result")
					.getJSONObject("simple_means").getJSONArray("symbols").getJSONObject(0);
			String am = jsonObject0.getString("ph_am");
			System.out.println("读音: [" + am + "]");
			JSONArray jsonArray = jsonObject0.getJSONArray("parts");
			// System.out.println(jsonArray);
			String means = "";
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (i != 0) {
					means += "\n" + jsonObject.getString("part") + jsonObject.getString("means");
				} else {
					means += jsonObject.getString("part") + jsonObject.getString("means");
				}
			}
			means = means.replaceAll("[\\[\\]\"]", "");
			System.out.println(means);

			String orginal = getOrigin(baiduWordTranslate);
			if (!word.equalsIgnoreCase(orginal)) {
				System.out.println("原型:" + orginal);
			} else {
				orginal = "";
			}

			String sql = "INSERT INTO Dictionary(TOPIC_WORD, MEAN_CN, ACCENT, ORGINAL, WEIGHT) VALUES('"
					+ StringEscapeUtils.escapeSql(word) + "','" + StringEscapeUtils.escapeSql(means) + "','"
					+ StringEscapeUtils.escapeSql(am) + "','" + StringEscapeUtils.escapeSql(orginal) + "'," + weight
					+ ")";
			System.out.println(sql);
			DbExecMgr.execOnlyOneUpdate(sql);
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void doWE(String word, BaiduWordTranslate baiduWordTranslate) {
		try {
			WordExchange wordExchange = baiduWordTranslate.getWordExchange();
			System.out.println(wordExchange);
			if (wordExchange != null
					&& (isNotNull(wordExchange.getWord_done()) || isNotNull(wordExchange.getWord_done())
							|| isNotNull(wordExchange.getWord_er()) || isNotNull(wordExchange.getWord_est())
							|| isNotNull(wordExchange.getWord_ing()) || isNotNull(wordExchange.getWord_past())
							|| isNotNull(wordExchange.getWord_pl()) || isNotNull(wordExchange.getWord_third()))) {
				DictionaryDao.insertExchange(word, wordExchange);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean isNotNull(String str) {
		return StringUtils.isNotBlank(str) && !str.equals("null");
	}

	private static String getOrigin(BaiduWordTranslate baiduWordTranslate) throws Exception {
		String string = "";
		try {
			string = baiduWordTranslate.getJsonObject().getJSONObject("dict_result").getJSONObject("collins")
					.getString("word_name");
		} catch (Exception e) {
		}
		return string;
	}
}
