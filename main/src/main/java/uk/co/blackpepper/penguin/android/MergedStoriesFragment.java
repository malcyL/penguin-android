package uk.co.blackpepper.penguin.android;

import uk.co.blackpepper.penguin.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A fragment holding the list of merged stories.
 */
public class MergedStoriesFragment extends Fragment
{
	// Fragment methods -------------------------------------------------------
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View viewRoot = inflater.inflate(R.layout.fragment_story_list_merged, container, false);
		return viewRoot;
	}
}
