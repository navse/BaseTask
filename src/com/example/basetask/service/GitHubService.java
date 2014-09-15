package com.example.basetask.service;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.activeandroid.loaders.ModelLoader;
import com.activeandroid.query.Select;
import com.example.basetask.GitHubApi;
import com.example.basetask.events.ApiErrorEvent;
import com.example.basetask.events.ToastEvent;
import com.example.basetask.model.GitHubUser;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class GitHubService
{
	private GitHubApi mApi;
	private Bus mBus;
	private Context mContext;
	private DbUserLoader mLoader;

	static boolean LOAD_FROM_DB = false;
	static int PAGE_SIZE = 100;
	static int LOADER_ID = 0;

	public GitHubService(GitHubApi aApi, Bus aBus, Context aContext)
	{
		mApi = aApi;
		mBus = aBus;
		mContext = aContext;
	}

	@Subscribe
	public void onLoadUsers(TaskEvent event)
	{
		if (!event.getEvantName().equals(TaskEvent.RELOAD))
		{
			return;
		}

		if (GitHubService.LOAD_FROM_DB
				&& new Select().from(GitHubUser.class).count() > 0)
		{
			loadFromDb(event.getmLoaderManager(), 0);
		} else
		{
			loadFromServer(1);
		}
	}

	void loadFromDb(LoaderManager aLoaderManager, int aOffset)
	{
		mLoader = new DbUserLoader(aLoaderManager);
		Bundle bundle = new Bundle();
		bundle.putInt("offset", aOffset);
		aLoaderManager.initLoader(LOADER_ID, bundle, mLoader);
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

	public class DbUserLoader implements
			LoaderManager.LoaderCallbacks<List<GitHubUser>>
	{
		LoaderManager mLoaderManager;
		int mOffset = 0;

		public DbUserLoader(LoaderManager aLoaderManager)
		{
			mLoaderManager = aLoaderManager;
		}

		@Override
		public Loader<List<GitHubUser>> onCreateLoader(int id, Bundle args)
		{
			mOffset = args.getInt("offset");
			return new ModelLoader<GitHubUser>(mContext, GitHubUser.class,
					new Select().from(GitHubUser.class)
							.orderBy("login COLLATE NOCASE ASC")
							.limit(PAGE_SIZE).offset(mOffset), true);
		}

		@Override
		public void onLoadFinished(Loader<List<GitHubUser>> loader,
				List<GitHubUser> data)
		{
			mBus.post(new LoadUsersEvent(data, true));

			mLoaderManager.destroyLoader(LOADER_ID);
			if (data.size() == 100)
			{
				loadFromDb(mLoaderManager, mOffset + PAGE_SIZE);
			}
		}

		@Override
		public void onLoaderReset(Loader<List<GitHubUser>> loader)
		{

		}
	}
}
