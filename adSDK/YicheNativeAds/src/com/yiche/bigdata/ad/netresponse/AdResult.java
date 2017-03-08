package com.yiche.bigdata.ad.netresponse;

import java.util.List;

import com.yiche.bigdata.ad.bean.AdBean;

public class AdResult extends BaseResult{
	private String dvid;
	private List<AdBean> list;
	public AdResult(String dvid, List<AdBean> list) {
		super();
		this.dvid = dvid;
		this.list = list;
	}
	public AdResult() {
		super();
	}
	public String getDvid() {
		return dvid;
	}
	public void setDvid(String dvid) {
		this.dvid = dvid;
	}
	public List<AdBean> getList() {
		return list;
	}
	public void setList(List<AdBean> list) {
		this.list = list;
	}
	
	
	

}
   