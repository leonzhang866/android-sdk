package com.yiche.ycanalytics.utils;


public class Constants implements YCNoProguard{
	
	/**
     * 调试总开关。
     */
    public static boolean DEBUG = false;
	
	// 默认渠道
    public static final String YC_SDK_DEFAULT_CHANNEL = "1000000";
    //server地址 test
//    public static final String YC_SERVEL_URL = "https://atsdktest.ctags.cn";
    //server地址 online
    public static final String YC_SERVEL_URL = "https://atsdk.ctags.cn";
    //配置服务器地址
    public static final String YC_CONFIG_URL = "https://sdkserver.ctags.cn/data/cfg";
    //SDK版本 1.0.5-->6 | 1.1.0-->7 |1.2.0 -->8 |2.0.0 -->9  
	public static final String YC_SDK_VERSION = "9";
	public static final String YC_SDK_VERSIONNAME = "200";
	
	public static final String ANDROID = "2";
	
	public static final int NET_TAG_DEFAULT = 8008;
	
	public static final String APPSTART = "appStart";

	public static final String HTMLPAGE = "htmlPage";

	public static final String CUSTOM = "custom";
	
	public static final String NATIVEVIEW = "nativeView";
	
	public static final String STATISTICS = "statistics";
	
	public static final String APPRUNTIME = "appRuntime";
	
	public static final String APPBACKENDTIME = "appBackendTime";

	public static final String CHANNEL_NAME = "YC_CHANNEL";
	public static final String KEY_NAME = "YC_APPKEY";

	public static final String NETINFO = "netinfo";
	public static final String BEHAVIORINFO = "behaviorinfo";
	public static final String APPINFO = "appinfo";

	public static final int NET_TAG_INITPARAMS = 1;
	
	public static String CID = "unknow";

}
