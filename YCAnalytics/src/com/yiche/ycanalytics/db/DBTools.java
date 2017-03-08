package com.yiche.ycanalytics.db;

import java.util.Date;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

/**
 * 数据库工具类
 * @author wanglirong
 *
 */
public abstract class DBTools {

	public static void bindString(SQLiteStatement statement, int index, String value) {
		
		if (value != null) {
			statement.bindString(index, value);
		} else {
			statement.bindNull(index);
		}
	}
	
	public static void bindDate(SQLiteStatement statement, int index, Date value) {
		
		if (value != null) {
			statement.bindLong(index, value.getTime());
		} else {
			statement.bindNull(index);
		}
	}
	
	public static void bindLong(SQLiteStatement statement, int index, long value) {
		
		statement.bindLong(index, value);
	}

	/**
	 * 获取日期
	 * @param cursor
	 * @param index
	 * @return
	 */
	public static Date getDate(Cursor cursor, int index) {
		if (cursor.isNull(index)) {
			return null;
		}
		return new Date(cursor.getLong(index));
	}
	
}
