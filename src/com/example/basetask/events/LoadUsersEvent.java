package com.example.basetask.events;

import java.util.List;

import com.example.basetask.model.GitHubUser;

public class LoadUsersEvent
{

	private List<GitHubUser> mList;
	private boolean mIsFromDb = false;

	public LoadUsersEvent(List<GitHubUser> aList)
	{
		mList = aList;
	}

	public LoadUsersEvent(List<GitHubUser> aList, boolean aIsFromDb)
	{
		mList = aList;
		setIsFromDb(aIsFromDb);
	}

	public LoadUsersEvent()
	{
		// TODO Auto-generated constructor stub
	}

	public List<GitHubUser> getUsers()
	{
		return mList;
	}

	public boolean isFromDb()
	{
		return mIsFromDb;
	}

	public void setIsFromDb(boolean mIsFromDb)
	{
		this.mIsFromDb = mIsFromDb;
	}

}
