package com.example.basetask.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.activeandroid.content.ContentProvider;
import com.example.basetask.BusProvider_;
import com.example.basetask.R;
import com.example.basetask.adapters.SectionedListAdapter;
import com.example.basetask.adapters.UserList;
import com.example.basetask.events.TaskEvent;
import com.example.basetask.model.GitHubUser;
import com.example.basetask.service.LoadUsersEvent;
import com.example.basetask.widget.IndexableListView;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class IndexableUserListFragment extends ListFragment implements
		OnItemClickListener
{

	IndexableListView list;

	List<GitHubUser> mUsersList = new ArrayList<GitHubUser>();

	BusProvider_ mBus;

	SectionedListAdapter mAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		init();
		// initCursorAdapter();
	}

	void init()
	{
		mAdapter = new SectionedListAdapter(getActivity());

		list.setAdapter(mAdapter);
		list.setFastScrollEnabled(true);
		list.setOnItemClickListener(this);

		// getBus().register(this);
		//
		// getBus().post(new TaskEvent(TaskEvent.RELOAD, getLoaderManager()));
	}

	void initCursorAdapter()
	{
		list.setAdapter(new SimpleCursorAdapter(getActivity(),
				android.R.layout.simple_expandable_list_item_1, null,
				new String[]
				{ "MyProperty" }, new int[]
				{ android.R.id.text1 }, 0));

		getActivity().getSupportLoaderManager().initLoader(0, null,
				new LoaderCallbacks<Cursor>()
				{
					@Override
					public Loader<Cursor> onCreateLoader(int arg0, Bundle cursor)
					{
						return new CursorLoader(getActivity(), ContentProvider
								.createUri(GitHubUser.class, null), null, null,
								null, null);
					}

					@Override
					public void onLoadFinished(Loader<Cursor> arg0,
							Cursor cursor)
					{
						((SimpleCursorAdapter) list.getAdapter())
								.swapCursor(cursor);
					}

					@Override
					public void onLoaderReset(Loader<Cursor> arg0)
					{
						((SimpleCursorAdapter) list.getAdapter())
								.swapCursor(null);
					}
				});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{

		View rootView = inflater.inflate(R.layout.indexed_list, container,
				false);
		list = (IndexableListView) rootView.findViewById(android.R.id.list);

		return rootView;
	}

	@Override
	public void onResume()
	{
		super.onResume();

		getBus().register(this);

		// getBus().post(new TaskEvent(TaskEvent.RELOAD, getLoaderManager()));
	}

	private Bus getBus()
	{
		if (mBus == null)
		{
			mBus = BusProvider_.getInstance_(getActivity());
		}
		return mBus;
	}

	@Subscribe
	public void onUsersLoaded(LoadUsersEvent event)
	{
		if (event.getUsers() == null)
		{
			return;
		}

		if (mUsersList == null)
		{
			mUsersList = event.getUsers();
		} else
		{
			mUsersList.addAll(event.getUsers());
		}

		UserList ul = new UserList(mUsersList);
		mAdapter.setNewModel(ul);

		// mAdapter.notifyDataSetChanged();
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
