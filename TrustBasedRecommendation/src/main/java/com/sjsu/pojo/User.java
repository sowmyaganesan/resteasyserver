package com.sjsu.pojo;

import java.util.List;

public class User {
	// public String id;
	public String name;
	public String email;
	public String city;
	public int zip;
	public String passwrd;
	public List<String> friendsList;
	
	public User()
	{
		
	}
	/*
	 * public String getId() { return id; } public void setId(String id) {
	 * this.id = id; }
	 */
	
	public String getName() {
		return name;
	}

	public String getPasswrd() {
		return passwrd;
	}

	public void setPasswrd(String passwrd) {
		this.passwrd = passwrd;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

	public List<String> getFriendsList() {
		return friendsList;
	}

	public void setFriendsList(List<String> friendsList) {
		this.friendsList = friendsList;
	}
	

}
