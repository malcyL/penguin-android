package uk.co.blackpepper.penguin.android;

import java.util.List;

import uk.co.blackpepper.penguin.R;
import uk.co.blackpepper.penguin.client.Queue;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class QueueAdapter extends ArrayAdapter<Queue>
{
	// fields -----------------------------------------------------------------
    
	private final Context context;

	// constructors -----------------------------------------------------------
	
	public QueueAdapter(Context context, int textViewResourceId)
	{
		super(context, textViewResourceId);
		this.context = context;
	}
	
	// Adapter methods --------------------------------------------------------

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.list_row, parent, false);
		
		Queue queue = getItem(position);
		int pendingCount = queue.getPendingCount(); 

		TextView authorTextView = (TextView) view.findViewById(R.id.title);
		authorTextView.setText(queue.getName());

		TextView subTitleTextView = (TextView) view.findViewById(R.id.sub_title);
		subTitleTextView.setText(
			context.getResources().getString(R.string.queue_list_item_pending) + " " + 
			pendingCount);
		
		int image = R.drawable.user_none;
		switch (pendingCount) 
		{
			case 0:
				image = R.drawable.user_none;
				break;
			case 1:
				image = R.drawable.user_one;
				break;
			case 2:
				image = R.drawable.user_two;
				break;
			case 3:
				image = R.drawable.user_three;
				break;
			default:
				image = R.drawable.user_more;
		}
		
		ImageView imageView = (ImageView) view.findViewById(R.id.thumbnail);
		imageView.setImageResource(image);
		
		return view;
	}
	
	// public methods ---------------------------------------------------------

	public void setData(List<Queue> data)
	{
		clear();
		if (data != null)
		{
			addAll(data);
		}
	}
}
