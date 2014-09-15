package com.example.basetask.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.example.basetask.BusProvider_;
import com.example.basetask.events.LoadUsersEvent;
import com.example.basetask.events.TaskEvent;
import com.example.basetask.model.EventEnum;
import com.example.basetask.model.GitHubUser;
import com.squareup.otto.Subscribe;

public class DbService extends Service
{

	@Override
	public void onCreate()
	{
		super.onCreate();
		BusProvider_.getInstance_(getApplicationContext()).register(this);
	}

	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{

		return START_STICKY;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	@Subscribe
	public void onUsersLoaded(LoadUsersEvent event)
	{
		if (event.getUsers() != null)
		{
			ActiveAndroid.beginTransaction();
			try
			{
				for (GitHubUser user : event.getUsers())
				{
					user.save();
				}
				ActiveAndroid.setTransactionSuccessful();
			} finally
			{
				ActiveAndroid.endTransaction();
			}

		}
	}

	@Subscribe
	public void onUsersLoaded(TaskEvent event)
	{
		if (event.getEvantType() == EventEnum.RELOAD)
		{
			ActiveAndroid.beginTransaction();
			try
			{
				new Delete().from(GitHubUser.class).execute();
				ActiveAndroid.setTransactionSuccessful();
			} finally
			{
				ActiveAndroid.endTransaction();
			}
		}
	}

}
