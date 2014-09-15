package com.example.basetask;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

import android.support.v7.app.ActionBarActivity;

import com.example.basetask.events.TaskEvent;
import com.example.basetask.events.ToastEvent;
import com.example.basetask.model.EventEnum;
import com.squareup.otto.Bus;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.main)
public class MainActivity extends ActionBarActivity
{

	BusProvider_ mBus;

	@AfterViews
	void init()
	{

	}

	@OptionsItem(R.id.action_settings)
	void menuSettings()
	{
		getBus().post(new ToastEvent("Not implemented"));
	}

	@OptionsItem(R.id.action_refresh)
	void menuRefresh()
	{
		getBus().post(new TaskEvent(EventEnum.RELOAD));
	}

	@OptionsItem(R.id.action_about)
	void menuAbout()
	{
		getBus().post(new ToastEvent("Not implemented"));
	}

	private Bus getBus()
	{
		if (mBus == null)
		{
			mBus = BusProvider_.getInstance_(this);
		}
		return mBus;
	}

}
