package com.yiche.ycanalytics.apkdata;

import java.util.Timer;
import java.util.TimerTask;

import com.yiche.ycanalytics.YCPlatformInternal;
import com.yiche.ycanalytics.utils.SharePreferenceUtil;

import android.R.transition;
import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;



public class StatisticsService extends Service {
	
	public static final String TAG = "StatisticsService";
	private Timer timer;
	private AdwareManager adwareManager;
	private boolean needMonitor = true;
	private long scan_interval;

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		init(this);

		adwareManager = new AdwareManager(getApplicationContext());
		//初始化获取所有系统的预装应用
		CommonData.systemApps = Util.getSystemApps(getApplicationContext());
		
		IntentFilter intentFilter=new IntentFilter();
		intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
		intentFilter.addAction(Intent.ACTION_SCREEN_ON);
		registerReceiver(new ScreenReceiver(),intentFilter);
	}

	/**
	 * service设置为前台 提高优先级
	 * @param statisticsService
     */
	private void init(StatisticsService statisticsService) {
		Notification notification = new Notification();
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		notification.flags |= Notification.FLAG_NO_CLEAR;
		notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
		statisticsService.startForeground(0, notification);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (timer == null) {

			scan_interval = SharePreferenceUtil.getInstance(getApplicationContext()).getInterger("scan_interval");
			if (scan_interval > 2) {
				timer = new Timer();
				timer.scheduleAtFixedRate(new MonitorTask(), 0, scan_interval * 1000);
			}

		}
		
		return super.onStartCommand(intent, flags, startId);
	}

	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(timer!=null){
			timer.purge();
			timer.cancel();
		}
	}
	
	
	
	//定时逻辑都在这里面
	class MonitorTask extends TimerTask{
		@Override
		public void run() {
			if (needMonitor) {
				try {
					adwareManager.onDo();
				} catch (Throwable e) {
				}

			}
		}
		
	}

	public class ScreenReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(final Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				//锁屏停止监控
				needMonitor = false;
			} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
				//开启屏幕开始监控
				needMonitor = true;
			}
		}
	}
	
}
