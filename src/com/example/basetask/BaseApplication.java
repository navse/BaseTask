package com.example.basetask;

import retrofit.RestAdapter;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.basetask.events.ApiErrorEvent;
import com.example.basetask.service.DbService;
import com.example.basetask.service.GitHubApi;
import com.example.basetask.service.GitHubService;
import com.example.basetask.util.AppConstants;
import com.squareup.otto.Subscribe;

public class BaseApplication extends com.activeandroid.app.Application
{

	private GitHubService mService;

	private BusProvider mBus;

	@Override
	public void onCreate()
	{
		super.onCreate();
		mBus = BusProvider_.getInstance_(getBaseContext());
		mService = new GitHubService(buildApi(), mBus, getApplicationContext());
		mBus.register(mService);

		mBus.register(this); // listen for "global" events

		startService();
	}

	private GitHubApi buildApi()
	{
		return new RestAdapter.Builder().setEndpoint(AppConstants.API_URL)
				.build().create(GitHubApi.class);
	}

	@Override
	public void onTerminate()
	{
		super.onTerminate();
		stopService();
	}

	@Subscribe
	public void onApiError(ApiErrorEvent event)
	{
		Toast.makeText(getApplicationContext(), event.getErrorMessage(),
				Toast.LENGTH_SHORT).show();

		Log.e("BaseApplication", event.getErrorMessage());
	}

	// Method to start the service
	public void startService()
	{
		startService(new Intent(getBaseContext(), DbService.class));
	}

	// Method to stop the service
	public void stopService()
	{
		stopService(new Intent(getBaseContext(), DbService.class));
	}

}
