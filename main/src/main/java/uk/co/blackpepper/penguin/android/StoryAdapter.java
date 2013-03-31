package uk.co.blackpepper.penguin.android;

import java.util.List;

import uk.co.blackpepper.penguin.R;
import uk.co.blackpepper.penguin.client.Story;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StoryAdapter extends ArrayAdapter<Story>
{
	// fields -----------------------------------------------------------------
    
    	private final Context context;
    
	// constructors -----------------------------------------------------------
	
	public StoryAdapter(Context context, int textViewResourceId)
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
		View view = inflater.inflate(R.layout.list_row_selectable, parent, false);

		Story story = getItem(position);
		
		TextView authorTextView = (TextView) view.findViewById(R.id.title);
		authorTextView.setText(story.getAuthor());

		TextView subTitleTextView = (TextView) view.findViewById(R.id.sub_title);
		subTitleTextView.setText(story.getTitle());

		TextView referenceTextView = (TextView) view.findViewById(R.id.note);
		referenceTextView.setText(story.getReference());

		if (story.isMerged())
		{
			authorTextView.setPaintFlags(authorTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			subTitleTextView.setPaintFlags(subTitleTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			referenceTextView.setPaintFlags(subTitleTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		}
		else
		{
			authorTextView.setPaintFlags(authorTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
			subTitleTextView.setPaintFlags(subTitleTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
			referenceTextView.setPaintFlags(subTitleTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
		}

		return view;
	}

	// public methods ---------------------------------------------------------

	public void setData(List<Story> data)
	{
		clear();
		if (data != null)
		{
			addAll(data);
		}
	}
}
