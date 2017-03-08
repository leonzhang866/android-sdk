package com.yiche.ycanalytics.bean;

public class EventBean {
	private String content;
	private String eventType;
	private Long updateTime;
	private int eventId;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public EventBean() {
		super();
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}
	

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public EventBean(String content, String eventType, Long updateTime) {
		this.content = content;
		this.eventType = eventType;
		this.updateTime = updateTime;
	}

	
}
