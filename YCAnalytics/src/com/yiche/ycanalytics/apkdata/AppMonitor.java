package com.yiche.ycanalytics.apkdata;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

public class AppMonitor {
	
	public static String LastPackName = "";
	public static String RunningName = "";
	public static boolean needMonitor = true;
	
	/** 
	 * 方法: checkRunningApp 
	 * 描述: packageName == "" ,即当前可能处于桌面
	 * 参数: @param context
	 * 返回: void
	 * 异常 
	 */
	public static void checkRunningApp(Context mContext){

		ActivityManager.RunningAppProcessInfo currentInfo = null;
		Field field = null;
		int START_TASK_TO_FRONT = 2;
		String currentApp = "CurrentNULL";
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			try {
				field = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
			} catch (Exception e) {
				e.printStackTrace();
			}
			ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
			List<ActivityManager.RunningAppProcessInfo> appList = am.getRunningAppProcesses();
			for (ActivityManager.RunningAppProcessInfo app : appList) {
				if (app.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
					Integer state = null;
					try {
						state = field.getInt(app);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (state != null && state == START_TASK_TO_FRONT) {
						currentInfo = app;
						break;
					}
				}
			}
			if (currentInfo != null) {
				currentApp = currentInfo.processName;
			}
		} else {
			ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
			List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
			currentApp = tasks.get(0).processName;
		}
		//最后赋值给RunningName ，尽可能减少RunningName的值变动次数
		RunningName = currentApp;
	}
	
	/** 
	 * 方法: isSelfApp 
	 * 描述: 是否是广告SDK应用自身 true:是   false:不是
	 * 参数: @param context
	 * 参数: @param packageName
	 * 参数: @return
	 * 返回: boolean
	 * 异常 
	 */
	public static boolean isSelfApp(Context context,String packageName){
		return packageName.contains(context.getPackageName());
	}
	
	
	/**
	 * 获得属于桌面的应用的应用包名称
	 * 
	 * @return 返回包含所有包名的字符串列表
	 */
	public static List<String> getHomes(Context context) {
		List<String> names = new ArrayList<String>();
		PackageManager packageManager = context.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo ri : resolveInfo) {
			names.add(ri.activityInfo.packageName);
		}
		return names;
	}


	
}
