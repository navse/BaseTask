package com.example.basetask.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;

import com.example.basetask.model.GitHubUser;
import com.example.basetask.util.SortedList;
import com.google.common.collect.Maps;

public class UserList
{
	private SortedList<GitHubUser> users;
	private SortedMap<Integer, Character> sections;
	private Comparator<GitHubUser> comparator;

	public UserList(List<GitHubUser> users)
	{
		comparator = new Comparator<GitHubUser>()
		{
			public int compare(GitHubUser user1, GitHubUser user2)
			{
				return user1.getLogin().compareTo(user2.getLogin());
			}
		};

		List<GitHubUser> usersCopy = new ArrayList<GitHubUser>(users);
		Collections.sort(usersCopy, comparator);
		this.users = new SortedList<GitHubUser>(usersCopy, comparator);
		naiveAlgorithmInitialize();
	}

	private void naiveAlgorithmInitialize()
	{
		Character currSection = null;
		sections = Maps.newTreeMap();
		int index = 0;
		for (GitHubUser user : users)
		{
			final Character section = user.getSection();
			if (!section.equals(currSection))
			{
				sections.put(sections.size() + index, section);
				currSection = section;
			}
			index++;
		}
	}

	public boolean isSection(int position)
	{
		return sections.get(position) != null;
	}

	public char getSection(int position)
	{
		return sections.get(position);
	}

	public GitHubUser getItem(int position)
	{
		int positionInList = position - sections.headMap(position).size();
		return users.get(positionInList);
	}

	public int getCount()
	{
		return users.size() + sections.size();
	}

	public void addAll(List<GitHubUser> list)
	{
		List<GitHubUser> usersCopy = new ArrayList<GitHubUser>(list);
		Collections.sort(usersCopy, comparator);
		for (GitHubUser user : usersCopy)
		{
			this.users.addSorted(user);
		}
		naiveAlgorithmInitialize();
	}

}
