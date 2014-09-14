package com.example.basetask.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.basetask.R;
import com.example.basetask.model.GitHubUser;
import com.example.basetask.util.StringMatcher;

public class SectionedListAdapter extends BaseAdapter implements SectionIndexer
{

	private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private UserList currentList;

	private Context context;
	private LayoutInflater inflater;

	public SectionedListAdapter(Context context)
	{
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getPositionForSection(int section)
	{
		// If there is no item for current section, previous section will be
		// selected
		for (int i = section; i >= 0; i--)
		{
			for (int j = 0; j < getCount(); j++)
			{
				if (i == 0)
				{
					// For numeric section
					for (int k = 0; k <= 9; k++)
					{
						if (StringMatcher.match(String.valueOf(currentList
								.getItem(j).getSection()), String.valueOf(k)))
							return j;
					}
				} else
				{
					if (StringMatcher
							.match(String.valueOf(currentList.getItem(j)
									.getSection()), String.valueOf(mSections
									.charAt(i))))
						return j;
				}
			}
		}
		return 0;
	}

	@Override
	public int getSectionForPosition(int position)
	{
		return 0;
	}

	@Override
	public Object[] getSections()
	{
		String[] sections = new String[mSections.length()];
		for (int i = 0; i < mSections.length(); i++)
			sections[i] = String.valueOf(mSections.charAt(i));
		return sections;
	}

	public void setNewModel(UserList list)
	{
		this.currentList = list;
		notifyDataSetChanged();
	}

	public void addItems(List<GitHubUser> list)
	{
		this.currentList.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		if (currentList == null)
		{
			return 0;
		}
		return currentList.getCount();
	}

	@Override
	public Object getItem(int position)
	{
		if (currentList.isSection(position))
		{
			return currentList.getSection(position);
		}
		return currentList.getItem(position);
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup)
	{
		if (view == null)
		{
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

	private int getColorForPosition(int position)
	{
		if (currentList.isSection(position))
		{
			return context.getResources().getColor(R.color.section_header);
		}
		return context.getResources().getColor(android.R.color.black);
	}

	private CharSequence getTextForPosition(int position)
	{
		if (currentList.isSection(position))
		{
			return String.valueOf(currentList.getSection(position));
		}
		final GitHubUser person = currentList.getItem(position);
		return person.getLogin();
	}

	@Override
	public boolean isEnabled(int position)
	{
		return true;
	}

	private static class ViewHolder
	{
		TextView text;
	}
}
