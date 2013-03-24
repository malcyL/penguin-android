package uk.co.blackpepper.penguin.android;

import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;

import uk.co.blackpepper.penguin.R;
import uk.co.blackpepper.penguin.client.ServiceException;
import uk.co.blackpepper.penguin.client.Story;
import uk.co.blackpepper.penguin.client.StoryService;
import uk.co.blackpepper.penguin.client.httpclient.HttpClientStoryService;
import android.os.AsyncTask;
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

public class StoryListFragment extends ListFragment 
	implements LoaderCallbacks<Either<List<Story>, ServiceException>>, MergeTaskCompleted
{
	// constants --------------------------------------------------------------
	
	private static final int STORY_LIST_FRAGMENT = 1;

	public static final String MERGED_KEY = "merged";
	
	private static final String TAG = StoryListFragment.class.getName();
	
	// fields -----------------------------------------------------------------

	private StoryService storyService;

	private StoryAdapter adapter;

	private String queueId;

	private boolean merged;

	private MergeAsyncTask mergeTask; 
	
	// ListFragment methods ---------------------------------------------------

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		queueId = getActivity().getIntent().getExtras().getString(QueueListFragment.QUEUE_ID_KEY);
		merged = getArguments().getBoolean(MERGED_KEY);

		storyService = new HttpClientStoryService(new DefaultHttpClient(),
			PreferenceUtils.getServerApiUrl(inflater.getContext()));

		adapter = new StoryAdapter(inflater.getContext(), android.R.layout.simple_list_item_1);
		setListAdapter(adapter);

		getLoaderManager().initLoader(0, null, this);

		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id)
	{
		Story story = (Story) listView.getItemAtPosition(position);
		
		MergeConfirmDialogFragment fragment = new MergeConfirmDialogFragment();

		Bundle arguments = new Bundle();
		arguments.putString(MergeConfirmDialogFragment.QUEUE_ID_KEY, queueId);
		arguments.putString(MergeConfirmDialogFragment.STORY_ID_KEY, story.getId());
		arguments.putString(MergeConfirmDialogFragment.STORY_REF_KEY, story.getReference());
		fragment.setArguments(arguments);
		
		fragment.setTargetFragment(this, STORY_LIST_FRAGMENT);

		fragment.show(getFragmentManager(), "merge");
	}

    @Override
    public void onDestroy() {
        super.onDestroy();
 
        if (mergeTask != null) {
        	mergeTask.cancel(true);
        }
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
	
	// Merge Async Task methods ------------------------------------------------

	public void mergeConfirmed(String queueId, String storyId) 
	{
		mergeTask = new MergeAsyncTask(queueId, storyId, this);
		mergeTask.execute();
	}
	
	@Override
	public void onTaskCompleted(Either<List<Story>, ServiceException> data)
	{
		if (data.isLeft())
		{
			adapter.setData(data.left());
		}
		else
		{
			Toast.makeText(getActivity(), R.string.toast_service_error, Toast.LENGTH_LONG).show();
			Log.e(TAG, "Error merging story for queue: " + queueId, data.right());
		}
	}

	private class MergeAsyncTask extends AsyncTask<Void, String, Either<List<Story>, ServiceException>> 
	{
		private final String queueId;
		private final String storyId;
		private final MergeTaskCompleted callback;

		public MergeAsyncTask(String queueId, String storyId, MergeTaskCompleted callback)
		{
			this.queueId = queueId;
			this.storyId = storyId;
			this.callback = callback;
		}

		@Override
		protected Either<List<Story>, ServiceException> doInBackground(Void... params)
		{
			try
			{
				storyService.merge(queueId, storyId);
			}
			catch (ServiceException e)
			{
				return Either.right(e);
			}

			// Reloading the list seems a bit inefficient, but it works as a first step.
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
		
		@Override
		protected void onPostExecute(Either<List<Story>, ServiceException> result)
		{
			callback.onTaskCompleted(result);
	    }	
	}
}
