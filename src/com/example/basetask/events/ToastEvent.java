package com.example.basetask.events;

public class ToastEvent
{
	private String message;

	public ToastEvent(String message)
	{
		this.message = message;
	}

	public String getMessage()
	{
		return message;
	}

}
