package com.yiche.ycanalytics;

import com.yiche.ycanalytics.utils.YCNoProguard;

public class YCPlatformSettings implements YCNoProguard{
	private String appkey ;
	private String channelId;
	
	public String getAppkey() {
		return appkey;
	}
	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public YCPlatformSettings(String appkey, String channelId) {
		this.appkey = appkey;
		this.channelId = channelId;
	}
	public YCPlatformSettings() {
		super();
	}
	
	

}
