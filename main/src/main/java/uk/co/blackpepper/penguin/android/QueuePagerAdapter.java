package uk.co.blackpepper.penguin.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment representing an object in the
 * collection.
 */
public class QueuePagerAdapter extends FragmentStatePagerAdapter
{
	// constructors -----------------------------------------------------------
	
	public QueuePagerAdapter(FragmentManager fragmentManager)
	{
		super(fragmentManager);
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
	
	// private methods --------------------------------------------------------
	
	private static Fragment createStoryListFragment(boolean merged)
	{
		Fragment fragment = new StoryListFragment();
		
		Bundle arguments = new Bundle();
		arguments.putBoolean(StoryListFragment.MERGED_KEY, merged);
		fragment.setArguments(arguments);
		
		return fragment;
	}
}
