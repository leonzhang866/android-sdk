package com.wjj.ath.activity;

import com.yiche.ycanalytics.YCPlatform;
import com.yiche.ycanalytics.utils.Constants;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application{
	
	@Override
	public void onCreate() {
		super.onCreate();
		Constants.DEBUG=true;
		YCPlatform.init(this);
	}

}
