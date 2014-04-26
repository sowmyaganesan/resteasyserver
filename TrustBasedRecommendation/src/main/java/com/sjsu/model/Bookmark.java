package com.sjsu.model;

public class Bookmark {
	String id;
	Item item;
	String tried;
	String status;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public String getTried() {
		return tried;
	}
	public void setTried(String tried) {
		this.tried = tried;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
