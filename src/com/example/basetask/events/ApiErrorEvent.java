package com.example.basetask.events;

import retrofit.RetrofitError;

public class ApiErrorEvent
{

	private String errorMessage = "Something went wrong..";

	public ApiErrorEvent(RetrofitError error)
	{
		errorMessage = error.getMessage();
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

}
