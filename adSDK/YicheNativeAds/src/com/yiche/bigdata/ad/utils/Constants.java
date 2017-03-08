package com.yiche.bigdata.ad.utils;

import android.os.Environment;

public class Constants implements YCNoProguard{
	
	/**
     * 调试总开关。
     */
    public static boolean DEBUG = false;
	
	// 默认渠道
    public static final String YC_SDK_DEFAULT_CHANNEL = "1000000";
    //server地址 online
//    public static final String YC_SERVEL_URL = "http://echo.adsense.cig.com.cn/ssp/app/v2.0/post";
    public static final String YC_SERVEL_URL = "https://echo.ctags.cn/ssp/app/v2.0/post";
    //server地址 test
//    public static final String YC_SERVEL_URL = "http://124.250.36.222/ssp/app/v2.0/post";
    //曝光地址 test
//    public static final String YC_EXPOSE_URL = "http://i.mn.ctags.cn";
  //曝光地址online
    public static final String YC_EXPOSE_URL = "https://im.ctags.cn";
    //SDK版本
    //v1.1.0-->2  1.2.0-->3
	public static final String YC_SDK_VERSION = "3";
	
	public static final String ANDROID = "2";
	
	public static final int NET_TAG_DEFAULT = 8008;

	public static final int NET_TAG_EXPOSE = 1001;

//	public static final int NET_TAG_THIRD_EXPOSE = 1002;
	
	
	public static String mLogInfoDir = Environment
			.getExternalStorageDirectory() + "/bigdata/sdk/";
	public static String mLogInfoPath = mLogInfoDir + "LogInfos.txt";
	
	
	public static final String KEY = "7e6d3fb4665197eb";
	

}
