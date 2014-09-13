package com.example.basetask.loader;

import java.util.List;

import com.example.basetask.model.GitHubUser;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public abstract class GiguHubLoader{// extends AsyncTaskLoader<Response<List<GitHubUser>>> {

	// protected ApiService service;
	// protected Response<GitHubUser> response;
	//
	// public GiguHubLoader(Context context) {
	// super(context);
	// Vibes app = (Vibes) context.getApplicationContext();
	// service = app.getApiService();
	// }
	//
	// @Override
	// public Response<List<GitHubUser>> loadInBackground() {
	// Response<GitHubUser> localResponse = new Response<GitHubUser>();
	//
	// try {
	// localResponse.setResult(callServerInBackground(service));
	// } catch(Exception e) {
	// localResponse.setError(e);
	// }
	//
	// response = localResponse;
	// return response;
	// }
	//
	// @Override
	// protected void onStartLoading() {
	// super.onStartLoading();
	// if(response != null) {
	// deliverResult(response);
	// }
	//
	// if(takeContentChanged() || response == null) {
	// forceLoad();
	// }
	// }
	//
	// @Override
	// protected void onReset() {
	// super.onReset();
	// response = null;
	// }
	//
	//
	// abstract protected Response<List<GitHubUser>> callServerInBackground(SecondLevelApiService api) throws Exception;

}


