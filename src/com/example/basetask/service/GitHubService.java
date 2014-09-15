package com.example.basetask.service;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Context;

import com.activeandroid.query.Select;
import com.example.basetask.events.ApiErrorEvent;
import com.example.basetask.events.TaskEvent;
import com.example.basetask.events.ToastEvent;
import com.example.basetask.model.EventEnum;
import com.example.basetask.model.GitHubUser;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class GitHubService
{
	private GitHubApi mApi;
	private Bus mBus;

	public GitHubService(GitHubApi aApi, Bus aBus, Context aContext)
	{
		mApi = aApi;
		mBus = aBus;
	}

	@Subscribe
	public void onLoadUsers(TaskEvent event)
	{
		boolean hasData = (new Select().from(GitHubUser.class).count()) > 0;

		if (event.getEvantType() == EventEnum.RELOAD
				|| (event.getEvantType() == EventEnum.INIT && !hasData))
		{
			loadFromServer(1);
		}

	}

	void loadFromServer(final int id)
	{
		mApi.userList(id, new Callback<List<GitHubUser>>()
		{

			@Override
			public void failure(RetrofitError retrofitError)
			{
				mBus.post(new ApiErrorEvent(retrofitError));
			}

			@Override
			public void success(List<GitHubUser> arg0, Response response)
			{
				mBus.post(new LoadUsersEvent(arg0));
				if (!arg0.isEmpty())
				{
					loadFromServer(arg0.get(arg0.size() - 1).getUserId());
				} else
				{
					mBus.post(new ToastEvent("All users loaded"));
				}
			}
		});
	}
}
