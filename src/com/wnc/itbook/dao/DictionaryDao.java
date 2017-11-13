package com.wnc.itbook.dao;

import java.sql.SQLException;

import com.wnc.basic.BasicFileUtil;

import db.DbExecMgr;
import db.DbField;
import db.DbFieldSqlUtil;
import translate.bean.WordExchange;

public class DictionaryDao {

	public static boolean insertExchange(String word_org, WordExchange wordExchange) throws SQLException {
		DbFieldSqlUtil util = new DbFieldSqlUtil("WORD_EXCHANGE", "");
		util.addInsertField(new DbField("WORD_ORG", word_org));
		util.addInsertField(new DbField("WORD_THIRD", wordExchange.getWord_third()));
		util.addInsertField(new DbField("WORD_DONE", wordExchange.getWord_done()));
		util.addInsertField(new DbField("WORD_PL", wordExchange.getWord_pl()));
		util.addInsertField(new DbField("WORD_ING", wordExchange.getWord_ing()));
		util.addInsertField(new DbField("WORD_PAST", wordExchange.getWord_past()));
		util.addInsertField(new DbField("WORD_EST", wordExchange.getWord_est()));
		util.addInsertField(new DbField("WORD_ER", wordExchange.getWord_er()));
		BasicFileUtil.writeFileString("exchange_sql.txt", util.getInsertSql() + "\r\n", null, true);
		System.out.println(util.getInsertSql());
		return DbExecMgr.execOnlyOneUpdate(util.getInsertSql());
	}
}
