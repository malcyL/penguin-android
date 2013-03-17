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
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class QueueListActivity extends ListActivity
	implements LoaderManager.LoaderCallbacks<Either<List<Queue>, ServiceException>>
{
	private static final String TAG = QueueListActivity.class.getName();

	private QueueService queueService;

	private QueueListAdapter adapter;

	@Override
	public void onResume()
	{
		if (isServerUrlConfigured())
		{
			queueService = new HttpClientQueueService(new DefaultHttpClient(), PreferenceUtils.getServerApiUrl(this));

			adapter = new QueueListAdapter(this, android.R.layout.simple_list_item_1);
			setListAdapter(adapter);
			
			getLoaderManager().initLoader(0, null, this);
		}
		else
		{
			displaySettings();
		}
		
		super.onResume();
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

	@Override
	public Loader<Either<List<Queue>, ServiceException>> onCreateLoader(int id, Bundle args)
	{
		return new AsyncTaskLoader<Either<List<Queue>, ServiceException>>(this)
		{
			@Override
			protected void onStartLoading()
			{
				forceLoad();
			}

			@Override
			public Either<List<Queue>, ServiceException> loadInBackground()
			{
				try
				{
					return Either.left(queueService.getAll());
				}
				catch (ServiceException exception)
				{
					return Either.right(exception);
				}
			}
		};
	}

	@Override
	public void onLoadFinished(Loader<Either<List<Queue>, ServiceException>> loader,
		Either<List<Queue>, ServiceException> data)
	{
		if (data.isLeft())
		{
			adapter.setData(data.left());
		}
		else
		{
			Toast.makeText(this, R.string.toast_service_error, Toast.LENGTH_LONG).show();
			Log.e(TAG, "Error loading queues", data.right());
		}
	}

	@Override
	public void onLoaderReset(Loader<Either<List<Queue>, ServiceException>> loader)
	{
		adapter.setData(null);
	}
	
	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id)
	{
		Queue queue = (Queue) listView.getItemAtPosition(position);
		
		Intent intent = new Intent(this,  QueueActivity.class);
		intent.putExtra("id", queue.getId());
		intent.putExtra("queueName", queue.getName());
		startActivity(intent);
	}

	private boolean isServerUrlConfigured() 
	{
		String defaultServerUrl = getResources().getString(R.string.pref_default_server_url);
		String serverUrl = PreferenceUtils.getServerUrl(this);
		
		if (null == serverUrl || defaultServerUrl.equals(serverUrl)) {
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
