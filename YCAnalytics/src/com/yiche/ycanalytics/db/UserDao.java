package com.yiche.ycanalytics.db;

import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;
import com.yiche.ycanalytics.YCPlatformInternal;
import com.yiche.ycanalytics.bean.EventBean;
import com.yiche.ycanalytics.utils.Constants;

/**
 * 数据库操作实体类
 * 
 * @author wanglirong
 * 
 */
public class UserDao implements IUserDao {
	/**
	 * 
	 */

	public static final String CONTENT = "content";
	public static final String EVENTTYPE = "eventType";
	public static final String UPDATETIME = "updateTime";

	/**
	 * 
	 */
	private UserSqliteHelper mSqliteHelper = null;

	

	private static final byte DATABASE_VERSION = 1;

	/**
	 * 
	 */
	private static final String DATABASE_NAME = "yiche.db";

	/**
	 * 
	 */
	private static final String TABLE_NAME = "user_events";

	/**
	 * 创建新数据库
	 */
	private static final String CREATE_SQL = "create table " + TABLE_NAME + "("
			+ BaseColumns._ID + " INTEGER PRIMARY KEY," + "content TEXT,"
			+ "eventType TEXT," + "updateTime LONG," + "time TEXT" + ")";

	/**
	 * 
	 */
	private static final String ADD_ONE_EVENT = "INSERT INTO "+TABLE_NAME+" (content,eventType,updateTime,time) "
			+ "VALUES (?,?,?,?)";

	



	/**
	 * 数据库辅助类来创建或打开数据库
	 * 
	 * @author wanglirong
	 * 
	 */
	public class UserSqliteHelper extends SQLiteOpenHelper {
		public UserSqliteHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_SQL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
		}

	}

	/**
	 * 打开数据库连接
	 */
	private synchronized void openConnection() {
		mSqliteHelper = new UserSqliteHelper(YCPlatformInternal.getInstance()
				.getSDKContext());
	}

	/**
	 * 关闭数据库连接
	 */
	private synchronized void closeConnection() {
		mSqliteHelper = null;
	}
	
	/**
	 * 插入一条event
	 * 
	 * @param eventBean
	 * @return
	 */
	public synchronized boolean addoneEvents(EventBean eventBean) {
		boolean executeSuccessed = true; // 判断是否正常执行完
		SQLiteDatabase db = null;
		try {
			openConnection();
			db = mSqliteHelper.getWritableDatabase();
			db.beginTransaction();
			SQLiteStatement statement = db.compileStatement(ADD_ONE_EVENT);
			DBTools.bindString(statement, 1, eventBean.getContent());
			DBTools.bindString(statement, 2, eventBean.getEventType());
			DBTools.bindLong(statement, 3, eventBean.getUpdateTime());
			statement.executeInsert();
			db.setTransactionSuccessful();
		} catch (Throwable e) {
			executeSuccessed = false;
			if(Constants.DEBUG){
				//e.printStackTrace();
			}
		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
			closeConnection();
		}
		return executeSuccessed;
	}

	@Override
	public void deleteEventById(ArrayList<EventBean> list) {
        SQLiteDatabase dbInstance = null;
        try
        {
            openConnection();
            dbInstance = mSqliteHelper.getWritableDatabase();
            for(int i=0;i<list.size();i++){
            	String queryStr = BaseColumns._ID+ " = '" +list.get(i).getEventId()  + "'";
                dbInstance.delete(TABLE_NAME, queryStr, null);
            }
        }
        catch (Throwable e)
        {
        	if(Constants.DEBUG){
				//e.printStackTrace();
			}
        }
        finally
        {
            if (dbInstance != null)
            {
                dbInstance.close();
            }
            closeConnection();
        }

	}

	@Override
	public ArrayList<EventBean> getAllEventList() {
		ArrayList<EventBean> list = new ArrayList<EventBean>();
		Cursor cursor = null;
		SQLiteDatabase dbInstance = null;
        try
        {
            openConnection();
            dbInstance = mSqliteHelper.getReadableDatabase();
			cursor = dbInstance.query(TABLE_NAME, null, null, null, null, null,
					BaseColumns._ID+" ASC");
			if (cursor != null && cursor.moveToFirst()) {
				do {
					EventBean eventBean = new EventBean();
					eventBean.setContent(cursor.getString(cursor.getColumnIndex(UserDao.CONTENT)));
					eventBean.setEventType(cursor.getString(cursor.getColumnIndex(UserDao.EVENTTYPE)));
					eventBean.setUpdateTime(cursor.getLong(cursor.getColumnIndex(UserDao.UPDATETIME)));
					eventBean.setEventId(cursor.getInt(cursor.getColumnIndex(BaseColumns._ID)));
					if (eventBean != null) {
						list.add(eventBean);
					}
				} while (cursor.moveToNext()&&list.size()<=500);
			}

		} catch (Throwable e) {
			if(Constants.DEBUG){
				//e.printStackTrace();
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (dbInstance != null)
            {
                dbInstance.close();
            }
			closeConnection();
		}
		return list;
	}

	@Override
	public int geteventTotalNumber() {
		int num=0;
		Cursor cursor = null;
		SQLiteDatabase dbInstance = null;
        try
        {
            openConnection();
            dbInstance = mSqliteHelper.getReadableDatabase();
			cursor = dbInstance.query(TABLE_NAME, null, null, null, null, null,
					null);
			if (cursor != null ) {
				num=cursor.getCount();
			}
		} catch (Throwable e ) {
			if(Constants.DEBUG){
				//e.printStackTrace();
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (dbInstance != null)
            {
                dbInstance.close();
            }
			closeConnection();
		}
		return num;
	}

}
