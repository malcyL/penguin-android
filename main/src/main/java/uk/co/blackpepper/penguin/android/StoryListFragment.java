package uk.co.blackpepper.penguin.android;

import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;

import uk.co.blackpepper.penguin.R;
import uk.co.blackpepper.penguin.client.ServiceException;
import uk.co.blackpepper.penguin.client.Story;
import uk.co.blackpepper.penguin.client.StoryService;
import uk.co.blackpepper.penguin.client.httpclient.HttpClientStoryService;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class StoryListFragment extends ListFragment implements LoaderCallbacks<Either<List<Story>, ServiceException>>
{
	// constants --------------------------------------------------------------
	
	private static final String TAG = StoryListFragment.class.getName();
	
	// fields -----------------------------------------------------------------

	private StoryService storyService;

	private StoryAdapter adapter;

	private String queueId;

	private boolean merged;
	
	// ListFragment methods ---------------------------------------------------

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		queueId = getActivity().getIntent().getExtras().getString(QueueListFragment.QUEUE_ID_KEY);
		merged = (getId() == R.id.merged_story_list_fragment);

		storyService = new HttpClientStoryService(new DefaultHttpClient(),
			PreferenceUtils.getServerApiUrl(inflater.getContext()));

		adapter = new StoryAdapter(inflater.getContext(), android.R.layout.simple_list_item_1);
		setListAdapter(adapter);

		getLoaderManager().initLoader(0, null, this);

		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	// LoaderCallbacks methods ------------------------------------------------

	@Override
	public Loader<Either<List<Story>, ServiceException>> onCreateLoader(int id, Bundle args)
	{
		return new AsyncTaskLoader<Either<List<Story>, ServiceException>>(getActivity())
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
					List<Story> stories = merged ? storyService.getMerged(queueId) : storyService.getUnmerged(queueId);
					return Either.left(stories);
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
			Toast.makeText(getActivity(), R.string.toast_service_error, Toast.LENGTH_LONG).show();
			Log.e(TAG, "Error loading stories for queue: " + queueId, data.right());
		}
	}

	@Override
	public void onLoaderReset(Loader<Either<List<Story>, ServiceException>> loader)
	{
		adapter.setData(null);
	}
}
