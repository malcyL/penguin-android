package uk.co.blackpepper.penguin.android;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary sections of the app.
 */
public class MainPagerAdapter extends FragmentPagerAdapter
{
	// constructors -----------------------------------------------------------
	
	public MainPagerAdapter(FragmentManager fragmentManager)
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
				return new QueueListFragment();

			default:
				throw new IllegalArgumentException("position: " + position);
		}
	}

	// PagerAdapter methods ---------------------------------------------------

	@Override
	public int getCount()
	{
		return 1;
	}

	@Override
	public CharSequence getPageTitle(int position)
	{
		switch (position)
		{
			case 0:
				// TODO This should come from strings.xml
				return "Queues";

			default:
				throw new IllegalArgumentException("position: " + position);
		}
	}
}
