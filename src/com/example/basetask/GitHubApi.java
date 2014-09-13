package com.example.basetask;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

import com.example.basetask.model.GitHubUser;

public interface GitHubApi {

	@GET("/users")
	void userList(@Query("since") int since,
			Callback<List<GitHubUser>> callback);
}
