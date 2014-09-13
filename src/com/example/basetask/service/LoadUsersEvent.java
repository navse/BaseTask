package com.example.basetask.service;

import java.util.List;

import com.example.basetask.model.GitHubUser;

public class LoadUsersEvent {

	private List<GitHubUser> list;

	public LoadUsersEvent(List<GitHubUser> arg0) {
		list = arg0;
	}

	public LoadUsersEvent() {
		// TODO Auto-generated constructor stub
	}

	public List<GitHubUser> getUsers() {
		return list;
	}

}
