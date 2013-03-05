package uk.co.blackpepper.penguin.android;

import java.util.List;

import uk.co.blackpepper.penguin.client.Queue;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class QueueListAdapter extends ArrayAdapter<Queue>
{
	public QueueListAdapter(Context context, int textViewResourceId)
	{
		super(context, textViewResourceId);
	}

	public void setData(List<Queue> data)
	{
		clear();
		if (data != null)
		{
			addAll(data);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view = super.getView(position, convertView, parent);
		
		Queue item = getItem(position);
		((TextView) view).setText(item.getName());

		return view;
	}
}
