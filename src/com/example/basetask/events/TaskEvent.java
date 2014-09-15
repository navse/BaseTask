package com.example.basetask.events;

import com.example.basetask.model.EventEnum;

public class TaskEvent
{

	private EventEnum mEventType;

	public TaskEvent(EventEnum aEventType)
	{
		this.mEventType = aEventType;
	}

	public EventEnum getEvantType()
	{
		return mEventType;
	}

	public void setEvantType(EventEnum evantType)
	{
		this.mEventType = evantType;
	}

}
