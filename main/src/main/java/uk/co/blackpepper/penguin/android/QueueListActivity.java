package uk.co.blackpepper.penguin.android;

import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;

import uk.co.blackpepper.penguin.R;
import uk.co.blackpepper.penguin.client.Queue;
import uk.co.blackpepper.penguin.client.QueueService;
import uk.co.blackpepper.penguin.client.ServiceException;
import uk.co.blackpepper.penguin.client.httpclient.HttpClientQueueService;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class QueueListActivity extends ListActivity implements LoaderManager.LoaderCallbacks<List<Queue>>
{
	private static final String TAG = QueueListActivity.class.getName();

	private QueueService queueService;

	private QueueListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		queueService = new HttpClientQueueService(new DefaultHttpClient(), "http://10.0.2.2:8080/api");

		adapter = new QueueListAdapter(this, R.layout.queue_list_item);

		setListAdapter(adapter);

		getLoaderManager().initLoader(0, null, this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_queue_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle item selection
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

	@Override
	public Loader<List<Queue>> onCreateLoader(int id, Bundle args)
	{
		return new AsyncTaskLoader<List<Queue>>(this)
		{
			@Override
			protected void onStartLoading()
			{
				forceLoad();
			}

			@Override
			public List<Queue> loadInBackground()
			{
				try
				{
					return queueService.getAll();
				}
				catch (ServiceException e)
				{
					// TODO Handle Properly
					Log.e(TAG, "Error Loading Queues", e);
					throw new RuntimeException(e);
				}
			}
		};
	}

	@Override
	public void onLoadFinished(Loader<List<Queue>> loader, List<Queue> data)
	{
		adapter.setData(data);
	}

	@Override
	public void onLoaderReset(Loader<List<Queue>> loader)
	{
		adapter.setData(null);
	}
}
