package uk.co.blackpepper.penguin.android;

import org.apache.http.impl.client.DefaultHttpClient;

import uk.co.blackpepper.penguin.R;
import uk.co.blackpepper.penguin.client.Queue;
import uk.co.blackpepper.penguin.client.QueueService;
import uk.co.blackpepper.penguin.client.ServiceException;
import uk.co.blackpepper.penguin.client.httpclient.HttpClientQueueService;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class QueueActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Queue>
{
	// constants --------------------------------------------------------------
	
	private static final String TAG = QueueActivity.class.getName();
	
	// fields -----------------------------------------------------------------
	
	private QueueService queueService;
	
	private String queueId;
	
	private Queue queue;
	
	// Activity methods -------------------------------------------------------
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		queueService = new HttpClientQueueService(new DefaultHttpClient(), PreferenceUtils.getServerApiUrl(this));
		
		queueId = getIntent().getExtras().getString("id");
		
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
	public Loader<Queue> onCreateLoader(int id, Bundle args)
	{
		return new AsyncTaskLoader<Queue>(this)
		{
			@Override
			protected void onStartLoading()
			{
				forceLoad();
			}
			
			@Override
			public Queue loadInBackground()
			{
				try
				{
					return queueService.get(queueId);
				}
				catch (ServiceException exception)
				{
					Log.e(TAG, "Error loading queue: " + queueId, exception);
					return null;
				}
			}
		};
	}

	@Override
	public void onLoadFinished(Loader<Queue> loader, Queue queue)
	{
		this.queue = queue;
		
		setTitle(queue.getName());
	}

	@Override
	public void onLoaderReset(Loader<Queue> loader)
	{
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
