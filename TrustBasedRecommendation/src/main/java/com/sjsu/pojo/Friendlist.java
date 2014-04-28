package com.sjsu.pojo;

import java.util.List;

public class Friendlist {
	String user;
	List<Friends> friends;
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public List<Friends> getFriends() {
		return friends;
	}
	public void setFriends(List<Friends> friends) {
		this.friends = friends;
	}
}
