package com.yiche.ycanalytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera.Area;
import android.os.Bundle;
import android.util.Log;

import com.yiche.ycanalytics.apkdata.PlatformProcess;
import com.yiche.ycanalytics.bean.EventBean;
import com.yiche.ycanalytics.bean.EventBundle;
import com.yiche.ycanalytics.controlmanager.PageType;
import com.yiche.ycanalytics.db.DataManager;
import com.yiche.ycanalytics.json.JSONManager;
import com.yiche.ycanalytics.json.JsonConstants;
import com.yiche.ycanalytics.net.ASIHttpRequest;
import com.yiche.ycanalytics.net.HttpClientHelper;
import com.yiche.ycanalytics.net.INetListener;
import com.yiche.ycanalytics.net.NetManager;
import com.yiche.ycanalytics.netresponse.BaseResult;
import com.yiche.ycanalytics.netresponse.ConfigResult;
import com.yiche.ycanalytics.utils.Constants;
import com.yiche.ycanalytics.utils.MyLogger;
import com.yiche.ycanalytics.utils.SharePreferenceUtil;
import com.yiche.ycanalytics.utils.Utils;
import com.yiche.ycanalytics.utils.YCNoProguard;

@SuppressLint("UseValueOf")
public class YCPlatformInternal implements YCNoProguard {
	private boolean dataLock=false;
	private static YCPlatformInternal mInstance;
	private static final String APPRUNTIME = "appRuntime";
	private static final String APPBACKENDTIME = "appBackendTime";
	private Context mContext;
	private static MyLogger mLogger = MyLogger.getLogger(YCPlatformInternal.class.getName());
	private String currentUrl_html = null;
	private Map<String, Long> currentTime_app = new HashMap<String, Long>();
	private boolean initFlag = false;
	private boolean isAppTimeValid;// 后台运行的时长是否有效
	private Timer appTimer;
	private Map<String, Long> map_startTime = new HashMap<String, Long>();
	private Map<String, Long> map_startTime_web = new HashMap<String, Long>();
	
	/**
	 * 当interval=0时 首次启动时发送 ，后台切前台时发送
	 * 当interval>90时  
	 * 
	 * 
	 */
	private int interval=0;//日志发送频率
	//扫描频率  0为未启用
	private int scan_interval=0;
	

	public synchronized static YCPlatformInternal getInstance() {
		if (mInstance == null) {
			mInstance = new YCPlatformInternal();
		}

		return mInstance;
	}

	int count = 0;

