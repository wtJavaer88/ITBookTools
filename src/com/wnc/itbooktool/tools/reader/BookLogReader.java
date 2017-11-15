package com.wnc.itbooktool.tools.reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicFileUtil;
import com.wnc.basic.BasicStringUtil;
import com.wnc.itbooktool.BookLog;
import com.wnc.itbooktool.utils.MyAppParams;
import com.wnc.itbooktool.utils.TextUtils;
import com.wnc.run.RunCmd;
import com.wnc.tools.FileOp;

import db.DbExecMgr;

public class BookLogReader {
	private final int latelyDays = 3;

	// 未知单词
	Set<String> unKnown = new HashSet<String>();
	// 顺序存储单词id
	List<Integer> inSentenceList = new ArrayList<Integer>();
	// 单独存储单词id和时间关系
	Map<Integer, String> logTimeMap = new HashMap<Integer, String>();
	// 重复的id集合, 用于后期特别处理前端显示
	Set<Integer> duplicateSet = new HashSet<Integer>();

	public static void main(String[] args) {
		DbExecMgr.refreshCon(MyAppParams.sqliteName);
		BookLogReader bookLogReader = new BookLogReader();
		bookLogReader.parseLog2Html();
		System.out.println("成功生成html");
		bookLogReader.openHtml();
	}

	public BookLogReader() {

	}

	private void parseLog2Html() {
		List<String> readFrom = getLatelyLogs();
		for (String string : readFrom) {
			BookLog blog = JSONObject.parseObject(string, BookLog.class);
			String topic = blog.getTopic();
			if (BasicStringUtil.isNotNullString(topic)) {
				int parseInt = Integer.parseInt(topic);
				if (inSentenceList.contains(parseInt)) {
					duplicateSet.add(parseInt);
				}
				inSentenceList.add(parseInt);
				logTimeMap.put(parseInt, blog.getTime());
			} else if (TextUtils.isSingleWord(blog.getContent())) {
				unKnown.add(blog.getContent());
			}
		}

		Map<Integer, Map> explainMap = getExplainMap();

		new HtmlCreator().createHtml(unKnown, duplicateSet, inSentenceList, explainMap);
	}

	private Map<Integer, Map> getExplainMap() {
		String join = StringUtils.join(inSentenceList, ",");
		String sql = "SELECT ID,TOPIC_WORD,MEAN_CN,WEIGHT FROM dictionary WHERE id IN (" + join + ")";
		Map selectAllSqlMap = DbExecMgr.getSelectAllSqlMap(sql);

		Map<Integer, Map> explainMap = new HashMap<Integer, Map>();
		for (int i = 1; i <= selectAllSqlMap.size(); i++) {
			Map rowMap = (Map) selectAllSqlMap.get(i);
			int wordId = Integer.parseInt(rowMap.get("ID").toString());
			rowMap.put("TIME", logTimeMap.get(wordId));
			explainMap.put(wordId, rowMap);
		}
		return explainMap;
	}

	private void openHtml() {
		RunCmd.runCommand("cmd /c \"C:\\Users\\wnc\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe\" "
				+ MyAppParams.getHtmlPath());
	}

	private List<String> getLatelyLogs() {
		List<String> readFrom = new ArrayList<>();
		for (int i = 0; i < latelyDays; i++) {
			String day = BasicDateUtil.getDateBeforeDayDateString(BasicDateUtil.getCurrentDateString(), i);
			String logPath = MyAppParams.getInstance().getLogPath(day);
			if (BasicFileUtil.isExistFile(logPath))
				readFrom.addAll(FileOp.readFrom(logPath));
		}
		return readFrom;
	}
}
