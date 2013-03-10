package uk.co.blackpepper.penguin.android;

import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;

import uk.co.blackpepper.penguin.R;
import uk.co.blackpepper.penguin.client.Queue;
import uk.co.blackpepper.penguin.client.ServiceException;
import uk.co.blackpepper.penguin.client.Story;
import uk.co.blackpepper.penguin.client.StoryService;
import uk.co.blackpepper.penguin.client.httpclient.HttpClientStoryService;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class QueueActivity extends ListActivity implements LoaderManager.LoaderCallbacks<List<Story>>
{
	// constants --------------------------------------------------------------
	
	private static final String TAG = QueueActivity.class.getName();
	
	// fields -----------------------------------------------------------------
	
	private QueueAdapter adapter;
	
	private StoryService storyService;
	
	private String queueId;
	
	private Queue queue;
	
	// Activity methods -------------------------------------------------------
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		storyService = new HttpClientStoryService(new DefaultHttpClient(), PreferenceUtils.getServerApiUrl(this));
		
		queueId = getIntent().getExtras().getString("id");

		setTitle(getIntent().getExtras().getString("queueName"));

		adapter = new QueueAdapter(this, android.R.layout.simple_list_item_1);
		setListAdapter(adapter);
		
		getLoaderManager().initLoader(0, null, this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.queue_menu, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.menu_rename:
				showQueueEditDialog(queue);
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	// LoaderCallbacks methods ------------------------------------------------

	@Override
	public Loader<List<Story>> onCreateLoader(int id, Bundle args)
	{
		return new AsyncTaskLoader<List<Story>>(this)
		{
			@Override
			protected void onStartLoading()
			{
				forceLoad();
			}
			
			@Override
			public List<Story> loadInBackground()
			{
				try
				{
					return storyService.getAll(queueId);
				}
				catch (ServiceException exception)
				{
					Log.e(TAG, "Error loading stories for queue: " + queueId, exception);
					return null;
				}
			}
		};
	}

	@Override
	public void onLoadFinished(Loader<List<Story>> loader, List<Story> stories)
	{
		adapter.setData(stories);
	}

	@Override
	public void onLoaderReset(Loader<List<Story>> loader)
	{
		adapter.setData(null);
		setTitle(R.string.title_activity_queue);
	}
	
	// private methods --------------------------------------------------------
	
	private void showQueueEditDialog(Queue queue)
	{
		QueueEditDialogFragment fragment = new QueueEditDialogFragment();
		
		Bundle arguments = new Bundle();
		arguments.putString(QueueEditDialogFragment.ID, queue.getId());
		arguments.putString(QueueEditDialogFragment.NAME, queue.getName());
		fragment.setArguments(arguments);
		
		fragment.show(getFragmentManager(), "queueEdit");
	}
}
