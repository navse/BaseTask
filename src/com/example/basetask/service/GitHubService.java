package com.example.basetask.service;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.activeandroid.query.Select;
import com.example.basetask.events.ApiErrorEvent;
import com.example.basetask.events.LoadUsersEvent;
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
	Context mContext;

	public GitHubService(GitHubApi aApi, Bus aBus, Context aContext)
	{
		mApi = aApi;
		mBus = aBus;
		mContext = aContext;
	}

	// TODO: handle network connection (service waiting for connection?)
	@Subscribe
	public void onLoadUsers(TaskEvent event)
	{
		boolean hasData = (new Select().from(GitHubUser.class).count()) > 0;

		if (event.getEvantType() == EventEnum.RELOAD
				|| (event.getEvantType() == EventEnum.INIT && !hasData))
		{
			loadFromServer(1);
		} else if (event.getEvantType() == EventEnum.INIT && hasData)
		{ // load more users if previously failed or interrupted.
			GitHubUser user = new Select().from(GitHubUser.class)
					.orderBy("remoteId DESC").executeSingle();
			if (user != null)
			{
				loadFromServer(user.getUserId());
			}
		}

	}

	void loadFromServer(final int id)
	{
		if (!checkConnection())
		{
			mBus.post(new ToastEvent("No internet connection."));
			return;
		}

		Log.i("BASE", "Loading users since " + id);
		mApi.userList(id, new Callback<List<GitHubUser>>()
		{

			@Override
			public void failure(RetrofitError retrofitError)
			{
				// TODO: handle 403
				Log.e("BASE", "Server error: " + retrofitError.getMessage());
				mBus.post(new ApiErrorEvent(retrofitError));
			}

			@Override
			public void success(List<GitHubUser> arg0, Response response)
			{
				mBus.post(new LoadUsersEvent(arg0));
				if (!arg0.isEmpty())
				{
					Log.i("BASE", "Users loaded.");
					loadFromServer(arg0.get(arg0.size() - 1).getUserId());
				} else
				{
					Log.i("BASE", "All users loaded");
					mBus.post(new ToastEvent("All users loaded"));
				}
			}
		});
	}

	private boolean checkConnection()
	{
		ConnectivityManager connMgr = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		return (networkInfo != null && networkInfo.isConnected());
	}
}
