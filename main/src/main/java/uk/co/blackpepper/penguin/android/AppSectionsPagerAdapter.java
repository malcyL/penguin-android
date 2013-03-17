package uk.co.blackpepper.penguin.android;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary sections of the app.
 */
public class AppSectionsPagerAdapter extends FragmentPagerAdapter
{
	// constructors -----------------------------------------------------------
	
	public AppSectionsPagerAdapter(FragmentManager fm)
	{
		super(fm);
	}

	// FragmentStatePagerAdapter methods --------------------------------------

	@Override
	public Fragment getItem(int i)
	{
		switch (i)
		{
			case 0:
				return new QueuesFragment();

			case 1:
				return new PenguinsListFragment();

			default:
				// This is an error really and should never happen. Until I know how to deal with that, return Queue
				// List
				return new QueuesFragment();
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
				return "Queues";

			case 1:
				// TODO This should come from strings.xml
				return "Penguins";

			default:
				// TODO This shouldn't happen. How do we handle the error?
				return null;
		}
	}
}
