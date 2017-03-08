package com.yiche.ycanalytics.bean;

import com.yiche.ycanalytics.utils.YCNoProguard;

public class EventBundle implements YCNoProguard{
	private String key;
	private String value;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public EventBundle(String key, String value) {
		this.key = key;
		this.value = value;
	}
}
