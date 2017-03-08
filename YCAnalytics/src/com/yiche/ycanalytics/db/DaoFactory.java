package com.yiche.ycanalytics.db;

import com.yiche.ycanalytics.YCPlatformInternal;

import android.content.Context;

/**
 * 数据库工厂类
 * @author wanglirong
 *
 */
public class DaoFactory {
	
	private static DaoFactory mInstance = null;
	private UserDao userDao;
	
	/**
	 * 获取DaoFactory的实例
	 * @param context
	 * @return
	 */
	public synchronized static DaoFactory getInstance(Context context){ //该context由CP提供
		
		if(mInstance == null){
			mInstance = new DaoFactory();
		}
		
		return mInstance;
	}
	
	/**
	 * @constructor
	 */
	private DaoFactory() {
		userDao = new UserDao();
	}
	public IUserDao getUserDao(){
		return userDao;
	}
	
}
