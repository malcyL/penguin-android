package uk.co.blackpepper.penguin.android;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener
{
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the three primary
	 * sections of the app. We use a {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will keep
	 * every loaded fragment in memory. If this becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	AppSectionsPagerAdapter mAppSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will display the three primary sections of the app, one at a time.
	 */
	ViewPager mViewPager;

	private boolean created;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// The main activity is not created until a server is configured.
		// Creation which would normally be done here is performed in onResume if
		// a server has been configued.
		created = false;
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
	{
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
	{
		// When the given tab is selected, switch to the corresponding page in the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
	{
	}

	@Override
	public void onResume()
	{
		super.onResume();

		if (!isServerUrlConfigured())
		{
			displaySettings();
		}
		else if (!created)
		{
			create();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.queue_list_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.menu_settings:
				Intent settingsIntent = new Intent(this, SettingsActivity.class);
				startActivity(settingsIntent);
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary sections of the app.
	 */
	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter
	{
		public AppSectionsPagerAdapter(FragmentManager fm)
		{
			super(fm);
		}

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

	/**
	 * A fragment showing a list of queues.
	 */
	public static class QueuesFragment extends Fragment
	{
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.fragment_queues, container, false);
			return rootView;
		}
	}

	/**
	 * A penguin fragment - A list of where the penguins are coming soon!
	 */
	public static class PenguinsListFragment extends Fragment
	{
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.fragment_penguin_list, container, false);
			((TextView) rootView.findViewById(android.R.id.text1)).setText(R.string.coming_soon);
			return rootView;
		}
	}

	/**
	 * create activity.
	 * 
	 * This would normally be done in the onCreate method. However, as we can not load data from the server until it has
	 * been configured, it is extracted here and called from onResume when details of a server are available.
	 */
	private void create()
	{
		// Create the adapter that will return a fragment for each of the three primary sections
		// of the app.
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();

		// Specify that the Home/Up button should not be enabled, since there is no hierarchical
		// parent.
		actionBar.setHomeButtonEnabled(false);

		// Specify that we will be displaying tabs in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set up the ViewPager, attaching the adapter and setting up a listener for when the
		// user swipes between sections.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
		{
			@Override
			public void onPageSelected(int position)
			{
				// When swiping between different app sections, select the corresponding tab.
				// We can also use ActionBar.Tab#select() to do this if we have a reference to the
				// Tab.
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++)
		{
			// Create a tab with text corresponding to the page title defined by the adapter.
			// Also specify this Activity object, which implements the TabListener interface, as the
			// listener for when this tab is selected.
			actionBar.addTab(actionBar.newTab().setText(mAppSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
		}

		created = true;
	}

	private boolean isServerUrlConfigured()
	{
		String defaultServerUrl = getResources().getString(R.string.pref_default_server_url);
		String serverUrl = PreferenceUtils.getServerUrl(this);

		if (null == serverUrl || defaultServerUrl.equals(serverUrl))
		{
			return false;
		}
		
		return true;
	}

	private void displaySettings()
	{
		Intent settingsIntent = new Intent(this, SettingsActivity.class);
		startActivity(settingsIntent);
	}
}
