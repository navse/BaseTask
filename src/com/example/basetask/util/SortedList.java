package com.example.basetask.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class SortedList<E> extends ArrayList<E>
{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	private Comparator comparator;

	public SortedList(@SuppressWarnings("rawtypes") Comparator comparator)
	{
		this.comparator = comparator;
	}

	public SortedList(Collection<E> list, Comparator comparator)
	{
		super(list);
		this.comparator = comparator;
	}

	public void addSorted(E object)
	{
		int index = 0;
		for (E item : this)
		{
			int c = comparator.compare(object, item);
			if (c < 0)
			{
				this.add(index, object);
				return;
			} else if (c > 0)
			{
				index++;
			} else
			{
				this.add(index + 1, object);
				return;
			}
		}

	}

	// TODO: recursive binary search:
	private void addAtIndex(E object, int index)
	{
		int c = comparator.compare(object, get(index));
		if (c < 0)
		{
			addAtIndex(object, index / 2);
		} else if (c > 0)
		{
			addAtIndex(object, index / 2);
		} else
		{
			this.add(index, object);
		}
	}
}
