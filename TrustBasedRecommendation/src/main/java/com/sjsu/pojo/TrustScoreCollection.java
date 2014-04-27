package com.sjsu.pojo;

public class TrustScoreCollection {

	String user;
	String friend;
	String category;
	Double trustscore;
	String explicit;
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getFriend() {
		return friend;
	}
	public void setFriend(String friend) {
		this.friend = friend;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Double getTrustscore() {
		return trustscore;
	}
	public void setTrustscore(Double trustscore) {
		this.trustscore = trustscore;
	}
	public String getExplicit() {
		return explicit;
	}
	public void setExplicit(String explicit) {
		this.explicit = explicit;
	}
	
}
