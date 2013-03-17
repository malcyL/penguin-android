package uk.co.blackpepper.penguin.android;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;

import uk.co.blackpepper.penguin.client.Queue;
import uk.co.blackpepper.penguin.client.QueueService;
import uk.co.blackpepper.penguin.client.ServiceException;
import uk.co.blackpepper.penguin.client.httpclient.HttpClientQueueService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class QueueListFragment extends ListFragment implements LoaderCallbacks<List<Queue>>
{
	// constants --------------------------------------------------------------
	
	public static String QUEUE_ID_KEY = "id";

	public static String QUEUE_NAME_KEY = "queueName";

	private static final String TAG = QueueListFragment.class.getName();
	
	// fields -----------------------------------------------------------------

	private QueueService queueService;

	private QueueAdapter adapter;
	
	// ListFragment methods ---------------------------------------------------

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		queueService = new HttpClientQueueService(new DefaultHttpClient(),
			PreferenceUtils.getServerApiUrl(inflater.getContext()));

		adapter = new QueueAdapter(inflater.getContext(), android.R.layout.simple_list_item_1);
		setListAdapter(adapter);

		getLoaderManager().initLoader(0, null, this);

		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id)
	{
		Queue queue = (Queue) listView.getItemAtPosition(position);

		Intent intent = new Intent(getActivity(), QueueActivity.class);
		intent.putExtra(QUEUE_ID_KEY, queue.getId());
		intent.putExtra(QUEUE_NAME_KEY, queue.getName());
		startActivity(intent);
	}
	
	// LoaderCallbacks methods ------------------------------------------------

	@Override
	public Loader<List<Queue>> onCreateLoader(int id, Bundle args)
	{
		return new AsyncTaskLoader<List<Queue>>(getActivity())
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
					return new ArrayList<Queue>();
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