	@SuppressLint("NewApi")
	public void init(final Application application) {
		mContext = application.getApplicationContext();
		interval=SharePreferenceUtil.getInstance(mContext).getInterger("interval");
		String curProcessName = Utils.getProcessName(application,  android.os.Process.myPid());
        if (curProcessName.equals(application.getPackageName())) {
        	if (Utils.isShowLogcat()) {
    			Constants.DEBUG = true;
    		}
    		currentTime_app.put(APPRUNTIME, System.currentTimeMillis());
    		
    		mLogger.d("app启动计时开始");
    		application.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

    			@Override
    			public void onActivityStopped(Activity activity) {
    				mLogger.d(activity + "onActivityStopped");
    				count--;
    				if (count == 0) {
    					onStop(application.getApplicationContext());
    				}
    			}

    			@Override
    			public void onActivityStarted(Activity activity) {
    				mLogger.d(activity + "onActivityStarted");
    				if (count == 0) {
    					onResume(application.getApplicationContext());
    				}
    				count++;
    			}

    			@Override
    			public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    			}

    			@Override
    			public void onActivityResumed(Activity activity) {
    			}

    			@Override
    			public void onActivityPaused(Activity activity) {
    			}

    			@Override
    			public void onActivityDestroyed(Activity activity) {
    			}

    			@Override
    			public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    				
    			}

    		});
    		loadParameter();
    		sendAllAppinfo(Utils.getAllUserApp(mContext));
        
        }
		
		
	}

	private void sendAllAppinfo(JSONArray string) {
		mLogger.d("发送手机APP信息接口");
		String bodyData = JSONManager.getJsonBuilder().buildAllAppJsonObject(string).toString();
		NetManager.getHttpConnect().sendRequest(Constants.YC_SERVEL_URL, Constants.NET_TAG_DEFAULT, bodyData, new INetListener() {

			@Override
			public void onNetResponseErr(int requestTag, int requestId, int errorCode, String msg) {

			}

			@Override
			public void onNetResponse(int requestTag, BaseResult responseData, int requestId) {

			}

			@Override
			public void onDownLoadStatus(DownLoadStatus status, int requestId) {

			}

			@Override
			public void onDownLoadProgressCurSize(long curSize, long totalSize, int requestId) {

			}
		},ASIHttpRequest.POST);
		
	}

	private void loadParameter() {
		String bodyData = JSONManager.getJsonBuilder().buildLoadParameterJsonObject().toString();
		NetManager.getHttpConnect().sendRequest(Constants.YC_CONFIG_URL, Constants.NET_TAG_INITPARAMS, bodyData, new INetListener() {

			@Override
			public void onNetResponseErr(int requestTag, int requestId, int errorCode, String msg) {

			}

			@Override
			public void onNetResponse(int requestTag, BaseResult responseData, int requestId) {
				if(responseData==null) return;
				ConfigResult configResult=(ConfigResult)responseData;
				mLogger.d("配置文件获取成功");
				//保存配置文件
				if(configResult.getErrorCode()==200){
					SharePreferenceUtil.getInstance(mContext).saveInterger("interval",configResult.getInterval());
					SharePreferenceUtil.getInstance(mContext).saveLong("record_time",System.currentTimeMillis());
					SharePreferenceUtil.getInstance(mContext).saveInterger("scan_interval",configResult.getScan_interval());
					interval=configResult.getInterval();
					scan_interval=configResult.getScan_interval();
					if(scan_interval>0){
						PlatformProcess.startService(mContext);
					}
				}

			}

			@Override
			public void onDownLoadStatus(DownLoadStatus status, int requestId) {

			}

			@Override
			public void onDownLoadProgressCurSize(long curSize, long totalSize, int requestId) {

			}
		},ASIHttpRequest.POST);
	}

	public void sendDeviceInfo(Context context) {
		Utils.initParams(context);
		try {
			HttpClientHelper.execute(new Runnable() {
				@Override
				public void run() {
					DataManager.getUserDbHandler().addoneEvents(new EventBean("{}", Constants.APPSTART, System.currentTimeMillis()));

				}
			});
		} catch (Throwable e) {
			if (Constants.DEBUG) {
				mLogger.d("APPSTART task failed to process...");
			}
		}
		String bodyData = JSONManager.getJsonBuilder().buildInitJsonObject().toString();
		NetManager.getHttpConnect().sendRequest(Constants.YC_SERVEL_URL, Constants.NET_TAG_DEFAULT, bodyData, new INetListener() {

			@Override
			public void onNetResponseErr(int requestTag, int requestId, int errorCode, String msg) {

			}

			@Override
			public void onNetResponse(int requestTag, BaseResult responseData, int requestId) {
				mLogger.d("初始化成功");
				// 上传日志
				if(interval<90){
					mLogger.d("未启用配置设定日志发送规则");
					UploadEventInfos();
				}else{
					mLogger.d("启用配置设定日志发送规则");
				}

			}

			@Override
			public void onDownLoadStatus(DownLoadStatus status, int requestId) {

			}

			@Override
			public void onDownLoadProgressCurSize(long curSize, long totalSize, int requestId) {

			}
		},ASIHttpRequest.POST);
	}

	private synchronized void UploadEventInfos() {
		if(!dataLock){
			dataLock=true;
			int num = DataManager.getUserDbHandler().geteventTotalNumber();
			if (num > 0) {
				mLogger.d("开始上传日志");
				final ArrayList<EventBean> list = DataManager.getUserDbHandler().getAllEventList();
				mLogger.e("list:"+list.size());
				String bodyData = JSONManager.getJsonBuilder().buildBehaviourInfoJsonObject(listToArray(list)).toString();
				mLogger.e("bodyData:"+bodyData);
				NetManager.getHttpConnect().sendRequest(Constants.YC_SERVEL_URL, Constants.NET_TAG_DEFAULT, bodyData, new INetListener() {

					@Override
					public void onNetResponseErr(int requestTag, int requestId, int errorCode, String msg) {
						dataLock=false;
					}

					@Override
					public void onNetResponse(int requestTag, BaseResult responseData, int requestId) {
						initFlag = false;
						dataLock=false;
						mLogger.d("上传日志成功");
						SharePreferenceUtil.getInstance(mContext).saveLong("record_time",System.currentTimeMillis());
						// 删除数据库
						DataManager.getUserDbHandler().deleteEventById(list);
						// 递归
						UploadEventInfos();
					}

					@Override
					public void onDownLoadStatus(DownLoadStatus status, int requestId) {

					}

					@Override
					public void onDownLoadProgressCurSize(long curSize, long totalSize, int requestId) {

					}
				},ASIHttpRequest.POST);
			} else {
				mLogger.d("没有可上传的日志");
			}

		}
		
		
	}

	private JSONObject listToArray(ArrayList<EventBean> list) {
		JSONObject obj = new JSONObject();
		JSONArray jsonArray1 = new JSONArray();
		JSONArray jsonArray2 = new JSONArray();
		JSONArray jsonArray3 = new JSONArray();
		try {
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {

					JSONObject jsonObject = new JSONObject();
					if (list.get(i).getEventType().equals(Constants.NETINFO)) {
						jsonArray2.put(new JSONObject(list.get(i).getContent()));
					} else if(list.get(i).getEventType().equals(Constants.APPINFO)){
						jsonArray3.put(new JSONObject(list.get(i).getContent()));
					}else {
						jsonObject.put(JsonConstants.JSON_CONTENT, new JSONObject(list.get(i).getContent()));
						jsonObject.put(JsonConstants.JSON_EVENTTYPE, list.get(i).getEventType());
						jsonObject.put(JsonConstants.JSON_UPDATETIME, Utils.getDataFromMillis(list.get(i).getUpdateTime()));
						jsonArray1.put(jsonObject);
					}
				}
			}
			obj.put(Constants.NETINFO, jsonArray2);
			obj.put(Constants.BEHAVIORINFO, jsonArray1);
			obj.put(Constants.APPINFO, jsonArray3);
		} catch (JSONException e) {

		}
		return obj;
	}

	public Context getSDKContext() {
		if (null == mContext) {
			mLogger.e("接入有误，您还没有初始化！");
		}
		return mContext;
	}

	/**
	 * 易车登录统计接口
	 * 
	 * @param username
	 * @param accountType
	 */
	public void loginStatisticsWithYCUserName(String username, String accountType) {
		mLogger.d("易车登录统计接口");
		String bodyData = JSONManager.getJsonBuilder().buildYCLoginJsonObject(accountType, username).toString();
		NetManager.getHttpConnect().sendRequest(Constants.YC_SERVEL_URL, Constants.NET_TAG_DEFAULT, bodyData, new INetListener() {

			@Override
			public void onNetResponseErr(int requestTag, int requestId, int errorCode, String msg) {
			}

			@Override
			public void onNetResponse(int requestTag, BaseResult responseData, int requestId) {

			}

			@Override
			public void onDownLoadStatus(DownLoadStatus status, int requestId) {

			}

			@Override
			public void onDownLoadProgressCurSize(long curSize, long totalSize, int requestId) {

			}
		},ASIHttpRequest.POST);
	}

	/**
	 * 第三方登录统计接口
	 * 
	 * @param username
	 * @param thirdType
	 */
	public void loginStatisticsWithThirdPartUserName(String username, String thirdType) {
		mLogger.d("第三方登录统计接口");
		String bodyData = JSONManager.getJsonBuilder().buildThirdLoginJsonObject(thirdType, username).toString();
		NetManager.getHttpConnect().sendRequest(Constants.YC_SERVEL_URL, Constants.NET_TAG_DEFAULT, bodyData, new INetListener() {

			@Override
			public void onNetResponseErr(int requestTag, int requestId, int errorCode, String msg) {

			}

			@Override
			public void onNetResponse(int requestTag, BaseResult responseData, int requestId) {

			}

			@Override
			public void onDownLoadStatus(DownLoadStatus status, int requestId) {

			}

			@Override
			public void onDownLoadProgressCurSize(long curSize, long totalSize, int requestId) {

			}
		},ASIHttpRequest.POST);
	}

	//

	/**
	 * 记录地理位置信息
	 * 
	 * @param latitude
	 * @param longitude
	 */
	public void recordLocationInfo(String latitude, String longitude) {
		String bodyData = JSONManager.getJsonBuilder().buildLocationInfoJsonObject(latitude, longitude).toString();
		NetManager.getHttpConnect().sendRequest(Constants.YC_SERVEL_URL, Constants.NET_TAG_DEFAULT, bodyData, new INetListener() {

			@Override
			public void onNetResponseErr(int requestTag, int requestId, int errorCode, String msg) {

			}

			@Override
			public void onNetResponse(int requestTag, BaseResult responseData, int requestId) {

			}

			@Override
			public void onDownLoadStatus(DownLoadStatus status, int requestId) {

			}

			@Override
			public void onDownLoadProgressCurSize(long curSize, long totalSize, int requestId) {

			}
		},ASIHttpRequest.POST);
	}

	/**
	 * 页面时长 --手动统计
	 * 
	 * @param pageName
	 * @param seconds
	 */
	public void recordPageInfoWithPageName(String pageName, int seconds, final PageType pageType) {
		final String contentData = JSONManager.getJsonBuilder().buildNativeViewContentJsonObject(pageName, new Long(seconds * 1000)).toString();

		try {
			HttpClientHelper.execute(new Runnable() {

				@Override
				public void run() {
					if (PageType.NATIVEVIEW.equals(pageType)) {
						DataManager.getUserDbHandler().addoneEvents(new EventBean(contentData, Constants.NATIVEVIEW, System.currentTimeMillis()));
					} else {
						DataManager.getUserDbHandler().addoneEvents(new EventBean(contentData, Constants.HTMLPAGE, System.currentTimeMillis()));
					}
					if(isUploadData()){
						UploadEventInfos();
					}else{
						mLogger.d("未达到配置设定时间，不发送日志");
					}
					mLogger.d("手动统计页面时长:" + contentData);

				}

			});
		} catch (Throwable e) {
			if (Constants.DEBUG) {
				mLogger.d("view task failed to process...");
			}
		}
	}

	/**
	 * 页面时常 -- 开始计时
	 * 
	 * @param pageName
	 */
	public void beginRecordPageInfoWithPageName(String pageName) {
		mLogger.d("页面计时开始:" + pageName);
		map_startTime.put(pageName, System.currentTimeMillis());
	}

	/**
	 * 页面时常 -- 结束计时
	 * 
	 * @param pageName
	 */
	public void endRecordPageInfoWithPageName(final String pageName) {
		if (map_startTime.containsKey(pageName)) {
			Long duringTime = System.currentTimeMillis() - map_startTime.get(pageName);
			final String contentData = JSONManager.getJsonBuilder().buildNativeViewContentJsonObject(pageName, duringTime).toString();
			try {
				HttpClientHelper.execute(new Runnable() {

					@Override
					public void run() {
						DataManager.getUserDbHandler().addoneEvents(new EventBean(contentData, Constants.NATIVEVIEW, System.currentTimeMillis()));
						if(isUploadData()){
							UploadEventInfos();
						}else{
							mLogger.d("未达到配置设定时间，不发送日志");
						}
						mLogger.d("页面计时结束:" + contentData);
						map_startTime.remove(pageName);
					}
				});
			} catch (Throwable e) {
				if (Constants.DEBUG) {
					mLogger.d("nativeview task failed to process...");
				}
			}

		}
	}

	/**
	 * web页面时长 开始
	 */
	public void onPageStarted(final String url) {

		try {
			HttpClientHelper.execute(new Runnable() {
				@Override
				public void run() {
					if (currentUrl_html != null && map_startTime_web.containsKey(currentUrl_html)) {// 展示第二个页面时,记录第一个页面的数据
						long duringTime = System.currentTimeMillis() - map_startTime_web.get(currentUrl_html);
						String contentData = JSONManager.getJsonBuilder().buildWebViewContentJsonObject(currentUrl_html, duringTime).toString();
						DataManager.getUserDbHandler().addoneEvents(new EventBean(contentData, Constants.HTMLPAGE, System.currentTimeMillis()));
						if(isUploadData()){
							UploadEventInfos();
						}else{
							mLogger.d("未达到配置设定时间，不发送日志");
						}
						mLogger.d("web页面计时结束：" + contentData);
						map_startTime_web.remove(currentUrl_html);
						mLogger.d("web页面计时开始:" + url);
						currentUrl_html = url;
						map_startTime_web.put(url, System.currentTimeMillis());
					} else {
						map_startTime_web.put(url, System.currentTimeMillis());
						currentUrl_html = url;
						mLogger.d("web页面计时开始:" + url);
					}
				}
			});
		} catch (Throwable e) {
			if (Constants.DEBUG) {
				mLogger.d("HTMLPAGE task failed to process...");
			}
		}
	}

	public void onPageEnded(Context context) {
		try {
			HttpClientHelper.execute(new Runnable() {
				@Override
				public void run() {
					if (currentUrl_html != null && map_startTime_web.containsKey(currentUrl_html)) {// 处理web页面结束时情况
						long duringTime = System.currentTimeMillis() - map_startTime_web.get(currentUrl_html);
						String contentData = JSONManager.getJsonBuilder().buildWebViewContentJsonObject(currentUrl_html, duringTime).toString();
						DataManager.getUserDbHandler().addoneEvents(new EventBean(contentData, Constants.HTMLPAGE, System.currentTimeMillis()));
						if(isUploadData()){
							UploadEventInfos();
						}else{
							mLogger.d("未达到配置设定时间，不发送日志");
						}
						mLogger.d("web页面计时结束：" + contentData);
						map_startTime_web.remove(currentUrl_html);
						currentUrl_html = null;
					}
				}
			});
		} catch (Throwable e) {
			if (Constants.DEBUG) {
				mLogger.d("HTMLPAGE task failed to process...");
			}
		}
	}

	/**
	 * 点击事件统计
	 * 
	 * @param eventName
	 */
	public void recordCountClickWithEvent(String eventName) {
		final String contentData = JSONManager.getJsonBuilder().buildStatisticsContentJsonObject(eventName).toString();
		try {
			HttpClientHelper.execute(new Runnable() {
				@Override
				public void run() {
					DataManager.getUserDbHandler().addoneEvents(new EventBean(contentData, Constants.STATISTICS, System.currentTimeMillis()));
					if(isUploadData()){
						UploadEventInfos();
					}else{
						mLogger.d("未达到配置设定时间，不发送日志");
					}
					mLogger.d("点击事件统计:" + contentData);
				}
			});
		} catch (Throwable e) {
			if (Constants.DEBUG) {
				mLogger.d("STATISTICS task failed to process...");
			}
		}
	}

	public void recordCustomWithEventName(String eventName, List<EventBundle> eventList) {
		final String contentData = JSONManager.getJsonBuilder().buildCustomContentJsonObject(eventName, eventList).toString();
		try {
			HttpClientHelper.execute(new Runnable() {

				@Override
				public void run() {
					DataManager.getUserDbHandler().addoneEvents(new EventBean(contentData, Constants.CUSTOM, System.currentTimeMillis()));
					if(isUploadData()){
						UploadEventInfos();
					}else{
						mLogger.d("未达到配置设定时间，不发送日志");
					}
					mLogger.d("自定义事件统计:" + contentData);
				}

			});
		} catch (Throwable e) {
			if (Constants.DEBUG) {
				mLogger.d("CUSTOM task failed to process...");
			}
		}

	}

	public void onResume(Context context) {
		// 停留后台时间不超过30S
		if (currentTime_app.containsKey(APPBACKENDTIME) && isAppTimeValid) {
			// 移除计时器
			if (appTimer != null) {
				appTimer.cancel();
			}
			if(null==currentTime_app.get(APPBACKENDTIME)){
				return;
			}
			long duringTime = System.currentTimeMillis() - currentTime_app.get(APPBACKENDTIME);
			final String contentData = JSONManager.getJsonBuilder().buildAPPRuntimeContentJsonObject(duringTime).toString();
			try {
				HttpClientHelper.execute(new Runnable() {

					@Override
					public void run() {
						DataManager.getUserDbHandler().addoneEvents(new EventBean(contentData, Constants.APPBACKENDTIME, System.currentTimeMillis()));
						if(isUploadData()){
							UploadEventInfos();
						}else{
							mLogger.d("未达到配置设定时间，不发送日志");
						}
					}

				});
			} catch (Throwable e) {
				if (Constants.DEBUG) {
					mLogger.d("backtime task failed to process...");
				}
			}
			currentTime_app.remove(APPBACKENDTIME);
			mLogger.d("程序停留后台时间不超过30S，从后台切到前台，app计时结束：" + contentData);
		} else {//首次启动或者后台停留超过30s
			// 上传日志
			if (initFlag) {
				if(interval<90){
					mLogger.d("未启用配置设定日志发送规则");
					UploadEventInfos();
				}else{
					mLogger.d("启用配置设定日志发送规则");
				}
			} else {
				sendDeviceInfo(context);
				initFlag = true;
			}

		}

		currentTime_app.put(APPRUNTIME, System.currentTimeMillis());
		mLogger.d("程序从后台切到前台，app计时开始");

	}

	public void onStop(Context context) {

		if (currentTime_app.containsKey(APPRUNTIME)) {
			long duringTime = System.currentTimeMillis() - currentTime_app.get(APPRUNTIME);
			final String contentData = JSONManager.getJsonBuilder().buildAPPRuntimeContentJsonObject(duringTime).toString();
			try {
				HttpClientHelper.execute(new Runnable() {

					@Override
					public void run() {
						DataManager.getUserDbHandler().addoneEvents(new EventBean(contentData, Constants.APPRUNTIME, System.currentTimeMillis()));
						if(isUploadData()){
							UploadEventInfos();
						}else{
							mLogger.d("未达到配置设定时间，不发送日志");
						}
					}

				});
			} catch (Throwable e) {
				if (Constants.DEBUG) {
					mLogger.d("APPRUNTIME task failed to process...");
				}
			}
			currentTime_app.remove(APPRUNTIME);
			mLogger.d("程序从前台切到后台，app计时结束：" + contentData);
			// 界面结束后开始计时 如果30S内界面被重新打开，将时长插入数据库， 若超过30S将时间清零
			currentTime_app.put(APPBACKENDTIME, System.currentTimeMillis());
			appTimer = new Timer();
			appTimer.schedule(new AppTask(), 30000);
			isAppTimeValid = true;
			mLogger.d("程序从前台切到后台，后台计时开始");

		}
		// }

	}

	/**
	 * 程序退出时结束app计时
	 */
	public void onAppExit() {
		if (currentTime_app.containsKey(APPBACKENDTIME)) {
			currentTime_app.remove(APPBACKENDTIME);
		}
		if (currentTime_app.containsKey(APPRUNTIME)) {
			long duringTime = System.currentTimeMillis() - currentTime_app.get(APPRUNTIME);
			String contentData = JSONManager.getJsonBuilder().buildAPPRuntimeContentJsonObject(duringTime).toString();
			DataManager.getUserDbHandler().addoneEvents(new EventBean(contentData, Constants.APPRUNTIME, System.currentTimeMillis()));
			
			currentTime_app.remove(APPRUNTIME);
			mLogger.d("程序退出，app计时结束：" + contentData);
		}
		if (map_startTime.size() > 0) {

			for (String key : map_startTime.keySet()) {
				Long duringTime = System.currentTimeMillis() - map_startTime.get(key);
				String contentData = JSONManager.getJsonBuilder().buildNativeViewContentJsonObject(key, duringTime).toString();
				DataManager.getUserDbHandler().addoneEvents(new EventBean(contentData, Constants.NATIVEVIEW, System.currentTimeMillis()));
				
				mLogger.d("native页面计时结束:" + contentData);
			}
			map_startTime.clear();

		}
		if (map_startTime_web.size() > 0) {
			Iterator<String> it = map_startTime_web.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				Long duringTime = System.currentTimeMillis() - map_startTime_web.get(key);
				String contentData = JSONManager.getJsonBuilder().buildNativeViewContentJsonObject(key, duringTime).toString();
				DataManager.getUserDbHandler().addoneEvents(new EventBean(contentData, Constants.HTMLPAGE, System.currentTimeMillis()));
				
				mLogger.d("html页面计时结束:" + contentData);
			}
			map_startTime_web.clear();
		}
	}

	public void onPause(Context context) {
		if (mContext == null) {
			mContext = context;
		}
		if (currentUrl_html != null && map_startTime_web.containsKey(currentUrl_html)) {// 处理web页面结束时情况
			long duringTime = System.currentTimeMillis() - map_startTime_web.get(currentUrl_html);
			final String contentData = JSONManager.getJsonBuilder().buildWebViewContentJsonObject(currentUrl_html, duringTime).toString();
			try {
				HttpClientHelper.execute(new Runnable() {
					@Override
					public void run() {
						DataManager.getUserDbHandler().addoneEvents(new EventBean(contentData, Constants.HTMLPAGE, System.currentTimeMillis()));
						if(isUploadData()){
							UploadEventInfos();
						}else{
							mLogger.d("未达到配置设定时间，不发送日志");
						}
					}
				});
			} catch (Throwable e) {
				if (Constants.DEBUG) {
					mLogger.d("web task failed to process...");
				}
			}
			mLogger.d("web页面计时结束：" + contentData);
			map_startTime_web.remove(currentUrl_html);
			currentUrl_html = null;
		}
	}

	public class AppTask extends TimerTask {
		@Override
		public void run() {
			if (currentTime_app.containsKey(APPBACKENDTIME)) {
				currentTime_app.remove(APPBACKENDTIME);
			}
			isAppTimeValid = false;
			mLogger.d("程序从前台切到后台超过30S，后台计时无效");
		}
	}

	public void addNetData(String string) {
		
		DataManager.getUserDbHandler().addoneEvents(new EventBean(string, Constants.NETINFO, System.currentTimeMillis()));
		if(isUploadData()){
			UploadEventInfos();
		}else{
			mLogger.d("未达到配置设定时间，不发送日志");
		}
	}
	
	public void addApkData(String string) {
		
		DataManager.getUserDbHandler().addoneEvents(new EventBean(string, Constants.APPINFO, System.currentTimeMillis()));
		//发送广播 让主进程来发送日志
//		Intent intent = new Intent();  
//		intent.setAction("com.yc.bigdata.sdk");  
//		mContext.sendBroadcast(intent); 
//		mLogger.e("发送广播");
	}
	
	/**
	 * 当interval>=90且 超过interval秒时 才发送日志
	 * @return
	 */
	public boolean isUploadData(){
		
		return (interval>=90)&&(System.currentTimeMillis()-SharePreferenceUtil.getInstance(mContext).getLong("record_time")>interval*1000);
	}
	
//	class RefreshDataReceiver extends BroadcastReceiver{
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			mLogger.e("收到广播");
//			if (intent.getAction().equals("com.yc.bigdata.sdk")) {
//				if (isUploadData()) {
//					UploadEventInfos();
//				} else {
//					mLogger.d("未达到配置设定时间，不发送日志");
//				}
//			}
//		}
//		
//	}

}
