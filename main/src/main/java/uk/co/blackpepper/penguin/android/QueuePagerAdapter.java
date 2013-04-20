package uk.co.blackpepper.penguin.android;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment representing an object in the
 * collection.
 */
public class QueuePagerAdapter extends FragmentStatePagerAdapter implements Refreshable
{
	private Refreshable refreshCallback;
	private List<Refreshable> refreshables;
	
	// constructors -----------------------------------------------------------
	
	public QueuePagerAdapter(FragmentManager fragmentManager, Refreshable refreshCallback)
	{
		super(fragmentManager);
		refreshables = new ArrayList<Refreshable>();
		this.refreshCallback = refreshCallback;
	}
	
	// FragmentStatePagerAdapter methods --------------------------------------

	@Override
	public Fragment getItem(int position)
	{
		switch (position)
		{
			case 0:
				return createStoryListFragment(false);
				
			case 1:
				return createStoryListFragment(true);
				
			default:
				throw new IllegalArgumentException("position: " + position);
		}
	}
	
	// PagerAdapter methods ---------------------------------------------------

	@Override
	public int getCount()
	{
		return 2;
	}

	@Override
	public CharSequence getPageTitle(int position)
	{
		switch (position)
		{
			case 0:
				// TODO This should come from strings.xml
				return "Pending";
				
			case 1:
				// TODO This should come from strings.xml
				return "Merged";
				
			default:
				throw new IllegalArgumentException("position: " + position);
		}
	}
	
	// Refresh methods --------------------------------------------------------

	@Override
	public void refresh()
	{
		for (Refreshable r : refreshables)
		{
			r.refresh();
		}
	}

	// private methods --------------------------------------------------------
	
	private Fragment createStoryListFragment(boolean merged)
	{
		StoryListFragment fragment = new StoryListFragment();
		
		Bundle arguments = new Bundle();
		arguments.putBoolean(StoryListFragment.MERGED_KEY, merged);
		fragment.setArguments(arguments);
		
		refreshables.add(fragment);
		fragment.setRefreshCallback(refreshCallback);
		
		return fragment;
	}
}
