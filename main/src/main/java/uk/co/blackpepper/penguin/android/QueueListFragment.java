package uk.co.blackpepper.penguin.android;

import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;

import uk.co.blackpepper.penguin.R;
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
import android.widget.Toast;

public class QueueListFragment extends ListFragment implements LoaderCallbacks<Either<List<Queue>, ServiceException>>
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
	public Loader<Either<List<Queue>, ServiceException>> onCreateLoader(int id, Bundle args)
	{
		return new AsyncTaskLoader<Either<List<Queue>, ServiceException>>(getActivity())
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
			Toast.makeText(getActivity(), R.string.toast_service_error, Toast.LENGTH_LONG).show();
			Log.e(TAG, "Error loading queues", data.right());
		}
	}

	@Override
	public void onLoaderReset(Loader<Either<List<Queue>, ServiceException>> loader)
	{
		adapter.setData(null);
	}
}
