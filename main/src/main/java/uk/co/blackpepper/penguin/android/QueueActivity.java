package uk.co.blackpepper.penguin.android;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

public class QueueActivity extends FragmentActivity
{
	// fields -----------------------------------------------------------------
	
	private PagerAdapter pagerAdapter;

	private ViewPager viewPager;

	private String queueName;

	// FragmentActivity methods -----------------------------------------------

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_lateral_queue_navigation);

		queueName = getIntent().getExtras().getString(QueueListFragment.QUEUE_NAME_KEY);
		setTitle(queueName);

		pagerAdapter = new QueuePagerAdapter(getSupportFragmentManager());

		// configure action bar
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// configure view pager
		
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(pagerAdapter);
	}

	// Activity methods -------------------------------------------------------

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				// This is called when the Home (Up) button is pressed in the action bar.
				// Create a simple intent that starts the hierarchical parent activity and
				// use NavUtils in the Support Package to ensure proper handling of Up.
				Intent upIntent = new Intent(this, MainActivity.class);
				if (NavUtils.shouldUpRecreateTask(this, upIntent))
				{
					// This activity is not part of the application's task, so create a new task
					// with a synthesized back stack.
					TaskStackBuilder.from(this)
					// If there are ancestor activities, they should be added here.
						.addNextIntent(upIntent).startActivities();
					finish();
				}
				else
				{
					// This activity is part of the application's task, so simply
					// navigate up to the hierarchical parent activity.
					NavUtils.navigateUpTo(this, upIntent);
				}
				return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
