package uk.co.blackpepper.penguin.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A penguin fragment - A list of where the penguins are coming soon!
 */
public class PenguinsListFragment extends Fragment
{
	// Fragment methods -------------------------------------------------------
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_penguin_list, container, false);
		((TextView) rootView.findViewById(android.R.id.text1)).setText(R.string.coming_soon);
		return rootView;
	}
}