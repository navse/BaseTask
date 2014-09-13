package com.example.basetask;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.basetask.model.GitHubUser;

public class UsersListAdapter extends ArrayAdapter<GitHubUser> {
	private Context context;
	private boolean useList = true;


	public UsersListAdapter(Context context, List<GitHubUser> list) {
		super(context, android.R.layout.simple_list_item_1, list);
		this.context = context;
	}

	/** * Holder for the list items. */
	private class ViewHolder {
		TextView titleText;
	}

	/** * * @param position * @param convertView * @param parent * @return */
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		GitHubUser item = (GitHubUser) getItem(position);
		View viewToUse = null; // This block exists to inflate the settings list
								// item conditionally based on whether // we
								// want to support a grid or list view.
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			if (useList) {
				viewToUse = mInflater.inflate(R.layout.list_item, null);
			} else {
				viewToUse = mInflater.inflate(R.layout.list_item, null);
			}
			holder = new ViewHolder();
			holder.titleText = (TextView) viewToUse
					.findViewById(R.id.textViewLogin);
			viewToUse.setTag(holder);
		} else {
			viewToUse = convertView;
			holder = (ViewHolder) viewToUse.getTag();
		}
		holder.titleText.setText(item.getLogin());
		return viewToUse;
	}

	public void setUsers(List<GitHubUser> users) {
		// TODO Auto-generated method stub

	}
}
