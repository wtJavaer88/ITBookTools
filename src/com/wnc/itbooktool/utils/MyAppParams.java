package com.wnc.itbooktool.utils;

import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicFileUtil;

public class MyAppParams {
	private static MyAppParams instance = new MyAppParams();
	public static String workSpace = "D:/用户目录/workspace-sts/ITBookTools/";
	public static String sqliteName = "jdbc:sqlite:" + workSpace + "/database/itdict.db";
	public static String logFolder = "D:/itbook/";
	public static String htmlFolder = "D:/itbook/reader/";
	static {
		BasicFileUtil.makeDirectory(logFolder);
		BasicFileUtil.makeDirectory(htmlFolder);
	}

	private MyAppParams() {

	}

	public static MyAppParams getInstance() {
		return instance;
	}

	public String getTodayLogPath() {
		return getLogPath(BasicDateUtil.getCurrentDateString());
	}

	public String getLogPath(String day) {
		return logFolder + day + ".txt";
	}

	public static String getHtmlPath() {
		return htmlFolder + "readlog.html";
	}

	public static String getCssPath() {
		return htmlFolder + "reader.css";
	}

	public static String getAlertExe() {
		return MyAppParams.workSpace + "alert.vbs";
	}
}
