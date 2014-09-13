package com.example.basetask.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.basetask.BusProvider_;
import com.example.basetask.SectionedUsersListAdapter;
import com.example.basetask.UserList;
import com.example.basetask.model.GitHubUser;
import com.example.basetask.service.LoadUsersEvent;
import com.example.basetask.service.TaskEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

@Deprecated
public class SectionedUserListFragment extends ListFragment implements
		OnItemClickListener
{
	List<GitHubUser> list = new ArrayList<GitHubUser>();

	private Bus mBus;
	private SectionedUsersListAdapter mAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		mAdapter = new SectionedUsersListAdapter(getActivity());
		setListAdapter(mAdapter);
		getListView().setOnItemClickListener(this);
		getListView().setFastScrollEnabled(true);
	}

	@Override
	public void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();

		getBus().register(this);
		// getBus().post(new LoadUsersEvent());

		getBus().post(new TaskEvent(TaskEvent.RELOAD));
	}

	private Bus getBus()
	{
		if (mBus == null)
		{
			mBus = BusProvider_.getInstance_(getActivity());
		}
		return mBus;
	}

	public void setBus(Bus bus)
	{
		mBus = bus;
	}

	@Subscribe
	public void onUsersLoaded(LoadUsersEvent event)
	{
		if (event.getUsers() == null)
		{
			return;
		}

		if (list == null)
		{
			list = event.getUsers();
		} else
		{
			list.addAll(event.getUsers());
		}

		UserList ul = new UserList(list);
		mAdapter.setNewModel(ul);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		getBus().unregister(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		Object o = mAdapter.getItem(arg2);
		if (o instanceof GitHubUser)
		{
			String url = ((GitHubUser) o).getHtml_url();
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);
		}

	}

}
