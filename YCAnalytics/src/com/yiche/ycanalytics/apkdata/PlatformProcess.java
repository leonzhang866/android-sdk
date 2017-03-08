package com.yiche.ycanalytics.apkdata;

import android.content.Context;
import android.content.Intent;

/**
 * Created by wanglirong on 2016/12/29.
 */

public class PlatformProcess {

	/**
	 * 方法: startService 描述: 初始化启动Service
	 */
	public static void startService(Context context) {
		boolean isRunning = Util.getRunningProcess(context, "yiche_sdk");
		if (!isRunning) {
			Intent intent = new Intent(context, StatisticsService.class);
			context.startService(intent);
		}

	}

}
