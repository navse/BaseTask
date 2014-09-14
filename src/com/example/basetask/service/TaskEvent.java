package com.example.basetask.service;

import android.support.v4.app.LoaderManager;

public class TaskEvent
{

	private String mEventName;
	private LoaderManager mLoaderManager;

	public static String CLEAR_DB = "clear_db";
	public static String RELOAD = "reload";

	public TaskEvent(String aEventName)
	{
		this.mEventName = aEventName;
	}

	public TaskEvent(String aEventName, LoaderManager aLoaderManager)
	{
		this.mEventName = aEventName;
		this.setmLoaderManager(aLoaderManager);
	}

	public String getEvantName()
	{
		return mEventName;
	}

	public void setEvantName(String evantName)
	{
		this.mEventName = evantName;
	}

	public LoaderManager getmLoaderManager()
	{
		return mLoaderManager;
	}

	public void setmLoaderManager(LoaderManager aLoaderManager)
	{
		this.mLoaderManager = aLoaderManager;
	}

}
