package com.example.basetask;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

import com.squareup.otto.Bus;

import android.support.v7.app.ActionBarActivity;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.main)
public class MainActivity extends ActionBarActivity {

	@AfterViews
	void init() {

	}
	

	@OptionsItem(R.id.action_settings)
	void menuSettings() {

	}

	@OptionsItem(R.id.action_refresh)
	void menuRefresh() {

	}
	
	BusProvider mBus;
	
	private Bus getBus()
	{
		if (mBus == null)
		{
			mBus = BusProvider_.getInstance_(this);
		}
		return mBus;
	}

}