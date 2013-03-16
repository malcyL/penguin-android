package uk.co.blackpepper.penguin.android;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class LateralQueueNavigationActivity extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments representing
     * each object in a collection. We use a {@link android.support.v4.app.FragmentStatePagerAdapter}
     * derivative, which will destroy and re-create fragments as needed, saving and restoring their
     * state in the process. This is important to conserve memory and is a best practice when
     * allowing navigation between objects in a potentially large collection.
     */
    QueuePagerAdapter queuePagerAdapter;

    /**
     * The {@link android.support.v4.view.ViewPager} that will display the object collection.
     */
    ViewPager viewPager;

	private String queueName;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lateral_queue_navigation);

		queueName = getIntent().getExtras().getString(QueueListFragment.QUEUE_NAME_KEY); 
		
		setTitle(queueName);
        
        // Create an adapter that when requested, will return a fragment representing an object in
        // the collection.
        // 
        // ViewPager and its adapters use support library fragments, so we must use
        // getSupportFragmentManager.
        queuePagerAdapter = new QueuePagerAdapter(getSupportFragmentManager());

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Set up the ViewPager, attaching the adapter.
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(queuePagerAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
                Intent upIntent = new Intent(this, MainActivity.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is not part of the application's task, so create a new task
                    // with a synthesized back stack.
                    TaskStackBuilder.from(this)
                            // If there are ancestor activities, they should be added here.
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } else {
                    // This activity is part of the application's task, so simply
                    // navigate up to the hierarchical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment
     * representing an object in the collection.
     */
    public static class QueuePagerAdapter extends FragmentStatePagerAdapter {

        public QueuePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	switch (position) {
        		case 0: {
        			Fragment fragment = new UnMergedStoriesFragment();
                    return fragment;
        		}
        		case 1: {
        			Fragment fragment = new MergedStoriesFragment();
                    return fragment;
        		}
        		default : {
        			// TODO This shouldn't happen.  How do we handle the error?
                    return null;
        		}
        	}
        }

        @Override
        public int getCount() {
            // For this contrived example, we have a 100-object collection.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
        	switch (position) {
        		case 0: 
                	// TODO This should come from strings.xml
        			return "Pending";
        		case 1:
                	// TODO This should come from strings.xml
        			return "Merged";
        		default:
        			// TODO This shouldn't happen.  How do we handle the error?
        			return null;
        	}
        }
    }

    /**
     * A fragment holding the list of unmerged stories.
     */
    public static class UnMergedStoriesFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        	View viewRoot = inflater.inflate(R.layout.fragment_stories_unmerged, container, false);
            return viewRoot;
        }
    }

    /**
     * A fragment holding the list of merged stories.
     */
    public static class MergedStoriesFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        	View viewRoot = inflater.inflate(R.layout.fragment_stories_merged, container, false);
            return viewRoot;
        }
    }
}
