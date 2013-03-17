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
import android.widget.Toast;

public class QueueActivity extends ListActivity
	implements LoaderManager.LoaderCallbacks<Either<List<Story>, ServiceException>>
{
	// constants --------------------------------------------------------------
	
	private static final String TAG = QueueActivity.class.getName();
	
	// fields -----------------------------------------------------------------
	
	private QueueAdapter adapter;
	
	private StoryService storyService;
	
	private String queueId;

	private String queueName;
	
	private Queue queue;
	
	// Activity methods -------------------------------------------------------
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		storyService = new HttpClientStoryService(new DefaultHttpClient(), PreferenceUtils.getServerApiUrl(this));
		
		queueId = getIntent().getExtras().getString("id");
		queueName = getIntent().getExtras().getString("queueName"); 

		setTitle(queueName);

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
	public Loader<Either<List<Story>, ServiceException>> onCreateLoader(int id, Bundle args)
	{
		return new AsyncTaskLoader<Either<List<Story>, ServiceException>>(this)
		{
			@Override
			protected void onStartLoading()
			{
				forceLoad();
			}
			
			@Override
			public Either<List<Story>, ServiceException> loadInBackground()
			{
				try
				{
					return Either.left(storyService.getAll(queueId));
				}
				catch (ServiceException exception)
				{
					return Either.right(exception);
				}
			}
		};
	}

	@Override
	public void onLoadFinished(Loader<Either<List<Story>, ServiceException>> loader,
		Either<List<Story>, ServiceException> data)
	{
		if (data.isLeft())
		{
			adapter.setData(data.left());
		}
		else
		{
			Toast.makeText(this, R.string.toast_service_error, Toast.LENGTH_LONG).show();
			Log.e(TAG, "Error loading stories for queue: " + queueId, data.right());
		}
	}

	@Override
	public void onLoaderReset(Loader<Either<List<Story>, ServiceException>> loader)
	{
		adapter.setData(null);
		setTitle(R.string.title_activity_queue);
	}
	
	// private methods --------------------------------------------------------
	
	private void showQueueEditDialog(Queue queue)
	{
		QueueEditDialogFragment fragment = new QueueEditDialogFragment();
		
		Bundle arguments = new Bundle();
		arguments.putString(QueueEditDialogFragment.ID, queueId);
		arguments.putString(QueueEditDialogFragment.NAME, queueName);
		fragment.setArguments(arguments);
		
		fragment.show(getFragmentManager(), "queueEdit");
	}
}
