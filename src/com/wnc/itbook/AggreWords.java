package com.wnc.itbook;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wnc.basic.BasicFileUtil;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

import translate.util.JsoupHelper;

public class AggreWords {
	static final String folder = "D:/用户目录/workspace-sts/ITBookTools/books/txt";
	static Map<String, Integer> wordsFreqMap = new HashMap<String, Integer>();

	public static void main(String[] args) throws Exception {
		String jsonResult = JsoupHelper.getJsonResult("https://news.qq.com/c/816guonei_3.htm?0.8970850546059621");
		System.out.println(jsonResult);
	}

	public static void main2(String[] args) {
		List<String> readFrom;
		List<String> patternStrings;
		for (File f : new File(folder).listFiles()) {
			System.out.println(f.getAbsolutePath());
			readFrom = FileOp.readFrom(f.getAbsolutePath());
			for (String string : readFrom) {
				patternStrings = PatternUtil.getAllPatternGroup(string, "[ ,]([a-z]+)");
				for (String word : patternStrings) {
					recordWord(word.toLowerCase());
				}
			}
		}
		output();
	}

	private static void output() {

		List<KeyValue> map2List = map2List();

		Collections.sort(map2List, new Comparator<KeyValue>() {
			@Override
			public int compare(KeyValue o1, KeyValue o2) {
				return o2.getV() - o1.getV();
			}
		});

		// System.out.println(map2List);
		for (KeyValue keyValue : map2List) {
			BasicFileUtil.writeFileString("aggrev.txt", keyValue.getK() + " " + keyValue.getV() + "\r\n", null, true);
		}
	}

	static class KeyValue {
		String k;
		int v;

		public KeyValue(String k, int v) {
			this.k = k;
			this.v = v;
		}

		public String getK() {
			return k;
		}

		public void setK(String k) {
			this.k = k;
		}

		public int getV() {
			return v;
		}

		public void setV(int v) {
			this.v = v;
		}

		@Override
		public String toString() {
			return "KeyValue [k=" + k + ", v=" + v + "]";
		}
	}

	private static List<KeyValue> map2List() {
		List<KeyValue> kvList = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : wordsFreqMap.entrySet()) {
			kvList.add(new KeyValue(entry.getKey(), entry.getValue()));
		}
		return kvList;
	}

	private static void recordWord(String word) {
		if (!wordsFreqMap.containsKey(word)) {
			wordsFreqMap.put(word, 1);
		} else {
			wordsFreqMap.put(word, 1 + wordsFreqMap.get(word));
		}
	}
}
