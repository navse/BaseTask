package com.example.basetask;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.basetask.model.GitHubUser;

public class SectionedUsersListAdapter extends BaseAdapter {
	private UserList currentList;

	private Context context;
	private LayoutInflater inflater;

	public SectionedUsersListAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	public void setNewModel(UserList list) {
		this.currentList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (currentList == null) {
			return 0;
		}
		return currentList.getCount();
	}

	@Override
	public Object getItem(int position) {
		if (currentList.isSection(position)) {
			return currentList.getSection(position);
		}
		return currentList.getItem(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		if (view == null) {
			view = inflater.inflate(android.R.layout.simple_list_item_1,
					viewGroup, false);
			ViewHolder holder = new ViewHolder();
			holder.text = (TextView) view.findViewById(android.R.id.text1);
			view.setTag(holder);
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.text.setTextColor(getColorForPosition(position));
		holder.text.setText(getTextForPosition(position));
		return view;
	}

	private int getColorForPosition(int position) {
		if (currentList.isSection(position)) {
			return context.getResources().getColor(R.color.section_header);
		}
		return context.getResources().getColor(android.R.color.black);
	}

	private CharSequence getTextForPosition(int position) {
		if (currentList.isSection(position)) {
			return String.valueOf(currentList.getSection(position));
		}
		final GitHubUser person = currentList.getItem(position);
		return person.getLogin();
	}

	private static class ViewHolder {
		TextView text;
	}
}
