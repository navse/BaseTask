package com.example.basetask.service;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.database.Cursor;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.example.basetask.ApiErrorEvent;
import com.example.basetask.GitHubApi;
import com.example.basetask.events.ToastEvent;
import com.example.basetask.model.GitHubUser;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class GitHubService
{
	private GitHubApi mApi;
	private Bus mBus;

	static boolean LOAD_FROM_DB = true;

	public GitHubService(GitHubApi api, Bus bus)
	{
		mApi = api;
		mBus = bus;
	}

	@Subscribe
	public void onLoadUsers(TaskEvent event)
	{
		if (!event.getEvantName().equals(TaskEvent.RELOAD))
		{
			return;
		}

		if (GitHubService.LOAD_FROM_DB)
		{
			loadFromDb();
			// loadFromdbCursor();
			// loadPaging(0);
		} else
		{
			loadFromServer(1);
		}
	}

	void loadPaging(int offset)
	{
		if (offset == 0 && new Select().from(GitHubUser.class).count() == 0)
		{
			loadFromServer(1);
		} else
		{

			List<GitHubUser> list = new Select().from(GitHubUser.class)
					.orderBy("login COLLATE NOCASE ASC").limit(100)
					.offset(offset).execute();
			mBus.post(new LoadUsersEvent(list));
			if (list != null || list.size() == 100)
			{
				loadPaging(100);
			}
		}
	}

	void loadFromDb()
	{
		if ( new Select().from(GitHubUser.class).count() == 0)
		{
			loadFromServer(1);
		} else
		{

			List<GitHubUser> list = new Select().from(GitHubUser.class)
					.orderBy("login COLLATE NOCASE ASC").execute();

			mBus.post(new LoadUsersEvent(list));
		}
	}

	void loadFromdbCursor()
	{
		String query = new Select().from(GitHubUser.class)
				.orderBy("login COLLATE NOCASE ASC").toSql();

		Cursor cursor = ActiveAndroid.getDatabase().rawQuery(query, null);

		if (cursor.moveToFirst())
		{
			loadDbPage(cursor);
		} else
		{
			loadFromServer(1);
			cursor.close();
		}
	}

	void loadDbPage(Cursor cursor)
	{
		List<GitHubUser> list = new ArrayList<GitHubUser>(20);
		int counter = 20;
		boolean hasMore = true;
		while (counter > 0 && hasMore)
		{
			GitHubUser user = new GitHubUser();
			user.loadFromCursor(cursor);
			list.add(user);
			hasMore = cursor.moveToNext();
			counter--;
		}

		mBus.post(new LoadUsersEvent(list));
		if (hasMore)
		{
			loadDbPage(cursor);
		} else
		{
			cursor.close();
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
