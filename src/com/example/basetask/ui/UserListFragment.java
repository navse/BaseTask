package com.example.basetask.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AlphabetIndexer;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.activeandroid.content.ContentProvider;
import com.example.basetask.BusProvider_;
import com.example.basetask.events.TaskEvent;
import com.example.basetask.model.EventEnum;
import com.example.basetask.model.GitHubUser;
import com.squareup.otto.Bus;

public class UserListFragment extends ListFragment implements
		OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor>
{

	Cursor mCursor;
	UsersCursorAdapter mAdapter;
	BusProvider_ mBus;

	@Override
	public void onActivityCreated(Bundle aSavedInstanceState)
	{
		super.onActivityCreated(aSavedInstanceState);

		mAdapter = new UsersCursorAdapter(
				getActivity().getApplicationContext(),
				android.R.layout.simple_list_item_1, null, new String[]
				{ "login" }, new int[]
				{ android.R.id.text1 });
		setListAdapter(mAdapter);
		getListView().setOnItemClickListener(this);

		getLoaderManager().initLoader(0, null, this);
	}

	public Loader<Cursor> onCreateLoader(int aId, Bundle aArgs)
	{
		return new CursorLoader(getActivity(), ContentProvider.createUri(
				GitHubUser.class, null), null, null, null,
				"login COLLATE NOCASE ASC");

	}

	public void onLoadFinished(Loader<Cursor> aLoader, Cursor aData)
	{
		mAdapter.swapCursor(aData);

		getListView().setFastScrollAlwaysVisible(true);
		getListView().setFastScrollEnabled(true);
		getListView().setScrollingCacheEnabled(true);
	}

	public void onLoaderReset(Loader<Cursor> aLoader)
	{
		mAdapter.swapCursor(null);
	}

	@Override
	public void onItemClick(AdapterView<?> aAdapter, View aView, int aPosition,
			long aId)
	{
		Object o = mAdapter.getItem(aPosition);
		if (o instanceof Cursor)
		{
			Cursor c = (Cursor) o;
			String url = c.getString(c.getColumnIndex("html_url"));
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);
		}
	}

	@Override
	public void onResume()
	{
		super.onResume();

		getBus().register(this);

		getBus().post(new TaskEvent(EventEnum.INIT));
	}

	private Bus getBus()
	{
		if (mBus == null)
		{
			mBus = BusProvider_.getInstance_(getActivity());
		}
		return mBus;
	}

	public class UsersCursorAdapter extends SimpleCursorAdapter implements
			SectionIndexer
	{

		AlphabetIndexer mAlphabetIndexer;

		public UsersCursorAdapter(Context aContext, int aLayout,
				Cursor aCursor, String[] aStrings, int[] aIs)
		{
			super(aContext, aLayout, aCursor, aStrings, aIs);
		}

		public Cursor swapCursor(Cursor aCursor)
		{
			// Create our indexer
			if (aCursor != null)
			{
				mAlphabetIndexer = new AlphabetIndexer(aCursor,
						aCursor.getColumnIndex("login"),
						" ABCDEFGHIJKLMNOPQRSTUVWXYZ");
			}
			return super.swapCursor(aCursor);
		}

		/**
		 * Performs a binary search or cache lookup to find the first row that
		 * matches a given section's starting letter.
		 */
		@Override
		public int getPositionForSection(int aSectionIndex)
		{
			return mAlphabetIndexer.getPositionForSection(aSectionIndex);
		}

		/**
		 * Returns the section index for a given position in the list by
		 * querying the item and comparing it with all items in the section
		 * array.
		 */
		@Override
		public int getSectionForPosition(int aPosition)
		{
			return mAlphabetIndexer.getSectionForPosition(aPosition);
		}

		/**
		 * Returns the section array constructed from the alphabet provided in
		 * the constructor.
		 */
		@Override
		public Object[] getSections()
		{
			return mAlphabetIndexer.getSections();
		}

		/**
		 * Bind an existing view to the data pointed to by cursor
		 */
		@Override
		public void bindView(View view, Context context, Cursor cursor)
		{
			TextView txtView = (TextView) view.findViewById(android.R.id.text1);
			txtView.setTextColor(Color.BLACK);
			txtView.setText(cursor.getString(cursor.getColumnIndex("login")));
		}

		/**
		 * Makes a new view to hold the data pointed to by cursor.
		 */
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent)
		{
			LayoutInflater inflater = LayoutInflater.from(context);
			View newView = inflater.inflate(
					android.R.layout.simple_list_item_1, parent, false);
			return newView;
		}
	}
}
