package uk.co.blackpepper.penguin.android;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;

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

public class StoryListFragment extends ListFragment implements LoaderCallbacks<List<Story>>
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
		if (getId() == R.id.merged_story_list_fragment)
		{
			merged = true;
		}
		else
		{
			merged = false;
		}

		queueId = getActivity().getIntent().getExtras().getString(QueueListFragment.QUEUE_ID_KEY);

		storyService = new HttpClientStoryService(new DefaultHttpClient(),
			PreferenceUtils.getServerApiUrl(inflater.getContext()));

		adapter = new StoryAdapter(inflater.getContext(), android.R.layout.simple_list_item_1);
		setListAdapter(adapter);

		getLoaderManager().initLoader(0, null, this);

		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	// LoaderCallbacks methods ------------------------------------------------

	@Override
	public Loader<List<Story>> onCreateLoader(int id, Bundle args)
	{
		return new AsyncTaskLoader<List<Story>>(getActivity())
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
					if (merged)
					{
						return storyService.getMerged(queueId);
					}
					else
					{
						return storyService.getUnmerged(queueId);
					}
				}
				catch (ServiceException e)
				{
					// TODO Handle Properly
					Log.e(TAG, "Error Loading Stories", e);
					return new ArrayList<Story>();
				}
			}
		};
	}

	@Override
	public void onLoadFinished(Loader<List<Story>> loader, List<Story> data)
	{
		adapter.setData(data);
	}

	@Override
	public void onLoaderReset(Loader<List<Story>> loader)
	{
		adapter.setData(null);
	}
}
