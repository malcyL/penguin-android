package uk.co.blackpepper.penguin.android;

import uk.co.blackpepper.penguin.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class QueueEditDialogFragment extends DialogFragment
{
	// constants --------------------------------------------------------------
	
	public static final String ID = "id";
	
	public static final String NAME = "name";
	
	// DialogFragment methods -------------------------------------------------
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		final String id = getArguments().getString(ID);
		String name = getArguments().getString(NAME);
		
		final EditText view = new EditText(getActivity());
		view.setText(name);
		
		return new AlertDialog.Builder(getActivity())
			.setTitle(R.string.dialog_queue_edit)
			.setView(view)
			.setPositiveButton(R.string.rename, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					renameQueue(id, view.getText().toString());
				}
			})
			.setNegativeButton(R.string.cancel, null)
			.create();
	}
	
	// private methods --------------------------------------------------------
	
	private static void renameQueue(String id, String newName)
	{
		// TODO: rename queue
		Log.i(QueueEditDialogFragment.class.getName(), "TODO: rename queue " + id + " to " + newName);
	}
}
