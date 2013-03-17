package uk.co.blackpepper.penguin.android;

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
	
	public QueuePagerAdapter(FragmentManager fm)
	{
		super(fm);
	}
	
	// FragmentStatePagerAdapter methods --------------------------------------

	@Override
	public Fragment getItem(int position)
	{
		switch (position)
		{
			case 0:
				return new UnmergedStoriesFragment();
				
			case 1:
				return new MergedStoriesFragment();
				
			default:
				// TODO This shouldn't happen. How do we handle the error?
				return null;
		}
	}
	
	// PagerAdapter methods ---------------------------------------------------

	@Override
	public int getCount()
	{
		// For this contrived example, we have a 100-object collection.
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
				// TODO This shouldn't happen. How do we handle the error?
				return null;
		}
	}
}
