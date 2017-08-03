package com.team.noty.getshowrooms.adapter;

public class NavDrawerItem {
	

	private String id;
	private String title;
	private String rating;
	private String address;
	private String workTime;

	public NavDrawerItem(){}

	public NavDrawerItem(String title, String rating){
		this.title = title;
		this.rating = rating;
	}
	public NavDrawerItem(String id, String title, String rating, String address, String workTime){
		this.id = id;
		this.title = title;
		this.rating = rating;
		this.address = address;
		this.workTime = workTime;

	}

	public String getId() {
		return id;
	}

	public String getTitle(){
		return this.title;
	}

	public String getRating() {
		return rating;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getAddress() {
		return address;
	}

	public String getWorkTime() {
		return workTime;
	}
}
