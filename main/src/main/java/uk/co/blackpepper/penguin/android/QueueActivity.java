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

public class QueueActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Queue>
{
	// constants --------------------------------------------------------------
	
	private static final String TAG = QueueActivity.class.getName();
	
	// fields -----------------------------------------------------------------
	
	private QueueService queueService;
	
	private String queueId;
	
	// Activity methods -------------------------------------------------------
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		queueService = new HttpClientQueueService(new DefaultHttpClient(), "http://10.0.2.2:8080/api");
		
		queueId = getIntent().getExtras().getString("id");
		
		getLoaderManager().initLoader(0, null, this);
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
		setTitle(queue.getName());
	}

	@Override
	public void onLoaderReset(Loader<Queue> loader)
	{
		setTitle(R.string.title_activity_queue);
	}
}
