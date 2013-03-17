package uk.co.blackpepper.penguin.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A fragment holding the list of unmerged stories.
 */
public class UnmergedStoriesFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View viewRoot = inflater.inflate(R.layout.fragment_stories_unmerged, container, false);
		return viewRoot;
	}
}
