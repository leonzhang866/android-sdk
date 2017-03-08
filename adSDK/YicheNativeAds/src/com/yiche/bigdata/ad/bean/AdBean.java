package com.yiche.bigdata.ad.bean;

import com.yiche.bigdata.ad.netresponse.BaseResult;
import com.yiche.bigdata.ad.utils.YCNoProguard;


public class AdBean extends BaseResult implements YCNoProguard{
	private String resourceId;//客户端唯一识别
	private String dvid;
	private int pid ;
	private int creativeId;
	private int type;
	private String[] picUrls;
	private String title;
	private String url;
	private String exposureTp;
	private String clickTp;
	public String getDvid() {
		return dvid;
	}
	public void setDvid(String dvid) {
		this.dvid = dvid;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getCreativeId() {
		return creativeId;
	}
	public void setCreativeId(int creativeId) {
		this.creativeId = creativeId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String[] getPicUrls() {
		return picUrls;
	}
	public void setPicUrls(String[] picUrls) {
		this.picUrls = picUrls;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public AdBean() {
		super();
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getExposureTp() {
		return exposureTp;
	}
	public void setExposureTp(String exposureTp) {
		this.exposureTp = exposureTp;
	}
	public String getClickTp() {
		return clickTp;
	}
	public void setClickTp(String clickTp) {
		this.clickTp = clickTp;
	}
	

}
