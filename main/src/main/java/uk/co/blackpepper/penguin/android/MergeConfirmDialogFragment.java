package uk.co.blackpepper.penguin.android;

import uk.co.blackpepper.penguin.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class MergeConfirmDialogFragment extends DialogFragment
{
	// constants --------------------------------------------------------------
	
	public static final String QUEUE_ID_KEY = "queueid";
	
	public static final String STORY_ID_KEY = "storyid";
	
	public static final String STORY_REF_KEY = "storyRef";
	
	// DialogFragment methods -------------------------------------------------
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		final String queueId = getArguments().getString(QUEUE_ID_KEY);
		final String storyId = getArguments().getString(STORY_ID_KEY);
		final String storyRef = getArguments().getString(STORY_REF_KEY);

		return new AlertDialog.Builder(getActivity())
			.setTitle(getActivity().getResources().getString(R.string.dialog_merge_story) + " " + storyRef)
			.setPositiveButton(R.string.merge, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// TODO This method of achieving a callback taken from this answer on stack overflow:
					// http://stackoverflow.com/questions/13733304/callback-to-a-fragment-from-a-dialogfragment
					// I didn't want to abuse the onActivityResult method, so just used standard method.
					// This makes the nasty cast necessary. There may be a better way to do this callback.
					StoryListFragment callback = (StoryListFragment)getTargetFragment();
					callback.mergeConfirmed(queueId, storyId);
				}
			})
			.setNegativeButton(R.string.cancel, null)
			.create();
	}
}
