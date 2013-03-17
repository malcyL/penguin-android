package uk.co.blackpepper.penguin.android;

import uk.co.blackpepper.penguin.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A fragment showing a list of queues.
 */
public class QueuesFragment extends Fragment
{
	// Fragment methods -------------------------------------------------------
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_queue_list, container, false);
		return rootView;
	}
}
