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
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class StoryListFragment extends ListFragment 
	implements LoaderCallbacks<Either<List<Story>, ServiceException>>, MergeTaskCompleted, Refreshable, RefreshTaskCompleted
{
	// constants --------------------------------------------------------------
	
	public static final String MERGED_KEY = "merged";
	
	private static final String TAG = StoryListFragment.class.getName();
	
	// fields -----------------------------------------------------------------

	private StoryService storyService;

	private StoryAdapter adapter;

	private String queueId;

	private boolean merged;

	private MergeAsyncTask mergeTask; 
	
	private RefreshAsyncTask refreshTask; 
	
	private Refreshable refreshCallback;
	
	// Accessors
	
	public void setRefreshCallback(Refreshable refreshCallback) {
		this.refreshCallback = refreshCallback;
	}
	
	// ListFragment methods ---------------------------------------------------

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (null == savedInstanceState) {
			queueId = getActivity().getIntent().getExtras().getString(QueueListFragment.QUEUE_ID_KEY);
			merged = getArguments().getBoolean(MERGED_KEY);

			storyService = new HttpClientStoryService(new DefaultHttpClient(),
				PreferenceUtils.getServerApiUrl(inflater.getContext()));

			adapter = new StoryAdapter(inflater.getContext(), android.R.layout.simple_list_item_1);
			setListAdapter(adapter);

			getLoaderManager().initLoader(0, null, this);
		}

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
        ListView lv = getListView();
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lv.setMultiChoiceModeListener(new ModeCallback());
	}
	
    @Override
    public void onDestroy() 
    {
        super.onDestroy();
 
        if (mergeTask != null) 
        {
        	mergeTask.cancel(true);
        }
        
        if (refreshTask != null)
        {
        	refreshTask.cancel(true);
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

	public void merge(String queueId, String storyId) 
	{
		mergeTask = new MergeAsyncTask(queueId, storyId, this);
		mergeTask.execute();
	}
	
	@Override
	public void onTaskCompleted(ServiceException exception)
	{
		if (exception != null)
		{
			Toast.makeText(getActivity(), R.string.toast_service_error, Toast.LENGTH_LONG).show();
			Log.e(TAG, "Error merging story for queue: " + queueId, exception);
		}
	}

	private class MergeAsyncTask extends AsyncTask<Void, String, ServiceException> 
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
		protected ServiceException doInBackground(Void... params)
		{
			try
			{
				if (!merged)
				{
					storyService.merge(queueId, storyId);
				} 
				else 
				{
					storyService.unmerge(queueId, storyId);
				}
				return null;
			}
			catch (ServiceException e)
			{
				return e;
			}
		}
		
		@Override
		protected void onPostExecute(ServiceException exception)
		{
			callback.onTaskCompleted(exception);
	    }	
	}
	
	// Refresh Task methods ---------------------------------------------------------------

	@Override
	public void refresh()
	{
		refreshTask = new RefreshAsyncTask(queueId, this);
		refreshTask.execute();
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
			Log.e(TAG, "Error refreshing queue: " + queueId, data.right());
		}
	}
	
	private class RefreshAsyncTask extends AsyncTask<Void, String, Either<List<Story>, ServiceException>> 
	{
		private final String queueId;
		private final RefreshTaskCompleted callback;

		public RefreshAsyncTask(String queueId, RefreshTaskCompleted callback)
		{
			this.queueId = queueId;
			this.callback = callback;
		}

		@Override
		protected Either<List<Story>, ServiceException> doInBackground(Void... params)
		{
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
	
	// Multi Choice Mode Listener ---------------------------------------------------------------
	
    private class ModeCallback implements ListView.MultiChoiceModeListener {

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = getActivity().getMenuInflater();
            if (!merged) 
            {
                inflater.inflate(R.menu.pending_list_select_menu, menu);
                mode.setTitle("Merging");
            }
            else
            {
                inflater.inflate(R.menu.merged_list_select_menu, menu);
                mode.setTitle("Unmerging");
            }
            setSubtitle(mode);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
            case R.id.merge:
				String message = mergeCheckedStories();
                mode.finish();
        		refreshCallback.refresh();
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                break;
            default:
                break;
            }
            return true;
        }

		private String mergeCheckedStories()
		{
			StringBuilder builder = new StringBuilder();
			if (!merged) 
			{
				builder.append("Merged: ");
			} 
			else
			{
				builder.append("Unmerged: ");
			}
			final SparseBooleanArray checkedItems = getListView().getCheckedItemPositions();
			int checkedItemsCount = checkedItems.size();
			for (int i = 0; i < checkedItemsCount; ++i) {
				int position = checkedItems.keyAt(i);
				if(checkedItems.valueAt(i)) {
					Story storyToMerge = adapter.getItem(position); 
					merge(queueId, storyToMerge.getId());
					builder.append(storyToMerge.getReference());
					builder.append(" ");
				}
			}
			return builder.toString();
		}

        public void onDestroyActionMode(ActionMode mode) {
        }

        public void onItemCheckedStateChanged(ActionMode mode,
                int position, long id, boolean checked) {
            setSubtitle(mode);
        }

        private void setSubtitle(ActionMode mode) {
            final int checkedCount = getListView().getCheckedItemCount();
            switch (checkedCount) {
                case 0:
                    mode.setSubtitle(null);
                    break;
                case 1:
                    mode.setSubtitle("One item selected");
                    break;
                default:
                    mode.setSubtitle("" + checkedCount + " items selected");
                    break;
            }
        }
    }
}
