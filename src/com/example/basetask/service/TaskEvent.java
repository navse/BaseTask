package com.example.basetask.service;

public class TaskEvent {

	private String evantName;

	public static String CLEAR_DB = "clear_db";
	public static String RELOAD = "reload";

	public TaskEvent(String evantName) {
		this.evantName = evantName;
	}

	public String getEvantName() {
		return evantName;
	}

	public void setEvantName(String evantName) {
		this.evantName = evantName;
	}

}
