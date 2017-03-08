package com.yiche.ycanalytics;

import java.util.List;

import android.app.Application;
import android.content.Context;
import com.yiche.ycanalytics.apkdata.PlatformProcess;
import com.yiche.ycanalytics.bean.EventBundle;
import com.yiche.ycanalytics.controlmanager.PageType;
import com.yiche.ycanalytics.utils.Utils;
import com.yiche.ycanalytics.utils.YCNoProguard;

public class YCPlatform implements YCNoProguard{
	
	private YCPlatform()
    {
		
    }

    private static YCPlatformInternal mPlatformInternal = YCPlatformInternal.getInstance();
	
	//------初始化-------
    
	/**
	 * 初始化SDK 
	 * @param appkey 由易车分配
	 * @param channelId 渠道号
	 */
	public static void init(Application application){
		try{
	        mPlatformInternal.init(application);
		}catch(Throwable t){
			
		}
		
	}
	/**
	 * 易车账号登录统计
	 *@param username (必填) 易车账号.
	 *@param accountType 账号类型（手机号，邮箱，普通用户名）.
	 *@return void.
	 */
	public static void loginStatisticsWithYCUserName(String username,String accountType){
		try{
			mPlatformInternal.loginStatisticsWithYCUserName(username,accountType);
		}catch(Throwable t){
			
		}
		
	}
	
	
	/** 
	 * 第三方账号登录统计.
	 @param userName (必填) 可填登录账号或第三方登录成功后返回的唯一标识.
	 @param thirdType  登录方式，建议使用拼音缩写，如：wx表示微信, qq表示QQ ,wb表示微博
	 @return void.
	 */
	public static void loginStatisticsWithThirdPartUserName(String username,String thirdType){
		try{
			mPlatformInternal.loginStatisticsWithThirdPartUserName(username, thirdType);
		}catch(Throwable t){
			
		}
		
	}
	
	//------页面记时----单位秒-------------
	
	/**
	 * 手动记录页面时长
	 * @param pageName 页面名称
	 * @param seconds 时间
	 */
	public static void recordPageInfoWithPageName(String pageName ,int seconds,PageType pageType){
		try{
			mPlatformInternal.recordPageInfoWithPageName(pageName,seconds,pageType);
		}catch(Throwable t){
			
		}
		
	}
	/** 
	 * 自动页面时长统计, 开始记录某个页面展示时长.
	 使用方法：必须配对调用beginRecordPageInfoWithPageName:和endRecordPageInfoWithPageName:两个函数来完成自动统计，若只调用某一个函数不会生成有效数据。
	 在该页面展示时调用beginRecordPageInfoWithPageName:，当退出该页面时调用endRecordPageInfoWithPageName:
	 @param pageName (必填)统计的页面名称。(禁用中文)
	 @return void.
	 */
	public static void beginRecordPageInfoWithPageName(String pageName){
		try{
			mPlatformInternal.beginRecordPageInfoWithPageName(pageName);
		}catch(Throwable t){
			
		}
		
	}
	
	/** 自动页面时长统计, 结束记录某个页面展示时长.
	 使用方法：必须配对调用beginRecordPageInfoWithPageName:和endRecordPageInfoWithPageName:两个函数来完成自动统计，若只调用某一个函数不会生成有效数据。
	 在该页面展示时调用beginRecordPageInfoWithPageName:，当退出该页面时调用endRecordPageInfoWithPageName:
	 @param pageName (必填)统计的页面名称。(禁用中文)
	 @return void.
	 */
	public static void endRecordPageInfoWithPageName(String pageName){
		try{
			mPlatformInternal.endRecordPageInfoWithPageName(pageName);
		}catch(Throwable t){
			
		}
		
	}
	
	
	//---------html页面统计-------
	

	
	/** 
	 * webview页面 计时开始时调用   
	 @return void
	 */
	public static void onPageStarted(String url){
		try{
			mPlatformInternal.onPageStarted(url);
		}catch(Throwable t){
			
		}
		
	}


	
	//---------点击次数统计-----------
	/** 点击次数统计
	 @param  eventName 事件内容(禁用中文).
	 @return void
	 */
	public static void recordCountClickWithEvent(String eventName){
		try{
			mPlatformInternal.recordCountClickWithEvent(eventName);
		}catch(Throwable t){
			
		}
		
	}


	//---------------自定义内容统计-----------
	/** 自定义内容统计
	 @param  eventName 事件名称.(禁用中文).
	 @param  eventDic 事件内容（key－value）.
	 @return void
	 */
	public static void recordCustomWithEventName(String eventName,List<EventBundle> eventList ){
		try{
			mPlatformInternal.recordCustomWithEventName(eventName,eventList );
		}catch(Throwable t){
			
		}
	}
	
	
	//--------------地理位置统计----------------
	/**
	 * 地理位置统计
	 * @param latitude
	 * @param longitude
	 */
	public static void recordLocationInfo(String latitude,String longitude){
		try{
			mPlatformInternal.recordLocationInfo(latitude, longitude);
		}catch(Throwable t){
			
		}
	}
	
	public static void onPause(Context contex){
		try{
			mPlatformInternal.onPause(contex);
		}catch(Throwable t){
			
		}
		
	}
	
	public static void onPageEnded(Context contex) {
		try{
			mPlatformInternal.onPageEnded(contex);
		}catch(Throwable t){
			
		}
	}
	
	
	public static void onAppExit(Context contex){
		try{
			mPlatformInternal.onAppExit();
		}catch(Throwable t){
			
		}
	}
	
	/**
	 * 添加网络统计日志
	 */
	public static void addNetData(String string){
		try{
			mPlatformInternal.addNetData(string);
		}catch(Throwable t){
			
		}
	}
	
	/**
	 * 添加apk运行信息日志
	 */
	public static void addApkData(String string){
		try{
			mPlatformInternal.addApkData(string);
		}catch(Throwable t){
			
		}
	}
	
	


}
